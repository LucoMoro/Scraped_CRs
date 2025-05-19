//<Beginning of snippet n. 0>
case KeyEvent.KEYCODE_2:
case KeyEvent.KEYCODE_8:
    if (!mAlphaKeySet && !mDisplayWorldFlat) {
        if (isInitialized && mGLView != null) {
            int zoomAmount = (keyCode == KeyEvent.KEYCODE_2) ? -2 : 2;
            mGLView.zoom(zoomAmount);
        } else if (!isInitialized) {
            Toast.makeText(context, "Zoom actions not yet initialized", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "GLView is not initialized", Toast.LENGTH_SHORT).show();
        }
        handled = true;
    }
    break;
//<End of snippet n. 0>