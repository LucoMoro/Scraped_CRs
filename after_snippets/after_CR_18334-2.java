
//<Beginning of snippet n. 0>


void unregisterForT53AudioControlInfo(Handler h);

/**
     * Fires on if Modem enters or exits Emergency Callback mode
*/
void setEmergencyCallbackMode(Handler h, int what, Object obj);


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


protected static final int EVENT_RUIM_RECORDS_LOADED            = 22;
protected static final int EVENT_NV_READY                       = 23;
protected static final int EVENT_SET_ENHANCED_VP                = 24;
    // Both enter and exit emergency callback mode
    protected static final int EVENT_EMERGENCY_CALLBACK_MODE        = 25;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


case RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS:  ret =  responseRaw(p); break;
case RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL:  ret =  responseVoid(p); break;
case RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
            case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_CDMA_CALL_WAITING: ret = responseCdmaCallWaiting(p); break;
case RIL_UNSOL_CDMA_OTA_PROVISION_STATUS: ret = responseInts(p); break;
case RIL_UNSOL_CDMA_INFO_REC: ret = responseCdmaInformationRecord(p); break;
if (RILJ_LOGD) unsljLog(response);

if (mEmergencyCallbackModeRegistrant != null) {
                    mEmergencyCallbackModeRegistrant.notifyResult(true);
                }
                break;

            case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE:
                if (RILJ_LOGD) unsljLog(response);

                if (mEmergencyCallbackModeRegistrant != null) {
                    mEmergencyCallbackModeRegistrant.notifyResult(false);
}
break;

case RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL: return "UNSOL_CDMA_RUIM_SMS_STORAGE_FULL";
case RIL_UNSOL_RESTRICTED_STATE_CHANGED: return "UNSOL_RESTRICTED_STATE_CHANGED";
case RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE: return "UNSOL_ENTER_EMERGENCY_CALLBACK_MODE";
            case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: return "UNSOL_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_UNSOL_CDMA_CALL_WAITING: return "UNSOL_CDMA_CALL_WAITING";
case RIL_UNSOL_CDMA_OTA_PROVISION_STATUS: return "UNSOL_CDMA_OTA_PROVISION_STATUS";
case RIL_UNSOL_CDMA_INFO_REC: return "UNSOL_CDMA_INFO_REC";

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


int RIL_UNSOL_RESEND_INCALL_MUTE = 1030;
int RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 1031;
int RIL_UNSOL_CDMA_PRL_CHANGED = 1032;
    int RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE = 1033;
}

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


static final String LOG_TAG = "CDMA";
private static final boolean DBG = true;

    private static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 100;
// Min values used to by needsActivation
private static final String UNACTIVATED_MIN2_VALUE = "000000";
private static final String UNACTIVATED_MIN_VALUE = "1111110111";
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttach(this, EVENT_REGISTERED_TO_NETWORK, null);
mCM.registerForNVReady(this, EVENT_NV_READY, null);
        mCM.setEmergencyCallbackMode(this, EVENT_EMERGENCY_CALLBACK_MODE, null);

PowerManager pm
= (PowerManager) context.getSystemService(Context.POWER_SERVICE);
if (mEcmExitRespRegistrant != null) {
mEcmExitRespRegistrant.notifyRegistrant(ar);
}
        // if unsolicited or sucess exiting ecm
        if (ar == null || ar.exception == null) {
if (mIsPhoneInEcmState) {
mIsPhoneInEcmState = false;
setSystemProperty(TelephonyProperties.PROPERTY_INECM_MODE, "false");
}
break;

            case EVENT_EMERGENCY_CALLBACK_MODE:{
                ar = (AsyncResult) msg.obj;
                boolean enter = (Boolean) ar.result;
                if (enter)
                    handleEnterEmergencyCallbackMode(msg);
                else
                    handleExitEmergencyCallbackMode(msg);
}
break;


//<End of snippet n. 4>








