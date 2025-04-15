/*Telephony: Add helper functions to IccCardConstants

Change-Id:I1a666973dd23f510a0cce60ef7db462728717fc0*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardConstants.java b/telephony/java/com/android/internal/telephony/IccCardConstants.java
//Synthetic comment -- index 20439bc..5c26501 100644

//Synthetic comment -- @@ -22,6 +22,8 @@

/* The extra data for broacasting intent INTENT_ICC_STATE_CHANGE */
public static final String INTENT_KEY_ICC_STATE = "ss";
    /* UNKNOWN means the ICC state is unknown */
    static public final String INTENT_VALUE_ICC_UNKNOWN = "UNKNOWN";
/* NOT_READY means the ICC interface is not ready (eg, radio is off or powering on) */
public static final String INTENT_VALUE_ICC_NOT_READY = "NOT_READY";
/* ABSENT means ICC is missing */
//Synthetic comment -- @@ -69,5 +71,32 @@
|| (this == NETWORK_LOCKED) || (this == READY)
|| (this == PERM_DISABLED));
}

        public String getIntentString() {
            switch (this) {
                case ABSENT: return INTENT_VALUE_ICC_ABSENT;
                case PIN_REQUIRED: return INTENT_VALUE_ICC_LOCKED;
                case PUK_REQUIRED: return INTENT_VALUE_ICC_LOCKED;
                case NETWORK_LOCKED: return INTENT_VALUE_ICC_LOCKED;
                case READY: return INTENT_VALUE_ICC_READY;
                case NOT_READY: return INTENT_VALUE_ICC_NOT_READY;
                case PERM_DISABLED: return INTENT_VALUE_ICC_LOCKED;
                default: return INTENT_VALUE_ICC_UNKNOWN;
            }
        }

        /**
         * Locked state have a reason (PIN, PUK, NETWORK, PERM_DISABLED)
         * @return reason
         */
        public String getReason() {
            switch (this) {
                case PIN_REQUIRED: return INTENT_VALUE_LOCKED_ON_PIN;
                case PUK_REQUIRED: return INTENT_VALUE_LOCKED_ON_PUK;
                case NETWORK_LOCKED: return INTENT_VALUE_LOCKED_NETWORK;
                case PERM_DISABLED: return INTENT_VALUE_ABSENT_ON_PERM_DISABLED;
                default: return null;
           }
        }
}
}







