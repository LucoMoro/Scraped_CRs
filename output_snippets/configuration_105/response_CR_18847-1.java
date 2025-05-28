//<Beginning of snippet n. 0>

/** {@inheritDoc} */
public void cleanUp() {
        // dismiss the dialog.
if (mSimUnlockProgressDialog != null) {
            mSimUnlockProgressDialog.dismiss();
}
mUpdateMonitor.removeCallback(this);
}

//<End of snippet n. 0>