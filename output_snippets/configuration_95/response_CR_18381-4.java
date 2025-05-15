//<Beginning of snippet n. 0>

setTypeface(tf, styleIndex);
}

/**
* Sets the typeface and style in which the text should be displayed,
* and turns on the fake bold and italic bits in the Paint if the
}

@Override 
public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
    if (!isEnabled()) {
        return null; // Return null for disabled TextViews to prevent keyboard from appearing
    }
    
    if (onCheckIsTextEditor()) {
        if (mInputMethodState == null) {
            mInputMethodState = new InputMethodState();
        }
        return super.onCreateInputConnection(outAttrs); // Ensure call to parent method
    }

    if ((mMovement != null || onCheckIsTextEditor()) && mText instanceof Spannable && mLayout != null) {
        boolean handled = false;
    }
    return null; // Added a return for default case
}

@Override 
protected void onFocusChanged(boolean hasFocus, boolean gainFocus) {
    super.onFocusChanged(hasFocus, gainFocus);
    if (!isEnabled()) {
        // Prevent focus handling if not enabled
        clearFocus(); 
    }
}

//<End of snippet n. 0>