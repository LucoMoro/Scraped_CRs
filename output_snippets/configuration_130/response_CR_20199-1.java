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

        if (statusBar != null) {
            statusBar.systemReady();
        } else {
            Log.e("SafeMode", "statusBar is null");
        }

        if (wm != null) {
            wm.systemReady();
        } else {
            Log.e("SafeMode", "wm is null");
        }

        if (power != null) {
            power.systemReady();
        } else {
            Log.e("SafeMode", "power is null");
        }

        if (pm != null) {
            try {
                pm.systemReady();
            } catch (RemoteException e) {
                Log.e("SafeMode", "RemoteException during pm.systemReady()", e);
            }
        } else {
            Log.e("SafeMode", "pm is null");
        }
    }
}
//<End of snippet n. 0>