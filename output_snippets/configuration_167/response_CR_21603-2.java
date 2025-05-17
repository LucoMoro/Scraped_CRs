```
<Beginning of snippet n. 0>
private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;
private SmsBroadcastReceiver mSendReceiver;
super.setUp();
PackageManager packageManager = mContext.getPackageManager();
if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
    mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

    if (mTelephonyManager != null) {
        mDestAddr = mTelephonyManager.getLine1Number();
        mText = "This is a test message";

        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            mDeliveryReportSupported = true;
        } else {
            String deviceId = mTelephonyManager.getDeviceId();
            if (deviceId == null) {
                mDeliveryReportSupported = false;
            } else {
                String mccmnc = mTelephonyManager.getSimOperator();
            }
        }
    }
}
public void testSendMessages() throws InterruptedException {
    PackageManager packageManager = mContext.getPackageManager();
    if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
        return;
    }
}
<End of snippet n. 0>