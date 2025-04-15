/*CB: Add CDMA CMAS

- Add CDMA CMAS enabling/disabling
- Turn on CMAS Presidential
- Added ServiceStateListener.  Upon in service or emergency
  only service, app will send enable/disable broadcast IDs.

Change-Id:I5d70175862547475f49e08328ab3dcc5560e9096*/
//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastConfigService.java b/src/com/android/cellbroadcastreceiver/CellBroadcastConfigService.java
//Synthetic comment -- index a321d1b..9066f5b 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2011 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -28,6 +29,7 @@
import android.util.Log;

import com.android.internal.telephony.gsm.SmsCbConstants;

import static com.android.cellbroadcastreceiver.CellBroadcastReceiver.DBG;

//Synthetic comment -- @@ -45,14 +47,24 @@
public class CellBroadcastConfigService extends IntentService {
private static final String TAG = "CellBroadcastConfigService";

    static final String ACTION_ENABLE_CHANNELS = "ACTION_ENABLE_CHANNELS";

public CellBroadcastConfigService() {
super(TAG);          // use class name for worker thread name
}

    private static void setChannelRange(SmsManager manager, String ranges, boolean enable) {
        if (DBG)log("setChannelRange: " + ranges);

try {
for (String channelRange : ranges.split(",")) {
//Synthetic comment -- @@ -62,19 +74,35 @@
int endId = Integer.decode(channelRange.substring(dashIndex + 1).trim());
if (enable) {
if (DBG) log("enabling emergency IDs " + startId + '-' + endId);
                        manager.enableCellBroadcastRange(startId, endId);
} else {
if (DBG) log("disabling emergency IDs " + startId + '-' + endId);
                        manager.disableCellBroadcastRange(startId, endId);
}
} else {
int messageId = Integer.decode(channelRange.trim());
if (enable) {
if (DBG) log("enabling emergency message ID " + messageId);
                        manager.enableCellBroadcast(messageId);
} else {
if (DBG) log("disabling emergency message ID " + messageId);
                        manager.disableCellBroadcast(messageId);
}
}
}
//Synthetic comment -- @@ -84,7 +112,11 @@

// Make sure CMAS Presidential is enabled (See 3GPP TS 22.268 Section 6.2).
if (DBG) log("setChannelRange: enabling CMAS Presidential");
        manager.enableCellBroadcast(SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
}

/**
//Synthetic comment -- @@ -99,7 +131,10 @@
}

// Check for system property defining the emergency channel ranges to enable
        String emergencyIdRange = SystemProperties.get("ro.cellbroadcast.emergencyids");
if (TextUtils.isEmpty(emergencyIdRange)) {
return false;
}
//Synthetic comment -- @@ -128,66 +163,123 @@

@Override
protected void onHandleIntent(Intent intent) {
        if (ACTION_ENABLE_CHANNELS.equals(intent.getAction())) {
            try {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                Resources res = getResources();

                // Check for system property defining the emergency channel ranges to enable
                String emergencyIdRange = SystemProperties.get("ro.cellbroadcast.emergencyids");

                boolean enableEmergencyAlerts = prefs.getBoolean(
                        CellBroadcastSettings.KEY_ENABLE_EMERGENCY_ALERTS, true);

                boolean enableChannel50Alerts = res.getBoolean(R.bool.show_brazil_settings) &&
                        prefs.getBoolean(CellBroadcastSettings.KEY_ENABLE_CHANNEL_50_ALERTS, true);

                SmsManager manager = SmsManager.getDefault();
                if (enableEmergencyAlerts) {
                    if (DBG) log("enabling emergency cell broadcast channels");
                    if (!TextUtils.isEmpty(emergencyIdRange)) {
                        setChannelRange(manager, emergencyIdRange, true);
                    } else {
                        // No emergency channel system property, enable all emergency channels
                        manager.enableCellBroadcastRange(
                                SmsCbConstants.MESSAGE_ID_PWS_FIRST_IDENTIFIER,
                                SmsCbConstants.MESSAGE_ID_PWS_LAST_IDENTIFIER);

                        // CMAS Presidential must be on (See 3GPP TS 22.268 Section 6.2).
                        manager.enableCellBroadcast(
                               SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
                    }
                    if (DBG) log("enabled emergency cell broadcast channels");
} else {
                    // we may have enabled these channels previously, so try to disable them
                    if (DBG) log("disabling emergency cell broadcast channels");
                    if (!TextUtils.isEmpty(emergencyIdRange)) {
                        setChannelRange(manager, emergencyIdRange, false);
                    } else {
                        // No emergency channel system property, disable all emergency channels
                        // except for CMAS Presidential (See 3GPP TS 22.268 Section 6.2)
                        manager.disableCellBroadcastRange(
                                SmsCbConstants.MESSAGE_ID_PWS_FIRST_IDENTIFIER,
                                SmsCbConstants.MESSAGE_ID_PWS_LAST_IDENTIFIER);

                        manager.enableCellBroadcast(
                                SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
                    }
                    if (DBG) log("disabled emergency cell broadcast channels");
}

                if (enableChannel50Alerts) {
                    if (DBG) log("enabling cell broadcast channel 50");
                    manager.enableCellBroadcast(50);
                    if (DBG) log("enabled cell broadcast channel 50");
} else {
                    if (DBG) log("disabling cell broadcast channel 50");
                    manager.disableCellBroadcast(50);
                    if (DBG) log("disabled cell broadcast channel 50");
}
            } catch (Exception ex) {
                Log.e(TAG, "exception enabling cell broadcast channels", ex);
}
}
}









//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastReceiver.java b/src/com/android/cellbroadcastreceiver/CellBroadcastReceiver.java
//Synthetic comment -- index 94d7179..b6ce242 100644

//Synthetic comment -- @@ -24,6 +24,8 @@
import android.os.ServiceManager;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaSmsCbProgramData;
import android.util.Log;
//Synthetic comment -- @@ -34,6 +36,9 @@
public class CellBroadcastReceiver extends BroadcastReceiver {
private static final String TAG = "CellBroadcastReceiver";
static final boolean DBG = true;    // STOPSHIP: change to false before ship

@Override
public void onReceive(Context context, Intent intent) {
//Synthetic comment -- @@ -46,9 +51,14 @@
String action = intent.getAction();

if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            startConfigService(context);
} else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
boolean airplaneModeOn = intent.getBooleanExtra("state", false);
if (!airplaneModeOn) {
startConfigService(context);
}
//Synthetic comment -- @@ -155,19 +165,19 @@
* @param context the broadcast receiver context
*/
static void startConfigService(Context context) {
if (phoneIsCdma()) {
            if (DBG) log("CDMA phone detected; doing nothing");
        } else {
            Intent serviceIntent = new Intent(CellBroadcastConfigService.ACTION_ENABLE_CHANNELS,
                    null, context, CellBroadcastConfigService.class);
            context.startService(serviceIntent);
}
}

/**
* @return true if the phone is a CDMA phone type
*/
    private static boolean phoneIsCdma() {
boolean isCdma = false;
try {
ITelephony phone = ITelephony.Stub.asInterface(ServiceManager.checkService("phone"));
//Synthetic comment -- @@ -180,6 +190,20 @@
return isCdma;
}

private static void log(String msg) {
Log.d(TAG, msg);
}







