//<Beginning of snippet n. 0>
protected void onEnableNewApn() {
    // Check if there is an active connection before cleaning up
    if (isConnected()) {
        cleanUpConnection(true, Phone.REASON_APN_SWITCHED);
    }
}

protected void onTrySetupData(String reason) {
    // Check if the current connection is valid before setting up new data
    if (isConnected()) {
        // Existing setup code goes here
    }
}
//<End of snippet n. 0>