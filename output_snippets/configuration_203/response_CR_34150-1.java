//<Beginning of snippet n. 0>
mOppManager.startTransfer(remoteDevice);

// Attempt to fetch the latest device name
String deviceName;
try {
    deviceName = mOppManager.getLatestDeviceName(remoteDevice);
    if (deviceName != null) {
        String toastMsg = "Transferring to " + deviceName;
        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(context, "Failed to retrieve device name.", Toast.LENGTH_SHORT).show();
    }
} catch (Exception e) {
    Log.e("DeviceTransfer", "Error fetching device name", e);
    Toast.makeText(context, "Error fetching device name. Please try again.", Toast.LENGTH_SHORT).show();
}

int batchSize = mOppManager.getBatchSize();
if (mOppManager.mMultipleFlag) {
//<End of snippet n. 0>