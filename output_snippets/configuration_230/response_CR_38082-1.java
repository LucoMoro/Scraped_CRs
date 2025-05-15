<<Beginning of snippet n. 0>>
mScreenOn = true;
if (action.equals(Intent.ACTION_SCREEN_OFF)) {
    mScreenOn = false;
} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
    if (!isKeyguardLocked()) {
        turnOffLED();
    }
} else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
    // Additional handling here
}
    
private void handlePowerKeyPress() {
    turnOnLED();
    new Handler().postDelayed(() -> {
        if (!isKeyguardLocked()) {
            turnOffLED();
        }
    }, 10000);
}

private void userInteractedWithNotification() {
    if (mScreenOn && !isKeyguardLocked()) {
        turnOffLED();
    }
}
<<End of snippet n. 0>>