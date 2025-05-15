
//<Beginning of snippet n. 0>


// Keep track of screen on/off state, but do not turn off the notification light
// until user passes through the lock screen or views the notification.
mScreenOn = true;
                // the lock screen may not be present, so make sure the LED gets turned off
                // when the screen comes on.
                updateNotificationPulse();
} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
mScreenOn = false;
} else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {

//<End of snippet n. 0>








