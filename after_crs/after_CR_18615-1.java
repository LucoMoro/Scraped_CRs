/*Phone crash when old callback func is woken up when enabling BT.

When the remote Jerry device is powered down the BT link to the
phone is dropped, and the Jerry firmware in the phone quite
immediately tries to re-connect to the Jerry device. Then
SDP and Discover Services is started, fetchRemoteUuids() ->
discoverServicesNative(). This results in an asynchronous dbus
call dbus_func_args_async()  that is provided with a callback
function, onDiscoverServicesResult(), but before this callback
function is used Bluetooth is disabled according to the problem
scenario above. For some reason this discover services activity
is not cleared when Bluetooth is disabled, so when Bluetooth
is enabled again the (old) callback function
onDiscoverServicesResult() is executed, but the following
getAddressFromObjectPath() fails. The reason for this is that
the deviceObjectPath parameter contains an old value,
containg the process id of the old bluetoothd (the one running
before Bluetooth was disabled). Then the new updated
AdapterObjectPath /org/bluez/<new bluetooth hd pid>/hci0/dev_
is not a prefix of the old deviceObjectPath /org/bluez/<old
bluetooth hd pid>/hci0/dev_<BT_ADDR>, which results in that null
will be used as address in sendUuidIntent(), and later on,
ending up in the BluetoothDevice constructor where and
IllegalArgumentExceotion is thrown due to
Bluetooth address = null. Then the phone will crash.

Making sure sendUuidIntent() is not called when address is null
is a work-around for the problem.

Change-Id:I8ff60bad80de3b379cef0970402943dfa4de3cfd*/




//Synthetic comment -- diff --git a/core/java/android/server/BluetoothEventLoop.java b/core/java/android/server/BluetoothEventLoop.java
//Synthetic comment -- index 2f4fd85..f89bc1d 100644

//Synthetic comment -- @@ -635,7 +635,9 @@
if (result) {
mBluetoothService.updateRemoteDevicePropertiesCache(address);
}
        if (address != null) {
            mBluetoothService.sendUuidIntent(address);
        }
mBluetoothService.makeServiceChannelCallbacks(address);
}








