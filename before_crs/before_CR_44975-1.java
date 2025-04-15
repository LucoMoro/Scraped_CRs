/*Telephony: Fix to support FDN available or unavailable status

During the initialization, RIL reads the UST or SST file to verify
whether FDN service is available and based on the status FDN will
be enabled or disabled. This fix will provide the FDN service
information by reading RIL_REQUEST_QUERY_FACILITY_LOCK response.

Change-Id:I733bb9a618ff5595b62f0df219f88a7d58d2d9ef*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
//Synthetic comment -- index 2718af6..fc99316 100644

//Synthetic comment -- @@ -58,6 +58,8 @@
private PinState      mPin1State;
private PinState      mPin2State;
private boolean       mIccFdnEnabled;
private boolean       mDesiredFdnEnabled;
private boolean       mIccLockEnabled;
private boolean       mDesiredPinLocked;
//Synthetic comment -- @@ -213,8 +215,15 @@

int[] ints = (int[])ar.result;
if(ints.length != 0) {
                mIccFdnEnabled = (0!=ints[0]);
                if (DBG) log("Query facility lock : "  + mIccFdnEnabled);
} else {
loge("Bogus facility lock response");
}
//Synthetic comment -- @@ -555,6 +564,15 @@
}
}

public void supplyNetworkDepersonalization (String pin, Message onComplete) {
synchronized (mLock) {
if (DBG) log("supplyNetworkDepersonalization");







