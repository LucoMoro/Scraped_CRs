//<Beginning of snippet n. 0>


private PinState      mPin1State;
private PinState      mPin2State;
private boolean       mIccFdnEnabled = false; // Initialize to avoid null references
private boolean       mDesiredFdnEnabled;
private boolean       mIccLockEnabled;
private boolean       mDesiredPinLocked;

int[] ints = (int[])ar.result;
if (ints != null && ints.length != 0) {
    switch (ints[0]) {
        case 0:
            mIccFdnEnabled = false; 
            break;
        case 1:
            mIccFdnEnabled = true;
            break;
        default:
            loge("Invalid FDN state received: " + ints[0]);
            mIccFdnEnabled = false; // Default to false if not applicable
            break;
    }
    if (DBG) log("Query facility lock : " + mIccFdnEnabled);
} else {
    loge("Bogus facility lock response - result array is null or empty");
    // Additional error handling can be added here
}

public void supplyNetworkDepersonalization(String pin, Message onComplete) {
    synchronized (mLock) {
        if (DBG) log("supplyNetworkDepersonalization");
        // Logic to handle supplyNetworkDepersonalization
    }
}

//<End of snippet n. 0>