//<Beginning of snippet n. 0>


import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.HexDump;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

/**
 * Here, the error class is defined by the bits from 9-8, the status code by the bits from 7-0.
 * See C.S0015-B, v2.0, 4.5.21 for a detailed description of possible values.
 */
private int status;

private static final int STATUS_COMPLETE = 1;
private static final int STATUS_PENDING = 2;
private static final int STATUS_FAILED = 3;
private static final int STATUS_NONE = 0;
private static final int RETURN_NO_ACK = 0;
private static final int RETURN_ACK = 1;

private SmsEnvelope mEnvelope;
private BearerData mBearerData;

/**
 * Returns the status for a previously submitted message.
 * For not interfering with status codes from GSM, this status code is
 * shifted to the bits 31-16.
 */
public int getStatus() {
    return (status << 16);
}

/** 
 * Return true iff the bearer data message type is DELIVERY_ACK. 
 */
if (!mBearerData.messageStatusSet) {
    Log.d(LOG_TAG, "DELIVERY_ACK message without msgStatus (" +
            (userData == null ? "also missing" : "does have") +
            " userData).");
    status = STATUS_NONE;
} else {
    if ((mBearerData.errorClass < 0 || mBearerData.errorClass > 3) ||
        (mBearerData.messageStatus < 0 || mBearerData.messageStatus > 3)) {
        Log.e(LOG_TAG, "Invalid error class or message status");
        status = STATUS_NONE;
    } else {
        status = mBearerData.errorClass << 8;
        status |= mBearerData.messageStatus;
    }
}

if (mBearerData.messageType != BearerData.MESSAGE_TYPE_DELIVER) {
    throw new RuntimeException("Unsupported message type: " + mBearerData.messageType);
}

//<End of snippet n. 0>