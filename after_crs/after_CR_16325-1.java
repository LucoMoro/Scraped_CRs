/*Store the event arguments, an array of Integer and String, as an
Intent extra with no conversion to another type.  It contains Integer
and String, so it can be serialized with no risk of throwing a runtime
exception.

Change-Id:Iad71e78b1c72df795c83f831d1ff05290b5a1288*/




//Synthetic comment -- diff --git a/core/java/android/bluetooth/HeadsetBase.java b/core/java/android/bluetooth/HeadsetBase.java
//Synthetic comment -- index 71a0bd2..d82ae73 100644

//Synthetic comment -- @@ -189,6 +189,7 @@
new Intent(BluetoothDevice.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT);

broadcastIntent.putExtra(BluetoothDevice.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD, mCommandName);
            // assert: all elements of args are Serializable
broadcastIntent.putExtra(BluetoothDevice.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS, args);
broadcastIntent.putExtra(BluetoothDevice.EXTRA_DEVICE, mRemoteDevice);
mContext.sendBroadcast(broadcastIntent);







