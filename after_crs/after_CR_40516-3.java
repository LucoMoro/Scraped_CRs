/*Make AVDs repairable when the base device changes or is deleted

Change-Id:I3b931ee511dc157320ca8658fe421fe28a1ff387*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Camera.java
//Synthetic comment -- index 4f26994..d7d33fe 100644

//Synthetic comment -- @@ -99,7 +99,7 @@
@Override
public int hashCode() {
int hash = 17;
        hash = 31 * hash + mLocation.ordinal();
hash = 31 * hash + (mAutofocus ? 1 : 0);
hash = 31 * hash + (mFlash ? 1 : 0);
return hash;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Device.java
//Synthetic comment -- index e4da52e..dcd82a2 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.sdklib.devices;

import com.android.dvlib.DeviceSchema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
* Instances of this class contain the specifications for a device. Use the
* {@link Builder} class to construct a Device object, or the
//Synthetic comment -- @@ -272,10 +272,15 @@
}

@Override
    /** A hash that's stable across JVM instances */
public int hashCode() {
int hash = 17;
        for (Character c : mName.toCharArray()) {
            hash = 31 * hash + c;
        }
        for (Character c : mManufacturer.toCharArray()) {
            hash = 31 * hash + c;
        }
hash = 31 * hash + mSoftware.hashCode();
hash = 31 * hash + mState.hashCode();
hash = 31 * hash + mMeta.hashCode();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 9cf0378..a58a664 100644

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
//Synthetic comment -- @@ -104,8 +119,25 @@
* notifications when modifications to the {@link Device} list occur.
* @param listener The listener to remove.
*/
    public boolean unregisterListener(DevicesChangeListener listener) {
        return listeners.remove(listener);
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









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Hardware.java
//Synthetic comment -- index 8993cd6..b12f11d 100644

//Synthetic comment -- @@ -282,21 +282,50 @@
public int hashCode() {
int hash = 17;
hash = 31 * hash + mScreen.hashCode();

        // Since sets have no defined order, we need to hash them in such a way that order doesn't
        // matter.
        int temp = 0;
        for (Network n : mNetworking) {
            temp |= 1 << n.ordinal();
        }
        hash = 31 * hash + temp;

        temp = 0;
        for (Sensor s : mSensors) {
            temp |= 1 << s.ordinal();
        }

        hash = 31 * hash + temp;
hash = 31 * hash + (mMic ? 1 : 0);
        hash = mCameras.hashCode();
        hash = 31 * hash + mKeyboard.ordinal();
        hash = 31 * hash + mNav.ordinal();
hash = 31 * hash + mRam.hashCode();
        hash = 31 * hash + mButtons.ordinal();
hash = 31 * hash + mInternalStorage.hashCode();
hash = 31 * hash + mRemovableStorage.hashCode();

        for (Character c : mCpu.toCharArray()) {
            hash = 31 * hash + c;
        }

        for (Character c : mGpu.toCharArray()) {
            hash = 31 * hash + c;
        }

        temp = 0;
        for (Abi a : mAbis) {
            temp |= 1 << a.ordinal();
        }
        hash = 31 * hash + temp;

        temp = 0;
        for (UiMode ui : mUiModes) {
            temp |= 1 << ui.ordinal();
        }
        hash = 31 * hash + temp;

return hash;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Meta.java
//Synthetic comment -- index 911c286..4c19f3f 100644

//Synthetic comment -- @@ -143,19 +143,27 @@
public int hashCode() {
int hash = 17;
if(mIconSixteen != null){
            for (Character c : mIconSixteen.getAbsolutePath().toCharArray()) {
                hash = 31 * hash + c;
            }
}
if(mIconSixtyFour != null){
            for (Character c : mIconSixtyFour.getAbsolutePath().toCharArray()) {
                hash = 31 * hash + c;
            }
}
if(mFrame != null){
            for (Character c : mFrame.getAbsolutePath().toCharArray()) {
                hash = 31 * hash + c;
            }
}
if(mFrameOffsetLandscape != null){
            hash = 31 * hash + mFrameOffsetLandscape.x;
            hash = 31 * hash + mFrameOffsetLandscape.y;
}
if(mFrameOffsetPortrait != null){
            hash = 31 * hash + mFrameOffsetPortrait.x;
            hash = 31 * hash + mFrameOffsetPortrait.y;
}
return hash;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Screen.java
//Synthetic comment -- index 958219d..a7f4334 100644

//Synthetic comment -- @@ -170,20 +170,20 @@
@Override
public int hashCode() {
int hash = 17;
        hash = 31 * hash + mScreenSize.ordinal();
long f = Double.doubleToLongBits(mDiagonalLength);
hash = 31 * hash + (int) (f ^ (f >>> 32));
        hash = 31 * hash + mPixelDensity.ordinal();
        hash = 31 * hash + mScreenRatio.ordinal();
hash = 31 * hash + mXDimension;
hash = 31 * hash + mYDimension;
f = Double.doubleToLongBits(mXdpi);
hash = 31 * hash + (int) (f ^ (f >>> 32));
f = Double.doubleToLongBits(mYdpi);
hash = 31 * hash + (int) (f ^ (f >>> 32));
        hash = 31 * hash + mMultitouch.ordinal();
        hash = 31 * hash + mMechanism.ordinal();
        hash = 31 * hash + mScreenType.ordinal();
return hash;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/Software.java
//Synthetic comment -- index a452b6e..58f13b0 100644

//Synthetic comment -- @@ -125,14 +125,23 @@
}

@Override
    /** A stable hash across JVM instances */
public int hashCode() {
int hash = 17;
hash = 31 * hash + mMinSdkLevel;
hash = 31 * hash + mMaxSdkLevel;
hash = 31 * hash + (mLiveWallpaperSupport ? 1 : 0);
        for (BluetoothProfile bp : mBluetoothProfiles) {
            hash = 31 * hash + bp.ordinal();
        }
        for (Character c : mGlVersion.toCharArray()) {
            hash = 31 * hash + c;
        }
        for (String glExtension : mGlExtensions) {
            for (Character c : glExtension.toCharArray()) {
                hash = 31 * hash + c;
            }
        }
hash = 31 * hash + (mStatusBar ? 1 : 0);
return hash;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/State.java
//Synthetic comment -- index eb75605..1dc6961 100644

//Synthetic comment -- @@ -125,12 +125,17 @@
public int hashCode() {
int hash = 17;
hash = 31 * hash + (mDefaultState ? 1 : 0);
        for (Character c : mName.toCharArray()) {
            hash = 31 * hash + c;
        }
        for (Character c : mDescription.toCharArray()) {
            hash = 31 * hash + c;
        }
        hash = 31 * hash + mOrientation.ordinal();
        hash = 31 * hash + mKeyState.ordinal();
        hash = 31 * hash + mNavState.ordinal();
hash = 31 * hash + mHardwareOverride.hashCode();
return hash;
}

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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman1/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman1/AvdManagerPage.java
//Synthetic comment -- index 63c53e0..6408a27 100755

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.sdkuilib.internal.repository.sdkman1;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.DeviceManager.DevicesChangeListener;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.widgets.AvdSelector;
//Synthetic comment -- @@ -29,23 +31,29 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class AvdManagerPage extends UpdaterPage implements ISdkChangeListener, DevicesChangeListener {

private AvdSelector mAvdSelector;

private final UpdaterData mUpdaterData;
    private final DeviceManager mDeviceManager;
/**
* Create the composite.
* @param parent The parent of the composite.
* @param updaterData An instance of {@link UpdaterData}.
*/
    public AvdManagerPage(Composite parent,
            int swtStyle,
            UpdaterData updaterData,
            DeviceManager deviceManager) {
super(parent, swtStyle);

mUpdaterData = updaterData;
mUpdaterData.addListeners(this);

        mDeviceManager = deviceManager;
        mDeviceManager.registerListener(this);

createContents(this);
postCreate();  //$hide$
}
//Synthetic comment -- @@ -79,6 +87,7 @@
@Override
public void dispose() {
mUpdaterData.removeListener(this);
        mDeviceManager.unregisterListener(this);
super.dispose();
}

//Synthetic comment -- @@ -120,6 +129,14 @@
// nothing to be done for now.
}

    // --- Implementation of DevicesChangeListener

    @Override
    public void onDevicesChange() {
        mAvdSelector.refresh(false /*reload*/);
    }


// End of hiding from SWT Designer
//$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/AvdManagerWindowImpl1.java
//Synthetic comment -- index 06cdc74..8a04b51 100755

//Synthetic comment -- @@ -69,6 +69,8 @@
private final UpdaterData mUpdaterData;
/** True if this window created the UpdaterData, in which case it needs to dispose it. */
private final boolean mOwnUpdaterData;
    private final DeviceManager mDeviceManager;


// --- UI members ---

//Synthetic comment -- @@ -94,6 +96,7 @@
mContext = context;
mUpdaterData = new UpdaterData(osSdkRoot, sdkLog);
mOwnUpdaterData = true;
        mDeviceManager = new DeviceManager(sdkLog);
}

/**
//Synthetic comment -- @@ -115,6 +118,7 @@
mContext = context;
mUpdaterData = updaterData;
mOwnUpdaterData = false;
        mDeviceManager = new DeviceManager(mUpdaterData.getSdkLog());
}

/**
//Synthetic comment -- @@ -161,6 +165,7 @@
public void widgetDisposed(DisposeEvent e) {
ShellSizeAndPos.saveSizeAndPos(mShell, SIZE_POS_PREFIX);    //$hide$
onAndroidSdkUpdaterDispose();                               //$hide$
                mAvdPage.dispose();                                         //$hide$
}
});

//Synthetic comment -- @@ -180,7 +185,7 @@

private void createContents() {

        mAvdPage = new AvdManagerPage(mShell, SWT.NONE, mUpdaterData, mDeviceManager);
mAvdPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
}

//Synthetic comment -- @@ -353,15 +358,13 @@
Menu menuDevices = new Menu(menuBarDevices);
menuBarDevices.setMenu(menuDevices);

MenuItem createDevice = new MenuItem(menuDevices, SWT.NONE);
createDevice.setText("Create New Device");
createDevice.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
DeviceCreationDialog dlg = new DeviceCreationDialog(
                        mShell, mDeviceManager, mUpdaterData.getImageFactory(), null);
if (dlg.open() == Window.OK) {
setupDevices(menuBarDevices);
}
//Synthetic comment -- @@ -370,7 +373,7 @@
new MenuItem(menuDevices, SWT.SEPARATOR);

Map<String, List<Device>> devices = new HashMap<String, List<Device>>();
        for (Device d : mDeviceManager.getDevices(mUpdaterData.getOsSdkRoot())) {
List<Device> l;
if (devices.containsKey(d.getManufacturer())) {
l = devices.get(d.getManufacturer());
//Synthetic comment -- @@ -393,7 +396,7 @@
@Override
public void widgetSelected(SelectionEvent e) {
DeviceCreationDialog dlg = new DeviceCreationDialog(
                                mShell, mDeviceManager, mUpdaterData.getImageFactory(), d);
if(dlg.open() == Window.OK) {
setupDevices(menuBarDevices);
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







