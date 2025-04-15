/*Supporting configurable ADB port in ddms and ddmlib.

- Moving instantiation of sSocketAddr out of static initializer block
  into init().

Change-Id:Ibf3b81492802859673a0e77b4701be9d004509fa*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index 6b9dccc..3698f38 100644

//Synthetic comment -- @@ -51,23 +51,14 @@

private final static String ADB = "adb"; //$NON-NLS-1$
private final static String DDMS = "ddms"; //$NON-NLS-1$
    private final static String SERVER_PORT_ENV_VAR = "ANDROID_ADB_SERVER_PORT"; //$NON-NLS-1$

// Where to find the ADB bridge.
final static String ADB_HOST = "127.0.0.1"; //$NON-NLS-1$
final static int ADB_PORT = 5037;

    private static InetAddress sHostAddr;
    private static InetSocketAddress sSocketAddr;

private static AndroidDebugBridge sThis;
private static boolean sClientSupport;
//Synthetic comment -- @@ -185,6 +176,9 @@
public static void init(boolean clientSupport) {
sClientSupport = clientSupport;

        // Determine port and instantiate socket address.
        initAdbSocketAddr();

MonitorThread monitorThread = MonitorThread.createInstance();
monitorThread.start();

//Synthetic comment -- @@ -222,6 +216,13 @@
}

/**
     * Returns the socket address of the ADB server on the host.
     */
    public static InetSocketAddress getSocketAddress() {
        return sSocketAddr;
    }

    /**
* Creates a {@link AndroidDebugBridge} that is not linked to any particular executable.
* <p/>This bridge will expect adb to be running. It will not be able to start/stop/restart
* adb.
//Synthetic comment -- @@ -1049,4 +1050,69 @@
static Object getLock() {
return sLock;
}

    /**
     * Instantiates sSocketAddr with the address of the host's adb process. 
     */
    public static void initAdbSocketAddr() {
        try {
            int adb_port = determineAndValidateAdbPort();
            sHostAddr = InetAddress.getByName(ADB_HOST);
            sSocketAddr = new InetSocketAddress(sHostAddr, adb_port);
        } catch (UnknownHostException e) {
            // localhost should always be known.
        }
    }

    /**
     * Determines port where ADB is expected by looking at an env variable.
     * <p/>
     * The value for the environment variable ANDROID_ADB_SERVER_PORT is validated,
     * IllegalArgumentException is thrown on illegal values.
     * <p/>
     * @return The port number where the host's adb should be expected or started.
     * @throws IllegalArgumentException if ANDROID_ADB_SERVER_PORT has a non-numeric value.
     */
    private static int determineAndValidateAdbPort() {
        String adb_env_var;
        int result = ADB_PORT;
        try {
            adb_env_var = System.getenv(SERVER_PORT_ENV_VAR);

            if (adb_env_var != null) {
                adb_env_var = adb_env_var.trim();
            }

            if (adb_env_var != null && adb_env_var.length() > 0) {
                // C tools (adb, emulator) accept hex and octal port numbers, so need to accept
                // them too.
                result = Integer.decode(adb_env_var);

                if (result <= 0) {
                    String errMsg = "env var " + SERVER_PORT_ENV_VAR + ": must be >=0, got " //$NON-NLS-1$
                    + System.getenv(SERVER_PORT_ENV_VAR); //$NON-NLS-1$
                    throw new IllegalArgumentException(errMsg);
                }
            }
        } catch (NumberFormatException nfEx) {
            String errMsg = "env var " + SERVER_PORT_ENV_VAR + ": illegal value '" //$NON-NLS-1$
            + System.getenv(SERVER_PORT_ENV_VAR) + "'"; //$NON-NLS-1$
            throw new IllegalArgumentException(errMsg);
        } catch (SecurityException secEx) {
            // A security manager has been installed that doesn't allow access to env vars.
            // So an environment variable might have been set, but we can't tell.
            // Let's log a warning and continue with ADB's default port.
            // The issue is that adb would be started (by the forked process having access
            // to the env vars) on the desired port, but within this process, we can't figure out
            // what that port is. However, a security manager not granting access to env vars
            // but allowing to fork is a rare and interesting configuration, so the right
            // thing seems to be to continue using the default port, as forking is likely to
            // fail later on in the scenario of the security manager.
            Log.w(DDMS,
                    "No access to env variables allowed by current security manager. " //$NON-NLS-1$
                    + "If you've set ANDROID_ADB_SERVER_PORT: it's being ignored."); //$NON-NLS-1$
        }
        return result;
    }

}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java
//Synthetic comment -- index 8d14d00..4223248 100644

//Synthetic comment -- @@ -247,7 +247,7 @@
* @see com.android.ddmlib.IDevice#getSyncService()
*/
public SyncService getSyncService() throws IOException {
        SyncService syncService = new SyncService(AndroidDebugBridge.getSocketAddress(), this);
if (syncService.openSync()) {
return syncService;
}
//Synthetic comment -- @@ -268,7 +268,7 @@
* @see com.android.ddmlib.IDevice#getScreenshot()
*/
public RawImage getScreenshot() throws IOException {
        return AdbHelper.getFrameBuffer(AndroidDebugBridge.getSocketAddress(), this);
}

/*
//Synthetic comment -- @@ -277,7 +277,7 @@
*/
public void executeShellCommand(String command, IShellOutputReceiver receiver)
throws IOException {
        AdbHelper.executeRemoteCommand(AndroidDebugBridge.getSocketAddress(), command, this,
receiver);
}

//Synthetic comment -- @@ -286,7 +286,7 @@
* @see com.android.ddmlib.IDevice#runEventLogService(com.android.ddmlib.log.LogReceiver)
*/
public void runEventLogService(LogReceiver receiver) throws IOException {
        AdbHelper.runEventLogService(AndroidDebugBridge.getSocketAddress(), this, receiver);
}

/*
//Synthetic comment -- @@ -295,7 +295,7 @@
*/
public void runLogService(String logname,
LogReceiver receiver) throws IOException {
        AdbHelper.runLogService(AndroidDebugBridge.getSocketAddress(), this, logname, receiver);
}

/*
//Synthetic comment -- @@ -304,7 +304,7 @@
*/
public boolean createForward(int localPort, int remotePort) {
try {
            return AdbHelper.createForward(AndroidDebugBridge.getSocketAddress(), this,
localPort, remotePort);
} catch (IOException e) {
Log.e("adb-forward", e); //$NON-NLS-1$
//Synthetic comment -- @@ -318,7 +318,7 @@
*/
public boolean removeForward(int localPort, int remotePort) {
try {
            return AdbHelper.removeForward(AndroidDebugBridge.getSocketAddress(), this,
localPort, remotePort);
} catch (IOException e) {
Log.e("adb-remove-forward", e); //$NON-NLS-1$








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java b/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java
//Synthetic comment -- index 0fd0651..175b657 100644

//Synthetic comment -- @@ -232,7 +232,7 @@

SocketChannel adbChannel = null;
try {
            adbChannel = SocketChannel.open(AndroidDebugBridge.getSocketAddress());
adbChannel.socket().setTcpNoDelay(true);
} catch (IOException e) {
}
//Synthetic comment -- @@ -732,7 +732,7 @@
SocketChannel clientSocket;
try {
clientSocket = AdbHelper.createPassThroughConnection(
                    AndroidDebugBridge.getSocketAddress(), device, pid);

// required for Selector
clientSocket.configureBlocking(false);







