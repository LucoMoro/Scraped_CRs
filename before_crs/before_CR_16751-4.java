/*Telephony: Fix CDMA Sms status report message.

SMS delivery status is mapped to appropriate state like
COMPLETE/PENDING/FAILED/NONE based on Error Class in Bearer Data.
UI checks for status to be in one of these states when displaying
status report.

Change-Id:I44d08eb03c99bc17f55bef74ddefb9ebaf252b0e*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java b/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index b50502c..72d2857 100755

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.HexDump;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//Synthetic comment -- @@ -77,12 +78,18 @@
*  Here, the error class is defined by the bits from 9-8, the status code by the bits from 7-0.
*  See C.S0015-B, v2.0, 4.5.21 for a detailed description of possible values.
*/
    private int status;

/** Specifies if a return of an acknowledgment is requested for send SMS */
private static final int RETURN_NO_ACK  = 0;
private static final int RETURN_ACK     = 1;

private SmsEnvelope mEnvelope;
private BearerData mBearerData;

//Synthetic comment -- @@ -417,11 +424,9 @@

/**
* Returns the status for a previously submitted message.
     * For not interfering with status codes from GSM, this status code is
     * shifted to the bits 31-16.
*/
public int getStatus() {
        return (status << 16);
}

/** Return true iff the bearer data message type is DELIVERY_ACK. */
//Synthetic comment -- @@ -576,15 +581,28 @@
// being reported refers to.  The MsgStatus subparameter
// is primarily useful to indicate error conditions -- a
// message without this subparameter is assumed to
            // indicate successful delivery (status == 0).
if (! mBearerData.messageStatusSet) {
Log.d(LOG_TAG, "DELIVERY_ACK message without msgStatus (" +
(userData == null ? "also missing" : "does have") +
" userData).");
                status = 0;
} else {
                status = mBearerData.errorClass << 8;
                status |= mBearerData.messageStatus;
}
} else if (mBearerData.messageType != BearerData.MESSAGE_TYPE_DELIVER) {
throw new RuntimeException("Unsupported message type: " + mBearerData.messageType);







