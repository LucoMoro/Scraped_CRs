/*Modified so that if network does not support multipart / data SMS, it skips this test.

If the network is "NTT DOCOMO" (MCC+MNC = 44010), test on sending SMS of SendDataMessage and SendMultipartTextMessage always gives timeouts since NTT DOCOMO does not support Multipart / Data SMS.

I modified so that if the network was NTT DOCOMO it will skip this test.*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index deb2abe..c201fb7 100644

//Synthetic comment -- @@ -32,6 +32,8 @@
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* Tests for {@link android.telephony.SmsManager}.
//Synthetic comment -- @@ -52,6 +54,18 @@
private static final String SMS_SEND_ACTION = "CTS_SMS_SEND_ACTION";
private static final String SMS_DELIVERY_ACTION = "CTS_SMS_DELIVERY_ACTION";

    // List of network operators that doesn't support Data(binary) SMS message
    private static final List<String> UNSUPPORT_DATA_SMS_MESSAGES =
            Arrays.asList(
                    "44010"    // NTT DOCOMO
            );

    // List of network operators that doesn't support Maltipart SMS message
    private static final List<String> UNSUPPORT_MULTIPART_SMS_MESSAGES =
            Arrays.asList(
                    "44010"    // NTT DOCOMO
            );

private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;
//Synthetic comment -- @@ -111,6 +125,8 @@
return;
}

        String mccmnc = mTelephonyManager.getSimOperator();

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);

//Synthetic comment -- @@ -135,27 +151,37 @@
}

// send data sms
        if (!UNSUPPORT_DATA_SMS_MESSAGES.contains(mccmnc)) {
            byte[] data = mText.getBytes();
            short port = 19989;

            init();
            sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
            mSendReceiver.waitForCalls(1, TIME_OUT);
            mDeliveryReceiver.waitForCalls(1, TIME_OUT);
        } else {
            // This GSM network doesn't support Data(binary) SMS message.
            // Skip the test.
        }

// send multi parts text sms
        if (!UNSUPPORT_MULTIPART_SMS_MESSAGES.contains(mccmnc)) {
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
        } else {
            // This GSM network doesn't support Multipart SMS message.
            // Skip the test.
}
}

private void init() {







