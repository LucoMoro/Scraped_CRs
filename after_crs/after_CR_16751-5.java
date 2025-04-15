/*Telephony: Fix CDMA Sms status report message.

SMS delivery status is mapped to appropriate state like
COMPLETE/PENDING/FAILED/NONE based on Error Class in Bearer Data.
UI checks for status to be in one of these states when displaying
status report.

Change-Id:I44d08eb03c99bc17f55bef74ddefb9ebaf252b0e*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java b/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index b50502c..359b8e1 100755

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.HexDump;
import android.provider.Telephony.Sms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//Synthetic comment -- @@ -77,12 +78,18 @@
*  Here, the error class is defined by the bits from 9-8, the status code by the bits from 7-0.
*  See C.S0015-B, v2.0, 4.5.21 for a detailed description of possible values.
*/
    private int mStatus;

/** Specifies if a return of an acknowledgment is requested for send SMS */
private static final int RETURN_NO_ACK  = 0;
private static final int RETURN_ACK     = 1;

    /* Indicates the Cdma Error Class values
       Message Status (See 3GPP2 C.S0015-B, v2, 4.5.1) */
    private static final int CDMA_SMS_STATUS_NO_ERROR  = 0;  // No Error
    private static final int CDMA_SMS_STATUS_PENDING   = 2;  // Temporary Condition
    private static final int CDMA_SMS_STATUS_FAILED    = 3;  // Permanent Condition

private SmsEnvelope mEnvelope;
private BearerData mBearerData;

//Synthetic comment -- @@ -417,11 +424,11 @@

/**
* Returns the status for a previously submitted message.
     * SMS delivery status is mapped to appropriate state like
     * COMPLETE/PENDING/FAILED/NONE based on Error Class in Bearer Data.
*/
public int getStatus() {
        return mStatus;
}

/** Return true iff the bearer data message type is DELIVERY_ACK. */
//Synthetic comment -- @@ -576,15 +583,28 @@
// being reported refers to.  The MsgStatus subparameter
// is primarily useful to indicate error conditions -- a
// message without this subparameter is assumed to
            // indicate successful delivery (mStatus == 0).
if (! mBearerData.messageStatusSet) {
Log.d(LOG_TAG, "DELIVERY_ACK message without msgStatus (" +
(userData == null ? "also missing" : "does have") +
" userData).");
                mStatus = 0;
} else {
                // Message Status (See 3GPP2 C.S0015-B, v2, 4.5.1)
                switch(mBearerData.errorClass) {
                     case CDMA_SMS_STATUS_NO_ERROR:
                          mStatus = Sms.STATUS_COMPLETE;
                          break;
                     case CDMA_SMS_STATUS_PENDING:
                          mStatus = Sms.STATUS_PENDING;
                          break;
                     case CDMA_SMS_STATUS_FAILED:
                          mStatus = Sms.STATUS_FAILED;
                          break;
                     default:
                          mStatus = Sms.STATUS_NONE;
                          break;
                }
}
} else if (mBearerData.messageType != BearerData.MESSAGE_TYPE_DELIVER) {
throw new RuntimeException("Unsupported message type: " + mBearerData.messageType);







