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
        return null;
    }

    if (onCheckIsTextEditor()) {
        if (mInputMethodState == null) {
            mInputMethodState = new InputMethodState();
        }
        return super.onCreateInputConnection(outAttrs);
    }

    if ((mMovement != null || onCheckIsTextEditor()) && mText instanceof Spannable && mLayout != null) {
        boolean handled = false;
    }
    return super.onCreateInputConnection(outAttrs);
}

@Override 
public boolean onTouchEvent(MotionEvent event) {
    if (!isEnabled()) {
        return false;
    }
    return super.onTouchEvent(event);
}

@Override
protected void onFocusChanged(boolean hasFocus, boolean rejig) {
    super.onFocusChanged(hasFocus, rejig);
    if (!isEnabled() && hasFocus) {
        clearFocus();
    }
    if (!isEnabled()) {
        setFocusable(false);
    }
}
//<End of snippet n. 0>