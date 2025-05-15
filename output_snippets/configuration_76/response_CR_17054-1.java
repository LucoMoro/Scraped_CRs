//<Beginning of snippet n. 0>

boolean isInitialized = false; // Initialize the flag

// The '2' key zooms out
case KeyEvent.KEYCODE_2:
            if (!mAlphaKeySet && !mDisplayWorldFlat) {
                if (mGLView != null) {
                    mGLView.zoom(-2);
                } else {
                    Log.e("ZoomError", "Attempted to zoom out before initialization.");
                }
                handled = true;
            }

// The '8' key zooms in
case KeyEvent.KEYCODE_8:
            if (!mAlphaKeySet && !mDisplayWorldFlat) {
                if (mGLView != null) {
                    mGLView.zoom(2);
                } else {
                    Log.e("ZoomError", "Attempted to zoom in before initialization.");
                }
                handled = true;
            }

//<End of snippet n. 0>