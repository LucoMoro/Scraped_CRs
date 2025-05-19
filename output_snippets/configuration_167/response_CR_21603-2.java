//<Beginning of snippet n. 0>
private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;
private SmsBroadcastReceiver mSendReceiver;
super.setUp();
mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

if (mTelephonyManager != null && getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
    mDestAddr = mTelephonyManager.getLine1Number();
    mText = "This is a test message";

    int phoneType = mTelephonyManager.getPhoneType();
    mDeliveryReportSupported = (phoneType == TelephonyManager.PHONE_TYPE_CDMA);

    String mccmnc = mTelephonyManager.getSimOperator();
    if (mccmnc != null && mDestAddr != null) {
        // Handle GSM network logic if needed
    }
}
//<End of snippet n. 0>