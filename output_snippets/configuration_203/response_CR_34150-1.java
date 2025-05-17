//<Beginning of snippet n. 0>
String deviceName = mOppManager.getDeviceName(remoteDevice);
if (deviceName != null) {
    String toastMsg = "Transferring to " + deviceName;
    // Code to display the toast message
    mOppManager.startTransfer(remoteDevice);
} else {
    String errorMsg = "Failed to retrieve device name.";
    // Code to display the error message
}
//<End of snippet n. 0>