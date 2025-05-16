//<Beginning of snippet n. 0>
public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
    long startTime = System.currentTimeMillis();
    IDevice device;

    while (System.currentTimeMillis() - startTime < timeoutMs) {
        device = findAttachedDevice(deviceIdRegex);
        if (device != null && device.isOnline()) {
            AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
            devices.add(amd);
            LOG.info("Device connected: " + deviceIdRegex);
            return amd;
        }
        try {
            Thread.sleep(500); // Sleep for 500 milliseconds before the next check
        } catch (InterruptedException e) {
            LOG.warning("Thread was interrupted during wait: " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }
    LOG.warning("Connection timeout for deviceIdRegex: " + deviceIdRegex);
    return null;
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private static final Logger LOG = Logger.getLogger(AdbMonkeyDevice.class.getName());
private static final String[] ZERO_LENGTH_STRING_ARRAY = new String[0];
private static final long MANAGER_CREATE_TIMEOUT_MS = 5 * 1000; // 5 seconds
private final ExecutorService executor = Executors.newCachedThreadPool();

Socket monkeySocket = null;

try {
    monkeySocket = new Socket(addr, port);
    // Additional socket operations...
} catch (IOException e) {
    LOG.severe("Socket connection failed: " + e.getMessage());
    if (monkeySocket != null) {
        try {
            monkeySocket.close();
        } catch (IOException closeEx) {
            LOG.warning("Failed to close socket: " + closeEx.getMessage());
        }
    }
} finally {
    if (monkeySocket != null && !monkeySocket.isClosed()) {
        try {
            monkeySocket.close();
        } catch (IOException closeEx) {
            LOG.warning("Failed to close socket in finally block: " + closeEx.getMessage());
        }
    }
}
//<End of snippet n. 1>