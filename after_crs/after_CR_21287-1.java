/*Fix OpenGlEsVersionTest

Bug 3307156

Put the EGL calls between eglInitialize and eglTerminate.

Change-Id:I29e117e29d2f945a6bf97555c81280c78f093863*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/opengl/cts/OpenGlEsVersionTest.java b/tests/tests/graphics/src/android/opengl/cts/OpenGlEsVersionTest.java
//Synthetic comment -- index f1acd87..3ebc567 100644

//Synthetic comment -- @@ -83,33 +83,42 @@
EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
int[] numConfigs = new int[1];

        if (egl.eglInitialize(display, null)) {
            try {
                if (egl.eglGetConfigs(display, null, 0, numConfigs)) {
                    EGLConfig[] configs = new EGLConfig[numConfigs[0]];
                    if (egl.eglGetConfigs(display, configs, numConfigs[0], numConfigs)) {
                        int[] value = new int[1];
                        for (int i = 0; i < numConfigs[0]; i++) {
                            if (egl.eglGetConfigAttrib(display, configs[i],
                                    EGL10.EGL_RENDERABLE_TYPE, value)) {
                                if ((value[0] & EGL_OPENGL_ES2_BIT) == EGL_OPENGL_ES2_BIT) {
                                    return 2;
                                }
                            } else {
                                Log.w(TAG, "Getting config attribute with "
                                        + "EGL10#eglGetConfigAttrib failed "
                                        + "(" + i + "/" + numConfigs[0] + "): "
                                        + egl.eglGetError());
                            }
}
                        return 1;
} else {
                        Log.e(TAG, "Getting configs with EGL10#eglGetConfigs failed: "
+ egl.eglGetError());
                        return -1;
}
                } else {
                    Log.e(TAG, "Getting number of configs with EGL10#eglGetConfigs failed: "
                            + egl.eglGetError());
                    return -2;
}
              } finally {
                  egl.eglTerminate(display);
              }
} else {
            Log.e(TAG, "Couldn't initialize EGL.");
            return -3;
}
}








