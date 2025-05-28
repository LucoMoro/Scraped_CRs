//<Beginning of snippet n. 0>

case KeyEvent.KEYCODE_2:
case KeyEvent.KEYCODE_8:
    if (!mAlphaKeySet && !mDisplayWorldFlat) {
        if (mGLView != null && isInitialized()) {
            if (keyCode == KeyEvent.KEYCODE_2) {
                mGLView.zoom(-2);
            } else {
                mGLView.zoom(2);
            }
            handled = true;
        } else {
            Log.w("Zoom", "Zoom operation attempted before initialization: " + (mGLView == null ? "mGLView is null" : "application not initialized"));
        }
    }
    break;

//<End of snippet n. 0>