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
    return activeMmsTransactionsCount() > 0; // Updated logic to check ongoing MMS transactions
}

private int activeMmsTransactionsCount() {
    // Logic to count or check for active MMS transactions should be implemented here
    return 0; // Placeholder, replace with actual counting logic
}
<<End of snippet n. 0>>