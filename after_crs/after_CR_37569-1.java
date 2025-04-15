/*Telephony: CB duplicate detection

Detect and ignore duplicate CB.
3GPP TS 23.041  Section 2 General description states:
Each page of such CBS message will have the same message identifier
(indicating the source of the message), and the same serial number.
Using this information, the MS/UE is able to identify and ignore
re-broadcasts of already received messages.

Change-Id:Ifc5d83af48bda00ccacbf60ba35b711a84fe614a*/




//Synthetic comment -- diff --git a/telephony/java/android/telephony/SmsCbMessage.java b/telephony/java/android/telephony/SmsCbMessage.java
//Synthetic comment -- index 383e0f9..7a15d28 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.gsm.SmsCbHeader;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
//Synthetic comment -- @@ -456,4 +457,27 @@
return "SmsCbMessage{" + mHeader.toString() + ", language=" + mLanguage +
", body=\"" + mBody + "\"}";
}

    /**
     * Returns a byte array that can be use to uniquely identify a received CB message.
     * 3GPP TS 23.041  2 General description:
     * Each page of such CBS message will have the same message identifier
     * (indicating the source of the message), and the same serial number. Using this information,
     *  the MS/UE is able to identify and ignore re-broadcasts of already received messages.
     *
     * @return byte array uniquely identifying the message.
     * @hide
     */
    public byte[] getIncomingCbFingerprint() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // per 3GPP TS 23.041 9.4.1.2.1 Serial number contains
        // GS, Message Code, and Update Number
        output.write(getGeographicalScope());
        output.write(getMessageCode());
        output.write(getUpdateNumber());
        output.write(getMessageIdentifier());

        return output.toByteArray();
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 8a75f51..e774956 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import com.android.internal.telephony.SmsUsageMonitor;
import com.android.internal.telephony.TelephonyProperties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

//Synthetic comment -- @@ -50,6 +51,11 @@
public final class GsmSMSDispatcher extends SMSDispatcher {
private static final String TAG = "GSM";

    // Per 3GPP TS 23.041 Section 9.1.2: Duplicate message detection shall be
    // performed independently for primary and secondary notifications.
    private byte[] mLastDispatchedCbPrimaryFingerprint;
    private byte[] mLastDispatchedCbFingerprint;

/** Status report received */
private static final int EVENT_NEW_SMS_STATUS_REPORT = 100;

//Synthetic comment -- @@ -474,4 +480,39 @@
Log.e(TAG, "Error in decoding SMS CB pdu", e);
}
}

    protected void dispatchBroadcastPdus(byte[][] pdus, boolean isEmergencyMessage) {
        SmsCbMessage message = SmsCbMessage.createFromPdu((byte[]) pdus[0]);
        if (message == null) {
            // should never come here
            Log.e(TAG, "dispatchBroadcastPdus received illegal pdus.");
            return;
        }
        // See if we have a network duplicate broadcast.
        // Per 3GPP TS 23.041 Section 9.1.2: Duplicate message detection shall be
        // performed independently for primary and secondary notifications.
        boolean isPrimary =
                (SmsCbHeader.FORMAT_ETWS_PRIMARY == message.getMessageFormat()) ? true : false;
        byte[] fingerPrint = message.getIncomingCbFingerprint();
        if (isPrimary) {
            if (mLastDispatchedCbPrimaryFingerprint != null
                    && Arrays.equals(mLastDispatchedCbPrimaryFingerprint, fingerPrint)) {
                // found duplicate, ignore.
                Log.d(TAG, "dispatchBroadcastPdus received duplicate primary cb, ignoring.");
                return;
            }
            mLastDispatchedCbPrimaryFingerprint = fingerPrint;
        } else {
            // must be non primary
            if (mLastDispatchedCbFingerprint != null
                    && Arrays.equals(mLastDispatchedCbFingerprint, fingerPrint)) {
                // found duplicate, ignore.
                Log.d(TAG, "dispatchBroadcastPdus received duplicate cb, ignoring.");
                return;
            }
            mLastDispatchedCbFingerprint = fingerPrint;
        }

        super.dispatchBroadcastPdus(pdus, isEmergencyMessage);
    }
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsCbHeader.java b/telephony/java/com/android/internal/telephony/gsm/SmsCbHeader.java
//Synthetic comment -- index 8e6b79b..ed03d74 100644

//Synthetic comment -- @@ -83,9 +83,9 @@

if (pdu.length <= PDU_LENGTH_ETWS) {
format = FORMAT_ETWS_PRIMARY;
            geographicalScope = (pdu[0] & 0xc0) >> 6;
            messageCode = ((pdu[0] & 0x3f) << 4) | ((pdu[1] & 0xf0) >> 4);
            updateNumber = pdu[1] & 0x0f;
messageIdentifier = ((pdu[2] & 0xff) << 8) | (pdu[3] & 0xff);
dataCodingScheme = -1;
pageIndex = -1;







