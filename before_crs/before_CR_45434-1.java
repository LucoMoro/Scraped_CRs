/*NullPointerException in UsbStorageActivity

I have found this bug with the FindBugs tool

Change-Id:I387dd6cbb7281cbc1e8879a75fecb09d55d1a345Signed-off-by: László Dávid <laszlo.david@gmail.com>*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/usb/UsbStorageActivity.java b/packages/SystemUI/src/com/android/systemui/usb/UsbStorageActivity.java
//Synthetic comment -- index e61ef8a..9ae39ad 100644

//Synthetic comment -- @@ -188,7 +188,7 @@
super.onPause();

unregisterReceiver(mUsbStateReceiver);
        if (mStorageManager == null && mStorageListener != null) {
mStorageManager.unregisterListener(mStorageListener);
}
}







