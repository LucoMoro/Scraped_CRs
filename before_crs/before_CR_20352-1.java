/*Fix SMS Tests for Non-telephony Devices

Change the assertions to check that no messages come back when sending
the SMS messages to the device. These tests will now timeout when
waiting for the messages which indicates success.

Change-Id:I449e0ad617bb4d5af68ade21319c483d57c140da*/
//Synthetic comment -- diff --git a/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java b/tests/tests/permission2/src/android/permission2/cts/NoReceiveSmsPermissionTest.java
//Synthetic comment -- index 7b14db7..f0f4b6f 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.test.AndroidTestCase;
//Synthetic comment -- @@ -71,9 +72,15 @@
Log.w(LOG_TAG, "wait for sms interrupted");
}
}
        assertTrue("Sms not sent successfully, test environment problem?",
                receiver.isMessageSent());
        assertFalse("Sms received without proper permissions", receiver.isSmsReceived());
}

private void sendSMSToSelf() {








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 3f75f94..b36ab83 100644

//Synthetic comment -- @@ -17,6 +17,10 @@
package android.telephony.cts;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
//Synthetic comment -- @@ -24,12 +28,14 @@
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
//Synthetic comment -- @@ -104,6 +110,8 @@
)
})
public void testSendMessages() throws InterruptedException {

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);
//Synthetic comment -- @@ -120,8 +128,10 @@
// send single text sms
init();
sendTextMessage(mDestAddr, mDestAddr, mSentIntent, mDeliveredIntent);
        mSendReceiver.waitForCalls(1, TIME_OUT);
        mDeliveryReceiver.waitForCalls(1, TIME_OUT);

if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
// TODO: temp workaround, OCTET encoding for EMS not properly supported
//Synthetic comment -- @@ -134,8 +144,10 @@

init();
sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
        mSendReceiver.waitForCalls(1, TIME_OUT);
        mDeliveryReceiver.waitForCalls(1, TIME_OUT);

// send multi parts text sms
init();
//Synthetic comment -- @@ -148,8 +160,10 @@
deliveryIntents.add(PendingIntent.getBroadcast(getContext(), 0, mDeliveryIntent, 0));
}
sendMultiPartTextMessage(mDestAddr, parts, sentIntents, deliveryIntents);
        mSendReceiver.waitForCalls(numParts, TIME_OUT);
        mDeliveryReceiver.waitForCalls(numParts, TIME_OUT);
}

private void init() {







