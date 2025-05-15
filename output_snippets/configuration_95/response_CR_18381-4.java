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
        return null; // Return null if the TextView is disabled
    }
    
    if (onCheckIsTextEditor()) {
        if (mInputMethodState == null) {
            mInputMethodState = new InputMethodState();
        }
        return super.onCreateInputConnection(outAttrs);
    }

    if ((mMovement != null || onCheckIsTextEditor()) && mText instanceof Spannable && mLayout != null) {
        boolean handled = false;
        // Additional logic can go here if needed
    }

    return null; // Return null if none of the conditions are met
}

@Override
public boolean onCheckIsTextEditor() {
    return isEnabled() && super.onCheckIsTextEditor(); // Return false if the TextView is disabled
}

@Override
protected void onFocusChanged(boolean hasFocus, boolean gainFocus) {
    super.onFocusChanged(hasFocus, gainFocus);
    if (!isEnabled() && gainFocus) {
        clearFocus(); // Prevents the TextView from gaining focus if disabled
    }
}

//<End of snippet n. 0>