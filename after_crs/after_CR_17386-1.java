/*Hold partial wakelock during shutdown to avoid entering sleep

The ShutdownThread can get suspended if the device enters sleep by
either the user pressing the power-key or (if the shutdown takes too
long) by the default screen time-out. It will also get immediately
suspended if it is started (in sleep) from the BatteryService upon a
dead battery notification. If the device is woken up before the
battery is drained, the ShutdownThread will resume and finally
complete the shutdown, but if not the phone will stay in sleep until
the battery level is so low that the power is ruthlessly cut.

Change-Id:If64429fd0c98a9136141942be6c337b5c79cf4f1*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ShutdownThread.java b/core/java/com/android/internal/app/ShutdownThread.java
//Synthetic comment -- index e35edc3..37d98be 100644

//Synthetic comment -- @@ -67,7 +67,8 @@
private boolean mActionDone;
private Context mContext;
private PowerManager mPowerManager;
    private PowerManager.WakeLock mCpuWakeLock;
    private PowerManager.WakeLock mScreenWakeLock;
private Handler mHandler;

private ShutdownThread() {
//Synthetic comment -- @@ -155,20 +156,36 @@

pd.show();

sInstance.mContext = context;
sInstance.mPowerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);

        // make sure we never fall asleep again
        sInstance.mCpuWakeLock = null;
        try {
            sInstance.mCpuWakeLock = sInstance.mPowerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, TAG + "-cpu");
            sInstance.mCpuWakeLock.setReferenceCounted(false);
            sInstance.mCpuWakeLock.acquire();
        } catch (SecurityException e) {
            Log.w(TAG, "No permission to acquire wake lock", e);
            sInstance.mCpuWakeLock = null;
        }

        // also make sure the screen stays on for better user experience
        sInstance.mScreenWakeLock = null;
if (sInstance.mPowerManager.isScreenOn()) {
try {
                sInstance.mScreenWakeLock = sInstance.mPowerManager.newWakeLock(
                        PowerManager.FULL_WAKE_LOCK, TAG + "-screen");
                sInstance.mScreenWakeLock.setReferenceCounted(false);
                sInstance.mScreenWakeLock.acquire();
} catch (SecurityException e) {
Log.w(TAG, "No permission to acquire wake lock", e);
                sInstance.mScreenWakeLock = null;
}
}

        // start the thread that initiates shutdown
sInstance.mHandler = new Handler() {
};
sInstance.start();







