/*Fix code style warnings.

Mostly removing unnecessary boxing and simplifying boolean expressions

Change-Id:If47b2290027d3ab17ccabc6a19d722517a1eeb7f*/
//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/AdbHelper.java b/ddmlib/src/main/java/com/android/ddmlib/AdbHelper.java
//Synthetic comment -- index aa47ea0..8bc42ca 100644

//Synthetic comment -- @@ -89,7 +89,7 @@
write(adbChan, req);

AdbResponse resp = readAdbResponse(adbChan, false);
            if (resp.okay == false) {
throw new AdbCommandRejectedException(resp.message);
}

//Synthetic comment -- @@ -136,7 +136,7 @@
write(adbChan, req);

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (resp.okay == false) {
throw new AdbCommandRejectedException(resp.message);
}

//Synthetic comment -- @@ -287,7 +287,7 @@
write(adbChan, request);

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (resp.okay == false) {
throw new AdbCommandRejectedException(resp.message);
}

//Synthetic comment -- @@ -311,7 +311,7 @@
buf.order(ByteOrder.LITTLE_ENDIAN);

// fill the RawImage with the header
            if (imageParams.readHeader(version, buf) == false) {
Log.e("Screenshot", "Unsupported protocol: " + version);
return null;
}
//Synthetic comment -- @@ -376,7 +376,7 @@
write(adbChan, request);

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (resp.okay == false) {
Log.e("ddms", "ADB rejected shell command (" + command + "): " + resp.message);
throw new AdbCommandRejectedException(resp.message);
}
//Synthetic comment -- @@ -472,7 +472,7 @@
write(adbChan, request);

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (resp.okay == false) {
throw new AdbCommandRejectedException(resp.message);
}

//Synthetic comment -- @@ -540,7 +540,7 @@
write(adbChan, request);

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (resp.okay == false) {
Log.w("create-forward", "Error creating forward: " + resp.message);
throw new AdbCommandRejectedException(resp.message);
}
//Synthetic comment -- @@ -584,7 +584,7 @@
write(adbChan, request);

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (resp.okay == false) {
Log.w("remove-forward", "Error creating forward: " + resp.message);
throw new AdbCommandRejectedException(resp.message);
}
//Synthetic comment -- @@ -748,7 +748,7 @@
write(adbChan, device_query);

AdbResponse resp = readAdbResponse(adbChan, false /* readDiagString */);
            if (resp.okay == false) {
throw new AdbCommandRejectedException(resp.message,
true/*errorDuringDeviceSelection*/);
}








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java b/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index 000d92a..1edc383 100644

//Synthetic comment -- @@ -307,7 +307,7 @@
synchronized (sLock) {
if (sThis != null) {
if (sThis.mAdbOsLocation != null && sThis.mAdbOsLocation.equals(osLocation) &&
                        forceNewBridge == false) {
return sThis;
} else {
// stop the current server
//Synthetic comment -- @@ -392,7 +392,7 @@
*/
public static void addDebugBridgeChangeListener(IDebugBridgeChangeListener listener) {
synchronized (sLock) {
            if (sBridgeListeners.contains(listener) == false) {
sBridgeListeners.add(listener);
if (sThis != null) {
// we attempt to catch any exception so that a bad listener doesn't kill our
//Synthetic comment -- @@ -426,7 +426,7 @@
*/
public static void addDeviceChangeListener(IDeviceChangeListener listener) {
synchronized (sLock) {
            if (sDeviceListeners.contains(listener) == false) {
sDeviceListeners.add(listener);
}
}
//Synthetic comment -- @@ -452,7 +452,7 @@
*/
public static void addClientChangeListener(IClientChangeListener listener) {
synchronized (sLock) {
            if (sClientListeners.contains(listener) == false) {
sClientListeners.add(listener);
}
}
//Synthetic comment -- @@ -551,7 +551,7 @@
* @throws InvalidParameterException
*/
private AndroidDebugBridge(String osLocation) throws InvalidParameterException {
        if (osLocation == null || osLocation.length() == 0) {
throw new InvalidParameterException();
}
mAdbOsLocation = osLocation;
//Synthetic comment -- @@ -705,7 +705,7 @@
* @return true if success.
*/
boolean start() {
        if (mAdbOsLocation != null && (mVersionCheck == false || startAdb() == false)) {
return false;
}

//Synthetic comment -- @@ -724,7 +724,7 @@
*/
boolean stop() {
// if we haven't started we return false;
        if (mStarted == false) {
return false;
}

//Synthetic comment -- @@ -732,7 +732,7 @@
mDeviceMonitor.stop();
mDeviceMonitor = null;

        if (stopAdb() == false) {
return false;
}

//Synthetic comment -- @@ -751,7 +751,7 @@
return false;
}

        if (mVersionCheck == false) {
Log.logAndDisplay(LogLevel.ERROR, ADB,
"Attempting to restart adb, but version check failed!"); //$NON-NLS-1$
return false;
//Synthetic comment -- @@ -947,7 +947,7 @@
ProcessBuilder processBuilder = new ProcessBuilder(command);
if (DdmPreferences.getUseAdbHost()) {
String adbHostValue = DdmPreferences.getAdbHostValue();
                if (adbHostValue != null && adbHostValue.length() > 0) {
//TODO : check that the String is a valid IP address
Map<String, String> env = processBuilder.environment();
env.put("ADBHOST", adbHostValue);
//Synthetic comment -- @@ -1142,7 +1142,7 @@
adb_env_var = adb_env_var.trim();
}

            if (adb_env_var != null && adb_env_var.length() > 0) {
// C tools (adb, emulator) accept hex and octal port numbers, so need to accept
// them too.
result = Integer.decode(adb_env_var);








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/Client.java b/ddmlib/src/main/java/com/android/ddmlib/Client.java
//Synthetic comment -- index cdc1647..2aac328 100644

//Synthetic comment -- @@ -328,7 +328,7 @@
*/
public void setThreadUpdateEnabled(boolean enabled) {
mThreadUpdateEnabled = enabled;
        if (enabled == false) {
mClientData.clearThreads();
}









//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/ClientData.java b/ddmlib/src/main/java/com/android/ddmlib/ClientData.java
//Synthetic comment -- index 3741df2..1e72523 100644

//Synthetic comment -- @@ -133,7 +133,7 @@
* String for feature indicating support for tracing OpenGL calls.
* @see #hasFeature(String)
*/
    public final static String FEATURE_OPENGL_TRACING = "opengl-tracing"; //$NON-NLS-1$

/**
* String for feature indicating support for providing view hierarchy.
//Synthetic comment -- @@ -472,7 +472,7 @@
* name to replace a specified one.
*/
void setClientDescription(String description) {
        if (mClientDescription == null && description.length() > 0) {
/*
* The application VM is first named <pre-initialized> before being assigned
* its real name.
//Synthetic comment -- @@ -480,7 +480,7 @@
* another one setting the final actual name. So if we get a SetClientDescription
* with this value we ignore it.
*/
            if (PRE_INITIALIZED.equals(description) == false) {
mClientDescription = description;
}
}








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/Device.java b/ddmlib/src/main/java/com/android/ddmlib/Device.java
//Synthetic comment -- index 88a0d9d..c3c745d 100644

//Synthetic comment -- @@ -99,7 +99,7 @@
@Override
public void processNewLines(String[] lines) {
for (String line : lines) {
                if (line.length() > 0) {
if (line.startsWith(SUCCESS_OUTPUT)) {
mErrorMessage = null;
} else {
//Synthetic comment -- @@ -191,7 +191,7 @@
* Sets the name of the AVD
*/
void setAvdName(String avdName) {
        if (isEmulator() == false) {
throw new IllegalArgumentException(
"Cannot set the AVD name of the device is not an emulator");
}
//Synthetic comment -- @@ -504,7 +504,7 @@
@Override
public boolean hasClients() {
synchronized (mClients) {
            return mClients.size() > 0;
}
}

//Synthetic comment -- @@ -613,7 +613,7 @@

private void removeClientInfo(Client client) {
int pid = client.getClientData().getPid();
        mClientInfo.remove(Integer.valueOf(pid));
}

private void clearClientInfo() {
//Synthetic comment -- @@ -625,12 +625,12 @@
pkgName = UNKNOWN_PACKAGE;
}

        mClientInfo.put(Integer.valueOf(pid), pkgName);
}

@Override
public String getClientName(int pid) {
        return mClientInfo.get(Integer.valueOf(pid));
}

@Override








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/DeviceMonitor.java b/ddmlib/src/main/java/com/android/ddmlib/DeviceMonitor.java
//Synthetic comment -- index b01de67..36ab15f 100644

//Synthetic comment -- @@ -165,7 +165,7 @@
mConnectionAttempt++;
Log.e("DeviceMonitor", "Connection attempts: " + mConnectionAttempt);
if (mConnectionAttempt > 10) {
                            if (mServer.startAdb() == false) {
mRestartAttemptCount++;
Log.e("DeviceMonitor",
"adb restart attempts: " + mRestartAttemptCount);
//Synthetic comment -- @@ -180,7 +180,7 @@
}
}

                if (mMainAdbConnection != null && mMonitoring == false) {
mMonitoring = sendDeviceListMonitoringRequest();
}

//Synthetic comment -- @@ -203,11 +203,11 @@
} catch (IOException ioe) {
handleExpectionInMonitorLoop(ioe);
}
        } while (mQuit == false);
}

private void handleExpectionInMonitorLoop(Exception e) {
        if (mQuit == false) {
if (e instanceof TimeoutException) {
Log.e("DeviceMonitor", "Adb connection Error: timeout");
} else {
//Synthetic comment -- @@ -280,7 +280,7 @@
AdbResponse resp = AdbHelper.readAdbResponse(mMainAdbConnection,
false /* readDiagString */);

            if (resp.okay == false) {
// request was refused by adb!
Log.e("DeviceMonitor", "adb refused request: " + resp.message);
}
//Synthetic comment -- @@ -366,8 +366,8 @@
// if the device just got ready/online, we need to start
// monitoring it.
if (device.isOnline()) {
                                    if (AndroidDebugBridge.getClientSupport() == true) {
                                        if (startMonitoringDevice(device) == false) {
Log.e("DeviceMonitor",
"Failed to start monitoring "
+ device.getSerialNumber());
//Synthetic comment -- @@ -386,7 +386,7 @@
}
}

                    if (foundMatch == false) {
// the device is gone, we need to remove it, and keep current index
// to process the next one.
removeDevice(device);
//Synthetic comment -- @@ -405,7 +405,7 @@
mServer.deviceConnected(newDevice);

// start monitoring them.
                    if (AndroidDebugBridge.getClientSupport() == true) {
if (newDevice.isOnline()) {
startMonitoringDevice(newDevice);
}
//Synthetic comment -- @@ -496,7 +496,7 @@
@Override
public void processNewLines(String[] lines) {
for (String line : lines) {
                    if (line.length() > 0) {
// this should be the only one.
device.setMountingPoint(name, line);
}
//Synthetic comment -- @@ -598,7 +598,7 @@
}

synchronized (mClientsToReopen) {
                    if (mClientsToReopen.size() > 0) {
Set<Client> clients = mClientsToReopen.keySet();
MonitorThread monitorThread = MonitorThread.getInstance();

//Synthetic comment -- @@ -669,12 +669,12 @@
}
}
} catch (IOException e) {
                if (mQuit == false) {

}
}

        } while (mQuit == false);
}

private boolean sendDeviceMonitoringRequest(SocketChannel socket, Device device)
//Synthetic comment -- @@ -689,7 +689,7 @@

AdbResponse resp = AdbHelper.readAdbResponse(socket, false /* readDiagString */);

            if (resp.okay == false) {
// request was refused by adb!
Log.e("DeviceMonitor", "adb refused request: " + resp.message);
}
//Synthetic comment -- @@ -743,7 +743,7 @@
synchronized (clients) {
for (Client c : clients) {
existingClients.put(
                            Integer.valueOf(c.getClientData().getPid()),
c);
}
}
//Synthetic comment -- @@ -765,7 +765,7 @@
openClient(device, newPid, getNextDebuggerPort(), monitorThread);
}

            if (pidsToAdd.size() > 0 || clientsToRemove.size() > 0) {
mServer.deviceChanged(device, Device.CHANGE_CLIENT_LIST);
}
}
//Synthetic comment -- @@ -856,14 +856,14 @@
private int getNextDebuggerPort() {
// get the first port and remove it
synchronized (mDebuggerPorts) {
            if (mDebuggerPorts.size() > 0) {
int port = mDebuggerPorts.get(0);

// remove it.
mDebuggerPorts.remove(0);

// if there's nothing left, add the next port to the list
                if (mDebuggerPorts.size() == 0) {
mDebuggerPorts.add(port+1);
}









//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/EmulatorConsole.java b/ddmlib/src/main/java/com/android/ddmlib/EmulatorConsole.java
//Synthetic comment -- index fe3aca1..4a87625 100644

//Synthetic comment -- @@ -212,7 +212,7 @@

if (console != null) {
// if the console exist, we ping the emulator to check the connection.
            if (console.ping() == false) {
RemoveConsole(console.mPort);
console = null;
}
//Synthetic comment -- @@ -564,7 +564,7 @@
} catch (Exception e) {
return false;
} finally {
            if (result == false) {
// FIXME connection failed somehow, we need to disconnect the console.
RemoveConsole(mPort);
}
//Synthetic comment -- @@ -610,7 +610,7 @@
int numWaits = 0;
boolean stop = false;

            while (buf.position() != buf.limit() && stop == false) {
int count;

count = mSocketChannel.read(buf);
//Synthetic comment -- @@ -652,14 +652,11 @@
* @param currentPosition The current position
*/
private boolean endsWithOK(int currentPosition) {
        if (mBuffer[currentPosition-1] == '\n' &&
                mBuffer[currentPosition-2] == '\r' &&
                mBuffer[currentPosition-3] == 'K' &&
                mBuffer[currentPosition-4] == 'O') {
            return true;
        }

        return false;
}

/**








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/FileListingService.java b/ddmlib/src/main/java/com/android/ddmlib/FileListingService.java
//Synthetic comment -- index 9b83787..5485a32 100644

//Synthetic comment -- @@ -337,11 +337,8 @@
return true;
}
long current = System.currentTimeMillis();
            if (current-fetchTime > REFRESH_TEST) {
                return true;
            }

            return false;
}

/**
//Synthetic comment -- @@ -445,13 +442,13 @@
public void processNewLines(String[] lines) {
for (String line : lines) {
// no need to handle empty lines.
                if (line.length() == 0) {
continue;
}

// run the line through the regexp
Matcher m = LS_L_PATTERN.matcher(line);
                if (m.matches() == false) {
continue;
}

//Synthetic comment -- @@ -693,7 +690,7 @@
final IListingReceiver receiver) {
// first thing we do is check the cache, and if we already have a recent
// enough children list, we just return that.
        if (useCache && entry.needFetch() == false) {
return entry.getCachedChildren();
}

//Synthetic comment -- @@ -730,7 +727,7 @@
@Override
public void processNewLines(String[] lines) {
for (String line : lines) {
                                    if (line.length() > 0) {
// get the filepath and package from the line
Matcher m = sPmPattern.matcher(line);
if (m.matches()) {
//Synthetic comment -- @@ -761,7 +758,7 @@
mThreadList.remove(this);

// then launch the next one if applicable.
                    if (mThreadList.size() > 0) {
Thread t = mThreadList.get(0);
t.start();
}








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/GetPropReceiver.java b/ddmlib/src/main/java/com/android/ddmlib/GetPropReceiver.java
//Synthetic comment -- index e7c8def..d7368c8 100644

//Synthetic comment -- @@ -47,7 +47,7 @@
// after all that.

for (String line : lines) {
            if (line.length() == 0 || line.startsWith("#")) {
continue;
}

//Synthetic comment -- @@ -56,7 +56,7 @@
String label = m.group(1);
String value = m.group(2);

                if (label.length() > 0) {
mDevice.addProperty(label, value);
}
}








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/HandleNativeHeap.java b/ddmlib/src/main/java/com/android/ddmlib/HandleNativeHeap.java
//Synthetic comment -- index e64bbc9..c3e6211 100644

//Synthetic comment -- @@ -267,7 +267,7 @@
String tmpLib = line.substring(index);

if (library == null ||
                            (library != null && tmpLib.equals(library) == false)) {

if (library != null) {
cd.addNativeLibraryMapInfo(startAddr, endAddr, library);








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java b/ddmlib/src/main/java/com/android/ddmlib/HandleViewDebug.java
//Synthetic comment -- index c80a4fe..1a279bd 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

final public class HandleViewDebug extends ChunkHandler {
/** Enable/Disable tracing of OpenGL calls. */
public static final int CHUNK_VUGL = type("VUGL");

//Synthetic comment -- @@ -85,7 +85,7 @@
@Override
public void clientDisconnected(Client client) {}

    public static abstract class ViewDumpHandler extends ChunkHandler {
private final CountDownLatch mLatch = new CountDownLatch(1);
private final int mChunkType;

//Synthetic comment -- @@ -267,28 +267,28 @@
Object arg = args[i];
if (arg instanceof Boolean) {
b.putChar('Z');
                    b.put((byte) (((Boolean) arg).booleanValue() ? 1 : 0));
} else if (arg instanceof Byte) {
b.putChar('B');
                    b.put(((Byte) arg).byteValue());
} else if (arg instanceof Character) {
b.putChar('C');
                    b.putChar(((Character) arg).charValue());
} else if (arg instanceof Short) {
b.putChar('S');
                    b.putShort(((Short) arg).shortValue());
} else if (arg instanceof Integer) {
b.putChar('I');
                    b.putInt(((Integer) arg).intValue());
} else if (arg instanceof Long) {
b.putChar('J');
                    b.putLong(((Long) arg).longValue());
} else if (arg instanceof Float) {
b.putChar('F');
                    b.putFloat(((Float) arg).floatValue());
} else if (arg instanceof Double) {
b.putChar('D');
                    b.putDouble(((Double) arg).doubleValue());
} else {
Log.e(TAG, "View method invocation only supports primitive arguments, supplied: " + arg);
return;








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/Log.java b/ddmlib/src/main/java/com/android/ddmlib/Log.java
//Synthetic comment -- index bab8a42..67ef50a 100644

//Synthetic comment -- @@ -84,7 +84,7 @@
* @return a <code>LogLevel</code> object or <code>null</code> if no match were found.
*/
public static LogLevel getByLetterString(String letter) {
            if (letter.length() > 0) {
return getByLetter(letter.charAt(0));
}









//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/MonitorThread.java b/ddmlib/src/main/java/com/android/ddmlib/MonitorThread.java
//Synthetic comment -- index 38cd7c9..a4ff115 100644

//Synthetic comment -- @@ -110,7 +110,7 @@
return;
}

        if (AndroidDebugBridge.getClientSupport() == false) {
return;
}

//Synthetic comment -- @@ -302,7 +302,7 @@
Client client = (Client)key.attachment();

try {
            if (key.isReadable() == false || key.isValid() == false) {
Log.d("ddms", "Invalid key from " + client + ". Dropping client.");
dropClient(client, true /* notify */);
return;
//Synthetic comment -- @@ -428,7 +428,7 @@
}

synchronized (mClientList) {
            if (mClientList.remove(client) == false) {
return;
}
}








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/MultiLineReceiver.java b/ddmlib/src/main/java/com/android/ddmlib/MultiLineReceiver.java
//Synthetic comment -- index 19a0177..52e0416 100644

//Synthetic comment -- @@ -49,7 +49,7 @@
*/
@Override
public final void addOutput(byte[] data, int offset, int length) {
        if (isCancelled() == false) {
String s = null;
try {
s = new String(data, offset, length, "UTF-8"); //$NON-NLS-1$
//Synthetic comment -- @@ -59,46 +59,44 @@
}

// ok we've got a string
            if (s != null) {
                // if we had an unfinished line we add it.
                if (mUnfinishedLine != null) {
                    s = mUnfinishedLine + s;
                    mUnfinishedLine = null;
}

                // now we split the lines
                mArray.clear();
                int start = 0;
                do {
                    int index = s.indexOf("\r\n", start); //$NON-NLS-1$

                    // if \r\n was not found, this is an unfinished line
                    // and we store it to be processed for the next packet
                    if (index == -1) {
                        mUnfinishedLine = s.substring(start);
                        break;
                    }

                    // so we found a \r\n;
                    // extract the line
                    String line = s.substring(start, index);
                    if (mTrimLines) {
                        line = line.trim();
                    }
                    mArray.add(line);

                    // move start to after the \r\n we found
                    start = index + 2;
                } while (true);

                if (mArray.size() > 0) {
                    // at this point we've split all the lines.
                    // make the array
                    String[] lines = mArray.toArray(new String[mArray.size()]);

                    // send it for final processing
                    processNewLines(lines);
}
}
}
}








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/NativeAllocationInfo.java b/ddmlib/src/main/java/com/android/ddmlib/NativeAllocationInfo.java
//Synthetic comment -- index 0e2a685..baefa81 100644

//Synthetic comment -- @@ -138,7 +138,7 @@
mResolvedStackCall.clear();
}
mResolvedStackCall.addAll(resolvedStackCall);
        mIsStackCallResolved = mResolvedStackCall.size() != 0;
}

/**
//Synthetic comment -- @@ -275,7 +275,7 @@
}

// couldn't find a relevant one, so we'll return the first one if it exists.
            if (mResolvedStackCall.size() > 0)
return mResolvedStackCall.get(0);
}









//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/SyncService.java b/ddmlib/src/main/java/com/android/ddmlib/SyncService.java
//Synthetic comment -- index cf91eb5..3884917 100644

//Synthetic comment -- @@ -172,7 +172,7 @@

AdbResponse resp = AdbHelper.readAdbResponse(mChannel, false /* readDiagString */);

            if (resp.okay == false) {
Log.w("ddms", "Got unhappy response from ADB sync req: " + resp.message);
mChannel.close();
mChannel = null;
//Synthetic comment -- @@ -246,10 +246,10 @@

// first we check the destination is a directory and exists
File f = new File(localPath);
        if (f.exists() == false) {
throw new SyncException(SyncError.NO_DIR_TARGET);
}
        if (f.isDirectory() == false) {
throw new SyncException(SyncError.TARGET_IS_FILE);
}

//Synthetic comment -- @@ -333,7 +333,7 @@
*/
public void push(String[] local, FileEntry remote, ISyncProgressMonitor monitor)
throws SyncException, IOException, TimeoutException {
        if (remote.isDirectory() == false) {
throw new SyncException(SyncError.REMOTE_IS_FILE);
}

//Synthetic comment -- @@ -367,7 +367,7 @@
public void pushFile(String local, String remote, ISyncProgressMonitor monitor)
throws SyncException, IOException, TimeoutException {
File f = new File(local);
        if (f.exists() == false) {
throw new SyncException(SyncError.NO_LOCAL_FILE);
}

//Synthetic comment -- @@ -445,7 +445,7 @@

for (FileEntry e : entries) {
// check if we're cancelled
            if (monitor.isCanceled() == true) {
throw new SyncException(SyncError.CANCELED);
}

//Synthetic comment -- @@ -506,8 +506,8 @@
AdbHelper.read(mChannel, pullResult, -1, timeOut);

// check we have the proper data back
            if (checkResult(pullResult, ID_DATA) == false &&
                    checkResult(pullResult, ID_DONE) == false) {
throw new SyncException(SyncError.TRANSFER_PROTOCOL_ERROR,
readErrorMessage(pullResult, timeOut));
}
//Synthetic comment -- @@ -530,7 +530,7 @@
// loop to get data until we're done.
while (true) {
// check if we're cancelled
                if (monitor.isCanceled() == true) {
throw new SyncException(SyncError.CANCELED);
}

//Synthetic comment -- @@ -538,7 +538,7 @@
if (checkResult(pullResult, ID_DONE)) {
break;
}
                if (checkResult(pullResult, ID_DATA) == false) {
// hmm there's an error
throw new SyncException(SyncError.TRANSFER_PROTOCOL_ERROR,
readErrorMessage(pullResult, timeOut));
//Synthetic comment -- @@ -589,7 +589,7 @@
throws SyncException, IOException, TimeoutException {
for (File f : fileArray) {
// check if we're canceled
            if (monitor.isCanceled() == true) {
throw new SyncException(SyncError.CANCELED);
}
if (f.exists()) {
//Synthetic comment -- @@ -651,7 +651,7 @@
// look while there is something to read
while (true) {
// check if we're canceled
                if (monitor.isCanceled() == true) {
throw new SyncException(SyncError.CANCELED);
}

//Synthetic comment -- @@ -694,7 +694,7 @@
byte[] result = new byte[8];
AdbHelper.read(mChannel, result, -1 /* full length */, timeOut);

        if (checkResult(result, ID_OKAY) == false) {
throw new SyncException(SyncError.TRANSFER_PROTOCOL_ERROR,
readErrorMessage(result, timeOut));
}
//Synthetic comment -- @@ -746,7 +746,7 @@
AdbHelper.read(mChannel, statResult, -1 /* full length */, DdmPreferences.getTimeOut());

// check we have the proper data back
        if (checkResult(statResult, ID_STAT) == false) {
return null;
}

//Synthetic comment -- @@ -833,14 +833,10 @@
* @return true if the code matches.
*/
private static boolean checkResult(byte[] result, byte[] code) {
        if (result[0] != code[0] ||
result[1] != code[1] ||
result[2] != code[2] ||
                result[3] != code[3]) {
            return false;
        }

        return true;

}









//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/log/EventContainer.java b/ddmlib/src/main/java/com/android/ddmlib/log/EventContainer.java
//Synthetic comment -- index 7157194..ce80005 100644

//Synthetic comment -- @@ -333,7 +333,7 @@
data = ((Object[])mData)[index];
}

        if (data.getClass().equals(data.getClass()) == false) {
throw new InvalidTypeException();
}

//Synthetic comment -- @@ -378,9 +378,9 @@
throw new InvalidTypeException();
case BIT_CHECK:
if (data instanceof Integer) {
                    return (((Integer)data).intValue() & ((Integer)value).intValue()) != 0;
} else if (data instanceof Long) {
                    return (((Long)data).longValue() & ((Long)value).longValue()) != 0;
}

// other types can't use this compare method.








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/log/EventLogParser.java b/ddmlib/src/main/java/com/android/ddmlib/log/EventLogParser.java
//Synthetic comment -- index 7df6429..568c1be 100644

//Synthetic comment -- @@ -156,7 +156,7 @@
*/
private void processTagLine(String line) {
// ignore empty lines and comment lines
        if (line.length() > 0 && line.charAt(0) != '#') {
Matcher m = PATTERN_TAG_WITH_DESC.matcher(line);
if (m.matches()) {
try {
//Synthetic comment -- @@ -176,7 +176,7 @@
} else {

String description = m.group(3);
                        if (description != null && description.length() > 0) {
EventValueDescription[] desc =
processDescription(description);

//Synthetic comment -- @@ -221,7 +221,7 @@
}

typeString = m.group(3);
                    if (typeString != null && typeString.length() > 0) {
//skip the |
typeString = typeString.substring(1);

//Synthetic comment -- @@ -245,7 +245,7 @@
}
}

        if (list.size() == 0) {
return null;
}

//Synthetic comment -- @@ -297,7 +297,7 @@
// [value1,value2...]
// or
// value
        if (textLogLine.length() == 0) {
return null;
}

//Synthetic comment -- @@ -397,7 +397,7 @@
ival = ArrayHelper.swap32bitFromArray(eventData, offset);
offset += 4;

                list.add(new Integer(ival));
}
break;
case EVENT_TYPE_LONG: { /* 64-bit signed long */
//Synthetic comment -- @@ -408,7 +408,7 @@
lval = ArrayHelper.swap64bitFromArray(eventData, offset);
offset += 8;

                list.add(new Long(lval));
}
break;
case EVENT_TYPE_STRING: { /* UTF-8 chars, not NULL-terminated */








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/log/GcEventContainer.java b/ddmlib/src/main/java/com/android/ddmlib/log/GcEventContainer.java
//Synthetic comment -- index deb2b8b..859e080f 100644

//Synthetic comment -- @@ -82,10 +82,10 @@
throws InvalidTypeException {
// do a quick easy check on the type.
if (index == 0) {
            if ((value instanceof String) == false) {
throw new InvalidTypeException();
}
        } else if ((value instanceof Long) == false) {
throw new InvalidTypeException();
}

//Synthetic comment -- @@ -94,18 +94,18 @@
if (index == 0) {
return processId.equals(value);
} else {
                    return getValueAsLong(index) == ((Long)value).longValue();
}
case LESSER_THAN:
                return getValueAsLong(index) <= ((Long)value).longValue();
case LESSER_THAN_STRICT:
                return getValueAsLong(index) < ((Long)value).longValue();
case GREATER_THAN:
                return getValueAsLong(index) >= ((Long)value).longValue();
case GREATER_THAN_STRICT:
                return getValueAsLong(index) > ((Long)value).longValue();
case BIT_CHECK:
                return (getValueAsLong(index) & ((Long)value).longValue()) != 0;
}

throw new ArrayIndexOutOfBoundsException();
//Synthetic comment -- @@ -118,7 +118,7 @@
}

try {
            return new Long(getValueAsLong(valueIndex));
} catch (InvalidTypeException e) {
// this would only happened if valueIndex was 0, which we test above.
}








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/log/LogReceiver.java b/ddmlib/src/main/java/com/android/ddmlib/log/LogReceiver.java
//Synthetic comment -- index f6eefe5..195eec6 100644

//Synthetic comment -- @@ -116,7 +116,7 @@
}

// loop while there is still data to be read and the receiver has not be cancelled.
        while (length > 0 && mIsCancelled == false) {
// first check if we have no current entry.
if (mCurrentEntry == null) {
if (mEntryHeaderOffset + length < ENTRY_HEADER_SIZE) {







