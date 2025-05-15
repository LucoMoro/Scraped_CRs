//<Beginning of snippet n. 0>


mOppManager.startTransfer(remoteDevice);

// Fetch the latest device name
String deviceName = mOppManager.getLatestDeviceName(remoteDevice);
String toastMsg;
int batchSize = mOppManager.getBatchSize();
if (mOppManager.mMultipleFlag) {
    // Construct the toast message with the updated device name
    toastMsg = "Transfer started with " + deviceName + ", Batch size: " + batchSize;
} else {
    // Handle single file transfer
    toastMsg = "Starting transfer with " + deviceName;
}

// Show the toast message
Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();

// Implementing error handling for fetching device name
if (deviceName == null || deviceName.isEmpty()) {
    Toast.makeText(context, "Failed to retrieve device name.", Toast.LENGTH_SHORT).show();
}

//<End of snippet n. 0>