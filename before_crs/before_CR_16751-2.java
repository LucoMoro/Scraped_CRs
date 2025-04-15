/*Telephony: FIX CDMA Sms status report message.

SMS delivery status is mapped to appropriate state like
COMPLETE/PENDING/FAILED/NONE based on Error Class in Bearer Data.
UI checks for status to be in one of these states when displaying
status report.

Change-Id:I44d08eb03c99bc17f55bef74ddefb9ebaf252b0e*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java b/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index b50502c..e245f60 100755

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.HexDump;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//Synthetic comment -- @@ -83,6 +84,12 @@
private static final int RETURN_NO_ACK  = 0;
private static final int RETURN_ACK     = 1;

private SmsEnvelope mEnvelope;
private BearerData mBearerData;

//Synthetic comment -- @@ -421,7 +428,7 @@
* shifted to the bits 31-16.
*/
public int getStatus() {
        return (status << 16);
}

/** Return true iff the bearer data message type is DELIVERY_ACK. */
//Synthetic comment -- @@ -583,8 +590,21 @@
" userData).");
status = 0;
} else {
                status = mBearerData.errorClass << 8;
                status |= mBearerData.messageStatus;
}
} else if (mBearerData.messageType != BearerData.MESSAGE_TYPE_DELIVER) {
throw new RuntimeException("Unsupported message type: " + mBearerData.messageType);







