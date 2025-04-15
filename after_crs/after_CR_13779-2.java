/*Add the UTMS check in trySetupData

Change-Id:I67e5b1f6e4f855cc09dd76c251a86a06990b6499*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 7b60474..7d88820 100644

//Synthetic comment -- @@ -443,6 +443,7 @@
if ((state == State.IDLE || state == State.SCANNING)
&& (gprsState == ServiceState.STATE_IN_SERVICE || noAutoAttach)
&& mGsmPhone.mSIMRecords.getRecordsLoaded()
                && mGsmPhone.mSST.isConcurrentVoiceAndData()
&& phone.getState() == Phone.State.IDLE
&& isDataAllowed()
&& !mIsPsRestricted







