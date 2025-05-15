//<Beginning of snippet n. 0>

private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;
private SmsBroadcastReceiver mSendReceiver;
super.setUp();
mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

// Check for telephony service presence
if (mTelephonyManager != null && packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
    mDestAddr = mTelephonyManager.getLine1Number();
    mText = "This is a test message";

    if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
        // CDMA supports SMS delivery report
        mDeliveryReportSupported = true;
    } else if (mTelephonyManager.getDeviceId() != null) {
        // Assume valid Device ID check
        String deviceId = mTelephonyManager.getDeviceId();
        mDeliveryReportSupported = !deviceId.equals("000000000000000");
    } else {
        // Handle cases where Device ID is null
        mDeliveryReportSupported = false;
    }

    String mccmnc = mTelephonyManager.getSimOperator();
} else {
    // Handle absence of telephony services
    mDestAddr = null;
    mText = null;
}

//<End of snippet n. 0>