/*Fix NPE in SmsManagerTest Setup Method

getDeviceId can return null, so check for the telephony feature.
Remove the emulator check since we don't use the emulator to test
CTS at all anymore.

Change-Id:I1611abc36d52424f77c54be8e003cb0066c7aad6*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 15326c0..b916328 100644

//Synthetic comment -- @@ -62,6 +62,7 @@
);

private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;
private SmsBroadcastReceiver mSendReceiver;
//Synthetic comment -- @@ -79,15 +80,15 @@
super.setUp();
mTelephonyManager =
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
//Synthetic comment -- @@ -128,8 +129,7 @@
)
})
public void testSendMessages() throws InterruptedException {
        PackageManager packageManager = mContext.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
return;
}








