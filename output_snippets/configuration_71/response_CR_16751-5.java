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
    return mapStatus(mBearerData.errorClass, mBearerData.messageStatus);
}

private int mapStatus(int errorClass, int messageStatus) {
    switch (errorClass) {
        case 0:
            switch (messageStatus) {
                case 0:
                    return 0; // COMPLETE
                case 1:
                    return 1; // PENDING
                case 2:
                    return 3; // FAILED
                default:
                    logError("Invalid messageStatus: " + messageStatus);
                    return 4; // NONE
            }
        case 1:
            return 5; // CUSTOM ERROR HANDLING
        default:
            logError("Invalid errorClass: " + errorClass);
            return 4; // NONE
    }
}

private void logError(String message) {
    Log.e(LOG_TAG, message);
}

public void processBearerData() {
    if (!mBearerData.messageStatusSet) {
        Log.d(LOG_TAG, "DELIVERY_ACK message without msgStatus (" +
            (userData == null ? "also missing" : "does have") +
            " userData).");
        status = 0; // COMPLETE
    } else {
        status = mapStatus(mBearerData.errorClass, mBearerData.messageStatus);
    }

    if (mBearerData.messageType != BearerData.MESSAGE_TYPE_DELIVER) {
        throw new RuntimeException("Unsupported message type: " + mBearerData.messageType);
    }
}

//<End of snippet n. 0>