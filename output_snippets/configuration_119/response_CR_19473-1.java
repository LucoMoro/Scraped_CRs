//<Beginning of snippet n. 0>
if (device.isEmulator() == false && launchInfo.isDebugMode()) {
    String debuggableDevice = device.getProperty(IDevice.PROP_DEBUGGABLE);
    if (debuggableDevice != null && debuggableDevice.equals("0")) {
        if (launchInfo.getDebuggable() != null && launchInfo.getDebuggable() == Boolean.FALSE) {
            String message = String.format("Application '%1$s' has its 'debuggable' attribute set to FALSE and cannot be debugged.",
                    launchInfo.getPackageName());
            AdtPlugin.printErrorToConsole(launchInfo.getProject(), message);

            String message1 = String.format(
                    "Device '%1$s' requires that applications explicitly declare themselves as debuggable in their manifest.",
                    device.getSerialNumber());
            String message2 = String.format("Application '%1$s' does not have the attribute 'debuggable' set to TRUE in its manifest and cannot be debugged.",
                    launchInfo.getPackageName());
            AdtPlugin.printErrorToConsole(launchInfo.getProject(), message1, message2);
            launchInfo.setDebugMode(false);
        }
        return; 
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public Boolean getDebuggable() {
    return mDebuggable; // The variable should allow NULL to be a valid state.
}
//<End of snippet n. 1>