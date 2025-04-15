/*Suppress the CDMA DEACTIVATE_DATA_CALL during data list change

When modem disconnects the CDMA data call and sends UNSOL_DATA_CALL_LIST_CHANGED
event, there is no need to deactivate the data call again during RADIO_TURN_OFF.

Also in 1x mode during radio power-off, if the data call is torn down before
the radio power off, modem will first have to send a data call release and then
change to idle state and send out power down registration. Instead if we directly
initiate a power down, modem just sets a power down registration bit in the release
order. This change optimizes the power down procedure in 1x by letting the modem
handle data call release during power down.

Change-Id:I69bb5880f0bc5f29a8fbb534453d64bb3b1b9da1*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaDataConnectionTracker.java
//Synthetic comment -- index 9f2a44b..2c73732 100644

//Synthetic comment -- @@ -920,7 +920,13 @@
Log.i(LOG_TAG, "onDataStateChanged: No active connection"
+ "state is CONNECTED, disconnecting/cleanup");
writeEventLogCdmaDataDrop();
                cleanUpConnection(true, null);
return;
}









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index 2cad6cc..3823597 100644

//Synthetic comment -- @@ -553,31 +553,43 @@

@Override
protected void powerOffRadioSafely(){
        // clean data connection
        DataConnectionTracker dcTracker = phone.mDataConnection;

        Message msg = dcTracker.obtainMessage(DataConnectionTracker.EVENT_CLEAN_UP_CONNECTION);
        msg.arg1 = 1; // tearDown is true
        msg.obj = CDMAPhone.REASON_RADIO_TURNED_OFF;
        dcTracker.sendMessage(msg);

        synchronized(this) {
            if (!mPendingRadioPowerOffAfterDataOff) {
                DataConnectionTracker.State currentState = dcTracker.getState();
                if (currentState != DataConnectionTracker.State.CONNECTED
                        && currentState != DataConnectionTracker.State.DISCONNECTING
                        && currentState != DataConnectionTracker.State.INITING) {
                    if (DBG) log("Data disconnected, turn off radio right away.");
                    hangupAndPowerOff();
                }
                else if (sendEmptyMessageDelayed(EVENT_SET_RADIO_POWER_OFF, 30000)) {
                    if (DBG) {
                        log("Wait up to 30 sec for data to disconnect, then turn off radio.");
}
                    mPendingRadioPowerOffAfterDataOff = true;
                } else {
                    Log.w(LOG_TAG, "Cannot send delayed Msg, turn off radio right away.");
                    hangupAndPowerOff();
}
}
}







