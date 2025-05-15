//<Beginning of snippet n. 0>
mOppManager.startTransfer(remoteDevice);

// Display toast message
mOppManager.getDeviceNameFromService(remoteDevice, new DeviceNameCallback() {
    @Override
    public void onDeviceNameRetrieved(String deviceName) {
        String toastMsg;
        int batchSize = mOppManager.getBatchSize();
        if (mOppManager.mMultipleFlag) {
            // handle multiple flag case
            toastMsg = "Starting transfer to multiple devices: " + deviceName + " | Batch Size: " + batchSize;
        } else {
            // handle single device case
            toastMsg = "Starting transfer to: " + deviceName;
        }
        showToast(toastMsg);
    }

    @Override
    public void onError() {
        showToast("Error retrieving device name.");
    }
});
//<End of snippet n. 0>