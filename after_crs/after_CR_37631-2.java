/*Fix the condition of while loop for TextureViewSnapshotTest

GLProducerThread#run() are expected to finish within 750 ms.
mShouldRender.get() returns false by being called
TextureViewSnapshotTestActivity#onSurfaceTextureUpdated().
But mRenderer.isFinished() doesn't return true unless
the while loop repeats more than Integer.MAX_VALUE times.
So the while loop cannot finish within 750ms.

Change-Id:I3e4c790855bf68c34b122093ff6aae8a87cf7bbf*/




//Synthetic comment -- diff --git a/tests/tests/textureview/src/android/textureview/cts/GLProducerThread.java b/tests/tests/textureview/src/android/textureview/cts/GLProducerThread.java
old mode 100644
new mode 100755
//Synthetic comment -- index 50ea451..1fad03f

//Synthetic comment -- @@ -139,7 +139,8 @@
mFrameStats = new FrameStats();

mRenderer.init(width[0], height[0]);
        while (!mRenderer.isFinished()
                && (mShouldRender != null ? mShouldRender.get() : true)) {
mFrameStats.startFrame();
mRenderer.renderFrame();
mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);







