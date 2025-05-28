//<Beginning of snippet n. 0>
mScreenOn = true;
if (action.equals(Intent.ACTION_SCREEN_OFF)) {
    mScreenOn = false;
    turnOffLED(); // Turn off the LED immediately if Keyguard is inactive
} else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
    if (isKeyguardActive()) {
        handleLEDActivation(); // Activate LED only when user acknowledges or Keyguard is engaged
    } else if (isNotificationAcknowledged()) {
        activateLED(); // Check if notification is acknowledged only when Keyguard is inactive
    }
} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
    if (!isKeyguardActive()) {
        turnOffLED(); // Ensure LED is turned off immediately when screen is on and Keyguard is inactive
    }
}
//<End of snippet n. 0>