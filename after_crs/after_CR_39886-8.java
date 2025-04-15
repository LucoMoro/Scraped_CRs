/*Use minSdk for device/avd compatibility checks

When checking whether a device can run a certain project,
the only valid conditionals are:
 - If the device API level is less than the minApiLevel,
   then the device cannot run the app.
 - If the app is built against a standard SDK, then the above
   check is sufficient.
 - If the app is built against an add-on, then we cannot
   determine for sure if a device can run the app.
   An AVD might provide some additional info that can be
   used to determine if the app cannot be run on a particular
   AVD.

This CL fixes a bug where platforms with API level greater
than the build target API level were being filtered out from
the DeviceChooserDialog.

It also fixes another bug where running devices that are
clearly not capable of running an app were being displayed
in the DeviceChooserDialog, albeit with a red check mark
indicating that they cannot run the app. Selecting that
device for deployment would error out in the next step.
This CL filters out such devices.
Fixeshttp://code.google.com/p/android/issues/detail?id=35367Change-Id:Iba31a45572dabe3895c4e51707ed515d407caae0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 1f004f5..b8caadb 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.ddmlib.TimeoutException;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.actions.AvdManagerAction;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration.TargetMode;
import com.android.ide.eclipse.adt.internal.launch.DelayedLaunchInfo.InstallRetryMode;
import com.android.ide.eclipse.adt.internal.launch.DeviceChooserDialog.DeviceChooserResponse;
//Synthetic comment -- @@ -324,9 +325,15 @@
config.mTargetMode = TargetMode.MANUAL;
}

        // get the sdk against which the project is built
IAndroidTarget projectTarget = currentSdk.getTarget(project);

        // get the min required android version
        ManifestInfo mi = ManifestInfo.get(project);
        final int minApiLevel = mi.getMinSdkVersion();
        final String minApiCodeName = mi.getMinSdkCodeName();
        final AndroidVersion minApiVersion = new AndroidVersion(minApiLevel, minApiCodeName);

// FIXME: check errors on missing sdk, AVD manager, or project target.

// device chooser response object.
//Synthetic comment -- @@ -370,17 +377,22 @@
AvdInfo preferredAvd = null;
if (config.mAvdName != null) {
preferredAvd = avdManager.getAvd(config.mAvdName, true /*validAvdOnly*/);
                IAndroidTarget preferredAvdTarget = preferredAvd.getTarget();
                if (preferredAvdTarget != null
                        && !preferredAvdTarget.getVersion().canRun(minApiVersion)) {
preferredAvd = null;

AdtPlugin.printErrorToConsole(project, String.format(
                            "Preferred AVD '%1$s' (API Level: %2$d) cannot run application with minApi %3$s. Looking for a compatible AVD...",
                            config.mAvdName,
                            preferredAvdTarget.getVersion().getApiLevel(),
                            minApiVersion));
}
}

if (preferredAvd != null) {
                // We have a preferred avd that can actually run the application.
                // Now see if the AVD is running, and if so use it, otherwise launch it.

for (IDevice d : devices) {
String deviceAvd = d.getAvdName();
//Synthetic comment -- @@ -416,38 +428,31 @@
// a device which target is the same as the project's target) and use it as the
// new default.

            if (minApiCodeName != null && minApiLevel < projectTarget.getVersion().getApiLevel()) {
                int maxDist = projectTarget.getVersion().getApiLevel() - minApiLevel;
                IAndroidTarget candidate = null;

                for (IAndroidTarget target : currentSdk.getTargets()) {
                    if (target.canRunOn(projectTarget)) {
                        int currDist = target.getVersion().getApiLevel() - minApiLevel;
                        if (currDist >= 0 && currDist < maxDist) {
                            maxDist = currDist;
                            candidate = target;
                            if (maxDist == 0) {
                                // Found a perfect match
                                break;
}
}
}
}

                if (candidate != null) {
                    // We found a better SDK target candidate, that is closer to the
                    // API level from minSdkVersion than the one currently used by the
                    // project. Below (in the for...devices loop) we'll try to find
                    // a device/AVD for it.
                    projectTarget = candidate;
                }
}

HashMap<IDevice, AvdInfo> compatibleRunningAvds = new HashMap<IDevice, AvdInfo>();
//Synthetic comment -- @@ -578,7 +583,7 @@
boolean includeDevices = config.mTargetMode != TargetMode.ALL_EMULATORS;
boolean includeAvds = config.mTargetMode != TargetMode.ALL_DEVICES;
Collection<IDevice> compatibleDevices = findCompatibleDevices(devices,
                    minApiVersion, includeDevices, includeAvds);
if (compatibleDevices.size() == 0) {
AdtPlugin.printErrorToConsole(project,
"No active compatible AVD's or devices found. "
//Synthetic comment -- @@ -601,7 +606,8 @@
// or the AVD to launch.
DeviceChooserDialog dialog = new DeviceChooserDialog(
AdtPlugin.getDisplay().getActiveShell(),
                            response, launchInfo.getPackageName(),
                            desiredProjectTarget, minApiVersion);
if (dialog.open() == Dialog.OK) {
DeviceChoiceCache.put(launch.getLaunchConfiguration().getName(), response);
continueLaunch.set(true);
//Synthetic comment -- @@ -634,22 +640,14 @@
/**
* Returns devices that can run a app of provided API level.
* @param devices list of devices to filter from
     * @param requiredVersion minimum required API that should be supported
* @param includeDevices include physical devices in the filtered list
* @param includeAvds include emulators in the filtered list
* @return set of compatible devices, may be an empty set
*/
private Collection<IDevice> findCompatibleDevices(IDevice[] devices,
            AndroidVersion requiredVersion, boolean includeDevices, boolean includeAvds) {
Set<IDevice> compatibleDevices = new HashSet<IDevice>(devices.length);
AvdManager avdManager = Sdk.getCurrent().getAvdManager();
for (IDevice d: devices) {
boolean isEmulator = d.isEmulator();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChooserDialog.java
//Synthetic comment -- index 781deaf..5638d51 100644

//Synthetic comment -- @@ -57,6 +57,9 @@
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import java.util.ArrayList;
import java.util.List;

/**
* A dialog that lets the user choose a device to deploy an application.
* The user can either choose an exiting running device (including running emulators)
//Synthetic comment -- @@ -80,6 +83,7 @@
private final DeviceChooserResponse mResponse;
private final String mPackageName;
private final IAndroidTarget mProjectTarget;
    private final AndroidVersion mMinApiVersion;
private final Sdk mSdk;

private Button mDeviceRadioButton;
//Synthetic comment -- @@ -92,16 +96,32 @@
* Basic Content Provider for a table full of {@link IDevice} objects. The input is
* a {@link AndroidDebugBridge}.
*/
    private class ContentProvider implements IStructuredContentProvider {
@Override
public Object[] getElements(Object inputElement) {
if (inputElement instanceof AndroidDebugBridge) {
                return findCompatibleDevices(((AndroidDebugBridge)inputElement).getDevices());
}

return new Object[0];
}

        private Object[] findCompatibleDevices(IDevice[] devices) {
            if (devices == null) {
                return null;
            }

            List<IDevice> compatibleDevices = new ArrayList<IDevice>(devices.length);
            for (IDevice device : devices) {
                AndroidVersion deviceVersion = Sdk.getDeviceVersion(device);
                if (deviceVersion == null || deviceVersion.canRun(mMinApiVersion)) {
                    compatibleDevices.add(device);
                }
            }

            return compatibleDevices.toArray();
        }

@Override
public void dispose() {
// pass
//Synthetic comment -- @@ -113,7 +133,6 @@
}
}

/**
* A Label Provider for the {@link TableViewer} in {@link DeviceChooserDialog}.
* It provides labels and images for {@link IDevice} objects.
//Synthetic comment -- @@ -136,7 +155,7 @@
if (deviceVersion == null) {
return mWarningImage;
} else {
                                if (!deviceVersion.canRun(mMinApiVersion)) {
return mNoMatchImage;
}

//Synthetic comment -- @@ -152,6 +171,17 @@
if (info == null) {
return mWarningImage;
}
                            IAndroidTarget avdTarget = info.getTarget();
                            if (avdTarget == null) {
                                return mWarningImage;
                            }

                            // for platform targets, we only need to check the min api level
                            if (mProjectTarget.isPlatform()
                                    && avdTarget.getVersion().canRun(mMinApiVersion))
                                return mMatchImage;

                            // for add on targets, check if required libraries are available
return mProjectTarget.canRunOn(info.getTarget()) ?
mMatchImage : mNoMatchImage;
}
//Synthetic comment -- @@ -259,12 +289,13 @@
}

public DeviceChooserDialog(Shell parent, DeviceChooserResponse response, String packageName,
            IAndroidTarget projectTarget, AndroidVersion minApiVersion) {
super(parent);

mResponse = response;
mPackageName = packageName;
mProjectTarget = projectTarget;
        mMinApiVersion = minApiVersion;
mSdk = Sdk.getCurrent();

AndroidDebugBridge.addDeviceChangeListener(this);
//Synthetic comment -- @@ -340,9 +371,16 @@
Composite top = new Composite(parent, SWT.NONE);
top.setLayout(new GridLayout(1, true));

        String msg;
        if (mProjectTarget.isPlatform()) {
            msg = String.format("Select a device with min API level %s.",
                    mMinApiVersion.getApiString());
        } else {
            msg = String.format("Select a device compatible with target %s.",
                    mProjectTarget.getFullName());
        }
Label label = new Label(top, SWT.NONE);
        label.setText(msg);

mDeviceRadioButton = new Button(top, SWT.RADIO);
mDeviceRadioButton.setText("Choose a running Android device");
//Synthetic comment -- @@ -736,12 +774,23 @@

@Override
public boolean accept(AvdInfo avd) {
            IAndroidTarget avdTarget = avd.getTarget();

if (mDevices != null) {
for (IDevice d : mDevices) {
                    if (avd.getName().equals(d.getAvdName())) {
return false;
}

                    if (avdTarget == null) {
                        return true;
                    }

                    if (mProjectTarget.isPlatform()) {
                        return avdTarget.getVersion().canRun(mMinApiVersion);
                    }

                    return mProjectTarget.canRunOn(avd.getTarget());
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchDelegate.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchDelegate.java
//Synthetic comment -- index 1c463ab..54225ea 100644

//Synthetic comment -- @@ -17,10 +17,10 @@
package com.android.ide.eclipse.ndk.internal.launch;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IDevice.DeviceUnixSocketNamespace;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.InstallException;
import com.android.ddmlib.ShellCommandUnresponsiveException;
//Synthetic comment -- @@ -119,6 +119,8 @@
}

final ManifestData manifestData = AndroidManifestHelper.parseForData(project);
        final AndroidVersion minSdkVersion = new AndroidVersion(manifestData.getMinSdkVersion(),
                null);

// Get the activity name to launch
String activityName = getActivityToLaunch(
//Synthetic comment -- @@ -159,7 +161,7 @@
AdtPlugin.getDisplay().getActiveShell(),
response,
manifestData.getPackage(),
                            projectTarget, minSdkVersion);
if (dialog.open() == Dialog.OK) {
DeviceChoiceCache.put(configName, response);
continueLaunch[0] = true;







