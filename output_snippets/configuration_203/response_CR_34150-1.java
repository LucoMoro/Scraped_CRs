//<Beginning of snippet n. 0>
mOppManager.startTransfer(remoteDevice);

// Display toast message
String deviceName = mBluetoothAdapter.getRemoteDevice(remoteDevice.getAddress()).getName();
String toastMsg;
int batchSize = mOppManager.getBatchSize();
if (mOppManager.mMultipleFlag) {
//<End of snippet n. 0>