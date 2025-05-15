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

    String emergencyIdRange = isCdma ? "" : SystemProperties.get(EMERGENCY_BROADCAST_RANGE_GSM);
    boolean enableEmergencyAlerts = prefs.getBoolean(CellBroadcastSettings.KEY_ENABLE_EMERGENCY_ALERTS, true);
    boolean enableChannel50Alerts = res.getBoolean(R.bool.show_brazil_settings) && prefs.getBoolean(CellBroadcastSettings.KEY_ENABLE_CHANNEL_50_ALERTS, true);

    SmsManager manager = SmsManager.getDefault();
    if (enableEmergencyAlerts) {
        if (DBG) log("enabling emergency cell broadcast channels");
        if (!TextUtils.isEmpty(emergencyIdRange)) {
            setChannelRange(manager, emergencyIdRange, true);
        } else if (isCdma) {
            manager.enableCellBroadcastRange(SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT, SmsEnvelope.SERVICE_CATEGORY_CMAS_TEST_MESSAGE);
            manager.enableCellBroadcast(SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT);
        } else {
            manager.enableCellBroadcastRange(SmsCbConstants.MESSAGE_ID_PWS_FIRST_IDENTIFIER, SmsCbConstants.MESSAGE_ID_PWS_LAST_IDENTIFIER);
            manager.enableCellBroadcast(SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
        }
        if (DBG) log("enabled emergency cell broadcast channels");
    } else {
        if (DBG) log("disabling emergency cell broadcast channels");
        if (!TextUtils.isEmpty(emergencyIdRange)) {
            setChannelRange(manager, emergencyIdRange, false);
        } else if (isCdma) {
            manager.disableCellBroadcastRange(SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT, SmsEnvelope.SERVICE_CATEGORY_CMAS_TEST_MESSAGE);
            manager.enableCellBroadcast(SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT);
        } else {
            manager.disableCellBroadcastRange(SmsCbConstants.MESSAGE_ID_PWS_FIRST_IDENTIFIER, SmsCbConstants.MESSAGE_ID_PWS_LAST_IDENTIFIER);
            manager.enableCellBroadcast(SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
        }
        if (DBG) log("disabled emergency cell broadcast channels");
    }
    if (enableChannel50Alerts) {
        manager.enableCellBroadcast(50);
        if (DBG) log("enabled cell broadcast channel 50");
    } else {
        manager.disableCellBroadcast(50);
        if (DBG) log("disabled cell broadcast channel 50");
    }
} catch (SpecificExceptionType1 ex) {
    Log.e(TAG, "exception enabling cell broadcast channels for specific condition", ex);
} catch (SpecificExceptionType2 ex) {
    Log.e(TAG, "other specific exception while enabling cell broadcast channels", ex);
} catch (Exception ex) {
    Log.e(TAG, "general exception enabling cell broadcast channels", ex);
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
if (enableChannel50Alerts) {
    enableChannel50Alerts.setOnPreferenceChangeListener(startConfigServiceListener);
}
//<End of snippet n. 2>