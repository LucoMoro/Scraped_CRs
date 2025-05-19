//<Beginning of snippet n. 0>
if (launchInfo.getDebuggable() != Boolean.FALSE) {
    if (launchInfo.isDebugMode()) {
        // Debugging allowed
    } else {
        // Debugging not allowed, handle if needed
    }
} else {
    String message1 = String.format(
        "Device '%1$s' requires that applications explicitly declare themselves as debuggable in their manifest.",
        device.getSerialNumber());
    String message2 = String.format("Application '%1$s' cannot be debugged because the 'debuggable' attribute is not set to TRUE.",
        launchInfo.getPackageName());
    AdtPlugin.printErrorToConsole(launchInfo.getProject(), message1, message2);
    launchInfo.setDebugMode(false);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public Boolean getDebuggable() {
    return mDebuggable;
//<End of snippet n. 1>