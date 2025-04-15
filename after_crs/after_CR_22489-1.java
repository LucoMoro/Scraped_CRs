/*Fixing crash in BluetoothPbapService.

If Bluetooth is turned off while there is an PBAP connect request
pending, the status bar notification or yes/no activity will not
disappear. If user selects yes, an intent will start PbapService
again. PbapService will try to make the device trusted. As this
instance of PbapService don't have any connected device, crash will
occur.

Make sure status bar notification or yes/no activity disappears, by
sending timeout intent when Bluetooth is turned off.
Also, PbapReceiver should not forward any intents except STATE_ON
if Bluetooth is off as this will start the PbapService.

Change-Id:Iee4f95ab2de34db6e00ff9b1fd7f8677947b8f4f*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapReceiver.java b/src/com/android/bluetooth/pbap/BluetoothPbapReceiver.java
//Synthetic comment -- index b450376..55cb66c 100644

//Synthetic comment -- @@ -61,6 +61,12 @@
|| (state == BluetoothAdapter.STATE_TURNING_OFF)) {
startService = false;
}
        } else {
            // Don't forward intent unless device has bluetooth and bluetooth is enabled.
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter == null || !adapter.isEnabled()) {
                startService = false;
            }
}
if (startService) {
context.startService(in);








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapService.java b/src/com/android/bluetooth/pbap/BluetoothPbapService.java
//Synthetic comment -- index 3b1216e..a7225b2 100644

//Synthetic comment -- @@ -245,10 +245,17 @@
int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
boolean removeTimeoutMsg = true;
if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
if (state == BluetoothAdapter.STATE_OFF) {
                // Send any pending timeout now, as this service will be destroyed.
                if (mSessionStatusHandler.hasMessages(USER_TIMEOUT)) {
                    Intent timeoutIntent = new Intent(USER_CONFIRM_TIMEOUT_ACTION);
                    sendBroadcast(timeoutIntent);
                    removePbapNotification(NOTIFICATION_ID_ACCESS);
                }
// Release all resources
closeService();
            } else {
                removeTimeoutMsg = false;
}
} else if (action.equals(ACCESS_ALLOWED_ACTION)) {
if (intent.getBooleanExtra(EXTRA_ALWAYS_ALLOWED, false)) {







