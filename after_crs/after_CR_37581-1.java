/*Fix the condition of while loop for TextureViewSnapshotTest

GLProducerThread#run() are expected to finish within 750 ms.
mShouldRender.get() returns false by being called
TextureViewSnapshotTestActivity#onSurfaceTextureUpdated().
But mRenderer.isFinished() doesn't return true unless
the while loop repeats more than Integer.MAX_VALUE times.
So the while loop cannot finish within 750ms.*/




//Synthetic comment -- diff --git a/tests/tests/textureview/src/android/textureview/cts/GLProducerThread.java b/tests/tests/textureview/src/android/textureview/cts/GLProducerThread.java
//Synthetic comment -- index 50ea451..0029020 100644

//Synthetic comment -- @@ -139,7 +139,7 @@
mFrameStats = new FrameStats();

mRenderer.init(width[0], height[0]);
        while (!mRenderer.isFinished() && (mShouldRender != null && mShouldRender.get())) {
mFrameStats.startFrame();
mRenderer.renderFrame();
mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);







