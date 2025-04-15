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
//Synthetic comment -- index a7d0964..78a4152 100644

//Synthetic comment -- @@ -31,8 +31,9 @@
* @hide
*/
public final class Daemons {
    private static final int NANOS_PER_MILLI = 1000 * 1000;
    private static final int NANOS_PER_SECOND = NANOS_PER_MILLI * 1000;
    private static final long MAX_FINALIZE_NANOS = 10L * NANOS_PER_SECOND;

public static void start() {
ReferenceQueueDaemon.INSTANCE.start();
//Synthetic comment -- @@ -203,41 +204,78 @@

@Override public void run() {
while (isRunning()) {
                Object object = waitForObject();
                if (object == null) {
                    // We have been interrupted, need to see if this daemon has been stopped.
                    continue;
                }
                boolean finalized = waitForFinalization(object);
                if (!finalized && !VMRuntime.getRuntime().isDebuggerActive()) {
                    finalizerTimedOut(object);
                    break;
}
}
}

        private Object waitForObject() {
            while (true) {
                Object object = FinalizerDaemon.INSTANCE.finalizingObject;
                if (object != null) {
                    return object;
                }
                synchronized (this) {
                    // wait until something is ready to be finalized
                    // http://code.google.com/p/android/issues/detail?id=22778
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        // Daemon.stop may have interrupted us.
                        return null;
                    }
                }
            }
        }

        private void sleepFor(long startNanos, long durationNanos) {
            while (true) {
                long elapsedNanos = System.nanoTime() - startNanos;
                long sleepNanos = durationNanos - elapsedNanos;
                long sleepMills = sleepNanos / NANOS_PER_MILLI;
                if (sleepMills <= 0) {
                    return;
                }
                try {
                    Thread.sleep(sleepMills);
                } catch (InterruptedException e) {
                    if (!isRunning()) {
                        return;
                    }
                }
            }
        }

        private boolean waitForFinalization(Object object) {
            sleepFor(FinalizerDaemon.INSTANCE.finalizingStartedNanos, MAX_FINALIZE_NANOS);
            return object != FinalizerDaemon.INSTANCE.finalizingObject;
        }

        private static void finalizerTimedOut(Object object) {
            // The current object has exceeded the finalization deadline; abort!
            String message = object.getClass().getName() + ".finalize() timed out after "
                    + (MAX_FINALIZE_NANOS / NANOS_PER_SECOND) + " seconds";
            Exception syntheticException = new TimeoutException(message);
            // We use the stack from where finalize() was running to show where it was stuck.
            syntheticException.setStackTrace(FinalizerDaemon.INSTANCE.getStackTrace());
            Thread.UncaughtExceptionHandler h = Thread.getDefaultUncaughtExceptionHandler();
            if (h == null) {
                // If we have no handler, log and exit.
                System.logE(message, syntheticException);
                System.exit(2);
            }
            // Otherwise call the handler to do crash reporting.
            // We don't just throw because we're not the thread that
            // timed out; we're the thread that detected it.
            h.uncaughtException(Thread.currentThread(), syntheticException);
        }
}
}







