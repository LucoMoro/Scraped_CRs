/*Set default scaled screen size to device's screen size

Change-Id:I6b169f0ef21d8f45da35dd10a7f76b30f3defcbb*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index a58a664..9fb51c8 100644

//Synthetic comment -- @@ -125,6 +125,15 @@

public DeviceStatus getDeviceStatus(
@Nullable String sdkLocation, String name, String manufacturer, int hashCode) {
List<Device> devices;
if (sdkLocation != null) {
devices = getDevices(sdkLocation);
//Synthetic comment -- @@ -134,10 +143,10 @@
}
for (Device d : devices) {
if (d.getName().equals(name) && d.getManufacturer().equals(manufacturer)) {
                return d.hashCode() == hashCode ? DeviceStatus.EXISTS : DeviceStatus.CHANGED;
}
}
        return DeviceStatus.MISSING;
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 594ed21..c447c89 100644

//Synthetic comment -- @@ -1089,7 +1089,7 @@
}

AvdStartDialog dialog = new AvdStartDialog(mTable.getShell(), avdInfo, mOsSdkPath,
                mController);
if (dialog.open() == Window.OK) {
String path = mOsSdkPath + File.separator
+ SdkConstants.OS_SDK_TOOLS_FOLDER








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java
//Synthetic comment -- index 63787f9..9649f95 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.sdkuilib.internal.widgets;

import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdkuilib.internal.repository.SettingsController;
//Synthetic comment -- @@ -72,6 +75,7 @@
private final AvdInfo mAvd;
private final String mSdkLocation;
private final SettingsController mSettingsController;

private Text mScreenSize;
private Text mMonitorDpi;
//Synthetic comment -- @@ -91,11 +95,12 @@
private Button mSnapshotLaunchCheckbox;

AvdStartDialog(Shell parentShell, AvdInfo avd, String sdkLocation,
            SettingsController settingsController) {
super(parentShell, 2, false);
mAvd = avd;
mSdkLocation = sdkLocation;
mSettingsController = settingsController;
if (mAvd == null) {
throw new IllegalArgumentException("avd cannot be null");
}
//Synthetic comment -- @@ -430,7 +435,8 @@
/**
* Returns the screen size to start with.
* <p/>If an emulator with the same skin was already launched, scaled, the size used is reused.
     * <p/>Otherwise the default is returned (3)
*/
private String getScreenSize() {
String size = sSkinScaling.get(mAvd.getName());
//Synthetic comment -- @@ -438,6 +444,20 @@
return size;
}

return "3";
}

//Synthetic comment -- @@ -607,5 +627,4 @@
mSnapshotLaunch = enabled && sSnapshotLaunch;
mSnapshotLaunchCheckbox.setSelection(mSnapshotLaunch);
}

}







