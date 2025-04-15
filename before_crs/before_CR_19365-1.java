/*frameworks/base: fixes the null check

This condition was never met. If it was met it would be NullPointerException.
Fixed the condition so that the listener will be unregistered properly.

Change-Id:I21e7eb0fc579999c6b3d3849a1b9766bb0220e85*/
//Synthetic comment -- diff --git a/services/java/com/android/server/status/UsbStorageActivity.java b/services/java/com/android/server/status/UsbStorageActivity.java
//Synthetic comment -- index e8631c53..4da5385 100644

//Synthetic comment -- @@ -152,7 +152,7 @@
super.onPause();

unregisterReceiver(mBatteryReceiver);
        if (mStorageManager == null && mStorageListener != null) {
mStorageManager.unregisterListener(mStorageListener);
}
}







