//<Beginning of snippet n. 0>
if (DBG) {
    log("applyNewState(" + apnContext.getApnType() + ", " + enabled +
        "("+ apnContext.isEnabled() + "), " + met + "(" +
        apnContext.getDependencyMet() +"))");
}
if (apnContext.isReady()) {
    if (enabled && met) return;
    if (!enabled) {
        apnContext.setReason(Phone.REASON_DATA_DISABLED);
    } else {
        if (apnContext.isDisconnected()) {
            apnContext.setApnState(ApnContext.FAILED);
            log("APN state set to FAILED due to disconnection.");
        } else if (apnContext.getRetryCount() >= MAX_RETRY_COUNT) {
            if (apnContext.isDisconnected()) {
                apnContext.setApnState(ApnContext.FAILED);
                log("APN state set to FAILED after max retries with disconnection.");
            } else {
                apnContext.incrementRetryCount(); // Prepare for a retry
                log("Retrying APN connection. Current attempt: " + apnContext.getRetryCount());
            }
        }
    }
}
//<End of snippet n. 0>