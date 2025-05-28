
//<Beginning of snippet n. 0>


private PinState      mPin1State;
private PinState      mPin2State;
private boolean       mIccFdnEnabled;
private boolean       mDesiredFdnEnabled;
private boolean       mIccLockEnabled;
private boolean       mDesiredPinLocked;

int[] ints = (int[])ar.result;
if(ints.length != 0) {
                mIccFdnEnabled = (0!=ints[0]);
                if (DBG) log("Query facility lock : "  + mIccFdnEnabled);
} else {
loge("Bogus facility lock response");
}
}
}

public void supplyNetworkDepersonalization (String pin, Message onComplete) {
synchronized (mLock) {
if (DBG) log("supplyNetworkDepersonalization");

//<End of snippet n. 0>








