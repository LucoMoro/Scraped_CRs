/*Aprof support for JNI libraries

Signed-off-by: Kito Cheng <kito@0xlab.org>

Change-Id:Ie2fd278a9b3b20fc565c039dfaa8ec320327b595*/
//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
//Synthetic comment -- index d880817..23ab093 100644

//Synthetic comment -- @@ -130,6 +130,16 @@
}
}

/**
* This manages the execution of the main thread in an
* application process, scheduling and executing activities,
//Synthetic comment -- @@ -2212,6 +2222,7 @@
}

private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
// If we are getting ready to gc after going to the background, well
// we are back active so skip it.
unscheduleGcIdler();
//Synthetic comment -- @@ -3206,6 +3217,7 @@
info.activity = r;
info.state = r.state;
mH.post(info);
}

final void performRestartActivity(IBinder token) {
//Synthetic comment -- @@ -3521,6 +3533,7 @@
// If the system process has died, it's game over for everyone.
}
}
}

public final void requestRelaunchActivity(IBinder token,
//Synthetic comment -- @@ -3580,6 +3593,7 @@
}

private void handleRelaunchActivity(ActivityClientRecord tmp) {
// If we are getting ready to gc after going to the background, well
// we are back active so skip it.
unscheduleGcIdler();







