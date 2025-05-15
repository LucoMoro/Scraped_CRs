
//<Beginning of snippet n. 0>


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



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
)
})
public void testSendMessages() throws InterruptedException {

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);
init();
sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
mSendReceiver.waitForCalls(1, TIME_OUT);
        mDeliveryReceiver.waitForCalls(1, TIME_OUT);

// send multi parts text sms
init();

//<End of snippet n. 1>








