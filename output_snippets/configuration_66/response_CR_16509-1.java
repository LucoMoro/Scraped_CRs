//<Beginning of snippet n. 0>
private static boolean sIsStarted = false;
private static final Object sIsStartedGuard = new Object();
private static boolean isShutdownInitiated = false;

private static void beginShutdownSequence(Context context, boolean confirm) {
    synchronized (sIsStartedGuard) {
        if (!sIsStarted && !isShutdownInitiated) {
            isShutdownInitiated = true;
            // Log shutdown request
            Log.d("Shutdown", "Shutdown sequence initiated");
            // Proceed with shutdown if confirmed
            if (confirm) {
                sIsStarted = true;
                // Add shutdown logic here
            } else {
                // Handle case where confirmation is false
                Log.d("Shutdown", "Shutdown request received without confirmation");
                isShutdownInitiated = false; // Reset the flag if not confirmed
            }
        } else if (isShutdownInitiated) {
            Log.d("Shutdown", "Shutdown already in progress");
        } else {
            Log.d("Shutdown", "Shutdown request ignored due to already started state");
        }
    }
}
//<End of snippet n. 0>