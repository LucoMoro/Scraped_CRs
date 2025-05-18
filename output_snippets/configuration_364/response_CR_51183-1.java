//<Beginning of snippet n. 0>
public class YourActivity extends Activity {
    private static final Object JNI_LOCK = new Object();

    static {
        try {
            System.loadLibrary("your_native_library");
        } catch (UnsatisfiedLinkError e) {
            // Handle library loading exceptions with user-friendly logging
            Log.e("YourActivity", "Failed to load native library: " + e.getMessage());
        }
    }

    private native void initializeJNI();
    private native void cleanupJNI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        synchronized (JNI_LOCK) {
            initializeJNI();
        }
    }

    private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
        unscheduleGcIdler();
        info.activity = r;
        info.state = r.state;
        mH.post(info);
    }

    final void performRestartActivity(IBinder token) {
        if (token == null) {
            throw new IllegalStateException("Token cannot be null");
        }
        // Additional restart logic here
    }
    
    public final void requestRelaunchActivity(IBinder token) {
        if (token == null) {
            throw new IllegalStateException("Token cannot be null");
        }
        // Logic for relaunching the activity
    }

    private void handleRelaunchActivity(ActivityClientRecord tmp) {
        unscheduleGcIdler();
        // Additional relaunch logic if needed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (JNI_LOCK) {
            cleanupJNI();
        }
    }
}
//<End of snippet n. 0>