/*ADT: Fix issue with launch checking manifest debuggable attribute.

The builds done by the incremental builders now always insert
debuggable=true (unless there's another value already set), so
the check for the attribute at lauch should only test if the
attribute is set to false manually.

Change-Id:Ia0949af16e1650352eb7d52bde3a93bb180b8be2*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 575502a..6ee74ed 100644

//Synthetic comment -- @@ -280,7 +280,7 @@
* @param apk the resource to the apk to launch.
* @param packageName the Android package name of the app
* @param debugPackageName the Android package name to debug
     * @param debuggable the debuggable value of the app, or null if not set.
* @param requiredApiVersionNumber the api version required by the app, or null if none.
* @param launchAction the action to perform after app sync
* @param config the launch configuration
//Synthetic comment -- @@ -769,26 +769,16 @@
}
}


// now checks that the device/app can be debugged (if needed)
if (device.isEmulator() == false && launchInfo.isDebugMode()) {
String debuggableDevice = device.getProperty(IDevice.PROP_DEBUGGABLE);
if (debuggableDevice != null && debuggableDevice.equals("0")) { //$NON-NLS-1$
// the device is "secure" and requires apps to declare themselves as debuggable!
                    if (launchInfo.getDebuggable() == null) {
                        String message1 = String.format(
                                "Device '%1$s' requires that applications explicitely declare themselves as debuggable in their manifest.",
                                device.getSerialNumber());
                        String message2 = String.format("Application '%1$s' does not have the attribute 'debuggable' set to TRUE in its manifest and cannot be debugged.",
                                launchInfo.getPackageName());
                        AdtPlugin.printErrorToConsole(launchInfo.getProject(), message1, message2);

                        // because am -D does not check for ro.debuggable and the
                        // 'debuggable' attribute, it is important we do not use the -D option
                        // in this case or the app will wait for a debugger forever and never
                        // really launch.
                        launchInfo.setDebugMode(false);
                    } else if (launchInfo.getDebuggable() == Boolean.FALSE) {
String message = String.format("Application '%1$s' has its 'debuggable' attribute set to FALSE and cannot be debugged.",
launchInfo.getPackageName());
AdtPlugin.printErrorToConsole(launchInfo.getProject(), message);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DelayedLaunchInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DelayedLaunchInfo.java
//Synthetic comment -- index 01c94fe..b0a6dda 100644

//Synthetic comment -- @@ -84,7 +84,7 @@
* @param debugPackageName the package name of the Andriod app to debug
* @param launchAction action to perform after app install
* @param pack IFile to the package (.apk) file
     * @param debuggable debuggable attribute of the app's manifest file.
* @param requiredApiVersionNumber required SDK version by the app. null means no requirements.
* @param launch the launch object
* @param monitor progress monitor for launch
//Synthetic comment -- @@ -150,7 +150,9 @@
}

/**
     * @return true if Android app is marked as debuggable in its manifest
*/
public Boolean getDebuggable() {
return mDebuggable;







