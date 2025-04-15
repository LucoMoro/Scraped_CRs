/*CB: Cancel notification bar

Fix to remove notification icon from notification bar when
Cell Broadcast app is opened.

Change-Id:Ifcf383ecd4807f0b4dc282163f20570e924e64fa*/




//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastAlertService.java b/src/com/android/cellbroadcastreceiver/CellBroadcastAlertService.java
//Synthetic comment -- index dafb391..35896ef 100644

//Synthetic comment -- @@ -40,10 +40,6 @@
public class CellBroadcastAlertService extends Service {
private static final String TAG = "CellBroadcastAlertService";

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
String action = intent.getAction();
//Synthetic comment -- @@ -216,7 +212,6 @@
// Trigger the list activity to fire up a dialog that shows the received messages
Intent intent = new Intent(context, CellBroadcastListActivity.class);
intent.putExtra(CellBroadcastMessage.SMS_CB_MESSAGE_EXTRA, message);

// This line is needed to make this intent compare differently than the other intents
// created here for other messages. Without this line, the PendingIntent always gets the








//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastListActivity.java b/src/com/android/cellbroadcastreceiver/CellBroadcastListActivity.java
//Synthetic comment -- index eafeec2..410a2fe 100644

//Synthetic comment -- @@ -257,19 +257,17 @@
if (intent == null) {
return;
}
        // Dismiss the notification
        NotificationManager notificationManager =
            (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

Bundle extras = intent.getExtras();
if (extras == null) {
return;
}

CellBroadcastMessage cbm = extras.getParcelable(CellBroadcastMessage.SMS_CB_MESSAGE_EXTRA);
boolean isEmergencyAlert = cbm.isPublicAlertMessage() || CellBroadcastConfigService
.isOperatorDefinedEmergencyId(cbm.getMessageIdentifier());








