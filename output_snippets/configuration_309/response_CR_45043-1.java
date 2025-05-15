//<Beginning of snippet n. 0>
if (mPrefScreen.findPreference(BUTTON_OPERATOR_SELECTION_EXPAND_KEY) != null) {
    mButtonOperatorSelectionExpand.setEnabled(false);
    log("[CSP] Disabling Operator Selection menu.");
} else {
    mButtonOperatorSelectionExpand.setEnabled(true);
}
//<End of snippet n. 0>