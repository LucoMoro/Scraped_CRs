
//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

mContentResolver.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.ADB_ENABLED),
false, new AdbSettingsObserver());

        // WBT fix - CID363 prevent null pointer reference [START]
// Before things start rolling, be sure we have decided whether
        // we are in safe mode.        
        final boolean safeMode = (wm != null) ? wm.detectSafeMode() : false;
        // WBT fix - CID363 prevent null pointer reference [END]
if (safeMode) {
try {
ActivityManagerNative.getDefault().enterSafeMode();
if (statusBar != null) {
statusBar.systemReady();
}

        // WBT fix - CID363 prevent null pointer reference [START]
        if (wm != null) {
            wm.systemReady();
        }

        if (power != null) {
            power.systemReady();
        }
        // WBT fix - CID363 prevent null pointer reference [END]
		
try {
pm.systemReady();
} catch (RemoteException e) {

//<End of snippet n. 0>








