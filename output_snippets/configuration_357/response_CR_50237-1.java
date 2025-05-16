//<Beginning of snippet n. 0>

case EVENT_AUTO_SELECT_DONE:
    if (DBG) log("hideProgressPanel");

    if (isDialogVisible(DIALOG_NETWORK_AUTO_SELECT)) {
        dismissDialog(DIALOG_NETWORK_AUTO_SELECT);
    }

    if (isDialogVisible(DIALOG_NETWORK_LIST_LOAD)) {
        dismissDialog(DIALOG_NETWORK_LIST_LOAD);
    }

    getPreferenceScreen().setEnabled(true);

//<End of snippet n. 0>