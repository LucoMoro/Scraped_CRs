//<Beginning of snippet n. 0>
if (mExtractAction != null) {
    if (ei != null && ei.inputType != InputType.TYPE_NULL) {
        mExtractAccessories.setVisibility(View.VISIBLE);
        if (ei.actionLabel != null) {
            mExtractAction.setText(ei.actionLabel);
        } else {
            mExtractAction.setText(getTextForImeAction(ei.imeOptions));
        }
        mExtractAction.setOnClickListener(mActionClickListener);
    } else {
        mExtractAccessories.setVisibility(View.GONE);
        mExtractAction.setOnClickListener(null);
        if (ei == null) {
            Log.w("ExtractAction", "ExtractedInfo ei is null");
        } else {
            Log.w("ExtractAction", "InputType is null");
        }
    }
} else {
    mExtractAccessories.setVisibility(View.GONE);
    Log.w("ExtractAction", "mExtractAction is null");
}
//<End of snippet n. 0>