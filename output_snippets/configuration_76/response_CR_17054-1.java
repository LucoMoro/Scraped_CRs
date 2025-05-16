//<Beginning of snippet n. 0>
case KeyEvent.KEYCODE_2:
    if (mGLView != null && !mAlphaKeySet && !mDisplayWorldFlat) {
        mGLView.zoom(-2);
        handled = true;
    } else {
        Log.w("ZoomAction", "Attempted to zoom out but mGLView is uninitialized.");
    }

case KeyEvent.KEYCODE_8:
    if (mGLView != null && !mAlphaKeySet && !mDisplayWorldFlat) {
        mGLView.zoom(2);
        handled = true;
    } else {
        Log.w("ZoomAction", "Attempted to zoom in but mGLView is uninitialized.");
    }
//<End of snippet n. 0>