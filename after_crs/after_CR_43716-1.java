/*Check SmsTracker's mSentIntent for null

When SmsManager's sendMultipartTextMessage is invoked with null, the SmsTracker
object is initialized with mSentIntent as null. This causes NullPointerException
in Fuzzer tests.

Change-Id:I20895b4b1d3c1d38274fc2f9f75ad773f76fcda0*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SMSDispatcher.java b/src/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 4a6c5dc..a333c4f 100644

//Synthetic comment -- @@ -989,7 +989,9 @@
if (mPendingTrackerCount >= MO_MSG_QUEUE_LIMIT) {
// Deny sending message when the queue limit is reached.
try {
                if (tracker.mSentIntent != null) {
                    tracker.mSentIntent.send(RESULT_ERROR_LIMIT_EXCEEDED);
                }
} catch (CanceledException ex) {
Log.e(TAG, "failed to send back RESULT_ERROR_LIMIT_EXCEEDED");
}







