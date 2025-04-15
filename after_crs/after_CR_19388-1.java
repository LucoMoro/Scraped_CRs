/*Avoid dead lock by send NTP request in separate thread.

Requests to NTP server takes time and could also fail. This will hang
the Throttle service thread. As resources are locked during this time,
other processes requesting throttle info during this time will also
hang and cause ANRs.

Instead send NTP requests in separate thread.

Change-Id:Id77b447125e7efae41b31fc02023fd493799180b*/




//Synthetic comment -- diff --git a/services/java/com/android/server/ThrottleService.java b/services/java/com/android/server/ThrottleService.java
//Synthetic comment -- index a93a6ee..10f9afb 100644

//Synthetic comment -- @@ -727,27 +727,52 @@
private int mMaxNtpCacheAgeSec = MAX_NTP_CACHE_AGE_SEC;
private long cachedNtp;
private long cachedNtpTimestamp;
    private final Object mNtpRequestPendingGuard = new Object();
    private boolean mNtpRequestPending = false;

private long getBestTime() {
if (mNtpServer != null) {
            // Use cached time if available and not to about to expire.
            // NTP requests are sent over UDP, so should request new time ahead
            // (3 * mPolicyPollPeriodSec) of expiration.
            long ntpAge = SystemClock.elapsedRealtime() - cachedNtpTimestamp;
if (mNtpActive) {
                if (ntpAge < (mMaxNtpCacheAgeSec - 3 * mPolicyPollPeriodSec) * 1000) {
if (VDBG) Slog.v(TAG, "using cached time");
return cachedNtp + ntpAge;
}
}

            synchronized (mNtpRequestPendingGuard) {
                if (!mNtpRequestPending) {
                    mNtpRequestPending = true;
                    new Thread() {
                        @Override
                        public void run() {
                            SntpClient client = new SntpClient();
                            if (client.requestTime(mNtpServer, MAX_NTP_FETCH_WAIT)) {
                                cachedNtp = client.getNtpTime();
                                cachedNtpTimestamp = SystemClock.elapsedRealtime();
                                if (!mNtpActive) {
                                    mNtpActive = true;
                                    if (VDBG) Slog.d(TAG, "found Authoritative time - reseting alarm");
                                    mHandler.obtainMessage(EVENT_RESET_ALARM).sendToTarget();
                                }
                            }
                            synchronized (mNtpRequestPendingGuard) {
                                mNtpRequestPending = false;
                            }
                        }
                    }.start();
}
            }

            // If cache is still valid, use it.
            if (mNtpActive) {
                if (ntpAge < mMaxNtpCacheAgeSec * 1000) {
                    if (VDBG) Slog.v(TAG, "using cached time");
                    return cachedNtp + ntpAge;
                }
}
}
long time = System.currentTimeMillis();







