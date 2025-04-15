/*Fix send/receive MMS while the phone's state isn't in IDLE and the Network type can support voice and data calls concurrently
Change-Id:I67e5b1f6e4f855cc09dd76c251a86a06990b6499*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 4bfb707a..2ed0dbc 100644

//Synthetic comment -- @@ -442,7 +442,8 @@
if ((state == State.IDLE || state == State.SCANNING)
&& (gprsState == ServiceState.STATE_IN_SERVICE || noAutoAttach)
&& mGsmPhone.mSIMRecords.getRecordsLoaded()
                && phone.getState() == Phone.State.IDLE
&& isDataAllowed()
&& !mIsPsRestricted
&& desiredPowerState ) {







