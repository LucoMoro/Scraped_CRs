//<Beginning of snippet n. 0>


import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.HexDump;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

private int status;

private static final int RETURN_NO_ACK  = 0;
private static final int RETURN_ACK     = 1;

private SmsEnvelope mEnvelope;
private BearerData mBearerData;

public int getStatus() {
    return (status << 16);
}

public void updateStatus() {
    if (mBearerData == null) {
        throw new IllegalStateException("mBearerData is null");
    }

    if (!mBearerData.messageStatusSet) {
        Log.d(LOG_TAG, "DELIVERY_ACK message without msgStatus (" +
                (userData == null ? "also missing" : "does have") +
                " userData).");
        status = 0;
    } else {
        validateBearerData(mBearerData);
        status = mapErrorClassToStatus(mBearerData.errorClass);
        status |= mBearerData.messageStatus;
    }

    if (mBearerData.messageType != BearerData.MESSAGE_TYPE_DELIVER) {
        throw new IllegalArgumentException("Unsupported message type: " + mBearerData.messageType);
    }

    Log.d(LOG_TAG, "Status updated to: " + status);
}

private void validateBearerData(BearerData data) {
    if (data.errorClass < 0 || data.errorClass > 3 || 
        data.messageStatus < 0 || data.messageStatus > 3) {
        throw new IllegalArgumentException("Invalid errorClass or messageStatus values");
    }
}

private int mapErrorClassToStatus(int errorClass) {
    switch (errorClass) {
        case 0:
            return MessageStatus.COMPLETE; // Assume COMPLETE maps to 0
        case 1:
            return MessageStatus.PENDING;  // Assume PENDING maps to 1
        case 2:
            return MessageStatus.FAILED;   // Assume FAILED maps to 2
        case 3:
            return MessageStatus.NONE;     // Assume NONE maps to 3
        default:
            throw new IllegalArgumentException("Unsupported errorClass: " + errorClass);
    }
}

//<End of snippet n. 0>