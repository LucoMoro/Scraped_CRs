//<Beginning of snippet n. 0>

private PinState      mPin1State;
private PinState      mPin2State;
private boolean       mIccFdnEnabled;
private boolean       mDesiredFdnEnabled;
private boolean       mIccLockEnabled;
private boolean       mDesiredPinLocked;

if (ar.result instanceof int[] && ((int[]) ar.result).length > 0) {
    int[] ints = (int[]) ar.result;
    if (ints[0] == 0 || ints[0] == 1) {
        mIccFdnEnabled = (0 != ints[0]);
        if (DBG) log("Query facility lock : " + mIccFdnEnabled);
        notifyFdnStatusChanged(mIccFdnEnabled);
    } else {
        loge("Invalid value in facility lock response");
        handleInvalidResponse();
    }
} else {
    loge("Bogus facility lock response");
    handleInvalidResponse();
}

public void supplyNetworkDepersonalization(String pin, Message onComplete) {
    synchronized (mLock) {
        if (DBG) log("supplyNetworkDepersonalization");
        // Additional implementation logic could go here
    }
}

private void notifyFdnStatusChanged(boolean newStatus) {
    // Notify all relevant components of the FDN status change
    log("FDN status changed to: " + (newStatus ? "enabled" : "disabled"));
}

private void handleInvalidResponse() {
    mIccFdnEnabled = false; // Fallback to a known state
    mDesiredFdnEnabled = false; // Reset desired state on invalid response
    notifyFdnStatusChanged(mIccFdnEnabled);
}

//<End of snippet n. 0>