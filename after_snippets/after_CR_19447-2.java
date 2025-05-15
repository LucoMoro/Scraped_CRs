
//<Beginning of snippet n. 0>



import android.os.AsyncResult;
import android.os.Message;
import android.util.Log;

import android.os.SystemProperties;
import android.text.TextUtils;
import java.util.ArrayList;
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
        REGULAR_DEACTIVATION,
        NETWORK_FAILURE,
PROTOCOL_ERRORS,
REGISTRATION_FAIL,
GPRS_REGISTRATION_FAIL,

RADIO_NOT_AVAILABLE;

        private static final int RIL_FAILCAUSE_OPERATOR_BARRED                  = 0x08;
        private static final int RIL_FAILCAUSE_INSUFFICIENT_RESOURCES           = 0x1A;
        private static final int RIL_FAILCAUSE_MISSING_UNKNOWN_APN              = 0x1B;
        private static final int RIL_FAILCAUSE_UNKNOWN_PDP_ADDRESS_TYPE         = 0x1C;
        private static final int RIL_FAILCAUSE_USER_AUTHENTICATION              = 0x1D;
        private static final int RIL_FAILCAUSE_ACTIVATION_REJECT_GGSN           = 0x1E;
        private static final int RIL_FAILCAUSE_ACTIVATION_REJECT_UNSPECIFIED    = 0x1F;
        private static final int RIL_FAILCAUSE_SERVICE_OPTION_NOT_SUPPORTED     = 0x20;
        private static final int RIL_FAILCAUSE_SERVICE_OPTION_NOT_SUBSCRIBED    = 0x21;
        private static final int RIL_FAILCAUSE_SERVICE_OPTION_OUT_OF_ORDER      = 0x22;
        private static final int RIL_FAILCAUSE_NSAPI_IN_USE                     = 0x23;
        private static final int RIL_FAILCAUSE_REGULAR_DEACTIVATION             = 0x24;
        private static final int RIL_FAILCAUSE_NETWORK_FAILURE                  = 0x26;
        private static final int RIL_FAILCAUSE_PROTOCOL_ERRORS                  = 0x6F;
        private static final int RIL_FAILCAUSE_ERROR_UNSPECIFIED                = 0xffff;

        private static final int RIL_FAILCAUSE_REGISTRATION_FAIL = -1;
        private static final int RIL_FAILCAUSE_GPRS_REGISTRATION_FAIL = -2;

public boolean isPermanentFail() {
            if (isTemporaryFail()) return false;
return (this == OPERATOR_BARRED) || (this == MISSING_UNKNOWN_APN) ||
(this == UNKNOWN_PDP_ADDRESS) || (this == USER_AUTHENTICATION) ||
(this == ACTIVATION_REJECT_GGSN) || (this == ACTIVATION_REJECT_UNSPECIFIED) ||
(this == PROTOCOL_ERRORS);
}

        private boolean isTemporaryFail() {
            for (FailCause f : tempFailCauses)
                if (this == f) return true;
            return false;
        }

public boolean isEventLoggable() {
return (this == OPERATOR_BARRED) || (this == INSUFFICIENT_RESOURCES) ||
                    (this == MISSING_UNKNOWN_APN) || (this == UNKNOWN_PDP_ADDRESS) ||
                    (this == USER_AUTHENTICATION) || (this == ACTIVATION_REJECT_GGSN) ||
                    (this == ACTIVATION_REJECT_UNSPECIFIED) ||
(this == SERVICE_OPTION_NOT_SUPPORTED) ||
                    (this == SERVICE_OPTION_NOT_SUBSCRIBED) ||
                    (this == SERVICE_OPTION_OUT_OF_ORDER) ||
                    (this == NSAPI_IN_USE) || (this == REGULAR_DEACTIVATION) ||
                    (this == NETWORK_FAILURE) || (this == PROTOCOL_ERRORS) ||
                    (this == REGISTRATION_FAIL) || (this == GPRS_REGISTRATION_FAIL) ||
                    (this == RADIO_NOT_AVAILABLE);
}

@Override
return "Data Services Out of Order";
case NSAPI_IN_USE:
return "NSAPI in use";
            case REGULAR_DEACTIVATION:
                return "Regular Deactivation";
            case NETWORK_FAILURE:
                return "Network Failure";
case PROTOCOL_ERRORS:
return "Protocol Errors";
case REGISTRATION_FAIL:
return "Unknown Data Error";
}
}

        public static FailCause getFailCauseFromRequest(int rilCause) {
            FailCause cause;
            switch (rilCause) {
                case RIL_FAILCAUSE_OPERATOR_BARRED:
                    cause = FailCause.OPERATOR_BARRED;
                    break;
                case RIL_FAILCAUSE_INSUFFICIENT_RESOURCES:
                    cause = FailCause.INSUFFICIENT_RESOURCES;
                    break;
                case RIL_FAILCAUSE_MISSING_UNKNOWN_APN:
                    cause = FailCause.MISSING_UNKNOWN_APN;
                    break;
                case RIL_FAILCAUSE_UNKNOWN_PDP_ADDRESS_TYPE:
                    cause = FailCause.UNKNOWN_PDP_ADDRESS;
                    break;
                case RIL_FAILCAUSE_USER_AUTHENTICATION:
                    cause = FailCause.USER_AUTHENTICATION;
                    break;
                case RIL_FAILCAUSE_ACTIVATION_REJECT_GGSN:
                    cause = FailCause.ACTIVATION_REJECT_GGSN;
                    break;
                case RIL_FAILCAUSE_ACTIVATION_REJECT_UNSPECIFIED:
                    cause = FailCause.ACTIVATION_REJECT_UNSPECIFIED;
                    break;
                case RIL_FAILCAUSE_SERVICE_OPTION_OUT_OF_ORDER:
                    cause = FailCause.SERVICE_OPTION_OUT_OF_ORDER;
                    break;
                case RIL_FAILCAUSE_SERVICE_OPTION_NOT_SUPPORTED:
                    cause = FailCause.SERVICE_OPTION_NOT_SUPPORTED;
                    break;
                case RIL_FAILCAUSE_SERVICE_OPTION_NOT_SUBSCRIBED:
                    cause = FailCause.SERVICE_OPTION_NOT_SUBSCRIBED;
                    break;
                case RIL_FAILCAUSE_NSAPI_IN_USE:
                    cause = FailCause.NSAPI_IN_USE;
                    break;
                case RIL_FAILCAUSE_REGULAR_DEACTIVATION:
                    cause = FailCause.REGULAR_DEACTIVATION;
                    break;
                case RIL_FAILCAUSE_NETWORK_FAILURE:
                    cause = FailCause.NETWORK_FAILURE;
                    break;
                case RIL_FAILCAUSE_PROTOCOL_ERRORS:
                    cause = FailCause.PROTOCOL_ERRORS;
                    break;
                case RIL_FAILCAUSE_ERROR_UNSPECIFIED:
                    cause = FailCause.UNKNOWN;
                    break;
                case RIL_FAILCAUSE_REGISTRATION_FAIL:
                    cause = FailCause.REGISTRATION_FAIL;
                    break;
                case RIL_FAILCAUSE_GPRS_REGISTRATION_FAIL:
                    cause = FailCause.GPRS_REGISTRATION_FAIL;
                    break;
                default:
                    cause = FailCause.UNKNOWN;
            }
            return cause;
        }
}

    public static final String LOG_TAG = "DataConnection";

// ***** Event codes for driving the state machine
protected static final int EVENT_RESET = 1;
protected static final int EVENT_CONNECT = 2;
protected static final String NULL_IP = "0.0.0.0";
Object userData;

    protected static ArrayList<FailCause> tempFailCauses = new ArrayList<FailCause>();

//***** Abstract methods
public abstract String toString();

protected abstract void onConnect(ConnectionParams cp);

protected abstract boolean isDnsOk(String[] domainNameServers);

protected abstract void log(String s);

clearSettings();

        // Initializing temporary failure causes list
        String settingStr = SystemProperties.get("ro.net.data_tmpfailures");
        configureTempFailureCauses(settingStr);

setDbg(false);
addState(mDefaultState);
addState(mInactiveState, mDefaultState);
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
                        FailCause failCause = FailCause.getFailCauseFromRequest(rilCause);
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
protected boolean isDnsOk(String[] domainNameServers) {
if ((NULL_IP.equals(domainNameServers[0])
&& NULL_IP.equals(domainNameServers[1])

//<End of snippet n. 1>










//<Beginning of snippet n. 2>



private static final String LOG_TAG = "GSM";

//***** Instance Variables
private ApnSetting apn;

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
                if ((mReregisterOnReconnectFailure || !lastFailCauseCode.isPermanentFail())) {
// We've re-registerd once now just retry forever.
mRetryMgr.retryForeverUsingLastTimeout();
} else {

//<End of snippet n. 3>








