
//<Beginning of snippet n. 0>


* @param apk the resource to the apk to launch.
* @param packageName the Android package name of the app
* @param debugPackageName the Android package name to debug
     * @param debuggable the debuggable value of the app's manifest, or null if not set.
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
                    // launchInfo.getDebuggable() will return null if the manifest doesn't declare
                    // anything. In this case this is fine since the build system does insert
                    // debuggable=true. The only case to look for is if false is manually set
                    // in the manifest.
                    if (launchInfo.getDebuggable() == Boolean.FALSE) {
String message = String.format("Application '%1$s' has its 'debuggable' attribute set to FALSE and cannot be debugged.",
launchInfo.getPackageName());
AdtPlugin.printErrorToConsole(launchInfo.getProject(), message);

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


* @param debugPackageName the package name of the Andriod app to debug
* @param launchAction action to perform after app install
* @param pack IFile to the package (.apk) file
     * @param debuggable the debuggable value of the app's manifest, or null if not set.
* @param requiredApiVersionNumber required SDK version by the app. null means no requirements.
* @param launch the launch object
* @param monitor progress monitor for launch
}

/**
     * Returns the value of the manifest debuggable attribute. If the attribute was not set,
     * then the method returns null.
     * @return the manifest debuggable attribute.
*/
public Boolean getDebuggable() {
return mDebuggable;

//<End of snippet n. 1>








