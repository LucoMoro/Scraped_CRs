<<Beginning of snippet n. 0>>
mScreenOn = true;
} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
    mScreenOn = false;
    lastScreenOffTime = System.currentTimeMillis(); // Track when the screen was turned off
} else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
    long currentTime = System.currentTimeMillis();
    if (!mScreenOn && !isKeyguardActive() && !areNotificationsVisible() && (currentTime - lastScreenOffTime > GRACE_PERIOD)) {
        turnOffLED();
    } else if (mScreenOn && isKeyguardActive()) {
        turnOnLED();
    } else if (mScreenOn && !isKeyguardActive()) {
        turnOffLED();
    }
} else if (action.equals(Intent.ACTION_USER_PRESENT)) {
    resetLED();
}

private boolean isKeyguardActive() {
    KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
    return keyguardManager.isKeyguardLocked();
}

private boolean areNotificationsVisible() {
    StatusBarManager statusBarManager = (StatusBarManager) getSystemService(Context.STATUS_BAR_SERVICE);
    return statusBarManager != null && statusBarManager.getActiveNotifications().length > 0;
}

private void turnOnLED() {
    // Implement the logic to turn on the LED
}

private void turnOffLED() {
    // Implement the logic to turn off the LED
}

private void resetLED() {
    // Implement the logic to reset the LED state
}

private long lastScreenOffTime = 0; // Timestamp for the last screen off event
private static final long GRACE_PERIOD = 10000; // Grace period of 10 seconds
<<End of snippet n. 0>>