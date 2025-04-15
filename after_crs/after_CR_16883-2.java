/*Handle stopping of services with still bound applications.

When a service is stopping we get unbindFinished when all connections
are unbinded. If applications are still bound to this Service we will
rebind the connection making the service hang in stopping state. This
fixed issues with ANR during CTS test
android.os.cts.BinderTest#testTransact

Change-Id:I9402aebd0d2d0fa3e0e6381fb51d3189d530f31b*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 16cd62b..019417c 100644

//Synthetic comment -- @@ -9694,8 +9694,10 @@
if (DEBUG_SERVICE) Slog.v(TAG, "unbindFinished in " + r
+ " at " + b + ": apps="
+ (b != null ? b.apps.size() : 0));

                boolean inStopping = mStoppingServices.contains(r);
if (b != null) {
                    if (b.apps.size() > 0 && !inStopping) {
// Applications have already bound since the last
// unbind, so just rebind right here.
requestServiceBindingLocked(r, b, true);
//Synthetic comment -- @@ -9706,7 +9708,7 @@
}
}

                serviceDoneExecutingLocked(r, inStopping);

Binder.restoreCallingIdentity(origId);
}







