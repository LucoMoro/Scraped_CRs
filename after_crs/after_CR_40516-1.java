/*Make AVDs repairable when the base device changes or is deleted

Change-Id:I3b931ee511dc157320ca8658fe421fe28a1ff387*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 9cf0378..55db44f 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.devices;

import com.android.annotations.Nullable;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.resources.Keyboard;
//Synthetic comment -- @@ -23,7 +24,6 @@
import com.android.resources.Navigation;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.repository.PkgProps;
//Synthetic comment -- @@ -71,6 +71,21 @@
private static final List<DevicesChangeListener> listeners =
Collections.synchronizedList(new ArrayList<DevicesChangeListener>());

    public static enum DeviceStatus {
        /**
         * The device exists unchanged from the given configuration
         */
        EXISTS,
        /**
         * A device exists with the given name and manufacturer, but has a different configuration
         */
        CHANGED,
        /**
         * There is no device with the given name and manufacturer
         */
        MISSING;
    }

// TODO: Refactor this to look more like AvdManager so that we don't have
// multiple instances
// in the same application, which forces us to parse the XML multiple times
//Synthetic comment -- @@ -108,6 +123,23 @@
listeners.remove(listener);
}

    public DeviceStatus getDeviceStatus(
            @Nullable String sdkLocation, String name, String manufacturer, int hashCode) {
        List<Device> devices;
        if (sdkLocation != null) {
            devices = getDevices(sdkLocation);
        } else {
            devices = new ArrayList<Device>(getDefaultDevices());
            devices.addAll(getUserDevices());
        }
        for (Device d : devices) {
            if (d.getName().equals(name) && d.getManufacturer().equals(manufacturer)) {
                return d.hashCode() == hashCode ? DeviceStatus.EXISTS : DeviceStatus.CHANGED;
            }
        }
        return DeviceStatus.MISSING;
    }

/**
* Returns both vendor provided and user created {@link Device}s.
* 
//Synthetic comment -- @@ -290,7 +322,6 @@
public static Map<String, String> getHardwareProperties(State s) {
Hardware hw = s.getHardware();
Map<String, String> props = new HashMap<String, String>();
props.put("hw.mainKeys", getBooleanVal(hw.getButtonType().equals(ButtonType.HARD)));
props.put("hw.trackBall", getBooleanVal(hw.getNav().equals(Navigation.TRACKBALL)));
props.put("hw.keyboard", getBooleanVal(hw.getKeyboard().equals(Keyboard.QWERTY)));
//Synthetic comment -- @@ -302,7 +333,6 @@
props.put("hw.sensors.orientation", getBooleanVal(sensors.contains(Sensor.GYROSCOPE)));
props.put("hw.audioInput", getBooleanVal(hw.hasMic()));
props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
props.put("hw.lcd.density",
Integer.toString(hw.getScreen().getPixelDensity().getDpiValue()));
props.put("hw.sensors.proximity",
//Synthetic comment -- @@ -324,6 +354,9 @@
props.put("hw.keyboard.lid", getBooleanVal(true));
}
}
        props.put(AvdManager.AVD_INI_DEVICE_HASH, Integer.toString(d.hashCode()));
        props.put(AvdManager.AVD_INI_DEVICE_NAME, d.getName());
        props.put(AvdManager.AVD_INI_DEVICE_MANUFACTURER, d.getManufacturer());
return props;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java
//Synthetic comment -- index 54e2fa7..d2ec9db 100755

//Synthetic comment -- @@ -46,7 +46,11 @@
/** Unable to parse config.ini */
ERROR_PROPERTIES,
/** System Image folder in config.ini doesn't exist */
        ERROR_IMAGE_DIR,
        /** The {@link Device} this AVD is based on has changed from its original configuration*/
        ERROR_DEVICE_CHANGED,
        /** The {@link Device} this AVD is based on is no longer available */
        ERROR_DEVICE_MISSING;
}

private final String mName;
//Synthetic comment -- @@ -284,6 +288,14 @@
return String.format(
"Invalid value in image.sysdir. Run 'android update avd -n %1$s'",
mName);
            case ERROR_DEVICE_CHANGED:
                return String.format("%1$s %2$s configuration has changed since AVD creation",
                        mProperties.get(AvdManager.AVD_INI_DEVICE_MANUFACTURER),
                        mProperties.get(AvdManager.AVD_INI_DEVICE_NAME));
            case ERROR_DEVICE_MISSING:
                return String.format("%1$s %2$s no longer exists as a device",
                        mProperties.get(AvdManager.AVD_INI_DEVICE_MANUFACTURER),
                        mProperties.get(AvdManager.AVD_INI_DEVICE_NAME));
case OK:
assert false;
return null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 99ede54..06bdf1b 100644

//Synthetic comment -- @@ -25,6 +25,8 @@
import com.android.sdklib.ISystemImage;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.DeviceManager.DeviceStatus;
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.util.GrabProcessOutput;
//Synthetic comment -- @@ -182,6 +184,11 @@
public final static String AVD_INI_DATA_PARTITION_SIZE = "disk.dataPartition.size";

/**
     * AVD/config.ini key name representing the hash of the device this AVD is based on
     */
    public final static String AVD_INI_DEVICE_HASH = "hw.device.hash";

    /**
* Pattern to match pixel-sized skin "names", e.g. "320x480".
*/
public final static Pattern NUMERIC_SKIN_SIZE = Pattern.compile("([0-9]{2,})x([0-9]{2,})"); //$NON-NLS-1$
//Synthetic comment -- @@ -1389,6 +1396,20 @@
}
}

        // Get the device status if this AVD is associated with a device
        DeviceStatus deviceStatus = null;
        if (properties != null) {
            String deviceName = properties.get(AVD_INI_DEVICE_NAME);
            String deviceMfctr = properties.get(AVD_INI_DEVICE_MANUFACTURER);
            String hash = properties.get(AVD_INI_DEVICE_HASH);
            if (deviceName != null && deviceMfctr != null && hash != null) {
                int deviceHash = Integer.parseInt(hash);
                deviceStatus = (new DeviceManager(log)).getDeviceStatus(
                        mSdkManager.getLocation(), deviceName, deviceMfctr, deviceHash);
            }
        }


// TODO: What about missing sdcard, skins, etc?

AvdStatus status;
//Synthetic comment -- @@ -1405,6 +1426,10 @@
status = AvdStatus.ERROR_PROPERTIES;
} else if (validImageSysdir == false) {
status = AvdStatus.ERROR_IMAGE_DIR;
        } else if (deviceStatus == DeviceStatus.CHANGED) {
            status = AvdStatus.ERROR_DEVICE_CHANGED;
        } else if (deviceStatus == DeviceStatus.MISSING) {
            status = AvdStatus.ERROR_DEVICE_MISSING;
} else {
status = AvdStatus.OK;
}
//Synthetic comment -- @@ -1579,10 +1604,16 @@
//FIXME: display paths to empty image folders?
status = AvdStatus.ERROR_IMAGE_DIR;
}
        updateAvd(avd, properties, status, log);
    }

    public void updateAvd(AvdInfo avd,
            Map<String, String> newProperties,
            AvdStatus status,
            ISdkLog log) throws IOException {
// now write the config file
File configIniFile = new File(avd.getDataFolderPath(), CONFIG_INI);
        writeIniFile(configIniFile, newProperties);

// finally create a new AvdInfo for this unbroken avd and add it to the list.
// instead of creating the AvdInfo object directly we reparse it, to detect other possible
//Synthetic comment -- @@ -1595,8 +1626,7 @@
avd.getTargetHash(),
avd.getTarget(),
avd.getAbiType(),
                newProperties);

replaceAvd(avd, newAvd);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 6f3ec0e..fe6c058 100644

//Synthetic comment -- @@ -878,9 +878,6 @@
return false;
}

// Although the device has this information, some devices have more RAM than we'd want to
// allocate to an emulator.
hwProps.put(AvdManager.AVD_INI_RAM_SIZE, mRam.getText());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 58bacec..594ed21 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.NullSdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus;
import com.android.sdklib.internal.avd.AvdManager;
//Synthetic comment -- @@ -66,7 +68,10 @@
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
//Synthetic comment -- @@ -1000,21 +1005,49 @@
false /*logErrorsOnly*/);
}

        boolean success = true;

        if (avdInfo.getStatus() == AvdStatus.ERROR_IMAGE_DIR) {
            // delete the AVD
            try {
                mAvdManager.updateAvd(avdInfo, log);
                refresh(false /*reload*/);
            } catch (IOException e) {
                log.error(e, null);
                success = false;
            }
        } else if (avdInfo.getStatus() == AvdStatus.ERROR_DEVICE_CHANGED) {
            // Overwrite the properties derived from the device and nothing else
            Map<String, String> properties = new HashMap<String, String>(avdInfo.getProperties());

            List<Device> devices = (new DeviceManager(mSdkLog)).getDevices(mOsSdkPath);
            String name = properties.get(AvdManager.AVD_INI_DEVICE_NAME);
            String manufacturer = properties.get(AvdManager.AVD_INI_DEVICE_MANUFACTURER);

            if (properties != null && devices != null && name != null && manufacturer != null) {
                for (Device d : devices) {
                    if (d.getName().equals(name) && d.getManufacturer().equals(manufacturer)) {
                        properties.putAll(DeviceManager.getHardwareProperties(d));
                        try {
                            mAvdManager.updateAvd(avdInfo, properties, AvdStatus.OK, log);
                        } catch (IOException e) {
                            log.error(e,null);
                            success = false;
                        }
                    }
                }
            } else {
                log.error(null, "Base device information incomplete or missing.");
                success = false;
            }

// display the result
if (log instanceof MessageBoxLog) {
                ((MessageBoxLog) log).displayResult(success);
}
refresh(false /*reload*/);
        } else if (avdInfo.getStatus() == AvdStatus.ERROR_DEVICE_MISSING) {
            onEdit();
}
}

//Synthetic comment -- @@ -1200,6 +1233,8 @@
}

private boolean isAvdRepairable(AvdStatus avdStatus) {
        return avdStatus == AvdStatus.ERROR_IMAGE_DIR
                || avdStatus == AvdStatus.ERROR_DEVICE_CHANGED
                || avdStatus == AvdStatus.ERROR_DEVICE_MISSING;
}
}







