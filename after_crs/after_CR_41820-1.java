/*IntentService: Fix to run with a lower priority flag.

IntentService is currently using an HandlerThread with the default constructor which assigns
Process.THREAD_PRIORITY_DEFAULT. Since this service is designed to work in the background the
Process.THREAD_PRIORITY_BACKGROUND should be more appropriate.

Change-Id:I4224ca56ae7eee2c099140bf0242be33b8690ad8Signed-off-by: Gonçalo Ferreira <monxalo@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/app/IntentService.java b/core/java/android/app/IntentService.java
//Synthetic comment -- index 96767ae..ec1918e 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

/**
* IntentService is a base class for {@link Service}s that handle asynchronous
//Synthetic comment -- @@ -104,7 +105,7 @@
// method that would launch the service & hand off a wakelock.

super.onCreate();
        HandlerThread thread = new HandlerThread("IntentService[" + mName + "]", Process.THREAD_PRIORITY_BACKGROUND);
thread.start();

mServiceLooper = thread.getLooper();







