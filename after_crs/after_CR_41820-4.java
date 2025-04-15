/*IntentService: Fix to run with a lower priority flag.

Added API to change the priority of an IntentService.
API is protected and final to avoid overrides and be called
only by its subclass.

Change-Id:I4224ca56ae7eee2c099140bf0242be33b8690ad8Signed-off-by: Gonçalo Ferreira <monxalo@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/app/IntentService.java b/core/java/android/app/IntentService.java
//Synthetic comment -- index 96767ae..2581fde 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

/**
* IntentService is a base class for {@link Service}s that handle asynchronous
//Synthetic comment -- @@ -54,6 +55,7 @@
private volatile ServiceHandler mServiceHandler;
private String mName;
private boolean mRedelivery;
    private int mPriority = Process.THREAD_PRIORITY_DEFAULT;

private final class ServiceHandler extends Handler {
public ServiceHandler(Looper looper) {
//Synthetic comment -- @@ -104,7 +106,7 @@
// method that would launch the service & hand off a wakelock.

super.onCreate();
        HandlerThread thread = new HandlerThread("IntentService[" + mName + "]", mPriority);
thread.start();

mServiceLooper = thread.getLooper();
//Synthetic comment -- @@ -147,6 +149,17 @@
}

/**
     * This method sets the priority of the IntentService. To work it must be called in
     * your subclass's constructor.
     *
     * @param priority The priority to run the IntentService. The value supplied must be
     * from {@link android.os.Process}
     */
    protected final void setPriority(int priority) {
        mPriority = priority;
    }

    /**
* This method is invoked on the worker thread with a request to process.
* Only one Intent is processed at a time, but the processing happens on a
* worker thread that runs independently from other application logic.







