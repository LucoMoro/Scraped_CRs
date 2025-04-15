/*Fix refresh issue in the AVD Manager.

In tools 21.0.0, hitting refrehs in the AVD Manager will
create the list display to blink rapidly several times
in a row. Same happens when a new AVD is created from a
device.

The issue is that the DeviceManager reparses and rebuilds
its internal lists from scratch for every single call to
getDevices() and getDeviceStatus(). Each time it notifies
the listeners, and consequently the AVD Manager rebuilds
its table. 2 fixes for that:
- First use a boolean guard to prevent recursive refreshes
  of the avd manager table in AvdSelector.
- Second fix DeviceManager to not rebuilt its lists all
  the time.

This also changes DeviceManager so that callers create
one instance that contains all the lists and there no static
data shared between the instances.
This is more deterministic. It shifts the responsibility to
the callers to pass around the same instance if they want the
data to remain consistent.

Change-Id:Ia0c104395fe599071892e24bc234f8cffde9f635*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java
//Synthetic comment -- index 750c192..a81ddc8 100644

//Synthetic comment -- @@ -69,7 +69,7 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.DeviceManager.DevicesChangeListener;
import com.android.sdklib.devices.State;
import com.android.utils.Pair;
import com.google.common.base.Objects;
//Synthetic comment -- @@ -109,7 +109,7 @@
* {@link Configuration} by configuring various constraints.
*/
public class ConfigurationChooser extends Composite
        implements DevicesChangeListener, DisposeListener {
private static final String ICON_SQUARE = "square";           //$NON-NLS-1$
private static final String ICON_LANDSCAPE = "landscape";     //$NON-NLS-1$
private static final String ICON_PORTRAIT = "portrait";       //$NON-NLS-1$
//Synthetic comment -- @@ -858,11 +858,11 @@
private void initDevices() {
final Sdk sdk = Sdk.getCurrent();
if (sdk != null) {
            mDeviceList = sdk.getDevices();
DeviceManager manager = sdk.getDeviceManager();
// This method can be called more than once, so avoid duplicate entries
manager.unregisterListener(this);
manager.registerListener(this);
} else {
mDeviceList = new ArrayList<Device>();
}
//Synthetic comment -- @@ -1353,12 +1353,16 @@
}
}

    // ---- Implements DevicesChangeListener ----

@Override
    public void onDevicesChange() {
final Sdk sdk = Sdk.getCurrent();
        mDeviceList = sdk.getDevices();
}

// ---- Reacting to UI changes ----








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index d9020f8..66c5dcd 100644

//Synthetic comment -- @@ -702,12 +702,6 @@
return mDeviceManager;
}

    /** Returns the devices provided by the SDK, including user created devices */
    @NonNull
    public List<Device> getDevices() {
        return mDeviceManager.getDevices(getSdkLocation());
    }

/**
* Returns a list of {@link ProjectState} representing projects depending, directly or
* indirectly on a given library project.
//Synthetic comment -- @@ -784,10 +778,10 @@
IResourceDelta.CHANGED | IResourceDelta.ADDED | IResourceDelta.REMOVED);

// pre-compute some paths
        mDocBaseUrl = getDocumentationBaseUrl(mManager.getLocation() +
SdkConstants.OS_SDK_DOCS_FOLDER);

        mDeviceManager = new DeviceManager(AdtPlugin.getDefault());

// update whatever ProjectState is already present with new IAndroidTarget objects.
synchronized (LOCK) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationTest.java
//Synthetic comment -- index 726d9c9..4ab2935 100644

//Synthetic comment -- @@ -49,8 +49,9 @@
configuration.setTheme("@style/Theme");
assertEquals("@style/Theme", configuration.getTheme());

        DeviceManager deviceManager = new DeviceManager(new StdLogger(StdLogger.Level.VERBOSE));
        List<Device> devices = deviceManager.getDefaultDevices();
assertNotNull(devices);
assertTrue(devices.size() > 0);
configuration.setDevice(devices.get(0), false);
//Synthetic comment -- @@ -107,8 +108,9 @@
assertNotNull(configuration);
configuration.setTheme("@style/Theme");
assertEquals("@style/Theme", configuration.getTheme());
        DeviceManager deviceManager = new DeviceManager(new StdLogger(StdLogger.Level.VERBOSE));
        List<Device> devices = deviceManager.getDefaultDevices();
assertNotNull(devices);
assertTrue(devices.size() > 0);
configuration.setDevice(devices.get(0), false);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 37b6876..2f6a9a5 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdklib.devices;

import com.android.SdkConstants;
import com.android.annotations.Nullable;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
//Synthetic comment -- @@ -56,20 +57,26 @@
*/
public class DeviceManager {

    private final static String sDeviceProfilesProp = "DeviceProfiles";
    private final static Pattern sPathPropertyPattern = Pattern.compile("^" + PkgProps.EXTRA_PATH
            + "=" + sDeviceProfilesProp + "$");
private ILogger mLog;
    // Vendor devices can't be a static list since they change based on the SDK
    // Location
private List<Device> mVendorDevices;
    // Keeps track of where the currently loaded vendor devices were loaded from
    private String mVendorDevicesLocation = "";
    private static List<Device> mUserDevices;
    private static List<Device> mDefaultDevices;
    private static final Object sLock = new Object();
    private static final List<DevicesChangeListener> sListeners =
                                        new ArrayList<DevicesChangeListener>();

public static enum DeviceStatus {
/**
//Synthetic comment -- @@ -86,10 +93,26 @@
MISSING;
}

    // TODO: Refactor this to look more like AvdManager so that we don't have
    // multiple instances in the same application, which forces us to parse
    // the XML multiple times when we don't have to.
    public DeviceManager(ILogger log) {
mLog = log;
}

//Synthetic comment -- @@ -97,11 +120,11 @@
* Interface implemented by objects which want to know when changes occur to the {@link Device}
* lists.
*/
    public static interface DevicesChangeListener {
/**
* Called after one of the {@link Device} lists has been updated.
*/
        public void onDevicesChange();
}

/**
//Synthetic comment -- @@ -109,7 +132,7 @@
*
* @param listener The listener to add. Ignored if already registered.
*/
    public void registerListener(DevicesChangeListener listener) {
if (listener != null) {
synchronized (sListeners) {
if (!sListeners.contains(listener)) {
//Synthetic comment -- @@ -125,15 +148,14 @@
*
* @param listener The listener to remove.
*/
    public boolean unregisterListener(DevicesChangeListener listener) {
synchronized (sListeners) {
return sListeners.remove(listener);
}
}

    public DeviceStatus getDeviceStatus(
            @Nullable String sdkLocation, String name, String manufacturer, int hashCode) {
        Device d = getDevice(sdkLocation, name, manufacturer);
if (d == null) {
return DeviceStatus.MISSING;
} else {
//Synthetic comment -- @@ -141,46 +163,64 @@
}
}

    public Device getDevice(@Nullable String sdkLocation, String name, String manufacturer) {
        List<Device> devices;
        if (sdkLocation != null) {
            devices = getDevices(sdkLocation);
        } else {
            devices = new ArrayList<Device>(getDefaultDevices());
            devices.addAll(getUserDevices());
        }
        for (Device d : devices) {
            if (d.getName().equals(name) && d.getManufacturer().equals(manufacturer)) {
                return d;
}
}
return null;
}

/**
     * Returns both vendor provided and user created {@link Device}s.
*
     * @param sdkLocation Location of the Android SDK
     * @return A list of both vendor and user provided {@link Device}s
*/
    public List<Device> getDevices(String sdkLocation) {
        List<Device> devices = new ArrayList<Device>(getVendorDevices(sdkLocation));
        devices.addAll(getDefaultDevices());
        devices.addAll(getUserDevices());
return Collections.unmodifiableList(devices);
}

/**
     * Gets the {@link List} of {@link Device}s packaged with the SDK.
     *
     * @return The {@link List} of default {@link Device}s
*/
    public List<Device> getDefaultDevices() {
        synchronized (sLock) {
if (mDefaultDevices == null) {
try {
mDefaultDevices = DeviceParser.parse(
DeviceManager.class.getResourceAsStream(SdkConstants.FN_DEVICES_XML));
} catch (IllegalStateException e) {
// The device builders can throw IllegalStateExceptions if
// build gets called before everything is properly setup
//Synthetic comment -- @@ -190,65 +230,64 @@
mLog.error(null, "Error reading default devices");
mDefaultDevices = new ArrayList<Device>();
}
                notifyListeners();
}
}
        return Collections.unmodifiableList(mDefaultDevices);
}

/**
     * Returns all vendor-provided {@link Device}s
     *
     * @param sdkLocation Location of the Android SDK
     * @return A list of vendor-provided {@link Device}s
*/
    public List<Device> getVendorDevices(String sdkLocation) {
        synchronized (sLock) {
            if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
                mVendorDevicesLocation = sdkLocation;
                List<Device> devices = new ArrayList<Device>();

                // Load devices from tools folder
                File toolsDevices = new File(sdkLocation, SdkConstants.OS_SDK_TOOLS_LIB_FOLDER +
                        File.separator + SdkConstants.FN_DEVICES_XML);
                if (toolsDevices.isFile()) {
                    devices.addAll(loadDevices(toolsDevices));
                }

                // Load devices from vendor extras
                File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
                List<File> deviceDirs = getExtraDirs(extrasFolder);
                for (File deviceDir : deviceDirs) {
                    File deviceXml = new File(deviceDir, SdkConstants.FN_DEVICES_XML);
                    if (deviceXml.isFile()) {
                        devices.addAll(loadDevices(deviceXml));
}
}
                mVendorDevices = devices;
                notifyListeners();
}
}
        return Collections.unmodifiableList(mVendorDevices);
}

/**
     * Returns all user-created {@link Device}s
     *
     * @return All user-created {@link Device}s
*/
    public List<Device> getUserDevices() {
        synchronized (sLock) {
if (mUserDevices == null) {
// User devices should be saved out to
// $HOME/.android/devices.xml
mUserDevices = new ArrayList<Device>();
File userDevicesFile = null;
try {
                    userDevicesFile = new File(AndroidLocation.getFolder(),
SdkConstants.FN_DEVICES_XML);
if (userDevicesFile.exists()) {
mUserDevices.addAll(DeviceParser.parse(userDevicesFile));
                        notifyListeners();
}
} catch (AndroidLocationException e) {
mLog.warning("Couldn't load user devices: %1$s", e.getMessage());
//Synthetic comment -- @@ -262,7 +301,8 @@
renamedConfig = new File(base + '.' + (i++));
}
mLog.error(null, "Error parsing %1$s, backing up to %2$s",
                                userDevicesFile.getAbsolutePath(), renamedConfig.getAbsolutePath());
userDevicesFile.renameTo(renamedConfig);
}
} catch (ParserConfigurationException e) {
//Synthetic comment -- @@ -274,42 +314,52 @@
}
}
}
        return Collections.unmodifiableList(mUserDevices);
}

public void addUserDevice(Device d) {
        synchronized (sLock) {
if (mUserDevices == null) {
                getUserDevices();
}
            mUserDevices.add(d);
}
        notifyListeners();
}

public void removeUserDevice(Device d) {
        synchronized (sLock) {
if (mUserDevices == null) {
                getUserDevices();
}
            Iterator<Device> it = mUserDevices.iterator();
            while (it.hasNext()) {
                Device userDevice = it.next();
                if (userDevice.getName().equals(d.getName())
                        && userDevice.getManufacturer().equals(d.getManufacturer())) {
                    it.remove();
                    notifyListeners();
                    break;
                }

}
}
}

public void replaceUserDevice(Device d) {
        synchronized (sLock) {
if (mUserDevices == null) {
                getUserDevices();
}
removeUserDevice(d);
addUserDevice(d);
//Synthetic comment -- @@ -339,7 +389,7 @@
return;
}

        synchronized (sLock) {
if (mUserDevices.size() > 0) {
try {
DeviceWriter.writeToXml(new FileOutputStream(userDevicesFile), mUserDevices);
//Synthetic comment -- @@ -446,8 +496,8 @@

private void notifyListeners() {
synchronized (sListeners) {
            for (DevicesChangeListener listener : sListeners) {
                listener.onDevicesChange();
}
}
}
//Synthetic comment -- @@ -483,7 +533,7 @@
try {
String line;
while ((line = propertiesReader.readLine()) != null) {
                    Matcher m = sPathPropertyPattern.matcher(line);
if (m.matches()) {
return true;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 6b219d3..f70471f 100644

//Synthetic comment -- @@ -59,7 +59,7 @@
/**
* Exception thrown when something is wrong with a target path.
*/
    private final static class InvalidTargetPathException extends Exception {
private static final long serialVersionUID = 1L;

InvalidTargetPathException(String message) {
//Synthetic comment -- @@ -69,26 +69,26 @@

public static final String AVD_FOLDER_EXTENSION = ".avd";  //$NON-NLS-1$

    public final static String AVD_INFO_PATH = "path";         //$NON-NLS-1$
    public final static String AVD_INFO_TARGET = "target";     //$NON-NLS-1$

/**
* AVD/config.ini key name representing the abi type of the specific avd
*
*/
    public final static String AVD_INI_ABI_TYPE = "abi.type"; //$NON-NLS-1$

/**
* AVD/config.ini key name representing the CPU architecture of the specific avd
*
*/
    public final static String AVD_INI_CPU_ARCH = "hw.cpu.arch"; //$NON-NLS-1$

/**
* AVD/config.ini key name representing the CPU architecture of the specific avd
*
*/
    public final static String AVD_INI_CPU_MODEL = "hw.cpu.model"; //$NON-NLS-1$

/**
* AVD/config.ini key name representing the manufacturer of the device this avd was based on.
//Synthetic comment -- @@ -106,19 +106,19 @@
*
* @see #NUMERIC_SKIN_SIZE
*/
    public final static String AVD_INI_SKIN_PATH = "skin.path"; //$NON-NLS-1$
/**
* AVD/config.ini key name representing an UI name for the skin.
* This config key is ignored by the emulator. It is only used by the SDK manager or
* tools to give a friendlier name to the skin.
* If missing, use the {@link #AVD_INI_SKIN_PATH} key instead.
*/
    public final static String AVD_INI_SKIN_NAME = "skin.name"; //$NON-NLS-1$

/**
* AVD/config.ini key name representing whether a dynamic skin should be displayed.
*/
    public final static String AVD_INI_SKIN_DYNAMIC = "skin.dynamic"; //$NON-NLS-1$

/**
* AVD/config.ini key name representing the path to the sdcard file.
//Synthetic comment -- @@ -127,7 +127,7 @@
*
* @see #SDCARD_IMG
*/
    public final static String AVD_INI_SDCARD_PATH = "sdcard.path"; //$NON-NLS-1$
/**
* AVD/config.ini key name representing the size of the SD card.
* This property is for UI purposes only. It is not used by the emulator.
//Synthetic comment -- @@ -135,7 +135,7 @@
* @see #SDCARD_SIZE_PATTERN
* @see #parseSdcardSize(String, String[])
*/
    public final static String AVD_INI_SDCARD_SIZE = "sdcard.size"; //$NON-NLS-1$
/**
* AVD/config.ini key name representing the first path where the emulator looks
* for system images. Typically this is the path to the add-on system image or
//Synthetic comment -- @@ -143,80 +143,80 @@
* <p/>
* The emulator looks at {@link #AVD_INI_IMAGES_1} before {@link #AVD_INI_IMAGES_2}.
*/
    public final static String AVD_INI_IMAGES_1 = "image.sysdir.1"; //$NON-NLS-1$
/**
* AVD/config.ini key name representing the second path where the emulator looks
* for system images. Typically this is the path to the platform system image.
*
* @see #AVD_INI_IMAGES_1
*/
    public final static String AVD_INI_IMAGES_2 = "image.sysdir.2"; //$NON-NLS-1$
/**
* AVD/config.ini key name representing the presence of the snapshots file.
* This property is for UI purposes only. It is not used by the emulator.
*
* @see #SNAPSHOTS_IMG
*/
    public final static String AVD_INI_SNAPSHOT_PRESENT = "snapshot.present"; //$NON-NLS-1$

/**
* AVD/config.ini key name representing whether hardware OpenGLES emulation is enabled
*/
    public final static String AVD_INI_GPU_EMULATION = "hw.gpu.enabled"; //$NON-NLS-1$

/**
* AVD/config.ini key name representing how to emulate the front facing camera
*/
    public final static String AVD_INI_CAMERA_FRONT = "hw.camera.front"; //$NON-NLS-1$

/**
* AVD/config.ini key name representing how to emulate the rear facing camera
*/
    public final static String AVD_INI_CAMERA_BACK = "hw.camera.back"; //$NON-NLS-1$

/**
* AVD/config.ini key name representing the amount of RAM the emulated device should have
*/
    public final static String AVD_INI_RAM_SIZE = "hw.ramSize";

/**
* AVD/config.ini key name representing the amount of memory available to applications by default
*/
    public final static String AVD_INI_VM_HEAP_SIZE = "vm.heapSize";

/**
* AVD/config.ini key name representing the size of the data partition
*/
    public final static String AVD_INI_DATA_PARTITION_SIZE = "disk.dataPartition.size";

/**
* AVD/config.ini key name representing the hash of the device this AVD is based on
*/
    public final static String AVD_INI_DEVICE_HASH = "hw.device.hash";

/**
* Pattern to match pixel-sized skin "names", e.g. "320x480".
*/
    public final static Pattern NUMERIC_SKIN_SIZE = Pattern.compile("([0-9]{2,})x([0-9]{2,})"); //$NON-NLS-1$

    private final static String USERDATA_IMG = "userdata.img"; //$NON-NLS-1$
    final static String CONFIG_INI = "config.ini"; //$NON-NLS-1$
    private final static String SDCARD_IMG = "sdcard.img"; //$NON-NLS-1$
    private final static String SNAPSHOTS_IMG = "snapshots.img"; //$NON-NLS-1$

    final static String INI_EXTENSION = ".ini"; //$NON-NLS-1$
    private final static Pattern INI_NAME_PATTERN = Pattern.compile("(.+)\\" + //$NON-NLS-1$
INI_EXTENSION + "$",                                               //$NON-NLS-1$
Pattern.CASE_INSENSITIVE);

    private final static Pattern IMAGE_NAME_PATTERN = Pattern.compile("(.+)\\.img$", //$NON-NLS-1$
Pattern.CASE_INSENSITIVE);

/**
* Pattern for matching SD Card sizes, e.g. "4K" or "16M".
* Callers should use {@link #parseSdcardSize(String, String[])} instead of using this directly.
*/
    private final static Pattern SDCARD_SIZE_PATTERN = Pattern.compile("(\\d+)([KMG])"); //$NON-NLS-1$

/**
* Minimal size of an SDCard image file in bytes. Currently 9 MiB.
//Synthetic comment -- @@ -229,19 +229,19 @@
public static final long SDCARD_MAX_BYTE_SIZE = 1023L<<30;

/** The sdcard string represents a valid number but the size is outside of the allowed range. */
    public final static int SDCARD_SIZE_NOT_IN_RANGE = 0;
/** The sdcard string looks like a size number+suffix but the number failed to decode. */
    public final static int SDCARD_SIZE_INVALID = -1;
/** The sdcard string doesn't look like a size, it might be a path instead. */
    public final static int SDCARD_NOT_SIZE_PATTERN = -2;

/** Regex used to validate characters that compose an AVD name. */
    public final static Pattern RE_AVD_NAME = Pattern.compile("[a-zA-Z0-9._-]+"); //$NON-NLS-1$

/** List of valid characters for an AVD name. Used for display purposes. */
    public final static String CHARS_AVD_NAME = "a-z A-Z 0-9 . _ -"; //$NON-NLS-1$

    public final static String HARDWARE_INI = "hardware.ini"; //$NON-NLS-1$

/**
* Status returned by {@link AvdManager#isAvdNameConflicting(String)}.
//Synthetic comment -- @@ -1410,8 +1410,8 @@
String hash = properties.get(AVD_INI_DEVICE_HASH);
if (deviceName != null && deviceMfctr != null && hash != null) {
int deviceHash = Integer.parseInt(hash);
                deviceStatus = (new DeviceManager(log)).getDeviceStatus(
                        mSdkManager.getLocation(), deviceName, deviceMfctr, deviceHash);
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java
//Synthetic comment -- index 4d7e79e..a092550 100755

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.DeviceManager.DevicesChangeListener;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdkuilib.internal.repository.UpdaterData;
//Synthetic comment -- @@ -44,7 +44,7 @@
* page displays the actually list of AVDs and various action buttons.
*/
public class AvdManagerPage extends Composite
    implements ISdkChangeListener, DevicesChangeListener, DisposeListener {

private AvdSelector mAvdSelector;

//Synthetic comment -- @@ -162,7 +162,7 @@
// --- Implementation of DevicesChangeListener

@Override
    public void onDevicesChange() {
mAvdSelector.refresh(false /*reload*/);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java
//Synthetic comment -- index 08eed1b..2fab4fd 100755

//Synthetic comment -- @@ -94,7 +94,7 @@
mContext = context;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
mOwnUpdaterData = true;
        mDeviceManager = new DeviceManager(sdkLog);
}

/**
//Synthetic comment -- @@ -116,7 +116,8 @@
mContext = context;
mUpdaterData = updaterData;
mOwnUpdaterData = false;
        mDeviceManager = new DeviceManager(mUpdaterData.getSdkLog());
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
//Synthetic comment -- index 6bac1ba..7b63d39 100755

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.DeviceManager.DevicesChangeListener;
import com.android.sdklib.devices.Hardware;
import com.android.sdklib.devices.Screen;
import com.android.sdklib.devices.Storage;
//Synthetic comment -- @@ -88,7 +88,7 @@
* - a filter box to do a string search on any part of the display.
*/
public class DeviceManagerPage extends Composite
    implements ISdkChangeListener, DevicesChangeListener, DisposeListener {

public interface IAvdCreatedListener {
public void onAvdCreated(AvdInfo createdAvdInfo);
//Synthetic comment -- @@ -355,11 +355,10 @@
try {
mDisableRefresh = true;
disposables.addAll(fillDevices(table, boldFont, true,
                    mDeviceManager.getUserDevices(),
                    null));
disposables.addAll(fillDevices(table, boldFont, false,
                    mDeviceManager.getDefaultDevices(),
                    mDeviceManager.getVendorDevices(mUpdaterData.getOsSdkRoot())));
} finally {
mDisableRefresh = false;
}
//Synthetic comment -- @@ -456,19 +455,15 @@
Table table,
Font boldFont,
boolean isUser,
            List<Device> devices1,
            List<Device> devices2) {
List<Resource> disposables = new ArrayList<Resource>();
Display display = table.getDisplay();

TextStyle boldStyle = new TextStyle();
boldStyle.font = boldFont;


        List<Device> devices = new ArrayList<Device>(devices1);
        if (devices2 != null) {
            devices.addAll(devices2);
        }

if (isUser) {
// Just sort user devices by alphabetical name. They will show up at the top.
//Synthetic comment -- @@ -582,15 +577,14 @@
}

// Constants extracted from DeviceMenuListerner -- TODO refactor somewhere else.
    @SuppressWarnings("unused")
    private static final String NEXUS = "Nexus";       //$NON-NLS-1$
private static final String GENERIC = "Generic";   //$NON-NLS-1$
private static Pattern PATTERN = Pattern.compile(
"(\\d+\\.?\\d*)in (.+?)( \\(.*Nexus.*\\))?"); //$NON-NLS-1$
/**
* Returns a pretty name for the device.
*
     * Extracted from DeviceMenuListerner.
* Modified to remove the leading space insertion as it doesn't render
* neatly in the avd manager. Instead added the option to add leading
* zeroes to make the string names sort properly.
//Synthetic comment -- @@ -828,7 +822,7 @@
// --- Implementation of DevicesChangeListener

@Override
    public void onDevicesChange() {
onRefresh();
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 5d2a264..4a3eb5e 100644

//Synthetic comment -- @@ -461,8 +461,8 @@
SdkManager sdkManager = mAvdManager.getSdkManager();
String location = sdkManager.getLocation();
if (sdkManager != null && location != null) {
            DeviceManager deviceManager = new DeviceManager(mSdkLog);
            List<Device> deviceList = new ArrayList<Device>(deviceManager.getDevices(location));

// Sort
List<Device> nexus = new ArrayList<Device>(deviceList.size());
//Synthetic comment -- @@ -608,6 +608,9 @@
case XHIGH:
case XXHIGH:
vmHeapSize = 128;
}
} else {
switch (density) {
//Synthetic comment -- @@ -622,7 +625,9 @@
case XHIGH:
case XXHIGH:
vmHeapSize = 64;

}
}
mVmHeap.setText(Integer.toString(vmHeapSize));
//Synthetic comment -- @@ -734,6 +739,8 @@
}
}

private IAndroidTarget getSelectedTarget() {
IAndroidTarget[] targets = (IAndroidTarget[]) mTarget.getData();
int index = mTarget.getSelectionIndex();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 42d85eb..0b8cd5c 100644

//Synthetic comment -- @@ -115,6 +115,8 @@

private final ILogger mSdkLog;


/**
* The display mode of the AVD Selector.
//Synthetic comment -- @@ -467,21 +469,29 @@
* <code>false</code>.
*/
public boolean refresh(boolean reload) {
        if (reload) {
try {
                mAvdManager.reloadAvds(NullLogger.getLogger());
            } catch (AndroidLocationException e) {
                return false;
}
}

        AvdInfo selected = getSelected();

        fillTable(mTable);

        setSelection(selected);

        return true;
}

/**
//Synthetic comment -- @@ -1021,7 +1031,8 @@
// Overwrite the properties derived from the device and nothing else
Map<String, String> properties = new HashMap<String, String>(avdInfo.getProperties());

            List<Device> devices = (new DeviceManager(mSdkLog)).getDevices(mOsSdkPath);
String name = properties.get(AvdManager.AVD_INI_DEVICE_NAME);
String manufacturer = properties.get(AvdManager.AVD_INI_DEVICE_MANUFACTURER);









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java
//Synthetic comment -- index 0c68dcf..500b76a 100644

//Synthetic comment -- @@ -100,7 +100,7 @@
mAvd = avd;
mSdkLocation = sdkLocation;
mSettingsController = settingsController;
        mDeviceManager = new DeviceManager(sdkLog);
if (mAvd == null) {
throw new IllegalArgumentException("avd cannot be null");
}
//Synthetic comment -- @@ -449,7 +449,7 @@
String name = properties.get(AvdManager.AVD_INI_DEVICE_NAME);
String mfctr = properties.get(AvdManager.AVD_INI_DEVICE_MANUFACTURER);
if (name != null && mfctr != null) {
                Device d = mDeviceManager.getDevice(mSdkLocation, name, mfctr);
if (d != null) {
double screenSize =
d.getDefaultHardware().getScreen().getDiagonalLength();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java
//Synthetic comment -- index 0707ddc..68c4fd5 100644

//Synthetic comment -- @@ -143,7 +143,7 @@
mImageFactory = imageFactory;
mDevice = device;
mManager = manager;
        mUserDevices = mManager.getUserDevices();
}

/**







