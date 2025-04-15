/*Telephony: Fix to support FDN available or unavailable status

During the initialization, RIL reads the UST or SST file to verify
whether FDN service is available and based on the status FDN will
be enabled or disabled. This fix will provide the FDN service
information by reading RIL_REQUEST_QUERY_FACILITY_LOCK response.

Change-Id:I295e72e4bad22d61d551bc6c96800b9dca47b331*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCard.java b/telephony/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 955849d..eb4f836 100644

//Synthetic comment -- @@ -60,7 +60,8 @@
private boolean mIccPinLocked = true; // Default to locked
private boolean mIccFdnEnabled = false; // Default to disabled.
// Will be updated when SIM_READY.
    private boolean mIccFdnAvailable = true; // Default is enabled.
                                             // Will be updated when SIM_READY.

/* The extra data for broacasting intent INTENT_ICC_STATE_CHANGE */
static public final String INTENT_KEY_ICC_STATE = "ss";
//Synthetic comment -- @@ -273,6 +274,15 @@
}

/**
     * Check whether fdn (fixed dialing number) service is available.
     * @return true if ICC fdn service available
     *         false if ICC fdn service not available
     */
    public boolean getIccFdnAvailable() {
        return mIccFdnAvailable;
    }

    /**
* Check whether ICC pin lock is enabled
* This is a sync call which returns the cached pin enabled state
*
//Synthetic comment -- @@ -525,8 +535,15 @@

int[] ints = (int[])ar.result;
if(ints.length != 0) {
            if (ints[0] != 2) {
                mIccFdnEnabled = (0!=ints[0]);
                mIccFdnAvailable = true;
            } else {
                if(mDbg) log("Query facility lock: FDN Service Unavailable!");
                mIccFdnAvailable = false;
                mIccFdnEnabled = false;
            }
        if(mDbg) log("Query facility lock for FDN : "  + mIccFdnEnabled);
} else {
Log.e(mLogTag, "[IccCard] Bogus facility lock response");
}







