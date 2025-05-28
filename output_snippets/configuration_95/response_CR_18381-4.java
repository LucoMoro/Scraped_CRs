//<Beginning of snippet n. 0>
@Override public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
    if (isEnabled()) {
        if (onCheckIsTextEditor()) {
            if (mInputMethodState == null) {
                mInputMethodState = new InputMethodState();
            }
            return super.onCreateInputConnection(outAttrs);
        }
        return null; // Prevent input connection if disabled
    }
    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
        imm.hideSoftInputFromWindow(getWindowToken(), 0); // Ensure keyboard closure
    }
    return null; // Prevent input connection if disabled
}

@Override public boolean onTouchEvent(MotionEvent event) {
    if (!isEnabled()) {
        clearFocus(); // Prevent focus gain when disabled
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0); // Ensure keyboard closure
        }
        return true; // Prevent editing
    }
    return super.onTouchEvent(event);
}

@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (!isEnabled()) {
        return true; // Prevent edits
    }
    if ((keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN || 
         keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && hasFocus()) {
        clearFocus(); // Prevent focus gain through directional keys
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0); // Close keyboard if necessary
        }
        return true; // Prevent editing
    }
    return super.onKeyDown(keyCode, event);
}

@Override public void onFocusChanged(boolean hasFocus, boolean oldFocus) {
    super.onFocusChanged(hasFocus, oldFocus);
    if (!isEnabled() && hasFocus) {
        clearFocus(); // Prevent focus on disabled state
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0); // Close keyboard
        }
    }
}
//<End of snippet n. 0>