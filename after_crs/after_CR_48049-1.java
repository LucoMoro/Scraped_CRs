/*Sort modifier in the canonical order.

Automated IDE refactoring; no other changes.

Change-Id:Ifc8f22c670c65a79bc4eb13805bdfff702ae20f2*/




//Synthetic comment -- diff --git a/assetstudio/src/com/android/assetstudiolib/Util.java b/assetstudio/src/com/android/assetstudiolib/Util.java
//Synthetic comment -- index ee2a5f7..2c76438 100644

//Synthetic comment -- @@ -406,7 +406,7 @@
* An effect to apply in
* {@link Util#drawEffects(java.awt.Graphics2D, java.awt.image.BufferedImage, int, int, Util.Effect[])}
*/
    public abstract static class Effect {
}

/**








//Synthetic comment -- diff --git a/common/src/main/java/com/android/SdkConstants.java b/common/src/main/java/com/android/SdkConstants.java
//Synthetic comment -- index 48cecc1..d12e36d 100644

//Synthetic comment -- @@ -881,7 +881,7 @@
/** Extension for pre-processable images. Right now pngs */
public static final String EXT_PNG = "png"; //$NON-NLS-1$

    private static final String DOT = "."; //$NON-NLS-1$

/** Dot-Extension of the Application package Files, i.e. ".apk". */
public static final String DOT_ANDROID_PACKAGE = DOT + EXT_ANDROID_PACKAGE;








//Synthetic comment -- diff --git a/common/src/main/java/com/android/prefs/AndroidLocation.java b/common/src/main/java/com/android/prefs/AndroidLocation.java
//Synthetic comment -- index 6af8e9b..11c1540 100644

//Synthetic comment -- @@ -53,7 +53,8 @@
* @return an OS specific path, terminated by a separator.
* @throws AndroidLocationException
*/
    @NonNull
    public static final String getFolder() throws AndroidLocationException {
if (sPrefsLocation == null) {
String home = findValidPath("ANDROID_SDK_HOME", "user.home", "HOME");

//Synthetic comment -- @@ -96,7 +97,7 @@
/**
* Resets the folder used to store android related files. For testing.
*/
    public static final void resetFolder() {
sPrefsLocation = null;
}









//Synthetic comment -- diff --git a/common/src/main/java/com/android/utils/PositionXmlParser.java b/common/src/main/java/com/android/utils/PositionXmlParser.java
//Synthetic comment -- index 73574d5..acbd86c 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final String UTF_16 = "UTF_16";               //$NON-NLS-1$
private static final String UTF_16LE = "UTF_16LE";           //$NON-NLS-1$
private static final String CONTENT_KEY = "contents";        //$NON-NLS-1$
    private static final String POS_KEY = "offsets";             //$NON-NLS-1$
private static final String NAMESPACE_PREFIX_FEATURE =
"http://xml.org/sax/features/namespace-prefixes";    //$NON-NLS-1$
private static final String NAMESPACE_FEATURE =








//Synthetic comment -- diff --git a/common/src/main/java/com/android/xml/AndroidXPathFactory.java b/common/src/main/java/com/android/xml/AndroidXPathFactory.java
//Synthetic comment -- index ee5b87b..87788be 100644

//Synthetic comment -- @@ -32,13 +32,13 @@
*/
public class AndroidXPathFactory {
/** Default prefix for android name space: 'android' */
    public static final String DEFAULT_NS_PREFIX = "android"; //$NON-NLS-1$

    private static final XPathFactory sFactory = XPathFactory.newInstance();

/** Name space context for Android resource XML files. */
private static class AndroidNamespaceContext implements NamespaceContext {
        private static final AndroidNamespaceContext sThis = new AndroidNamespaceContext(
DEFAULT_NS_PREFIX);

private final String mAndroidPrefix;








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/AllocationInfo.java b/ddmlib/src/main/java/com/android/ddmlib/AllocationInfo.java
//Synthetic comment -- index 157b044..021edef 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
NUMBER, SIZE, CLASS, THREAD, IN_CLASS, IN_METHOD;
}

    public static final class AllocationSorter implements Comparator<AllocationInfo> {

private SortMode mSortMode = SortMode.SIZE;
private boolean mDescending = true;








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java b/ddmlib/src/main/java/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index 5407d7f..67f05d4 100644

//Synthetic comment -- @@ -45,19 +45,19 @@
* ADB_SERVER_VERSION found in //device/tools/adb/adb.h
*/

    private static final int ADB_VERSION_MICRO_MIN = 20;
    private static final int ADB_VERSION_MICRO_MAX = -1;

    private static final Pattern sAdbVersion = Pattern.compile(
"^.*(\\d+)\\.(\\d+)\\.(\\d+)$"); //$NON-NLS-1$

    private static final String ADB = "adb"; //$NON-NLS-1$
    private static final String DDMS = "ddms"; //$NON-NLS-1$
    private static final String SERVER_PORT_ENV_VAR = "ANDROID_ADB_SERVER_PORT"; //$NON-NLS-1$

// Where to find the ADB bridge.
    static final String ADB_HOST = "127.0.0.1"; //$NON-NLS-1$
    static final int ADB_PORT = 5037;

private static InetAddress sHostAddr;
private static InetSocketAddress sSocketAddr;
//Synthetic comment -- @@ -75,11 +75,11 @@

private DeviceMonitor mDeviceMonitor;

    private static final ArrayList<IDebugBridgeChangeListener> sBridgeListeners =
new ArrayList<IDebugBridgeChangeListener>();
    private static final ArrayList<IDeviceChangeListener> sDeviceListeners =
new ArrayList<IDeviceChangeListener>();
    private static final ArrayList<IClientChangeListener> sClientListeners =
new ArrayList<IClientChangeListener>();

// lock object for synchronization








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/ClientData.java b/ddmlib/src/main/java/com/android/ddmlib/ClientData.java
//Synthetic comment -- index ff83c37..1612435 100644

//Synthetic comment -- @@ -49,7 +49,7 @@


/** Temporary name of VM to be ignored. */
    private static final String PRE_INITIALIZED = "<pre-initialized>"; //$NON-NLS-1$

public static enum DebuggerStatus {
/** Debugger connection status: not waiting on one, not connected to one, but accepting
//Synthetic comment -- @@ -100,46 +100,46 @@
* Name of the value representing the max size of the heap, in the {@link Map} returned by
* {@link #getVmHeapInfo(int)}
*/
    public static final String HEAP_MAX_SIZE_BYTES = "maxSizeInBytes"; //$NON-NLS-1$
/**
* Name of the value representing the size of the heap, in the {@link Map} returned by
* {@link #getVmHeapInfo(int)}
*/
    public static final String HEAP_SIZE_BYTES = "sizeInBytes"; //$NON-NLS-1$
/**
* Name of the value representing the number of allocated bytes of the heap, in the
* {@link Map} returned by {@link #getVmHeapInfo(int)}
*/
    public static final String HEAP_BYTES_ALLOCATED = "bytesAllocated"; //$NON-NLS-1$
/**
* Name of the value representing the number of objects in the heap, in the {@link Map}
* returned by {@link #getVmHeapInfo(int)}
*/
    public static final String HEAP_OBJECTS_ALLOCATED = "objectsAllocated"; //$NON-NLS-1$

/**
* String for feature enabling starting/stopping method profiling
* @see #hasFeature(String)
*/
    public static final String FEATURE_PROFILING = "method-trace-profiling"; //$NON-NLS-1$

/**
* String for feature enabling direct streaming of method profiling data
* @see #hasFeature(String)
*/
    public static final String FEATURE_PROFILING_STREAMING = "method-trace-profiling-streaming"; //$NON-NLS-1$

/**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/
    public static final String FEATURE_HPROF = "hprof-heap-dump"; //$NON-NLS-1$

/**
* String for feature allowing direct streaming of hprof dumps
* @see #hasFeature(String)
*/
    public static final String FEATURE_HPROF_STREAMING = "hprof-heap-dump-streaming"; //$NON-NLS-1$

private static IHprofDumpHandler sHprofDumpHandler;
private static IMethodProfilingHandler sMethodProfilingHandler;








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/DdmConstants.java b/ddmlib/src/main/java/com/android/ddmlib/DdmConstants.java
//Synthetic comment -- index 0b107e4..6aec91e 100644

//Synthetic comment -- @@ -18,28 +18,28 @@

public final class DdmConstants {

    public static final int PLATFORM_UNKNOWN = 0;
    public static final int PLATFORM_LINUX = 1;
    public static final int PLATFORM_WINDOWS = 2;
    public static final int PLATFORM_DARWIN = 3;

/**
* Returns current platform, one of {@link #PLATFORM_WINDOWS}, {@link #PLATFORM_DARWIN},
* {@link #PLATFORM_LINUX} or {@link #PLATFORM_UNKNOWN}.
*/
    public static final int CURRENT_PLATFORM = currentPlatform();

/**
* Extension for Traceview files.
*/
    public static final String DOT_TRACE = ".trace";

/** hprof-conv executable (with extension for the current OS)  */
    public static final String FN_HPROF_CONVERTER = (CURRENT_PLATFORM == PLATFORM_WINDOWS) ?
"hprof-conv.exe" : "hprof-conv"; //$NON-NLS-1$ //$NON-NLS-2$

/** traceview executable (with extension for the current OS)  */
    public static final String FN_TRACEVIEW = (CURRENT_PLATFORM == PLATFORM_WINDOWS) ?
"traceview.bat" : "traceview"; //$NON-NLS-1$ //$NON-NLS-2$

/**








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/DdmPreferences.java b/ddmlib/src/main/java/com/android/ddmlib/DdmPreferences.java
//Synthetic comment -- index d286917..b0072ec 100644

//Synthetic comment -- @@ -31,22 +31,22 @@
public final class DdmPreferences {

/** Default value for thread update flag upon client connection. */
    public static final boolean DEFAULT_INITIAL_THREAD_UPDATE = false;
/** Default value for heap update flag upon client connection. */
    public static final boolean DEFAULT_INITIAL_HEAP_UPDATE = false;
/** Default value for the selected client debug port */
    public static final int DEFAULT_SELECTED_DEBUG_PORT = 8700;
/** Default value for the debug port base */
    public static final int DEFAULT_DEBUG_PORT_BASE = 8600;
/** Default value for the logcat {@link LogLevel} */
    public static final LogLevel DEFAULT_LOG_LEVEL = LogLevel.ERROR;
/** Default timeout values for adb connection (milliseconds) */
public static final int DEFAULT_TIMEOUT = 5000; // standard delay, in ms
/** Default profiler buffer size (megabytes) */
public static final int DEFAULT_PROFILER_BUFFER_SIZE_MB = 8;
/** Default values for the use of the ADBHOST environment variable. */
    public static final boolean DEFAULT_USE_ADBHOST = false;
    public static final String DEFAULT_ADBHOST_VALUE = "127.0.0.1";

private static boolean sThreadUpdate = DEFAULT_INITIAL_THREAD_UPDATE;
private static boolean sInitialHeapUpdate = DEFAULT_INITIAL_HEAP_UPDATE;








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/Device.java b/ddmlib/src/main/java/com/android/ddmlib/Device.java
//Synthetic comment -- index 0566275..9886066 100644

//Synthetic comment -- @@ -37,12 +37,12 @@
private static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
private static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$

    private static final int INSTALL_TIMEOUT = 2*60*1000; //2min
private static final int BATTERY_TIMEOUT = 2*1000; //2 seconds
private static final int GETPROP_TIMEOUT = 2*1000; //2 seconds

/** Emulator Serial Number regexp. */
    static final String RE_EMULATOR_SN = "emulator-(\\d+)"; //$NON-NLS-1$

/** Serial number of the device */
private String mSerialNumber = null;








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/EmulatorConsole.java b/ddmlib/src/main/java/com/android/ddmlib/EmulatorConsole.java
//Synthetic comment -- index 2f4175f..f40c1bd 100644

//Synthetic comment -- @@ -46,34 +46,34 @@
*/
public final class EmulatorConsole {

    private static final String DEFAULT_ENCODING = "ISO-8859-1"; //$NON-NLS-1$

    private static final int WAIT_TIME = 5; // spin-wait sleep, in ms

    private static final int STD_TIMEOUT = 5000; // standard delay, in ms

    private static final String HOST = "127.0.0.1";  //$NON-NLS-1$

    private static final String COMMAND_PING = "help\r\n"; //$NON-NLS-1$
    private static final String COMMAND_AVD_NAME = "avd name\r\n"; //$NON-NLS-1$
    private static final String COMMAND_KILL = "kill\r\n"; //$NON-NLS-1$
    private static final String COMMAND_GSM_STATUS = "gsm status\r\n"; //$NON-NLS-1$
    private static final String COMMAND_GSM_CALL = "gsm call %1$s\r\n"; //$NON-NLS-1$
    private static final String COMMAND_GSM_CANCEL_CALL = "gsm cancel %1$s\r\n"; //$NON-NLS-1$
    private static final String COMMAND_GSM_DATA = "gsm data %1$s\r\n"; //$NON-NLS-1$
    private static final String COMMAND_GSM_VOICE = "gsm voice %1$s\r\n"; //$NON-NLS-1$
    private static final String COMMAND_SMS_SEND = "sms send %1$s %2$s\r\n"; //$NON-NLS-1$
    private static final String COMMAND_NETWORK_STATUS = "network status\r\n"; //$NON-NLS-1$
    private static final String COMMAND_NETWORK_SPEED = "network speed %1$s\r\n"; //$NON-NLS-1$
    private static final String COMMAND_NETWORK_LATENCY = "network delay %1$s\r\n"; //$NON-NLS-1$
    private static final String COMMAND_GPS = "geo fix %1$f %2$f %3$f\r\n"; //$NON-NLS-1$

    private static final Pattern RE_KO = Pattern.compile("KO:\\s+(.*)"); //$NON-NLS-1$

/**
* Array of delay values: no delay, gprs, edge/egprs, umts/3d
*/
    public static final int[] MIN_LATENCIES = new int[] {
0,      // No delay
150,    // gprs
80,     // edge/egprs
//Synthetic comment -- @@ -94,7 +94,7 @@
};

/** Arrays of valid network speeds */
    public static final String[] NETWORK_SPEEDS = new String[] {
"full", //$NON-NLS-1$
"gsm", //$NON-NLS-1$
"hscsd", //$NON-NLS-1$
//Synthetic comment -- @@ -105,7 +105,7 @@
};

/** Arrays of valid network latencies */
    public static final String[] NETWORK_LATENCIES = new String[] {
"none", //$NON-NLS-1$
"gprs", //$NON-NLS-1$
"edge", //$NON-NLS-1$
//Synthetic comment -- @@ -157,19 +157,19 @@
}
}

    public static final String RESULT_OK = null;

    private static final Pattern sEmulatorRegexp = Pattern.compile(Device.RE_EMULATOR_SN);
    private static final Pattern sVoiceStatusRegexp = Pattern.compile(
"gsm\\s+voice\\s+state:\\s*([a-z]+)", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$
    private static final Pattern sDataStatusRegexp = Pattern.compile(
"gsm\\s+data\\s+state:\\s*([a-z]+)", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$
    private static final Pattern sDownloadSpeedRegexp = Pattern.compile(
"\\s+download\\s+speed:\\s+(\\d+)\\s+bits.*", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$
    private static final Pattern sMinLatencyRegexp = Pattern.compile(
"\\s+minimum\\s+latency:\\s+(\\d+)\\s+ms", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

    private static final HashMap<Integer, EmulatorConsole> sEmulators =
new HashMap<Integer, EmulatorConsole>();

/** Gsm Status class */








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/FileListingService.java b/ddmlib/src/main/java/com/android/ddmlib/FileListingService.java
//Synthetic comment -- index 97c57b9..9b83787 100644

//Synthetic comment -- @@ -31,28 +31,28 @@
public final class FileListingService {

/** Pattern to find filenames that match "*.apk" */
    private static final Pattern sApkPattern =
Pattern.compile(".*\\.apk", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

    private static final String PM_FULL_LISTING = "pm list packages -f"; //$NON-NLS-1$

/** Pattern to parse the output of the 'pm -lf' command.<br>
* The output format looks like:<br>
* /data/app/myapp.apk=com.mypackage.myapp */
    private static final Pattern sPmPattern = Pattern.compile("^package:(.+?)=(.+)$"); //$NON-NLS-1$

/** Top level data folder. */
    public static final String DIRECTORY_DATA = "data"; //$NON-NLS-1$
/** Top level sdcard folder. */
    public static final String DIRECTORY_SDCARD = "sdcard"; //$NON-NLS-1$
/** Top level mount folder. */
    public static final String DIRECTORY_MNT = "mnt"; //$NON-NLS-1$
/** Top level system folder. */
    public static final String DIRECTORY_SYSTEM = "system"; //$NON-NLS-1$
/** Top level temp folder. */
    public static final String DIRECTORY_TEMP = "tmp"; //$NON-NLS-1$
/** Application folder. */
    public static final String DIRECTORY_APP = "app"; //$NON-NLS-1$

public static final long REFRESH_RATE = 5000L;
/**
//Synthetic comment -- @@ -104,11 +104,11 @@
/**
* Represents an entry in a directory. This can be a file or a directory.
*/
    public static final class FileEntry {
/** Pattern to escape filenames for shell command consumption.
*  This pattern identifies any special characters that need to be escaped with a
*  backslash. */
        private static final Pattern sEscapePattern = Pattern.compile(
"([\\\\()*+?\"'&#/\\s])"); //$NON-NLS-1$

/**








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/GetPropReceiver.java b/ddmlib/src/main/java/com/android/ddmlib/GetPropReceiver.java
//Synthetic comment -- index 2033f04..e7c8def 100644

//Synthetic comment -- @@ -24,9 +24,9 @@
* {@link #GETPROP_COMMAND} on a device.
*/
final class GetPropReceiver extends MultiLineReceiver {
    static final String GETPROP_COMMAND = "getprop"; //$NON-NLS-1$

    private static final Pattern GETPROP_PATTERN = Pattern.compile("^\\[([^]]+)\\]\\:\\s*\\[(.*)\\]$"); //$NON-NLS-1$

/** indicates if we need to read the first */
private Device mDevice = null;








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/HeapSegment.java b/ddmlib/src/main/java/com/android/ddmlib/HeapSegment.java
//Synthetic comment -- index 42f740c..1469aec 100644

//Synthetic comment -- @@ -237,7 +237,7 @@
protected ByteBuffer mUsageData;

//* mStartAddress is set to this value when the segment becomes invalid.
    private static final long INVALID_START_ADDRESS = -1;

/**
* Create a new HeapSegment based on the raw contents








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/IDevice.java b/ddmlib/src/main/java/com/android/ddmlib/IDevice.java
//Synthetic comment -- index 452d032..a69528d 100644

//Synthetic comment -- @@ -26,14 +26,14 @@
*/
public interface IDevice {

    public static final String PROP_BUILD_VERSION = "ro.build.version.release";
    public static final String PROP_BUILD_API_LEVEL = "ro.build.version.sdk";
    public static final String PROP_BUILD_CODENAME = "ro.build.version.codename";

    public static final String PROP_DEBUGGABLE = "ro.debuggable";

/** Serial number of the first connected emulator. */
    public static final String FIRST_EMULATOR_SN = "emulator-5554"; //$NON-NLS-1$
/** Device change bit mask: {@link DeviceState} change. */
public static final int CHANGE_STATE = 0x0001;
/** Device change bit mask: {@link Client} list change. */
//Synthetic comment -- @@ -43,11 +43,11 @@

/** @deprecated Use {@link #PROP_BUILD_API_LEVEL}. */
@Deprecated
    public static final String PROP_BUILD_VERSION_NUMBER = PROP_BUILD_API_LEVEL;

    public static final String MNT_EXTERNAL_STORAGE = "EXTERNAL_STORAGE"; //$NON-NLS-1$
    public static final String MNT_ROOT = "ANDROID_ROOT"; //$NON-NLS-1$
    public static final String MNT_DATA = "ANDROID_DATA"; //$NON-NLS-1$

/**
* The state of a device.








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/NativeStackCallInfo.java b/ddmlib/src/main/java/com/android/ddmlib/NativeStackCallInfo.java
//Synthetic comment -- index 1d4af86..be365bf 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
* information as one object.
*/
public final class NativeStackCallInfo {
    private static final Pattern SOURCE_NAME_PATTERN = Pattern.compile("^(.+):(\\d+)$");

/** address of this stack frame */
private long mAddress;








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/SyncService.java b/ddmlib/src/main/java/com/android/ddmlib/SyncService.java
//Synthetic comment -- index f207567..73d7a38 100644

//Synthetic comment -- @@ -37,26 +37,26 @@
*/
public final class SyncService {

    private static final byte[] ID_OKAY = { 'O', 'K', 'A', 'Y' };
    private static final byte[] ID_FAIL = { 'F', 'A', 'I', 'L' };
    private static final byte[] ID_STAT = { 'S', 'T', 'A', 'T' };
    private static final byte[] ID_RECV = { 'R', 'E', 'C', 'V' };
    private static final byte[] ID_DATA = { 'D', 'A', 'T', 'A' };
    private static final byte[] ID_DONE = { 'D', 'O', 'N', 'E' };
    private static final byte[] ID_SEND = { 'S', 'E', 'N', 'D' };
//    private final static byte[] ID_LIST = { 'L', 'I', 'S', 'T' };
//    private final static byte[] ID_DENT = { 'D', 'E', 'N', 'T' };

    private static final NullSyncProgresMonitor sNullSyncProgressMonitor =
new NullSyncProgresMonitor();

    private static final int S_ISOCK = 0xC000; // type: symbolic link
    private static final int S_IFLNK = 0xA000; // type: symbolic link
    private static final int S_IFREG = 0x8000; // type: regular file
    private static final int S_IFBLK = 0x6000; // type: block device
    private static final int S_IFDIR = 0x4000; // type: directory
    private static final int S_IFCHR = 0x2000; // type: character device
    private static final int S_IFIFO = 0x1000; // type: fifo
/*
private final static int S_ISUID = 0x0800; // set-uid bit
private final static int S_ISGID = 0x0400; // set-gid bit
//Synthetic comment -- @@ -75,8 +75,8 @@
private final static int S_IXOTH = 0x0001; // other: execute
*/

    private static final int SYNC_DATA_MAX = 64*1024;
    private static final int REMOTE_PATH_MAX_LENGTH = 1024;

/**
* Classes which implement this interface provide methods that deal








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/log/EventContainer.java b/ddmlib/src/main/java/com/android/ddmlib/log/EventContainer.java
//Synthetic comment -- index 0afdf5d..0465fcf 100644

//Synthetic comment -- @@ -75,7 +75,7 @@
LIST(4),
TREE(5);

        private static final Pattern STORAGE_PATTERN = Pattern.compile("^(\\d+)@(.*)$"); //$NON-NLS-1$

private int mValue;









//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/log/EventLogParser.java b/ddmlib/src/main/java/com/android/ddmlib/log/EventLogParser.java
//Synthetic comment -- index b2d8256..7df6429 100644

//Synthetic comment -- @@ -45,25 +45,25 @@
public final class EventLogParser {

/** Location of the tag map file on the device */
    private static final String EVENT_TAG_MAP_FILE = "/system/etc/event-log-tags"; //$NON-NLS-1$

/**
* Event log entry types.  These must match up with the declarations in
* java/android/android/util/EventLog.java.
*/
    private static final int EVENT_TYPE_INT      = 0;
    private static final int EVENT_TYPE_LONG     = 1;
    private static final int EVENT_TYPE_STRING   = 2;
    private static final int EVENT_TYPE_LIST     = 3;

    private static final Pattern PATTERN_SIMPLE_TAG = Pattern.compile(
"^(\\d+)\\s+([A-Za-z0-9_]+)\\s*$"); //$NON-NLS-1$
    private static final Pattern PATTERN_TAG_WITH_DESC = Pattern.compile(
"^(\\d+)\\s+([A-Za-z0-9_]+)\\s*(.*)\\s*$"); //$NON-NLS-1$
    private static final Pattern PATTERN_DESCRIPTION = Pattern.compile(
"\\(([A-Za-z0-9_\\s]+)\\|(\\d+)(\\|\\d+){0,1}\\)"); //$NON-NLS-1$

    private static final Pattern TEXT_LOG_LINE = Pattern.compile(
"(\\d\\d)-(\\d\\d)\\s(\\d\\d):(\\d\\d):(\\d\\d).(\\d{3})\\s+I/([a-zA-Z0-9_]+)\\s*\\(\\s*(\\d+)\\):\\s+(.*)"); //$NON-NLS-1$

private final TreeMap<Integer, String> mTagMap = new TreeMap<Integer, String>();








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/log/GcEventContainer.java b/ddmlib/src/main/java/com/android/ddmlib/log/GcEventContainer.java
//Synthetic comment -- index 7bae202..deb2b8b 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
*/
final class GcEventContainer extends EventContainer {

    public static final int GC_EVENT_TAG = 20001;

private String processId;
private long gcTime;








//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/log/LogReceiver.java b/ddmlib/src/main/java/com/android/ddmlib/log/LogReceiver.java
//Synthetic comment -- index b49f025..7a68226 100644

//Synthetic comment -- @@ -26,12 +26,12 @@
*/
public final class LogReceiver {

    private static final int ENTRY_HEADER_SIZE = 20; // 2*2 + 4*4; see LogEntry.

/**
* Represents a log entry and its raw data.
*/
    public static final class LogEntry {
/*
* See //device/include/utils/logger.h
*/








//Synthetic comment -- diff --git a/layoutlib_api/src/main/java/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/main/java/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index a19b8d5..2b77112 100644

//Synthetic comment -- @@ -32,7 +32,7 @@
*/
public abstract class Bridge {

    public static final int API_CURRENT = 9;

/**
* Returns the API level of the layout library.








//Synthetic comment -- diff --git a/layoutlib_api/src/main/java/com/android/ide/common/rendering/api/LayoutLog.java b/layoutlib_api/src/main/java/com/android/ide/common/rendering/api/LayoutLog.java
//Synthetic comment -- index df29537..17244e9 100644

//Synthetic comment -- @@ -27,7 +27,7 @@
* {@code tag.startsWith(LayoutLog.TAG_RESOURCE_PREFIX} will test if the tag is any type
* of resource warning/error
*/
    public static final String TAG_RESOURCES_PREFIX = "resources.";

/**
* Prefix for matrix warnings/errors. This is not meant to be used as-is by the Layout
//Synthetic comment -- @@ -36,87 +36,87 @@
* {@code tag.startsWith(LayoutLog.TAG_MATRIX_PREFIX} will test if the tag is any type
* of matrix warning/error
*/
    public static final String TAG_MATRIX_PREFIX = "matrix.";

/**
* Tag for unsupported feature that can have a big impact on the rendering. For instance, aild
* access.
*/
    public static final String TAG_UNSUPPORTED = "unsupported";

/**
* Tag for error when something really unexpected happens.
*/
    public static final String TAG_BROKEN = "broken";

/**
* Tag for resource resolution failure.
* In this case the warning/error data object will be a ResourceValue containing the type
* and name of the resource that failed to resolve
*/
    public static final String TAG_RESOURCES_RESOLVE = TAG_RESOURCES_PREFIX + "resolve";

/**
* Tag for resource resolution failure, specifically for theme attributes.
* In this case the warning/error data object will be a ResourceValue containing the type
* and name of the resource that failed to resolve
*/
    public static final String TAG_RESOURCES_RESOLVE_THEME_ATTR = TAG_RESOURCES_RESOLVE + ".theme";

/**
* Tag for failure when reading the content of a resource file.
*/
    public static final String TAG_RESOURCES_READ = TAG_RESOURCES_PREFIX + "read";

/**
* Tag for wrong format in a resource value.
*/
    public static final String TAG_RESOURCES_FORMAT = TAG_RESOURCES_PREFIX + "format";

/**
* Fidelity Tag used when a non affine transformation matrix is used in a Java API.
*/
    public static final String TAG_MATRIX_AFFINE = TAG_MATRIX_PREFIX + "affine";

/**
* Tag used when a matrix cannot be inverted.
*/
    public static final String TAG_MATRIX_INVERSE = TAG_MATRIX_PREFIX + "inverse";

/**
* Fidelity Tag used when a mask filter type is used but is not supported.
*/
    public static final String TAG_MASKFILTER = "maskfilter";

/**
* Fidelity Tag used when a draw filter type is used but is not supported.
*/
    public static final String TAG_DRAWFILTER = "drawfilter";

/**
* Fidelity Tag used when a path effect type is used but is not supported.
*/
    public static final String TAG_PATHEFFECT = "patheffect";

/**
* Fidelity Tag used when a color filter type is used but is not supported.
*/
    public static final String TAG_COLORFILTER = "colorfilter";

/**
* Fidelity Tag used when a rasterize type is used but is not supported.
*/
    public static final String TAG_RASTERIZER = "rasterizer";

/**
* Fidelity Tag used when a shader type is used but is not supported.
*/
    public static final String TAG_SHADER = "shader";

/**
* Fidelity Tag used when a xfermode type is used but is not supported.
*/
    public static final String TAG_XFERMODE = "xfermode";

/**
* Logs a warning.








//Synthetic comment -- diff --git a/layoutlib_api/src/main/java/com/android/ide/common/rendering/api/RenderParams.java b/layoutlib_api/src/main/java/com/android/ide/common/rendering/api/RenderParams.java
//Synthetic comment -- index 2e53f14..50ed766 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
*/
public abstract class RenderParams {

    public static final long DEFAULT_TIMEOUT = 250; //ms

private final Object mProjectKey;
private final HardwareConfig mHardwareConfig;








//Synthetic comment -- diff --git a/layoutlib_api/src/main/java/com/android/ide/common/rendering/api/RenderResources.java b/layoutlib_api/src/main/java/com/android/ide/common/rendering/api/RenderResources.java
//Synthetic comment -- index c362224..d4609f4 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
*/
public class RenderResources {

    public static final String REFERENCE_NULL = "@null";

public static class FrameworkResourceIdProvider {
public Integer getId(ResourceType resType, String resName) {








//Synthetic comment -- diff --git a/layoutlib_api/src/main/java/com/android/layoutlib/api/ILayoutResult.java b/layoutlib_api/src/main/java/com/android/layoutlib/api/ILayoutResult.java
//Synthetic comment -- index 6aeaf9f..143f6c5 100644

//Synthetic comment -- @@ -32,13 +32,13 @@
/**
* Success return code
*/
    static final int SUCCESS = 0;

/**
* Error return code, in which case an error message is guaranteed to be defined.
* @see #getErrorMessage()
*/
    static final int ERROR = 1;

/**
* Returns the result code.








//Synthetic comment -- diff --git a/layoutlib_api/src/main/java/com/android/resources/Density.java b/layoutlib_api/src/main/java/com/android/resources/Density.java
//Synthetic comment -- index 1f3fb52..1a49c25 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
LOW("ldpi", "Low Density", 120, 4), //$NON-NLS-1$
NODPI("nodpi", "No Density", 0, 4); //$NON-NLS-1$

    public static final int DEFAULT_DENSITY = 160;

private final String mValue;
private final String mDisplayValue;








//Synthetic comment -- diff --git a/layoutlib_api/src/main/java/com/android/resources/FolderTypeRelationship.java b/layoutlib_api/src/main/java/com/android/resources/FolderTypeRelationship.java
//Synthetic comment -- index 61a6d85..d174940 100644

//Synthetic comment -- @@ -29,10 +29,10 @@
*/
public final class FolderTypeRelationship {

    private static final Map<ResourceType, List<ResourceFolderType>> mTypeToFolderMap =
new HashMap<ResourceType, List<ResourceFolderType>>();

    private static final Map<ResourceFolderType, List<ResourceType>> mFolderToTypeMap =
new HashMap<ResourceFolderType, List<ResourceType>>();

static {








//Synthetic comment -- diff --git a/layoutlib_api/src/main/java/com/android/resources/ResourceConstants.java b/layoutlib_api/src/main/java/com/android/resources/ResourceConstants.java
//Synthetic comment -- index 748f88a..4e92e3c 100644

//Synthetic comment -- @@ -22,29 +22,29 @@
public class ResourceConstants {

/** Default anim resource folder name, i.e. "anim" */
    public static final String FD_RES_ANIM = "anim"; //$NON-NLS-1$
/** Default animator resource folder name, i.e. "animator" */
    public static final String FD_RES_ANIMATOR = "animator"; //$NON-NLS-1$
/** Default color resource folder name, i.e. "color" */
    public static final String FD_RES_COLOR = "color"; //$NON-NLS-1$
/** Default drawable resource folder name, i.e. "drawable" */
    public static final String FD_RES_DRAWABLE = "drawable"; //$NON-NLS-1$
/** Default interpolator resource folder name, i.e. "interpolator" */
    public static final String FD_RES_INTERPOLATOR = "interpolator"; //$NON-NLS-1$
/** Default layout resource folder name, i.e. "layout" */
    public static final String FD_RES_LAYOUT = "layout"; //$NON-NLS-1$
/** Default menu resource folder name, i.e. "menu" */
    public static final String FD_RES_MENU = "menu"; //$NON-NLS-1$
/** Default menu resource folder name, i.e. "mipmap" */
    public static final String FD_RES_MIPMAP = "mipmap"; //$NON-NLS-1$
/** Default values resource folder name, i.e. "values" */
    public static final String FD_RES_VALUES = "values"; //$NON-NLS-1$
/** Default xml resource folder name, i.e. "xml" */
    public static final String FD_RES_XML = "xml"; //$NON-NLS-1$
/** Default raw resource folder name, i.e. "raw" */
    public static final String FD_RES_RAW = "raw"; //$NON-NLS-1$

/** Separator between the resource folder qualifier. */
    public static final String RES_QUALIFIER_SEP = "-"; //$NON-NLS-1$

}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/RuleAction.java b/rule_api/src/com/android/ide/common/api/RuleAction.java
//Synthetic comment -- index 34b8837..98609e8 100644

//Synthetic comment -- @@ -51,12 +51,12 @@
* Character used to split multiple checked choices.
* The pipe character "|" is used, to natively match Android resource flag separators.
*/
    public static final String CHOICE_SEP = "|"; //$NON-NLS-1$

/**
* Same as {@link #CHOICE_SEP} but safe for use in regular expressions.
*/
    public static final String CHOICE_SEP_PATTERN = Pattern.quote(CHOICE_SEP);

/**
* The unique id of the action.
//Synthetic comment -- @@ -90,7 +90,7 @@
/**
* Special value which will insert a separator in the choices' submenu.
*/
    public static final String SEPARATOR = "----";

// Factories

//Synthetic comment -- @@ -688,14 +688,16 @@
return mIconUrls;
}

        @NonNull
@Override
        public List<String> getIds() {
ensureInitialized();
return mIds;
}

        @NonNull
@Override
        public List<String> getTitles() {
ensureInitialized();
return mTitles;
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/rendering/HardwareConfigHelper.java b/sdk_common/src/com/android/ide/common/rendering/HardwareConfigHelper.java
//Synthetic comment -- index afca8fe..a6db019 100644

//Synthetic comment -- @@ -35,8 +35,10 @@
*/
public class HardwareConfigHelper {

    @NonNull
    private final Device mDevice;
    @NonNull
    private ScreenOrientation mScreenOrientation = ScreenOrientation.PORTRAIT;

// optional
private int mMaxRenderWidth = -1;








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/sdk_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index 0a353f9..a72470c 100644

//Synthetic comment -- @@ -78,7 +78,7 @@
@SuppressWarnings("deprecation")
public class LayoutLibrary {

    public static final String CLASS_BRIDGE = "com.android.layoutlib.bridge.Bridge"; //$NON-NLS-1$

/** Link to the layout bridge */
private final Bridge mBridge;








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/MultiResourceFile.java b/sdk_common/src/com/android/ide/common/resources/MultiResourceFile.java
//Synthetic comment -- index c9a8bc7..8531f3e 100644

//Synthetic comment -- @@ -43,7 +43,7 @@
*/
public final class MultiResourceFile extends ResourceFile implements IValueResourceRepository {

    private static final SAXParserFactory sParserFactory = SAXParserFactory.newInstance();

private final Map<ResourceType, Map<String, ResourceValue>> mResourceItems =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ResourceItem.java b/sdk_common/src/com/android/ide/common/resources/ResourceItem.java
//Synthetic comment -- index 49396eb..3fb0f91 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
*/
public class ResourceItem implements Comparable<ResourceItem> {

    private static final Comparator<ResourceFile> sComparator = new Comparator<ResourceFile>() {
@Override
public int compare(ResourceFile file1, ResourceFile file2) {
// get both FolderConfiguration and compare them








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/SingleResourceFile.java b/sdk_common/src/com/android/ide/common/resources/SingleResourceFile.java
//Synthetic comment -- index 8e394d3..1e7bb7c 100644

//Synthetic comment -- @@ -39,7 +39,7 @@
*/
public class SingleResourceFile extends ResourceFile {

    private static final SAXParserFactory sParserFactory = SAXParserFactory.newInstance();
static {
sParserFactory.setNamespaceAware(true);
}








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ValueResourceParser.java b/sdk_common/src/com/android/ide/common/resources/ValueResourceParser.java
//Synthetic comment -- index aabfd35..53a6bd1 100644

//Synthetic comment -- @@ -32,15 +32,15 @@
public final class ValueResourceParser extends DefaultHandler {

// TODO: reuse definitions from somewhere else.
    private static final String NODE_RESOURCES = "resources";
    private static final String NODE_ITEM = "item";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_PARENT = "parent";
    private static final String ATTR_VALUE = "value";

    private static final String DEFAULT_NS_PREFIX = "android:";
    private static final int DEFAULT_NS_PREFIX_LEN = DEFAULT_NS_PREFIX.length();

public interface IValueResourceRepository {
void addResourceValue(ResourceValue value);








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/configuration/CountryCodeQualifier.java b/sdk_common/src/com/android/ide/common/resources/configuration/CountryCodeQualifier.java
//Synthetic comment -- index eb7cc0d..1d5abc3 100644

//Synthetic comment -- @@ -24,9 +24,9 @@
*/
public final class CountryCodeQualifier extends ResourceQualifier {
/** Default pixel density value. This means the property is not set. */
    private static final int DEFAULT_CODE = -1;

    private static final Pattern sCountryCodePattern = Pattern.compile("^mcc(\\d{3})$");//$NON-NLS-1$

private final int mCode;









//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/configuration/DensityQualifier.java b/sdk_common/src/com/android/ide/common/resources/configuration/DensityQualifier.java
//Synthetic comment -- index a9e4a01..de23a4f 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
* Resource Qualifier for Screen Pixel Density.
*/
public final class DensityQualifier extends EnumBasedResourceQualifier {
    private static final Pattern sDensityLegacyPattern = Pattern.compile("^(\\d+)dpi$");//$NON-NLS-1$

public static final String NAME = "Density";









//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java b/sdk_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java
//Synthetic comment -- index fc83359..f01a091 100644

//Synthetic comment -- @@ -32,7 +32,7 @@
*/
public final class FolderConfiguration implements Comparable<FolderConfiguration> {

    private static final ResourceQualifier[] DEFAULT_QUALIFIERS;

static {
// get the default qualifiers.
//Synthetic comment -- @@ -44,28 +44,28 @@

private final ResourceQualifier[] mQualifiers = new ResourceQualifier[INDEX_COUNT];

    private static final int INDEX_COUNTRY_CODE          = 0;
    private static final int INDEX_NETWORK_CODE          = 1;
    private static final int INDEX_LANGUAGE              = 2;
    private static final int INDEX_REGION                = 3;
    private static final int INDEX_LAYOUTDIR             = 4;
    private static final int INDEX_SMALLEST_SCREEN_WIDTH = 5;
    private static final int INDEX_SCREEN_WIDTH          = 6;
    private static final int INDEX_SCREEN_HEIGHT         = 7;
    private static final int INDEX_SCREEN_LAYOUT_SIZE    = 8;
    private static final int INDEX_SCREEN_RATIO          = 9;
    private static final int INDEX_SCREEN_ORIENTATION    = 10;
    private static final int INDEX_UI_MODE               = 11;
    private static final int INDEX_NIGHT_MODE            = 12;
    private static final int INDEX_PIXEL_DENSITY         = 13;
    private static final int INDEX_TOUCH_TYPE            = 14;
    private static final int INDEX_KEYBOARD_STATE        = 15;
    private static final int INDEX_TEXT_INPUT_METHOD     = 16;
    private static final int INDEX_NAVIGATION_STATE      = 17;
    private static final int INDEX_NAVIGATION_METHOD     = 18;
    private static final int INDEX_SCREEN_DIMENSION      = 19;
    private static final int INDEX_VERSION               = 20;
    private static final int INDEX_COUNT                 = 21;

/**
* Creates a {@link FolderConfiguration} matching the folder segments.








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/configuration/LanguageQualifier.java b/sdk_common/src/com/android/ide/common/resources/configuration/LanguageQualifier.java
//Synthetic comment -- index 76514e2..128b72b 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
* Resource Qualifier for Language.
*/
public final class LanguageQualifier extends ResourceQualifier {
    private static final Pattern sLanguagePattern = Pattern.compile("^[a-z]{2}$"); //$NON-NLS-1$

public static final String FAKE_LANG_VALUE = "__"; //$NON-NLS-1$
public static final String NAME = "Language";








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/configuration/NetworkCodeQualifier.java b/sdk_common/src/com/android/ide/common/resources/configuration/NetworkCodeQualifier.java
//Synthetic comment -- index 1ef2015..a1b41ba 100644

//Synthetic comment -- @@ -24,13 +24,13 @@
*/
public final class NetworkCodeQualifier extends ResourceQualifier {
/** Default pixel density value. This means the property is not set. */
    private static final int DEFAULT_CODE = -1;

    private static final Pattern sNetworkCodePattern = Pattern.compile("^mnc(\\d{1,3})$"); //$NON-NLS-1$

private final int mCode;

    public static final String NAME = "Mobile Network Code";

/**
* Creates and returns a qualifier from the given folder segment. If the segment is incorrect,








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/configuration/RegionQualifier.java b/sdk_common/src/com/android/ide/common/resources/configuration/RegionQualifier.java
//Synthetic comment -- index bd033bd..7993090 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
* Resource Qualifier for Region.
*/
public final class RegionQualifier extends ResourceQualifier {
    private static final Pattern sRegionPattern = Pattern.compile("^r([A-Z]{2})$"); //$NON-NLS-1$

public static final String FAKE_REGION_VALUE = "__"; //$NON-NLS-1$
public static final String NAME = "Region";








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/configuration/ScreenDimensionQualifier.java b/sdk_common/src/com/android/ide/common/resources/configuration/ScreenDimensionQualifier.java
//Synthetic comment -- index dce6c68..74dc245 100644

//Synthetic comment -- @@ -24,9 +24,9 @@
*/
public final class ScreenDimensionQualifier extends ResourceQualifier {
/** Default screen size value. This means the property is not set */
    static final int DEFAULT_SIZE = -1;

    private static final Pattern sDimensionPattern = Pattern.compile(
"^(\\d+)x(\\d+)$"); //$NON-NLS-1$

public static final String NAME = "Screen Dimension";








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/configuration/ScreenHeightQualifier.java b/sdk_common/src/com/android/ide/common/resources/configuration/ScreenHeightQualifier.java
//Synthetic comment -- index 08bba61..14e2c38 100644

//Synthetic comment -- @@ -24,10 +24,10 @@
*/
public final class ScreenHeightQualifier extends ResourceQualifier {
/** Default screen size value. This means the property is not set */
    static final int DEFAULT_SIZE = -1;

    private static final Pattern sParsePattern = Pattern.compile("^h(\\d+)dp$");//$NON-NLS-1$
    private static final String sPrintPattern = "h%1$ddp";

public static final String NAME = "Screen Height";









//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/configuration/ScreenWidthQualifier.java b/sdk_common/src/com/android/ide/common/resources/configuration/ScreenWidthQualifier.java
//Synthetic comment -- index ab9134b..50bba41 100644

//Synthetic comment -- @@ -24,10 +24,10 @@
*/
public final class ScreenWidthQualifier extends ResourceQualifier {
/** Default screen size value. This means the property is not set */
    static final int DEFAULT_SIZE = -1;

    private static final Pattern sParsePattern = Pattern.compile("^w(\\d+)dp$"); //$NON-NLS-1$
    private static final String sPrintPattern = "w%1$ddp"; //$NON-NLS-1$

public static final String NAME = "Screen Width";









//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/configuration/SmallestScreenWidthQualifier.java b/sdk_common/src/com/android/ide/common/resources/configuration/SmallestScreenWidthQualifier.java
//Synthetic comment -- index 35d1ab1..babdd06 100644

//Synthetic comment -- @@ -24,10 +24,10 @@
*/
public final class SmallestScreenWidthQualifier extends ResourceQualifier {
/** Default screen size value. This means the property is not set */
    static final int DEFAULT_SIZE = -1;

    private static final Pattern sParsePattern = Pattern.compile("^sw(\\d+)dp$"); //$NON-NLS-1$
    private static final String sPrintPattern = "sw%1$ddp"; //$NON-NLS-1$

public static final String NAME = "Smallest Screen Width";









//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/configuration/VersionQualifier.java b/sdk_common/src/com/android/ide/common/resources/configuration/VersionQualifier.java
//Synthetic comment -- index 078d4af..24a2631 100644

//Synthetic comment -- @@ -24,9 +24,9 @@
*/
public final class VersionQualifier extends ResourceQualifier {
/** Default pixel density value. This means the property is not set. */
    private static final int DEFAULT_VERSION = -1;

    private static final Pattern sVersionPattern = Pattern.compile("^v(\\d+)$");//$NON-NLS-1$

private int mVersion = DEFAULT_VERSION;









//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/xml/AndroidManifestParser.java b/sdk_common/src/com/android/ide/common/xml/AndroidManifestParser.java
//Synthetic comment -- index 7fde6ce..eafa13d 100644

//Synthetic comment -- @@ -50,14 +50,14 @@

public class AndroidManifestParser {

    private static final int LEVEL_TOP = 0;
    private static final int LEVEL_INSIDE_MANIFEST = 1;
    private static final int LEVEL_INSIDE_APPLICATION = 2;
    private static final int LEVEL_INSIDE_APP_COMPONENT = 3;
    private static final int LEVEL_INSIDE_INTENT_FILTER = 4;

    private static final String ACTION_MAIN = "android.intent.action.MAIN"; //$NON-NLS-1$
    private static final String CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER"; //$NON-NLS-1$

public interface ManifestErrorHandler extends ErrorHandler {
/**
//Synthetic comment -- @@ -569,7 +569,7 @@

}

    private static final SAXParserFactory sParserFactory;

static {
sParserFactory = SAXParserFactory.newInstance();








//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/xml/ManifestData.java b/sdk_common/src/com/android/ide/common/xml/ManifestData.java
//Synthetic comment -- index 9b68d60..da45d2b 100644

//Synthetic comment -- @@ -33,13 +33,13 @@
* Value returned by {@link #getMinSdkVersion()} when the value of the minSdkVersion attribute
* in the manifest is a codename and not an integer value.
*/
    public static final int MIN_SDK_CODENAME = 0;

/**
* Value returned by {@link #getGlEsVersion()} when there are no <uses-feature> node with the
* attribute glEsVersion set.
*/
    public static final int GL_ES_VERSION_NOT_SET = -1;

/** Application package */
String mPackage;
//Synthetic comment -- @@ -74,7 +74,7 @@
/**
* Instrumentation info obtained from manifest
*/
    public static final class Instrumentation {
private final String mName;
private final String mTargetPackage;

//Synthetic comment -- @@ -101,7 +101,7 @@
/**
* Activity info obtained from the manifest.
*/
    public static final class Activity {
private final String mName;
private final boolean mIsExported;
private boolean mHasAction = false;
//Synthetic comment -- @@ -158,7 +158,7 @@
*
* To get an instance with all the actual values, use {@link #resolveSupportsScreensValues(int)}
*/
    public static final class SupportsScreens {
private Boolean mResizeable;
private Boolean mAnyDensity;
private Boolean mSmallScreens;
//Synthetic comment -- @@ -466,7 +466,7 @@
/**
* Class representing a <code>uses-library</code> node in the manifest.
*/
    public static final class UsesLibrary {
String mName;
Boolean mRequired = Boolean.TRUE; // default is true even if missing

//Synthetic comment -- @@ -482,7 +482,7 @@
/**
* Class representing a <code>uses-feature</code> node in the manifest.
*/
    public static final class UsesFeature {
String mName;
int mGlEsVersion = 0;
Boolean mRequired = Boolean.TRUE;  // default is true even if missing
//Synthetic comment -- @@ -506,7 +506,7 @@
/**
* Class representing the <code>uses-configuration</code> node in the manifest.
*/
    public static final class UsesConfiguration {
Boolean mReqFiveWayNav;
Boolean mReqHardKeyboard;
Keyboard mReqKeyboardType;







