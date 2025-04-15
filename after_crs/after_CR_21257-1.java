/*Reconnecting a bluetooth headset while in multipoint

In multipoint a bluetooth headset can be paired with two handsets
but only connected with one at a time. When reconnecting a device
that is in progress or connected, shall not require a disconnect
followed by a connect.

Change-Id:Ie65bddabd4879b045318eda6e58d3e9ba96697e1*/




//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/LocalBluetoothProfileManager.java b/src/com/android/settings/bluetooth/LocalBluetoothProfileManager.java
//Synthetic comment -- index 8d29428..58e00e8 100644

//Synthetic comment -- @@ -408,6 +408,10 @@
// disconnect from any headset first
BluetoothDevice currDevice = mService.getCurrentHeadset();
if (currDevice != null) {
                if (currDevice.equals(device)) {
                    // In progress or already connected
                    return true;
                }
mService.disconnectHeadset(currDevice);
}
return mService.connectHeadset(device);







