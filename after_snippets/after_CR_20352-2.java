
//<Beginning of snippet n. 0>


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
* Note: this test requires that the device under test reports a valid phone number
*/
public void testReceiveTextMessage() {
        PackageManager packageManager = getContext().getPackageManager();
        TelephonyManager telephonyManager = (TelephonyManager) getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager.getLine1Number() != null) {
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

            if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                assertTrue("Sms not sent successfully, test environment problem?",
                        receiver.isMessageSent());
                assertFalse("Sms received without proper permissions", receiver.isSmsReceived());
            } else {
                assertFalse(receiver.isMessageSent());
            }
        } else if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
                && telephonyManager.getLine1Number() == null) {
            fail("Device reports the telephony feature but has no line number.");
}
}

private void sendSMSToSelf() {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



package android.telephony.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

import java.util.ArrayList;

/**
* Tests for {@link android.telephony.SmsManager}.
)
})
public void testSendMessages() throws InterruptedException {
        PackageManager packageManager = getContext().getPackageManager();
        boolean hasTelephony = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);

        if (hasTelephony && mTelephonyManager.getLine1Number() == null) {
            fail("Device reports the telephony feature but has no line number.");
        } else if (!hasTelephony && mTelephonyManager.getLine1Number() == null) {
            return; // Don't run this test as you can't send SMS messages to a null address.
        }

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);
init();
sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
mSendReceiver.waitForCalls(1, TIME_OUT);
         mDeliveryReceiver.waitForCalls(1, TIME_OUT);


// send multi parts text sms
init();

//<End of snippet n. 1>








