/*Add new exceptions to ddmlib.

- AdbCommandRejectedException is thrown when adb doesn't respond
to the command with OKAY. This used to throw a normal IOException
but it can be useful to throw a different type. The message
of the exception is the error string returned by adb.

- ShellCommandUnresponsiveException is the new "timeout" exception
for output received by shell command running on devices. This
makes the distinction between timeout when talking to adb and
issue with shell command not outputting anything. Also made the
javadoc for the IDevice.executeShellCommand clearer to what the
"timeout" (renamed to maxTimeToOutputResponse) does.

Also added a better timeout to the IDevice methods to install/uninstall
apps as the default 5sec timeout was likely to be too low.
Current default value is 2min.

Change-Id:I4ecb9498926295a4e801e71b33df5d611e8120b8*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AdbCommandRejectedException.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AdbCommandRejectedException.java
new file mode 100644
//Synthetic comment -- index 0000000..673acb5

//Synthetic comment -- @@ -0,0 +1,56 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java
//Synthetic comment -- index f31e27f..1f343b2 100644

//Synthetic comment -- @@ -68,9 +68,11 @@
* @param devicePort the port we're opening
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
public static SocketChannel open(InetSocketAddress adbSockAddr,
            Device device, int devicePort) throws IOException, TimeoutException {

SocketChannel adbChan = SocketChannel.open(adbSockAddr);
try {
//Synthetic comment -- @@ -87,8 +89,8 @@
write(adbChan, req);

AdbResponse resp = readAdbResponse(adbChan, false);
            if (!resp.okay) {
                throw new IOException("connection request rejected"); //$NON-NLS-1$
}

adbChan.configureBlocking(true);
//Synthetic comment -- @@ -112,10 +114,12 @@
* to the first available device.
* @param pid the process pid to connect to.
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
public static SocketChannel createPassThroughConnection(InetSocketAddress adbSockAddr,
            Device device, int pid) throws TimeoutException, IOException {

SocketChannel adbChan = SocketChannel.open(adbSockAddr);
try {
//Synthetic comment -- @@ -132,8 +136,8 @@
write(adbChan, req);

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (!resp.okay) {
                throw new IOException("connection request rejected: " + resp.message); //$NON-NLS-1$
}

adbChan.configureBlocking(true);
//Synthetic comment -- @@ -258,10 +262,11 @@
/**
* Retrieve the frame buffer from the device.
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
static RawImage getFrameBuffer(InetSocketAddress adbSockAddr, Device device)
            throws TimeoutException, IOException {

RawImage imageParams = new RawImage();
byte[] request = formAdbRequest("framebuffer:"); //$NON-NLS-1$
//Synthetic comment -- @@ -283,7 +288,7 @@

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
if (resp.okay == false) {
                throw new IOException(resp.message);
}

// first the protocol version.
//Synthetic comment -- @@ -338,16 +343,22 @@
* @param device the {@link IDevice} on which to execute the command.
* @param rcvr the {@link IShellOutputReceiver} that will receives the output of the shell
* command
     * @param timeout timeout value in ms for the connection. 0 means no timeout. This only affects
* the timeout for reading the command output. Execution setup uses the normal timeout.
     * @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*
* @see DdmPreferences#getTimeOut()
*/
static void executeRemoteCommand(InetSocketAddress adbSockAddr,
            String command, IDevice device, IShellOutputReceiver rcvr, int timeout)
            throws TimeoutException, IOException {
Log.v("ddms", "execute: running " + command);

SocketChannel adbChan = null;
//Synthetic comment -- @@ -366,12 +377,12 @@
AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
if (resp.okay == false) {
Log.e("ddms", "ADB rejected shell command (" + command + "): " + resp.message);
                throw new IOException("sad result from adb: " + resp.message);
}

byte[] data = new byte[16384];
ByteBuffer buf = ByteBuffer.wrap(data);
            int timeoutCount = 0;
while (true) {
int count;

//Synthetic comment -- @@ -390,16 +401,16 @@
} else if (count == 0) {
try {
int wait = WAIT_TIME * 5;
                        timeoutCount += wait;
                        if (timeout > 0 && timeoutCount > timeout) {
                            throw new TimeoutException();
}
Thread.sleep(wait);
} catch (InterruptedException ie) {
}
} else {
// reset timeout
                    timeoutCount = 0;

// send data to receiver if present
if (rcvr != null) {
//Synthetic comment -- @@ -424,10 +435,11 @@
* @param device the Device on which to run the service
* @param rcvr the {@link LogReceiver} to receive the log output
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
public static void runEventLogService(InetSocketAddress adbSockAddr, Device device,
            LogReceiver rcvr) throws TimeoutException, IOException {
runLogService(adbSockAddr, device, "events", rcvr); //$NON-NLS-1$
}

//Synthetic comment -- @@ -439,10 +451,11 @@
* @param logName the name of the log file to output
* @param rcvr the {@link LogReceiver} to receive the log output
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
public static void runLogService(InetSocketAddress adbSockAddr, Device device, String logName,
            LogReceiver rcvr) throws TimeoutException, IOException {
SocketChannel adbChan = null;

try {
//Synthetic comment -- @@ -458,7 +471,7 @@

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
if (resp.okay == false) {
                throw new IOException("Device rejected log command: " + resp.message);
}

byte[] data = new byte[16384];
//Synthetic comment -- @@ -498,12 +511,12 @@
* @param device the device on which to do the port fowarding
* @param localPort the local port to forward
* @param remotePort the remote port.
     * @return <code>true</code> if success.
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
    public static boolean createForward(InetSocketAddress adbSockAddr, Device device, int localPort,
            int remotePort) throws TimeoutException, IOException {

SocketChannel adbChan = null;
try {
//Synthetic comment -- @@ -519,8 +532,8 @@
AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
if (resp.okay == false) {
Log.w("create-forward", "Error creating forward: " + resp.message);
}
            return resp.okay;
} finally {
if (adbChan != null) {
adbChan.close();
//Synthetic comment -- @@ -534,12 +547,12 @@
* @param device the device on which to remove the port fowarding
* @param localPort the local port of the forward
* @param remotePort the remote port.
     * @return <code>true</code> if success.
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
    public static boolean removeForward(InetSocketAddress adbSockAddr, Device device, int localPort,
            int remotePort) throws TimeoutException, IOException {

SocketChannel adbChan = null;
try {
//Synthetic comment -- @@ -555,8 +568,8 @@
AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
if (resp.okay == false) {
Log.w("remove-forward", "Error creating forward: " + resp.message);
}
            return resp.okay;
} finally {
if (adbChan != null) {
adbChan.close();
//Synthetic comment -- @@ -703,10 +716,11 @@
* @param adbChan the socket connection to adb
* @param device The device to talk to.
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
static void setDevice(SocketChannel adbChan, IDevice device)
            throws TimeoutException, IOException {
// if the device is not -1, then we first tell adb we're looking to talk
// to a specific device
if (device != null) {
//Synthetic comment -- @@ -716,20 +730,23 @@
write(adbChan, device_query);

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (!resp.okay)
                throw new IOException("device (" + device +
                        ") request rejected: " + resp.message);
}

}

/**
* Reboot the device.
*
* @param into what to reboot into (recovery, bootloader).  Or null to just reboot.
*/
public static void reboot(String into, InetSocketAddress adbSockAddr,
            Device device) throws TimeoutException, IOException {
byte[] request;
if (into == null) {
request = formAdbRequest("reboot:"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java
//Synthetic comment -- index 24c20b2..7eff98e 100644

//Synthetic comment -- @@ -36,6 +36,8 @@
*/
final class Device implements IDevice {

/** Emulator Serial Number regexp. */
final static String RE_EMULATOR_SN = "emulator-(\\d+)"; //$NON-NLS-1$

//Synthetic comment -- @@ -246,7 +248,8 @@
* (non-Javadoc)
* @see com.android.ddmlib.IDevice#getSyncService()
*/
    public SyncService getSyncService() throws TimeoutException, IOException {
SyncService syncService = new SyncService(AndroidDebugBridge.getSocketAddress(), this);
if (syncService.openSync()) {
return syncService;
//Synthetic comment -- @@ -263,67 +266,44 @@
return new FileListingService(this);
}

    /*
     * (non-Javadoc)
     * @see com.android.ddmlib.IDevice#getScreenshot()
     */
    public RawImage getScreenshot() throws TimeoutException, IOException {
return AdbHelper.getFrameBuffer(AndroidDebugBridge.getSocketAddress(), this);
}

public void executeShellCommand(String command, IShellOutputReceiver receiver)
            throws TimeoutException, IOException {
AdbHelper.executeRemoteCommand(AndroidDebugBridge.getSocketAddress(), command, this,
receiver, DdmPreferences.getTimeOut());
}

    public void executeShellCommand(String command, IShellOutputReceiver receiver, int timeout)
            throws TimeoutException, IOException {
AdbHelper.executeRemoteCommand(AndroidDebugBridge.getSocketAddress(), command, this,
                receiver, timeout);
}

    public void runEventLogService(LogReceiver receiver) throws TimeoutException, IOException {
AdbHelper.runEventLogService(AndroidDebugBridge.getSocketAddress(), this, receiver);
}

public void runLogService(String logname, LogReceiver receiver)
            throws TimeoutException, IOException {
AdbHelper.runLogService(AndroidDebugBridge.getSocketAddress(), this, logname, receiver);
}

    /*
     * (non-Javadoc)
     * @see com.android.ddmlib.IDevice#createForward(int, int)
     */
    public boolean createForward(int localPort, int remotePort) {
        try {
            return AdbHelper.createForward(AndroidDebugBridge.getSocketAddress(), this,
                    localPort, remotePort);
        } catch (TimeoutException e) {
            Log.e("adb-forward", "timeout");
            return false;
        } catch (IOException e) {
            Log.e("adb-forward", e); //$NON-NLS-1$
            return false;
        }
}

    /*
     * (non-Javadoc)
     * @see com.android.ddmlib.IDevice#removeForward(int, int)
     */
    public boolean removeForward(int localPort, int remotePort) {
        try {
            return AdbHelper.removeForward(AndroidDebugBridge.getSocketAddress(), this,
                    localPort, remotePort);
        } catch (TimeoutException e) {
            Log.e("adb-remove-forward", "timeout");
            return false;
        } catch (IOException e) {
            Log.e("adb-remove-forward", e); //$NON-NLS-1$
            return false;
        }
}

/*
//Synthetic comment -- @@ -427,22 +407,17 @@
mMountPoints.put(name, value);
}

    /**
     * {@inheritDoc}
     */
public String installPackage(String packageFilePath, boolean reinstall)
           throws TimeoutException, IOException {
String remoteFilePath = syncPackageToDevice(packageFilePath);
String result = installRemotePackage(remoteFilePath, reinstall);
removeRemotePackage(remoteFilePath);
return result;
}

    /**
     * {@inheritDoc}
     */
public String syncPackageToDevice(String localFilePath)
            throws IOException, TimeoutException {
try {
String packageFileName = getFileName(localFilePath);
String remoteFilePath = String.format("/data/local/tmp/%1$s", packageFileName); //$NON-NLS-1$
//Synthetic comment -- @@ -485,25 +460,25 @@
return new File(filePath).getName();
}

    /**
     * {@inheritDoc}
     */
public String installRemotePackage(String remoteFilePath, boolean reinstall)
            throws TimeoutException, IOException {
InstallReceiver receiver = new InstallReceiver();
String cmd = String.format(reinstall ? "pm install -r \"%1$s\"" : "pm install \"%1$s\"",
remoteFilePath);
        executeShellCommand(cmd, receiver);
return receiver.getErrorMessage();
}

/**
* {@inheritDoc}
*/
    public void removeRemotePackage(String remoteFilePath) throws TimeoutException, IOException {
// now we delete the app we sync'ed
try {
            executeShellCommand("rm " + remoteFilePath, new NullOutputReceiver());
} catch (IOException e) {
Log.e(LOG_TAG, String.format("Failed to delete temporary package: %1$s",
e.getMessage()));
//Synthetic comment -- @@ -514,9 +489,11 @@
/**
* {@inheritDoc}
*/
    public String uninstallPackage(String packageName) throws TimeoutException, IOException {
InstallReceiver receiver = new InstallReceiver();
        executeShellCommand("pm uninstall " + packageName, receiver);
return receiver.getErrorMessage();
}

//Synthetic comment -- @@ -524,7 +501,8 @@
* (non-Javadoc)
* @see com.android.ddmlib.IDevice#reboot()
*/
    public void reboot(String into) throws TimeoutException, IOException {
AdbHelper.reboot(into, AndroidDebugBridge.getSocketAddress(), this);
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java b/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java
//Synthetic comment -- index 82b3bc1..21869af 100644

//Synthetic comment -- @@ -447,15 +447,30 @@
}
}
} catch (TimeoutException e) {
            Log.w("DeviceMonitor", String.format("Timeout getting device %s info",
device.getSerialNumber()));
} catch (IOException e) {
            // if we can't get the build info, it doesn't matter too much
}
}

private void queryNewDeviceForMountingPoint(final Device device, final String name)
            throws TimeoutException, IOException {
device.executeShellCommand("echo $" + name, new MultiLineReceiver() { //$NON-NLS-1$
public boolean isCancelled() {
return false;
//Synthetic comment -- @@ -514,6 +529,16 @@
Log.d("DeviceMonitor",
"Connection Failure when starting to monitor device '"
+ device + "' : timeout");
} catch (IOException e) {
try {
// attempt to close the socket if needed.
//Synthetic comment -- @@ -636,7 +661,7 @@
}

private boolean sendDeviceMonitoringRequest(SocketChannel socket, Device device)
            throws TimeoutException, IOException {

try {
AdbHelper.setDevice(socket, device);
//Synthetic comment -- @@ -769,6 +794,11 @@
Log.w("DeviceMonitor",
"Failed to connect to client '" + pid + "': timeout");
return;
} catch (IOException ioe) {
Log.w("DeviceMonitor",
"Failed to connect to client '" + pid + "': " + ioe.getMessage());








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java b/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java
//Synthetic comment -- index 59ae910..8865daf 100755

//Synthetic comment -- @@ -21,7 +21,6 @@
import java.io.IOException;
import java.util.Map;


/**
*  A Device. It can be a physical device or an emulator.
*/
//Synthetic comment -- @@ -169,9 +168,11 @@
* @return <code>null</code> if the SyncService couldn't be created. This can happen if adb
* refuse to open the connection because the {@link IDevice} is invalid (or got disconnected).
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException if the connection with adb failed.
*/
    public SyncService getSyncService() throws TimeoutException, IOException;

/**
* Returns a {@link FileListingService} for this device.
//Synthetic comment -- @@ -183,52 +184,63 @@
* @return the screenshot as a <code>RawImage</code> or <code>null</code> if
* something went wrong.
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
    public RawImage getScreenshot() throws TimeoutException, IOException;

/**
* Executes a shell command on the device, and sends the result to a <var>receiver</var>
     * <p/>This use the default timeout value returned by {@link DdmPreferences#getTimeOut()}.
* @param command the shell command to execute
* @param receiver the {@link IShellOutputReceiver} that will receives the output of the shell
* command
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*
* @see #executeShellCommand(String, IShellOutputReceiver, int)
*/
    public void executeShellCommand(String command,
            IShellOutputReceiver receiver) throws TimeoutException, IOException;

/**
* Executes a shell command on the device, and sends the result to a <var>receiver</var>.
     * <p/>The timeout value is used as a maximum waiting time when expecting the command
     * output from the device.<br>
     * If the shell command takes a long time to run before outputting anything, this may be
     * impacted by the timeout. For instance, if the command outputs one line every 10sec but the
     * timeout is set to 5sec (default value) then the method will timeout.
     * <p/>For commands like log output, a timeout value of 0 (no timeout, always blocking till the
     * receiver's {@link IShellOutputReceiver#isCancelled()} return <code>true</code> should be
* used.
     * <p/>When setting up the shell command to run, the normal timeout value is used while
     * communicating with adb.
*
* @param command the shell command to execute
* @param receiver the {@link IShellOutputReceiver} that will receives the output of the shell
* command
     * @param timeout timeout value in ms for the connection.
     * @param timeout the timeout. timeout value in ms for the connection.If 0, there is no timeout.
     * @throws TimeoutException in case of timeout on the connection. Even with a timeout parameter
     * of 0, timeout can happen during the setup of the shell command. Once command has launched,
     * no timeout will occur as it's not possible to detect a difference between no output and
     * no timeout.
* @throws IOException in case of I/O error on the connection.
*
* @see DdmPreferences#getTimeOut()
*/
    public void executeShellCommand(String command,
            IShellOutputReceiver receiver, int timeout) throws TimeoutException, IOException;

/**
* Runs the event log service and outputs the event log to the {@link LogReceiver}.
//Synthetic comment -- @@ -237,9 +249,11 @@
* @throws TimeoutException in case of timeout on the connection. This can only be thrown if the
* timeout happens during setup. Once logs start being received, no timeout will occur as it's
* not possible to detect a difference between no log and timeout.
* @throws IOException in case of I/O error on the connection.
*/
    public void runEventLogService(LogReceiver receiver) throws TimeoutException, IOException;

/**
* Runs the log service for the given log and outputs the log to the {@link LogReceiver}.
//Synthetic comment -- @@ -249,26 +263,35 @@
* @throws TimeoutException in case of timeout on the connection. This can only be thrown if the
* timeout happens during setup. Once logs start being received, no timeout will occur as it's
* not possible to detect a difference between no log and timeout.
* @throws IOException in case of I/O error on the connection.
*/
public void runLogService(String logname, LogReceiver receiver)
            throws TimeoutException, IOException;

/**
* Creates a port forwarding between a local and a remote port.
* @param localPort the local port to forward
* @param remotePort the remote port.
* @return <code>true</code> if success.
*/
    public boolean createForward(int localPort, int remotePort);

/**
* Removes a port forwarding between a local and a remote port.
* @param localPort the local port to forward
* @param remotePort the remote port.
* @return <code>true</code> if success.
*/
    public boolean removeForward(int localPort, int remotePort);

/**
* Returns the name of the client by pid or <code>null</code> if pid is unknown
//Synthetic comment -- @@ -284,53 +307,75 @@
* @param reinstall set to <code>true</code> if re-install of app should be performed
* @return a {@link String} with an error code, or <code>null</code> if success.
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
public String installPackage(String packageFilePath, boolean reinstall)
            throws TimeoutException, IOException;

/**
* Pushes a file to device
* @param localFilePath the absolute path to file on local host
* @return {@link String} destination path on device for file
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException in case of I/O error on the connection.
*/
public String syncPackageToDevice(String localFilePath)
            throws TimeoutException, IOException;

/**
* Installs the application package that was pushed to a temporary location on the device.
* @param remoteFilePath absolute file path to package file on device
* @param reinstall set to <code>true</code> if re-install of app should be performed
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException if installation failed
*/
public String installRemotePackage(String remoteFilePath, boolean reinstall)
            throws TimeoutException, IOException;

/**
     * Remove a file from device
* @param remoteFilePath path on device of file to remove
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException if file removal failed
*/
    public void removeRemotePackage(String remoteFilePath) throws TimeoutException, IOException;

/**
     * Uninstall an package from the device.
* @param packageName the Android application package name to uninstall
* @return a {@link String} with an error code, or <code>null</code> if success.
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException
*/
    public String uninstallPackage(String packageName) throws TimeoutException, IOException;

/**
* Reboot the device.
*
* @param into the bootloader name to reboot into, or null to just reboot the device.
* @throws IOException
*/
    public void reboot(String into) throws TimeoutException, IOException;
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/ShellCommandUnresponsiveException.java b/ddms/libs/ddmlib/src/com/android/ddmlib/ShellCommandUnresponsiveException.java
new file mode 100644
//Synthetic comment -- index 0000000..2dc5d3a

//Synthetic comment -- @@ -0,0 +1,28 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java
//Synthetic comment -- index e4740d0..d2b8af3 100644

//Synthetic comment -- @@ -214,9 +214,10 @@
* @return true if the connection opened, false if adb refuse the connection. This can happen
* if the {@link Device} is invalid.
* @throws TimeoutException in case of timeout on the connection.
* @throws IOException If the connection to adb failed.
*/
    boolean openSync() throws TimeoutException, IOException {
try {
mChannel = SocketChannel.open(mAddress);
mChannel.configureBlocking(false);








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/IRemoteAndroidTestRunner.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/IRemoteAndroidTestRunner.java
//Synthetic comment -- index a478216..7cb6557 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ddmlib.testrunner;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.TimeoutException;

import java.io.IOException;
//Synthetic comment -- @@ -171,13 +173,17 @@
public void setCoverage(boolean coverage);

/**
     * Sets the timeout to use for the adb shell command issued.
* <p/>
* By default no timeout will be specified.
*
* @see {@link IDevice#executeShellCommand(String, com.android.ddmlib.IShellOutputReceiver, int)}
*/
    public void setTimeout(int timeout);

/**
* Execute this test run.
//Synthetic comment -- @@ -186,18 +192,32 @@
*
* @param listeners listens for test results
* @throws TimeoutException in case of a timeout on the connection.
* @throws IOException if connection to device was lost.
*/
    public void run(ITestRunListener... listeners) throws TimeoutException, IOException;

/**
* Execute this test run.
*
* @param listeners collection of listeners for test results
* @throws TimeoutException in case of a timeout on the connection.
* @throws IOException if connection to device was lost.
*/
    public void run(Collection<ITestRunListener> listeners) throws TimeoutException, IOException;

/**
* Requests cancellation of this test run.








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java
//Synthetic comment -- index 9fd3fbf..681c214 100644

//Synthetic comment -- @@ -19,6 +19,8 @@

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.TimeoutException;

import java.io.IOException;
//Synthetic comment -- @@ -37,7 +39,7 @@
private final  String mRunnerName;
private IDevice mRemoteDevice;
// default to no timeout
    private int mAdbTimeout = 0;

/** map of name-value instrumentation argument pairs */
private Map<String, String> mArgMap;
//Synthetic comment -- @@ -205,28 +207,32 @@
/**
* {@inheritDoc}
*/
    public void setTimeout(int timeout) {
        mAdbTimeout = timeout;
}

/**
* {@inheritDoc}
*/
    public void run(ITestRunListener... listeners)  throws IOException, TimeoutException {
run(Arrays.asList(listeners));
}

/**
* {@inheritDoc}
*/
    public void run(Collection<ITestRunListener> listeners)  throws TimeoutException, IOException {
final String runCaseCommandStr = String.format("am instrument -w -r %s %s",
getArgsCommand(), getRunnerPath());
Log.i(LOG_TAG, String.format("Running %s on %s", runCaseCommandStr,
mRemoteDevice.getSerialNumber()));
mParser = new InstrumentationResultParser(listeners);

        mRemoteDevice.executeShellCommand(runCaseCommandStr, mParser, mAdbTimeout);
}

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java
//Synthetic comment -- index a884c3b..85b57aa 100644

//Synthetic comment -- @@ -144,7 +144,7 @@
return mLastShellCommand;
}

        public boolean createForward(int localPort, int remotePort) {
throw new UnsupportedOperationException();
}

//Synthetic comment -- @@ -216,7 +216,7 @@
throw new UnsupportedOperationException();
}

        public boolean removeForward(int localPort, int remotePort) {
throw new UnsupportedOperationException();
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/ActivityLaunchAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/ActivityLaunchAction.java
//Synthetic comment -- index d1502d3..0a6257f 100644

//Synthetic comment -- @@ -16,7 +16,10 @@

package com.android.ide.eclipse.adt.internal.launch;

import com.android.ddmlib.IDevice;
import com.android.ide.eclipse.adt.AdtPlugin;

import java.io.IOException;
//Synthetic comment -- @@ -49,8 +52,16 @@
* @see IAndroidLaunchAction#doLaunchAction(DelayedLaunchInfo, IDevice)
*/
public boolean doLaunchAction(DelayedLaunchInfo info, IDevice device) {
try {
            String msg = String.format("Starting activity %1$s on device ", mActivity,
device);
AdtPlugin.printToConsole(info.getProject(), msg);

//Synthetic comment -- @@ -60,15 +71,7 @@
info.incrementAttemptCount();

// now we actually launch the app.
            device.executeShellCommand("am start" //$NON-NLS-1$
                    + (info.isDebugMode() ? " -D" //$NON-NLS-1$
                            : "") //$NON-NLS-1$
                    + " -n " //$NON-NLS-1$
                    + info.getPackageName() + "/" //$NON-NLS-1$
                    + mActivity.replaceAll("\\$", "\\\\\\$") //$NON-NLS-1$ //$NON-NLS-2$
                    + " -a android.intent.action.MAIN"  //$NON-NLS-1$
                    + " -c android.intent.category.LAUNCHER",  //$NON-NLS-1$
                    new AMReceiver(info, device, mLaunchController));

// if the app is not a debug app, we need to do some clean up, as
// the process is done!
//Synthetic comment -- @@ -77,6 +80,17 @@
// provide any control over the app
return false;
}
} catch (IOException e) {
// something went wrong trying to launch the app.
// lets stop the Launch








//Synthetic comment -- diff --git a/hierarchyviewer/src/com/android/hierarchyviewer/device/DeviceBridge.java b/hierarchyviewer/src/com/android/hierarchyviewer/device/DeviceBridge.java
//Synthetic comment -- index 0f60be6..209577d 100644

//Synthetic comment -- @@ -16,10 +16,12 @@

package com.android.hierarchyviewer.device;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.MultiLineReceiver;

import java.io.IOException;
import java.io.File;
//Synthetic comment -- @@ -120,8 +122,20 @@
synchronized (devicePortMap) {
if (device.getState() == IDevice.DeviceState.ONLINE) {
int localPort = nextLocalPort++;
                device.createForward(localPort, Configuration.DEFAULT_SERVER_PORT);
                devicePortMap.put(device, localPort);
}
}
}
//Synthetic comment -- @@ -130,8 +144,20 @@
synchronized (devicePortMap) {
final Integer localPort = devicePortMap.get(device);
if (localPort != null) {
                device.removeForward(localPort, Configuration.DEFAULT_SERVER_PORT);
                devicePortMap.remove(device);
}
}
}







