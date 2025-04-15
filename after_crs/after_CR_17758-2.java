/*Fixing values for Sms.STATUS_PENDING and Sms.STATUS_FAILED

Sms.STATUS_FAILED was defined as 128 and Sms.STATUS_PENDING was
defined as 64. According to 3GPP 23.040 9.2.3.15 the range of TP-ST
values for 'permanent error' is [64, 127] while the range for
'temporary error, still trying' is [32, 63]. The latter range is what
is defined as 'pending'. Values equal or greater than 128 are
reserved and should not be used.

Apparently the values of these constants were mistakenly left-shifted
one bit, hence Sms.STATUS_PENDING is changed into 32 and
Sms.STATUS_FAILED is changed into 64.*/




//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java b/core/java/android/provider/Telephony.java
//Synthetic comment -- index 803446f..1a35112 100644

//Synthetic comment -- @@ -115,8 +115,8 @@

public static final int STATUS_NONE = -1;
public static final int STATUS_COMPLETE = 0;
        public static final int STATUS_PENDING = 32;
        public static final int STATUS_FAILED = 64;

/**
* The subject of the message, if present







