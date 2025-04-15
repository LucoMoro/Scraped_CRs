/*Improve waitForConnection for booting devices.

Have waitForConnection actually wait until the device is ONLINE before returning
it.  Also give the on device monkey some more time to startup.

Change-Id:I86193a8532a84d64dddd9a60012af4f3ef507841*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbBackend.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbBackend.java
//Synthetic comment -- index 63badf5..1a1693f 100644

//Synthetic comment -- @@ -69,7 +69,8 @@
public MonkeyDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
do {
IDevice device = findAttacedDevice(deviceIdRegex);
            // Only return the device one it is online
            if (device != null && device.getState() == IDevice.DeviceState.ONLINE) {
AdbMonkeyDevice amd = new AdbMonkeyDevice(device);
devices.add(amd);
return amd;








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java
//Synthetic comment -- index dedc1ea..b180ccd 100644

//Synthetic comment -- @@ -51,7 +51,8 @@
private static final Logger LOG = Logger.getLogger(AdbMonkeyDevice.class.getName());

private static final String[] ZERO_LENGTH_STRING_ARRAY = new String[0];
    private static final long MANAGER_CREATE_TIMEOUT_MS = 30 * 1000; // 30 seconds
    private static final long MANAGER_CREATE_WAIT_TIME_MS = 1000; // wait 1 second

private final ExecutorService executor = Executors.newCachedThreadPool();

//Synthetic comment -- @@ -151,6 +152,12 @@
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







