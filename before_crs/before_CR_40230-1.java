/*Throw on finalizer timeout so it can be reported by crash reporters

Bug: 6894375
Change-Id:I94664f8bb54dc7647b4d3666484d88b1c8428c4b*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Daemons.java b/luni/src/main/java/java/lang/Daemons.java
//Synthetic comment -- index a7d0964..3653359 100644

//Synthetic comment -- @@ -103,14 +103,6 @@
}
}
}

        /**
         * Returns the current stack trace of the thread, or an empty stack trace
         * if the thread is not currently running.
         */
        public synchronized StackTraceElement[] getStackTrace() {
            return thread != null ? thread.getStackTrace() : EmptyArray.STACK_TRACE_ELEMENT;
        }
}

/**
//Synthetic comment -- @@ -229,12 +221,10 @@
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







