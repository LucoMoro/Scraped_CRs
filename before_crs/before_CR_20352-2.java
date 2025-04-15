/*Fix SMS Tests for Non-telephony Devices

Don't send the test SMS if the device doesn't have a line number. Fail
though if the device says it has telephony but doesn't have a line
number. If the device doesn't report having telephony but has a line
number, then check that no messages come back when sending
the SMS messages to the device. These tests will now timeout when
waiting for the messages which indicates success.

Change-Id:I449e0ad617bb4d5af68ade21319c483d57c140da*/
//Synthetic comment -- diff --git a/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java b/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java
//Synthetic comment -- index 7b14db7..b0c7b49 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
//Synthetic comment -- @@ -51,29 +52,43 @@
* Note: this test requires that the device under test reports a valid phone number
*/
public void testReceiveTextMessage() {
        // register our test receiver to receive SMSs. This won't throw a SecurityException,
        // so test needs to wait to determine if it actual receives an SMS
        // admittedly, this is a weak verification
        // this test should be used in conjunction with a test that verifies an SMS can be
        // received successfully using the same logic if all permissions are in place
        IllegalSmsReceiver receiver = new IllegalSmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TELEPHONY_SMS_RECEIVED);
        filter.addAction(MESSAGE_SENT_ACTION);
        filter.addAction(MESSAGE_STATUS_RECEIVED_ACTION);

        getContext().registerReceiver(receiver, filter);
        sendSMSToSelf();
        synchronized(receiver) {
            try {
                receiver.wait(WAIT_TIME);
            } catch (InterruptedException e) {
                Log.w(LOG_TAG, "wait for sms interrupted");
}
}
        assertTrue("Sms not sent successfully, test environment problem?",
                receiver.isMessageSent());
        assertFalse("Sms received without proper permissions", receiver.isSmsReceived());
}

private void sendSMSToSelf() {








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 3f75f94..3c432a9 100644

//Synthetic comment -- @@ -16,20 +16,22 @@

package android.telephony.cts;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.telephony.SmsManager;
import android.test.AndroidTestCase;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

/**
* Tests for {@link android.telephony.SmsManager}.
//Synthetic comment -- @@ -104,6 +106,14 @@
)
})
public void testSendMessages() throws InterruptedException {

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);
//Synthetic comment -- @@ -135,7 +145,8 @@
init();
sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
mSendReceiver.waitForCalls(1, TIME_OUT);
        mDeliveryReceiver.waitForCalls(1, TIME_OUT);

// send multi parts text sms
init();







