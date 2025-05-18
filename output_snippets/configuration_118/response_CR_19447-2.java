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
    INSUFFICIENT_RESOURCES,
    REGISTRATION_FAIL,
    GPRS_REGISTRATION_FAIL,
    UNKNOWN,
    SERVICE_OPTION_NOT_SUPPORTED // Added missing Enum
}

public boolean isPermanentFail() {
    return (this == FailCause.OPERATOR_BARRED) || (this == FailCause.MISSING_UNKNOWN_APN) ||
           (this == FailCause.UNKNOWN_PDP_ADDRESS) || (this == FailCause.USER_AUTHENTICATION) ||
           (this == FailCause.ACTIVATION_REJECT_GGSN) || (this == FailCause.ACTIVATION_REJECT_UNSPECIFIED) ||
           (this == FailCause.PROTOCOL_ERRORS);
}

public boolean isEventLoggable() {
    return (this == FailCause.OPERATOR_BARRED) || (this == FailCause.INSUFFICIENT_RESOURCES) ||
           (this == FailCause.UNKNOWN_PDP_ADDRESS) || (this == FailCause.USER_AUTHENTICATION) ||
           (this == FailCause.ACTIVATION_REJECT_GGSN) || (this == FailCause.ACTIVATION_REJECT_UNSPECIFIED) ||
           (this == FailCause.SERVICE_OPTION_NOT_SUBSCRIBED) || (this == FailCause.SERVICE_OPTION_OUT_OF_ORDER) ||
           (this == FailCause.NSAPI_IN_USE) || (this == FailCause.PROTOCOL_ERRORS);
}

@Override
public String toString() {
    switch (this) {
        case OPERATOR_BARRED: return "Operator Barred";
        case MISSING_UNKNOWN_APN: return "Missing Unknown APN";
        case UNKNOWN_PDP_ADDRESS: return "Unknown PDP Address";
        case USER_AUTHENTICATION: return "User Authentication";
        case ACTIVATION_REJECT_GGSN: return "Activation Reject GGSN";
        case ACTIVATION_REJECT_UNSPECIFIED: return "Activation Reject Unspecified";
        case PROTOCOL_ERRORS: return "Protocol Errors";
        case SERVICE_OPTION_NOT_SUBSCRIBED: return "Service Option Not Subscribed";
        case SERVICE_OPTION_OUT_OF_ORDER: return "Service Option Out of Order";
        case NSAPI_IN_USE: return "NSAPI in Use";
        case INSUFFICIENT_RESOURCES: return "Insufficient Resources";
        case REGISTRATION_FAIL: return "Registration Fail";
        case GPRS_REGISTRATION_FAIL: return "GPRS Registration Fail";
        case UNKNOWN: 
        default: return "Unknown Fail Cause";
    }
}

protected static final int EVENT_RESET = 1;
protected static final int EVENT_CONNECT = 2;
protected static final String NULL_IP = "0.0.0.0";
Object userData;

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
if (DBG) log("DcActivatingState msg.what=EVENT_GET_LAST_FAIL_DONE");
if (ar.exception == null) {
    int rilFailCause = ((int[]) (ar.result))[0];
    cause = getFailCauseFromRequest(rilFailCause);
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
    String tmpFailures = SystemProperties.get("ro.net.data_tmpfailures", "");
    FailCause cause;

    if (!tmpFailures.isEmpty()) {
        String[] causes = tmpFailures.split(",");
        if (causes.length > rilCause) {
            return FailCause.valueOf(causes[rilCause]);
        }
    }

    switch (rilCause) {
        case PS_NET_DOWN_REASON_OPERATOR_DETERMINED_BARRING:
            cause = FailCause.OPERATOR_BARRED;
            break;
        case PS_NET_DOWN_REASON_AUTH_FAILED:
            cause = FailCause.USER_AUTHENTICATION;
            break;
        case PS_NET_DOWN_REASON_OPTION_NOT_SUPPORTED:
            cause = FailCause.SERVICE_OPTION_NOT_SUPPORTED;
            break;
        case PS_NET_DOWN_REASON_OPTION_UNSUBSCRIBED:
            cause = FailCause.SERVICE_OPTION_NOT_SUBSCRIBED;
            break;
        default:
            cause = FailCause.UNKNOWN;
    }
    return cause;
}

@Override
protected boolean isDnsOk(String[] domainNameServers) {
    return (NULL_IP.equals(domainNameServers[0]) && NULL_IP.equals(domainNameServers[1]));
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
private static final String LOG_TAG = "GSM";

//***** Instance Variables
private ApnSetting apn;

@Override
protected FailCause getFailCauseFromRequest(int rilCause) {
    String tmpFailures = SystemProperties.get("ro.net.data_tmpfailures", "");
    FailCause cause;

    if (!tmpFailures.isEmpty()) {
        String[] causes = tmpFailures.split(",");
        if (causes.length > rilCause) {
            return FailCause.valueOf(causes[rilCause]);
        }
    }

    switch (rilCause) {
        case PDP_FAIL_OPERATOR_BARRED:
            cause = FailCause.OPERATOR_BARRED;
            break;
        case PDP_FAIL_INSUFFICIENT_RESOURCES:
            cause = FailCause.INSUFFICIENT_RESOURCES;
            break;
        case PDP_FAIL_MISSING_UKNOWN_APN:
            cause = FailCause.MISSING_UNKNOWN_APN;
            break;
        case PDP_FAIL_UNKNOWN_PDP_ADDRESS_TYPE:
            cause = FailCause.UNKNOWN_PDP_ADDRESS;
            break;
        case PDP_FAIL_USER_AUTHENTICATION:
            cause = FailCause.USER_AUTHENTICATION;
            break;
        case PDP_FAIL_ACTIVATION_REJECT_GGSN:
            cause = FailCause.ACTIVATION_REJECT_GGSN;
            break;
        case PDP_FAIL_ACTIVATION_REJECT_UNSPECIFIED:
            cause = FailCause.ACTIVATION_REJECT_UNSPECIFIED;
            break;
        case PDP_FAIL_SERVICE_OPTION_OUT_OF_ORDER:
            cause = FailCause.SERVICE_OPTION_OUT_OF_ORDER;
            break;
        case PDP_FAIL_SERVICE_OPTION_NOT_SUPPORTED:
            cause = FailCause.SERVICE_OPTION_NOT_SUPPORTED;
            break;
        case PDP_FAIL_SERVICE_OPTION_NOT_SUBSCRIBED:
            cause = FailCause.SERVICE_OPTION_NOT_SUBSCRIBED;
            break;
        case PDP_FAIL_NSAPI_IN_USE:
            cause = FailCause.NSAPI_IN_USE;
            break;
        case PDP_FAIL_PROTOCOL_ERRORS:
            cause = FailCause.PROTOCOL_ERRORS;
            break;
        case PDP_FAIL_ERROR_UNSPECIFIED:
            cause = FailCause.UNKNOWN;
            break;
        case PDP_FAIL_REGISTRATION_FAIL:
            cause = FailCause.REGISTRATION_FAIL;
            break;
        case PDP_FAIL_GPRS_REGISTRATION_FAIL:
            cause = FailCause.GPRS_REGISTRATION_FAIL;
            break;
        default:
            cause = FailCause.UNKNOWN;
    }
    return cause;
}

@Override
protected boolean isDnsOk(String[] domainNameServers) {
    return (NULL_IP.equals(domainNameServers[0]) && NULL_IP.equals(domainNameServers[1]) && !((GSMPhone) phone).isDnsCheckDisabled());
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
onEnableApn(apnTypeToId(mRequestedApnType), DISABLED);
return;
if (mReregisterOnReconnectFailure) {
    mRetryMgr.retryForeverUsingLastTimeout();
} else {
//<End of snippet n. 3>