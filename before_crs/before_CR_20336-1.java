/*Huawei fix for send message tests failing on Mobile Internet Devices, Personal Media Players and other non-phone devices

Change-Id:I66d275396f8d81a4a022fb5efb16980ae6cadb56*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index a97aa96..9e86f98 100644

//Synthetic comment -- @@ -80,7 +80,10 @@
mDestAddr = mTelephonyManager.getLine1Number();
mText = "This is a test message";

        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
// CDMA supports SMS delivery report
mDeliveryReportSupported = true;
} else if (mTelephonyManager.getDeviceId().equals("000000000000000")) {
//Synthetic comment -- @@ -127,6 +130,11 @@
})
public void testSendMessages() throws InterruptedException {

mSendIntent = new Intent(SMS_SEND_ACTION);
mDeliveryIntent = new Intent(SMS_DELIVERY_ACTION);








