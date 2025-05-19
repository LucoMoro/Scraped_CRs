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
    
    if (telephonyManager == null || telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
        return; // No telephony support
    }

    String lineNumber = telephonyManager.getLine1Number();
    if (lineNumber == null || lineNumber.isEmpty()) {
        fail("Line number is required when telephony is supported.");
        return; // No line number present
    }

    IllegalSmsReceiver receiver = new IllegalSmsReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction(TELEPHONY_SMS_RECEIVED);
    filter.addAction(MESSAGE_SENT_ACTION);
    filter.addAction(MESSAGE_STATUS_RECEIVED_ACTION);

    getContext().registerReceiver(receiver, filter);
    try {
        sendSMSToSelf();
        synchronized (receiver) {
            receiver.wait(WAIT_TIME);
        }

        assertTrue("Sms not sent successfully, test environment problem?", receiver.isMessageSent());
        assertFalse("Sms received without proper permissions", receiver.isSmsReceived());
        mDeliveryReceiver.waitForCalls(1, TIME_OUT);
    } catch (InterruptedException e) {
        Log.w(LOG_TAG, "wait for sms interrupted");
    } finally {
        getContext().unregisterReceiver(receiver);
    }
}

private void sendSMSToSelf() {
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
import android.util.Log;

public void testSendMessages() throws InterruptedException {
    TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    
    String lineNumber = null;
    if (telephonyManager != null && telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE) {
        lineNumber = telephonyManager.getLine1Number();
    } 

    if (lineNumber == null || lineNumber.isEmpty()) {
        fail("Line number is required when telephony is supported.");
        return; // No line number present
    }

    mSendIntent = new Intent(SMS_SEND_ACTION);
    mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);
    init();
    
    SmsManager smsManager = SmsManager.getDefault();
    smsManager.sendTextMessage(lineNumber, null, "Test Message", PendingIntent.getBroadcast(getContext(), 0, mSendIntent, 0), PendingIntent.getBroadcast(getContext(), 0, mDeliveryIntent, 0));

    mSendReceiver.waitForCalls(1, TIME_OUT);
    mDeliveryReceiver.waitForCalls(1, TIME_OUT);

    // Confirm no messages received if telephony is unsupported
    if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
        assertFalse("Messages should not be received when telephony is absent", mDeliveryReceiver.isMessageReceived());
    }
    
    assertTimeout(); // Assuming timeout assertions needed
}
//<End of snippet n. 1>