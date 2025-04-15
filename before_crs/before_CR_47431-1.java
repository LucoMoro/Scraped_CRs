/*Notification replacement sound fix

When a notifiaction was replaced in the NotificationManagerService's
notification list(due to having the same tag, package and id), and
that was the notification that triggered the playing sound, the
sound did not stop when the notification was dismissed. This fix
updates the variable that holds the current sound's notification when
that notification is replaced

Change-Id:I5225f5c200a29de663f987b55af88d7f7b2be941*/
//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index f3a38f0..db30f27 100644

//Synthetic comment -- @@ -982,6 +982,11 @@
notification.flags |=
old.notification.flags&Notification.FLAG_FOREGROUND_SERVICE;
}
}

// Ensure if this is a foreground service that the proper additional







