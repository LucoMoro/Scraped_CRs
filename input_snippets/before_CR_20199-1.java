
//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

mContentResolver.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.ADB_ENABLED),
false, new AdbSettingsObserver());

// Before things start rolling, be sure we have decided whether
        // we are in safe mode.
        final boolean safeMode = wm.detectSafeMode();
if (safeMode) {
try {
ActivityManagerNative.getDefault().enterSafeMode();
if (statusBar != null) {
statusBar.systemReady();
}
        wm.systemReady();
        power.systemReady();
try {
pm.systemReady();
} catch (RemoteException e) {

//<End of snippet n. 0>








