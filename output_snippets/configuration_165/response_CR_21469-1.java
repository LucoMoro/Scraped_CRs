//<Beginning of snippet n. 0>


import android.test.AndroidTestCase;
import android.telephony.TelephonyManager;
import android.content.Intent;
import java.util.ArrayList;

/**
* Tests for {@link android.telephony.SmsManager}.
*/
private static final String SMS_SEND_ACTION = "CTS_SMS_SEND_ACTION";
private static final String SMS_DELIVERY_ACTION = "CTS_SMS_DELIVERY_ACTION";

private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;

private boolean isNetworkNTTDOCOMO() {
    // Method to check if the current network is "NTT DOCOMO" based on MCC+MNC
    // Example implementation to determine if the network is NTT DOCOMO
    // Assume mTelephonyManager is initialized and ready to use.
    // Replace this comment with proper logic to make the check,
    // return true if MCC+MNC matches 44010, else return false.
}

private void testSendSms() {
    if (isNetworkNTTDOCOMO()) {
        return; // Skip tests for NTT DOCOMO network
    }

    Intent mSendIntent = new Intent(SMS_SEND_ACTION);
    Intent mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);

    // Send data SMS
    byte[] data = mText.getBytes();
    short port = 19989;

    init();
    sendDataMessage(mDestAddr, port, data, mSendIntent, mDeliveryIntent);
    mSendReceiver.waitForCalls(1, TIME_OUT);
    mDeliveryReceiver.waitForCalls(1, TIME_OUT);

    // Send multi parts text SMS
    ArrayList<String> parts = divideMessage(LONG_TEXT);
    int numParts = parts.size();
    ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
    ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
    for (int i = 0; i < numParts; i++) {
        sentIntents.add(PendingIntent.getBroadcast(getContext(), 0, mSendIntent, 0));
        deliveryIntents.add(PendingIntent.getBroadcast(getContext(), 0, mDeliveryIntent, 0));
    }
    sendMultiPartTextMessage(mDestAddr, parts, sentIntents, deliveryIntents);
    mSendReceiver.waitForCalls(numParts, TIME_OUT);
    mDeliveryReceiver.waitForCalls(numParts, TIME_OUT);
}

private void init() {
    // Initialization code here, should not be called if SMS tests are skipped
}

//<End of snippet n. 0>