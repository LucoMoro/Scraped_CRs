/*Support setNumber(int) in NotificationCompat.Builder

The "number" parameter was ignored in NotificationCompatHoneycomb, so it does not get displayed in the notification. This simply calls through to the native Notification.Builder#setNumber(int) method.

Change-Id:Ic867efa6f4b4b79fa64723443e0df2045f262d01Signed-off-by: Scott Kennedy <skennedy27@gmail.com>*/
//Synthetic comment -- diff --git a/v4/honeycomb/android/support/v4/app/NotificationCompatHoneycomb.java b/v4/honeycomb/android/support/v4/app/NotificationCompatHoneycomb.java
//Synthetic comment -- index 1e59a1b..3048e91 100644

//Synthetic comment -- @@ -46,7 +46,8 @@
.setDeleteIntent(n.deleteIntent)
.setFullScreenIntent(fullScreenIntent,
(n.flags & Notification.FLAG_HIGH_PRIORITY) != 0)
                .setLargeIcon(largeIcon);

return b.getNotification();
}







