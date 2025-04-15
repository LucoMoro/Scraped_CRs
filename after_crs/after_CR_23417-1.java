/*fixed:for non-telephony device, the getDeviceId() will return null

Change-Id:Idf57959206a9f2d7a5f2deeaf87191bba25d07c0*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3177d894..29fd874

//Synthetic comment -- @@ -81,11 +81,13 @@
(TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
mDestAddr = mTelephonyManager.getLine1Number();
mText = "This is a test message";
        
        String deviceId =mTelephonyManager.getDeviceId();

if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
// CDMA supports SMS delivery report
mDeliveryReportSupported = true;
        } else if (deviceId !=null && deviceId.equals("000000000000000")) {
// emulator doesn't support SMS delivery report
mDeliveryReportSupported = false;
} else {







