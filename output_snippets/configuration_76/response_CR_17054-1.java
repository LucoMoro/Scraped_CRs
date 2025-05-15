//<Beginning of snippet n. 0>


// The '2' key zooms out
case KeyEvent.KEYCODE_2:
    if (mGLView != null && !mAlphaKeySet && !mDisplayWorldFlat) {
        mGLView.zoom(-2);
        handled = true;
    }
    return; // Early return if mGLView is not initialized

// The '8' key zooms in
case KeyEvent.KEYCODE_8:
    if (mGLView != null && !mAlphaKeySet && !mDisplayWorldFlat) {
        mGLView.zoom(2);
        handled = true;
    }
    return; // Early return if mGLView is not initialized

//<End of snippet n. 0>