/*frameworks/base/telephony: Release wakelock on RIL request send error

Android telephony does not release the partial wakelock right away if
there is an error in sending the RIL request. The wake lock is released
only after EVENT_WAKE_LOCK_TIMEOUT occurs that prevents the phone to go
in power collpase. The change is to release the wake lock as soon as the
error in send is detected.

Change-Id:I0fd157b9a9cdaa791a599150a721060bb3681a68*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index d8e313a7..d0e97c14 100644

//Synthetic comment -- @@ -359,6 +359,8 @@
rr.onError(GENERIC_FAILURE, null);
rr.release();
}
                    } finally {
                        releaseWakeLockIfDone();
}

if (!alreadySubtracted) {







