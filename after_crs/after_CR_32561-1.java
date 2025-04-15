/*Aprof support for JNI libraries

Change-Id:If958d11bac3506c4616a90d2c74f6d9d0aa6d02dSigned-off-by: Kito Cheng <kito@0xlab.org>*/




//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
//Synthetic comment -- index 0c761fc..8e71ad7 100644

//Synthetic comment -- @@ -112,6 +112,16 @@
}
}

final class Aprof {
    static {
        System.loadLibrary("aprof_jni_runtime");
    }
    public static final String TAG = "aprof";
    public static final int APROF_ON = 1;
    public static final int APROF_OFF = 0;
    public static native void control(int c);
}

/**
* This manages the execution of the main thread in an
* application process, scheduling and executing activities,
//Synthetic comment -- @@ -1963,6 +1973,7 @@
}

private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
        Aprof.control(Aprof.APROF_ON);
// If we are getting ready to gc after going to the background, well
// we are back active so skip it.
unscheduleGcIdler();
//Synthetic comment -- @@ -2867,6 +2878,7 @@
r.token, r.state, info.thumbnail, info.description);
} catch (RemoteException ex) {
}
        Aprof.control(Aprof.APROF_OFF);
}

final void performRestartActivity(IBinder token) {
//Synthetic comment -- @@ -3182,6 +3194,7 @@
// If the system process has died, it's game over for everyone.
}
}
        Aprof.control(Aprof.APROF_OFF);
}

public final void requestRelaunchActivity(IBinder token,
//Synthetic comment -- @@ -3241,6 +3254,7 @@
}

private void handleRelaunchActivity(ActivityClientRecord tmp) {
        Aprof.control(Aprof.APROF_ON);
// If we are getting ready to gc after going to the background, well
// we are back active so skip it.
unscheduleGcIdler();







