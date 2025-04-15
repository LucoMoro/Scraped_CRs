/*Cancel inquiry before outgoing service connection.

Before connecting to a headset, discovery needs to be
canceled, to avoid "Connection timeout" error.

Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>
Change-Id:I5bdbb99ed5deee316cd6fd04bc06c4a82cb6eacd*/
//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHeadsetService.java b/src/com/android/phone/BluetoothHeadsetService.java
//Synthetic comment -- index c0452b5..095b19c 100755

//Synthetic comment -- @@ -585,6 +585,9 @@
mRemoteHeadsets.get(device).mHeadsetType = type;
int channel = device.getServiceChannel(BluetoothUuid.Handsfree);
mConnectThread = new RfcommConnectThread(device, channel, type);
mConnectThread.start();
if (getPriority(device) < BluetoothHeadset.PRIORITY_AUTO_CONNECT) {
setPriority(device, BluetoothHeadset.PRIORITY_AUTO_CONNECT);
//Synthetic comment -- @@ -596,6 +599,9 @@
mRemoteHeadsets.get(device).mHeadsetType = type;
int channel = device.getServiceChannel(BluetoothUuid.HSP);
mConnectThread = new RfcommConnectThread(device, channel, type);
mConnectThread.start();
if (getPriority(device) < BluetoothHeadset.PRIORITY_AUTO_CONNECT) {
setPriority(device, BluetoothHeadset.PRIORITY_AUTO_CONNECT);







