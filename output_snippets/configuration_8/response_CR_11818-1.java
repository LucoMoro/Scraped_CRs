<<Beginning of snippet n. 0>>
protected void onEnableNewApn() {
    if (!isConnectionNecessary() && !hasOngoingMmsTransactions()) {
        cleanUpConnection(true, Phone.REASON_APN_SWITCHED);
    }
}

protected void onTrySetupData(String reason) {
    synchronized (this) {
        if (isConnectionActive()) {
            // Presumably the logic to set up data will go here
        }
    }
}

private boolean isConnectionActive() {
    return checkCurrentConnectionStatus() && !hasOngoingMmsTransactions(); 
}

private boolean isConnectionNecessary() {
    return determineIfConnectionIsNeeded(); 
}

private boolean hasOngoingMmsTransactions() {
    // Implement logic to check for ongoing MMS transactions
    return false; // Placeholder return, should be updated with actual checking logic
}
<<End of snippet n. 0>>