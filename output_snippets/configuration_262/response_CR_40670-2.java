//<Beginning of snippet n. 0>

if (DBG) {
    log("applyNewState(" + apnContext.getApnType() + ", " + enabled +
    "(" + apnContext.isEnabled() + "), " + met + "(" +
                        apnContext.getDependencyMet() +"))");
}
if (apnContext.isReady()) {
    if (enabled && met) return;
    if (!enabled) {
        apnContext.setReason(Phone.REASON_DATA_DISABLED);
        return;
    }
    
    // Implement Reconnection Strategy
    if (apnContext.getState() == ApnContext.State.DISCONNECTED) {
        log("Attempting reconnection strategy for DISCONNECTED state");
        for (int retryCount = 0; retryCount < MAX_RETRY_ATTEMPTS; retryCount++) {
            if (attemptConnection()) {
                apnContext.setState(ApnContext.State.CONNECTED);
                log("APN state set to CONNECTED after retry " + (retryCount + 1));
                return;
            } else if (isBadRequestError()) {
                log("Received 400 Bad Request, retrying...");
                continue; // Allow further attempts on bad request
            }
            log("Retry attempt " + (retryCount + 1) + " failed.");
        }
    }

    // Final state set after MAX_RETRY_ATTEMPTS
    apnContext.setState(ApnContext.State.FAILED);
    log("APN state permanently set to FAILED after max retries");
}

//<End of snippet n. 0>