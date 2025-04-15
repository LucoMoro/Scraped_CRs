/*Add option to disable initialization of AndroidDebugBridge.

Change-Id:Iea659ca03c864db79f53154a419d32dece9aba66*/




//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/ChimpChat.java b/chimpchat/src/com/android/chimpchat/ChimpChat.java
//Synthetic comment -- index ad9ef0d..5618ec6 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
public class ChimpChat {
private final IChimpBackend mBackend;
private static String sAdbLocation;
    private static boolean sNoInitAdb;

private ChimpChat(IChimpBackend backend) {
this.mBackend = backend;
//Synthetic comment -- @@ -45,6 +46,8 @@
*/
public static ChimpChat getInstance(Map<String, String> options) {
sAdbLocation = options.get("adbLocation");
        sNoInitAdb = Boolean.valueOf(options.get("noInitAdb"));

IChimpBackend backend = createBackendByName(options.get("backend"));
if (backend == null) {
return null;
//Synthetic comment -- @@ -72,11 +75,7 @@

private static IChimpBackend createBackendByName(String backendName) {
if ("adb".equals(backendName)) {
            return new AdbBackend(sAdbLocation, sNoInitAdb);
} else {
return null;
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/adb/AdbBackend.java b/chimpchat/src/com/android/chimpchat/adb/AdbBackend.java
//Synthetic comment -- index b4dc233..2cb5be7 100644

//Synthetic comment -- @@ -38,23 +38,38 @@
private static final int CONNECTION_ITERATION_TIMEOUT_MS = 200;
private final List<IChimpDevice> devices = Lists.newArrayList();
private final AndroidDebugBridge bridge;
    private final boolean initAdb;

    /**
     * Constructs an AdbBackend with default options.
     */
public AdbBackend() {
        this(null, false);
    }

    /**
     * Constructs an AdbBackend.
     *
     * @param adbLocation The location of the adb binary. If null, AdbBackend will
     *     attempt to find the binary by itself.
     * @param noInitAdb If true, AdbBackend will not initialize AndroidDebugBridge.
     */
    public AdbBackend(String adbLocation, boolean noInitAdb) {
        this.initAdb = !noInitAdb;

        // [try to] ensure ADB is running
        if (adbLocation == null) {
            adbLocation = findAdb();
        }

        if (initAdb) {
            AndroidDebugBridge.init(false /* debugger support */);
        }

bridge = AndroidDebugBridge.createBridge(
adbLocation, true /* forceNewBridge */);
}

private String findAdb() {
String mrParentLocation =
System.getProperty("com.android.monkeyrunner.bindir"); //$NON-NLS-1$
//Synthetic comment -- @@ -126,6 +141,8 @@
for (IChimpDevice device : devices) {
device.dispose();
}
        if (initAdb) {
            AndroidDebugBridge.terminate();
        }
}
}







