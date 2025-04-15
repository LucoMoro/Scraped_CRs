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
        case EF_FDN:
        case EF_MSISDN:
case EF_SDN:
case EF_EXT1:
case EF_EXT2:








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
//Synthetic comment -- index 2718af6..d18a08c 100644

//Synthetic comment -- @@ -61,6 +61,9 @@
private boolean       mDesiredFdnEnabled;
private boolean       mIccLockEnabled;
private boolean       mDesiredPinLocked;

private CommandsInterface mCi;
private Context mContext;
//Synthetic comment -- @@ -213,8 +216,16 @@

int[] ints = (int[])ar.result;
if(ints.length != 0) {
                mIccFdnEnabled = (0!=ints[0]);
                if (DBG) log("Query facility lock : "  + mIccFdnEnabled);
} else {
loge("Bogus facility lock response");
}
//Synthetic comment -- @@ -595,6 +606,29 @@
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
}
return getCommonIccEFPath(efid);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java b/src/java/com/android/internal/telephony/gsm/SIMFileHandler.java
//Synthetic comment -- index 0387a70..3974a29 100644

//Synthetic comment -- @@ -45,6 +45,8 @@
// TODO(): DF_GSM can be 7F20 or 7F21 to handle backward compatibility.
// Implement this after discussion with OEMs.
switch(efid) {
case EF_SMS:
return MF_SIM + DF_TELECOM;








