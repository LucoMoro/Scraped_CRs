//<Beginning of snippet n. 0>

private PinState      mPin1State;
private PinState      mPin2State;
private boolean       mIccFdnEnabled;
private boolean       mDesiredFdnEnabled;
private boolean       mIccLockEnabled;
private boolean       mDesiredPinLocked;

int[] ints = (int[]) ar.result;
if (ints != null && ints.length != 0) {
    mIccFdnEnabled = (0 != ints[0]);
    if (DBG) log("Query facility lock : " + mIccFdnEnabled);
} else {
    loge("Bogus facility lock response");
}

if (mIccFdnEnabled != mDesiredFdnEnabled) {
    mDesiredFdnEnabled = mIccFdnEnabled;
    notifyFdnAvailabilityChanged(mIccFdnEnabled); // Notify other components
}

public void supplyNetworkDepersonalization(String pin, Message onComplete) {
    synchronized (mLock) {
        if (DBG) log("supplyNetworkDepersonalization");
        if (!mIccFdnEnabled) {
            // Perform depersonalization as FDN is not enabled
            // actual depersonalization logic here...
        } else {
            // Handle case when FDN is enabled
            loge("Cannot depersonalize while FDN is enabled");
            if (onComplete != null) {
                onComplete.sendToTarget(); // Notify completion
            }
        }
    }
}

//<End of snippet n. 0>