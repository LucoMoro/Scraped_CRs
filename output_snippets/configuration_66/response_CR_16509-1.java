<<Beginning of snippet n. 0>>
private static void beginShutdownSequence(Context context) {
    synchronized (sIsStartedGuard) {
        if (sIsStarted || shutdownDialogActive) {
            return;
        }
        shutdownDialogActive = true;
    }
    
    // Logic to display shutdown dialog here
    // Assuming a dialog confirmation callback to manage sIsStarted
}

// Callback method after user confirmation
private static void onShutdownConfirmed() {
    synchronized (sIsStartedGuard) {
        sIsStarted = true;
        shutdownDialogActive = false;
    }
    // Logic to perform actual shutdown
}

// Callback method after user cancels shutdown
private static void onShutdownCancelled() {
    synchronized (sIsStartedGuard) {
        shutdownDialogActive = false;
    }
}
<<End of snippet n. 0>>