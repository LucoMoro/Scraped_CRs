//<Beginning of snippet n. 0>
setTypeface(tf, styleIndex);
}

/**
* Sets the typeface and style in which the text should be displayed,
* and turns on the fake bold and italic bits in the Paint if the
*/

@Override 
public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
    if (isEnabled()) {
        if (onCheckIsTextEditor()) {
            if (mInputMethodState == null) {
                mInputMethodState = new InputMethodState();
            }
            return super.onCreateInputConnection(outAttrs);
        }
    } else {
        // Close the on-screen keyboard explicitly when the TextView is disabled
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        outAttrs.flags &= ~EditorInfo.IME_FLAG_FORCE_ASCII_DECIMAL; // Prevent focus change
        outAttrs.flags |= EditorInfo.IME_FLAG_NO_EXTRACT_UI; // Additional flag to prevent focus gain
        return null; 
    }

    if ((mMovement != null || onCheckIsTextEditor()) && mText instanceof Spannable && mLayout != null) {
        boolean handled = false;
    }
    return null; // Return null if conditions don't match
}

@Override
public boolean onKeyPreIme(int keyCode, KeyEvent event) {
    if (!isEnabled()) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                clearFocus();
                return true; // Prevent actions from happening if disabled
            }
            return true; // Prevent direct editing via directional keys
        }
    }
    return super.onKeyPreIme(keyCode, event);
}
//<End of snippet n. 0>