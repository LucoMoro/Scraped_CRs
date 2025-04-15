/*Move NTP updates outside locks

When the ServerThread handles ACTION_SHUTDOWN intent it sometimes get
stuck on mStatsLock in NetworkStatsService.java.
0  com.android.server.net.NetworkStatsService$5.onReceive()
1  android.app.LoadedApk$ReceiverDispatcher$Args.run()
2  android.os.Handler.handleCallback()
3  android.os.Handler.dispatchMessage()
4  android.os.Looper.loop()
5  com.android.server.ServerThread.run()
This happens when the NetworkStats thread is already holding the
mStatsLock while updating NTP.
0  libcore.io.Posix.getaddrinfo()
1  libcore.io.ForwardingOs.getaddrinfo()
2  java.net.InetAddress.lookupHostByName()
3  java.net.InetAddress.getAllByNameImpl()
4  java.net.InetAddress.getByName()
5  android.net.SntpClient.requestTime()
6  android.util.NtpTrustedTime.forceRefresh()
7  com.android.server.net.NetworkStatsService.performPoll()
8  com.android.server.net.NetworkStatsService.access$100()
9  com.android.server.net.NetworkStatsService$2.onReceive()
10 android.app.LoadedApk$ReceiverDispatcher$Args.run()
11 android.os.Handler.handleCallback()
12 android.os.Handler.dispatchMessage()
13 android.os.Looper.loop()
14 android.os.HandlerThread.run()
Since the NTP update consists of several socket operations it may get
stuck long enough to trigger a System Server Watchdog even though the
socket timeout is set to 20 second.
Further, the NTP update doesn't actually need to be performed inside
the locks and an attempt to change this was made earlier, but the code
wasn't actually moved outside the locks.

Change-Id:Ib37a2b8c2d51a01adb7ff01764f82309433703f0*/




//Synthetic comment -- diff --git a/services/java/com/android/server/net/NetworkStatsService.java b/services/java/com/android/server/net/NetworkStatsService.java
//Synthetic comment -- index ba122ec..4d3eca6 100644

//Synthetic comment -- @@ -905,14 +905,14 @@
}

private void performPoll(int flags) {
        // try refreshing time source when stale
        if (mTime.getCacheAge() > mSettings.getTimeCacheMaxAge()) {
            mTime.forceRefresh();
        }

synchronized (mStatsLock) {
mWakeLock.acquire();

try {
performPollLocked(flags);
} finally {







