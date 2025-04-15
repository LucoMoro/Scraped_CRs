/*proposed fix for issue #1703:http://code.google.com/p/android/issues/detail?id=1703*/
//Synthetic comment -- diff --git a/core/java/android/view/SurfaceView.java b/core/java/android/view/SurfaceView.java
//Synthetic comment -- index 0d9e221..b54db48 100644

//Synthetic comment -- @@ -30,8 +30,9 @@
import android.util.AttributeSet;
import android.util.Config;
import android.util.Log;
import java.util.ArrayList;

import java.util.concurrent.locks.ReentrantLock;

/**
//Synthetic comment -- @@ -308,7 +309,7 @@
mLayout.memoryType = mRequestedType;

if (mWindow == null) {
                    mWindow = new MyWindow();
mLayout.type = WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA;
mLayout.gravity = Gravity.LEFT|Gravity.TOP;
mSession.add(mWindow, mLayout,
//Synthetic comment -- @@ -393,32 +394,48 @@
updateWindow(false);
}

    private class MyWindow extends IWindow.Stub {
public void resized(int w, int h, Rect coveredInsets,
Rect visibleInsets, boolean reportDraw) {
            if (localLOGV) Log.v(
                "SurfaceView", SurfaceView.this + " got resized: w=" +
                w + " h=" + h + ", cur w=" + mCurWidth + " h=" + mCurHeight);
            synchronized (this) {
                if (mCurWidth != w || mCurHeight != h) {
                    mCurWidth = w;
                    mCurHeight = h;
                }
                if (reportDraw) {
                    try {
                        mSession.finishDrawing(mWindow);
                    } catch (RemoteException e) {
}
}
}
}

public void dispatchKey(KeyEvent event) {
            //Log.w("SurfaceView", "Unexpected key event in surface: " + event);
            if (mSession != null && mSurface != null) {
                try {
                    mSession.finishKey(mWindow);
                } catch (RemoteException ex) {
}
}
}
//Synthetic comment -- @@ -448,8 +465,12 @@
}

public void dispatchGetNewSurface() {
            Message msg = mHandler.obtainMessage(GET_NEW_SURFACE_MSG);
            mHandler.sendMessage(msg);
}

public void windowFocusChanged(boolean hasFocus, boolean touchEnabled) {







