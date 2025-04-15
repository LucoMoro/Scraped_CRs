/*telephony:  Uninstall Cat Menu when REFRESH RESET received

Change-Id:I980c797faa4141df74b68d94db60488bb27dc3ef*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccRecords.java b/telephony/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index b8d9e3c..db02568 100644

//Synthetic comment -- @@ -35,6 +35,7 @@

protected PhoneBase phone;
protected RegistrantList recordsLoadedRegistrants = new RegistrantList();

protected int recordsToLoad;  // number of pending load requests

//Synthetic comment -- @@ -99,6 +100,20 @@
recordsLoadedRegistrants.remove(h);
}

public String getMsisdnNumber() {
return msisdn;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 1e23e34..e4b0cea 100644

//Synthetic comment -- @@ -138,6 +138,7 @@

// Events to signal SIM presence or absent in the device.
private static final int MSG_ID_ICC_RECORDS_LOADED       = 20;

private static final int DEV_ID_KEYPAD      = 0x01;
private static final int DEV_ID_DISPLAY     = 0x02;
//Synthetic comment -- @@ -172,12 +173,16 @@
// Register for SIM ready event.
mIccRecords.registerForRecordsLoaded(this, MSG_ID_ICC_RECORDS_LOADED, null);

mCmdIf.reportStkServiceIsRunning(null);
CatLog.d(this, "Is running");
}

public void dispose() {
mIccRecords.unregisterForRecordsLoaded(this);
mCmdIf.unSetOnCatSessionEnd(this);
mCmdIf.unSetOnCatProactiveCmd(this);
mCmdIf.unSetOnCatEvent(this);
//Synthetic comment -- @@ -579,6 +584,9 @@
case MSG_ID_RESPONSE:
handleCmdResponse((CatResponseMessage) msg.obj);
break;
default:
throw new AssertionError("Unrecognized CAT command: " + msg.what);
}
//Synthetic comment -- @@ -612,6 +620,19 @@
return false;
}

private void handleCmdResponse(CatResponseMessage resMsg) {
// Make sure the response details match the last valid command. An invalid
// response is a one that doesn't have a corresponding proactive command








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java b/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index 87b0c60..f4ac807 100644

//Synthetic comment -- @@ -369,14 +369,7 @@
break;
case CommandsInterface.SIM_REFRESH_RESET:
if (DBG) log("handleRuimRefresh with SIM_REFRESH_RESET");
                phone.mCM.setRadioPower(false, null);
                /* Note: no need to call setRadioPower(true).  Assuming the desired
                * radio power state is still ON (as tracked by ServiceStateTracker),
                * ServiceStateTracker will call setRadioPower when it receives the
                * RADIO_STATE_CHANGED notification for the power off.  And if the
                * desired power state has changed in the interim, we don't want to
                * override it with an unconditional power on.
                */
break;
default:
// unknown refresh operation








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index c80c608..5521ff2 100644

//Synthetic comment -- @@ -1080,14 +1080,7 @@
break;
case CommandsInterface.SIM_REFRESH_RESET:
		if (DBG) log("handleSimRefresh with SIM_REFRESH_RESET");
                phone.mCM.setRadioPower(false, null);
                /* Note: no need to call setRadioPower(true).  Assuming the desired
                * radio power state is still ON (as tracked by ServiceStateTracker),
                * ServiceStateTracker will call setRadioPower when it receives the
                * RADIO_STATE_CHANGED notification for the power off.  And if the
                * desired power state has changed in the interim, we don't want to
                * override it with an unconditional power on.
                */
break;
default:
// unknown refresh operation







