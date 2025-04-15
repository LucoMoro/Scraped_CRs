/*Fixed a memory leak in the bluetooth settings

Release resources in onPause rather than onResume to fix a memory leak.
Without this fix the app will crash if you are in Bluetooth settings
and tap "Device name" and then roate the device a few times.

Change-Id:I57a8346247c13f5113e7c4ea39dde5b29a7359bb*/




//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/BluetoothSettings.java b/src/com/android/settings/bluetooth/BluetoothSettings.java
//Synthetic comment -- index 78c531c..16bb132 100644

//Synthetic comment -- @@ -161,8 +161,6 @@

// Repopulate (which isn't too bad since it's cached in the settings
// bluetooth manager
addDevices();

if (mScreenType == SCREEN_TYPE_SETTINGS) {
//Synthetic comment -- @@ -187,6 +185,9 @@
protected void onPause() {
super.onPause();

        mDevicePreferenceMap.clear();
        mDeviceList.removeAll();

mLocalManager.setForegroundActivity(null);
mDevicePreferenceMap.clear();
mDeviceList.removeAll();







