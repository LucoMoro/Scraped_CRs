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
//Synthetic comment -- index 4791335..2f4fd85 100644

//Synthetic comment -- @@ -414,7 +414,13 @@
mBluetoothService.sendUuidIntent(address);
} else if (name.equals("Paired")) {
if (propValues[1].equals("true")) {
                // If locally initiated pairing, we will
                // not go to BOND_BONDED state until we have received a
                // successful return value in onCreatePairedDeviceResult
                if (null == mBluetoothService.getBondState().getPendingOutgoingBonding()) {
                    mBluetoothService.getBondState().setBondState(address,
                            BluetoothDevice.BOND_BONDED);
                }
} else {
mBluetoothService.getBondState().setBondState(address,
BluetoothDevice.BOND_NONE);







