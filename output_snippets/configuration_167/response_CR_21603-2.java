//<Beginning of snippet n. 0>


);

private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;
private SmsBroadcastReceiver mSendReceiver;
super.setUp();
mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
mDestAddr = mTelephonyManager.getLine1Number();
mText = "This is a test message";

if (mTelephonyManager != null && mTelephonyManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
    if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
        // CDMA supports SMS delivery report
        mDeliveryReportSupported = true;
    } else {
        String deviceId = mTelephonyManager.getDeviceId();
        if (deviceId != null && !deviceId.equals("000000000000000")) {
            // Handle GSM network case or real devices
            String mccmnc = mTelephonyManager.getSimOperator();
            // Implement logic based on mccmnc if necessary
        } else {
            mDeliveryReportSupported = false; // emulator doesn't support SMS delivery report
        }
    }
}


//<End of snippet n. 0>