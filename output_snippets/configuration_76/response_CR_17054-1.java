//<Beginning of snippet n. 0>

case KeyEvent.KEYCODE_2:
            if (!mAlphaKeySet && !mDisplayWorldFlat) {
                if (mGLView != null) {
                    mGLView.zoom(-2);
                } else {
                    Log.w("ZoomOperation", "Attempted to zoom out before mGLView was initialized.");
                    // Provide user feedback if needed
                }
                handled = true;
            }

case KeyEvent.KEYCODE_8:
            if (!mAlphaKeySet && !mDisplayWorldFlat) {
                if (mGLView != null) {
                    mGLView.zoom(2);
                } else {
                    Log.w("ZoomOperation", "Attempted to zoom in before mGLView was initialized.");
                    // Provide user feedback if needed
                }
                handled = true;
            }

//<End of snippet n. 0>