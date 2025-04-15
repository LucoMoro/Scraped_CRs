/*Reset sInitialized when calling AndroidDebugBridge.terminate().

This will allow calling init() again after a terminate().

Change-Id:Icdda999624f13485f419ae53bc2139a5ceaa3229*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AndroidDebugBridge.java
//Synthetic comment -- index e965ccd..f697cba 100644

//Synthetic comment -- @@ -200,7 +200,7 @@
/**
* Terminates the ddm library. This must be called upon application termination.
*/
    public static void terminate() {
// kill the monitoring services
if (sThis != null && sThis.mDeviceMonitor != null) {
sThis.mDeviceMonitor.stop();
//Synthetic comment -- @@ -211,6 +211,8 @@
if (monitorThread != null) {
monitorThread.quit();
}
}

/**







