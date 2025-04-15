/*Increase limit of bulk SMS to 500

The maximum number of bulk SMS is limited by the number of SMS sent in
a checking period without user permit. Based on operator requests this
limit is increased from 100 to 500.*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SMSDispatcher.java b/telephony/java/com/android/internal/telephony/SMSDispatcher.java
//Synthetic comment -- index ca526a5..e214328 100644

//Synthetic comment -- @@ -72,7 +72,7 @@
private static final int DEFAULT_SMS_CHECK_PERIOD = 3600000;

/** Default number of SMS sent in checking period without user permit */
    private static final int DEFAULT_SMS_MAX_COUNT = 100;

/** Default timeout for SMS sent query */
private static final int DEFAULT_SMS_TIMOUEOUT = 6000;







