/*WifiApDialog has wrong lifecycle

Best way to reproduce the problem:
1. Go to Settings -> Wireless -> Tethering.. -> Portable Wi-fi ..
 -> Configure ..
2. Click on Security so the picker dialog opens
3. Press home button
4. Go to Settings. (App crashes)

The problem is that the Spinner in WifiApDialog.java tries to dismiss
the dialog after the dialog has been removed by the window manager
because of leak detection.

The reason this happens is that the WifiApDialog that's created in
WifiApSettings.java is created in such a way that the life-cycle of
the Dialog is handled by the base class Activity. That causes the
dialog problem that the WifiApDialog is removed in onDestroy() of
WifiApSettings, which will send an event that will execute
onDetachFromWindow() on the Spinner instance in the future (this is
handled in ViewRoot.java). That message won't be processed until the
WindowManager has removed the view (the Spinner dialog) because of
leak detection, and once the onDetachedFromWindow() on Spinner
executes it dialog has already been removed.

The problem can be solved by handling the life-cycle of the
WifiApDialog completely in WifiApSettings analog to WifiSettings.

Change-Id:I6c5eea9d155589670f9b819efc28eb2e397e72ac*/




//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiApSettings.java b/src/com/android/settings/wifi/WifiApSettings.java
//Synthetic comment -- index ca1b856..8843dea 100644

//Synthetic comment -- @@ -49,8 +49,6 @@
private static final int OPEN_INDEX = 0;
private static final int WPA_INDEX = 1;

private String[] mSecurityType;
private Preference mCreateNetwork;
private CheckBoxPreference mEnableWifiAp;
//Synthetic comment -- @@ -87,13 +85,9 @@
}
}

    private void showDialog() {
        mDialog = new WifiApDialog(this, this, mWifiConfig);
        mDialog.show();
}

@Override
//Synthetic comment -- @@ -106,12 +100,16 @@
protected void onPause() {
super.onPause();
mWifiApEnabler.pause();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
}

@Override
public boolean onPreferenceTreeClick(PreferenceScreen screen, Preference preference) {
if (preference == mCreateNetwork) {
            showDialog();
}
return true;
}







