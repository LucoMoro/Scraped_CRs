//<Beginning of snippet n. 0>
mOppManager.startTransfer(remoteDevice);

// Display toast message
String deviceName = mBluetoothService.getDeviceName(remoteDevice);
String toastMsg;
if (deviceName != null) {
    toastMsg = "Transferring to: " + deviceName;
} else {
    toastMsg = "Device name could not be retrieved.";
}
int batchSize = mOppManager.getBatchSize();
if (mOppManager.mMultipleFlag) {
//<End of snippet n. 0>