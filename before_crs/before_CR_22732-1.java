/*BT: Wrong publishing result of bonding error code.

During bonding bluez stack publish the error code over dbus.
JNI gets the error, in this ER case:
org.bluez.Error.AuthenticationFailed (Authentication Failed),
and then wrong call to overloaded setBondState() is made on
callstack using default result code parameter as 0 (BOND_SUCCESS).

Change-Id:I6f743cedc76e63d0c2a35e89d3aa48267b89c06eSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
old mode 100644
new mode 100755
//Synthetic comment -- index 4d4d309..6144766

//Synthetic comment -- @@ -1351,7 +1351,7 @@
}

/*package*/ synchronized boolean setBondState(String address, int state, int reason) {
        mBondState.setBondState(address.toUpperCase(), state);
return true;
}








