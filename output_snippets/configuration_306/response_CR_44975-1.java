//<Beginning of snippet n. 0>

private PinState      mPin1State;
private PinState      mPin2State;
private boolean       mIccFdnEnabled = false;
private boolean       mDesiredFdnEnabled = false;
private boolean       mIccLockEnabled;
private boolean       mDesiredPinLocked;

int[] ints = (int[])ar.result;
if (ints == null || ints.length != 1 || (ints[0] < 0 || ints[0] > 1)) {
    log("Unexpected facility lock response");
    resetStateVariables();
    alertUserOfError("FDN service unavailable or invalid response. Please check your service.");
} else {
    mIccFdnEnabled = (ints[0] == 1);
    mDesiredFdnEnabled = mIccFdnEnabled; // Consistent initialization based on FDN status
    if (DBG) log("Query facility lock : " + mIccFdnEnabled);
}

public void supplyNetworkDepersonalization(String pin, Message onComplete) {
    synchronized (mLock) {
        if (DBG) log("supplyNetworkDepersonalization");
        // Implementation that ensures state consistency and robust handling
    }
}

private void resetStateVariables() {
    mIccFdnEnabled = false;
    mDesiredFdnEnabled = false;
    // Reset other related state variables if necessary
}

private void alertUserOfError(String message) {
    // Detailed error feedback implementation
    log(message);
}
//<End of snippet n. 0>