/*To prevent the reference to null pointer

When the mStorageManager is null, the reference to member method using mStorageManager should not be executed.

Change-Id:If48c751d44c1fef54642dfdade93e22b527f3723*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/usb/UsbStorageActivity.java b/packages/SystemUI/src/com/android/systemui/usb/UsbStorageActivity.java
old mode 100644
new mode 100755
//Synthetic comment -- index 5c52783..0ed6206

//Synthetic comment -- @@ -193,7 +193,7 @@
super.onPause();

unregisterReceiver(mUsbStateReceiver);
        if (mStorageManager == null && mStorageListener != null) {
mStorageManager.unregisterListener(mStorageListener);
}
}







