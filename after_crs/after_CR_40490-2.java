/*Telephony: Make sms retry delay configurable

The delay sms dispathcer waits after receiving FAIL_RETRY
answer from ril before sending next message can be configured
by setting persist.radio.sms_retry_delay value in milliseconds.

Change-Id:I68396861fc9ff904cff298954c0f36ba7ecc3ae5*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SMSDispatcher.java b/src/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index 4a6c5dc..d92b396 100644

//Synthetic comment -- @@ -136,8 +136,13 @@

/** Maximum number of times to retry sending a failed SMS. */
private static final int MAX_SEND_RETRIES = 3;
    /** Default for delay before next send attempt on a failed SMS, in milliseconds. */
    private static final int DEFAULT_SEND_RETRY_DELAY = 2000;
    /** Currently used delay for sms sending retries. */
    private static int mSendRetryDelay = SystemProperties.getInt(
            TelephonyProperties.PROPERTY_SMS_RETRY_DELAY,
            DEFAULT_SEND_RETRY_DELAY);

/** single part SMS */
private static final int SINGLE_PART_SMS = 1;
/** Message sending queue limit */
//Synthetic comment -- @@ -416,7 +421,7 @@
//       implementations this retry is handled by the baseband.
tracker.mRetryCount++;
Message retryMsg = obtainMessage(EVENT_SEND_RETRY, tracker);
                sendMessageDelayed(retryMsg, mSendRetryDelay);
} else if (tracker.mSentIntent != null) {
int error = RESULT_ERROR_GENERIC_FAILURE;








