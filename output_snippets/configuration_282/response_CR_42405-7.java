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

    synchronized (manager) {
        if (enableEmergencyAlerts) {
            if (DBG) log("enabling emergency cell broadcast channels");
            if (!TextUtils.isEmpty(emergencyIdRange)) {
                setChannelRange(manager, emergencyIdRange, true);
            } else {
                enableAllEmergencyChannels(manager);
            }
            if (DBG) log("enabled emergency cell broadcast channels");
        } else {
            if (DBG) log("disabling emergency cell broadcast channels");
            if (!TextUtils.isEmpty(emergencyIdRange)) {
                setChannelRange(manager, emergencyIdRange, false);
            } else {
                disableAllEmergencyChannels(manager);
            }
            if (DBG) log("disabled emergency cell broadcast channels");
        }
        
        toggleChannel50(manager, enableChannel50Alerts);
    }
} catch (SecurityException ex) {
    Log.e(TAG, "Security issue enabling cell broadcast channels", ex);
} catch (IllegalArgumentException ex) {
    Log.e(TAG, "Invalid argument enabling cell broadcast channels", ex);
} catch (Exception ex) {
    Log.e(TAG, "exception enabling cell broadcast channels", ex);
}

private void enableAllEmergencyChannels(SmsManager manager) {
    manager.enableCellBroadcastRange(SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT, SmsEnvelope.SERVICE_CATEGORY_CMAS_TEST_MESSAGE);
    manager.enableCellBroadcast(SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT);
}

private void disableAllEmergencyChannels(SmsManager manager) {
    manager.disableCellBroadcastRange(SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT, SmsEnvelope.SERVICE_CATEGORY_CMAS_TEST_MESSAGE);
    manager.enableCellBroadcast(SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT);
}

private void toggleChannel50(SmsManager manager, boolean enable) {
    if (enable) {
        manager.enableCellBroadcast(50);
        if (DBG) log("enabled cell broadcast channel 50");
    } else {
        manager.disableCellBroadcast(50);
        if (DBG) log("disabled cell broadcast channel 50");
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
if (PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(CellBroadcastSettings.KEY_ENABLE_CHANNEL_50_ALERTS, true)) {
    enableChannel50Alerts.setOnPreferenceChangeListener(startConfigServiceListener);
}
//<End of snippet n. 2>