//<Beginning of snippet n. 0>
if (DBG) {
    log("applyNewState(" + apnContext.getApnType() + ", " + enabled +
    " (" + apnContext.isEnabled() + "), " + met + "(" +
    apnContext.getDependencyMet() + "))");
}
if (apnContext.isDisconnected()) {
    apnContext.incrementRetryCount(); // Track retry attempts
    if (apnContext.getRetryCount() < MAX_RETRIES) {
        // Log failed connection attempt
        log("Retrying connection, attempt: " + apnContext.getRetryCount());
        return;
    } else {
        apnContext.setState(APNState.READY); // Allow new connection attempts
        apnContext.resetRetryCount(); // Reset counter after reaching max retries
    }
}
if (apnContext.isReady()) {
    if (enabled && met) return;
    if (!enabled) {
        apnContext.setReason(Phone.REASON_DATA_DISABLED);
        apnContext.setState(APNState.DISCONNECTED);
    }
    // Expanded error handling for various status codes
    int errorCode = apnContext.getLastErrorCode();
    if (errorCode == 400 || errorCode == 404 || errorCode == 500) {
        apnContext.setState(APNState.RETRYING);
        // Initiate reconnection logic here
    }
}
//<End of snippet n. 0>