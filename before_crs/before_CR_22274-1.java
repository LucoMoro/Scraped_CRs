/*WLAN: Bugfix for BT/WLAN coexistence

If WLAN is available, but Bluetooth is not,
old code caused a crash.

Change-Id:I3242ec5a77536358a686b92322f1af10ad3cdcfbSigned-off-by: christian bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index bf2d033..baeb2fe 100644

//Synthetic comment -- @@ -768,12 +768,14 @@

private void checkIsBluetoothPlaying() {
boolean isBluetoothPlaying = false;
        Set<BluetoothDevice> connected = mBluetoothA2dp.getConnectedSinks();

        for (BluetoothDevice device : connected) {
            if (mBluetoothA2dp.getSinkState(device) == BluetoothA2dp.STATE_PLAYING) {
                isBluetoothPlaying = true;
                break;
}
}
setBluetoothScanMode(isBluetoothPlaying);







