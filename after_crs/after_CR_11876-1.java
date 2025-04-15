/*Make the ADB connected notification non-persistent.  It's useful, but as
a persistent notification it takes up too much space, especially for
users who are actively developing on their device.*/




//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index aac7124..e9ddb50 100644

//Synthetic comment -- @@ -972,7 +972,6 @@
mAdbNotification = new Notification();
mAdbNotification.icon = com.android.internal.R.drawable.stat_sys_warning;
mAdbNotification.when = 0;
mAdbNotification.tickerText = title;
mAdbNotification.defaults |= Notification.DEFAULT_SOUND;
}







