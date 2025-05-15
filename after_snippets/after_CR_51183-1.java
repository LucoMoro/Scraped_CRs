
//<Beginning of snippet n. 0>


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
}

private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
        Aprof.control(Aprof.APROF_ON);
// If we are getting ready to gc after going to the background, well
// we are back active so skip it.
unscheduleGcIdler();
info.activity = r;
info.state = r.state;
mH.post(info);
        Aprof.control(Aprof.APROF_OFF);
}

final void performRestartActivity(IBinder token) {
// If the system process has died, it's game over for everyone.
}
}
        Aprof.control(Aprof.APROF_OFF);
}

public final void requestRelaunchActivity(IBinder token,
}

private void handleRelaunchActivity(ActivityClientRecord tmp) {
        Aprof.control(Aprof.APROF_ON);
// If we are getting ready to gc after going to the background, well
// we are back active so skip it.
unscheduleGcIdler();

//<End of snippet n. 0>








