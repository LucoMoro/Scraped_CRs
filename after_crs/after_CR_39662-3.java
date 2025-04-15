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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
//Synthetic comment -- @@ -58,30 +60,44 @@
private final static Pattern sPathPropertyPattern = Pattern.compile("^" + PkgProps.EXTRA_PATH
+ "=" + sDeviceProfilesProp + "$");
private ISdkLog mLog;
    // Vendor devices can't be a static list since they change based on the SDK
    // Location
private List<Device> mVendorDevices;
    // Keeps track of where the currently loaded vendor devices were loaded from
    private String mVendorDevicesLocation = "";
    private static List<Device> mUserDevices;
    private static List<Device> mDefaultDevices;
    private static final Object lock = new Object();

    // TODO: Refactor this to look more like AvdManager so that we don't have
    // multiple instances
    // in the same application, which forces us to parse the XML multiple times
    // when we don't
    // to.
public DeviceManager(ISdkLog log) {
mLog = log;
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
        synchronized (lock) {
if (mDefaultDevices == null) {
try {
mDefaultDevices = DeviceParser.parse(
//Synthetic comment -- @@ -97,19 +113,18 @@
}
}
}
        return Collections.unmodifiableList(mDefaultDevices);
}

/**
* Returns all vendor provided {@link Device}s
     * 
     * @param sdkLocation Location of the Android SDK
* @return A list of vendor provided {@link Device}s
*/
public List<Device> getVendorDevices(String sdkLocation) {
        synchronized (lock) {
            if (mVendorDevices == null || !mVendorDevicesLocation.equals(sdkLocation)) {
List<Device> devices = new ArrayList<Device>();
File extrasFolder = new File(sdkLocation, SdkConstants.FD_EXTRAS);
List<File> deviceDirs = getExtraDirs(extrasFolder);
//Synthetic comment -- @@ -122,16 +137,16 @@
mVendorDevices = devices;
}
}
        return Collections.unmodifiableList(mVendorDevices);
}

/**
* Returns all user created {@link Device}s
     * 
* @return All user created {@link Device}s
*/
public List<Device> getUserDevices() {
        synchronized (lock) {
if (mUserDevices == null) {
// User devices should be saved out to
// $HOME/.android/devices.xml
//Synthetic comment -- @@ -145,11 +160,52 @@
}
}
}
        return Collections.unmodifiableList(mUserDevices);
}

    public void addUserDevice(Device d) {
        synchronized (lock) {
            if (mUserDevices == null) {
                getUserDevices();
            }
            mUserDevices.add(d);
        }
    }

    public void removeUserDevice(Device d) {
        synchronized (lock) {
            if (mUserDevices == null) {
                getUserDevices();
            }
            Iterator<Device> it = mUserDevices.iterator();
            while (it.hasNext()) {
                Device userDevice = it.next();
                if (userDevice.getName().equals(d.getName())
                        && userDevice.getManufacturer().equals(d.getManufacturer())) {
                    it.remove();
                    break;
                }

            }
        }
    }

    public void replaceUserDevice(Device d) {
        synchronized (lock) {
            if (mUserDevices == null) {
                getUserDevices();
            }
            removeUserDevice(d);
            addUserDevice(d);
        }
    }

    /**
     * Saves out the user devices to {@link SdkConstants#FN_DEVICES_XML} in
     * {@link AndroidLocation#getFolder()}.
     */
public void saveUserDevices() {
        synchronized (lock) {
if (mUserDevices != null && mUserDevices.size() != 0) {
File userDevicesFile;
try {
//Synthetic comment -- @@ -173,6 +229,7 @@

/**
* Returns hardware properties (defined in hardware.ini) as a {@link Map}.
     * 
* @param The {@link State} from which to derive the hardware properties.
* @return A {@link Map} of hardware properties.
*/
//Synthetic comment -- @@ -188,6 +245,7 @@
props.put("hw.gps", getBooleanVal(sensors.contains(Sensor.GPS)));
props.put("hw.battery", getBooleanVal(hw.getChargeType().equals(PowerType.BATTERY)));
props.put("hw.accelerometer", getBooleanVal(sensors.contains(Sensor.ACCELEROMETER)));
        props.put("hw.sensors.orientation", getBooleanVal(sensors.contains(Sensor.GYROSCOPE)));
props.put("hw.audioInput", getBooleanVal(hw.hasMic()));
props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
//Synthetic comment -- @@ -195,24 +253,46 @@
Integer.toString(hw.getScreen().getPixelDensity().getDpiValue()));
props.put("hw.sensors.proximity",
getBooleanVal(sensors.contains(Sensor.PROXIMITY_SENSOR)));

        for (Camera c : hw.getCameras()) {
            String prop;
            if (c.getLocation().equals(CameraLocation.FRONT)) {
                prop = "hw.camera.front";
            } else {
                prop = "hw.camera.back";
            }
            props.put(prop, "emulated");
        }

return props;
}

/**
     * Returns the hardware properties defined in
     * {@link AvdManager.HARDWARE_INI} as a {@link Map}.
     * 
* @param The {@link Device} from which to derive the hardware properties.
* @return A {@link Map} of hardware properties.
*/
public static Map<String, String> getHardwareProperties(Device d) {
Map<String, String> props = getHardwareProperties(d.getDefaultState());
for (State s : d.getAllStates()) {
            if (s.getKeyState().equals(KeyboardState.HIDDEN)) {
props.put("hw.keyboard.lid", getBooleanVal(true));
}
}
return getHardwareProperties(d.getDefaultState());
}

    /**
     * Takes a boolean and returns the appropriate value for
     * {@link HardwareProperties}
     * 
     * @param bool The boolean value to turn into the appropriate
     *            {@link HardwareProperties} value.
     * @return {@value HardwareProperties#BOOLEAN_VALUES[0]} if true,
     *         {@value HardwareProperties#BOOLEAN_VALUES[1]} otherwise.
     */
private static String getBooleanVal(boolean bool) {
if (bool) {
return HardwareProperties.BOOLEAN_VALUES[0];
//Synthetic comment -- @@ -276,7 +356,8 @@
} finally {
propertiesReader.close();
}
        } catch (IOException ignore) {
        }
return false;
}
}







