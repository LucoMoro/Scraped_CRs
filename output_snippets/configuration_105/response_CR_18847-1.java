//<Beginning of snippet n. 0>


/** {@inheritDoc} */
public void cleanUp() {
        // dismiss the dialog.
if (mSimUnlockProgressDialog != null) {
            mSimUnlockProgressDialog.dismiss();
            mSimUnlockProgressDialog = null; // Clear reference to prevent memory leaks.
}
mUpdateMonitor.removeCallback(this);
}

//<End of snippet n. 0>