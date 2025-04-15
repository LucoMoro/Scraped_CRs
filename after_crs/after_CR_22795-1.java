/*UICC support for SIMalliance Open Mobile API for Secure Element access

The SIMalliance Open Mobile API specifies the access to any supported secure
element from a mobile application. This patch adds support for the UICC.
Since the UICC is linked to the baseband processor, and not to the application
processor, access to the UICC must be supported by the baseband firmware.
For this reason, this patch extends the Android emulator's virtual modem by
some required AT commands (AT+CSIM, AT+CCHO, AT+CCHC, and AT+CGLA as defined
in 3GPP TS 27.007), and extends the RIL appropriate. For security reasons
access to the UICC is limited to the SIMalliance Open Mobile API.
This patch also adds support for using a hardware UICC inserted to a PC/SC
card reader instead of the emulator's virtual UICC.

The SIMalliance Open Mobile API was contributed under the following change IDs:Ib014d041950494cc1900a7206093f87d7b520d43,I05469de464a21f20efdc7da892f4ccfcedc4b2b8, andI30e78f50542fa8df87897806fc015f4447e02a62Change-Id:I4c15eaf4c80b20f1f1f457e831006f6581508c17Signed-off-by: Robert H. <robert.hockauf@gi-de.com>*/




//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index f3ee97c..8d70955 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.content.Intent;
import android.net.Uri;
import android.os.Process;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Bundle;
//Synthetic comment -- @@ -36,9 +37,15 @@
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.IccIoResult;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.CommandException;

import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
* Implementation of the ITelephony interface.
//Synthetic comment -- @@ -54,12 +61,20 @@
private static final int CMD_ANSWER_RINGING_CALL = 4;
private static final int CMD_END_CALL = 5;  // not used yet
private static final int CMD_SILENCE_RINGER = 6;
    private static final int CMD_EXCHANGE_APDU = 7;
    private static final int EVENT_EXCHANGE_APDU_DONE = 8;
    private static final int CMD_OPEN_CHANNEL = 9;
    private static final int EVENT_OPEN_CHANNEL_DONE = 10;
    private static final int CMD_CLOSE_CHANNEL = 11;
    private static final int EVENT_CLOSE_CHANNEL_DONE = 12;

PhoneApp mApp;
Phone mPhone;
CallManager mCM;
MainThreadHandler mMainThreadHandler;

    private int lastError;

/**
* A request object for use with {@link MainThreadHandler}. Requesters should wait() on the
* request after sending. The main thread will notify the request when it is complete.
//Synthetic comment -- @@ -75,6 +90,23 @@
}
}

    private static final class IccAPDUArgument {

        public int channel, cla, command, p1, p2, p3;
        public String data;

        public IccAPDUArgument(int cla, int command, int channel,
                int p1, int p2, int p3, String data) {
            this.channel = channel;
            this.cla = cla;
            this.command = command;
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            this.data = data;
        }
    }

/**
* A handler that processes messages on the main thread in the phone process. Since many
* of the Phone calls are not thread safe this is needed to shuttle the requests from the
//Synthetic comment -- @@ -95,6 +127,81 @@
AsyncResult ar;

switch (msg.what) {
                case CMD_EXCHANGE_APDU:
                    request = (MainThreadRequest) msg.obj;
                    IccAPDUArgument argument =
                            (IccAPDUArgument) request.argument;
                    onCompleted = obtainMessage(EVENT_EXCHANGE_APDU_DONE,
                            request);
                    mPhone.getIccCard().exchangeAPDU(argument.cla,
                            argument.command,
                            argument.channel, argument.p1, argument.p2,
                            argument.p3, argument.data, onCompleted);
                    break;
                case EVENT_EXCHANGE_APDU_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (MainThreadRequest) ar.userObj;
                    if (ar.exception == null && ar.result != null) {
                        request.result = ar.result;
                    } else {
                        request.result = new IccIoResult(0x6f, 0,
                                (byte[])null);
                    }
                    synchronized (request) {
                        request.notifyAll();
                    }
                    break;
                case CMD_OPEN_CHANNEL:
                    request = (MainThreadRequest) msg.obj;
                    onCompleted = obtainMessage(EVENT_OPEN_CHANNEL_DONE,
                            request);
                    mPhone.getIccCard().openLogicalChannel(
                            (String)request.argument, onCompleted);
                    break;
                case EVENT_OPEN_CHANNEL_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (MainThreadRequest) ar.userObj;
                    if (ar.exception == null && ar.result != null) {
                        request.result = new Integer(((int[])ar.result)[0]);
                    } else {
                        request.result = new Integer(0);
                        lastError = 0;
                        if ((ar.exception != null) &&
                                (ar.exception instanceof CommandException)) {
                            if (ar.exception.getMessage().compareTo(
                                    "MISSING_RESOURCE") == 0) {
                                lastError = 1;
                            } else {
                                if (ar.exception.getMessage().compareTo(
                                        "NO_SUCH_ELEMENT") == 0)
                                    lastError = 2;
                            }
                        }
                    }
                    synchronized (request) {
                        request.notifyAll();
                    }
                    break;
                case CMD_CLOSE_CHANNEL:
                    request = (MainThreadRequest) msg.obj;
                    onCompleted = obtainMessage(EVENT_CLOSE_CHANNEL_DONE,
                            request);
                    mPhone.getIccCard().closeLogicalChannel(
                            ((Integer)request.argument).intValue(),
                            onCompleted);
                    break;
                case EVENT_CLOSE_CHANNEL_DONE:
                    ar = (AsyncResult) msg.obj;
                    request = (MainThreadRequest) ar.userObj;
                    if (ar.exception == null) {
                        request.result = new Integer(0);
                    } else {
                        request.result = new Integer(-1);
                    }
                    synchronized (request) {
                        request.notifyAll();
                    }
                    break;
case CMD_HANDLE_PIN_MMI:
request = (MainThreadRequest) msg.obj;
request.result = Boolean.valueOf(
//Synthetic comment -- @@ -730,4 +837,57 @@
public boolean hasIccCard() {
return mPhone.getIccCard().hasIccCard();
}

    private String exchangeIccAPDU(int cla, int command,
            int channel, int p1, int p2, int p3, String data) {
        if (Binder.getCallingUid() != Process.SMARTCARD_UID)
            throw new SecurityException("Only Smartcard API may access UICC");
        Log.d(LOG_TAG, "> exchangeAPDU " + channel + " " + cla + " " +
                command + " " + p1 + " " + p2 + " " + p3 + " " + data);
        IccIoResult response =
                (IccIoResult)sendRequest(CMD_EXCHANGE_APDU,
                        new IccAPDUArgument(cla, command, channel,
                        p1, p2, p3, data));
        Log.d(LOG_TAG, "< exchangeAPDU " + response);
        String s = Integer.toHexString(
                (response.sw1 << 8) + response.sw2 + 0x10000).substring(1);
        if (response.payload != null)
            s = IccUtils.bytesToHexString(response.payload) + s;
        return s;
    }

    public String transmitIccBasicChannel(int cla, int command,
            int p1, int p2, int p3, String data) {
        return exchangeIccAPDU(cla, command, 0, p1, p2, p3, data);
    }

    public String transmitIccLogicalChannel(int cla, int command,
            int channel, int p1, int p2, int p3, String data) {
        return exchangeIccAPDU(cla, command, channel, p1, p2, p3, data);
    }
 
    public int openIccLogicalChannel(String AID) {
        if (Binder.getCallingUid() != Process.SMARTCARD_UID)
            throw new SecurityException("Only Smartcard API may access UICC");
        Log.d(LOG_TAG, "> openIccLogicalChannel " + AID);
        Integer channel = (Integer)sendRequest(CMD_OPEN_CHANNEL, AID);
        Log.d(LOG_TAG, "< openIccLogicalChannel " + channel);
        return channel.intValue();
    }

    public boolean closeIccLogicalChannel(int channel) {
        if (Binder.getCallingUid() != Process.SMARTCARD_UID)
            throw new SecurityException("Only Smartcard API may access UICC");
        Log.d(LOG_TAG, "> closeIccLogicalChannel " + channel);
        Integer err = (Integer)sendRequest(CMD_CLOSE_CHANNEL,
                new Integer(channel));
        Log.d(LOG_TAG, "< closeIccLogicalChannel " + err);
        if(err.intValue() == 0)
            return true;
        return false;
    }

    public int getLastError() {
        return lastError;
    }
}







