/*frameworks/base/telephony: Release wakelock on RIL request send error

Android telephony does not release the partial wakelock right away if
there is an error in sending the RIL request. The wake lock is released
only after EVENT_WAKE_LOCK_TIMEOUT occurs that prevents the phone to go
in power collpase. The change is to release the wake lock as soon as the
error in send is detected.

Change-Id:Ia39a4b9ac12f4064e301a65abfd26409d49babe1*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 2f7aa21..04b1bd2 100644

//Synthetic comment -- @@ -360,6 +360,8 @@
rr.onError(GENERIC_FAILURE, null);
rr.release();
}
                    } finally {
                        releaseWakeLockIfDone();
}

if (!alreadySubtracted) {







