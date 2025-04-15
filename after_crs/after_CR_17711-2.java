/*telephony:  Uninstall Cat Menu when REFRESH RESET received

Change-Id:I980c797faa4141df74b68d94db60488bb27dc3ef*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccRecords.java b/telephony/java/com/android/internal/telephony/IccRecords.java
//Synthetic comment -- index b8d9e3c..db02568 100644

//Synthetic comment -- @@ -35,6 +35,7 @@

protected PhoneBase phone;
protected RegistrantList recordsLoadedRegistrants = new RegistrantList();
    protected RegistrantList mIccRefreshRegistrants = new RegistrantList();

protected int recordsToLoad;  // number of pending load requests

//Synthetic comment -- @@ -99,6 +100,20 @@
recordsLoadedRegistrants.remove(h);
}

    /** Register for IccRefresh */
    public void registerForIccRefreshReset(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        mIccRefreshRegistrants.add(r);
    }

    public void unregisterForIccRefreshReset(Handler h) {
        mIccRefreshRegistrants.remove(h);
    }

    public void onIccRefreshReset() {
        mIccRefreshRegistrants.notifyRegistrants();
    }

public String getMsisdnNumber() {
return msisdn;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 1e23e34..e4b0cea 100644

//Synthetic comment -- @@ -138,6 +138,7 @@

// Events to signal SIM presence or absent in the device.
private static final int MSG_ID_ICC_RECORDS_LOADED       = 20;
    private static final int MSG_ID_ICC_REFRESH_RESET        = 30;

private static final int DEV_ID_KEYPAD      = 0x01;
private static final int DEV_ID_DISPLAY     = 0x02;
//Synthetic comment -- @@ -172,12 +173,16 @@
// Register for SIM ready event.
mIccRecords.registerForRecordsLoaded(this, MSG_ID_ICC_RECORDS_LOADED, null);

        // Register for IccRefreshReset event.
        mIccRecords.registerForIccRefreshReset(this, MSG_ID_ICC_REFRESH_RESET, null);

mCmdIf.reportStkServiceIsRunning(null);
CatLog.d(this, "Is running");
}

public void dispose() {
mIccRecords.unregisterForRecordsLoaded(this);
        mIccRecords.unregisterForIccRefreshReset(this);
mCmdIf.unSetOnCatSessionEnd(this);
mCmdIf.unSetOnCatProactiveCmd(this);
mCmdIf.unSetOnCatEvent(this);
//Synthetic comment -- @@ -579,6 +584,9 @@
case MSG_ID_RESPONSE:
handleCmdResponse((CatResponseMessage) msg.obj);
break;
        case MSG_ID_ICC_REFRESH_RESET:
            handleIccRefreshReset();
            break;
default:
throw new AssertionError("Unrecognized CAT command: " + msg.what);
}
//Synthetic comment -- @@ -612,6 +620,19 @@
return false;
}

    private  void  handleIccRefreshReset() {
        CommandDetails cmdDet = new  CommandDetails();
        cmdDet.typeOfCommand = AppInterface.CommandType.SET_UP_MENU.value();
        SelectItemParams cmdParams = new  SelectItemParams(cmdDet,null,false);
        CatCmdMessage cmdMsg = new CatCmdMessage(cmdParams);

        // Send intent with NULL menu to uninstall STK Main menu.
        CatLog.d(this, "Sending NULL menu to uninstall CAT menu");
        Intent intent = new Intent(AppInterface.CAT_CMD_ACTION);
        intent.putExtra("STK CMD", cmdMsg);
        mContext.sendBroadcast(intent);
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
                onIccRefreshReset();
break;
default:
// unknown refresh operation








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index c80c608..5521ff2 100644

//Synthetic comment -- @@ -1080,14 +1080,7 @@
break;
case CommandsInterface.SIM_REFRESH_RESET:
		if (DBG) log("handleSimRefresh with SIM_REFRESH_RESET");
                onIccRefreshReset();
break;
default:
// unknown refresh operation







