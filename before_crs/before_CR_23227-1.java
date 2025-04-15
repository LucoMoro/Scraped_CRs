/*frameworks/base: Fix to limit the number of Notification requests

Fix to limit the number of Notification requests, both notify and
cancel.

Change-Id:I22378e7c75f3f47447d64b5f7131198b7ddfb082*/
//Synthetic comment -- diff --git a/services/java/com/android/server/NotificationManagerService.java b/services/java/com/android/server/NotificationManagerService.java
//Synthetic comment -- index 540389e..f451098 100755

//Synthetic comment -- @@ -52,6 +52,7 @@
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
//Synthetic comment -- @@ -66,6 +67,7 @@
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/** {@hide} */
public class NotificationManagerService extends INotificationManager.Stub
//Synthetic comment -- @@ -74,6 +76,8 @@
private static final boolean DBG = false;

private static final int MAX_PACKAGE_NOTIFICATIONS = 50;

// message codes
private static final int MESSAGE_TIMEOUT = 2;
//Synthetic comment -- @@ -124,6 +128,14 @@
private final ArrayList<NotificationRecord> mNotificationList =
new ArrayList<NotificationRecord>();

private ArrayList<ToastRecord> mToastQueue;

private ArrayList<NotificationRecord> mLights = new ArrayList<NotificationRecord>();
//Synthetic comment -- @@ -715,6 +727,42 @@
}
}

// This conditional is a dirty hack to limit the logging done on
//     behalf of the download manager without affecting other apps.
if (!pkg.equals("com.android.providers.downloads")
//Synthetic comment -- @@ -1007,6 +1055,26 @@

public void cancelNotificationWithTag(String pkg, String tag, int id) {
checkIncomingCall(pkg);
// Don't allow client applications to cancel foreground service notis.
cancelNotification(pkg, tag, id, 0,
Binder.getCallingUid() == Process.SYSTEM_UID







