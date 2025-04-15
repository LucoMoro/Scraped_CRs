/*Telephony: Check SmsTracker's mSentIntent for null

When SmsManager's sendMultipartTextMessage is invoked with null sentIntents,
the SmsTracker object is initialized with mSentIntent as null.
This causes NullPointerException.

Change-Id:I7d5bafcc95cfda517a34739d70afd0101eb690c8*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index a42a267..034fb86 100644

//Synthetic comment -- @@ -288,7 +288,9 @@
if (mSTrackers.isEmpty() == false) {
try {
SmsTracker sTracker = mSTrackers.remove(0);
                    if (sTracker.mSentIntent != null) {
                        sTracker.mSentIntent.send(RESULT_ERROR_LIMIT_EXCEEDED);
                    }
} catch (CanceledException ex) {
Log.e(TAG, "failed to send back RESULT_ERROR_LIMIT_EXCEEDED");
}
//Synthetic comment -- @@ -315,7 +317,9 @@
// Remove the latest one.
try {
SmsTracker sTracker = mSTrackers.remove(mSTrackers.size() - 1);
                    if (sTracker.mSentIntent != null) {
                        sTracker.mSentIntent.send(RESULT_ERROR_LIMIT_EXCEEDED);
                    }
} catch (CanceledException ex) {
Log.e(TAG, "failed to send back RESULT_ERROR_LIMIT_EXCEEDED");
}
//Synthetic comment -- @@ -915,7 +919,9 @@
if (mSTrackers.size() >= MO_MSG_QUEUE_LIMIT) {
// Deny the sending when the queue limit is reached.
try {
                if (tracker.mSentIntent != null) {
                    tracker.mSentIntent.send(RESULT_ERROR_LIMIT_EXCEEDED);
                }
} catch (CanceledException ex) {
Log.e(TAG, "failed to send back RESULT_ERROR_LIMIT_EXCEEDED");
}







