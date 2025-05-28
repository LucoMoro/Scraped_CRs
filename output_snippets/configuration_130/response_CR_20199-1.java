//<Beginning of snippet n. 0>
mContentResolver.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.ADB_ENABLED),
false, new AdbSettingsObserver());

final boolean safeMode = wm != null && wm.detectSafeMode();
if (safeMode) {
    try {
        ActivityManagerNative.getDefault().enterSafeMode();
        if (statusBar != null) {
            statusBar.systemReady();
        }
        if (wm != null) {
            try {
                wm.systemReady();
            } catch (RemoteException e) {
                Log.e("MyApp", "Error during wm.systemReady()", e);
            }
        }
        if (power != null) {
            try {
                power.systemReady();
            } catch (RemoteException e) {
                Log.e("MyApp", "Error during power.systemReady()", e);
            }
        }
        if (pm != null) {
            try {
                pm.systemReady();
            } catch (RemoteException e) {
                Log.e("MyApp", "Error during pm.systemReady()", e);
            }
        }
    } catch (Exception e) {
        Log.e("MyApp", "Error during system initialization", e);
    }
}
//<End of snippet n. 0>