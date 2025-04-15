/*WLAN: Bugfix for BT/WLAN coexistence

If WLAN is available, but Bluetooth is not,
old code caused a crash.

Change-Id:I9bbd8f091a4ad1d031be5efc5004b813105e00cfSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index bcbd07c..68dae7f 100644

//Synthetic comment -- @@ -770,12 +770,14 @@

private void checkIsBluetoothPlaying() {
boolean isBluetoothPlaying = false;
        if (mBluetoothA2dp != null) {
            Set<BluetoothDevice> connected = mBluetoothA2dp.getConnectedSinks();

            for (BluetoothDevice device : connected) {
                if (mBluetoothA2dp.getSinkState(device) == BluetoothA2dp.STATE_PLAYING) {
                    isBluetoothPlaying = true;
                    break;
                }
}
}
setBluetoothScanMode(isBluetoothPlaying);
//Synthetic comment -- @@ -905,7 +907,12 @@
mWM.initializeMulticastFiltering();

if (mBluetoothA2dp == null) {
                    try {
                        mBluetoothA2dp = new BluetoothA2dp(mContext);
                    }
                    catch (RuntimeException e) {
                        // Bluetooth is not available
                    }
}
checkIsBluetoothPlaying();








