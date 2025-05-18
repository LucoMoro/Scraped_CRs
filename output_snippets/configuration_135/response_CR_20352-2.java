//<Beginning of snippet n. 0>


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.test.AndroidTestCase;

public void testReceiveTextMessage() {
    TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager == null || !telephonyManager.isSmsCapable()) {
        Log.w(LOG_TAG, "Device does not support SMS capabilities. Test cannot proceed.");
        return;
    }

    String lineNumber = telephonyManager.getLine1Number();
    if (lineNumber == null) {
        fail("Telephony is supported but no valid line number found.");
    }

    IllegalSmsReceiver receiver = new IllegalSmsReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction(TELEPHONY_SMS_RECEIVED);
    filter.addAction(MESSAGE_SENT_ACTION);
    filter.addAction(MESSAGE_STATUS_RECEIVED_ACTION);

    getContext().registerReceiver(receiver, filter);
    try {
        if (lineNumber != null && !lineNumber.isEmpty()) {
            sendSMSToSelf();
            synchronized (receiver) {
                try {
                    receiver.wait(WAIT_TIME);
                } catch (InterruptedException e) {
                    Log.w(LOG_TAG, "Wait for SMS interrupted");
                }
            }
            assertTrue("SMS not sent successfully, test environment problem?",
                    receiver.isMessageSent());
            assertFalse("SMS received without proper permissions", receiver.isSmsReceived());
        } else {
            Log.w(LOG_TAG, "Telephony is absent but a valid line number exists.");
        }
    } finally {
        getContext().unregisterReceiver(receiver);
    }
}

private void sendSMSToSelf() {
    // Implementation remains here to send SMS
}

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

public void testSendMessages() throws InterruptedException {
    TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager == null || !telephonyManager.isSmsCapable()) {
        Log.w(LOG_TAG, "Device does not support SMS capabilities. Test cannot proceed.");
        return;
    }

    String lineNumber = telephonyManager.getLine1Number();
    if (lineNumber == null) {
        fail("Telephony is supported but no valid line number found. SMS sending is blocked.");
    } else if (lineNumber.length() > 0) {
        mSendIntent = new Intent(SMS_SEND_ACTION);
        mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);
        init();
        sendDataMessage(mDestAddr, port, data, mSendIntent, mDeliveryIntent);
        
        mSendReceiver.waitForCalls(1, TIME_OUT);
        mDeliveryReceiver.waitForCalls(1, TIME_OUT);
        
        assertTrue("SMS was expected to be sent", mSendReceiver.hasMessages());
    }
    
    // Handle case when line number is invalid
    Log.w(LOG_TAG, "Line number is invalid; SMS sending is not executed.");

    // send multi parts text sms
    init();
}

//<End of snippet n. 1>