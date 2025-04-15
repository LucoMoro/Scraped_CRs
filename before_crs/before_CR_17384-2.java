/*Limit bluetooth device name to safe length to avoid crashes

Bluetooth device name can become too long when unicode characters are
used. For device name the max length is 248 bytes but since a unicode
character can be up to 4 bytes the safe length is only 62.

Change-Id:I4a345125f82863e13b8faffece4b2e4718bbd129*/
//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/BluetoothNamePreference.java b/src/com/android/settings/bluetooth/BluetoothNamePreference.java
//Synthetic comment -- index 7a9a0c1..a615a24 100644

//Synthetic comment -- @@ -40,8 +40,9 @@
*/
public class BluetoothNamePreference extends EditTextPreference implements TextWatcher {
private static final String TAG = "BluetoothNamePreference";
    // TODO(): Investigate bluetoothd/dbus crash when length is set to 248, limit as per spec.
    private static final int BLUETOOTH_NAME_MAX_LENGTH = 200;

private LocalBluetoothManager mLocalManager;








