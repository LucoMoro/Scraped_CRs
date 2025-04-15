/*Tell init to shutdown/reboot for us

Let it cleanup running services etc...

This may not be the right place to change things, possibly should just
change the core/jni/android_os_Power.cpp functions to tell init instead.*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ShutdownThread.java b/core/java/com/android/internal/app/ShutdownThread.java
//Synthetic comment -- index a96253b..adb0cc1 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.os.SystemProperties;
import android.os.storage.IMountService;
import android.os.storage.IMountShutdownObserver;

//Synthetic comment -- @@ -324,7 +325,8 @@
if (mReboot) {
Log.i(TAG, "Rebooting, reason: " + mRebootReason);
try {
//                Power.reboot(mRebootReason);
                SystemProperties.set("ctl.start", "reboot:" + mRebootReason);
} catch (Exception e) {
Log.e(TAG, "Reboot failed, will attempt shutdown instead", e);
}
//Synthetic comment -- @@ -341,6 +343,7 @@

// Shutdown power
Log.i(TAG, "Performing low-level shutdown...");
//        Power.shutdown();
        SystemProperties.set("ctl.start", "shutdown");
}
}







