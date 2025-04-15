/*Fix the problem that the wrong message is shown if this activty is re-created
after the uninstallation is done.

Change-Id:I2d3b052a3b8f8ea328ba05861d84b1f7fa6d5f0b*/




//Synthetic comment -- diff --git a/src/com/android/packageinstaller/UninstallAppProgress.java b/src/com/android/packageinstaller/UninstallAppProgress.java
//Synthetic comment -- index 42d9937..a96bb1b 100755

//Synthetic comment -- @@ -48,11 +48,14 @@
private ProgressBar mProgressBar;
private View mOkPanel;
private volatile int mResultCode = -1;
    // preserve the result of an uninstallation
    private Integer mStatus = null;
private final int UNINSTALL_COMPLETE = 1;
public final static int SUCCEEDED=1;
public final static int FAILED=0;
private Handler mHandler = new Handler() {
public void handleMessage(Message msg) {
            mStatus = msg.arg1;
switch (msg.what) {
case UNINSTALL_COMPLETE:
mResultCode = msg.arg1;
//Synthetic comment -- @@ -75,6 +78,16 @@
@Override
public void onCreate(Bundle icicle) {
super.onCreate(icicle);
        /**
         * Use the preserved result of the recent uninstallation
         * to show the correct information even if this Activty instance is re-created.
         */
        if (icicle != null && icicle.containsKey("UNINSTALL_RESULT")) {
            mStatus = icicle.getInt("UNINSTALL_RESULT");
            Message msg = mHandler.obtainMessage(UNINSTALL_COMPLETE);
            msg.arg1 = (Integer)icicle.getInt("UNINSTALL_RESULT");
            mHandler.sendMessage(msg);
        }
Intent intent = getIntent();
mAppInfo = intent.getParcelableExtra(PackageUtil.INTENT_ATTR_APPLICATION_INFO);
initView();
//Synthetic comment -- @@ -91,6 +104,7 @@
void setResultAndFinish(int retCode) {
setResult(retCode);
finish();
        mStatus = null;
}

public void initView() {
//Synthetic comment -- @@ -107,6 +121,9 @@
mOkButton = (Button)findViewById(R.id.ok_button);
mOkButton.setOnClickListener(this);
mOkPanel.setVisibility(View.INVISIBLE);
        if (mStatus != null) {
            return;
        }
PackageDeleteObserver observer = new PackageDeleteObserver();
getPackageManager().deletePackage(mAppInfo.packageName, observer, 0);
}
//Synthetic comment -- @@ -131,4 +148,13 @@
}
return super.dispatchKeyEvent(ev);
}

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Backup the result of the current uninstallation
        if (mStatus != null) {
            outState.putInt("UNINSTALL_RESULT", mStatus);
        }
    }
}







