//<Beginning of snippet n. 0>


import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.HexDump;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

private int status;

private static final int RETURN_NO_ACK  = 0;
private static final int RETURN_ACK     = 1;

private static final int STATUS_COMPLETE = 1;
private static final int STATUS_PENDING  = 2;
private static final int STATUS_FAILED   = 3;
private static final int STATUS_NONE     = 0;

private SmsEnvelope mEnvelope;
private BearerData mBearerData;

public int getStatus() {
    return (status << 16);
}

public boolean isDeliverAck() {
    if (!mBearerData.messageStatusSet) {
        Log.d(LOG_TAG, "DELIVERY_ACK message without msgStatus (" +
            (userData == null ? "also missing" : "does have") +
            " userData).");
        status = STATUS_NONE;
        return true;
    } else {
        int errorClass = mBearerData.errorClass;
        int messageStatus = mBearerData.messageStatus;

        if (errorClass < 0 || errorClass > 3) {
            Log.e(LOG_TAG, "Invalid error class: " + errorClass);
            throw new InvalidErrorClassException("Unsupported error class: " + errorClass);
        }

        if (messageStatus < 0 || messageStatus > 255) {
            Log.e(LOG_TAG, "Invalid message status: " + messageStatus);
            throw new InvalidMessageStatusException("Unsupported message status: " + messageStatus);
        }

        int mappedStatus = mapDeliveryState(errorClass, messageStatus);
        if (!isValidStatus(mappedStatus)) {
            Log.e(LOG_TAG, "Invalid status value: " + mappedStatus);
            throw new RuntimeException("Invalid status mapping");
        }
        status = mappedStatus;
    }

    if (mBearerData.messageType != BearerData.MESSAGE_TYPE_DELIVER) {
        Log.e(LOG_TAG, "Unsupported message type: " + mBearerData.messageType);
        throw new RuntimeException("Unsupported message type: " + mBearerData.messageType);
    }

    return true;
}

private int mapDeliveryState(int errorClass, int messageStatus) {
    if (errorClass == 0) {
        return (messageStatus == 0) ? STATUS_COMPLETE : STATUS_FAILED;
    } else if (errorClass == 1) {
        return STATUS_PENDING;
    } else if (errorClass == 2) {
        return (messageStatus == 0) ? STATUS_PENDING : STATUS_FAILED;
    } else if (errorClass == 3) {
        return (messageStatus == 0) ? STATUS_FAILED : STATUS_NONE;  
    }
    return STATUS_NONE;
}

private boolean isValidStatus(int status) {
    return status == STATUS_COMPLETE || status == STATUS_PENDING || status == STATUS_FAILED || status == STATUS_NONE;
}

//<End of snippet n. 0>