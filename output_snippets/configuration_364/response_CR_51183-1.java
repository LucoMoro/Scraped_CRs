//<Beginning of snippet n. 0>
public class MainActivity extends Activity {
    static {
        System.loadLibrary("native-lib");
    }

    private long nativeHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nativeHandle = initNative();
        if (nativeHandle == 0) {
            throw new RuntimeException("Failed to initialize native handle");
        }
    }

    private native long initNative();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanupNative();
    }

    private void cleanupNative() {
        if (nativeHandle != 0) {
            cleanupNative(nativeHandle);
            nativeHandle = 0;
        }
    }

    private native void cleanupNative(long handle);

    private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
        unscheduleGcIdler();
        info.activity = r;
        info.state = r.state;
        mH.post(info);
    }

    final void performRestartActivity(IBinder token) {
        // Handle activity restart logic here
    }

    public final void requestRelaunchActivity(IBinder token, Intent customIntent) {
        // Logic for relaunch
    }

    private void handleRelaunchActivity(ActivityClientRecord tmp) {
        unscheduleGcIdler();
    }
}
//<End of snippet n. 0>