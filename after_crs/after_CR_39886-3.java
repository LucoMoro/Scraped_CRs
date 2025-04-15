/*WIP: Use minSdk for device/avd compatibility checks

Change-Id:Iba31a45572dabe3895c4e51707ed515d407caae0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 1f004f5..26a7873 100644

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
        AndroidVersion minRequiredVersion = new AndroidVersion(minApiLevel, null);

// FIXME: check errors on missing sdk, AVD manager, or project target.

// device chooser response object.
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
                    minRequiredVersion, includeDevices, includeAvds);
if (compatibleDevices.size() == 0) {
AdtPlugin.printErrorToConsole(project,
"No active compatible AVD's or devices found. "
//Synthetic comment -- @@ -634,22 +641,14 @@
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







