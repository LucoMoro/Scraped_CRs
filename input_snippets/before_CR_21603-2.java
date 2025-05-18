
//<Beginning of snippet n. 0>


);

private TelephonyManager mTelephonyManager;
private String mDestAddr;
private String mText;
private SmsBroadcastReceiver mSendReceiver;
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
)
})
public void testSendMessages() throws InterruptedException {
        PackageManager packageManager = mContext.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
return;
}


//<End of snippet n. 0>








