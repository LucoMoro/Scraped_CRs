/*telephony: Handle RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE

Handle RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE as a way to exit from
Emergency Callback Mode

Change-Id:I0e9447689be9bb61fd5c3b4880d53f6af4118b98*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 430473f..112aca3 100644

//Synthetic comment -- @@ -525,7 +525,7 @@
void unregisterForT53AudioControlInfo(Handler h);

/**
     * Fires on if Modem enters Emergency Callback mode
*/
void setEmergencyCallbackMode(Handler h, int what, Object obj);









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index c3c8f5e..772cf41 100644

//Synthetic comment -- @@ -97,8 +97,8 @@
protected static final int EVENT_RUIM_RECORDS_LOADED            = 22;
protected static final int EVENT_NV_READY                       = 23;
protected static final int EVENT_SET_ENHANCED_VP                = 24;
    protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER  = 25;
    protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;

// Key used to read/write current CLIR setting
public static final String CLIR_KEY = "clir_key";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index c318756..5acfc28 100644

//Synthetic comment -- @@ -2360,6 +2360,7 @@
case RIL_UNSOL_RESPONSE_NEW_BROADCAST_SMS:  ret =  responseRaw(p); break;
case RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL:  ret =  responseVoid(p); break;
case RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
case RIL_UNSOL_CDMA_CALL_WAITING: ret = responseCdmaCallWaiting(p); break;
case RIL_UNSOL_CDMA_OTA_PROVISION_STATUS: ret = responseInts(p); break;
case RIL_UNSOL_CDMA_INFO_REC: ret = responseCdmaInformationRecord(p); break;
//Synthetic comment -- @@ -2618,7 +2619,15 @@
if (RILJ_LOGD) unsljLog(response);

if (mEmergencyCallbackModeRegistrant != null) {
                    mEmergencyCallbackModeRegistrant.notifyRegistrant();
}
break;

//Synthetic comment -- @@ -3329,6 +3338,7 @@
case RIL_UNSOL_CDMA_RUIM_SMS_STORAGE_FULL: return "UNSOL_CDMA_RUIM_SMS_STORAGE_FULL";
case RIL_UNSOL_RESTRICTED_STATE_CHANGED: return "UNSOL_RESTRICTED_STATE_CHANGED";
case RIL_UNSOL_ENTER_EMERGENCY_CALLBACK_MODE: return "UNSOL_ENTER_EMERGENCY_CALLBACK_MODE";
case RIL_UNSOL_CDMA_CALL_WAITING: return "UNSOL_CDMA_CALL_WAITING";
case RIL_UNSOL_CDMA_OTA_PROVISION_STATUS: return "UNSOL_CDMA_OTA_PROVISION_STATUS";
case RIL_UNSOL_CDMA_INFO_REC: return "UNSOL_CDMA_INFO_REC";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index 1a847e5..e888e2c 100644

//Synthetic comment -- @@ -272,4 +272,5 @@
int RIL_UNSOL_RESEND_INCALL_MUTE = 1030;
int RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 1031;
int RIL_UNSOL_CDMA_PRL_CHANGED = 1032;
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 7aecf5b..f8890a7 100755

//Synthetic comment -- @@ -83,6 +83,7 @@
static final String LOG_TAG = "CDMA";
private static final boolean DBG = true;

// Min values used to by needsActivation
private static final String UNACTIVATED_MIN2_VALUE = "000000";
private static final String UNACTIVATED_MIN_VALUE = "1111110111";
//Synthetic comment -- @@ -172,7 +173,7 @@
mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
mSST.registerForNetworkAttach(this, EVENT_REGISTERED_TO_NETWORK, null);
mCM.registerForNVReady(this, EVENT_NV_READY, null);
        mCM.setEmergencyCallbackMode(this, EVENT_EMERGENCY_CALLBACK_MODE_ENTER, null);

PowerManager pm
= (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//Synthetic comment -- @@ -937,8 +938,8 @@
if (mEcmExitRespRegistrant != null) {
mEcmExitRespRegistrant.notifyRegistrant(ar);
}
        // if exiting ecm success
        if (ar.exception == null) {
if (mIsPhoneInEcmState) {
mIsPhoneInEcmState = false;
setSystemProperty(TelephonyProperties.PROPERTY_INECM_MODE, "false");
//Synthetic comment -- @@ -1023,8 +1024,13 @@
}
break;

            case EVENT_EMERGENCY_CALLBACK_MODE_ENTER:{
                handleEnterEmergencyCallbackMode(msg);
}
break;








