/*Telephony: Reset the data registration state on radio off/unavailable

Change-Id:I813a5686db6997a620a5238b40de7245f2c7e6f3*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index a0be5d0..676f75b 100755

//Synthetic comment -- @@ -692,6 +692,7 @@
switch (cm.getRadioState()) {
case RADIO_UNAVAILABLE:
newSS.setStateOutOfService();
newCellLoc.setStateInvalid();
setSignalStrengthDefaultValues();
mGotCountryCode = false;
//Synthetic comment -- @@ -701,6 +702,7 @@

case RADIO_OFF:
newSS.setStateOff();
newCellLoc.setStateInvalid();
setSignalStrengthDefaultValues();
mGotCountryCode = false;







