/*Do pulse LED on new notification when the screen is turned off.

If a new notification with LED pulse information is occurred
and the screen is turned off then the LED should be pulsed
until the lock screen is swiped off.

Change-Id:Id90d0dcd42ca0c4dadd131f6b4990d2790b402fbSigned-off-by: Soowon Choi <choi.soowon@gmail.com>*/




//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index f6d3b608..547c487 100755

//Synthetic comment -- @@ -537,6 +537,7 @@
mScreenOn = true;
} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
mScreenOn = false;
                updateNotificationPulse();
} else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
mInCall = (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
TelephonyManager.EXTRA_STATE_OFFHOOK));







