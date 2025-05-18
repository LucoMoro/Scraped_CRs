//<Beginning of snippet n. 0>
private static boolean isShutdownRequested = false;
private static boolean isShutdownInProgress = false;

private static void beginShutdownSequence(Context context, boolean confirm) {
    synchronized (sIsStartedGuard) {
        if (!confirm || isShutdownRequested || sIsStarted || isShutdownInProgress) {
            return;
        }
        isShutdownRequested = true;
        isShutdownInProgress = true;
        sIsStarted = true;
    }
    // Initiate shutdown logic here
    // Reset flags after shutdown completion
    synchronized (sIsStartedGuard) {
        isShutdownRequested = false;
        isShutdownInProgress = false;
    }
    // Provide user feedback here
}
//<End of snippet n. 0>