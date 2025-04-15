/*Fix some bugs in CTS telephony tests for SMS.

- SmsManagerTest was not reporting failure on wait timeout:
   Fix inner SmsBroadcastReceiver class to wait for the correct time interval
   and to return success status in waitForCalls().
- AT&T network doesn't support SMS delivery reports, so don't wait to receive
   them if the SIM card operator is "310410". Other MCC/MNC codes can be added
   to SmsManagerTest.java if we find out that they also ignore
   TP-Status-Report-Request.
- Device emulator also doesn't support SMS delivery reports.
- Remove unnecessary cast in cts/SmsMessageTest.java.
- Add @Override annotation in gsm/cts/SmsManagerTest.java.

Change-Id:Ibf3372adeff6e0ee48c51ae5fcbfd117e696d9e3*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 3f75f94..a97aa96 100644

//Synthetic comment -- @@ -17,12 +17,15 @@
package android.telephony.cts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.telephony.SmsManager;
import android.test.AndroidTestCase;
//Synthetic comment -- @@ -50,6 +53,12 @@
private static final String SMS_SEND_ACTION = "CTS_SMS_SEND_ACTION";
private static final String SMS_DELIVERY_ACTION = "CTS_SMS_DELIVERY_ACTION";

    // List of network operators that don't support SMS delivery report
    private static final List<String> NO_DELIVERY_REPORTS =
            Arrays.asList(
                    "310410"    // AT&T Mobility
            );

private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;
//Synthetic comment -- @@ -59,6 +68,7 @@
private PendingIntent mDeliveredIntent;
private Intent mSendIntent;
private Intent mDeliveryIntent;
    private boolean mDeliveryReportSupported;

private static final int TIME_OUT = 1000 * 60 * 4;

//Synthetic comment -- @@ -69,6 +79,18 @@
(TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
mDestAddr = mTelephonyManager.getLine1Number();
mText = "This is a test message";

        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            // CDMA supports SMS delivery report
            mDeliveryReportSupported = true;
        } else if (mTelephonyManager.getDeviceId().equals("000000000000000")) {
            // emulator doesn't support SMS delivery report
            mDeliveryReportSupported = false;
        } else {
            // is this a GSM network that doesn't support SMS delivery report?
            String mccmnc = mTelephonyManager.getSimOperator();
            mDeliveryReportSupported = !(NO_DELIVERY_REPORTS.contains(mccmnc));
        }
}

@TestTargetNew(
//Synthetic comment -- @@ -120,8 +142,10 @@
// send single text sms
init();
sendTextMessage(mDestAddr, mDestAddr, mSentIntent, mDeliveredIntent);
        assertTrue(mSendReceiver.waitForCalls(1, TIME_OUT));
        if (mDeliveryReportSupported) {
            assertTrue(mDeliveryReceiver.waitForCalls(1, TIME_OUT));
        }

if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
// TODO: temp workaround, OCTET encoding for EMS not properly supported
//Synthetic comment -- @@ -134,8 +158,10 @@

init();
sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
        assertTrue(mSendReceiver.waitForCalls(1, TIME_OUT));
        if (mDeliveryReportSupported) {
            assertTrue(mDeliveryReceiver.waitForCalls(1, TIME_OUT));
        }

// send multi parts text sms
init();
//Synthetic comment -- @@ -148,8 +174,10 @@
deliveryIntents.add(PendingIntent.getBroadcast(getContext(), 0, mDeliveryIntent, 0));
}
sendMultiPartTextMessage(mDestAddr, parts, sentIntents, deliveryIntents);
        assertTrue(mSendReceiver.waitForCalls(numParts, TIME_OUT));
        if (mDeliveryReportSupported) {
            assertTrue(mDeliveryReceiver.waitForCalls(numParts, TIME_OUT));
        }
}

private void init() {
//Synthetic comment -- @@ -220,12 +248,20 @@
}
}

        public boolean waitForCalls(int expectedCalls, long timeout) throws InterruptedException {
synchronized(mLock) {
mExpectedCalls = expectedCalls;
                long startTime = SystemClock.elapsedRealtime();

                while (mCalls < mExpectedCalls) {
                    long waitTime = timeout - (SystemClock.elapsedRealtime() - startTime);
                    if (waitTime > 0) {
                        mLock.wait(waitTime);
                    } else {
                        return false;  // timed out
                    }
}
                return true;  // success
}
}
}








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsMessageTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsMessageTest.java
//Synthetic comment -- index 44b0871..515f8b5 100644

//Synthetic comment -- @@ -212,7 +212,7 @@
assertEquals(SCA2, sms.getServiceCenterAddress());
assertEquals(OA2, sms.getOriginatingAddress());
assertEquals(MESSAGE_BODY2, sms.getMessageBody());
        CharSequence msgBody = sms.getMessageBody();
result = SmsMessage.calculateLength(msgBody, false);
assertEquals(SMS_NUMBER2, result[0]);
assertEquals(sms.getMessageBody().length(), result[1]);








//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/gsm/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/gsm/cts/SmsManagerTest.java
//Synthetic comment -- index 8dc6936..512e89d 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
@TestTargetClass(SmsManager.class)
public class SmsManagerTest extends android.telephony.cts.SmsManagerTest {

    @Override
@TestTargetNew(
level = TestLevel.COMPLETE,
method = "getDefault",







