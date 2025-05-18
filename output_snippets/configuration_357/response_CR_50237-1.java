//<Beginning of snippet n. 0>


case EVENT_AUTO_SELECT_DONE:
    if (DBG) log("hideProgressPanel");

    handleDialogDismissal(DIALOG_NETWORK_AUTO_SELECT);
    handleDialogDismissal(DIALOG_NETWORK_LIST_LOAD);
    
    getPreferenceScreen().setEnabled(true);

@Override
protected void onResume() {
    super.onResume();
    dismissDialog(DIALOG_NETWORK_AUTO_SELECT);
    dismissDialog(DIALOG_NETWORK_LIST_LOAD);
}

private void handleDialogDismissal(int dialogId) {
    if (isNetworkOperationComplete()) {
        dismissDialog(dialogId);
    } else {
        Toast.makeText(this, "Cannot dismiss dialog, operation in progress", Toast.LENGTH_SHORT).show();
    }
}

//<End of snippet n. 0>