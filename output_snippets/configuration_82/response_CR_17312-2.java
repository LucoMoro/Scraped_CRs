//<Beginning of snippet n. 0>
public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
    long endTime = System.currentTimeMillis() + timeoutMs;
    int attempts = 0;
    long sleepDuration = 1000; // Initial sleep duration

    while (System.currentTimeMillis() < endTime) {
        IDevice device = findAttacedDevice(deviceIdRegex);
        if (device != null) {
            if (device.isOnline() && isDeviceFullyOperational(device)) {
                AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
                devices.add(amd);
                return amd;
            }
        }
        attempts++;
        try {
            Thread.sleep(sleepDuration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted during wait", e);
        }
        sleepDuration = Math.min(sleepDuration * 2, timeoutMs - (System.currentTimeMillis() - (endTime - timeoutMs))); // Exponential backoff
    }
    LOG.warning(String.format("Device did not become ONLINE within the specified timeout. Total attempts: %d, Elapsed time: %d ms.", attempts, timeoutMs - (endTime - System.currentTimeMillis())));
    throw new RuntimeException("Device did not become ONLINE within the specified timeout.");
}

private boolean isDeviceFullyOperational(IDevice device) {
    // Implement additional checks to determine if the device is fully operational.
    return true; // Placeholder implementation
}
//<End of snippet n. 0>