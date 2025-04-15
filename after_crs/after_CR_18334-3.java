/*telephony: Handle RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE

Handle RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE as a way to exit from
Emergency Callback Mode

Change-Id:I0e9447689be9bb61fd5c3b4880d53f6af4118b98*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 1981b43..26d14b7 100644

//Synthetic comment -- @@ -525,7 +525,7 @@
void unregisterForT53AudioControlInfo(Handler h);

/**
     * Fires on if Modem enters or exits Emergency Callback mode
*/
void setEmergencyCallbackMode(Handler h, int what, Object obj);









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 1674ad6..b76fd68 100644

//Synthetic comment -- @@ -97,8 +97,8 @@
protected static final int EVENT_RUIM_RECORDS_LOADED            = 22;
protected static final int EVENT_NV_READY                       = 23;
protected static final int EVENT_SET_ENHANCED_VP                = 24;
    // Both enter and exit emergency callback mode
    protected static final int EVENT_EMERGENCY_CALLBACK_MODE        = 25;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 9a6c95c..89af5a0 100644

//Synthetic comment -- @@ -2368,6 +2368,7 @@
case RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS:  ret =  responseRaw(p); break;
case RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL:  ret =  responseVoid(p); break;
case RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
            case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_CDMA_CALL_WAITING: ret = responseCdmaCallWaiting(p); break;
case RIL_UNSOL_CDMA_OTA_PROVISION_STATUS: ret = responseInts(p); break;
case RIL_UNSOL_CDMA_INFO_REC: ret = responseCdmaInformationRecord(p); break;
//Synthetic comment -- @@ -2632,7 +2633,15 @@
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

//Synthetic comment -- @@ -3342,6 +3351,7 @@
case RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL: return "UNSOL_CDMA_RUIM_SMS_STORAGE_FULL";
case RIL_UNSOL_RESTRICTED_STATE_CHANGED: return "UNSOL_RESTRICTED_STATE_CHANGED";
case RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE: return "UNSOL_ENTER_EMERGENCY_CALLBACK_MODE";
            case RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE: return "UNSOL_EXIT_EMERGENCY_CALLBACK_MODE";
case RIL_UNSOL_CDMA_CALL_WAITING: return "UNSOL_CDMA_CALL_WAITING";
case RIL_UNSOL_CDMA_OTA_PROVISION_STATUS: return "UNSOL_CDMA_OTA_PROVISION_STATUS";
case RIL_UNSOL_CDMA_INFO_REC: return "UNSOL_CDMA_INFO_REC";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index cc71b48..cd7a322 100644

//Synthetic comment -- @@ -272,4 +272,5 @@
int RIL_UNSOL_RESEND_INCALL_MUTE = 1030;
int RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 1031;
int RIL_UNSOL_CDMA_PRL_CHANGED = 1032;
    int RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE = 1033;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index f31bf24..bd0d434 100644

//Synthetic comment -- @@ -83,6 +83,7 @@
static final String LOG_TAG = "CDMA";
private static final boolean DBG = true;

    private static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 100;
// Min values used to by needsActivation
private static final String UNACTIVATED_MIN2_VALUE = "000000";
private static final String UNACTIVATED_MIN_VALUE = "1111110111";
//Synthetic comment -- @@ -171,7 +172,7 @@
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttach(this, EVENT_REGISTERED_TO_NETWORK, null);
mCM.registerForNVReady(this, EVENT_NV_READY, null);
        mCM.setEmergencyCallbackMode(this, EVENT_EMERGENCY_CALLBACK_MODE, null);

PowerManager pm
= (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//Synthetic comment -- @@ -941,8 +942,8 @@
if (mEcmExitRespRegistrant != null) {
mEcmExitRespRegistrant.notifyRegistrant(ar);
}
        // if unsolicited or sucess exiting ecm
        if (ar == null || ar.exception == null) {
if (mIsPhoneInEcmState) {
mIsPhoneInEcmState = false;
setSystemProperty(TelephonyProperties.PROPERTY_INECM_MODE, "false");
//Synthetic comment -- @@ -1027,8 +1028,13 @@
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








