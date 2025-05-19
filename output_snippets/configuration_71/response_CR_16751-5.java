//<Beginning of snippet n. 0>

import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.HexDump;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

private int status;

/** Specifies if a return of an acknowledgment is requested for send SMS */
private static final int RETURN_NO_ACK  = 0;
private static final int RETURN_ACK     = 1;

private SmsEnvelope mEnvelope;
private BearerData mBearerData;

private static final int STATUS_COMPLETE = 1;
private static final int STATUS_PENDING = 2;
private static final int STATUS_FAILED = 3;
private static final int STATUS_NONE = 0;

/**
 * Returns the status for a previously submitted message.
 * For not interfering with status codes from GSM, this status code is
 * shifted to the bits 31-16.
 */
public int getStatus() {
    return (status << 16);
}

/** Return true iff the bearer data message type is DELIVERY_ACK. */
public boolean isDeliveryAck() {
    if (mBearerData == null) {
        throw new InvalidBearerDataException("BearerData is null");
    }

    if (!mBearerData.messageStatusSet) {
        Log.d(LOG_TAG, "DELIVERY_ACK message without msgStatus (" +
                (userData == null ? "also missing" : "does have") +
                " userData).");
        status = STATUS_NONE;
    } else {
        int errorStatus = mapErrorClassToStatus(mBearerData.errorClass);
        if (errorStatus == STATUS_NONE) {
            status = STATUS_NONE;
        } else {
            status = errorStatus << 8;
            int messageStatusValue = mapMessageStatusToStatus(mBearerData.messageStatus);
            if (messageStatusValue == STATUS_NONE) {
                status |= STATUS_NONE;
            } else {
                status |= messageStatusValue;
            }
        }
    }

    if (mBearerData.messageType != BearerData.MESSAGE_TYPE_DELIVER) {
        throw new UnsupportedMessageTypeException("Unsupported message type: " + mBearerData.messageType);
    }

    validateStatus();
    return true;
}

private void validateStatus() {
    if (status < STATUS_NONE || status > STATUS_FAILED) {
        throw new InvalidStatusException("Invalid status value: " + status);
    }
}

private int mapErrorClassToStatus(Integer errorClass) {
    if (errorClass == null || errorClass < 0 || errorClass > 3) {
        return STATUS_NONE;
    }
    switch (errorClass) {
        case 1: return STATUS_COMPLETE;
        case 2: return STATUS_PENDING;
        case 3: return STATUS_FAILED;
        default: return STATUS_NONE;
    }
}

private int mapMessageStatusToStatus(Integer messageStatus) {
    if (messageStatus == null || messageStatus < 0 || messageStatus > 255) {
        return STATUS_NONE;
    }
    switch (messageStatus) {
        case 0: return STATUS_COMPLETE;
        case 1: return STATUS_PENDING;
        case 2: return STATUS_FAILED;
        default: return STATUS_NONE;
    }
}

//<End of snippet n. 0>