/*Fix a possible deadlock in Eclipse/ADT.

The issue is that sometimes phones will stop responding to shell
commands launched through adb.

One of these commands is executed in DeviceMonitor#queryNewDeviceForInfo
However this is done from a loop that acquires a lock that is used in
other places, including building. This basically renders eclipse
useless as Eclipse will refuse to do anything until the build is
finished.

The fix is twofold:
First, move the call to queryNewDeviceForInfo outside of the
synchronized block.

This fixes the deadlock issue but this will not fix the problem
that the device monitor will still be stuck with a shell command
that never returns.

The second part of the fix is to add a timeout to shell command
execution. Additionnaly, this patch contains a lot of clean up
of the adb API in ddmlib, especially around exceptions thrown
during timeout.

Change-Id:Ice8ef787c825e0e7b535ff0bb939bf6f25e3d7e4*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java
//Synthetic comment -- index 7b39076..0f5a532 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ddmlib;

import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.log.LogReceiver;

import java.io.IOException;
//Synthetic comment -- @@ -51,17 +50,12 @@
*/
static class AdbResponse {
public AdbResponse() {
            // ioSuccess = okay = timeout = false;
message = "";
}

        public boolean ioSuccess; // read all expected data, no timeoutes

public boolean okay; // first 4 bytes in response were "OKAY"?

        public boolean timeout; // TODO: implement

        public String message; // diagnostic string
}

/**
//Synthetic comment -- @@ -72,9 +66,11 @@
* @param device the device to connect to. Can be null in which case the connection will be
* to the first available device.
* @param devicePort the port we're opening
*/
public static SocketChannel open(InetSocketAddress adbSockAddr,
            Device device, int devicePort) throws IOException {

SocketChannel adbChan = SocketChannel.open(adbSockAddr);
try {
//Synthetic comment -- @@ -88,17 +84,20 @@
byte[] req = createAdbForwardRequest(null, devicePort);
// Log.hexDump(req);

            if (write(adbChan, req) == false)
                throw new IOException("failed submitting request to ADB"); //$NON-NLS-1$

AdbResponse resp = readAdbResponse(adbChan, false);
            if (!resp.okay)
throw new IOException("connection request rejected"); //$NON-NLS-1$

adbChan.configureBlocking(true);
        } catch (IOException ioe) {
adbChan.close();
            throw ioe;
}

return adbChan;
//Synthetic comment -- @@ -112,9 +111,11 @@
* @param device the device to connect to. Can be null in which case the connection will be
* to the first available device.
* @param pid the process pid to connect to.
*/
public static SocketChannel createPassThroughConnection(InetSocketAddress adbSockAddr,
            Device device, int pid) throws IOException {

SocketChannel adbChan = SocketChannel.open(adbSockAddr);
try {
//Synthetic comment -- @@ -128,17 +129,20 @@
byte[] req = createJdwpForwardRequest(pid);
// Log.hexDump(req);

            if (write(adbChan, req) == false)
                throw new IOException("failed submitting request to ADB"); //$NON-NLS-1$

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (!resp.okay)
throw new IOException("connection request rejected: " + resp.message); //$NON-NLS-1$

adbChan.configureBlocking(true);
        } catch (IOException ioe) {
adbChan.close();
            throw ioe;
}

return adbChan;
//Synthetic comment -- @@ -196,17 +200,16 @@
* @param readDiagString If true, we're expecting an OKAY response to be
*      followed by a diagnostic string. Otherwise, we only expect the
*      diagnostic string to follow a FAIL.
*/
static AdbResponse readAdbResponse(SocketChannel chan, boolean readDiagString)
            throws IOException {

AdbResponse resp = new AdbResponse();

byte[] reply = new byte[4];
        if (read(chan, reply) == false) {
            return resp;
        }
        resp.ioSuccess = true;

if (isOkay(reply)) {
resp.okay = true;
//Synthetic comment -- @@ -216,38 +219,37 @@
}

// not a loop -- use "while" so we can use "break"
        while (readDiagString) {
            // length string is in next 4 bytes
            byte[] lenBuf = new byte[4];
            if (read(chan, lenBuf) == false) {
                Log.w("ddms", "Expected diagnostic string not found");
break;
}

            String lenStr = replyToString(lenBuf);

            int len;
            try {
                len = Integer.parseInt(lenStr, 16);
            } catch (NumberFormatException nfe) {
                Log.w("ddms", "Expected digits, got '" + lenStr + "': "
                        + lenBuf[0] + " " + lenBuf[1] + " " + lenBuf[2] + " "
                        + lenBuf[3]);
                Log.w("ddms", "reply was " + replyToString(reply));
                break;
            }

            byte[] msg = new byte[len];
            if (read(chan, msg) == false) {
                Log.w("ddms", "Failed reading diagnostic string, len=" + len);
                break;
            }

            resp.message = replyToString(msg);
            Log.v("ddms", "Got reply '" + replyToString(reply) + "', diag='"
                    + resp.message + "'");

            break;
}

return resp;
//Synthetic comment -- @@ -255,9 +257,11 @@

/**
* Retrieve the frame buffer from the device.
*/
    public static RawImage getFrameBuffer(InetSocketAddress adbSockAddr, Device device)
            throws IOException {

RawImage imageParams = new RawImage();
byte[] request = formAdbRequest("framebuffer:"); //$NON-NLS-1$
//Synthetic comment -- @@ -275,25 +279,17 @@
// to a specific device
setDevice(adbChan, device);

            if (write(adbChan, request) == false)
                throw new IOException("failed asking for frame buffer");

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (!resp.ioSuccess || !resp.okay) {
                Log.w("ddms", "Got timeout or unhappy response from ADB fb req: "
                        + resp.message);
                adbChan.close();
                return null;
}

// first the protocol version.
reply = new byte[4];
            if (read(adbChan, reply) == false) {
                Log.w("ddms", "got partial reply from ADB fb:");
                Log.hexDump("ddms", LogLevel.WARN, reply, 0, reply.length);
                adbChan.close();
                return null;
            }
ByteBuffer buf = ByteBuffer.wrap(reply);
buf.order(ByteOrder.LITTLE_ENDIAN);

//Synthetic comment -- @@ -304,12 +300,8 @@

// read the header
reply = new byte[headerSize * 4];
            if (read(adbChan, reply) == false) {
                Log.w("ddms", "got partial reply from ADB fb:");
                Log.hexDump("ddms", LogLevel.WARN, reply, 0, reply.length);
                adbChan.close();
                return null;
            }
buf = ByteBuffer.wrap(reply);
buf.order(ByteOrder.LITTLE_ENDIAN);

//Synthetic comment -- @@ -323,15 +315,10 @@
+ imageParams.size + ", width=" + imageParams.width
+ ", height=" + imageParams.height);

            if (write(adbChan, nudge) == false)
                throw new IOException("failed nudging");

reply = new byte[imageParams.size];
            if (read(adbChan, reply) == false) {
                Log.w("ddms", "got truncated reply from ADB fb data");
                adbChan.close();
                return null;
            }

imageParams.data = reply;
} finally {
//Synthetic comment -- @@ -344,12 +331,20 @@
}

/**
     * Execute a command on the device and retrieve the output. The output is
     * handed to "rcvr" as it arrives.
*/
    public static void executeRemoteCommand(InetSocketAddress adbSockAddr,
            String command, Device device, IShellOutputReceiver rcvr)
            throws IOException {
Log.v("ddms", "execute: running " + command);

SocketChannel adbChan = null;
//Synthetic comment -- @@ -363,17 +358,17 @@
setDevice(adbChan, device);

byte[] request = formAdbRequest("shell:" + command); //$NON-NLS-1$
            if (write(adbChan, request) == false)
                throw new IOException("failed submitting shell command");

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (!resp.ioSuccess || !resp.okay) {
Log.e("ddms", "ADB rejected shell command (" + command + "): " + resp.message);
throw new IOException("sad result from adb: " + resp.message);
}

byte[] data = new byte[16384];
ByteBuffer buf = ByteBuffer.wrap(data);
while (true) {
int count;

//Synthetic comment -- @@ -391,10 +386,19 @@
break;
} else if (count == 0) {
try {
                        Thread.sleep(WAIT_TIME * 5);
} catch (InterruptedException ie) {
}
} else {
if (rcvr != null) {
rcvr.addOutput(buf.array(), buf.arrayOffset(), buf.position());
}
//Synthetic comment -- @@ -412,26 +416,30 @@
/**
* Runs the Event log service on the {@link Device}, and provides its output to the
* {@link LogReceiver}.
* @param adbSockAddr the socket address to connect to adb
* @param device the Device on which to run the service
* @param rcvr the {@link LogReceiver} to receive the log output
     * @throws IOException
*/
public static void runEventLogService(InetSocketAddress adbSockAddr, Device device,
            LogReceiver rcvr) throws IOException {
runLogService(adbSockAddr, device, "events", rcvr); //$NON-NLS-1$
}

/**
* Runs a log service on the {@link Device}, and provides its output to the {@link LogReceiver}.
* @param adbSockAddr the socket address to connect to adb
* @param device the Device on which to run the service
* @param logName the name of the log file to output
* @param rcvr the {@link LogReceiver} to receive the log output
     * @throws IOException
*/
public static void runLogService(InetSocketAddress adbSockAddr, Device device, String logName,
            LogReceiver rcvr) throws IOException {
SocketChannel adbChan = null;

try {
//Synthetic comment -- @@ -443,12 +451,10 @@
setDevice(adbChan, device);

byte[] request = formAdbRequest("log:" + logName);
            if (write(adbChan, request) == false) {
                throw new IOException("failed to submit the log command");
            }

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (!resp.ioSuccess || !resp.okay) {
throw new IOException("Device rejected log command: " + resp.message);
}

//Synthetic comment -- @@ -490,10 +496,11 @@
* @param localPort the local port to forward
* @param remotePort the remote port.
* @return <code>true</code> if success.
     * @throws IOException
*/
public static boolean createForward(InetSocketAddress adbSockAddr, Device device, int localPort,
            int remotePort) throws IOException {

SocketChannel adbChan = null;
try {
//Synthetic comment -- @@ -504,21 +511,18 @@
"host-serial:%1$s:forward:tcp:%2$d;tcp:%3$d", //$NON-NLS-1$
device.getSerialNumber(), localPort, remotePort));

            if (write(adbChan, request) == false) {
                throw new IOException("failed to submit the forward command.");
            }

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (!resp.ioSuccess || !resp.okay) {
                throw new IOException("Device rejected command: " + resp.message);
}
} finally {
if (adbChan != null) {
adbChan.close();
}
}

        return true;
}

/**
//Synthetic comment -- @@ -528,10 +532,11 @@
* @param localPort the local port of the forward
* @param remotePort the remote port.
* @return <code>true</code> if success.
     * @throws IOException
*/
public static boolean removeForward(InetSocketAddress adbSockAddr, Device device, int localPort,
            int remotePort) throws IOException {

SocketChannel adbChan = null;
try {
//Synthetic comment -- @@ -542,21 +547,18 @@
"host-serial:%1$s:killforward:tcp:%2$d;tcp:%3$d", //$NON-NLS-1$
device.getSerialNumber(), localPort, remotePort));

            if (!write(adbChan, request)) {
                throw new IOException("failed to submit the remove forward command.");
            }

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (!resp.ioSuccess || !resp.okay) {
                throw new IOException("Device rejected command: " + resp.message);
}
} finally {
if (adbChan != null) {
adbChan.close();
}
}

        return true;
}

/**
//Synthetic comment -- @@ -584,22 +586,16 @@
/**
* Reads from the socket until the array is filled, or no more data is coming (because
* the socket closed or the timeout expired).
*
* @param chan the opened socket to read from. It must be in non-blocking
*      mode for timeouts to work
* @param data the buffer to store the read data into.
     * @return "true" if all data was read.
     * @throws IOException
*/
    static boolean read(SocketChannel chan, byte[] data) {
       try {
           read(chan, data, -1, DdmPreferences.getTimeOut());
       } catch (IOException e) {
           Log.d("ddms", "readAll: IOException: " + e.getMessage());
           return false;
       }

       return true;
}

/**
//Synthetic comment -- @@ -614,9 +610,9 @@
* @param data the buffer to store the read data into.
* @param length the length to read or -1 to fill the data buffer completely
* @param timeout The timeout value. A timeout of zero means "wait forever".
     * @throws IOException
*/
    static void read(SocketChannel chan, byte[] data, int length, int timeout) throws IOException {
ByteBuffer buf = ByteBuffer.wrap(data, 0, length != -1 ? length : data.length);
int numWaits = 0;

//Synthetic comment -- @@ -631,7 +627,7 @@
// TODO: need more accurate timeout?
if (timeout != 0 && numWaits * WAIT_TIME > timeout) {
Log.d("ddms", "read: timeout");
                    throw new IOException("timeout");
}
// non-blocking spin
try {
//Synthetic comment -- @@ -646,20 +642,15 @@
}

/**
     * Write until all data in "data" is written or the connection fails.
* @param chan the opened socket to write to.
* @param data the buffer to send.
     * @return "true" if all data was written.
*/
    static boolean write(SocketChannel chan, byte[] data) {
        try {
            write(chan, data, -1, DdmPreferences.getTimeOut());
        } catch (IOException e) {
            Log.e("ddms", e);
            return false;
        }

        return true;
}

/**
//Synthetic comment -- @@ -670,10 +661,11 @@
* @param data the buffer to send.
* @param length the length to write or -1 to send the whole buffer.
* @param timeout The timeout value. A timeout of zero means "wait forever".
     * @throws IOException
*/
static void write(SocketChannel chan, byte[] data, int length, int timeout)
            throws IOException {
ByteBuffer buf = ByteBuffer.wrap(data, 0, length != -1 ? length : data.length);
int numWaits = 0;

//Synthetic comment -- @@ -688,7 +680,7 @@
// TODO: need more accurate timeout?
if (timeout != 0 && numWaits * WAIT_TIME > timeout) {
Log.d("ddms", "write: timeout");
                    throw new IOException("timeout");
}
// non-blocking spin
try {
//Synthetic comment -- @@ -707,19 +699,18 @@
*
* @param adbChan the socket connection to adb
* @param device The device to talk to.
     * @throws IOException
*/
    static void setDevice(SocketChannel adbChan, Device device)
            throws IOException {
// if the device is not -1, then we first tell adb we're looking to talk
// to a specific device
if (device != null) {
String msg = "host:transport:" + device.getSerialNumber(); //$NON-NLS-1$
byte[] device_query = formAdbRequest(msg);

            if (write(adbChan, device_query) == false)
                throw new IOException("failed submitting device (" + device +
                        ") request to ADB");

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
if (!resp.okay)
//Synthetic comment -- @@ -735,7 +726,7 @@
* @param into what to reboot into (recovery, bootloader).  Or null to just reboot.
*/
public static void reboot(String into, InetSocketAddress adbSockAddr,
            Device device) throws IOException {
byte[] request;
if (into == null) {
request = formAdbRequest("reboot:"); //$NON-NLS-1$
//Synthetic comment -- @@ -752,8 +743,7 @@
// to a specific device
setDevice(adbChan, device);

            if (write(adbChan, request) == false)
                throw new IOException("failed asking for reboot");
} finally {
if (adbChan != null) {
adbChan.close();








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java
//Synthetic comment -- index a601c16..24c20b2 100644

//Synthetic comment -- @@ -246,7 +246,7 @@
* (non-Javadoc)
* @see com.android.ddmlib.IDevice#getSyncService()
*/
    public SyncService getSyncService() throws IOException {
SyncService syncService = new SyncService(AndroidDebugBridge.getSocketAddress(), this);
if (syncService.openSync()) {
return syncService;
//Synthetic comment -- @@ -267,34 +267,28 @@
* (non-Javadoc)
* @see com.android.ddmlib.IDevice#getScreenshot()
*/
    public RawImage getScreenshot() throws IOException {
return AdbHelper.getFrameBuffer(AndroidDebugBridge.getSocketAddress(), this);
}

    /*
     * (non-Javadoc)
     * @see com.android.ddmlib.IDevice#executeShellCommand(java.lang.String, com.android.ddmlib.IShellOutputReceiver)
     */
public void executeShellCommand(String command, IShellOutputReceiver receiver)
            throws IOException {
AdbHelper.executeRemoteCommand(AndroidDebugBridge.getSocketAddress(), command, this,
                receiver);
}

    /*
     * (non-Javadoc)
     * @see com.android.ddmlib.IDevice#runEventLogService(com.android.ddmlib.log.LogReceiver)
     */
    public void runEventLogService(LogReceiver receiver) throws IOException {
AdbHelper.runEventLogService(AndroidDebugBridge.getSocketAddress(), this, receiver);
}

    /*
     * (non-Javadoc)
     * @see com.android.ddmlib.IDevice#runLogService(com.android.ddmlib.log.LogReceiver)
     */
    public void runLogService(String logname,
            LogReceiver receiver) throws IOException {
AdbHelper.runLogService(AndroidDebugBridge.getSocketAddress(), this, logname, receiver);
}

//Synthetic comment -- @@ -306,6 +300,9 @@
try {
return AdbHelper.createForward(AndroidDebugBridge.getSocketAddress(), this,
localPort, remotePort);
} catch (IOException e) {
Log.e("adb-forward", e); //$NON-NLS-1$
return false;
//Synthetic comment -- @@ -320,6 +317,9 @@
try {
return AdbHelper.removeForward(AndroidDebugBridge.getSocketAddress(), this,
localPort, remotePort);
} catch (IOException e) {
Log.e("adb-remove-forward", e); //$NON-NLS-1$
return false;
//Synthetic comment -- @@ -431,7 +431,7 @@
* {@inheritDoc}
*/
public String installPackage(String packageFilePath, boolean reinstall)
           throws IOException {
String remoteFilePath = syncPackageToDevice(packageFilePath);
String result = installRemotePackage(remoteFilePath, reinstall);
removeRemotePackage(remoteFilePath);
//Synthetic comment -- @@ -442,7 +442,7 @@
* {@inheritDoc}
*/
public String syncPackageToDevice(String localFilePath)
            throws IOException {
try {
String packageFileName = getFileName(localFilePath);
String remoteFilePath = String.format("/data/local/tmp/%1$s", packageFileName); //$NON-NLS-1$
//Synthetic comment -- @@ -466,6 +466,9 @@
throw new IOException("Unable to open sync connection!");
}
return remoteFilePath;
} catch (IOException e) {
Log.e(LOG_TAG, String.format("Unable to open sync connection! reason: %1$s",
e.getMessage()));
//Synthetic comment -- @@ -486,7 +489,7 @@
* {@inheritDoc}
*/
public String installRemotePackage(String remoteFilePath, boolean reinstall)
            throws IOException {
InstallReceiver receiver = new InstallReceiver();
String cmd = String.format(reinstall ? "pm install -r \"%1$s\"" : "pm install \"%1$s\"",
remoteFilePath);
//Synthetic comment -- @@ -497,7 +500,7 @@
/**
* {@inheritDoc}
*/
    public void removeRemotePackage(String remoteFilePath) throws IOException {
// now we delete the app we sync'ed
try {
executeShellCommand("rm " + remoteFilePath, new NullOutputReceiver());
//Synthetic comment -- @@ -511,7 +514,7 @@
/**
* {@inheritDoc}
*/
    public String uninstallPackage(String packageName) throws IOException {
InstallReceiver receiver = new InstallReceiver();
executeShellCommand("pm uninstall " + packageName, receiver);
return receiver.getErrorMessage();
//Synthetic comment -- @@ -521,7 +524,7 @@
* (non-Javadoc)
* @see com.android.ddmlib.IDevice#reboot()
*/
    public void reboot(String into) throws IOException {
AdbHelper.reboot(into, AndroidDebugBridge.getSocketAddress(), this);
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java b/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java
//Synthetic comment -- index 175b657..82b3bc1 100644

//Synthetic comment -- @@ -196,23 +196,33 @@
}
} catch (AsynchronousCloseException ace) {
// this happens because of a call to Quit. We do nothing, and the loop will break.
} catch (IOException ioe) {
                if (mQuit == false) {
                    Log.e("DeviceMonitor", "Adb connection Error:" + ioe.getMessage());
                    mMonitoring = false;
                    if (mMainAdbConnection != null) {
                        try {
                            mMainAdbConnection.close();
                        } catch (IOException ioe2) {
                            // we can safely ignore that one.
                        }
                        mMainAdbConnection = null;
                    }
                }
}
} while (mQuit == false);
}

/**
* Sleeps for a little bit.
*/
//Synthetic comment -- @@ -245,30 +255,26 @@
* @return
* @throws IOException
*/
    private boolean sendDeviceListMonitoringRequest() throws IOException {
byte[] request = AdbHelper.formAdbRequest("host:track-devices"); //$NON-NLS-1$

        if (AdbHelper.write(mMainAdbConnection, request) == false) {
Log.e("DeviceMonitor", "Sending Tracking request failed!");
mMainAdbConnection.close();
            throw new IOException("Sending Tracking request failed!");
}

        AdbResponse resp = AdbHelper.readAdbResponse(mMainAdbConnection,
                false /* readDiagString */);

        if (resp.ioSuccess == false) {
            Log.e("DeviceMonitor", "Failed to read the adb response!");
            mMainAdbConnection.close();
            throw new IOException("Failed to read the adb response!");
        }

        if (resp.okay == false) {
            // request was refused by adb!
            Log.e("DeviceMonitor", "adb refused request: " + resp.message);
        }

        return resp.okay;
}

/**
//Synthetic comment -- @@ -310,6 +316,10 @@
// because we are going to call mServer.deviceDisconnected which will acquire this lock
// we lock it first, so that the AndroidDebugBridge lock is always locked first.
synchronized (AndroidDebugBridge.getLock()) {
synchronized (mDevices) {
// For each device in the current list, we look for a matching the new list.
// * if we find it, we update the current object with whatever new information
//Synthetic comment -- @@ -349,7 +359,7 @@
}

if (device.getPropertyCount() == 0) {
                                        queryNewDeviceForInfo(device);
}
}
}
//Synthetic comment -- @@ -387,10 +397,15 @@

// look for their build info.
if (newDevice.isOnline()) {
                        queryNewDeviceForInfo(newDevice);
}
}
}
}
newList.clear();
}
//Synthetic comment -- @@ -431,13 +446,16 @@
device.setAvdName(console.getAvdName());
}
}
} catch (IOException e) {
// if we can't get the build info, it doesn't matter too much
}
}

private void queryNewDeviceForMountingPoint(final Device device, final String name)
            throws IOException {
device.executeShellCommand("echo $" + name, new MultiLineReceiver() { //$NON-NLS-1$
public boolean isCancelled() {
return false;
//Synthetic comment -- @@ -486,6 +504,16 @@

return true;
}
} catch (IOException e) {
try {
// attempt to close the socket if needed.
//Synthetic comment -- @@ -608,32 +636,30 @@
}

private boolean sendDeviceMonitoringRequest(SocketChannel socket, Device device)
            throws IOException {

        AdbHelper.setDevice(socket, device);

        byte[] request = AdbHelper.formAdbRequest("track-jdwp"); //$NON-NLS-1$

        if (AdbHelper.write(socket, request) == false) {
Log.e("DeviceMonitor", "Sending jdwp tracking request failed!");
            socket.close();
            throw new IOException();
}

        AdbResponse resp = AdbHelper.readAdbResponse(socket, false /* readDiagString */);

        if (resp.ioSuccess == false) {
            Log.e("DeviceMonitor", "Failed to read the adb response!");
            socket.close();
            throw new IOException();
        }

        if (resp.okay == false) {
            // request was refused by adb!
            Log.e("DeviceMonitor", "adb refused request: " + resp.message);
        }

        return resp.okay;
}

private void processIncomingJdwpData(Device device, SocketChannel monitorSocket, int length)
//Synthetic comment -- @@ -739,6 +765,10 @@
} catch (UnknownHostException uhe) {
Log.d("DeviceMonitor", "Unknown Jdwp pid: " + pid);
return;
} catch (IOException ioe) {
Log.w("DeviceMonitor",
"Failed to connect to client '" + pid + "': " + ioe.getMessage());








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java b/ddms/libs/ddmlib/src/com/android/ddmlib/EmulatorConsole.java
//Synthetic comment -- index 1a126b4..43d0c0c 100644

//Synthetic comment -- @@ -545,7 +545,7 @@
AdbHelper.write(mSocketChannel, bCommand, bCommand.length, DdmPreferences.getTimeOut());

result = true;
        } catch (IOException e) {
return false;
} finally {
if (result == false) {








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/FileListingService.java
//Synthetic comment -- index f85d020..bfeeea2 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ddmlib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//Synthetic comment -- @@ -700,7 +699,7 @@
return false;
}
});
                    } catch (IOException e) {
// adb failed somehow, we do nothing.
}
}
//Synthetic comment -- @@ -756,7 +755,8 @@

// finish the process of the receiver to handle links
receiver.finishLinks();
        } catch (IOException e) {
}










//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java b/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java
//Synthetic comment -- index bd6f436..d75c9c3 100755

//Synthetic comment -- @@ -168,9 +168,10 @@
* Returns a {@link SyncService} object to push / pull files to and from the device.
* @return <code>null</code> if the SyncService couldn't be created. This can happen if adb
* refuse to open the connection because the {@link IDevice} is invalid (or got disconnected).
* @throws IOException if the connection with adb failed.
*/
    public SyncService getSyncService() throws IOException;

/**
* Returns a {@link FileListingService} for this device.
//Synthetic comment -- @@ -181,33 +182,74 @@
* Takes a screen shot of the device and returns it as a {@link RawImage}.
* @return the screenshot as a <code>RawImage</code> or <code>null</code> if
* something went wrong.
     * @throws IOException
*/
    public RawImage getScreenshot() throws IOException;

/**
     * Executes a shell command on the device, and sends the result to a receiver.
     * @param command The command to execute
     * @param receiver The receiver object getting the result from the command.
     * @throws IOException
*/
public void executeShellCommand(String command,
            IShellOutputReceiver receiver) throws IOException;

/**
* Runs the event log service and outputs the event log to the {@link LogReceiver}.
* @param receiver the receiver to receive the event log entries.
     * @throws IOException
*/
    public void runEventLogService(LogReceiver receiver) throws IOException;

/**
* Runs the log service for the given log and outputs the log to the {@link LogReceiver}.
* @param logname the logname of the log to read from.
* @param receiver the receiver to receive the event log entries.
     * @throws IOException
*/
    public void runLogService(String logname, LogReceiver receiver) throws IOException;

/**
* Creates a port forwarding between a local and a remote port.
//Synthetic comment -- @@ -238,42 +280,48 @@
* @param packageFilePath the absolute file system path to file on local host to install
* @param reinstall set to <code>true</code> if re-install of app should be performed
* @return a {@link String} with an error code, or <code>null</code> if success.
     * @throws IOException
*/
    public String installPackage(String packageFilePath, boolean reinstall)  throws IOException;

/**
* Pushes a file to device
* @param localFilePath the absolute path to file on local host
* @return {@link String} destination path on device for file
     * @throws IOException if fatal error occurred when pushing file
*/
public String syncPackageToDevice(String localFilePath)
            throws IOException;

/**
* Installs the application package that was pushed to a temporary location on the device.
* @param remoteFilePath absolute file path to package file on device
* @param reinstall set to <code>true</code> if re-install of app should be performed
     * @throws InstallException if installation failed
*/
public String installRemotePackage(String remoteFilePath, boolean reinstall)
            throws IOException;

/**
* Remove a file from device
* @param remoteFilePath path on device of file to remove
* @throws IOException if file removal failed
*/
    public void removeRemotePackage(String remoteFilePath) throws IOException;

/**
* Uninstall an package from the device.
* @param packageName the Android application package name to uninstall
* @return a {@link String} with an error code, or <code>null</code> if success.
* @throws IOException
*/
    public String uninstallPackage(String packageName) throws IOException;

/**
* Reboot the device.
//Synthetic comment -- @@ -281,5 +329,5 @@
* @param into the bootloader name to reboot into, or null to just reboot the device.
* @throws IOException
*/
    public void reboot(String into) throws IOException;
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java
//Synthetic comment -- index 9f6b561..e4740d0 100644

//Synthetic comment -- @@ -108,6 +108,8 @@
public static final int RESULT_REMOTE_IS_FILE = 13;
/** Result code for receiving too much data from the remove device at once */
public static final int RESULT_BUFFER_OVERRUN = 14;

/**
* A file transfer result.
//Synthetic comment -- @@ -211,9 +213,10 @@
* Opens the sync connection. This must be called before any calls to push[File] / pull[File].
* @return true if the connection opened, false if adb refuse the connection. This can happen
* if the {@link Device} is invalid.
* @throws IOException If the connection to adb failed.
*/
    boolean openSync() throws IOException {
try {
mChannel = SocketChannel.open(mAddress);
mChannel.configureBlocking(false);
//Synthetic comment -- @@ -226,14 +229,23 @@

AdbResponse resp = AdbHelper.readAdbResponse(mChannel, false /* readDiagString */);

            if (!resp.ioSuccess || !resp.okay) {
                Log.w("ddms",
                        "Got timeout or unhappy response from ADB sync req: "
                        + resp.message);
mChannel.close();
mChannel = null;
return false;
}
} catch (IOException e) {
if (mChannel != null) {
try {
//Synthetic comment -- @@ -309,6 +321,8 @@
return "Remote path is a file.";
case RESULT_BUFFER_OVERRUN:
return "Receiving too much data.";
}

throw new RuntimeException();
//Synthetic comment -- @@ -586,6 +600,8 @@
}
} catch (UnsupportedEncodingException e) {
return new SyncResult(RESULT_REMOTE_PATH_ENCODING, e);
} catch (IOException e) {
return new SyncResult(RESULT_CONNECTION_ERROR, e);
}
//Synthetic comment -- @@ -633,6 +649,8 @@

// get the header for the next packet.
AdbHelper.read(mChannel, pullResult, -1, timeOut);
} catch (IOException e) {
return new SyncResult(RESULT_CONNECTION_ERROR, e);
}
//Synthetic comment -- @@ -739,6 +757,8 @@
// file and network IO exceptions.
try {
AdbHelper.write(mChannel, msg, -1, timeOut);
} catch (IOException e) {
return new SyncResult(RESULT_CONNECTION_ERROR, e);
}
//Synthetic comment -- @@ -777,6 +797,8 @@
// now write it
try {
AdbHelper.write(mChannel, mBuffer, readCount+8, timeOut);
} catch (IOException e) {
return new SyncResult(RESULT_CONNECTION_ERROR, e);
}
//Synthetic comment -- @@ -819,6 +841,8 @@

return new SyncResult(RESULT_UNKNOWN_ERROR);
}
} catch (IOException e) {
return new SyncResult(RESULT_CONNECTION_ERROR, e);
}
//Synthetic comment -- @@ -831,29 +855,27 @@
* @param path the remote file
* @return and Integer containing the mode if all went well or null
*      otherwise
*/
    private Integer readMode(String path) {
        try {
            // create the stat request message.
            byte[] msg = createFileReq(ID_STAT, path);

            AdbHelper.write(mChannel, msg, -1 /* full length */, DdmPreferences.getTimeOut());

            // read the result, in a byte array containing 4 ints
            // (id, mode, size, time)
            byte[] statResult = new byte[16];
            AdbHelper.read(mChannel, statResult, -1 /* full length */, DdmPreferences.getTimeOut());

            // check we have the proper data back
            if (checkResult(statResult, ID_STAT) == false) {
                return null;
            }

            // we return the mode (2nd int in the array)
            return ArrayHelper.swap32bitFromArray(statResult, 4);
        } catch (IOException e) {
return null;
}
}

/**








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/TimeoutException.java b/ddms/libs/ddmlib/src/com/android/ddmlib/TimeoutException.java
new file mode 100644
//Synthetic comment -- index 0000000..25be2f9

//Synthetic comment -- @@ -0,0 +1,27 @@








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/log/EventLogParser.java b/ddms/libs/ddmlib/src/com/android/ddmlib/log/EventLogParser.java
//Synthetic comment -- index 0b2ce69..31f265f 100644

//Synthetic comment -- @@ -97,7 +97,8 @@
return false;
}
});
        } catch (IOException e) {
return false;
}









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/IRemoteAndroidTestRunner.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/IRemoteAndroidTestRunner.java
//Synthetic comment -- index cd40527..22cafa6 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ddmlib.testrunner;

import java.io.IOException;
import java.util.Collection;

//Synthetic comment -- @@ -173,17 +175,19 @@
* Convenience method for {@link #run(Collection)}.
*
* @param listeners listens for test results
* @throws IOException if connection to device was lost.
*/
    public void run(ITestRunListener... listeners) throws IOException;

/**
* Execute this test run.
*
* @param listeners collection of listeners for test results
* @throws IOException if connection to device was lost.
*/
    public void run(Collection<ITestRunListener> listeners) throws IOException;

/**
* Requests cancellation of this test run.








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunner.java
//Synthetic comment -- index c0ae309..7edbe74 100644

//Synthetic comment -- @@ -19,6 +19,7 @@

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;

import java.io.IOException;
import java.util.Arrays;
//Synthetic comment -- @@ -201,14 +202,14 @@
/**
* {@inheritDoc}
*/
    public void run(ITestRunListener... listeners)  throws IOException {
run(Arrays.asList(listeners));
}

/**
* {@inheritDoc}
*/
    public void run(Collection<ITestRunListener> listeners)  throws IOException {
final String runCaseCommandStr = String.format("am instrument -w -r %s %s",
getArgsCommand(), getRunnerPath());
Log.i(LOG_TAG, String.format("Running %s on %s", runCaseCommandStr,








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java
//Synthetic comment -- index d365248..a884c3b 100644

//Synthetic comment -- @@ -130,6 +130,14 @@
}

/**
* Get the last command provided to executeShellCommand.
*/
public String getLastShellCommand() {








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java
//Synthetic comment -- index 297d731..710d7eb 100644

//Synthetic comment -- @@ -513,7 +513,7 @@

try {
mCurrentLoggedDevice = device;
                        device.executeShellCommand("logcat -v long", mCurrentLogCat); //$NON-NLS-1$
} catch (Exception e) {
Log.e("Logcat", e);
} finally {







