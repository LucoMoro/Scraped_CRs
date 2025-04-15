/*add source code to test if the data sms sent on port can be received correctly.

add a receiver on port 19989 to get the data sms and verify if the content can be decode correclty.

Signed-off-by: gzhhong <gzhhong@gmail.com>

	modified:   tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java

Change-Id:I3f609ad58d4e1d6207a853e4ad0221f975af0e26*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
old mode 100755
new mode 100644
//Synthetic comment -- index 7e9af21..f917ddc

//Synthetic comment -- @@ -31,6 +31,8 @@
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Arrays;
//Synthetic comment -- @@ -54,6 +56,7 @@

private static final String SMS_SEND_ACTION = "CTS_SMS_SEND_ACTION";
private static final String SMS_DELIVERY_ACTION = "CTS_SMS_DELIVERY_ACTION";

// List of network operators that don't support SMS delivery report
private static final List<String> NO_DELIVERY_REPORTS =
//Synthetic comment -- @@ -97,11 +100,15 @@
private String mText;
private SmsBroadcastReceiver mSendReceiver;
private SmsBroadcastReceiver mDeliveryReceiver;
private PendingIntent mSentIntent;
private PendingIntent mDeliveredIntent;
private Intent mSendIntent;
private Intent mDeliveryIntent;
private boolean mDeliveryReportSupported;

private static final int TIME_OUT = 1000 * 60 * 5;

//Synthetic comment -- @@ -168,15 +175,21 @@

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);

IntentFilter sendIntentFilter = new IntentFilter(SMS_SEND_ACTION);
IntentFilter deliveryIntentFilter = new IntentFilter(SMS_DELIVERY_ACTION);

mSendReceiver = new SmsBroadcastReceiver(SMS_SEND_ACTION);
mDeliveryReceiver = new SmsBroadcastReceiver(SMS_DELIVERY_ACTION);

getContext().registerReceiver(mSendReceiver, sendIntentFilter);
getContext().registerReceiver(mDeliveryReceiver, deliveryIntentFilter);

// send single text sms
init();
//Synthetic comment -- @@ -202,6 +215,9 @@
if (mDeliveryReportSupported) {
assertTrue(mDeliveryReceiver.waitForCalls(1, TIME_OUT));
}
} else {
// This GSM network doesn't support Data(binary) SMS message.
// Skip the test.
//Synthetic comment -- @@ -232,6 +248,8 @@
private void init() {
mSendReceiver.reset();
mDeliveryReceiver.reset();
mSentIntent = PendingIntent.getBroadcast(getContext(), 0, mSendIntent,
PendingIntent.FLAG_ONE_SHOT);
mDeliveredIntent = PendingIntent.getBroadcast(getContext(), 0, mDeliveryIntent,
//Synthetic comment -- @@ -287,6 +305,23 @@

@Override
public void onReceive(Context context, Intent intent) {
if (intent.getAction().equals(mAction)) {
synchronized (mLock) {
mCalls += 1;







