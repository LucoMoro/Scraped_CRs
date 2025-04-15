/*Remove the LED pulse when adding notifications

When creating notifications including a light pattern
there will come a strange/unexpected pulse before the
actual lights notification starts. This is mostly seen
in cases where the display is on (the user is active)
and receiving a notification.

The description of how to create Status Notifications
does not either describe this behavior.

Change-Id:Ia109f359a286b33f8dd4d8f72a2a2db403d05bcd*/




//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index f6d3b608..9cd5bef 100755

//Synthetic comment -- @@ -116,7 +116,6 @@
private WorkerHandler mHandler;
private StatusBarManagerService mStatusBar;
private LightsService.Light mNotificationLight;

private int mDefaultNotificationColor;
private int mDefaultNotificationLedOn;
//Synthetic comment -- @@ -591,7 +590,6 @@
statusBar.setNotificationCallbacks(mNotificationCallbacks);

mNotificationLight = lights.getLight(LightsService.LIGHT_ID_NOTIFICATIONS);

Resources resources = mContext.getResources();
mDefaultNotificationColor = resources.getColor(
//Synthetic comment -- @@ -988,9 +986,6 @@
long identity = Binder.clearCallingIdentity();
try {
r.statusBarKey = mStatusBar.addNotification(n);
}
finally {
Binder.restoreCallingIdentity(identity);







