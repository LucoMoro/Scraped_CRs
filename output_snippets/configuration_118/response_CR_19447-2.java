//<Beginning of snippet n. 0>

import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemProperties;
import android.util.EventLog;

public enum FailCause {
    OPERATOR_BARRED,
    MISSING_UNKNOWN_APN,
    UNKNOWN_PDP_ADDRESS,
    USER_AUTHENTICATION,
    ACTIVATION_REJECT_GGSN,
    ACTIVATION_REJECT_UNSPECIFIED,
    PROTOCOL_ERRORS,
    SERVICE_OPTION_NOT_SUBSCRIBED,
    SERVICE_OPTION_OUT_OF_ORDER,
    NSAPI_IN_USE,
    GPRS_REGISTRATION_FAIL,
    REGISTRATION_FAIL,
    UNKNOWN;

    public boolean isPermanentFail() {
        return (this == OPERATOR_BARRED) || (this == MISSING_UNKNOWN_APN) ||
               (this == UNKNOWN_PDP_ADDRESS) || (this == USER_AUTHENTICATION) ||
               (this == ACTIVATION_REJECT_GGSN) || (this == ACTIVATION_REJECT_UNSPECIFIED) ||
               (this == PROTOCOL_ERRORS);
    }

    public boolean isEventLoggable() {
        return (this == OPERATOR_BARRED) || (this == UNKNOWN_PDP_ADDRESS) ||
               (this == USER_AUTHENTICATION) || (this == ACTIVATION_REJECT_GGSN) ||
               (this == ACTIVATION_REJECT_UNSPECIFIED) || (this == SERVICE_OPTION_NOT_SUBSCRIBED) ||
               (this == SERVICE_OPTION_OUT_OF_ORDER) || (this == NSAPI_IN_USE) ||
               (this == PROTOCOL_ERRORS);
    }

    public static FailCause fromRilCause(int rilCause) {
        switch (rilCause) {
            case 8:  return OPERATOR_BARRED;
            case 29: return USER_AUTHENTICATION;
            case 33: return SERVICE_OPTION_NOT_SUBSCRIBED;
            case 32: return SERVICE_OPTION_NOT_SUBSCRIBED; // Corrected repeated value
            default: return UNKNOWN;
        }
    }

    public static FailCause getFailCauseFromSystemProperty() {
        String tmpFailures = SystemProperties.get("ro.net.data_tmpfailures");
        if (tmpFailures != null) {
            if (tmpFailures.contains("operator_barred")) {
                return FailCause.OPERATOR_BARRED;
            } else if (tmpFailures.contains("missing_unknown_apn")) {
                return FailCause.MISSING_UNKNOWN_APN;
            } else if (tmpFailures.contains("unknown_pdp_address")) {
                return FailCause.UNKNOWN_PDP_ADDRESS;
            }
            // Add additional parsing logic for other FailCause enums as needed
        }
        return UNKNOWN; 
    }
}

// ***** Event codes for driving the state machine
protected static final int EVENT_RESET = 1;
protected static final int EVENT_CONNECT = 2;
protected static final String NULL_IP = "0.0.0.0";
Object userData;

//***** Abstract methods
public abstract String toString();

protected abstract void onConnect(ConnectionParams cp);

protected abstract FailCause getFailCauseFromRequest(int rilCause);

protected abstract boolean isDnsOk(String[] domainNameServers);

protected abstract void log(String s);

clearSettings();

setDbg(false);
addState(mDefaultState);
addState(mInactiveState, mDefaultState);
if (DBG) log("DataConnection constructor X");

public void handleFailure(AsyncResult ar) {
    if (DBG) log("DcActivatingState msg.what=EVENT_GET_LAST_FAIL_DONE");
    if (ar.exception == null) {
        int rilFailCause = ((int[]) (ar.result))[0];
        FailCause cause = FailCause.fromRilCause(rilFailCause);
        log("Failure Cause: " + cause);
        if (cause.isEventLoggable()) {
            EventLog.writeEvent(EventLogTags.DATA_CONNECTION_FAILURE, cause.ordinal());
        }
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

private static final String LOG_TAG = "CDMA";

// ***** Constructor
private CdmaDataConnection(CDMAPhone phone, String name) {
    super(phone, name);
}

@Override
protected FailCause getFailCauseFromRequest(int rilCause) {
    return FailCause.fromRilCause(rilCause);
}

@Override
protected boolean isDnsOk(String[] domainNameServers) {
    return (NULL_IP.equals(domainNameServers[0]) && NULL_IP.equals(domainNameServers[1]));
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

private static final String LOG_TAG = "GSM";

// ***** Instance Variables
private ApnSetting apn;

@Override
protected FailCause getFailCauseFromRequest(int rilCause) {
    return FailCause.fromRilCause(rilCause);
}

@Override
protected boolean isDnsOk(String[] domainNameServers) {
    return (NULL_IP.equals(domainNameServers[0]) && NULL_IP.equals(domainNameServers[1]) &&
            !((GSMPhone) phone).isDnsCheckDisabled());
}

//<End of snippet n. 2>

//<Beginning of snippet n. 3>

onEnableApn(apnTypeToId(mRequestedApnType), DISABLED);
return;
}
if (mReregisterOnReconnectFailure) {
    mRetryMgr.retryForeverUsingLastTimeout();
} else {
//<End of snippet n. 3>