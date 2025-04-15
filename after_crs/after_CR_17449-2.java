/*Use a service's onStartCommand instead of the deprecated onStart.

Also, return a service's requested started state (as returned by
onStartCommand) and provide a startService variant for setting the
flags with which onStartCommand is being called.

Change-Id:Ie7582edd3cca6419d02ad079898bb309d78470c2*/




//Synthetic comment -- diff --git a/test-runner/src/android/test/ServiceTestCase.java b/test-runner/src/android/test/ServiceTestCase.java
//Synthetic comment -- index fcb9d55..959d744 100644

//Synthetic comment -- @@ -140,13 +140,18 @@

/**
* Start the service under test, in the same way as if it was started by
     * {@link android.content.Context#startService Context.startService()}, providing the
* arguments it supplied.  If you use this method to start the service, it will automatically
* be stopped by {@link #tearDown}.
     *
* @param intent The Intent as if supplied to {@link android.content.Context#startService}.
     * @param flags  The flags that would be passed to {@link android.app.Service#onStartCommand}
     * by the system.
     *
     * @return The return value indicates what semantics the service asks the system to use for
     * the service's current started state.
*/
    protected int startService(Intent intent, int flags) {
assertFalse(mServiceStarted);
assertFalse(mServiceBound);

//Synthetic comment -- @@ -159,11 +164,27 @@
mService.onCreate();
mServiceCreated = true;
}
        int startState = mService.onStartCommand(intent, flags, mServiceId);

mServiceStarted = true;
        return startState;
}

    /**
     * Start the service under test, in the same way as if it was started by
     * {@link android.content.Context#startService Context.startService()}, providing the 
     * arguments it supplied.  If you use this method to start the service, it will automatically
     * be stopped by {@link #tearDown}.
     *  
     * @param intent The Intent as if supplied to {@link android.content.Context#startService}.
     *
     * @return The return value indicates what semantics the service asks the system to use for
     * the service's current started state.
     */
    protected int startService(Intent intent) {
        return startService(intent, 0);
    }

/**
* Start the service under test, in the same way as if it was started by
* {@link android.content.Context#bindService Context.bindService()}, providing the 







