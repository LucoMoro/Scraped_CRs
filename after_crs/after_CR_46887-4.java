/*CB: Add CDMA CMAS

Add CDMA CMAS enabling/disabling

Change-Id:I5d70175862547475f49e08328ab3dcc5560e9096*/




//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastConfigService.java b/src/com/android/cellbroadcastreceiver/CellBroadcastConfigService.java
//Synthetic comment -- index a321d1b..4258e5c 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.gsm.SmsCbConstants;

import static com.android.cellbroadcastreceiver.CellBroadcastReceiver.DBG;
//Synthetic comment -- @@ -47,6 +48,9 @@

static final String ACTION_ENABLE_CHANNELS = "ACTION_ENABLE_CHANNELS";

    static final String EMERGENCY_BROADCAST_RANGE_GSM =
            "ro.cb.gsm.emergencyids";

public CellBroadcastConfigService() {
super(TAG);          // use class name for worker thread name
}
//Synthetic comment -- @@ -84,7 +88,11 @@

// Make sure CMAS Presidential is enabled (See 3GPP TS 22.268 Section 6.2).
if (DBG) log("setChannelRange: enabling CMAS Presidential");
        if (CellBroadcastReceiver.phoneIsCdma()) {
            manager.enableCellBroadcast(SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT);
        } else {
            manager.enableCellBroadcast(SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
        }
}

/**
//Synthetic comment -- @@ -99,7 +107,9 @@
}

// Check for system property defining the emergency channel ranges to enable
        String emergencyIdRange = (CellBroadcastReceiver.phoneIsCdma()) ?
                "" : SystemProperties.get(EMERGENCY_BROADCAST_RANGE_GSM);

if (TextUtils.isEmpty(emergencyIdRange)) {
return false;
}
//Synthetic comment -- @@ -132,9 +142,11 @@
try {
SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
Resources res = getResources();
                boolean isCdma = CellBroadcastReceiver.phoneIsCdma();

// Check for system property defining the emergency channel ranges to enable
                String emergencyIdRange = isCdma ?
                        "" : SystemProperties.get(EMERGENCY_BROADCAST_RANGE_GSM);

boolean enableEmergencyAlerts = prefs.getBoolean(
CellBroadcastSettings.KEY_ENABLE_EMERGENCY_ALERTS, true);
//Synthetic comment -- @@ -147,6 +159,15 @@
if (DBG) log("enabling emergency cell broadcast channels");
if (!TextUtils.isEmpty(emergencyIdRange)) {
setChannelRange(manager, emergencyIdRange, true);
                    } else if (isCdma){
                        // No emergency channel system property, enable all emergency channels
                        manager.enableCellBroadcastRange(
                                SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT,
                                SmsEnvelope.SERVICE_CATEGORY_CMAS_TEST_MESSAGE);

                        // CMAS Presidential must be on.
                        manager.enableCellBroadcast(
                                SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT);
} else {
// No emergency channel system property, enable all emergency channels
manager.enableCellBroadcastRange(
//Synthetic comment -- @@ -163,6 +184,15 @@
if (DBG) log("disabling emergency cell broadcast channels");
if (!TextUtils.isEmpty(emergencyIdRange)) {
setChannelRange(manager, emergencyIdRange, false);
                    } else if (isCdma) {
                        // No emergency channel system property, disable all emergency channels
                        manager.disableCellBroadcastRange(
                                SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT,
                                SmsEnvelope.SERVICE_CATEGORY_CMAS_TEST_MESSAGE);

                        // CMAS Presidential must be on.
                        manager.enableCellBroadcast(
                                SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT);
} else {
// No emergency channel system property, disable all emergency channels
// except for CMAS Presidential (See 3GPP TS 22.268 Section 6.2)
//Synthetic comment -- @@ -176,7 +206,9 @@
if (DBG) log("disabled emergency cell broadcast channels");
}

                if (isCdma) {
                    if (DBG) log("channel 50 is not aplicable for cdma");
                } else if (enableChannel50Alerts) {
if (DBG) log("enabling cell broadcast channel 50");
manager.enableCellBroadcast(50);
if (DBG) log("enabled cell broadcast channel 50");








//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastReceiver.java b/src/com/android/cellbroadcastreceiver/CellBroadcastReceiver.java
//Synthetic comment -- index 94d7179..ac5b1d8 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
startConfigService(context);
} else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
boolean airplaneModeOn = intent.getBooleanExtra("state", false);
            if (DBG) log("airplaneModeOn: " + airplaneModeOn);
if (!airplaneModeOn) {
startConfigService(context);
}
//Synthetic comment -- @@ -62,7 +63,7 @@
intent.setClass(context, CellBroadcastAlertService.class);
context.startService(intent);
} else {
                loge("ignoring unprivileged action received " + action);
}
} else if (Telephony.Sms.Intents.SMS_SERVICE_CATEGORY_PROGRAM_DATA_RECEIVED_ACTION
.equals(action)) {
//Synthetic comment -- @@ -72,10 +73,10 @@
if (programDataList != null) {
handleCdmaSmsCbProgramData(context, programDataList);
} else {
                    loge("SCPD intent received with no program_data_list");
}
} else {
                loge("ignoring unprivileged action received " + action);
}
} else {
Log.w(TAG, "onReceive() unexpected action " + action);
//Synthetic comment -- @@ -113,7 +114,7 @@
break;

default:
                    loge("Ignoring unknown SCPD operation " + programData.getOperation());
}
}
}
//Synthetic comment -- @@ -155,19 +156,15 @@
* @param context the broadcast receiver context
*/
static void startConfigService(Context context) {
        Intent serviceIntent = new Intent(CellBroadcastConfigService.ACTION_ENABLE_CHANNELS,
                null, context, CellBroadcastConfigService.class);
        context.startService(serviceIntent);
}

/**
* @return true if the phone is a CDMA phone type
*/
    static boolean phoneIsCdma() {
boolean isCdma = false;
try {
ITelephony phone = ITelephony.Stub.asInterface(ServiceManager.checkService("phone"));
//Synthetic comment -- @@ -183,4 +180,8 @@
private static void log(String msg) {
Log.d(TAG, msg);
}

    private static void loge(String msg) {
        Log.e(TAG, msg);
    }
}







