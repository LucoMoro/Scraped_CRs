//<Beginning of snippet n. 0>
private static boolean isShutdownRequested = false;

private static void beginShutdownSequence(Context context, boolean confirm) {
    synchronized (sIsStartedGuard) {
        if (!confirm || isShutdownRequested || sIsStarted) {
            return;
        }
        isShutdownRequested = true;
        sIsStarted = true;
    }
    // Initiate shutdown logic here
}
//<End of snippet n. 0>