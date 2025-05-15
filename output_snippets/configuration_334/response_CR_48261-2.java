<<Beginning of snippet n. 0>>
if (isBackupTransportValid()) {
    mBackup.setChecked(enable);
    mAutoRestore.setEnabled(enable);
    mConfigure.setEnabled(enable);
} else {
    mBackup.setChecked(false);
    mAutoRestore.setEnabled(false);
    mConfigure.setEnabled(false);
    showUserFeedback("No valid backup transport is configured.");
}
<<End of snippet n. 0>>