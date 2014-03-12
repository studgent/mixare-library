package org.mixare.lib.model3d.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mixare.lib.gui.GLParameters;
import org.mixare.lib.model3d.Mesh;
import org.mixare.lib.model3d.ModelLoadException;
import org.mixare.lib.model3d.Vertex;

import static org.mixare.lib.model3d.Vertex.X;
import static org.mixare.lib.model3d.Vertex.Y;
import static org.mixare.lib.model3d.Vertex.Z;

import android.content.Context;
import android.util.Log;

public class ObjReader extends ModelReader {
	private static final Pattern FACE_ELEMENT_PATTERN = Pattern
			.compile("(\\d+)(?:/(\\d*))?(?:/(\\d*))?");

	
	public ObjReader(Context context) {
		super(context);
	}

	public Mesh readMesh(InputStream is) throws ModelLoadException {
		String line = null;
		int lineNumber = 0;
		Mesh mesh = new Mesh();
		List<Triangle> triangles = new ArrayList<Triangle>();
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Vertex> normals = new ArrayList<Vertex>();
		Vertex min = mesh.min = new Vertex();
		Vertex max = mesh.max = new Vertex();

		try {
			Reader r = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(r, BUFFER_SIZE);

			boolean backFaces = GLParameters.ENABLEBACKFACES;
			while ((line = br.readLine()) != null) {
				if (line.endsWith("\\")) {
					line = line.substring(0, line.length()-1);
					line += br.readLine();
				}

				lineNumber++;
				line = line.trim();
				// skip blank lines
				if (line.length() == 0) {
					continue;
				}
				// skip comments, and things we do not support
				// TODO: handle "s" command
				if (line.startsWith("#") || line.startsWith("o")
						|| line.startsWith("s") || line.startsWith("g")
						|| line.startsWith("mtllib")
						|| line.startsWith("usemtl") || line.startsWith("vt")) {
					continue;
				}
				
				
				// parse normal
				if (line.startsWith("vn")) {
					StringTokenizer tok = new StringTokenizer(line);
					tok.nextToken();
					Vertex n = new Vertex();
					n.vertex[X] = Float.parseFloat(tok.nextToken());
					n.vertex[Y] = Float.parseFloat(tok.nextToken());
					if (tok.hasMoreTokens()) {
						n.vertex[Z] = Float.parseFloat(tok.nextToken());
					}
					normals.add(n);
					Log.d("modelview", "added normal: " + n);
					continue;
				}
				// parse vertex
				if (line.startsWith("v")) {
					StringTokenizer tok = new StringTokenizer(line);
					tok.nextToken();
					Vertex v = new Vertex();
					v.vertex[X] = Float.parseFloat(tok.nextToken());
					v.vertex[Y] = Float.parseFloat(tok.nextToken());
					if (tok.hasMoreTokens()) {
						v.vertex[Z] = Float.parseFloat(tok.nextToken());
					}
					vertices.add(v);

					// check for mesh.max, mesh.min
					if (v.vertex[X] > max.vertex[X]) {
						max.vertex[X] = v.vertex[X];
					} else if (v.vertex[X] < min.vertex[X]) {
						min.vertex[X] = v.vertex[X];
					}
					if (v.vertex[Y] > max.vertex[Y]) {
						max.vertex[Y] = v.vertex[Y];
					} else if (v.vertex[Y] < min.vertex[Y]) {
						min.vertex[Y] = v.vertex[Y];
					}
					if (v.vertex[Z] > max.vertex[Z]) {
						max.vertex[Z] = v.vertex[Z];
					} else if (v.vertex[Z] < min.vertex[Z]) {
						min.vertex[Z] = v.vertex[Z];
					}

					continue;
				}
				// parse face
				if (line.startsWith("f")) {
					Matcher m = FACE_ELEMENT_PATTERN.matcher(line);
					List<Vertex> triVertices = new ArrayList<Vertex>();
					List<Vertex> triNormals = new ArrayList<Vertex>();

					while (m.find()) {
						int vertexPtr = Integer.parseInt(m.group(1));
						triVertices.add(vertices.get(vertexPtr - 1));
						if (m.group(3) != null) {
							int normalPtr = Integer.parseInt(m.group(3));
							triNormals.add(normals.get(normalPtr - 1));
						}
					}
					for (int i = 1; i < triVertices.size() - 1; i++) {
						Vertex v1 = triVertices.get(0);
						Vertex v2 = triVertices.get(i);
						Vertex v3 = triVertices.get(i + 1);

						// TODO: we potentially have normals for each
						// vertex, but AFAIK, a normal is per-face
						// we just pick the single normal for the first
						// vertice here
						Vertex normal = null;
						if (triNormals.size() > 0) {
							normal = triNormals.get(0);
						}
						Triangle t = new Triangle(v1, v2, v3, normal);
						
						triangles.add(t);
						if (backFaces) {
							triangles.add(t.reverse());
						}						
					}
				}
			}
			if (triangles.size() == 0) {
				throw new IllegalArgumentException("no triangles read");
			}

			mesh.setTriangles(triangles);

			return mesh;
		} catch (Throwable t) {
			mesh = null;
			triangles = null;
			vertices = null;
			normals = null;

			ModelLoadException mle = new ModelLoadException(t);
			mle.setLineNumber(lineNumber);
			mle.setLine(line);
			throw mle;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}

	}
}