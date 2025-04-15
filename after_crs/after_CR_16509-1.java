/*Crash when powering off.

Fixing a race condition. If "confirm" is true and the shutdown
dialog is shown it may take some time before the user presses the
ok button. During this time a new call to shutdown may be made with
confirm=false. When this happens the shutdown thread is started by
the second call, and later when the users presses the ok button. This
results in ShutdownThread.beginShutdownSequence() being called
twice. I.e., the beginShutdownSequence lacks the proper protection
for this error case.

Change-Id:Ib9b01c5a43f4dc23de09057f3fc1507000317faf*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ShutdownThread.java b/core/java/com/android/internal/app/ShutdownThread.java
//Synthetic comment -- index a96253b..e35edc3 100644

//Synthetic comment -- @@ -133,6 +133,10 @@

private static void beginShutdownSequence(Context context) {
synchronized (sIsStartedGuard) {
            if (sIsStarted) {
                Log.d(TAG, "Shutdown sequence already running, returning.");
                return;
            }
sIsStarted = true;
}








