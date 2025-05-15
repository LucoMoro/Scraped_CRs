
//<Beginning of snippet n. 0>


public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
do {
IDevice device = findAttacedDevice(deviceIdRegex);
            // Only return the device when it is online
            if (device != null && device.getState() == IDevice.DeviceState.ONLINE) {
AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
devices.add(amd);
return amd;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


private static final Logger LOG = Logger.getLogger(AdbMonkeyDevice.class.getName());

private static final String[] ZERO_LENGTH_STRING_ARRAY = new String[0];
    private static final long MANAGER_CREATE_TIMEOUT_MS = 30 * 1000; // 30 seconds
    private static final long MANAGER_CREATE_WAIT_TIME_MS = 1000; // wait 1 second

private final ExecutorService executor = Executors.newCachedThreadPool();

return null;
}

            try {
                Thread.sleep(MANAGER_CREATE_WAIT_TIME_MS);
            } catch (InterruptedException e) {
                LOG.log(Level.SEVERE, "Unable to sleep", e);
            }

Socket monkeySocket;
try {
monkeySocket = new Socket(addr, port);

//<End of snippet n. 1>








