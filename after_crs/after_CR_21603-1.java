/*Fix NPE in SmsManagerTest Setup Method

getDeviceId can return null, so check for the telephony feature.

Change-Id:I1611abc36d52424f77c54be8e003cb0066c7aad6*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 3177d894..9daf7fb 100644

//Synthetic comment -- @@ -73,16 +73,20 @@
private boolean mDeliveryReportSupported;

private static final int TIME_OUT = 1000 * 60 * 4;
    private PackageManager mPackageManager;

@Override
protected void setUp() throws Exception {
super.setUp();
mTelephonyManager =
(TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        mPackageManager = mContext.getPackageManager();
mDestAddr = mTelephonyManager.getLine1Number();
mText = "This is a test message";

        if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            mDeliveryReportSupported = false;
        } else if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
// CDMA supports SMS delivery report
mDeliveryReportSupported = true;
} else if (mTelephonyManager.getDeviceId().equals("000000000000000")) {
//Synthetic comment -- @@ -128,8 +132,7 @@
)
})
public void testSendMessages() throws InterruptedException {
        if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
return;
}








