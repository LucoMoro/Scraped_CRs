//<Beginning of snippet n. 0>
if (mButtonOperatorSelectionExpand != null) {
    if (mPrefScreen != null) {
        log("[CSP] Disabling Operator Selection menu.");
        mButtonOperatorSelectionExpand.setEnabled(false);
    } else {
        log("[CSP] No preference screen available. Buttons will remain enabled.");
        mButtonOperatorSelectionExpand.setEnabled(true);
    }
} else {
    log("[CSP] Button for Operator Selection is null. Cannot change state.");
}
//<End of snippet n. 0>