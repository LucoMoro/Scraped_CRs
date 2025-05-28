//<Beginning of snippet n. 0>


case EVENT_AUTO_SELECT_DONE:
    if (DBG) log("hideProgressPanel");

    if (isNetworkOperationComplete()) {
        dismissDialog(DIALOG_NETWORK_AUTO_SELECT);
    } else {
        Toast.makeText(this, "Cannot dismiss auto-select dialog, operation in progress", Toast.LENGTH_SHORT).show();
    }
    
    // Update the state of the preferences.
    if (DBG) log("hideProgressPanel");

    if (isNetworkOperationComplete()) {
        dismissDialog(DIALOG_NETWORK_LIST_LOAD);
    } else {
        Toast.makeText(this, "Cannot dismiss network list dialog, operation in progress", Toast.LENGTH_SHORT).show();
    }
    
    getPreferenceScreen().setEnabled(true);

//<End of snippet n. 0>