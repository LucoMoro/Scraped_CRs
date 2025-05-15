
//<Beginning of snippet n. 0>


);

private TelephonyManager mTelephonyManager;
    private PackageManager mPackageManager;
private String mDestAddr;
private String mText;
private SmsBroadcastReceiver mSendReceiver;
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
} else {
// is this a GSM network that doesn't support SMS delivery report?
String mccmnc = mTelephonyManager.getSimOperator();
)
})
public void testSendMessages() throws InterruptedException {
        if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
return;
}


//<End of snippet n. 0>








