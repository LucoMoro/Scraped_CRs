//<Beginning of snippet n. 0>

// Keep track of screen on/off state, but do not turn off the notification light
// until user passes through the lock screen or views the notification.
mScreenOn = true;

if (isKeyguardActive()) {
    // Keyguard is active, LED behavior should remain unchanged
} else {
    // If Keyguard is inactive, turn off the notification LED
    turnOffNotificationLED();
}

} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
    mScreenOn = false;

    // Check if Keyguard is inactive and user's LanckScreen setting is disabled
    if (!isKeyguardActive() && !isLockScreenEnabled()) {
        turnOffNotificationLED();
    }

} else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
    // handle phone state change
}

// Add grace period logic for POWER key pressed during screen timeout
if (mScreenOn && isPowerKeyPressed() && !isKeyguardActive()) {
    turnOffNotificationLED();
}

//<End of snippet n. 0>