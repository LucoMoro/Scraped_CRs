
//<Beginning of snippet n. 0>



/** {@inheritDoc} */
public void cleanUp() {
        // hide the dialog.
if (mSimUnlockProgressDialog != null) {
            mSimUnlockProgressDialog.hide();
}
mUpdateMonitor.removeCallback(this);
}

//<End of snippet n. 0>








