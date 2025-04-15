/*Make AVDs repairable when the base device changes or is deleted

Change-Id:I3b931ee511dc157320ca8658fe421fe28a1ff387*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 9cf0378..55db44f 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.devices;

import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.resources.Keyboard;
//Synthetic comment -- @@ -23,7 +24,6 @@
import com.android.resources.Navigation;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.devices.Storage.Unit;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.repository.PkgProps;
//Synthetic comment -- @@ -71,6 +71,21 @@
private static final List<DevicesChangeListener> listeners =
Collections.synchronizedList(new ArrayList<DevicesChangeListener>());

// TODO: Refactor this to look more like AvdManager so that we don't have
// multiple instances
// in the same application, which forces us to parse the XML multiple times
//Synthetic comment -- @@ -108,6 +123,23 @@
listeners.remove(listener);
}

/**
* Returns both vendor provided and user created {@link Device}s.
* 
//Synthetic comment -- @@ -290,7 +322,6 @@
public static Map<String, String> getHardwareProperties(State s) {
Hardware hw = s.getHardware();
Map<String, String> props = new HashMap<String, String>();
        props.put("hw.ramSize", Long.toString(hw.getRam().getSizeAsUnit(Unit.MiB)));
props.put("hw.mainKeys", getBooleanVal(hw.getButtonType().equals(ButtonType.HARD)));
props.put("hw.trackBall", getBooleanVal(hw.getNav().equals(Navigation.TRACKBALL)));
props.put("hw.keyboard", getBooleanVal(hw.getKeyboard().equals(Keyboard.QWERTY)));
//Synthetic comment -- @@ -302,7 +333,6 @@
props.put("hw.sensors.orientation", getBooleanVal(sensors.contains(Sensor.GYROSCOPE)));
props.put("hw.audioInput", getBooleanVal(hw.hasMic()));
props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
        props.put("hw.sdCard", getBooleanVal(hw.getRemovableStorage().size() > 0));
props.put("hw.lcd.density",
Integer.toString(hw.getScreen().getPixelDensity().getDpiValue()));
props.put("hw.sensors.proximity",
//Synthetic comment -- @@ -324,6 +354,9 @@
props.put("hw.keyboard.lid", getBooleanVal(true));
}
}
return props;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java
//Synthetic comment -- index 54e2fa7..d2ec9db 100755

//Synthetic comment -- @@ -46,7 +46,11 @@
/** Unable to parse config.ini */
ERROR_PROPERTIES,
/** System Image folder in config.ini doesn't exist */
        ERROR_IMAGE_DIR;
}

private final String mName;
//Synthetic comment -- @@ -284,6 +288,14 @@
return String.format(
"Invalid value in image.sysdir. Run 'android update avd -n %1$s'",
mName);
case OK:
assert false;
return null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 99ede54..06bdf1b 100644

//Synthetic comment -- @@ -25,6 +25,8 @@
import com.android.sdklib.ISystemImage;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.util.GrabProcessOutput;
//Synthetic comment -- @@ -182,6 +184,11 @@
public final static String AVD_INI_DATA_PARTITION_SIZE = "disk.dataPartition.size";

/**
* Pattern to match pixel-sized skin "names", e.g. "320x480".
*/
public final static Pattern NUMERIC_SKIN_SIZE = Pattern.compile("([0-9]{2,})x([0-9]{2,})"); //$NON-NLS-1$
//Synthetic comment -- @@ -1389,6 +1396,20 @@
}
}

// TODO: What about missing sdcard, skins, etc?

AvdStatus status;
//Synthetic comment -- @@ -1405,6 +1426,10 @@
status = AvdStatus.ERROR_PROPERTIES;
} else if (validImageSysdir == false) {
status = AvdStatus.ERROR_IMAGE_DIR;
} else {
status = AvdStatus.OK;
}
//Synthetic comment -- @@ -1579,10 +1604,16 @@
//FIXME: display paths to empty image folders?
status = AvdStatus.ERROR_IMAGE_DIR;
}

// now write the config file
File configIniFile = new File(avd.getDataFolderPath(), CONFIG_INI);
        writeIniFile(configIniFile, properties);

// finally create a new AvdInfo for this unbroken avd and add it to the list.
// instead of creating the AvdInfo object directly we reparse it, to detect other possible
//Synthetic comment -- @@ -1595,8 +1626,7 @@
avd.getTargetHash(),
avd.getTarget(),
avd.getAbiType(),
                properties,
                status);

replaceAvd(avd, newAvd);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 6f3ec0e..fe6c058 100644

//Synthetic comment -- @@ -878,9 +878,6 @@
return false;
}

        hwProps.put(AvdManager.AVD_INI_DEVICE_MANUFACTURER, mDeviceManufacturer.getText());
        hwProps.put(AvdManager.AVD_INI_DEVICE_NAME, mDeviceName.getText());

// Although the device has this information, some devices have more RAM than we'd want to
// allocate to an emulator.
hwProps.put(AvdManager.AVD_INI_RAM_SIZE, mRam.getText());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 58bacec..594ed21 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.NullSdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus;
import com.android.sdklib.internal.avd.AvdManager;
//Synthetic comment -- @@ -66,7 +68,10 @@
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Locale;


/**
//Synthetic comment -- @@ -1000,21 +1005,49 @@
false /*logErrorsOnly*/);
}

        // delete the AVD
        try {
            mAvdManager.updateAvd(avdInfo, log);

// display the result
if (log instanceof MessageBoxLog) {
                ((MessageBoxLog) log).displayResult(true /* success */);
}
refresh(false /*reload*/);

        } catch (IOException e) {
            log.error(e, null);
            if (log instanceof MessageBoxLog) {
                ((MessageBoxLog) log).displayResult(false /* success */);
            }
}
}

//Synthetic comment -- @@ -1200,6 +1233,8 @@
}

private boolean isAvdRepairable(AvdStatus avdStatus) {
        return avdStatus == AvdStatus.ERROR_IMAGE_DIR;
}
}







