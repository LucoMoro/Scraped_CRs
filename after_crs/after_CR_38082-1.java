/*Fix: Notification LED will not turn off without Keyguard

The problem is that there are cases where the screen turns on,
but there is no Keyguard:
1) The user disables the LockScreen from the Settings.
2) Following the auto screen-timeout, there is a grace period
   (~10 secs) where the Keyguard will not activate after the
   user presses the POWER key.
In those cases, the LED will continue to flash, with the screen
turned on, until the user explicitly acknowledges the notification.
This is redundant (since the user can see their notifications in
the status bar) and a waste of power.

Change-Id:I9a3319be0c3e6dd3baf5691515259e7f08baf217*/




//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index 3cf447c..4c8a661 100755

//Synthetic comment -- @@ -349,6 +349,9 @@
// Keep track of screen on/off state, but do not turn off the notification light
// until user passes through the lock screen or views the notification.
mScreenOn = true;
                // the lock screen may not be present, so make sure the LED gets turned off
                // when the screen comes on.
                updateNotificationPulse();
} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
mScreenOn = false;
} else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {







