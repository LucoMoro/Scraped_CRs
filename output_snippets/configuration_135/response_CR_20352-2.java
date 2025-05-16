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
    
    TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE && telephonyManager.getLine1Number() != null) {
        sendSMSToSelf();
    } else {
        assertFalse("Sms received without proper telephony capabilities or line number", receiver.isSmsReceived());
    }

    synchronized(receiver) {
        try {
            receiver.wait(WAIT_TIME);
        } catch (InterruptedException e) {
            Log.w(LOG_TAG, "wait for sms interrupted");
        }
    }
    assertTrue("Sms not sent successfully, test environment problem?", receiver.isMessageSent());
    assertFalse("Sms received without proper permissions", receiver.isSmsReceived());
}

private void sendSMSToSelf() {
    TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE && telephonyManager.getLine1Number() != null) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(telephonyManager.getLine1Number(), null, "Test SMS", null, null);
    }
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

public void testSendMessages() throws InterruptedException {
    mSendIntent = new Intent(SMS_SEND_ACTION);
    mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);
    init();

    TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE && telephonyManager.getLine1Number() != null) {
        sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
        mSendReceiver.waitForCalls(1, TIME_OUT);
        mDeliveryReceiver.waitForCalls(1, TIME_OUT);
    } else {
        assertFalse("Sms cannot be sent without proper telephony capabilities or line number", true);
    }
    
    // send multi parts text sms
    init();
}
//<End of snippet n. 1>