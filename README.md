To be improved:

- Create a decent layout for text on screen (includes improving bounding boxes for touch events)
- rewrite everything which takes care of calculations and drawing, dataview.draw is a big bottleneck (allocates a lot and is heavy on cpu). Should be able to gain a lot of speed.
- Adding a lot of markers might cause rendering to hang for a short amount of time, something to be fixed with point 2.
- The webview doesnt work for panoramio and OSM (sometimes). Not caused by 3d tho ;-).

Anything else:

- When resuming Mixare the Surface3D does not appear on my One X, while it does on my TF101..
- Firmware bug on my TF101 caused by glTexParameteriv. Does not happen on my One X. See following trace:

I/DEBUG   (  112): *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***
I/DEBUG   (  112): Build fingerprint: 'asus/US_epad/EeePad:4.0.3/IML74K/US_epad-9.2.1.11-20120221:user/release-keys'
I/DEBUG   (  112): pid: 9749, tid: 9888, name: Thread-259  >>> org.mixare <<<
I/DEBUG   (  112): signal 11 (SIGSEGV), code 1 (SEGV_MAPERR), fault addr 006e0075
I/DEBUG   (  112):     r0 006e0061  r1 00000de1  r2 59939008  r3 00000000
I/DEBUG   (  112):     r4 419218e0  r5 00000de1  r6 00008b9d  r7 00000000
I/DEBUG   (  112):     r8 419218e0  r9 5be89e54  sl 00008b9d  fp 5df05c94
I/DEBUG   (  112):     ip 00000000  sp 5df05c48  lr 5bf65a3c  pc 5be01286  cpsr 60030030
I/DEBUG   (  112):     d0  3f7f80003f19999a  d1  3f8000003f800000
I/DEBUG   (  112):     d2  3f19999a3f800000  d3  bf7fffffbf7fffff
I/DEBUG   (  112):     d4  0000000000000000  d5  0000000000000000
I/DEBUG   (  112):     d6  3f80000000000000  d7  3f19999a3f800000
I/DEBUG   (  112):     d8  42200000ffffffbd  d9  4230000042200000
I/DEBUG   (  112):     d10 4158000041b00000  d11 c2700000c0000000
I/DEBUG   (  112):     d12 41e80000c2a40000  d13 3f800000c2180000
I/DEBUG   (  112):     d14 40745a0f00000000  d15 3d71979941d97b08
I/DEBUG   (  112):     scr 80000011
I/DEBUG   (  112): 
I/DEBUG   (  112): backtrace:
I/DEBUG   (  112):     #00  pc 0000a286  /system/lib/egl/libGLESv1_CM_tegra.so (glTexParameteriv+73)
I/DEBUG   (  112):     #01  pc 0003dfbc  /system/lib/libandroid_runtime.so
I/DEBUG   (  112):     #02  pc 0001e0b0  /system/lib/libdvm.so (dvmPlatformInvoke+112)
I/DEBUG   (  112):     #03  pc 0004d24b  /system/lib/libdvm.so (dvmCallJNIMethod(unsigned int const*, JValue*, Method const*, Thread*)+394)
I/DEBUG   (  112):     #04  pc 000009e0  /dev/ashmem/dalvik-jit-code-cache (deleted)
