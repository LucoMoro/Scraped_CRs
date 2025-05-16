//<Beginning of snippet n. 0>
protected void onEnableNewApn() {
    if (isConnectionActive()) {
        cleanUpConnection(true, Phone.REASON_APN_SWITCHED);
    }
}

protected void onTrySetupData(String reason) {
    if (isConnectionActive()) {
        cleanUpConnection(false, Phone.REASON_APN_TRY_SETUP);
    }
}

private boolean isConnectionActive() {
    // Implement logic to check if a connection is active
    return false; // Replace with actual check
}
//<End of snippet n. 0>