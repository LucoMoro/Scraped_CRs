/*screen activation on pairing request

When the screen is off, the screen is not activated
on a pairing request notification from a bluetooth device.

This patch adds a full wakelock with a 15s timeout
when a pairing request that occurs when the screen
is off is detected. The effect is that the screen
goes bright for 15s when the request is received, leaving the
opportunity for the user to treat it properly.

Change-Id:I744cc37294db6fe631175ed1ef0929b2786096e7Author: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 3669 17106*/
//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/BluetoothPairingRequest.java b/src/com/android/settings/bluetooth/BluetoothPairingRequest.java
//Synthetic comment -- index 838e7b1..6dea372 100644

//Synthetic comment -- @@ -38,6 +38,8 @@
public final class BluetoothPairingRequest extends BroadcastReceiver {

private static final int NOTIFICATION_ID = android.R.drawable.stat_sys_data_bluetooth;

@Override
public void onReceive(Context context, Intent intent) {
//Synthetic comment -- @@ -71,6 +73,14 @@
// just open the dialog
context.startActivity(pairingIntent);
} else {
// Put up a notification that leads to the dialog
Resources res = context.getResources();
Notification.Builder builder = new Notification.Builder(context)







