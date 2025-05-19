//<Beginning of snippet n. 0>
@Override public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
    if (!isEnabled()) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0); // Ensure keyboard closure
        }
        return null; // Prevent input connection if disabled
    }
    if (onCheckIsTextEditor()) {
        if (mInputMethodState == null) {
            mInputMethodState = new InputMethodState();
        }
        return super.onCreateInputConnection(outAttrs);
    }
    return null;
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
    if (hasFocus()) {
        clearFocus(); // Prevent focus gain
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