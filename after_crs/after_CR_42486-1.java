/*Update PS state when radio off or unavailable

When radio off or unavailable, CS state has been reset,
but PS state has not updated, in fact both CS and PS
state should be out of service. So SMS will try to be
sent out when sending sms, the result is "could not send"
rather than sms being queued for sending.
This fix will reset PS state to power off or out of
service when radio off or unavailable accordingly.

Change-Id:I46e962c30021ff1273dcc559579bdedc7f432a64*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
old mode 100755
new mode 100644
//Synthetic comment -- index c19cc5e..620914c

//Synthetic comment -- @@ -135,6 +135,7 @@
switch (cm.getRadioState()) {
case RADIO_UNAVAILABLE:
newSS.setStateOutOfService();
                mLteSS.setStateOutOfService();
newCellLoc.setStateInvalid();
setSignalStrengthDefaultValues();
mGotCountryCode = false;
//Synthetic comment -- @@ -144,6 +145,7 @@

case RADIO_OFF:
newSS.setStateOff();
                mLteSS.setStateOff();
newCellLoc.setStateInvalid();
setSignalStrengthDefaultValues();
mGotCountryCode = false;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
old mode 100755
new mode 100644
//Synthetic comment -- index 697ad73..e6d98e9

//Synthetic comment -- @@ -827,6 +827,7 @@
switch (cm.getRadioState()) {
case RADIO_UNAVAILABLE:
newSS.setStateOutOfService();
            mNewDataConnectionState = ServiceState.STATE_OUT_OF_SERVICE;
newCellLoc.setStateInvalid();
setSignalStrengthDefaultValues();
mGotCountryCode = false;
//Synthetic comment -- @@ -836,6 +837,7 @@

case RADIO_OFF:
newSS.setStateOff();
            mNewDataConnectionState = ServiceState.STATE_POWER_OFF;
newCellLoc.setStateInvalid();
setSignalStrengthDefaultValues();
mGotCountryCode = false;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
old mode 100755
new mode 100644
//Synthetic comment -- index 42443fe..e293bb5

//Synthetic comment -- @@ -688,6 +688,7 @@
switch (cm.getRadioState()) {
case RADIO_UNAVAILABLE:
newSS.setStateOutOfService();
                newGPRSState = ServiceState.STATE_OUT_OF_SERVICE;
newCellLoc.setStateInvalid();
setSignalStrengthDefaultValues();
mGotCountryCode = false;
//Synthetic comment -- @@ -697,6 +698,7 @@

case RADIO_OFF:
newSS.setStateOff();
                newGPRSState = ServiceState.STATE_POWER_OFF;
newCellLoc.setStateInvalid();
setSignalStrengthDefaultValues();
mGotCountryCode = false;







