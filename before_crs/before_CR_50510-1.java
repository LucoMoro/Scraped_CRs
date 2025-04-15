/*Bluetooth discoverability to be timed out by latest setting of it.

After having set bluetooth visibility once using settings UI,
it cannot be changed by application to permanent visibility through
API. The API call succeeds, and the UI shows the change, but it
will still timeout after the timeout set first, before the change
by the app.

The intent BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE is received
and the phone will now show a dialog that asks if you allow to set
the phone Bluetooth discoverability to always on. The settings shows
time 0:00, not counting down, however it times out anyway.

Change-Id:Ic5429b3df06e9b0509eb3ef6454450afc89b2b60*/
//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/RequestPermissionActivity.java b/src/com/android/settings/bluetooth/RequestPermissionActivity.java
//Synthetic comment -- index 08c10fb..f843848 100644

//Synthetic comment -- @@ -227,6 +227,7 @@
returnCode = RESULT_OK;
} else if (mLocalAdapter.setScanMode(
BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, mTimeout)) {
// If already in discoverable mode, this will extend the timeout.
long endTime = System.currentTimeMillis() + (long) mTimeout * 1000;
LocalBluetoothPreferences.persistDiscoverableEndTimestamp(







