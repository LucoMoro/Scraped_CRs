//<Beginning of snippet n. 0>


import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemProperties;
import android.util.EventLog;

/**
*        EVENT_GET_LAST_FAIL_DONE,
*        EVENT_DEACTIVATE_DONE.
*     }
 *   ++ # mInactiveState 
*        e(doNotifications)
*        x(clearNotifications) {
*            EVENT_RESET { notifiyDisconnectCompleted }.
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

    public static FailCause getFailCauseFromRequest(int rilCause) {
        switch (rilCause) {
            case 8: // PS_NET_DOWN_REASON_OPERATOR_DETERMINED_BARRING
                return OPERATOR_BARRED;
            case 29: // PS_NET_DOWN_REASON_AUTH_FAILED
                return USER_AUTHENTICATION;
            case 32: // PS_NET_DOWN_REASON_OPTION_NOT_SUPPORTED
                return SERVICE_OPTION_NOT_SUPPORTED;
            case 33: // PS_NET_DOWN_REASON_OPTION_UNSUBSCRIBED
                return SERVICE_OPTION_NOT_SUBSCRIBED;
            case 0x08: // PDP_FAIL_OPERATOR_BARRED
                return OPERATOR_BARRED;
            case 0x1A: // PDP_FAIL_INSUFFICIENT_RESOURCES
                return INSUFFICIENT_RESOURCES;
            case 0x1B: // PDP_FAIL_MISSING_UKNOWN_APN
                return MISSING_UNKNOWN_APN;
            case 0x1C: // PDP_FAIL_UNKNOWN_PDP_ADDRESS_TYPE
                return UNKNOWN_PDP_ADDRESS;
            case 0x1D: // PDP_FAIL_USER_AUTHENTICATION
                return USER_AUTHENTICATION;
            case 0x1E: // PDP_FAIL_ACTIVATION_REJECT_GGSN
                return ACTIVATION_REJECT_GGSN;
            case 0x1F: // PDP_FAIL_ACTIVATION_REJECT_UNSPECIFIED
                return ACTIVATION_REJECT_UNSPECIFIED;
            case 0x20: // PDP_FAIL_SERVICE_OPTION_NOT_SUPPORTED
                return SERVICE_OPTION_NOT_SUPPORTED;
            case 0x21: // PDP_FAIL_SERVICE_OPTION_NOT_SUBSCRIBED
                return SERVICE_OPTION_NOT_SUBSCRIBED;
            case 0x22: // PDP_FAIL_SERVICE_OPTION_OUT_OF_ORDER
                return SERVICE_OPTION_OUT_OF_ORDER;
            case 0x23: // PDP_FAIL_NSAPI_IN_USE
                return NSAPI_IN_USE;
            case 0x6F: // PDP_FAIL_PROTOCOL_ERRORS
                return PROTOCOL_ERRORS;
            case -1: // PDP_FAIL_REGISTRATION_FAIL
                return REGISTRATION_FAIL;
            case -2: // PDP_FAIL_GPRS_REGISTRATION_FAIL
                return GPRS_REGISTRATION_FAIL;
            default:
                return UNKNOWN;
        }
    }

    public boolean isPermanentFail() {
        return (this == OPERATOR_BARRED) || (this == MISSING_UNKNOWN_APN) ||
               (this == UNKNOWN_PDP_ADDRESS) || (this == USER_AUTHENTICATION) ||
               (this == ACTIVATION_REJECT_GGSN) || (this == ACTIVATION_REJECT_UNSPECIFIED) ||
               (this == PROTOCOL_ERRORS);
    }

    public boolean isEventLoggable() {
        return (this == OPERATOR_BARRED) || (this == INSUFFICIENT_RESOURCES) ||
               (this == UNKNOWN_PDP_ADDRESS) || (this == USER_AUTHENTICATION) ||
               (this == ACTIVATION_REJECT_GGSN) || (this == ACTIVATION_REJECT_UNSPECIFIED) ||
               (this == SERVICE_OPTION_NOT_SUBSCRIBED) ||
               (this == SERVICE_OPTION_NOT_SUPPORTED) ||
               (this == SERVICE_OPTION_OUT_OF_ORDER) || (this == NSAPI_IN_USE) ||
               (this == PROTOCOL_ERRORS);
    }

    @Override
    public String toString() {
        switch (this) {
            case OPERATOR_BARRED:
                return "Operator Barred";
            case MISSING_UNKNOWN_APN:
                return "Missing Unknown APN";
            case UNKNOWN_PDP_ADDRESS:
                return "Unknown PDP Address";
            case USER_AUTHENTICATION:
                return "User Authentication Failed";
            case ACTIVATION_REJECT_GGSN:
                return "Activation Reject GGSN";
            case ACTIVATION_REJECT_UNSPECIFIED:
                return "Activation Reject Unspecified";
            case PROTOCOL_ERRORS:
                return "Protocol Errors";
            case SERVICE_OPTION_NOT_SUBSCRIBED:
                return "Service Option Not Subscribed";
            case SERVICE_OPTION_OUT_OF_ORDER:
                return "Service Option Out of Order";
            case NSAPI_IN_USE:
                return "NSAPI in Use";
            case GPRS_REGISTRATION_FAIL:
                return "GPRS Registration Fail";
            case REGISTRATION_FAIL:
                return "Registration Fail";
            case UNKNOWN:
            default:
                return "Unknown Error";
        }
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

protected abstract boolean isDnsOk(String[] domainNameServers);

protected abstract void log(String s);

clearSettings();

setDbg(false);
addState(mDefaultState);
addState(mInactiveState, mDefaultState);
if (DBG) log("DataConnection constructor X");
}

/**
* TearDown the data connection.
*
if (DBG) log("DcActivatingState msg.what=EVENT_GET_LAST_FAIL_DONE");
if (ar.exception == null) {
    int rilFailCause = ((int[]) (ar.result))[0];
    cause = FailCause.getFailCauseFromRequest(rilFailCause);
}
// Transition to inactive but send notifications after
// we've entered the mInactive state.

//<End of snippet n. 0>

//<Beginning of snippet n. 1>


private static final String LOG_TAG = "CDMA";

// ***** Constructor
private CdmaDataConnection(CDMAPhone phone, String name) {
    super(phone, name);
}

@Override
protected FailCause getFailCauseFromRequest(int rilCause) {
    return FailCause.getFailCauseFromRequest(rilCause);
}

@Override
protected boolean isDnsOk(String[] domainNameServers) {
    if ((NULL_IP.equals(domainNameServers[0]) && NULL_IP.equals(domainNameServers[1])) {
        // Logic omitted for clarity
        return false;
    }
    return true;
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

private static final String LOG_TAG = "GSM";

//***** Instance Variables
private ApnSetting apn;

@Override
protected FailCause getFailCauseFromRequest(int rilCause) {
    return FailCause.getFailCauseFromRequest(rilCause);
}

@Override
protected boolean isDnsOk(String[] domainNameServers) {
    if (NULL_IP.equals(domainNameServers[0]) && NULL_IP.equals(domainNameServers[1])
            && !((GSMPhone) phone).isDnsCheckDisabled()) {
        // Logic omitted for clarity
        return false;
    }
    return true;
}

//<End of snippet n. 2>

//<Beginning of snippet n. 3>

//<End of snippet n. 3>