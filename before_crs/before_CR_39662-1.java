/*Add user device management capabilities to DeviceManager

Also shared user devices across manager instances to prevent multiple
parses of the device specifications and multiple copies of the devices
being kept in memory.

Change-Id:I19236cc8efa5553d73a4d84aef4175831f20b986*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index ac5fe5e..61d8307 100644

//Synthetic comment -- @@ -38,7 +38,9 @@
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
//Synthetic comment -- @@ -58,30 +60,44 @@
private final static Pattern sPathPropertyPattern = Pattern.compile("^" + PkgProps.EXTRA_PATH
+ "=" + sDeviceProfilesProp + "$");
private ISdkLog mLog;
private List<Device> mVendorDevices;
    private List<Device> mUserDevices;
    private List<Device> mDefaultDevices;

public DeviceManager(ISdkLog log) {
mLog = log;
}

/**
* Returns both vendor provided and user created {@link Device}s.
     *
     * @param sdkLocation
     *            Location of the Android SDK
* @return A list of both vendor and user provided {@link Device}s
*/
public List<Device> getDevices(String sdkLocation) {
List<Device> devices = new ArrayList<Device>(getVendorDevices(sdkLocation));
devices.addAll(getDefaultDevices());
devices.addAll(getUserDevices());
        return devices;
}

public List<Device> getDefaultDevices() {
        synchronized (this) {
if (mDefaultDevices == null) {
try {
mDefaultDevices = DeviceParser.parse(
//Synthetic comment -- @@ -97,19 +113,18 @@
}
}
}
        return mDefaultDevices;
}

/**
* Returns all vendor provided {@link Device}s
     *
     * @param sdkLocation
     *            Location of the Android SDK
* @return A list of vendor provided {@link Device}s
*/
public List<Device> getVendorDevices(String sdkLocation) {
        synchronized (this) {
            if (mVendorDevices == null) {
List<Device> devices = new ArrayList<Device>();
File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
List<File> deviceDirs = getExtraDirs(extrasFolder);
//Synthetic comment -- @@ -122,16 +137,16 @@
mVendorDevices = devices;
}
}
        return mVendorDevices;
}

/**
* Returns all user created {@link Device}s
     *
* @return All user created {@link Device}s
*/
public List<Device> getUserDevices() {
        synchronized (this) {
if (mUserDevices == null) {
// User devices should be saved out to
// $HOME/.android/devices.xml
//Synthetic comment -- @@ -145,11 +160,52 @@
}
}
}
        return mUserDevices;
}

public void saveUserDevices() {
        synchronized (this) {
if (mUserDevices != null && mUserDevices.size() != 0) {
File userDevicesFile;
try {
//Synthetic comment -- @@ -173,6 +229,7 @@

/**
* Returns hardware properties (defined in hardware.ini) as a {@link Map}.
* @param The {@link State} from which to derive the hardware properties.
* @return A {@link Map} of hardware properties.
*/
//Synthetic comment -- @@ -188,6 +245,7 @@
props.put("hw.gps", getBooleanVal(sensors.contains(Sensor.GPS)));
props.put("hw.battery", getBooleanVal(hw.getChargeType().equals(PowerType.BATTERY)));
props.put("hw.accelerometer", getBooleanVal(sensors.contains(Sensor.ACCELEROMETER)));
props.put("hw.audioInput", getBooleanVal(hw.hasMic()));
props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
//Synthetic comment -- @@ -195,24 +253,46 @@
Integer.toString(hw.getScreen().getPixelDensity().getDpiValue()));
props.put("hw.sensors.proximity",
getBooleanVal(sensors.contains(Sensor.PROXIMITY_SENSOR)));
return props;
}

/**
     * Returns the hardware properties defined in {@link AvdManager.HARDWARE_INI} as a {@link Map}.
* @param The {@link Device} from which to derive the hardware properties.
* @return A {@link Map} of hardware properties.
*/
public static Map<String, String> getHardwareProperties(Device d) {
Map<String, String> props = getHardwareProperties(d.getDefaultState());
for (State s : d.getAllStates()) {
            if (s.getKeyState().equals(KeyboardState.HIDDEN)){
props.put("hw.keyboard.lid", getBooleanVal(true));
}
}
return getHardwareProperties(d.getDefaultState());
}

private static String getBooleanVal(boolean bool) {
if (bool) {
return HardwareProperties.BOOLEAN_VALUES[0];
//Synthetic comment -- @@ -276,7 +356,8 @@
} finally {
propertiesReader.close();
}
        } catch (IOException ignore) { }
return false;
}
}







