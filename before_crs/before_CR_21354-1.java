/*Removing dead store; it was likely a relict of debuging code.

Change-Id:I4daab1359d7c4b300ef61e3de1578d9fecb6f8da*/
//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothDeviceProfileState.java b/core/java/android/bluetooth/BluetoothDeviceProfileState.java
//Synthetic comment -- index 875af1d..1f922cd 100644

//Synthetic comment -- @@ -600,7 +600,6 @@
@Override
protected boolean processMessage(Message message) {
log("IncomingA2dp State->Processing Message: " + message.what);
            Message deferMsg = new Message();
switch(message.what) {
case CONNECT_HFP_OUTGOING:
deferMessage(message);







