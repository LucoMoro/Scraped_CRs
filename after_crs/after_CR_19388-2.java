/*Avoid dead lock by send NTP request in separate thread.

Requests to NTP server takes time and could also fail. This will hang
the Throttle service thread. As resources are locked during this time,
other processes requesting throttle info during this time will also
hang and cause ANRs.

Instead send NTP requests in separate thread.

Change-Id:Id77b447125e7efae41b31fc02023fd493799180b*/




//Synthetic comment -- diff --git a/services/java/com/android/server/ThrottleService.java b/services/java/com/android/server/ThrottleService.java
//Synthetic comment -- index a93a6ee..c41b964 100644

//Synthetic comment -- @@ -345,6 +345,8 @@
private static final int EVENT_POLL_ALARM      = 2;
private static final int EVENT_RESET_ALARM     = 3;
private static final int EVENT_IFACE_UP        = 4;
    private static final int EVENT_NTP_ALARM       = 5;

private class MyHandler extends Handler {
public MyHandler(Looper l) {
super(l);
//Synthetic comment -- @@ -367,6 +369,9 @@
break;
case EVENT_IFACE_UP:
onIfaceUp();
                break;
            case EVENT_NTP_ALARM:
                onNtpAlarm();
}
}

//Synthetic comment -- @@ -521,6 +526,26 @@
}
}

        private void onNtpAlarm() {
            long ntpAge = SystemClock.elapsedRealtime() - cachedNtpTimestamp;
            SntpClient client = new SntpClient();
            if (client.requestTime(mNtpServer, MAX_NTP_FETCH_WAIT)) {
                cachedNtp = client.getNtpTime();
                cachedNtpTimestamp = SystemClock.elapsedRealtime();
                if (!mNtpActive) {
                    mNtpActive = true;
                    if (VDBG) Slog.d(TAG, "found Authoritative time - reseting alarm");
                    mHandler.obtainMessage(EVENT_RESET_ALARM).sendToTarget();
                }
                // Cache has been updated, throw away any pending requests in queue.
                mHandler.removeMessages(EVENT_NTP_ALARM);
            }
            else if (mNtpActive && ntpAge > mMaxNtpCacheAgeSec * 1000) {
                // Failed to retreive time and cache is old
                mNtpActive = false;
            }
        }

private void checkThrottleAndPostNotification(long currentTotal) {
// is throttling enabled?
if (mPolicyThreshold == 0) {
//Synthetic comment -- @@ -730,29 +755,27 @@

private long getBestTime() {
if (mNtpServer != null) {
            // Use cached time if available and not about to expire.
            // NTP requests are sent over UDP, so should request new time ahead
            // (3 * mPolicyPollPeriodSec) of expiration.
            long ntpAge = SystemClock.elapsedRealtime() - cachedNtpTimestamp;
            if (mNtpActive &&
                    ntpAge < (mMaxNtpCacheAgeSec - 3 * mPolicyPollPeriodSec) * 1000) {
                if (VDBG) Slog.v(TAG, "using cached time");
                return cachedNtp + ntpAge;
}

            // Request new time.
            mHandler.obtainMessage(EVENT_NTP_ALARM).sendToTarget();

            // If cache is still valid, use it.
            if (mNtpActive && ntpAge < mMaxNtpCacheAgeSec * 1000) {
                if (VDBG) Slog.v(TAG, "using cached time");
                return cachedNtp + ntpAge;
}
}
long time = System.currentTimeMillis();
if (VDBG) Slog.v(TAG, "using User time: " + time);
return time;
}








