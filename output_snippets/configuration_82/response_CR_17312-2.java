//<Beginning of snippet n. 0>
public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
    long startTime = System.currentTimeMillis();
    while (System.currentTimeMillis() - startTime < timeoutMs) {
        IDevice device = findAttacedDevice(deviceIdRegex);
        if (device != null) {
            AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
            devices.add(amd);
            if (amd.isOnline()) {
                return amd;
            }
            LOG.info("Device not ONLINE. Waiting for status change...");
        }
        try {
            Thread.sleep(500); // Delay to avoid high CPU usage
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            return null;
        }
    }
    LOG.warning("Timeout exceeded while waiting for device to become online.");
    return null;
//<End of snippet n. 0>