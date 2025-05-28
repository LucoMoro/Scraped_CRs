
//<Beginning of snippet n. 0>


case EVENT_AUTO_SELECT_DONE:
if (DBG) log("hideProgressPanel");

                    if (mIsForeground) {
dismissDialog(DIALOG_NETWORK_AUTO_SELECT);
}
getPreferenceScreen().setEnabled(true);

// update the state of the preferences.
if (DBG) log("hideProgressPanel");

        if (mIsForeground) {
dismissDialog(DIALOG_NETWORK_LIST_LOAD);
}

getPreferenceScreen().setEnabled(true);

//<End of snippet n. 0>








