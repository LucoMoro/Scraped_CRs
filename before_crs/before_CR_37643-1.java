/*Telephony: Make sms retry delay configurable

The delay sms dispathcer waits after receiving FAIL_RETRY
answer from ril before sending next message can be configured
by setting persist.radio.sms_retry_delay value in milliseconds.

Change-Id:I00cfb3e85b9f5e2aa5ee07cb75d0e727f9bc2ae8*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index a42a267..2002453 100644

//Synthetic comment -- @@ -125,8 +125,13 @@

/** Maximum number of times to retry sending a failed SMS. */
private static final int MAX_SEND_RETRIES = 3;
    /** Delay before next send attempt on a failed SMS, in milliseconds. */
    private static final int SEND_RETRY_DELAY = 2000;
/** single part SMS */
private static final int SINGLE_PART_SMS = 1;
/** Message sending queue limit */
//Synthetic comment -- @@ -410,7 +415,7 @@
//       implementations this retry is handled by the baseband.
tracker.mRetryCount++;
Message retryMsg = obtainMessage(EVENT_SEND_RETRY, tracker);
                sendMessageDelayed(retryMsg, SEND_RETRY_DELAY);
} else if (tracker.mSentIntent != null) {
int error = RESULT_ERROR_GENERIC_FAILURE;









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/TelephonyProperties.java b/telephony/java/com/android/internal/telephony/TelephonyProperties.java
//Synthetic comment -- index abb4523..e7c1238 100644

//Synthetic comment -- @@ -182,4 +182,7 @@
* in commercial configuration.
*/
static final String PROPERTY_TEST_CSIM = "persist.radio.test-csim";
}







