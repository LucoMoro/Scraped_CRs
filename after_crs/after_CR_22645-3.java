/*apps/Phone: Add a RIL interface to change the transmit power

Change-Id:Ic450bc4359b8d78c610f0828c44f3af6d3f289b9*/




//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index f3ee97c..3f062bd 100644

//Synthetic comment -- @@ -54,6 +54,8 @@
private static final int CMD_ANSWER_RINGING_CALL = 4;
private static final int CMD_END_CALL = 5;  // not used yet
private static final int CMD_SILENCE_RINGER = 6;
    private static final int CMD_SET_TRANSMIT_POWER = 7;
    private static final int EVENT_SET_TRANSMIT_POWER_DONE = 8;

PhoneApp mApp;
Phone mPhone;
//Synthetic comment -- @@ -93,6 +95,7 @@
MainThreadRequest request;
Message onCompleted;
AsyncResult ar;
            boolean retStatus = false;

switch (msg.what) {
case CMD_HANDLE_PIN_MMI:
//Synthetic comment -- @@ -157,6 +160,28 @@
}
break;

                case CMD_SET_TRANSMIT_POWER:
                    request = (MainThreadRequest) msg.obj;
                    onCompleted = obtainMessage(EVENT_SET_TRANSMIT_POWER_DONE, request);
                    mPhone.setTransmitPower((Integer) request.argument, onCompleted);
                    break;

                case EVENT_SET_TRANSMIT_POWER_DONE:
                    retStatus = false;
                    ar = (AsyncResult)msg.obj;
                    request = (MainThreadRequest)ar.userObj;

                    if (ar.exception == null) {
                        retStatus = true;
                    }
                    request.result = retStatus;

                    // Wake up the requesting thread
                    synchronized (request) {
                        request.notifyAll();
                    }
                    break;

default:
Log.w(LOG_TAG, "MainThreadHandler: unexpected message code: " + msg.what);
break;
//Synthetic comment -- @@ -730,4 +755,17 @@
public boolean hasIccCard() {
return mPhone.getIccCard().hasIccCard();
}

    /**
     * Sets the transmit power
     *
     * @param power - Specifies the transmit power that is allowed. One of
     *            TRANSMIT_POWER_DEFAULT      - restore default transmit power
     *            TRANSMIT_POWER_WIFI_HOTSPOT - reduce transmit power as per FCC
     *                               regulations (CFR47 2.1093) for WiFi hotspot
     */
    public boolean setTransmitPower(int powerLevel) {
        enforceModifyPermission();
        return (Boolean) sendRequest(CMD_SET_TRANSMIT_POWER, powerLevel);
    }
}







