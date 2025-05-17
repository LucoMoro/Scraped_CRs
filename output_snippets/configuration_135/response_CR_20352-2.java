//<Beginning of snippet n. 0>
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
import android.util.Log;

public void testReceiveTextMessage() {
    TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager != null && telephonyManager.getPhoneCount() > 0) {
        String line1Number = telephonyManager.getLine1Number();
        if (line1Number == null) {
            fail("Device has telephony capabilities but no line number.");
        }
        
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
        
        assertTrue("Sms not sent successfully, test environment problem?", receiver.isMessageSent());
        assertFalse("Sms received without proper permissions", receiver.isSmsReceived());
    } else {
        fail("No telephony capabilities on this device.");
    }
}

private void sendSMSToSelf() {
    // Implementation of SMS sending
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
package android.telephony.cts;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.telephony.SmsManager;
import android.test.AndroidTestCase;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

public void testSendMessages() throws InterruptedException {
    TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager != null && telephonyManager.getPhoneCount() > 0) {
        String line1Number = telephonyManager.getLine1Number();
        if (line1Number == null) {
            fail("Device has telephony capabilities but no line number.");
        }

        mSendIntent = new Intent(SMS_SEND_ACTION);
        mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);
        init();
        sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);

        if (!mSendReceiver.waitForCalls(1, TIME_OUT)) {
            fail("Timed out waiting for message send callback.");
        }
        if (!mDeliveryReceiver.waitForCalls(1, TIME_OUT)) {
            fail("Timed out waiting for delivery callback.");
        }
    } else {
        fail("Device does not have telephony capabilities.");
    }
}
//<End of snippet n. 1>