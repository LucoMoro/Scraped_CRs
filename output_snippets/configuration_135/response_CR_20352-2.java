//<Beginning of snippet n. 0>


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;

public void testReceiveTextMessage() {
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
    TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    String lineNumber = telephonyManager.getLine1Number();

    if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE || lineNumber == null) {
        Log.w(LOG_TAG, "SMS not sent: Telephony capabilities or line number not valid.");
        return;
    }

    SmsManager smsManager = SmsManager.getDefault();
    smsManager.sendTextMessage(lineNumber, null, "Test SMS", null, null);
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
    mSendIntent = new Intent(SMS_SEND_ACTION);
    mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);
    init();

    TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    String lineNumber = telephonyManager.getLine1Number();

    if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE && lineNumber != null) {
        // Device should not send messages as it lacks capability
        Log.w(LOG_TAG, "Device cannot send messages, telephony capabilities absent but line number exists.");
        return;
    }

    sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
    mSendReceiver.waitForCalls(1, TIME_OUT);
    mDeliveryReceiver.waitForCalls(1, TIME_OUT);

    // send multi parts text sms
    init();
}

//<End of snippet n. 1>