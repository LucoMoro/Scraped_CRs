/*Store the event arguments, an array of Integer and String, as an Intent
extra with no conversion to another type.  It contains Integer and String, so
it can be serialized with no risk of throwing a runtime exception.

Change-Id:I9e308ead80fb716503016e59442fb73a6d36d5c6*/




//Synthetic comment -- diff --git a/core/java/android/bluetooth/HeadsetBase.java b/core/java/android/bluetooth/HeadsetBase.java
//Synthetic comment -- index b76d84c..71a0bd2 100644

//Synthetic comment -- @@ -184,18 +184,12 @@
@Override
public AtCommandResult handleSetCommand(Object[] args) {

// put the AT command, arguments, and device in an Intent and broadcast it
Intent broadcastIntent =
new Intent(BluetoothDevice.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT);

broadcastIntent.putExtra(BluetoothDevice.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD, mCommandName);
            broadcastIntent.putExtra(BluetoothDevice.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS, args);
broadcastIntent.putExtra(BluetoothDevice.EXTRA_DEVICE, mRemoteDevice);
mContext.sendBroadcast(broadcastIntent);








