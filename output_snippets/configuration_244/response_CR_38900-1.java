//<Beginning of snippet n. 0>

old mode 100755
new mode 100644

import java.lang.Thread;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.Semaphore;

import junit.framework.Assert;

this(surfaceTexture, renderer, null, semaphore);
}

private void initGL() {
    mEgl = (EGL10) EGLContext.getEGL();

    mFrameStats.startFrame();
    mRenderer.renderFrame();
    mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);
    mFrameStats.endFrame();
    WaitUntilNext60HzTick();
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

import java.nio.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;
import android.util.Log;
import android.graphics.SurfaceTexture;
import android.view.Surface;

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

mScrollOffset += 16;
mFramesRendered++;

// Removed WaitUntilNext60HzTick(); from here

void GLCHK() {

//<End of snippet n. 1>