//<Beginning of snippet n. 0>
public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) throws DeviceConnectionException {
    long startTime = System.currentTimeMillis();
    IDevice device = findAttacedDevice(deviceIdRegex);
    long pollingInterval = 500; // Polling interval

    while (System.currentTimeMillis() - startTime <= timeoutMs) {
        if (device != null && isDeviceOnline(device)) {
            AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
            devices.add(amd);
            LOG.info("Device connected: " + device.getSerialNumber());
            return amd;
        }

        LOG.info("Polling for device status.");
        try {
            Thread.sleep(pollingInterval);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.warning("Thread was interrupted during waiting for device connection.");
            throw new DeviceConnectionException("Device connection interrupted for device ID Regex: " + deviceIdRegex);
        }
        device = findAttacedDevice(deviceIdRegex);
    }

    LOG.severe("Timeout reached while waiting for device to be ONLINE. Device ID Regex: " + deviceIdRegex);
    throw new DeviceConnectionException("Failed to connect to device ID Regex: " + deviceIdRegex + " within timeout period.");
}
//<End of snippet n. 0>