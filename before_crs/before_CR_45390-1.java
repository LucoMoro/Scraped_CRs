/*Do pluse LED on new notification when the screen is turned off.

If a new notification with LED infos is occured and the screen is
turned off then the LED should be pulsed. But, It's doesn't work.

Change-Id:I5ea7e4d4e3abf71f544a572047e5450c649ff0d3Signed-off-by: Soowon Choi <choi.soowon@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index f6d3b608..547c487 100755

//Synthetic comment -- @@ -537,6 +537,7 @@
mScreenOn = true;
} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
mScreenOn = false;
} else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
mInCall = (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
TelephonyManager.EXTRA_STATE_OFFHOOK));







