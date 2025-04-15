/*OPP: Notification position is changing

If several transfers is ongoing the Bluetooth transfer notification
is changing position. The reason for this is that the when field is
not set in the notification. Solved by setting when.

Change-Id:If999fae6dd2d6b5de38c3bfeba786ad2df9da48f*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppNotification.java b/src/com/android/bluetooth/opp/BluetoothOppNotification.java
//Synthetic comment -- index e326ca3..7489ffc 100644

//Synthetic comment -- @@ -110,6 +110,8 @@

int totalTotal = 0; // total bytes for current transfer

String description; // the text above progress bar
}

//Synthetic comment -- @@ -247,6 +249,7 @@
// Batch sending case
} else {
NotificationItem item = new NotificationItem();
item.id = id;
item.direction = dir;
if (item.direction == BluetoothShare.DIRECTION_OUTBOUND) {
//Synthetic comment -- @@ -284,6 +287,7 @@

// Build the notification object
Notification n = new Notification();
if (item.direction == BluetoothShare.DIRECTION_OUTBOUND) {
n.icon = android.R.drawable.stat_sys_upload;
expandedView.setImageViewResource(R.id.appIcon, android.R.drawable.stat_sys_upload);







