//<Beginning of snippet n. 0>
public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
    long startTime = System.currentTimeMillis();
    IDevice device = findAttacedDevice(deviceIdRegex);
    long pollingInterval = 500; // Polling interval

    while (device == null || !isDeviceOnline(device)) {
        if (System.currentTimeMillis() - startTime > timeoutMs) {
            LOG.severe("Timeout reached while waiting for device to be ONLINE. Device ID Regex: " + deviceIdRegex);
            return null;
        }
        try {
            Thread.sleep(pollingInterval); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.warning("Thread was interrupted during waiting for device connection.");
            return null;
        }
        device = findAttacedDevice(deviceIdRegex);
        LOG.info("Polling for device status.");
    }

    AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
    devices.add(amd);
    LOG.info("Device connected: " + device.getSerialNumber());
    return amd;
}
//<End of snippet n. 0>