/*Allow data connections after a no-reattach message from the network

In the current code there is a check whenever an application tries
to setup a data connection if the connection is automatically
attached by the platform or not. However, this check doesn’t take
into account that the platform might be disconnected by the network
together with a return code which asks it not to attach again.

This fix solves the problem when the network sends a de-attach
request with the command that re-attach is not required. This
would cause the network to never re-attach due to the noAutoAttach
property. Gprs state check was removed as well. When trySetupData
is called, the phone will try to attach if possible.

Change-Id:Ia09b80bc7f724c412030dbb3d40802c0b07f4f42*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 5f651e7..62322cc 100644

//Synthetic comment -- @@ -83,9 +83,6 @@

//***** Instance Variables

private boolean mReregisterOnReconnectFailure = false;
private ContentResolver mResolver;

//Synthetic comment -- @@ -254,7 +251,6 @@
if (dataEnabled[APN_DEFAULT_ID]) {
enabledCount++;
}

if (!mRetryMgr.configure(SystemProperties.get("ro.gsm.data_retry_config"))) {
if (!mRetryMgr.configure(DEFAULT_DATA_RETRY_CONFIG)) {
//Synthetic comment -- @@ -436,11 +432,9 @@
return true;
}

boolean desiredPowerState = mGsmPhone.mSST.getDesiredPowerState();

if ((state == State.IDLE || state == State.SCANNING)
&& mGsmPhone.mSIMRecords.getRecordsLoaded()
&& (mGsmPhone.mSST.isConcurrentVoiceAndData() ||
phone.getState() == Phone.State.IDLE )
//Synthetic comment -- @@ -467,7 +461,6 @@
if (DBG)
log("trySetupData: Not ready for data: " +
" dataState=" + state +
" sim=" + mGsmPhone.mSIMRecords.getRecordsLoaded() +
" UMTS=" + mGsmPhone.mSST.isConcurrentVoiceAndData() +
" phoneState=" + phone.getState() +







