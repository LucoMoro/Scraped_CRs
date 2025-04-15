/*fix sms testcases for non-telephony devices

send/receive sms testing should be disabled for non-telephony devices.

Change-Id:I9e88d3443207eb8f8deb21b8222686be87125edb*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 3f75f94..64ab056 100644

//Synthetic comment -- @@ -105,6 +105,11 @@
})
public void testSendMessages() throws InterruptedException {

        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
 	   // TODO: temp workaround, this test is not applicable for non-telephony devices
           return;
        }

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);








