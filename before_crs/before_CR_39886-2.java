/*WIP: Use minSdk for device/avd compatibility checks

Change-Id:Iba31a45572dabe3895c4e51707ed515d407caae0*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 1f004f5..b505287 100644

//Synthetic comment -- @@ -324,8 +324,17 @@
config.mTargetMode = TargetMode.MANUAL;
}

        // get the project target
        IAndroidTarget projectTarget = currentSdk.getTarget(project);

// FIXME: check errors on missing sdk, AVD manager, or project target.

//Synthetic comment -- @@ -370,17 +379,22 @@
AvdInfo preferredAvd = null;
if (config.mAvdName != null) {
preferredAvd = avdManager.getAvd(config.mAvdName, true /*validAvdOnly*/);
                if (projectTarget.canRunOn(preferredAvd.getTarget()) == false) {
preferredAvd = null;

AdtPlugin.printErrorToConsole(project, String.format(
                            "Preferred AVD '%1$s' is not compatible with the project target '%2$s'. Looking for a compatible AVD...",
                            config.mAvdName, projectTarget.getName()));
}
}

if (preferredAvd != null) {
                // look for a matching device

for (IDevice d : devices) {
String deviceAvd = d.getAvdName();
//Synthetic comment -- @@ -416,38 +430,31 @@
// a device which target is the same as the project's target) and use it as the
// new default.

            int reqApiLevel = 0;
            try {
                reqApiLevel = Integer.parseInt(requiredApiVersionNumber);

                if (reqApiLevel > 0 && reqApiLevel < projectTarget.getVersion().getApiLevel()) {
                    int maxDist = projectTarget.getVersion().getApiLevel() - reqApiLevel;
                    IAndroidTarget candidate = null;

                    for (IAndroidTarget target : currentSdk.getTargets()) {
                        if (target.canRunOn(projectTarget)) {
                            int currDist = target.getVersion().getApiLevel() - reqApiLevel;
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
            } catch (NumberFormatException e) {
                // pass
}

HashMap<IDevice, AvdInfo> compatibleRunningAvds = new HashMap<IDevice, AvdInfo>();
//Synthetic comment -- @@ -459,17 +466,17 @@
String deviceAvd = d.getAvdName();
if (deviceAvd != null) { // physical devices return null.
AvdInfo info = avdManager.getAvd(deviceAvd, true /*validAvdOnly*/);
                    if (info != null && projectTarget.canRunOn(info.getTarget())) {
compatibleRunningAvds.put(d, info);
}
} else {
                    if (projectTarget.isPlatform()) { // means this can run on any device as long
// as api level is high enough
AndroidVersion deviceVersion = Sdk.getDeviceVersion(d);
// the deviceVersion may be null if it wasn't yet queried (device just
// plugged in or emulator just booting up.
if (deviceVersion != null &&
                                deviceVersion.canRun(projectTarget.getVersion())) {
// device is compatible with project
compatibleRunningAvds.put(d, null);
continue;
//Synthetic comment -- @@ -491,7 +498,7 @@

// we are going to take the closest AVD. ie a compatible AVD that has the API level
// closest to the project target.
                AvdInfo defaultAvd = findMatchingAvd(avdManager, projectTarget);

if (defaultAvd != null) {
response.setAvdToLaunch(defaultAvd);
//Synthetic comment -- @@ -505,7 +512,7 @@
} else {
AdtPlugin.printToConsole(project, String.format(
"Failed to find an AVD compatible with target '%1$s'.",
                            projectTarget.getName()));

final Display display = AdtPlugin.getDisplay();
final boolean[] searchAgain = new boolean[] { false };
//Synthetic comment -- @@ -524,12 +531,12 @@
});
if (searchAgain[0]) {
// attempt to reload the AVDs and find one compatible.
                        defaultAvd = findMatchingAvd(avdManager, projectTarget);

if (defaultAvd == null) {
AdtPlugin.printErrorToConsole(project, String.format(
"Still no compatible AVDs with target '%1$s': Aborting launch.",
                                    projectTarget.getName()));
stopLaunch(launchInfo);
} else {
response.setAvdToLaunch(defaultAvd);
//Synthetic comment -- @@ -578,7 +585,7 @@
boolean includeDevices = config.mTargetMode != TargetMode.ALL_EMULATORS;
boolean includeAvds = config.mTargetMode != TargetMode.ALL_DEVICES;
Collection<IDevice> compatibleDevices = findCompatibleDevices(devices,
                    requiredApiVersionNumber, includeDevices, includeAvds);
if (compatibleDevices.size() == 0) {
AdtPlugin.printErrorToConsole(project,
"No active compatible AVD's or devices found. "
//Synthetic comment -- @@ -591,7 +598,7 @@
}

// bring up the device chooser.
        final IAndroidTarget desiredProjectTarget = projectTarget;
final AtomicBoolean continueLaunch = new AtomicBoolean(false);
AdtPlugin.getDisplay().syncExec(new Runnable() {
@Override
//Synthetic comment -- @@ -634,22 +641,14 @@
/**
* Returns devices that can run a app of provided API level.
* @param devices list of devices to filter from
     * @param requiredApiVersionNumber minimum required API level that should be supported
* @param includeDevices include physical devices in the filtered list
* @param includeAvds include emulators in the filtered list
* @return set of compatible devices, may be an empty set
*/
private Collection<IDevice> findCompatibleDevices(IDevice[] devices,
            String requiredApiVersionNumber, boolean includeDevices, boolean includeAvds) {
Set<IDevice> compatibleDevices = new HashSet<IDevice>(devices.length);
        int minApi;
        try {
            minApi = Integer.parseInt(requiredApiVersionNumber);
        } catch (NumberFormatException e) {
            minApi = 1;
        }
        AndroidVersion requiredVersion = new AndroidVersion(minApi, null);

AvdManager avdManager = Sdk.getCurrent().getAvdManager();
for (IDevice d: devices) {
boolean isEmulator = d.isEmulator();
//Synthetic comment -- @@ -662,7 +661,7 @@

AvdInfo avdInfo = avdManager.getAvd(d.getAvdName(), true);
if (avdInfo != null && avdInfo.getTarget() != null) {
                    canRun = avdInfo.getTarget().getVersion().canRun(requiredVersion);
}
} else {
if (!includeDevices) {
//Synthetic comment -- @@ -671,7 +670,7 @@

AndroidVersion deviceVersion = Sdk.getDeviceVersion(d);
if (deviceVersion != null) {
                    canRun = deviceVersion.canRun(requiredVersion);
}
}








