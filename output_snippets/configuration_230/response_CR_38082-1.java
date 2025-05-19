//<Beginning of snippet n. 0>
mScreenOn = true;
if (action.equals(Intent.ACTION_SCREEN_OFF)) {
    mScreenOn = false;
    turnOffLED(); // Turn off the LED immediately if Keyguard is inactive
} else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
    if (isKeyguardActive()) {
        handleLEDActivation(); // Activate LED only when user acknowledges or Keyguard is engaged
    }
} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
    if (!isKeyguardActive() && !isNotificationsVisible()) {
        turnOffLED(); // Ensure LED is turned off immediately when screen is on, Keyguard is inactive, and notifications are not visible
    } else if (isKeyguardActive() || isNotificationAcknowledged()) {
        activateLED(); // Activate LED if Keyguard is active or notification is acknowledged
    }
}
//<End of snippet n. 0>