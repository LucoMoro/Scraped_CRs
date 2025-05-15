//<Beginning of snippet n. 0>
public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
    long startTime = System.currentTimeMillis();
    IDevice device;

    while ((System.currentTimeMillis() - startTime) < timeoutMs) {
        device = findAttacedDevice(deviceIdRegex);
        if (device != null && device.isOnline()) {
            AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
            devices.add(amd);
            try {
                Thread.sleep(1000); // Allow startup time for the monkey
            } catch (InterruptedException e) {
                LOG.warning("Interrupted during startup wait: " + e.getMessage());
            }
            return amd;
        }
    }

    LOG.warning("Device did not become ONLINE within timeout: " + timeoutMs + "ms");
    return null;
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private static final Logger LOG = Logger.getLogger(AdbMonkeyDevice.class.getName());
private static final String[] ZERO_LENGTH_STRING_ARRAY = new String[0];
private static final long MANAGER_CREATE_TIMEOUT_MS = 5 * 1000; // 5 seconds
private final ExecutorService executor = Executors.newCachedThreadPool();
//<End of snippet n. 1>