
//<Beginning of snippet n. 0>


import android.test.AndroidTestCase;

import java.util.ArrayList;

/**
* Tests for {@link android.telephony.SmsManager}.
private static final String SMS_SEND_ACTION = "CTS_SMS_SEND_ACTION";
private static final String SMS_DELIVERY_ACTION = "CTS_SMS_DELIVERY_ACTION";

private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;
return;
}

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);

}

// send data sms
        byte[] data = mText.getBytes();
        short port = 19989;

        init();
        sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
        mSendReceiver.waitForCalls(1, TIME_OUT);
        mDeliveryReceiver.waitForCalls(1, TIME_OUT);

// send multi parts text sms
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








