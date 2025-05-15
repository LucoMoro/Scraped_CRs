//<Beginning of snippet n. 0>

if (ei.inputType != InputType.TYPE_NULL) {
    if (hasAction) {
        mExtractAccessories.setVisibility(View.VISIBLE);
        if (ei != null) {
            if (ei.actionLabel != null) {
                if (mExtractAction != null) {
                    mExtractAction.setText(ei.actionLabel);
                }
            } else {
                if (mExtractAction != null) {
                    mExtractAction.setText(getTextForImeAction(ei.imeOptions));
                }
            }
            if (mExtractAction != null) {
                mExtractAction.setOnClickListener(mActionClickListener);
            }
        }
    } else {
        mExtractAccessories.setVisibility(View.GONE);
        if (mExtractAction != null) {
            mExtractAction.setOnClickListener(null);
        }
    }
}

//<End of snippet n. 0>