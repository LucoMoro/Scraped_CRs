/*Set default scaled screen size to device's screen size

Change-Id:I6b169f0ef21d8f45da35dd10a7f76b30f3defcbb*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index a58a664..9fb51c8 100644

//Synthetic comment -- @@ -125,6 +125,15 @@

public DeviceStatus getDeviceStatus(
@Nullable String sdkLocation, String name, String manufacturer, int hashCode) {
        Device d = getDevice(sdkLocation, name, manufacturer);
        if (d == null) {
            return DeviceStatus.MISSING;
        } else {
            return d.hashCode() == hashCode ? DeviceStatus.EXISTS : DeviceStatus.CHANGED;
        }
    }

    public Device getDevice(@Nullable String sdkLocation, String name, String manufacturer) {
List<Device> devices;
if (sdkLocation != null) {
devices = getDevices(sdkLocation);
//Synthetic comment -- @@ -134,10 +143,10 @@
}
for (Device d : devices) {
if (d.getName().equals(name) && d.getManufacturer().equals(manufacturer)) {
                return d;
}
}
        return null;
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 594ed21..c447c89 100644

//Synthetic comment -- @@ -1089,7 +1089,7 @@
}

AvdStartDialog dialog = new AvdStartDialog(mTable.getShell(), avdInfo, mOsSdkPath,
                mController, mSdkLog);
if (dialog.open() == Window.OK) {
String path = mOsSdkPath + File.separator
+ SdkConstants.OS_SDK_TOOLS_FOLDER








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java
//Synthetic comment -- index 63787f9..9649f95 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.sdkuilib.internal.widgets;

import com.android.sdklib.ISdkLog;
import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdkuilib.internal.repository.SettingsController;
//Synthetic comment -- @@ -72,6 +75,7 @@
private final AvdInfo mAvd;
private final String mSdkLocation;
private final SettingsController mSettingsController;
    private final DeviceManager mDeviceManager;

private Text mScreenSize;
private Text mMonitorDpi;
//Synthetic comment -- @@ -91,11 +95,12 @@
private Button mSnapshotLaunchCheckbox;

AvdStartDialog(Shell parentShell, AvdInfo avd, String sdkLocation,
            SettingsController settingsController, ISdkLog sdkLog) {
super(parentShell, 2, false);
mAvd = avd;
mSdkLocation = sdkLocation;
mSettingsController = settingsController;
        mDeviceManager = new DeviceManager(sdkLog);
if (mAvd == null) {
throw new IllegalArgumentException("avd cannot be null");
}
//Synthetic comment -- @@ -430,7 +435,8 @@
/**
* Returns the screen size to start with.
* <p/>If an emulator with the same skin was already launched, scaled, the size used is reused.
     * <p/>If one hasn't been launched and the AVD is based on a device, use the device's screen
     * size. Otherwise, use the default (3).
*/
private String getScreenSize() {
String size = sSkinScaling.get(mAvd.getName());
//Synthetic comment -- @@ -438,6 +444,20 @@
return size;
}

        Map<String, String> properties = mAvd.getProperties();
        if (properties != null) {
            String name = properties.get(AvdManager.AVD_INI_DEVICE_NAME);
            String mfctr = properties.get(AvdManager.AVD_INI_DEVICE_MANUFACTURER);
            if (name != null && mfctr != null) {
                Device d = mDeviceManager.getDevice(mSdkLocation, name, mfctr);
                if (d != null) {
                    double screenSize =
                        d.getDefaultHardware().getScreen().getDiagonalLength();
                    return String.format("%.1f", screenSize);
                }
            }
        }

return "3";
}

//Synthetic comment -- @@ -607,5 +627,4 @@
mSnapshotLaunch = enabled && sSnapshotLaunch;
mSnapshotLaunchCheckbox.setSelection(mSnapshotLaunch);
}
}







