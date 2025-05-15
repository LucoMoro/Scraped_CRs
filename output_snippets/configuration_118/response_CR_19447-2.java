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
    SERVICE_OPTION_NOT_SUPPORTED,
    SERVICE_OPTION_OUT_OF_ORDER,
    NSAPI_IN_USE,
    INSUFFICIENT_RESOURCES,
    REGISTRATION_FAIL,
    GPRS_REGISTRATION_FAIL,
    UNKNOWN;
}

public boolean isPermanentFail() {
    return (this == FailCause.OPERATOR_BARRED) || 
           (this == FailCause.MISSING_UNKNOWN_APN) ||
           (this == FailCause.UNKNOWN_PDP_ADDRESS) || 
           (this == FailCause.USER_AUTHENTICATION) ||
           (this == FailCause.ACTIVATION_REJECT_GGSN) || 
           (this == FailCause.ACTIVATION_REJECT_UNSPECIFIED) ||
           (this == FailCause.PROTOCOL_ERRORS);
}

public boolean isEventLoggable() {
    return (this == FailCause.OPERATOR_BARRED) || 
           (this == FailCause.INSUFFICIENT_RESOURCES) ||
           (this == FailCause.UNKNOWN_PDP_ADDRESS) || 
           (this == FailCause.USER_AUTHENTICATION) ||
           (this == FailCause.ACTIVATION_REJECT_GGSN) || 
           (this == FailCause.ACTIVATION_REJECT_UNSPECIFIED) ||
           (this == FailCause.SERVICE_OPTION_NOT_SUBSCRIBED) ||
           (this == FailCause.SERVICE_OPTION_NOT_SUPPORTED) ||
           (this == FailCause.SERVICE_OPTION_OUT_OF_ORDER) || 
           (this == FailCause.NSAPI_IN_USE) ||
           (this == FailCause.PROTOCOL_ERRORS);
}

@Override
public String toString() {
    switch (this) {
        case NSAPI_IN_USE:
            return "NSAPI in use";
        case PROTOCOL_ERRORS:
            return "Protocol Errors";
        case REGISTRATION_FAIL:
            return "Unknown Data Error";
        default:
            return "Data Services Out of Order";
    }
}

// ***** Event codes for driving the state machine
protected static final int EVENT_RESET = 1;
protected static final int EVENT_CONNECT = 2;
protected static final String NULL_IP = "0.0.0.0";
Object userData;

//***** Abstract methods
protected abstract FailCause getFailCauseFromRequest(int rilCause);
protected abstract boolean isDnsOk(String[] domainNameServers);
protected abstract void log(String s);

clearSettings();

setDbg(false);
addState(mDefaultState);
addState(mInactiveState, mDefaultState);
if (DBG) log("DataConnection constructor X");

// Handling last failure cause
if (DBG) log("DcActivatingState msg.what=EVENT_GET_LAST_FAIL_DONE");
if (ar.exception == null) {
    int rilFailCause = ((int[]) (ar.result))[0];
    cause = getFailCauseFromRequest(rilFailCause);
}
// Transition to inactive but send notifications after we've entered the mInactive state.
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private static final String LOG_TAG = "CDMA";

// ***** Constructor
private CdmaDataConnection(CDMAPhone phone, String name) {
    super(phone, name);
}

@Override
protected FailCause getFailCauseFromRequest(int rilCause) {
    FailCause cause;
    switch (rilCause) {
        case 8:
            cause = FailCause.OPERATOR_BARRED;
            break;
        case 29:
            cause = FailCause.USER_AUTHENTICATION;
            break;
        case 32:
            cause = FailCause.SERVICE_OPTION_NOT_SUPPORTED;
            break;
        case 33:
            cause = FailCause.SERVICE_OPTION_NOT_SUBSCRIBED;
            break;
        default:
            cause = FailCause.UNKNOWN;
    }
    return cause;
}

@Override
protected boolean isDnsOk(String[] domainNameServers) {
    return (domainNameServers.length > 1) && 
           (NULL_IP.equals(domainNameServers[0]) && NULL_IP.equals(domainNameServers[1]));
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
private static final String LOG_TAG = "GSM";

// ***** Instance Variables
private ApnSetting apn;

@Override
protected FailCause getFailCauseFromRequest(int rilCause) {
    FailCause cause;
    switch (rilCause) {
        case 0x08:
            cause = FailCause.OPERATOR_BARRED;
            break;
        case 0x1A:
            cause = FailCause.INSUFFICIENT_RESOURCES;
            break;
        case 0x1B:
            cause = FailCause.MISSING_UNKNOWN_APN;
            break;
        case 0x1C:
            cause = FailCause.UNKNOWN_PDP_ADDRESS;
            break;
        case 0x1D:
            cause = FailCause.USER_AUTHENTICATION;
            break;
        case 0x1E:
            cause = FailCause.ACTIVATION_REJECT_GGSN;
            break;
        case 0x1F:
            cause = FailCause.ACTIVATION_REJECT_UNSPECIFIED;
            break;
        case 0x20:
            cause = FailCause.SERVICE_OPTION_NOT_SUPPORTED;
            break;
        case 0x21:
            cause = FailCause.SERVICE_OPTION_NOT_SUBSCRIBED;
            break;
        case 0x22:
            cause = FailCause.SERVICE_OPTION_OUT_OF_ORDER;
            break;
        case 0x23:
            cause = FailCause.NSAPI_IN_USE;
            break;
        case 0x6F:
            cause = FailCause.PROTOCOL_ERRORS;
            break;
        case 0xffff:
            cause = FailCause.UNKNOWN;
            break;
        case -1:
            cause = FailCause.REGISTRATION_FAIL;
            break;
        case -2:
            cause = FailCause.GPRS_REGISTRATION_FAIL;
            break;
        default:
            cause = FailCause.UNKNOWN;
    }
    return cause;
}

@Override
protected boolean isDnsOk(String[] domainNameServers) {
    return (domainNameServers.length > 1) && 
           (NULL_IP.equals(domainNameServers[0]) && NULL_IP.equals(domainNameServers[1]) && 
           !((GSMPhone) phone).isDnsCheckDisabled());
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
onEnableApn(apnTypeToId(mRequestedApnType), DISABLED);
return;
if (mReregisterOnReconnectFailure) {
    mRetryMgr.retryForeverUsingLastTimeout();
} else {
//<End of snippet n. 3>