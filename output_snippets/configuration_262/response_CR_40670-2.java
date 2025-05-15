//<Beginning of snippet n. 0>

if (DBG) {
    log("applyNewState(" + apnContext.getApnType() + ", " + enabled +
    "(" + apnContext.isEnabled() + "), " + met + "(" +
    apnContext.getDependencyMet() +"))");
}

if (apnContext.isReady() && !apnContext.getState().equals("DISCONNECTED") && !apnContext.getState().equals("FAILED")) {
    if (enabled && met) return;
    if (!enabled) {
        apnContext.setReason(Phone.REASON_DATA_DISABLED);
    }
} else if (apnContext.getState().equals("DISCONNECTED") || apnContext.getState().equals("FAILED")) {
    // Implement reconnection attempts logic or state reset when in DISCONNECTED or FAILED state.
    apnContext.resetState(); // Pseudocode for resetting state
}

//<End of snippet n. 0>