/*Fix memory leak in ApnSettings-activity

When destroying the activity, the HandlerThread isnt stopped. This
results in that the thread remains, and if you enter/exit the
activity multiple times, you will end up with multiple threads and
a small memory leak

Change-Id:I4779d2625e6002b55066dd034c84d743b9f80ef5*/
//Synthetic comment -- diff --git a/src/com/android/settings/ApnSettings.java b/src/com/android/settings/ApnSettings.java
//Synthetic comment -- index 8ea2063..56ee7a9 100644

//Synthetic comment -- @@ -81,6 +81,7 @@

private RestoreApnUiHandler mRestoreApnUiHandler;
private RestoreApnProcessHandler mRestoreApnProcessHandler;

private String mSelectedKey;

//Synthetic comment -- @@ -141,10 +142,19 @@
@Override
protected void onPause() {
super.onPause();
        
unregisterReceiver(mMobileStateReceiver);
}

private void fillList() {
String where = "numeric=\""
+ android.os.SystemProperties.get(TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC, "")
//Synthetic comment -- @@ -273,12 +283,13 @@
mRestoreApnUiHandler = new RestoreApnUiHandler();
}

        if (mRestoreApnProcessHandler == null) {
            HandlerThread restoreDefaultApnThread = new HandlerThread(
"Restore default APN Handler: Process Thread");
            restoreDefaultApnThread.start();
mRestoreApnProcessHandler = new RestoreApnProcessHandler(
                    restoreDefaultApnThread.getLooper(), mRestoreApnUiHandler);
}

mRestoreApnProcessHandler







