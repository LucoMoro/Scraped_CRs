/*Removing dead store; it was likely a relict of debuging code.

Change-Id:I4daab1359d7c4b300ef61e3de1578d9fecb6f8da*/




//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothDeviceProfileState.java b/core/java/android/bluetooth/BluetoothDeviceProfileState.java
//Synthetic comment -- index f6d7073..9d7e641 100644

//Synthetic comment -- @@ -596,7 +596,6 @@
@Override
protected boolean processMessage(Message message) {
log("IncomingA2dp State->Processing Message: " + message.what);
switch(message.what) {
case CONNECT_HFP_OUTGOING:
deferMessage(message);







