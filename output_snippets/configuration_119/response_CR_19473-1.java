//<Beginning of snippet n. 0>


* @param apk the resource to the apk to launch.
* @param packageName the Android package name of the app
* @param debugPackageName the Android package name to debug
* @param debuggable the debuggable value of the app, or null if not set.
* @param requiredApiVersionNumber the api version required by the app, or null if none.
* @param launchAction the action to perform after app sync
* @param config the launch configuration
}
}


// now checks that the device/app can be debugged (if needed)
if (device.isEmulator() == false && launchInfo.isDebugMode()) {
String debuggableDevice = device.getProperty(IDevice.PROP_DEBUGGABLE);
if (debuggableDevice != null && debuggableDevice.equals("0")) { //$NON-NLS-1$
// the device is "secure" and requires apps to declare themselves as debuggable!
                        if (launchInfo.getDebuggable() == null) {
                            String message1 = String.format(
                                    "Device '%1$s' requires that applications explicitly declare themselves as debuggable in their manifest.",
                                    device.getSerialNumber());
                            String message2 = String.format("Application '%1$s' does not have the attribute 'debuggable' set to FALSE in its manifest and cannot be debugged.",
                                    launchInfo.getPackageName());
                            AdtPlugin.printErrorToConsole(launchInfo.getProject(), message1, message2);

                            // because am -D does not check for ro.debuggable and the
                            // 'debuggable' attribute, it is important we do not use the -D option
                            // in this case or the app will wait for a debugger forever and never
                            // really launch.
                            launchInfo.setDebugMode(false);
                        } else if (Boolean.FALSE.equals(launchInfo.getDebuggable())) {
                            String message = String.format("Application '%1$s' has its 'debuggable' attribute set to FALSE and cannot be debugged.",
                                    launchInfo.getPackageName());
                            AdtPlugin.printErrorToConsole(launchInfo.getProject(), message);
                        }

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


* @param debugPackageName the package name of the Android app to debug
* @param launchAction action to perform after app install
* @param pack IFile to the package (.apk) file
* @param debuggable debuggable attribute of the app's manifest file.
* @param requiredApiVersionNumber required SDK version by the app. null means no requirements.
* @param launch the launch object
* @param monitor progress monitor for launch
*/

/**
     * @return true if Android app is marked as debuggable in its manifest
*/
public Boolean getDebuggable() {
return mDebuggable;

//<End of snippet n. 1>