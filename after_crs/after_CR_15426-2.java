/*Enable Data Connection during voice call if supported

If Modem / Radio technology can support concurrent voice and data
allow data call setup to be initiated when voice call is active.

Change-Id:I5e10418c3d41b282771664bb2cecffc722d375ba*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index f6d4491..5f651e7 100644

//Synthetic comment -- @@ -442,7 +442,8 @@
if ((state == State.IDLE || state == State.SCANNING)
&& (gprsState == ServiceState.STATE_IN_SERVICE || noAutoAttach)
&& mGsmPhone.mSIMRecords.getRecordsLoaded()
                && (mGsmPhone.mSST.isConcurrentVoiceAndData() ||
                        phone.getState() == Phone.State.IDLE )
&& isDataAllowed()
&& !mIsPsRestricted
&& desiredPowerState ) {







