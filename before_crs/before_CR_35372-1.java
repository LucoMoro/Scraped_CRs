/*Launch on same device regardless of AUTO/MANUAL mode.

The device chooser dialog has a checkbox that allows the user
to say that the same device should be used for future launches.
This setting was respected only if that launch mode was AUTOMATIC.
This patch enables launching on the same device in MANUAL launch
mode as well.

Change-Id:I3bab9fd34a64c3891e03bf9adf285be11d4de017*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index d7d87cb..81509ec 100644

//Synthetic comment -- @@ -333,14 +333,14 @@

/*
* Launch logic:
* - Manually Mode
*       Always display a UI that lets a user see the current running emulators/devices.
*       The UI must show which devices are compatibles, and allow launching new emulators
*       with compatible (and not yet running) AVD.
* - Automatic Way
         *     * Last Launched Device/AVD set.
         *           If the last launched device/avd is still present, then simply launch
         *           on the same device/avd.
*     * Preferred AVD set.
*           If Preferred AVD is not running: launch it.
*           Launch the application on the preferred AVD.
//Synthetic comment -- @@ -349,19 +349,16 @@
*           If != 1, display a UI similar to manual mode.
*           If == 1, launch the application on this AVD/device.
*/

if (config.mTargetMode == TargetMode.AUTO) {
            // if we are in automatic target mode, we need to find the current devices
            IDevice[] devices = AndroidDebugBridge.getBridge().getDevices();

            IDevice deviceUsedInLastLaunch = getDeviceUsedForLastLaunch(devices,
                    launch.getLaunchConfiguration().getName());
            if (deviceUsedInLastLaunch != null) {
                response.setDeviceToUse(deviceUsedInLastLaunch);
                continueLaunch(response, project, launch, launchInfo, config);
                return;
            }

// first check if we have a preferred AVD name, and if it actually exists, and is valid
// (ie able to run the project).
// We need to check this in case the AVD was recreated with a different target that is







