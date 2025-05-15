
//<Beginning of snippet n. 0>


setTypeface(tf, styleIndex);
}

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }

        if (!enabled) {
            // Hide the soft input if the currently active TextView is disabled
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (imm.isActive(this)) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
        super.setEnabled(enabled);
    }

/**
* Sets the typeface and style in which the text should be displayed,
* and turns on the fake bold and italic bits in the Paint if the
}

@Override public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        if (onCheckIsTextEditor() && isEnabled()) {
if (mInputMethodState == null) {
mInputMethodState = new InputMethodState();
}
return superResult;
}

        if ((mMovement != null || onCheckIsTextEditor()) && isEnabled()
                && mText instanceof Spannable && mLayout != null) {

boolean handled = false;


//<End of snippet n. 0>








