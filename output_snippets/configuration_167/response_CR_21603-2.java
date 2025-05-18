//<Beginning of snippet n. 0>
private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;
private SmsBroadcastReceiver mSendReceiver;
super.setUp();
mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

if (mTelephonyManager != null) {
    PackageManager packageManager = getContext().getPackageManager();
    if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
        mDestAddr = mTelephonyManager.getLine1Number();
        mText = "This is a test message";

        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            mDeliveryReportSupported = true;
        } else {
            String deviceId = mTelephonyManager.getDeviceId(); // Removed Emulator-specific checks
            if (deviceId != null) {
                String mccmnc = mTelephonyManager.getSimOperator();
                if (mccmnc != null && !mccmnc.isEmpty()) {
                    // Additional handling for mccmnc can be done here
                }
            } else {
                mDeliveryReportSupported = false;
            }
        }
    }
}

public void testSendMessages() throws InterruptedException {
    PackageManager packageManager = mContext.getPackageManager();
    if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
        return;
    }
//<End of snippet n. 0>