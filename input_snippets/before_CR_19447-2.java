
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
SERVICE_OPTION_NOT_SUBSCRIBED,
SERVICE_OPTION_OUT_OF_ORDER,
NSAPI_IN_USE,
PROTOCOL_ERRORS,
REGISTRATION_FAIL,
GPRS_REGISTRATION_FAIL,

RADIO_NOT_AVAILABLE;

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
return "Data Services Out of Order";
case NSAPI_IN_USE:
return "NSAPI in use";
case PROTOCOL_ERRORS:
return "Protocol Errors";
case REGISTRATION_FAIL:
return "Unknown Data Error";
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

    protected abstract FailCause getFailCauseFromRequest(int rilCause);

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
                            cause = getFailCauseFromRequest(rilFailCause);
}
// Transition to inactive but send notifications after
// we've entered the mInactive state.

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



private static final String LOG_TAG = "CDMA";

    /** Fail cause of last Data Call activate from RIL_LastDataCallActivateFailCause */
    private final static int PS_NET_DOWN_REASON_OPERATOR_DETERMINED_BARRING         = 8;
    private final static int PS_NET_DOWN_REASON_AUTH_FAILED                         = 29;
    private final static int PS_NET_DOWN_REASON_OPTION_NOT_SUPPORTED                = 32;
    private final static int PS_NET_DOWN_REASON_OPTION_UNSUBSCRIBED                 = 33;


// ***** Constructor
private CdmaDataConnection(CDMAPhone phone, String name) {
super(phone, name);
}

@Override
    protected FailCause getFailCauseFromRequest(int rilCause) {
        FailCause cause;

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
if ((NULL_IP.equals(domainNameServers[0])
&& NULL_IP.equals(domainNameServers[1])

//<End of snippet n. 1>










//<Beginning of snippet n. 2>



private static final String LOG_TAG = "GSM";

    /** Fail cause of last PDP activate, from RIL_LastPDPActivateFailCause */
    private static final int PDP_FAIL_OPERATOR_BARRED = 0x08;
    private static final int PDP_FAIL_INSUFFICIENT_RESOURCES = 0x1A;
    private static final int PDP_FAIL_MISSING_UKNOWN_APN = 0x1B;
    private static final int PDP_FAIL_UNKNOWN_PDP_ADDRESS_TYPE = 0x1C;
    private static final int PDP_FAIL_USER_AUTHENTICATION = 0x1D;
    private static final int PDP_FAIL_ACTIVATION_REJECT_GGSN = 0x1E;
    private static final int PDP_FAIL_ACTIVATION_REJECT_UNSPECIFIED = 0x1F;
    private static final int PDP_FAIL_SERVICE_OPTION_NOT_SUPPORTED = 0x20;
    private static final int PDP_FAIL_SERVICE_OPTION_NOT_SUBSCRIBED = 0x21;
    private static final int PDP_FAIL_SERVICE_OPTION_OUT_OF_ORDER = 0x22;
    private static final int PDP_FAIL_NSAPI_IN_USE      = 0x23;
    private static final int PDP_FAIL_PROTOCOL_ERRORS   = 0x6F;
    private static final int PDP_FAIL_ERROR_UNSPECIFIED = 0xffff;

    private static final int PDP_FAIL_REGISTRATION_FAIL = -1;
    private static final int PDP_FAIL_GPRS_REGISTRATION_FAIL = -2;

//***** Instance Variables
private ApnSetting apn;

}

@Override
    protected FailCause getFailCauseFromRequest(int rilCause) {
        FailCause cause;

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
if (NULL_IP.equals(dnsServers[0]) && NULL_IP.equals(dnsServers[1])
&& !((GSMPhone) phone).isDnsCheckDisabled()) {

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


onEnableApn(apnTypeToId(mRequestedApnType), DISABLED);
return;
}
                if (mReregisterOnReconnectFailure) {
// We've re-registerd once now just retry forever.
mRetryMgr.retryForeverUsingLastTimeout();
} else {

//<End of snippet n. 3>








