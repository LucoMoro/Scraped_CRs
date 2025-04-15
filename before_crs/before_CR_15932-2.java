/*Fix the problem that the wrong message is shown if this activty is re-created
after the uninstallation is done.

Change-Id:I2d3b052a3b8f8ea328ba05861d84b1f7fa6d5f0b*/
//Synthetic comment -- diff --git a/src/com/android/packageinstaller/UninstallAppProgress.java b/src/com/android/packageinstaller/UninstallAppProgress.java
//Synthetic comment -- index 42d9937..6b85993 100755

//Synthetic comment -- @@ -48,11 +48,14 @@
private ProgressBar mProgressBar;
private View mOkPanel;
private volatile int mResultCode = -1;
private final int UNINSTALL_COMPLETE = 1;
public final static int SUCCEEDED=1;
public final static int FAILED=0;
private Handler mHandler = new Handler() {
public void handleMessage(Message msg) {
switch (msg.what) {
case UNINSTALL_COMPLETE:
mResultCode = msg.arg1;
//Synthetic comment -- @@ -75,6 +78,16 @@
@Override
public void onCreate(Bundle icicle) {
super.onCreate(icicle);
Intent intent = getIntent();
mAppInfo = intent.getParcelableExtra(PackageUtil.INTENT_ATTR_APPLICATION_INFO);
initView();
//Synthetic comment -- @@ -91,6 +104,7 @@
void setResultAndFinish(int retCode) {
setResult(retCode);
finish();
}

public void initView() {
//Synthetic comment -- @@ -107,6 +121,9 @@
mOkButton = (Button)findViewById(R.id.ok_button);
mOkButton.setOnClickListener(this);
mOkPanel.setVisibility(View.INVISIBLE);
PackageDeleteObserver observer = new PackageDeleteObserver();
getPackageManager().deletePackage(mAppInfo.packageName, observer, 0);
}
//Synthetic comment -- @@ -131,4 +148,14 @@
}
return super.dispatchKeyEvent(ev);
}
}







