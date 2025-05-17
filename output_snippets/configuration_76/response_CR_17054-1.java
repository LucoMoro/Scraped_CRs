//<Beginning of snippet n. 0>
boolean isInitialized = false;

// Initialization method
void initializeGLView() {
    mGLView = new GLView();
    isInitialized = true;
}

// The '2' key zooms out
case KeyEvent.KEYCODE_2:
            if (!mAlphaKeySet && !mDisplayWorldFlat && isInitialized) {
                mGLView.zoom(-2);
                handled = true;
            } else if (!isInitialized) {
                // Log or notify user about the failure to zoom
            }

// The '8' key zooms in
case KeyEvent.KEYCODE_8:
            if (!mAlphaKeySet && !mDisplayWorldFlat && isInitialized) {
                mGLView.zoom(2);
                handled = true;
            } else if (!isInitialized) {
                // Log or notify user about the failure to zoom
            }
//<End of snippet n. 0>