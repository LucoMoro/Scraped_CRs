/*Telephony: Add helper functions to IccCardConstants

Change-Id:I1a666973dd23f510a0cce60ef7db462728717fc0*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardConstants.java b/telephony/java/com/android/internal/telephony/IccCardConstants.java
//Synthetic comment -- index 20439bc..8bce673 100644

//Synthetic comment -- @@ -22,6 +22,8 @@

/* The extra data for broacasting intent INTENT_ICC_STATE_CHANGE */
public static final String INTENT_KEY_ICC_STATE = "ss";
    /* UNKNOWN means the ICC state is unknown */
    static public final String INTENT_VALUE_ICC_UNKNOWN = "UNKNOWN";
/* NOT_READY means the ICC interface is not ready (eg, radio is off or powering on) */
public static final String INTENT_VALUE_ICC_NOT_READY = "NOT_READY";
/* ABSENT means ICC is missing */







