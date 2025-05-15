//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

mContentResolver.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.ADB_ENABLED),
false, new AdbSettingsObserver());

// Before things start rolling, be sure we have decided whether
// we are in safe mode.
final boolean safeMode = wm != null && wm.detectSafeMode();
if (safeMode) {
    try {
        ActivityManagerNative.getDefault().enterSafeMode();
        if (statusBar != null) {
            statusBar.systemReady();
        }
        if (wm != null) {
            wm.systemReady();
        }
        if (power != null) {
            power.systemReady();
        }
        try {
            if (pm != null) {
                pm.systemReady();
            }
        } catch (RemoteException e) {
            // Log exception or take necessary action
        }
    } catch (Exception e) {
        // Log exception for debugging
    }
}

//<End of snippet n. 0>