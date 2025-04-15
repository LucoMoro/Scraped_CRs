/*BT: The Phone doesn't "wake up" when trying to pair with it.

To improve the pairing request usability:
- In the case where a notification shall be used the standard
  notification sound is added
- When the screen is off notification is always used.

Change-Id:I751a7913517c64de5d7ba06d7c17358197d002c5*/
//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/BluetoothPairingRequest.java b/src/com/android/settings/bluetooth/BluetoothPairingRequest.java
//Synthetic comment -- index 4253c5d..55f492e 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;

/**
* BluetoothPairingRequest is a receiver for any Bluetooth pairing request. It
//Synthetic comment -- @@ -61,9 +62,13 @@
pairingIntent.setAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
pairingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

String deviceAddress = device != null ? device.getAddress() : null;
            if (localManager.shouldShowDialogInForeground(deviceAddress)) {
                // Since the BT-related activity is in the foreground, just open the dialog
context.startActivity(pairingIntent);

} else {
//Synthetic comment -- @@ -88,6 +93,7 @@
res.getString(R.string.bluetooth_notif_message) + name,
pending);
notification.flags |= Notification.FLAG_AUTO_CANCEL;

NotificationManager manager = (NotificationManager)
context.getSystemService(Context.NOTIFICATION_SERVICE);







