//<Beginning of snippet n. 0>
public class YourActivity extends Activity {
    static {
        System.loadLibrary("your_native_library");
    }

    private native void nativeMethod();
    private boolean isJNIInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeJNI();
    }
    
    @Override
    protected void onDestroy() {
        cleanupJNI();
        super.onDestroy();
    }

    private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
        unscheduleGcIdler();
        info.activity = r;
        info.state = r.state;
        mH.post(info);
    }

    final void performRestartActivity(IBinder token) {
        if (token == null) {
            Toast.makeText(this, "Error: Activity token is null.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Handle activity restart
    }

    public final void requestRelaunchActivity(IBinder token, Intent customIntent) {
        // Your relaunch logic here
    }

    private void handleRelaunchActivity(ActivityClientRecord tmp) {
        unscheduleGcIdler();
        // Add necessary codes for relaunch based on tmp
    }

    private void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        initializeJNI();
    }

    private void detachJNIMethods() {
        if (isJNIInitialized) {
            // Detach JNI environment logic
            isJNIInitialized = false;
        }
    }

    private void handleNativeCall() {
        try {
            nativeMethod();
        } catch (Exception e) {
            Toast.makeText(this, "Error occurred while calling native method: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void initializeJNI() {
        if (!isJNIInitialized) {
            try {
                // Initialize JNI environment and attach to current thread
                isJNIInitialized = true;
            } catch (Exception e) {
                // Log error for debugging purposes
                Log.e("YourActivity", "Failed to initialize JNI: " + e.getMessage());
            }
        }
    }

    private void cleanupJNI() {
        if (isJNIInitialized) {
            detachJNIMethods();
            isJNIInitialized = false;
        }
    }
}
//<End of snippet n. 0>