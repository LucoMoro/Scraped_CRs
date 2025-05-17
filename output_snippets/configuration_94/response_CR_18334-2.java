//<Beginning of snippet n. 0>


void unregisterForT53AudioControlInfo(Handler h);

/**
     * Fires on if Modem enters Emergency Callback mode
*/
void setEmergencyCallbackMode(Handler h, int what, Object obj);


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


protected static final int EVENT_RUIM_RECORDS_LOADED            = 22;
protected static final int EVENT_NV_READY                       = 23;
protected static final int EVENT_SET_ENHANCED_VP                = 24;
protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER  = 25;
protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


case RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS:  ret =  responseRaw(p); break;
case RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL:  ret =  responseVoid(p); break;
case RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE: 
    ret = responseVoid(p); 
    if (mEmergencyCallbackModeRegistrant != null) {
        mEmergencyCallbackModeRegistrant.notifyRegistrant();
    }
    break;
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: 
    ret = responseVoid(p); 
    mIsPhoneInEcmState = false; // Set state before notifying
    if (mEcmExitRespRegistrant != null) {
        mEcmExitRespRegistrant.notifyRegistrant();
    }
    setSystemProperty(TelephonyProperties.PROPERTY_INECM_MODE, "false");
    if (RILJ_LOGD) unsljLog(response);
    break;
case RIL_UNSOL_CDMA_CALL_WAITING: ret = responseCdmaCallWaiting(p); break;
case RIL_UNSOL_CDMA_OTA_PROVISION_STATUS: ret = responseInts(p); break;
case RIL_UNSOL_CDMA_INFO_REC: ret = responseCdmaInformationRecord(p); break;
if (RILJ_LOGD) unsljLog(response);

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
}

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


static final String LOG_TAG = "CDMA";
private static final boolean DBG = true;

// Min values used to by needsActivation
private static final String UNACTIVATED_MIN2_VALUE = "000000";
private static final String UNACTIVATED_MIN_VALUE = "1111110111";
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttach(this, EVENT_REGISTERED_TO_NETWORK, null);
mCM.registerForNVReady(this, EVENT_NV_READY, null);
mCM.setEmergencyCallbackMode(this, EVENT_EMERGENCY_CALLBACK_MODE_ENTER, null);

PowerManager pm
= (PowerManager) context.getSystemService(Context.POWER_SERVICE);

// Exit handling
if (ar.exception == null) {
    if (mIsPhoneInEcmState) {
        mIsPhoneInEcmState = false;
        setSystemProperty(TelephonyProperties.PROPERTY_INECM_MODE, "false");
        if (mEcmExitRespRegistrant != null) {
            mEcmExitRespRegistrant.notifyRegistrant(ar);
        }
    }
}
break;

case EVENT_EMERGENCY_CALLBACK_MODE_ENTER: {
    handleEnterEmergencyCallbackMode(msg);
}
break;

//<End of snippet n. 4>