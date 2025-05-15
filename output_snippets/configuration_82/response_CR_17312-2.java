//<Beginning of snippet n. 0>
public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
    long startTime = System.currentTimeMillis();
    do {
        IDevice device = findAttachedDevice(deviceIdRegex);
        if (device != null) {
            AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
            devices.add(amd);
            LOG.info("Device connected: " + deviceIdRegex);
            return amd;
        }
        
        try {
            Thread.sleep(500); // Introduce delay for device startup
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt flag
            LOG.severe("Thread interrupted during wait for device connection.");
            return null;
        }
        
        if (System.currentTimeMillis() - startTime > timeoutMs) {
            LOG.warning("Timeout reached while waiting for device: " + deviceIdRegex);
            return null; // Exit loop if timeout is reached
        }
    } while (true);
}
//<End of snippet n. 0>