/*Telephony: Make sms retry delay configurable

The delay sms dispathcer waits after receiving FAIL_RETRY
answer from ril before sending next message can be configured
by setting persist.radio.sms_retry_delay value in milliseconds.

Change-Id:I00cfb3e85b9f5e2aa5ee07cb75d0e727f9bc2ae8*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/TelephonyProperties.java b/telephony/java/com/android/internal/telephony/TelephonyProperties.java
//Synthetic comment -- index f95e081..122c1b8 100644

//Synthetic comment -- @@ -187,4 +187,7 @@
* Ignore RIL_UNSOL_NITZ_TIME_RECEIVED completely, used for debugging/testing.
*/
static final String PROPERTY_IGNORE_NITZ = "telephony.test.ignore.nitz";

    /** The delay between sms sending retries */
    static final String PROPERTY_SMS_RETRY_DELAY = "persist.radio.sms_retry_delay";
}







