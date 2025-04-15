/*Corrected StatusBarNotificationSample

Change-Id:I892e84198cfe288e673b81e8ffc331525fee09b8*/
//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/app/StatusBarNotifications.java b/samples/ApiDemos/src/com/example/android/apis/app/StatusBarNotifications.java
//Synthetic comment -- index 97f6199..9b911cc 100644

//Synthetic comment -- @@ -174,7 +174,7 @@

// Send the notification.
// We use a layout id because it is a unique number.  We use it later to cancel.
        mNotificationManager.notify(R.layout.status_bar_notifications, notification);
}

private void setMoodView(int moodId, int textId) {
//Synthetic comment -- @@ -202,7 +202,7 @@

// we use a string id because is a unique number.  we use it later to cancel the
// notification
        mNotificationManager.notify(R.layout.status_bar_notifications, notif);
}

private void setDefault(int defaults) {
//Synthetic comment -- @@ -231,8 +231,8 @@
notification.defaults = defaults;

mNotificationManager.notify(
                   R.layout.status_bar_notifications, // we use a string id because it is a unique
                                                      // number.  we use it later to cancel the
                   notification);                     // notification
}    
}







