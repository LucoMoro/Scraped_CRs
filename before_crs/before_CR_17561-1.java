/*Handle onPaue/onResume race in StreamStarter

An IllegalArgumentException is thrown from unregisterReceiver in onPause
if the bindToService/onServiceConnected has not completed on onResume.

Trace:

Caused by: java.lang.IllegalArgumentException:
    Receiver not registered: com.android.music.StreamStarter$2@45275808
at android.app.ActivityThread$PackageInfo.forgetReceiverDispatcher(ActivityThread.java:671)
at android.app.ApplicationContext.unregisterReceiver(ApplicationContext.java:747)
at android.content.ContextWrapper.unregisterReceiver(ContextWrapper.java:321)
at com.android.music.StreamStarter.onPause(StreamStarter.java:113)
at android.app.Activity.performPause(Activity.java:3782)
at android.app.Instrumentation.callActivityOnPause(Instrumentation.java:1190)
at android.app.ActivityThread.performPauseActivity(ActivityThread.java:3156)

Change-Id:I26804b6e839eca596d00e76436636002d78f9569*/
//Synthetic comment -- diff --git a/src/com/android/music/StreamStarter.java b/src/com/android/music/StreamStarter.java
old mode 100644
new mode 100755
//Synthetic comment -- index e823ee2..4808a04

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
//Synthetic comment -- @@ -114,7 +115,11 @@
} catch (RemoteException ex) {
}
}
        unregisterReceiver(mStatusListener);
MusicUtils.unbindFromService(mToken);
super.onPause();
}







