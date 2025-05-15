//<Beginning of snippet n. 0>

if (ei != null && ei.inputType != InputType.TYPE_NULL) {
    mExtractAccessories.setVisibility(View.VISIBLE);
    if (mExtractAction != null) {
        if (ei.actionLabel != null) {
            mExtractAction.setText(ei.actionLabel);
        } else {
            mExtractAction.setText(getTextForImeAction(ei.imeOptions));
        }
        mExtractAction.setOnClickListener(mActionClickListener);
    }
} else {
    if (mExtractAction != null) {
        mExtractAccessories.setVisibility(View.GONE);
        mExtractAction.setOnClickListener(null);
    }
}

//<End of snippet n. 0>