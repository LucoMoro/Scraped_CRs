<<Beginning of snippet n. 0>>
boolean isValidBackupTransport() {
    Transport currentTransport = getCurrentTransport();
    
    if (currentTransport == null || !currentTransport.isConfigured()) {
        return false; // Transport must be valid and configured
    }

    // Enhanced validation for various types of transports
    if (currentTransport instanceof LocalTransport && !isDebugMode()) {
        return false; // LocalTransport is invalid outside of debug mode
    }
    
    // Add other specific transport validations if needed
    return true;
}

void updateBackupOptions() {
    boolean isTransportValid = isValidBackupTransport();
    
    mBackup.setChecked(isTransportValid);
    mAutoRestore.setEnabled(isTransportValid);
    mConfigure.setEnabled(isTransportValid);
    
    if (!isTransportValid) {
        String errorMsg = "Backup options are unavailable due to invalid transport configuration: " + getCurrentTransport().getErrorDescription();
        showToast(errorMsg);
    }
}
<<End of snippet n. 0>>