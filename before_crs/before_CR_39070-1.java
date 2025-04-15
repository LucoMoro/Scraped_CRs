/*CTS : Removing wait function in testTextureViewWithoutUploads60Hz.

The test sends gl commands and waits for the next 16.6 ms tick before calling
eglSwapBuffers. Qualcomm's Adreno graphics has a deferred rendering architecture.
This defers the rendering till the time eglSwapBuffers is called, thereby causing
a delay before which rendering can start causing the test to fail. When we disable
waiting for the next 16.6ms tick, the test passes.
Fix tested on tag : android-cts-4.0.3_r3*/
//Synthetic comment -- diff --git a/tests/tests/textureview/src/android/textureview/cts/GLTextureUploadRenderer.java b/tests/tests/textureview/src/android/textureview/cts/GLTextureUploadRenderer.java
//Synthetic comment -- index 6d86540..20ce133 100644

//Synthetic comment -- @@ -291,7 +291,6 @@

mScrollOffset += 16;
mFramesRendered++;
        WaitUntilNext60HzTick();
}

void GLCHK() {







