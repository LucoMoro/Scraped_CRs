/*Telephony: Update the state of FDN Service correctly

FDN Service state was derived based on the state of PIN2. Update the
state of FDN service based on the FACILTY_LOCK messages instead.

Telephony: Change the file path to FDN,MSISDN for USIM and CSIM
Correcting the file path to EF_FDN and EF_MSISDN from Telecom DF to ADF
for USIM and CSIM.

Telephony: Correcting the file path to EF_ADN and EF_MSISDN
This fix corrects the file path to EF_ADN and EF_MSISDN.

Change-Id:I099698d0031f980c3883c17f3c170fbcadd7fb84*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccFileHandler.java b/src/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 98ab17b..010699b 100644

//Synthetic comment -- @@ -541,8 +541,6 @@
protected String getCommonIccEFPath(int efid) {
switch(efid) {
case EF_ADN:
case EF_SDN:
case EF_EXT1:
case EF_EXT2:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
//Synthetic comment -- index 2718af6..d18a08c 100644

//Synthetic comment -- @@ -61,6 +61,9 @@
private boolean       mDesiredFdnEnabled;
private boolean       mIccLockEnabled;
private boolean       mDesiredPinLocked;
    private boolean       mIccFdnAvailable = true; // Default is enabled.
    private int mPin1RetryCount = -1;
    private int mPin2RetryCount = -1;

private CommandsInterface mCi;
private Context mContext;
//Synthetic comment -- @@ -213,8 +216,16 @@

int[] ints = (int[])ar.result;
if(ints.length != 0) {
                //0 - Available & Disabled, 1-Available & Enabled, 2-Unavailable.
                if (ints[0] == 2) {
                    mIccFdnEnabled = false;
                    mIccFdnAvailable = false;
                } else {
                    mIccFdnEnabled = (ints[0] == 1) ? true : false;
                    mIccFdnAvailable = true;
                }
                log("Query facility FDN : FDN service available: "+ mIccFdnAvailable
                        +" enabled: "  + mIccFdnEnabled);
} else {
loge("Bogus facility lock response");
}
//Synthetic comment -- @@ -595,6 +606,29 @@
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
     * @return No. of Attempts remaining to unlock PIN1/PUK1
     */
     public int getIccPin1RetryCount() {
         return mPin1RetryCount;
     }

     /**
      * @return No. of Attempts remaining to unlock PIN2/PUK2
     */
     public int getIccPin2RetryCount() {
         return mPin2RetryCount;
     }

    /**
* Set the ICC pin lock enabled or disabled
* When the operation is complete, onComplete will be sent to its handler
*








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java b/src/java/com/android/internal/telephony/cdma/RuimFileHandler.java
//Synthetic comment -- index 4c271f9..92df2fd 100644

//Synthetic comment -- @@ -61,6 +61,9 @@
case EF_CSIM_CDMAHOME:
case EF_CSIM_EPRL:
return MF_SIM + DF_CDMA;
        case EF_FDN:
        case EF_MSISDN:
            return MF_SIM + DF_TELECOM;
}
return getCommonIccEFPath(efid);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index 0387a70..3974a29 100644

//Synthetic comment -- @@ -45,6 +45,8 @@
// TODO(): DF_GSM can be 7F20 or 7F21 to handle backward compatibility.
// Implement this after discussion with OEMs.
switch(efid) {
        case EF_FDN:
        case EF_MSISDN:
case EF_SMS:
return MF_SIM + DF_TELECOM;








