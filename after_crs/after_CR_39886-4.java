/*WIP: Use minSdk for device/avd compatibility checks

Change-Id:Iba31a45572dabe3895c4e51707ed515d407caae0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 1f004f5..11f1971 100644

//Synthetic comment -- @@ -324,9 +324,18 @@
config.mTargetMode = TargetMode.MANUAL;
}

        // get the sdk against which the project is built
IAndroidTarget projectTarget = currentSdk.getTarget(project);

        // get the min required android version
        int minApiLevel;
        try {
            minApiLevel = Integer.parseInt(requiredApiVersionNumber);
        } catch (NumberFormatException e) {
            minApiLevel = 1;
        }
        final AndroidVersion minApiVersion = new AndroidVersion(minApiLevel, null);

// FIXME: check errors on missing sdk, AVD manager, or project target.

// device chooser response object.
//Synthetic comment -- @@ -370,17 +379,22 @@
AvdInfo preferredAvd = null;
if (config.mAvdName != null) {
preferredAvd = avdManager.getAvd(config.mAvdName, true /*validAvdOnly*/);
                IAndroidTarget preferredAvdTarget = preferredAvd.getTarget();
                if (preferredAvdTarget != null
                        && !preferredAvdTarget.getVersion().canRun(minApiVersion)) {
preferredAvd = null;

AdtPlugin.printErrorToConsole(project, String.format(
                            "Preferred AVD '%1$s' (API Level: %2$d) cannot run application with minApi %3$d. Looking for a compatible AVD...",
                            config.mAvdName,
                            preferredAvdTarget.getVersion().getApiLevel(),
                            minApiVersion.getApiLevel()));
}
}

if (preferredAvd != null) {
                // We have a preferred avd that can actually run the application.
                // Now see if the AVD is running, and if so use it, otherwise launch it.

for (IDevice d : devices) {
String deviceAvd = d.getAvdName();
//Synthetic comment -- @@ -416,38 +430,31 @@
// a device which target is the same as the project's target) and use it as the
// new default.

            if (minApiLevel < projectTarget.getVersion().getApiLevel()) {
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
//Synthetic comment -- @@ -578,7 +585,7 @@
boolean includeDevices = config.mTargetMode != TargetMode.ALL_EMULATORS;
boolean includeAvds = config.mTargetMode != TargetMode.ALL_DEVICES;
Collection<IDevice> compatibleDevices = findCompatibleDevices(devices,
                    minApiVersion, includeDevices, includeAvds);
if (compatibleDevices.size() == 0) {
AdtPlugin.printErrorToConsole(project,
"No active compatible AVD's or devices found. "
//Synthetic comment -- @@ -601,7 +608,8 @@
// or the AVD to launch.
DeviceChooserDialog dialog = new DeviceChooserDialog(
AdtPlugin.getDisplay().getActiveShell(),
                            response, launchInfo.getPackageName(),
                            desiredProjectTarget, minApiVersion);
if (dialog.open() == Dialog.OK) {
DeviceChoiceCache.put(launch.getLaunchConfiguration().getName(), response);
continueLaunch.set(true);
//Synthetic comment -- @@ -634,22 +642,14 @@
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
//Synthetic comment -- index 781deaf..220c9b8 100644

//Synthetic comment -- @@ -80,6 +80,7 @@
private final DeviceChooserResponse mResponse;
private final String mPackageName;
private final IAndroidTarget mProjectTarget;
    private final AndroidVersion mMinApiVersion;
private final Sdk mSdk;

private Button mDeviceRadioButton;
//Synthetic comment -- @@ -136,7 +137,7 @@
if (deviceVersion == null) {
return mWarningImage;
} else {
                                if (!deviceVersion.canRun(mMinApiVersion)) {
return mNoMatchImage;
}

//Synthetic comment -- @@ -152,8 +153,22 @@
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
                            if (mProjectTarget.canRunOn(info.getTarget())) {
                                return mMatchImage;
                            }

                            return mNoMatchImage;
}
}
}
//Synthetic comment -- @@ -259,12 +274,13 @@
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
//Synthetic comment -- @@ -340,9 +356,16 @@
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
//Synthetic comment -- @@ -736,12 +759,23 @@

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








