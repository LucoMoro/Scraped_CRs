/*WIP: Use minSdk for device/avd compatibility checks

Change-Id:Iba31a45572dabe3895c4e51707ed515d407caae0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 1f004f5..b505287 100644

//Synthetic comment -- @@ -324,8 +324,17 @@
config.mTargetMode = TargetMode.MANUAL;
}

        // get the sdk against which the project is built
        IAndroidTarget projectBuildTarget = currentSdk.getTarget(project);

        // get the min required android version
        int minApi;
        try {
            minApi = Integer.parseInt(requiredApiVersionNumber);
        } catch (NumberFormatException e) {
            minApi = 1;
        }
        AndroidVersion minRequiredVersion = new AndroidVersion(minApi, null);

// FIXME: check errors on missing sdk, AVD manager, or project target.

//Synthetic comment -- @@ -370,17 +379,22 @@
AvdInfo preferredAvd = null;
if (config.mAvdName != null) {
preferredAvd = avdManager.getAvd(config.mAvdName, true /*validAvdOnly*/);
                IAndroidTarget preferredAvdTarget = preferredAvd.getTarget();
                if (preferredAvdTarget != null
                        && !preferredAvdTarget.getVersion().canRun(minRequiredVersion)) {
preferredAvd = null;

AdtPlugin.printErrorToConsole(project, String.format(
                            "Preferred AVD '%1$s' (API Level: %2$d) cannot run application with minApi %3$d. Looking for a compatible AVD...",
                            config.mAvdName,
                            preferredAvdTarget.getVersion().getApiLevel(),
                            minRequiredVersion.getApiLevel()));
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

            if (minApi < projectBuildTarget.getVersion().getApiLevel()) {
                int maxDist = projectBuildTarget.getVersion().getApiLevel() - minApi;
                IAndroidTarget candidate = null;

                for (IAndroidTarget target : currentSdk.getTargets()) {
                    if (target.canRunOn(projectBuildTarget)) {
                        int currDist = target.getVersion().getApiLevel() - minApi;
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
                    projectBuildTarget = candidate;
                }
}

HashMap<IDevice, AvdInfo> compatibleRunningAvds = new HashMap<IDevice, AvdInfo>();
//Synthetic comment -- @@ -459,17 +466,17 @@
String deviceAvd = d.getAvdName();
if (deviceAvd != null) { // physical devices return null.
AvdInfo info = avdManager.getAvd(deviceAvd, true /*validAvdOnly*/);
                    if (info != null && projectBuildTarget.canRunOn(info.getTarget())) {
compatibleRunningAvds.put(d, info);
}
} else {
                    if (projectBuildTarget.isPlatform()) { // means this can run on any device as long
// as api level is high enough
AndroidVersion deviceVersion = Sdk.getDeviceVersion(d);
// the deviceVersion may be null if it wasn't yet queried (device just
// plugged in or emulator just booting up.
if (deviceVersion != null &&
                                deviceVersion.canRun(projectBuildTarget.getVersion())) {
// device is compatible with project
compatibleRunningAvds.put(d, null);
continue;
//Synthetic comment -- @@ -491,7 +498,7 @@

// we are going to take the closest AVD. ie a compatible AVD that has the API level
// closest to the project target.
                AvdInfo defaultAvd = findMatchingAvd(avdManager, projectBuildTarget);

if (defaultAvd != null) {
response.setAvdToLaunch(defaultAvd);
//Synthetic comment -- @@ -505,7 +512,7 @@
} else {
AdtPlugin.printToConsole(project, String.format(
"Failed to find an AVD compatible with target '%1$s'.",
                            projectBuildTarget.getName()));

final Display display = AdtPlugin.getDisplay();
final boolean[] searchAgain = new boolean[] { false };
//Synthetic comment -- @@ -524,12 +531,12 @@
});
if (searchAgain[0]) {
// attempt to reload the AVDs and find one compatible.
                        defaultAvd = findMatchingAvd(avdManager, projectBuildTarget);

if (defaultAvd == null) {
AdtPlugin.printErrorToConsole(project, String.format(
"Still no compatible AVDs with target '%1$s': Aborting launch.",
                                    projectBuildTarget.getName()));
stopLaunch(launchInfo);
} else {
response.setAvdToLaunch(defaultAvd);
//Synthetic comment -- @@ -578,7 +585,7 @@
boolean includeDevices = config.mTargetMode != TargetMode.ALL_EMULATORS;
boolean includeAvds = config.mTargetMode != TargetMode.ALL_DEVICES;
Collection<IDevice> compatibleDevices = findCompatibleDevices(devices,
                    minRequiredVersion, includeDevices, includeAvds);
if (compatibleDevices.size() == 0) {
AdtPlugin.printErrorToConsole(project,
"No active compatible AVD's or devices found. "
//Synthetic comment -- @@ -591,7 +598,7 @@
}

// bring up the device chooser.
        final IAndroidTarget desiredProjectTarget = projectBuildTarget;
final AtomicBoolean continueLaunch = new AtomicBoolean(false);
AdtPlugin.getDisplay().syncExec(new Runnable() {
@Override
//Synthetic comment -- @@ -634,22 +641,14 @@
/**
* Returns devices that can run a app of provided API level.
* @param devices list of devices to filter from
     * @param requiredApiVersion minimum required API that should be supported
* @param includeDevices include physical devices in the filtered list
* @param includeAvds include emulators in the filtered list
* @return set of compatible devices, may be an empty set
*/
private Collection<IDevice> findCompatibleDevices(IDevice[] devices,
            AndroidVersion requiredApiVersion, boolean includeDevices, boolean includeAvds) {
Set<IDevice> compatibleDevices = new HashSet<IDevice>(devices.length);
AvdManager avdManager = Sdk.getCurrent().getAvdManager();
for (IDevice d: devices) {
boolean isEmulator = d.isEmulator();
//Synthetic comment -- @@ -662,7 +661,7 @@

AvdInfo avdInfo = avdManager.getAvd(d.getAvdName(), true);
if (avdInfo != null && avdInfo.getTarget() != null) {
                    canRun = avdInfo.getTarget().getVersion().canRun(requiredApiVersion);
}
} else {
if (!includeDevices) {
//Synthetic comment -- @@ -671,7 +670,7 @@

AndroidVersion deviceVersion = Sdk.getDeviceVersion(d);
if (deviceVersion != null) {
                    canRun = deviceVersion.canRun(requiredApiVersion);
}
}








