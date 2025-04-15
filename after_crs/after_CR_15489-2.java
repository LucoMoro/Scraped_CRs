/*Do not explicity disconnect Data during power down for 1x.

In 1x, if the data call is torn down before radio power off, modem will
have to send a data call release and change to initialization state followed
by idle state and send out power down registration. If the power off request is sent
to the modem during Initialization state after call release, there is a chance that
modem does not perform power down registration.

Instead if we directly initiate a power down, modem just sets a power down registration
bit in the release order. This change also optimizes the power down procedure in 1x by
letting the modem handle data call release during power down.

Change-Id:I0f083cc3b005ec1e64105350abb43d10583b0881*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
old mode 100644
new mode 100755
//Synthetic comment -- index 2cad6cc..a121558

//Synthetic comment -- @@ -552,32 +552,53 @@
}

@Override
    protected void powerOffRadioSafely() {
DataConnectionTracker dcTracker = phone.mDataConnection;

Message msg = dcTracker.obtainMessage(DataConnectionTracker.EVENT_CLEAN_UP_CONNECTION);
msg.obj = CDMAPhone.REASON_RADIO_TURNED_OFF;

        if (networkType == ServiceState.RADIO_TECHNOLOGY_1xRTT) {
            /*
             * In 1x CDMA , during radio power off modem will disconnect the
             * data call and sends the power down registration message along
             * with the data call release message to the network
             */

            msg.arg1 = 0; // tearDown is false since modem does it anyway for 1X
            dcTracker.sendMessage(msg);

            Log.w(LOG_TAG, "Turn off the radio right away");
            hangupAndPowerOff();
        } else {
            synchronized (this) {
                if (!mPendingRadioPowerOffAfterDataOff) {
                    DataConnectionTracker.State currentState = dcTracker.getState();
                    if (currentState != DataConnectionTracker.State.CONNECTED
                            && currentState != DataConnectionTracker.State.DISCONNECTING
                            && currentState != DataConnectionTracker.State.INITING) {

                        msg.arg1 = 0; // tearDown is false as it is not needed.
                        dcTracker.sendMessage(msg);

                        if (DBG)
                            log("Data disconnected, turn off radio right away.");
                        hangupAndPowerOff();
                    } else {
                        // clean data connection
                        msg.arg1 = 1; // tearDown is true
                        dcTracker.sendMessage(msg);

                        if (sendEmptyMessageDelayed(EVENT_SET_RADIO_POWER_OFF, 30000)) {
                            if (DBG) {
                                log("Wait upto 30s for data to disconnect, then turn off radio.");
                            }
                            mPendingRadioPowerOffAfterDataOff = true;
                        } else {
                            Log.w(LOG_TAG, "Cannot send delayed Msg, turn off radio right away.");
                            hangupAndPowerOff();
                        }
}
}
}
}







