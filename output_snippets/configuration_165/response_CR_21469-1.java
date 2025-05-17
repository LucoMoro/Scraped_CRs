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

public void testSendSms() {
    if (isUnsupportedNetwork()) {
        return;
    }

    Intent mSendIntent = new Intent(SMS_SEND_ACTION);
    Intent mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);

    sendDataSms(mSendIntent, mDeliveryIntent);
    sendMultiPartSms(mSendIntent, mDeliveryIntent);
}

private boolean isUnsupportedNetwork() {
    return "44010".equals(mTelephonyManager.getNetworkOperator());
}

private void sendDataSms(Intent mSendIntent, Intent mDeliveryIntent) {
    byte[] data = mText.getBytes();
    short port = 19989;

    init();
    sendDataMessage(mDestAddr, port, data, mSendIntent, mDeliveryIntent);
    mSendReceiver.waitForCalls(1, TIME_OUT);
    mDeliveryReceiver.waitForCalls(1, TIME_OUT);
}

private void sendMultiPartSms(Intent mSendIntent, Intent mDeliveryIntent) {
    init();
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

//<End of snippet n. 0>