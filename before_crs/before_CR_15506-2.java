/*Notification sounds not enabled at first boot

All notifications sounds were disabled until the system property
Settings.Secure.DEVICE_PROVISIONED is set to 1. This is done by
the default Setup Wizard (Google application) at first boot. At
second boot the notications could be heard.
This is changed with adding a registration mechanism to be able
to receive updates when DEVICE_PROVISIONED is changed. Then
notification sounds can also be heard after first boot.

Change-Id:I5c985887a2ae01aeed72eaebad80e30ebf8e278f*/
//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index 73d17ea..eea63d5 100755

//Synthetic comment -- @@ -405,6 +405,36 @@
}
}

NotificationManagerService(Context context, StatusBarService statusBar,
LightsService lights)
{
//Synthetic comment -- @@ -439,6 +469,10 @@
if (0 == Settings.Secure.getInt(mContext.getContentResolver(),
Settings.Secure.DEVICE_PROVISIONED, 0)) {
mDisabledNotifications = StatusBarManager.DISABLE_NOTIFICATION_ALERTS;
}

// register for battery changed notifications







