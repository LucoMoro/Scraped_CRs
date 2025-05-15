
//<Beginning of snippet n. 0>



// The '2' key zooms out
case KeyEvent.KEYCODE_2:
            if (!mAlphaKeySet && !mDisplayWorldFlat) {
mGLView.zoom(-2);
handled = true;
}

// The '8' key zooms in
case KeyEvent.KEYCODE_8:
            if (!mAlphaKeySet && !mDisplayWorldFlat) {
mGLView.zoom(2);
handled = true;
}

//<End of snippet n. 0>








