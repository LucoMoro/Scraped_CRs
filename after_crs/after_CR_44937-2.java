/*Telephony: Reset the data registration state on radio off/unavailable

The data registration state is not set to the out of service or
power off whenever the radio state changed to RADIO_UNAVAILABLE
or RADIO_OFF.  Due to this the GsmSST.getCurrentDataConnectionState
will return the previous data state.  The data state will be set
correctly only after getting the data registration state from the
lower layers. This will lead the DCT to initiate the data call even
if the radio state is off/unavailable, if the response of the get
data registration request is getting delayed from the lower layers.

Changes to reset the data registration state immediately on radio off
or unavailable.

Change-Id:I813a5686db6997a620a5238b40de7245f2c7e6f3*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index 6110bd1..de22a99 100644

//Synthetic comment -- @@ -744,6 +744,7 @@
switch (cm.getRadioState()) {
case RADIO_UNAVAILABLE:
newSS.setStateOutOfService();
                newGPRSState = ServiceState.STATE_OUT_OF_SERVICE;
newCellLoc.setStateInvalid();
setSignalStrengthDefaultValues();
mGotCountryCode = false;
//Synthetic comment -- @@ -753,6 +754,7 @@

case RADIO_OFF:
newSS.setStateOff();
                newGPRSState = ServiceState.STATE_POWER_OFF;
newCellLoc.setStateInvalid();
setSignalStrengthDefaultValues();
mGotCountryCode = false;







