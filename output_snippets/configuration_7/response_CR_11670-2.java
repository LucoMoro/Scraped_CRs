//<Beginning of snippet n. 0>
if (ei != null && ei.inputType != InputType.TYPE_NULL) {
    if (mExtractAction != null) {
        mExtractAccessories.setVisibility(View.VISIBLE);
        if (ei.actionLabel != null) {
            mExtractAction.setText(ei.actionLabel);
        } else {
            mExtractAction.setText(getTextForImeAction(ei.imeOptions));
        }
        mExtractAction.setOnClickListener(mActionClickListener);
    } else {
        Log.e("ExtractAction", "mExtractAction is null");
        mExtractAccessories.setVisibility(View.GONE);
    }
} else {
    mExtractAccessories.setVisibility(View.GONE);
    if (mExtractAction != null) {
        mExtractAction.setOnClickListener(null);
    } else {
        Log.e("ExtractAction", "mExtractAction is null");
    }
}
//<End of snippet n. 0>