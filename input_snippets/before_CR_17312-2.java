
//<Beginning of snippet n. 0>


public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
do {
IDevice device = findAttacedDevice(deviceIdRegex);
            if (device != null) {
AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
devices.add(amd);
return amd;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


private static final Logger LOG = Logger.getLogger(AdbMonkeyDevice.class.getName());

private static final String[] ZERO_LENGTH_STRING_ARRAY = new String[0];
    private static final long MANAGER_CREATE_TIMEOUT_MS = 5 * 1000; // 5 seconds

private final ExecutorService executor = Executors.newCachedThreadPool();

return null;
}

Socket monkeySocket;
try {
monkeySocket = new Socket(addr, port);

//<End of snippet n. 1>








