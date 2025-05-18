//<Beginning of snippet n. 0>
if (mPrefScreen == null) {
    log("[CSP] Disabling Operator Selection menu due to null mPrefScreen.");
} 
log("[CSP] Disabling Operator Selection menu.");
mButtonOperatorSelectionExpand.setEnabled(mPrefScreen != null);
//<End of snippet n. 0>