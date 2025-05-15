
//<Beginning of snippet n. 0>



private static void beginShutdownSequence(Context context) {
synchronized (sIsStartedGuard) {
            if (sIsStarted) {
                Log.d(TAG, "Shutdown sequence already running, returning.");
                return;
            }
sIsStarted = true;
}


//<End of snippet n. 0>








