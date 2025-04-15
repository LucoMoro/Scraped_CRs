/*Telephony: Check SmsTracker's mSentIntent for null

When SmsManager's sendMultipartTextMessage is invoked with null sentIntents,
the SmsTracker object is initialized with mSentIntent as null.
This causes NullPointerException.

Change-Id:I545c4a23676ba67f6a5523f15564993f001e48ca*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SMSDispatcher.java b/src/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 4a6c5dc..a333c4f 100644

//Synthetic comment -- @@ -989,7 +989,9 @@
if (mPendingTrackerCount >= MO_MSG_QUEUE_LIMIT) {
// Deny sending message when the queue limit is reached.
try {
                tracker.mSentIntent.send(RESULT_ERROR_LIMIT_EXCEEDED);
} catch (CanceledException ex) {
Log.e(TAG, "failed to send back RESULT_ERROR_LIMIT_EXCEEDED");
}







