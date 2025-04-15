/*Synchronize to 60Hz tick at begining of render frame

Currently, test case is measuring the time from start of frame rendering till
SwapBuffer ends, during this time measurement, it is including the wait time
for the 60Hz tick, which is not appropriate.
If the test case intention is to limt 60fps, then it would be more appropriate
to synchronize to 60Hz tick at the beginning of renderframe instead of doing
in the middle.

Fix tested on tag : android-cts-4.0.3_r3*/




//Synthetic comment -- diff --git a/tests/tests/textureview/src/android/textureview/cts/GLProducerThread.java b/tests/tests/textureview/src/android/textureview/cts/GLProducerThread.java
//Synthetic comment -- index 3b992f3..180d1855 100755

//Synthetic comment -- @@ -31,6 +31,7 @@
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import java.util.concurrent.locks.LockSupport;

import static android.opengl.GLES20.*;

//Synthetic comment -- @@ -127,6 +128,19 @@
mEglSurface = EGL10.EGL_NO_SURFACE;
}

    public long WaitUntilNext60HzTick() {
	// Wait until the next 60Hz tick.
	long tickTime = 16666666; //60Hz tick
	long startTime = System.nanoTime();
	long endTime = startTime + (tickTime - (startTime % tickTime));
	long currentTime = startTime;
	while (currentTime < endTime) {
	LockSupport.parkNanos(endTime - currentTime);
	currentTime = System.nanoTime();
	}
	return currentTime;
    }

@Override
public void run() {
initGL();
//Synthetic comment -- @@ -140,6 +154,7 @@

mRenderer.init(width[0], height[0]);
while (!mRenderer.isFinished() && (mShouldRender == null || mShouldRender.get())) {
	    WaitUntilNext60HzTick();
mFrameStats.startFrame();
mRenderer.renderFrame();
mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);








//Synthetic comment -- diff --git a/tests/tests/textureview/src/android/textureview/cts/GLTextureUploadRenderer.java b/tests/tests/textureview/src/android/textureview/cts/GLTextureUploadRenderer.java
//Synthetic comment -- index 6d86540..9a0e5b9 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import java.nio.*;
import java.util.Arrays;
import java.util.Random;
import android.util.Log;
import android.graphics.SurfaceTexture;
import android.view.Surface;
//Synthetic comment -- @@ -225,20 +224,6 @@
return mFramesRendered >= mFramesToRender;
}

public int textureIndex(int tileX, int tileY) {
return (tileX + tileY * mTileCountX) % mTextures.length;
}
//Synthetic comment -- @@ -291,7 +276,6 @@

mScrollOffset += 16;
mFramesRendered++;
}

void GLCHK() {







