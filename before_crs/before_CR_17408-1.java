/*Telephony: Make sms retry delay configurable

The delay sms dispathcer waits after receiving FAIL_RETRY answer from ril before sending next message can be configured by setting persist.radio.sms_retry_delay value in milliseconds.

Change-Id:I4ed35b2d9c7a3590a300cfb079629fcec4ea40bb*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index ca526a5..f44e52f 100644

//Synthetic comment -- @@ -127,8 +127,13 @@

/** Maximum number of times to retry sending a failed SMS. */
private static final int MAX_SEND_RETRIES = 3;
    /** Delay before next send attempt on a failed SMS, in milliseconds. */
    private static final int SEND_RETRY_DELAY = 2000;
/** single part SMS */
private static final int SINGLE_PART_SMS = 1;
/** Message sending queue limit */
//Synthetic comment -- @@ -498,7 +503,7 @@
//       implementations this retry is handled by the baseband.
tracker.mRetryCount++;
Message retryMsg = obtainMessage(EVENT_SEND_RETRY, tracker);
                sendMessageDelayed(retryMsg, SEND_RETRY_DELAY);
} else if (tracker.mSentIntent != null) {
int error = RESULT_ERROR_GENERIC_FAILURE;









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/TelephonyProperties.java b/telephony/java/com/android/internal/telephony/TelephonyProperties.java
//Synthetic comment -- index a113787..e495d1d 100644

//Synthetic comment -- @@ -147,4 +147,7 @@
* when there is a radio technology change.
*/
static final String PROPERTY_RESET_ON_RADIO_TECH_CHANGE = "persist.radio.reset_on_switch";
}







