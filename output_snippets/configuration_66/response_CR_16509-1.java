//<Beginning of snippet n. 0>
private static boolean isShutdownInProgress = false;
private static final Object sIsStartedGuard = new Object();
private static boolean sIsStarted = false;

private static void beginShutdownSequence(Context context, boolean confirm) {
    if (!confirm || isShutdownInProgress) {
        return;
    }
    
    synchronized (sIsStartedGuard) {
        if (!sIsStarted) {
            isShutdownInProgress = true;
            sIsStarted = true;
            // Logic to handle the shutdown sequence goes here
        }
    }
}
//<End of snippet n. 0>