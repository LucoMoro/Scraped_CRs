/*Prevent null pointer reference

Change-Id:Ie2ad1e433f32de481ce556df6f3c5d0df1403f4f*/
//Synthetic comment -- diff --git a/services/java/com/android/server/SystemServer.java b/services/java/com/android/server/SystemServer.java
old mode 100644
new mode 100755
//Synthetic comment -- index 9d5d035..3e155fc

//Synthetic comment -- @@ -426,9 +426,11 @@
mContentResolver.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.ADB_ENABLED),
false, new AdbSettingsObserver());

// Before things start rolling, be sure we have decided whether
        // we are in safe mode.
        final boolean safeMode = wm.detectSafeMode();
if (safeMode) {
try {
ActivityManagerNative.getDefault().enterSafeMode();
//Synthetic comment -- @@ -456,8 +458,17 @@
if (statusBar != null) {
statusBar.systemReady();
}
        wm.systemReady();
        power.systemReady();
try {
pm.systemReady();
} catch (RemoteException e) {







