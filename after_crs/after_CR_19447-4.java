/*Customizable PDP reactivation behavior

Enable customization of PDP reactivation behavior, specifically
temporary failure causes, based on operator requests. The failure
cause implementation is moved from GsmDataConnection and
CdmaDataConnection into the FailCause enum in DataConnection in order
to centralize the FailCause handling within the FailCause enum. The
temporary failure causes can be customized through the system property
'ro.net.data_tmpfailures'.

Change-Id:I7bffc01dfa544593922ff8fab6046f5207be5f4f*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DataConnection.java b/telephony/java/com/android/internal/telephony/DataConnection.java
//Synthetic comment -- index 4619899..435e6f1 100644

//Synthetic comment -- @@ -29,9 +29,10 @@
import android.net.ProxyProperties;
import android.os.AsyncResult;
import android.os.Message;
import android.util.Log;

import android.os.SystemProperties;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//Synthetic comment -- @@ -115,6 +116,8 @@
SERVICE_OPTION_NOT_SUBSCRIBED(0x21),
SERVICE_OPTION_OUT_OF_ORDER(0x22),
NSAPI_IN_USE(0x23),
        REGULAR_DEACTIVATION(0x24),
        NETWORK_FAILURE(0x26),
ONLY_IPV4_ALLOWED(0x32),
ONLY_IPV6_ALLOWED(0x33),
ONLY_SINGLE_BEARER_ALLOWED(0x34),
//Synthetic comment -- @@ -154,6 +157,7 @@
}

public boolean isPermanentFail() {
            if (isTemporaryFail()) return false;
return (this == OPERATOR_BARRED) || (this == MISSING_UNKNOWN_APN) ||
(this == UNKNOWN_PDP_ADDRESS_TYPE) || (this == USER_AUTHENTICATION) ||
(this == SERVICE_OPTION_NOT_SUPPORTED) ||
//Synthetic comment -- @@ -161,6 +165,12 @@
(this == PROTOCOL_ERRORS);
}

        private boolean isTemporaryFail() {
            for (FailCause f : tempFailCauses)
                if (this == f) return true;
            return false;
        }

public boolean isEventLoggable() {
return (this == OPERATOR_BARRED) || (this == INSUFFICIENT_RESOURCES) ||
(this == UNKNOWN_PDP_ADDRESS_TYPE) || (this == USER_AUTHENTICATION) ||
//Synthetic comment -- @@ -168,6 +178,7 @@
(this == SERVICE_OPTION_NOT_SUBSCRIBED) ||
(this == SERVICE_OPTION_NOT_SUPPORTED) ||
(this == SERVICE_OPTION_OUT_OF_ORDER) || (this == NSAPI_IN_USE) ||
                    (this == REGULAR_DEACTIVATION) ||(this == NETWORK_FAILURE) ||
(this == PROTOCOL_ERRORS) ||
(this == UNACCEPTABLE_NETWORK_PARAMETER);
}
//Synthetic comment -- @@ -193,6 +204,8 @@
}
}

    public static final String LOG_TAG = "DataConnection";

// ***** Event codes for driving the state machine
protected static final int BASE = Protocol.BASE_DATA_CONNECTION;
protected static final int EVENT_CONNECT = BASE + 0;
//Synthetic comment -- @@ -221,6 +234,8 @@
private int mRefCount;
Object userData;

    protected static ArrayList<FailCause> tempFailCauses = new ArrayList<FailCause>();

//***** Abstract methods
@Override
public abstract String toString();
//Synthetic comment -- @@ -241,6 +256,10 @@
mRetryMgr = rm;
this.cid = -1;

        // Initializing temporary failure causes list
        String settingStr = SystemProperties.get("ro.net.data_tmpfailures");
        configureTempFailureCauses(settingStr);

setDbg(false);
addState(mDefaultState);
addState(mInactiveState, mDefaultState);
//Synthetic comment -- @@ -254,6 +273,26 @@
if (DBG) log("DataConnection constructor X");
}

    protected void configureTempFailureCauses(String configStr) {
        tempFailCauses.clear();
        if (!TextUtils.isEmpty(configStr)) {
            String strTempFailCausesArray[] = configStr.split(",");
            if (strTempFailCausesArray.length > 0) {
                for (String s : strTempFailCausesArray) {
                    try {
                        s = s.trim();
                        int rilCause = Integer.parseInt(s);
                        FailCause failCause = FailCause.fromInt(rilCause);
                        if (failCause != FailCause.UNKNOWN)
                            tempFailCauses.add(failCause);
                    } catch (NumberFormatException e) {
                        Log.e(LOG_TAG, "Temporary failure causes was not customized correctly.");
                    }
                }
            }
        }
    }

/**
* TearDown the data connection.
*








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 963db2c..28c1073 100644

//Synthetic comment -- @@ -1549,7 +1549,7 @@
mPhone.notifyDataConnection(Phone.REASON_APN_FAILED, apnContext.getApnType());
return;
}
                if ((mReregisterOnReconnectFailure || !lastFailCauseCode.isPermanentFail())) {
// We've re-registerd once now just retry forever.
apnContext.getDataConnection().retryForeverUsingLastTimeout();
} else {







