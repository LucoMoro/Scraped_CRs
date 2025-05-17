//<Beginning of snippet n. 0>
setTypeface(tf, styleIndex);
}

/**
* Sets the typeface and style in which the text should be displayed,
* and turns on the fake bold and italic bits in the Paint if the
*/

@Override public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
    if (isEnabled()) {
        if (onCheckIsTextEditor()) {
            if (mInputMethodState == null) {
                mInputMethodState = new InputMethodState();
            }
            return super.onCreateInputConnection(outAttrs);
        }
    }
    return null;
}

@Override
public boolean onTouchEvent(MotionEvent event) {
    if (!isEnabled()) {
        // Close the keyboard logic 
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        return true; // Consume the touch event
    }
    return super.onTouchEvent(event);
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (!isEnabled()) {
        return true; // Prevent interaction
    }
    return super.onKeyDown(keyCode, event);
}

@Override
public boolean onKeyUp(int keyCode, KeyEvent event) {
    if (!isEnabled()) {
        return true; // Prevent interaction
    }
    return super.onKeyUp(keyCode, event);
}
//<End of snippet n. 0>