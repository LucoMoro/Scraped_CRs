//<Beginning of snippet n. 0>
public class DeviceConnectionException extends RuntimeException {
    public DeviceConnectionException(String message) {
        super(message);
    }

    public DeviceConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}

public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex, long initialStartupDelayMs, long pollingIntervalMs) {
    long startTime = System.currentTimeMillis();
    IDevice device = null;

    try {
        // Initial wait to allow device initialization
        Thread.sleep(initialStartupDelayMs);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        LOG.severe("Initial waiting for device interrupted. Device ID: " + deviceIdRegex + ". Reason: " + e.getMessage());
        throw new DeviceConnectionException("Initial waiting for device was interrupted. Device ID: " + deviceIdRegex, e);
    }

    while (System.currentTimeMillis() - startTime <= timeoutMs) {
        device = findAttacedDevice(deviceIdRegex);
        if (device != null) {
            AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
            devices.add(amd);
            if (amd.isOnline()) {
                return amd;
            } else {
                LOG.info("Device is detected but not online. Waiting...");
            }
        } else {
            LOG.info("No device found matching ID: " + deviceIdRegex);
        }

        try {
            Thread.sleep(pollingIntervalMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.severe("Waiting for device interrupted. Device ID: " + deviceIdRegex + ". Reason: " + e.getMessage());
            throw new DeviceConnectionException("Waiting for device was interrupted. Device ID: " + deviceIdRegex, e);
        }
    }

    LOG.severe("Timeout reached while waiting for device to become online. Device ID: " + deviceIdRegex);
    throw new DeviceConnectionException("Device did not become ONLINE within the timeout period. Device ID: " + deviceIdRegex);
}
//<End of snippet n. 0>