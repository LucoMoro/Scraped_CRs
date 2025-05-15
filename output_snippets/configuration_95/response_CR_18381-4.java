//<Beginning of snippet n. 0>
setTypeface(tf, styleIndex);
}

/**
* Sets the typeface and style in which the text should be displayed,
* and turns on the fake bold and italic bits in the Paint if the
*/

@Override
public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
    if (!isEnabled()) {
        return null;  // Prevent editing when disabled
    }
    if (onCheckIsTextEditor()) {
        if (mInputMethodState == null) {
            mInputMethodState = new InputMethodState();
        }
        return super.onCreateInputConnection(outAttrs);
    }

    if ((mMovement != null || onCheckIsTextEditor()) && mText instanceof Spannable && mLayout != null) {
        boolean handled = false;
        // Additional handling logic...
    }
    return null; // Return null if not handled
}

@Override
public boolean onTouchEvent(MotionEvent event) {
    if (!isEnabled()) {
        return false;  // Intercept touch events when disabled
    }
    return super.onTouchEvent(event);
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (!isEnabled()) {
        return true;  // Block focus and editing when disabled
    }
    return super.onKeyDown(keyCode, event);
}

@Override
public boolean isFocusableInTouchMode() {
    return isEnabled() && super.isFocusableInTouchMode();
}

//<End of snippet n. 0>