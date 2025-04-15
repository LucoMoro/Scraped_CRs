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
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
//Synthetic comment -- @@ -66,6 +67,7 @@
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/** {@hide} */
public class NotificationManagerService extends INotificationManager.Stub
//Synthetic comment -- @@ -74,6 +76,8 @@
private static final boolean DBG = false;

private static final int MAX_PACKAGE_NOTIFICATIONS = 50;
    private static final int NOTIFICATION_REQUEST_INTERVAL = 30000; // 30 seconds
    private static final int MAX_PACKAGE_NOTIFICATION_REQUESTS = 500;

// message codes
private static final int MESSAGE_TIMEOUT = 2;
//Synthetic comment -- @@ -124,6 +128,14 @@
private final ArrayList<NotificationRecord> mNotificationList =
new ArrayList<NotificationRecord>();

    private class PackageRequestInfo {
        int requestCount;
        long lastPostTime;
    }

    private final HashMap<String, PackageRequestInfo> mPackageInfo =
            new HashMap<String, PackageRequestInfo>();

private ArrayList<ToastRecord> mToastQueue;

private ArrayList<NotificationRecord> mLights = new ArrayList<NotificationRecord>();
//Synthetic comment -- @@ -715,6 +727,42 @@
}
}

        // Limit the number of notification requests, notify and cancel that
        // a package can post. The MAX_PACKAGE_NOTIFICATIONS doesn't work for
        // packages which notify and quickly cancel it and do this in an
        // iteration
        if (!"android".equals(pkg)) {
            synchronized (mPackageInfo) {
                if (!mPackageInfo.containsKey(pkg)) {
                    final PackageRequestInfo pInfo = new PackageRequestInfo();
                    pInfo.requestCount = 1;
                    pInfo.lastPostTime = SystemClock.elapsedRealtime();
                    mPackageInfo.put(pkg,pInfo);
                }
                else {
                    final PackageRequestInfo pInfo = mPackageInfo.get(pkg);
                    final long currentTime = SystemClock.elapsedRealtime();
                    if ((currentTime - pInfo.lastPostTime) <= NOTIFICATION_REQUEST_INTERVAL) {
                         // Keep track of requests posted within last 30 seconds
                         pInfo.requestCount++;
                    }
                    else {
                         pInfo.requestCount = 1;
                         pInfo.lastPostTime = SystemClock.elapsedRealtime();
                    }

                    if (pInfo.requestCount >= MAX_PACKAGE_NOTIFICATION_REQUESTS) {
                        // 500 requests within a span of 30 seconds is high
                        if (pInfo.requestCount%MAX_PACKAGE_NOTIFICATION_REQUESTS == 0) {
                            Slog.e(TAG, "Package has already posted too many notifications. "
                                    + "Not showing more.  package=" + pkg);
                        }
                        return;
                    }
                }
            }
        }

// This conditional is a dirty hack to limit the logging done on
//     behalf of the download manager without affecting other apps.
if (!pkg.equals("com.android.providers.downloads")
//Synthetic comment -- @@ -1007,6 +1055,26 @@

public void cancelNotificationWithTag(String pkg, String tag, int id) {
checkIncomingCall(pkg);

        // Limit the number of notification requests, notify and cancel that
        // a package can post. The MAX_PACKAGE_NOTIFICATIONS doesn't work for
        // packages which notify and quickly cancel it and do this in an
        // iteration
        synchronized (mPackageInfo) {
            if (!"android".equals(pkg) && mPackageInfo.containsKey(pkg)) {
                final PackageRequestInfo pInfo = mPackageInfo.get(pkg);
                final long currentTime = SystemClock.elapsedRealtime();
                if ((currentTime - pInfo.lastPostTime) <= NOTIFICATION_REQUEST_INTERVAL) {
                    // Keep track of requests posted within last 30 seconds
                    pInfo.requestCount++;
                }
                else {
                    pInfo.requestCount = 1;
                    pInfo.lastPostTime = SystemClock.elapsedRealtime();
                }
            }
        }

// Don't allow client applications to cancel foreground service notis.
cancelNotification(pkg, tag, id, 0,
Binder.getCallingUid() == Process.SYSTEM_UID







