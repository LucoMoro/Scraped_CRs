//<Beginning of snippet n. 0>

if (DBG) {
    log("applyNewState(" + apnContext.getApnType() + ", " + enabled + "(" + apnContext.isEnabled() + "), " + met + "(" + apnContext.getDependencyMet() +"))");
}

if (!apnContext.isConnected()) {
    log("APN context is disconnected. Cannot proceed with the operation.");
    return;
}

if (apnContext.isReady()) {
    if (enabled && met) return;
    if (!enabled) {
        apnContext.setReason(Phone.REASON_DATA_DISABLED);
        resetApnContextIfNeeded();
    }
}

if (apnContext.getRetryCount() >= MAX_RETRY_COUNT) {
    log("Reached maximum retry count for APN: " + apnContext.getApnType() + ". Checking if it is a retriable error.");
    
    if (lastErrorCode == 400 || lastErrorCode == 500 || lastErrorCode == 503 || lastErrorCode == 504 || lastErrorCode == 408 || lastErrorCode == 429) {
        log("Detected retriable error code: " + lastErrorCode + ". Allowing retry instead of transitioning to FAILED state.");
        apnContext.incrementRetryCount();
        attemptReconnection();
        return;
    }

    log("Transitioning to FAILED state.");
    apnContext.setState(ApnContext.State.FAILED);
    log("APN context transitioned to FAILED state. Reason: " + apnContext.getReason());
    handleFailedState();
} else {
    log("Retrying... Attempt " + (apnContext.getRetryCount() + 1));
    apnContext.incrementRetryCount();
    attemptReconnection();
}

// Check disconnected state after retries
if (!apnContext.isConnected()) {
    log("APN context is still disconnected after retry attempts. Further action needed.");
}

//<End of snippet n. 0>