
//<Beginning of snippet n. 0>

old mode 100755
new mode 100644

import java.lang.Thread;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.LockSupport;

import junit.framework.Assert;

this(surfaceTexture, renderer, null, semaphore);
}

    private long waitUntilNext60HzTick() {
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

private void initGL() {
mEgl = (EGL10) EGLContext.getEGL();

mFrameStats.startFrame();
mRenderer.renderFrame();
mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);
            if (mRenderer instanceof GLTextureUploadRenderer) waitUntilNext60HzTick();
mFrameStats.endFrame();
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import java.nio.*;
import java.util.Arrays;
import java.util.Random;
import android.util.Log;
import android.graphics.SurfaceTexture;
import android.view.Surface;
return mFramesRendered >= mFramesToRender;
}

public int textureIndex(int tileX, int tileY) {
return (tileX + tileY * mTileCountX) % mTextures.length;
}

mScrollOffset += 16;
mFramesRendered++;
}

void GLCHK() {

//<End of snippet n. 1>








