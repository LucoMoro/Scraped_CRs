/*Move waitUntilNext60HzTick after eglSwapBuffers

After frame rendering the renderer waits for 60Hz tick. The loop should
wait last. There should NOT be any more 'work' done after the sync to
the next frame slot.

Change-Id:Ie8da8602d82706adb88e6947d904ee947db7a10e*/
//Synthetic comment -- diff --git a/tests/tests/textureview/src/android/textureview/cts/GLProducerThread.java b/tests/tests/textureview/src/android/textureview/cts/GLProducerThread.java
old mode 100755
new mode 100644
//Synthetic comment -- index 3b992f3..0e3a2b2

//Synthetic comment -- @@ -22,6 +22,7 @@
import java.lang.Thread;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.Semaphore;

import junit.framework.Assert;

//Synthetic comment -- @@ -63,6 +64,19 @@
this(surfaceTexture, renderer, null, semaphore);
}

private void initGL() {
mEgl = (EGL10) EGLContext.getEGL();

//Synthetic comment -- @@ -143,6 +157,7 @@
mFrameStats.startFrame();
mRenderer.renderFrame();
mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);
mFrameStats.endFrame();
}









//Synthetic comment -- diff --git a/tests/tests/textureview/src/android/textureview/cts/GLTextureUploadRenderer.java b/tests/tests/textureview/src/android/textureview/cts/GLTextureUploadRenderer.java
//Synthetic comment -- index 6d86540..9a0e5b9 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import java.nio.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;
import android.util.Log;
import android.graphics.SurfaceTexture;
import android.view.Surface;
//Synthetic comment -- @@ -225,20 +224,6 @@
return mFramesRendered >= mFramesToRender;
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


public int textureIndex(int tileX, int tileY) {
return (tileX + tileY * mTileCountX) % mTextures.length;
}
//Synthetic comment -- @@ -291,7 +276,6 @@

mScrollOffset += 16;
mFramesRendered++;
        WaitUntilNext60HzTick();
}

void GLCHK() {







