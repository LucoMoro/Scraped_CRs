```
<<Beginning of snippet n. 0>>
import android.test.AndroidTestCase;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.telephony.SubscriptionManager;

import java.util.ArrayList;

private static final String SMS_SEND_ACTION = "CTS_SMS_SEND_ACTION";
private static final String SMS_DELIVERY_ACTION = "CTS_SMS_DELIVERY_ACTION";

private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;

@Override
protected void setUp() throws Exception {
    super.setUp();
    mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
}

private boolean isNTTDOCOMO() {
    if (mTelephonyManager != null) {
        String operator = mTelephonyManager.getNetworkOperator();
        return operator != null && operator.equals("44010");
    }
    return false;
}

private void sendDataSMS() throws Exception {
    if (isNTTDOCOMO()) {
        Log.d("SMS", "Skipping sendDataSMS on NTT DOCOMO network.");
        return;
    }

    byte[] data = mText.getBytes();
    short port = 19989;

    init();
    try {
        sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
        mSendReceiver.waitForCalls(1, TIME_OUT);
        mDeliveryReceiver.waitForCalls(1, TIME_OUT);
    } catch (Exception e) {
        Log.e("SMS", "Error sending data SMS: ", e);
    }
}

private void sendMultiPartSMS() throws Exception {
    if (isNTTDOCOMO()) {
        Log.d("SMS", "Skipping sendMultiPartSMS on NTT DOCOMO network.");
        return;
    }

    init();
    ArrayList<String> parts = divideMessage(LONG_TEXT);
    int numParts = parts.size();
    ArrayList<PendingIntent> sentIntents = new ArrayList<>();
    ArrayList<PendingIntent> deliveryIntents = new ArrayList<>();
    for (int i = 0; i < numParts; i++) {
        sentIntents.add(PendingIntent.getBroadcast(getContext(), 0, new Intent(SMS_SEND_ACTION), PendingIntent.FLAG_UPDATE_CURRENT));
        deliveryIntents.add(PendingIntent.getBroadcast(getContext(), 0, new Intent(SMS_DELIVERY_ACTION), PendingIntent.FLAG_UPDATE_CURRENT));
    }
    try {
        sendMultiPartTextMessage(mDestAddr, parts, sentIntents, deliveryIntents);
        mSendReceiver.waitForCalls(numParts, TIME_OUT);
        mDeliveryReceiver.waitForCalls(numParts, TIME_OUT);
    } catch (Exception e) {
        Log.e("SMS", "Error sending multi-part SMS: ", e);
    }
}

private void init() {
}

//<End of snippet n. 0>