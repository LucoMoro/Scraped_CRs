/*Fix the patch for TextureViewSnapshotTest

The patch of Change-Id:I39d4f593was not good enough.
It will make all android.textureview.cts.TextureViewTest test cases
bypassed as (mShouldRender == null).

Change-Id:Ib3d0a7311c4791207e5ec2cd864d49017b8cc9bb*/
//Synthetic comment -- diff --git a/tests/tests/textureview/src/android/textureview/cts/GLProducerThread.java b/tests/tests/textureview/src/android/textureview/cts/GLProducerThread.java
old mode 100644
new mode 100755
//Synthetic comment -- index 0029020..1fad03f

//Synthetic comment -- @@ -139,7 +139,8 @@
mFrameStats = new FrameStats();

mRenderer.init(width[0], height[0]);
        while (!mRenderer.isFinished() && (mShouldRender != null && mShouldRender.get())) {
mFrameStats.startFrame();
mRenderer.renderFrame();
mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);







