/*Fix to get A2DP to connect after unpairing

In this fix, A2DP profile will be connected when pairing with a
previously paired headset. The reason for this error was that the
connection of the A2DP profile was sent before the callback
onCreatePairedDeviceResult was receied in BluetoothEventLoop.java.
By not going to the state BOND_BONDED until after this callback has been
received, the problem is fixed. However the use case is different if the
pairing is initiated by the remote device. In these cases state
BOND_BONDED will be set when onDevicePropertyChanged instead.

Change-Id:I5dedca87d0a6872705ff3a933a99cce6eb37618a*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothEventLoop.java b/core/java/android/server/BluetoothEventLoop.java
//Synthetic comment -- index 4791335..e078c09 100644

//Synthetic comment -- @@ -220,6 +220,7 @@
private void onCreatePairedDeviceResult(String address, int result) {
address = address.toUpperCase();
if (result == BluetoothDevice.BOND_SUCCESS) {
mBluetoothService.getBondState().setBondState(address, BluetoothDevice.BOND_BONDED);
if (mBluetoothService.getBondState().isAutoPairingAttemptsInProgress(address)) {
mBluetoothService.getBondState().clearPinAttempts(address);
//Synthetic comment -- @@ -414,7 +415,13 @@
mBluetoothService.sendUuidIntent(address);
} else if (name.equals("Paired")) {
if (propValues[1].equals("true")) {
                mBluetoothService.getBondState().setBondState(address, BluetoothDevice.BOND_BONDED);
} else {
mBluetoothService.getBondState().setBondState(address,
BluetoothDevice.BOND_NONE);








//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index c0affd3..23f5cb1 100644

//Synthetic comment -- @@ -601,6 +601,8 @@
// There can be only 1 pending outgoing connection at a time,
private String mPendingOutgoingBonding;

private synchronized void setPendingOutgoingBonding(String address) {
mPendingOutgoingBonding = address;
}
//Synthetic comment -- @@ -629,6 +631,14 @@
}
}

public synchronized void setBondState(String address, int state) {
setBondState(address, state, 0);
}
//Synthetic comment -- @@ -1133,7 +1143,7 @@
if (!createPairedDeviceNative(address, 60000 /* 1 minute */)) {
return false;
}

mBondState.setPendingOutgoingBonding(address);
mBondState.setBondState(address, BluetoothDevice.BOND_BONDING);

//Synthetic comment -- @@ -1152,7 +1162,7 @@
if (mBondState.getBondState(address) != BluetoothDevice.BOND_BONDING) {
return false;
}

mBondState.setBondState(address, BluetoothDevice.BOND_NONE,
BluetoothDevice.UNBOND_REASON_AUTH_CANCELED);
cancelDeviceCreationNative(address);







