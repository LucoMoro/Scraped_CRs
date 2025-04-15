/*Telephony: Move uicc classes into uicc package

Reduce constructor visibility to package where
possible

Change-Id:I80204a2f3dc57cac875abeab390bb9db7a636ff7*/




//Synthetic comment -- diff --git a/packages/WAPPushManager/tests/src/com/android/smspush/unitTests/WapPushTest.java b/packages/WAPPushManager/tests/src/com/android/smspush/unitTests/WapPushTest.java
//Synthetic comment -- index f436cb4..305ee37 100644

//Synthetic comment -- @@ -27,10 +27,10 @@
import android.test.ServiceTestCase;
import android.util.Log;

import com.android.internal.telephony.IWapPushManager;
import com.android.internal.telephony.WapPushManagerParams;
import com.android.internal.telephony.WspTypeDecoder;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.util.HexDump;
import com.android.smspush.WapPushManager;









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DctConstants.java b/telephony/java/com/android/internal/telephony/DctConstants.java
//Synthetic comment -- index 79872f3..10ac153 100644

//Synthetic comment -- @@ -90,6 +90,7 @@
public static final int CMD_SET_USER_DATA_ENABLE = BASE + 30;
public static final int CMD_SET_DEPENDENCY_MET = BASE + 31;
public static final int CMD_SET_POLICY_DATA_ENABLE = BASE + 32;
    public static final int EVENT_ICC_CHANGED = BASE + 33;

/***** Constants *****/









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardConstants.java b/telephony/java/com/android/internal/telephony/IccCardConstants.java
//Synthetic comment -- index 20439bc..e846b37 100644

//Synthetic comment -- @@ -22,6 +22,8 @@

/* The extra data for broacasting intent INTENT_ICC_STATE_CHANGE */
public static final String INTENT_KEY_ICC_STATE = "ss";
    /* UNKNOWN means the ICC state is unknown */
    static public final String INTENT_VALUE_ICC_UNKNOWN = "UNKNOWN";
/* NOT_READY means the ICC interface is not ready (eg, radio is off or powering on) */
public static final String INTENT_VALUE_ICC_NOT_READY = "NOT_READY";
/* ABSENT means ICC is missing */
//Synthetic comment -- @@ -69,5 +71,31 @@
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







