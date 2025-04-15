/*Add option to disable initialization of AndroidDebugBridge.

(cherry picked from commit 9d567c3de7d18a466e8d77ace93e5247b7af7120)

Change-Id:I74f84ff61536e8c9db38d63eb51f0d7ec2cbd90a*/
//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/ChimpChat.java b/chimpchat/src/com/android/chimpchat/ChimpChat.java
//Synthetic comment -- index ad9ef0d..5618ec6 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
public class ChimpChat {
private final IChimpBackend mBackend;
private static String sAdbLocation;

private ChimpChat(IChimpBackend backend) {
this.mBackend = backend;
//Synthetic comment -- @@ -45,6 +46,8 @@
*/
public static ChimpChat getInstance(Map<String, String> options) {
sAdbLocation = options.get("adbLocation");
IChimpBackend backend = createBackendByName(options.get("backend"));
if (backend == null) {
return null;
//Synthetic comment -- @@ -72,11 +75,7 @@

private static IChimpBackend createBackendByName(String backendName) {
if ("adb".equals(backendName)) {
            if (sAdbLocation == null) {
                return new AdbBackend();
            } else {
                return new AdbBackend(sAdbLocation);
            }
} else {
return null;
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/adb/AdbBackend.java b/chimpchat/src/com/android/chimpchat/adb/AdbBackend.java
//Synthetic comment -- index b4dc233..2cb5be7 100644

//Synthetic comment -- @@ -38,23 +38,38 @@
private static final int CONNECTION_ITERATION_TIMEOUT_MS = 200;
private final List<IChimpDevice> devices = Lists.newArrayList();
private final AndroidDebugBridge bridge;

public AdbBackend() {
        // [try to] ensure ADB is running
        String adbLocation = findAdb();

        AndroidDebugBridge.init(false /* debugger support */);

bridge = AndroidDebugBridge.createBridge(
adbLocation, true /* forceNewBridge */);
}

    public AdbBackend(String location) {
        AndroidDebugBridge.init(false /* debugger support */);
        bridge = AndroidDebugBridge.createBridge(location,
                                                 true /* force new bridge */);
    }

private String findAdb() {
String mrParentLocation =
System.getProperty("com.android.monkeyrunner.bindir"); //$NON-NLS-1$
//Synthetic comment -- @@ -126,6 +141,8 @@
for (IChimpDevice device : devices) {
device.dispose();
}
        AndroidDebugBridge.terminate();
}
}







