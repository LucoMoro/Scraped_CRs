//<Beginning of snippet n. 0>

import android.test.AndroidTestCase;
import android.telephony.TelephonyManager;
import android.content.Intent;
import android.app.PendingIntent;

import java.util.ArrayList;

/**
* Tests for {@link android.telephony.SmsManager}.
*/
private static final String SMS_SEND_ACTION = "CTS_SMS_SEND_ACTION";
private static final String SMS_DELIVERY_ACTION = "CTS_SMS_DELIVERY_ACTION";

private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;

private void sendSmsTest() {
    if (isNetworkTypeNotDocomo()) {
        sendDataSms();
        sendMultiPartTextSms();
    }
}

private boolean isNetworkTypeNotDocomo() {
    // Assuming getNetworkOperator() returns a string in the format "MCCMNC" 
    String networkOperator = mTelephonyManager.getNetworkOperator();
    return !(networkOperator != null && networkOperator.equals("44010"));
}

// send data sms
private void sendDataSms() {
    byte[] data = mText.getBytes();
    short port = 19989;

    init();
    sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
    mSendReceiver.waitForCalls(1, TIME_OUT);
}

// send multi parts text sms
private void sendMultiPartTextSms() {
    init();
    ArrayList<String> parts = divideMessage(LONG_TEXT);
    int numParts = parts.size();
    ArrayList<PendingIntent> sentIntents = new ArrayList<>();
    ArrayList<PendingIntent> deliveryIntents = new ArrayList<>();
    for (int i = 0; i < numParts; i++) {
        sentIntents.add(PendingIntent.getBroadcast(getContext(), 0, mSendIntent, 0));
        deliveryIntents.add(PendingIntent.getBroadcast(getContext(), 0, mDeliveryIntent, 0));
    }
    sendMultiPartTextMessage(mDestAddr, parts, sentIntents, deliveryIntents);
    mSendReceiver.waitForCalls(numParts, TIME_OUT);
}

private void init() {
    // Initialization logic here
}

//<End of snippet n. 0>