/*MMC: improve insertion notification on Gingerbread

Change-Id:I413a41f6e571075fadac3dffa343ec55cb804f80Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/usb/StorageNotification.java b/packages/SystemUI/src/com/android/systemui/usb/StorageNotification.java
//Synthetic comment -- index 7dff549..a01ec12 100644

//Synthetic comment -- @@ -175,8 +175,9 @@
/*
* Show safe to unmount media notification, and enable UMS
* notification if connected.
*/
                    if (Environment.isExternalStorageRemovable()) {
setMediaStorageNotification(
com.android.internal.R.string.ext_media_safe_unmount_notification_title,
com.android.internal.R.string.ext_media_safe_unmount_notification_message,







