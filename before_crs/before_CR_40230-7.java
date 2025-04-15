/*Use default UncaughtExceptionHandler to report finalizer timeouts when defined.

In a previous release dalvik would abort on finalizer timeouts which
would be noticed by various tools such as the monkey. However, more
recently when the handling of this was moved to managed code, we
simply logged and exited, which went unnoticed by the monkeys. By
reporting via the default UncaughtExceptionHandler, finalizer timeouts
will once again be noticed by the monkeys, this time via the
application crash reporting.

Bug: 6894375
Change-Id:I94664f8bb54dc7647b4d3666484d88b1c8428c4b*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Daemons.java b/luni/src/main/java/java/lang/Daemons.java
//Synthetic comment -- index a7d0964..c40228f 100644

//Synthetic comment -- @@ -31,8 +31,9 @@
* @hide
*/
public final class Daemons {
    private static final int NANOS_PER_MILLI = 1000000;
    private static final long MAX_FINALIZE_MILLIS = 10L * 1000L; // 10 seconds

public static void start() {
ReferenceQueueDaemon.INSTANCE.start();
//Synthetic comment -- @@ -203,41 +204,69 @@

@Override public void run() {
while (isRunning()) {
try {
                    Object object = FinalizerDaemon.INSTANCE.finalizingObject;
                    long startedNanos = FinalizerDaemon.INSTANCE.finalizingStartedNanos;

                    if (object == null) {
                        synchronized (this) {
                            // wait until something is being finalized
                            // http://code.google.com/p/android/issues/detail?id=22778
                            wait();
                            continue;
                        }
                    }

                    long elapsedMillis = (System.nanoTime() - startedNanos) / NANOS_PER_MILLI;
                    long sleepMillis = MAX_FINALIZE_MILLIS - elapsedMillis;
                    if (sleepMillis > 0) {
                        Thread.sleep(sleepMillis);
                        elapsedMillis = (System.nanoTime() - startedNanos) / NANOS_PER_MILLI;
                    }

                    if (object != FinalizerDaemon.INSTANCE.finalizingObject
                            || VMRuntime.getRuntime().isDebuggerActive()) {
                        continue;
                    }

                    // The current object has exceeded the finalization deadline; abort!
                    Exception syntheticException = new TimeoutException();
                    syntheticException.setStackTrace(FinalizerDaemon.INSTANCE.getStackTrace());
                    System.logE(object.getClass().getName() + ".finalize() timed out after "
                            + elapsedMillis + " ms; limit is " + MAX_FINALIZE_MILLIS + " ms",
                            syntheticException);
                    System.exit(2);
} catch (InterruptedException ignored) {
}
}
}
}
}







