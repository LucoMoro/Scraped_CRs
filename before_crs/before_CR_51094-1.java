/*TetherSettings: Update correct string when bluetooth is turned off

Update correct string when bluetooth is turned off

Change-Id:I463b6dd29d6bc401fd508d481f6af0dd2c164d43Signed-off-by: Vishal Mahaveer <vishalm@ti.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/TetherSettings.java b/src/com/android/settings/TetherSettings.java
//Synthetic comment -- index df44e91..d2c99dc 100644

//Synthetic comment -- @@ -388,7 +388,7 @@
int btState = adapter.getState();
if (btState == BluetoothAdapter.STATE_TURNING_OFF) {
mBluetoothTether.setEnabled(false);
            mBluetoothTether.setSummary(R.string.wifi_stopping);
} else if (btState == BluetoothAdapter.STATE_TURNING_ON) {
mBluetoothTether.setEnabled(false);
mBluetoothTether.setSummary(R.string.bluetooth_turning_on);







