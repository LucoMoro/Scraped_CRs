
//<Beginning of snippet n. 0>


case EVENT_AUTO_SELECT_DONE:
if (DBG) log("hideProgressPanel");

                    // Always try to dismiss the dialog because activity may
                    // be moved to background after dialog is shown.
                    try {
dismissDialog(DIALOG_NETWORK_AUTO_SELECT);
                    } catch (IllegalArgumentException e) {
                        // "auto select" is always trigged in foreground, so "auto select" dialog
                        //  should be shown when "auto select" is trigged. Should NOT get
                        // this exception, and Log it.
                        Log.w(LOG_TAG, "[NetworksList] Fail to dismiss auto select dialog", e);
}
getPreferenceScreen().setEnabled(true);

// update the state of the preferences.
if (DBG) log("hideProgressPanel");


        // Always try to dismiss the dialog because activity may
        // be moved to background after dialog is shown.
        try {
dismissDialog(DIALOG_NETWORK_LIST_LOAD);
        } catch (IllegalArgumentException e) {
            // It's not a error in following scenario, we just ignore it.
            // "Load list" dialog will not show, if NetworkQueryService is
            // connected after this activity is moved to background.
            if (DBG) log("Fail to dismiss network load list dialog");
}

getPreferenceScreen().setEnabled(true);

//<End of snippet n. 0>








