
//<Beginning of snippet n. 0>



case SmsCbCmasInfo.CMAS_CLASS_REQUIRED_MONTHLY_TEST:
case SmsCbCmasInfo.CMAS_CLASS_CMAS_EXERCISE:
return PreferenceManager.getDefaultSharedPreferences(this)
.getBoolean(CellBroadcastSettings.KEY_ENABLE_CMAS_TEST_ALERTS, false);


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


try {
SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
Resources res = getResources();
                boolean isCdma = CellBroadcastReceiver.phoneIsCdma();

                // Check for system property defining the emergency channel ranges to enable
                String emergencyIdRange = isCdma ?
                        "" : SystemProperties.get(EMERGENCY_BROADCAST_RANGE_GSM);

boolean enableEmergencyAlerts = prefs.getBoolean(
CellBroadcastSettings.KEY_ENABLE_EMERGENCY_ALERTS, true);

boolean enableChannel50Alerts = res.getBoolean(R.bool.show_brazil_settings) &&
prefs.getBoolean(CellBroadcastSettings.KEY_ENABLE_CHANNEL_50_ALERTS, true);

SmsManager manager = SmsManager.getDefault();
if (enableEmergencyAlerts) {
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
                                SmsCbConstants.MESSAGE_ID_PWS_FIRST_IDENTIFIER,
                                SmsCbConstants.MESSAGE_ID_PWS_LAST_IDENTIFIER);

// CMAS Presidential must be on (See 3GPP TS 22.268 Section 6.2).
                        manager.enableCellBroadcast(
                               SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
}
if (DBG) log("enabled emergency cell broadcast channels");
} else {
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
                        manager.disableCellBroadcastRange(
                                SmsCbConstants.MESSAGE_ID_PWS_FIRST_IDENTIFIER,
                                SmsCbConstants.MESSAGE_ID_PWS_LAST_IDENTIFIER);

                        manager.enableCellBroadcast(
                                SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
}
if (DBG) log("disabled emergency cell broadcast channels");
}
manager.disableCellBroadcast(50);
if (DBG) log("disabled cell broadcast channel 50");
}
} catch (Exception ex) {
Log.e(TAG, "exception enabling cell broadcast channels", ex);
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


if (enableChannel50Alerts != null) {
enableChannel50Alerts.setOnPreferenceChangeListener(startConfigServiceListener);
}
}
}
}

//<End of snippet n. 2>








