/*Fix null dereference crashing statusbar service

For the emulator and devices without bluetooth support, the bluetooth service
is null.  The statusbar crashes upon attempt to dereference this null service.
So, simply check for null first.  This fixes issue 1157.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/status/StatusBarPolicy.java b/services/java/com/android/server/status/StatusBarPolicy.java
//Synthetic comment -- index ad027db..612bd76 100644

//Synthetic comment -- @@ -755,8 +755,9 @@
private final void updateBluetooth(Intent intent) {
boolean visible;
if (intent == null) {  // Initialize
            visible = ((BluetoothDevice)
                mContext.getSystemService(Context.BLUETOOTH_SERVICE)).isEnabled();
mService.setIconVisibility(mBluetoothIcon, visible);
return;
}







