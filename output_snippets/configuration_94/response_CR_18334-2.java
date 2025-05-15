//<Beginning of snippet n. 2>
case RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS:  ret =  responseRaw(p); break;
case RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL:  ret =  responseVoid(p); break;
case RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: 
    ret = responseVoid(p); 
    if (mEcmExitRespRegistrant != null) {
        mEcmExitRespRegistrant.notifyRegistrant();
    }
    if (mIsPhoneInEcmState) {
        mIsPhoneInEcmState = false;
        setSystemProperty(TelephonyProperties.PROPERTY_INECM_MODE, "false");
    }
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