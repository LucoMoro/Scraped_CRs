/*frameworks/base/telephony: Release wakelock on RIL request send error

Android telephony does not release the partial wakelock right away if
there is an error in sending the RIL request. The wake lock is released
only after EVENT_WAKE_LOCK_TIMEOUT occurs that prevents the phone to go
in power collpase. The change is to release the wake lock as soon as the
error in send is detected.

Change-Id:Ia39a4b9ac12f4064e301a65abfd26409d49babe1*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 2f7aa21..af483db 100644

//Synthetic comment -- @@ -360,6 +360,11 @@
rr.onError(GENERIC_FAILURE, null);
rr.release();
}
                    } finally {
                        // Note: We are "Done" only if there are no outstanding
                        // requests or replies. Thus this code path will only release
                        // the wake lock on errors.
                        releaseWakeLockIfDone();
}

if (!alreadySubtracted) {
//Synthetic comment -- @@ -2044,6 +2049,12 @@
send(RILRequest rr) {
Message msg;

        if (mSocket == null) {
            rr.onError(RADIO_NOT_AVAILABLE, null);
            rr.release();
            return;
        }

msg = mSender.obtainMessage(EVENT_SEND, rr);

acquireWakeLock();







