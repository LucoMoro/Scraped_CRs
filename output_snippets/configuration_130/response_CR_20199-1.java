//<Beginning of snippet n. 0>
if (wm != null && power != null && pm != null && statusBar != null) {
    mContentResolver.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.ADB_ENABLED),
    false, new AdbSettingsObserver());

    final boolean safeMode = wm.detectSafeMode();
    if (safeMode) {
        try {
            ActivityManagerNative.getDefault().enterSafeMode();
        } catch (RemoteException e) {
            Log.e("SafeMode", "RemoteException during enter safe mode", e);
        }

        try {
            statusBar.systemReady();
        } catch (NullPointerException e) {
            Log.e("SafeMode", "statusBar is null", e);
        }

        try {
            wm.systemReady();
        } catch (NullPointerException e) {
            Log.e("SafeMode", "wm is null", e);
        }

        try {
            power.systemReady();
        } catch (NullPointerException e) {
            Log.e("SafeMode", "power is null", e);
        }

        try {
            pm.systemReady();
        } catch (RemoteException e) {
            Log.e("SafeMode", "RemoteException during pm.systemReady()", e);
        } catch (NullPointerException e) {
            Log.e("SafeMode", "pm is null", e);
        }
    }
}
//<End of snippet n. 0>