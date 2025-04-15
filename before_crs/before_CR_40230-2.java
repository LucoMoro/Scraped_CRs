/*Use default UncaughtExceptionHandler to report finalizer timeouts when defined.

In a previous release davlik would on finalizer timeouts which would
be noticed by various tools such as the monkey. However, more recently
when the handling of this was moved to managed code, we simply logged
and exited, which went unnoticed by the monkeys. By reporting via the
default UncaughtExceptionHandler, finalizer timeouts will once again
be noticed by the monkeys, this time via the application crash
reporting.

Bug: 6894375
Change-Id:I94664f8bb54dc7647b4d3666484d88b1c8428c4b*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Daemons.java b/luni/src/main/java/java/lang/Daemons.java
//Synthetic comment -- index a7d0964..e5a03a7 100644

//Synthetic comment -- @@ -229,12 +229,18 @@
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







