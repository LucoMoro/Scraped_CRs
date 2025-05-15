//<Beginning of snippet n. 0>
if (isValidBackupTransport()) {
    mBackup.setChecked(true);
    mAutoRestore.setEnabled(true);
    mConfigure.setEnabled(true);
} else {
    mBackup.setChecked(false);
    mAutoRestore.setEnabled(false);
    mConfigure.setEnabled(false);
    showFeedbackMessage("No valid backup transport available.");
}

@Override
//<End of snippet n. 0>