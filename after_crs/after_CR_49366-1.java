/*Fix Sync bug

All access to Outstanding count need to be protected so the
wake lock gets released when it should be.

Change-Id:I67d0b1154f62cf7a9f5ea94ed2639fd620a9fb58*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index ec5319d..6b55c79 100644

//Synthetic comment -- @@ -304,8 +304,10 @@
if (s == null) {
rr.onError(RADIO_NOT_AVAILABLE, null);
rr.release();
                        synchronized (mWakeLock) {
                            if (mRequestMessagesOutstanding > 0) {
                                mRequestMessagesOutstanding--;
                            }
}
releaseWakeLockIfDone();
return;
//Synthetic comment -- @@ -2119,8 +2121,9 @@
*/
private void clearRequestList(int error, boolean loggable) {
RILRequest rr;
        int count;
synchronized (mRequestList) {
            count = mRequestList.size();
if (RILJ_LOGD && loggable) {
Rlog.d(LOG_TAG, "WAKE_LOCK_TIMEOUT " +
" mRequestMessagesOutstanding=" + mRequestMessagesOutstanding +
//Synthetic comment -- @@ -2136,27 +2139,34 @@
rr.onError(error, null);
rr.release();
}
            mRequestList.clear();
        }
        synchronized (mWakeLock) {
mRequestMessagesOutstanding -= count;
if (mRequestMessagesOutstanding < 0) mRequestMessagesOutstanding = 0;
}
}

private RILRequest findAndRemoveRequestFromList(int serial) {
        RILRequest result = null;
synchronized (mRequestList) {
for (int i = 0, s = mRequestList.size() ; i < s ; i++) {
RILRequest rr = mRequestList.get(i);

if (rr.mSerial == serial) {
mRequestList.remove(i);
                    result = rr;
}
}
}

        if (result != null) {
            synchronized (mWakeLock) {
                if (mRequestMessagesOutstanding > 0) mRequestMessagesOutstanding--;
            }
        }

        return result;
}

private void
//Synthetic comment -- @@ -3849,8 +3859,10 @@
pw.println(" mReceiver=" + mReceiver);
pw.println(" mWakeLock=" + mWakeLock);
pw.println(" mWakeLockTimeout=" + mWakeLockTimeout);
        synchronized (mWakeLock) {
            pw.println(" mRequestMessagesOutstanding=" + mRequestMessagesOutstanding);
        }
synchronized (mRequestList) {
int count = mRequestList.size();
pw.println(" mRequestList count=" + count);
for (int i = 0; i < count; i++) {







