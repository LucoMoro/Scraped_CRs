/*Supporting configurable ADB port in ddms and ddmlib.

- Moving instantiation of sSocketAddr out of static initializer block
  into init().

Change-Id:Ibf3b81492802859673a0e77b4701be9d004509fa*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index 6b9dccc..f69f79a 100644

//Synthetic comment -- @@ -51,23 +51,14 @@

private final static String ADB = "adb"; //$NON-NLS-1$
private final static String DDMS = "ddms"; //$NON-NLS-1$

// Where to find the ADB bridge.
final static String ADB_HOST = "127.0.0.1"; //$NON-NLS-1$
final static int ADB_PORT = 5037;

    static InetAddress sHostAddr;
    static InetSocketAddress sSocketAddr;

    static {
        // built-in local address/port for ADB.
        try {
            sHostAddr = InetAddress.getByName(ADB_HOST);
            sSocketAddr = new InetSocketAddress(sHostAddr, ADB_PORT);
        } catch (UnknownHostException e) {

        }
    }

private static AndroidDebugBridge sThis;
private static boolean sClientSupport;
//Synthetic comment -- @@ -185,6 +176,9 @@
public static void init(boolean clientSupport) {
sClientSupport = clientSupport;

MonitorThread monitorThread = MonitorThread.createInstance();
monitorThread.start();

//Synthetic comment -- @@ -222,6 +216,13 @@
}

/**
* Creates a {@link AndroidDebugBridge} that is not linked to any particular executable.
* <p/>This bridge will expect adb to be running. It will not be able to start/stop/restart
* adb.
//Synthetic comment -- @@ -1049,4 +1050,69 @@
static Object getLock() {
return sLock;
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java
//Synthetic comment -- index 8d14d00..4223248 100644

//Synthetic comment -- @@ -247,7 +247,7 @@
* @see com.android.ddmlib.IDevice#getSyncService()
*/
public SyncService getSyncService() throws IOException {
        SyncService syncService = new SyncService(AndroidDebugBridge.sSocketAddr, this);
if (syncService.openSync()) {
return syncService;
}
//Synthetic comment -- @@ -268,7 +268,7 @@
* @see com.android.ddmlib.IDevice#getScreenshot()
*/
public RawImage getScreenshot() throws IOException {
        return AdbHelper.getFrameBuffer(AndroidDebugBridge.sSocketAddr, this);
}

/*
//Synthetic comment -- @@ -277,7 +277,7 @@
*/
public void executeShellCommand(String command, IShellOutputReceiver receiver)
throws IOException {
        AdbHelper.executeRemoteCommand(AndroidDebugBridge.sSocketAddr, command, this,
receiver);
}

//Synthetic comment -- @@ -286,7 +286,7 @@
* @see com.android.ddmlib.IDevice#runEventLogService(com.android.ddmlib.log.LogReceiver)
*/
public void runEventLogService(LogReceiver receiver) throws IOException {
        AdbHelper.runEventLogService(AndroidDebugBridge.sSocketAddr, this, receiver);
}

/*
//Synthetic comment -- @@ -295,7 +295,7 @@
*/
public void runLogService(String logname,
LogReceiver receiver) throws IOException {
        AdbHelper.runLogService(AndroidDebugBridge.sSocketAddr, this, logname, receiver);
}

/*
//Synthetic comment -- @@ -304,7 +304,7 @@
*/
public boolean createForward(int localPort, int remotePort) {
try {
            return AdbHelper.createForward(AndroidDebugBridge.sSocketAddr, this,
localPort, remotePort);
} catch (IOException e) {
Log.e("adb-forward", e); //$NON-NLS-1$
//Synthetic comment -- @@ -318,7 +318,7 @@
*/
public boolean removeForward(int localPort, int remotePort) {
try {
            return AdbHelper.removeForward(AndroidDebugBridge.sSocketAddr, this,
localPort, remotePort);
} catch (IOException e) {
Log.e("adb-remove-forward", e); //$NON-NLS-1$








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java b/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java
//Synthetic comment -- index 0fd0651..175b657 100644

//Synthetic comment -- @@ -232,7 +232,7 @@

SocketChannel adbChannel = null;
try {
            adbChannel = SocketChannel.open(AndroidDebugBridge.sSocketAddr);
adbChannel.socket().setTcpNoDelay(true);
} catch (IOException e) {
}
//Synthetic comment -- @@ -732,7 +732,7 @@
SocketChannel clientSocket;
try {
clientSocket = AdbHelper.createPassThroughConnection(
                    AndroidDebugBridge.sSocketAddr, device, pid);

// required for Selector
clientSocket.configureBlocking(false);







