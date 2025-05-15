
//<Beginning of snippet n. 0>


private PinState      mPin1State;
private PinState      mPin2State;
private boolean       mIccFdnEnabled;
    private boolean mIccFdnAvailable = true; // Default is enabled.
                                             // Will be updated when SIM_READY.
private boolean       mDesiredFdnEnabled;
private boolean       mIccLockEnabled;
private boolean       mDesiredPinLocked;

int[] ints = (int[])ar.result;
if(ints.length != 0) {
                if (ints[0] != 2) {
                    mIccFdnEnabled = (0!=ints[0]);
                    mIccFdnAvailable = true;
                } else {
                   if(DBG) log("Query facility lock: FDN Service Unavailable!");
                   mIccFdnAvailable = false;
                   mIccFdnEnabled = false;
                }
                if (DBG) log("Query facility lock for FDN: "  + mIccFdnEnabled);
} else {
loge("Bogus facility lock response");
}
}
}

    /**
     * Check whether fdn (fixed dialing number) service is available.
     * @return true if ICC fdn service available
     *         false if ICC fdn service not available
     */
    public boolean getIccFdnAvailable() {
        return mIccFdnAvailable;
    }

public void supplyNetworkDepersonalization (String pin, Message onComplete) {
synchronized (mLock) {
if (DBG) log("supplyNetworkDepersonalization");

//<End of snippet n. 0>








