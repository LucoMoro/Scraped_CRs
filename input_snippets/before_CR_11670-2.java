
//<Beginning of snippet n. 0>


ei.inputType != InputType.TYPE_NULL);
if (hasAction) {
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
}
}


//<End of snippet n. 0>








