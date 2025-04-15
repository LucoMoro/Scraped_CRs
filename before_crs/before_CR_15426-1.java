/*Enable Data Connection during voice call

Removed the check for phone state idle in trySetupData() function.
RIL or modem can return a failure if data call cannot be setup
while voice call is in progress.

Change-Id:Icd8a4a3c8fc1cedf45a726dbd50744b466ce3b3b*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 9f2a44b..0344762 100644

//Synthetic comment -- @@ -331,8 +331,6 @@
&& (psState == ServiceState.STATE_IN_SERVICE)
&& ((phone.mCM.getRadioState() == CommandsInterface.RadioState.NV_READY) ||
mCdmaPhone.mRuimRecords.getRecordsLoaded())
                && (mCdmaPhone.mSST.isConcurrentVoiceAndData() ||
                        phone.getState() == Phone.State.IDLE )
&& isDataAllowed()
&& desiredPowerState
&& !mPendingRestartRadio








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index f6d4491..c16eb0b 100644

//Synthetic comment -- @@ -442,7 +442,6 @@
if ((state == State.IDLE || state == State.SCANNING)
&& (gprsState == ServiceState.STATE_IN_SERVICE || noAutoAttach)
&& mGsmPhone.mSIMRecords.getRecordsLoaded()
                && phone.getState() == Phone.State.IDLE
&& isDataAllowed()
&& !mIsPsRestricted
&& desiredPowerState ) {







