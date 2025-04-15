/*Fix auto launch config to respect minSdkVersion

When launch config is in automatic mode with no
preferred AVD and minSdkVersion is smaller than the
project targert's API level, try to find an AVD
that's as close to the minSdkVersion as possible.

SDK Bughttp://b.android.com/7897Change-Id:Ied1f2db8f61c27430b466b2b7afbb5494fd0ffed*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 4c8621e..2f959ff 100644

//Synthetic comment -- @@ -313,7 +313,7 @@
}

// get the project target
        IAndroidTarget projectTarget = currentSdk.getTarget(project);

// FIXME: check errors on missing sdk, AVD manager, or project target.

//Synthetic comment -- @@ -358,6 +358,7 @@

if (preferredAvd != null) {
// look for a matching device

for (IDevice d : devices) {
String deviceAvd = d.getAvdName();
if (deviceAvd != null && deviceAvd.equals(config.mAvdName)) {
//Synthetic comment -- @@ -385,6 +386,47 @@
}

// no (valid) preferred AVD? look for one.

            // If the API level requested in the manifest is lower than the current project
            // target, when we will iterate devices/avds later ideally we will want to find
            // a device/avd which target is as close to the manifest as possible (instead of
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
boolean hasDevice = false; // if there's 1+ device running, we may force manual mode,
// as we cannot always detect proper compatibility with
//Synthetic comment -- @@ -507,6 +549,7 @@
}

// bring up the device chooser.
        final IAndroidTarget desiredProjectTarget = projectTarget;
AdtPlugin.getDisplay().asyncExec(new Runnable() {
public void run() {
try {
//Synthetic comment -- @@ -514,7 +557,7 @@
// or the AVD to launch.
DeviceChooserDialog dialog = new DeviceChooserDialog(
AdtPlugin.getDisplay().getActiveShell(),
                            response, launchInfo.getPackageName(), desiredProjectTarget);
if (dialog.open() == Dialog.OK) {
AndroidLaunchController.this.continueLaunch(response, project, launch,
launchInfo, config);







