//<Beginning of snippet n. 0>
protected void onEnableNewApn() {
    if (!isConnectionActive()) {
        cleanUpConnection(true, Phone.REASON_APN_SWITCHED);
    }
}

protected void onTrySetupData(String reason) {
    if (!isConnectionValid()) {
        return;
    }
    // Additional logic for setting up data goes here
}
//<End of snippet n. 0>