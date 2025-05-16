//<Beginning of snippet n. 0>
private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;
private SmsBroadcastReceiver mSendReceiver;
super.setUp();
mTelephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

if (mTelephonyManager != null) {
    mDestAddr = mTelephonyManager.getLine1Number();
    mText = "This is a test message";

    if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
        mDeliveryReportSupported = true;
    } else if (mTelephonyManager.getDeviceId() != null && mTelephonyManager.getDeviceId().equals("000000000000000")) {
        mDeliveryReportSupported = false;
    } else {
        String mccmnc = mTelephonyManager.getSimOperator();
    }
}
}
//<End of snippet n. 0>