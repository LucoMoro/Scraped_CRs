/*Telephony: Move uicc classes into uicc package

Reduce constructor visibility to package where
possible

Change-Id:I80204a2f3dc57cac875abeab390bb9db7a636ff7*/
//Synthetic comment -- diff --git a/packages/WAPPushManager/tests/src/com/android/smspush/unitTests/WapPushTest.java b/packages/WAPPushManager/tests/src/com/android/smspush/unitTests/WapPushTest.java
//Synthetic comment -- index f436cb4..305ee37 100644

//Synthetic comment -- @@ -27,10 +27,10 @@
import android.test.ServiceTestCase;
import android.util.Log;

import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.IWapPushManager;
import com.android.internal.telephony.WapPushManagerParams;
import com.android.internal.telephony.WspTypeDecoder;
import com.android.internal.util.HexDump;
import com.android.smspush.WapPushManager;









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardConstants.java b/telephony/java/com/android/internal/telephony/IccCardConstants.java
//Synthetic comment -- index 20439bc..e846b37 100644

//Synthetic comment -- @@ -22,6 +22,8 @@

/* The extra data for broacasting intent INTENT_ICC_STATE_CHANGE */
public static final String INTENT_KEY_ICC_STATE = "ss";
/* NOT_READY means the ICC interface is not ready (eg, radio is off or powering on) */
public static final String INTENT_VALUE_ICC_NOT_READY = "NOT_READY";
/* ABSENT means ICC is missing */
//Synthetic comment -- @@ -69,5 +71,31 @@
|| (this == NETWORK_LOCKED) || (this == READY)
|| (this == PERM_DISABLED));
}
}
}







