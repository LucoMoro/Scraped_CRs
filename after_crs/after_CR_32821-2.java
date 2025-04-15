/*Telephony: Add support for sending DTMF codes.

Implementations added to PhoneInterfaceManager:
- TelephonyManager sendDtmf, startDtmf, stopDtmf interface
  implementations forward actual work to CallManager.

Change-Id:Id30bce303c5b45f5ef7cdebe1246ab6006075407*/




//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index ab6011c..2b40d60 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -57,6 +57,9 @@
private static final int CMD_ANSWER_RINGING_CALL = 4;
private static final int CMD_END_CALL = 5;  // not used yet
private static final int CMD_SILENCE_RINGER = 6;
    private static final int CMD_START_DTMF = 7;
    private static final int CMD_STOP_DTMF = 8;
    private static final int CMD_SEND_DTMF = 9;

/** The singleton instance. */
private static PhoneInterfaceManager sInstance;
//Synthetic comment -- @@ -163,6 +166,39 @@
}
break;

                case CMD_START_DTMF:
                {
                    request = (MainThreadRequest) msg.obj;
                    DtmfData arg = (DtmfData)request.argument;
                    request.result = mCM.startDtmf(arg.dtmfTargetAddress, arg.dtmfChar);
                    synchronized (request) {
                        request.notifyAll();
                    }
                    break;
                }

                case CMD_STOP_DTMF:
                {
                    request = (MainThreadRequest) msg.obj;
                    String arg = (String)request.argument;
                    request.result = mCM.stopDtmf(arg);
                    synchronized (request) {
                        request.notifyAll();
                    }
                    break;
                }

                case CMD_SEND_DTMF:
                {
                    request = (MainThreadRequest) msg.obj;
                    DtmfData arg = (DtmfData)request.argument;
                    request.result = mCM.sendDtmf(arg.dtmfTargetAddress, arg.dtmfChar);
                    synchronized (request) {
                        request.notifyAll();
                    }
                    break;
                }

default:
Log.w(LOG_TAG, "MainThreadHandler: unexpected message code: " + msg.what);
break;
//Synthetic comment -- @@ -794,4 +830,59 @@
public int getLteOnCdmaMode() {
return mPhone.getLteOnCdmaMode();
}

    /**
     * Starts sending a DTMF tone to the specified target address.
     * @param address has to be the address of the active and
     * foreground call.
     * @param c the telephone key code of the DTMF tone to send.
     * Only characters '0' - '9', '*' and '#' may be sent, invalid characters
     * will be ignored.
     * @return status code for DTMF tone start.
     *
     * @see TelephonyManager#startDtmf(String, char)
     */
    public int startDtmf(String address, char c) {
        enforceCallPermission();
        return (Integer) sendRequest(CMD_START_DTMF, new DtmfData(address, c));
    }

    /**
     * Stops sending a DTMF tone to the specified target address.
     * @param address has to be the address of the active and
     * foreground call.
     * @return status code for DTMF tone stop.
     *
     * @see TelephonyManager#stopDtmf(String)
     */
    public int stopDtmf(String address) {
        enforceCallPermission();
        return (Integer) sendRequest(CMD_STOP_DTMF, address);
    }

    /**
     * Sends a DTMF tone to the specified target address.
     * @param address has to be the address of the active and
     * foreground call.
     * @param c the telephone key code of the DTMF tone to send.
     * Only characters '0' - '9', '*' and '#' may be sent, invalid characters
     * will be ignored.
     * @return status code for DTMF tone send.
     *
     * @see TelephonyManager#sendDtmf(String, char)
     */
    public int sendDtmf(String address, char c) {
        enforceCallPermission();
        return (Integer) sendRequest(CMD_SEND_DTMF, new DtmfData(address, c));
    }

    private class DtmfData {
        public char dtmfChar;
        public String dtmfTargetAddress;

        public DtmfData(String address, char c) {
            this.dtmfTargetAddress = address;
            this.dtmfChar = c;
        }
    }
}







