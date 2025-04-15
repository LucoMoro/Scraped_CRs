/*Apparently a typo resulting in loosing mobile data connection after a while.
Probably connected to issue 2207 (http://code.google.com/p/android/issues/detail?id=2207)

Change-Id:I69cedf1a7597056c08a67c71aaa86c4a16d18930*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index ab9cf2a..1c5dea2 100644

//Synthetic comment -- @@ -440,7 +440,7 @@
boolean desiredPowerState = mGsmPhone.mSST.getDesiredPowerState();

if ((state == State.IDLE || state == State.SCANNING)
                && (gprsState == ServiceState.STATE_IN_SERVICE || noAutoAttach)
&& mGsmPhone.mSIMRecords.getRecordsLoaded()
&& phone.getState() == Phone.State.IDLE
&& isDataAllowed()







