/*CB: Enable or Disable broadcast messages

Send set config broadcast messages to modem when user changes
preferences from CellBroadcast App. This is necessary to reduce
power consumpution.

Change-Id:I6953aa8e07769437ccc3661f46e86faca602a4fb*/




//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastAlertService.java b/src/com/android/cellbroadcastreceiver/CellBroadcastAlertService.java
//Synthetic comment -- index 3cc821c..592fa23 100644

//Synthetic comment -- @@ -237,6 +237,7 @@

case SmsCbCmasInfo.CMAS_CLASS_REQUIRED_MONTHLY_TEST:
case SmsCbCmasInfo.CMAS_CLASS_CMAS_EXERCISE:
                case SmsCbCmasInfo.CMAS_CLASS_OPERATOR_DEFINED_USE:
return PreferenceManager.getDefaultSharedPreferences(this)
.getBoolean(CellBroadcastSettings.KEY_ENABLE_CMAS_TEST_ALERTS, false);









//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastConfigService.java b/src/com/android/cellbroadcastreceiver/CellBroadcastConfigService.java
//Synthetic comment -- index 4258e5c..6f77a0a 100644

//Synthetic comment -- @@ -154,6 +154,21 @@
boolean enableChannel50Alerts = res.getBoolean(R.bool.show_brazil_settings) &&
prefs.getBoolean(CellBroadcastSettings.KEY_ENABLE_CHANNEL_50_ALERTS, true);

                boolean enableEtwsTestAlerts = prefs.getBoolean(
                        CellBroadcastSettings.KEY_ENABLE_ETWS_TEST_ALERTS, false);

                boolean enableCmasExtremeAlerts = prefs.getBoolean(
                        CellBroadcastSettings.KEY_ENABLE_CMAS_EXTREME_THREAT_ALERTS, true);

                boolean enableCmasSevereAlerts = prefs.getBoolean(
                        CellBroadcastSettings.KEY_ENABLE_CMAS_SEVERE_THREAT_ALERTS, true);

                boolean enableCmasAmberAlerts = prefs.getBoolean(
                        CellBroadcastSettings.KEY_ENABLE_CMAS_AMBER_ALERTS, true);

                boolean enableCmasTestAlerts = prefs.getBoolean(
                        CellBroadcastSettings.KEY_ENABLE_CMAS_TEST_ALERTS, false);

SmsManager manager = SmsManager.getDefault();
if (enableEmergencyAlerts) {
if (DBG) log("enabling emergency cell broadcast channels");
//Synthetic comment -- @@ -161,22 +176,61 @@
setChannelRange(manager, emergencyIdRange, true);
} else if (isCdma){
// No emergency channel system property, enable all emergency channels
                        // that have checkbox checked
                        if (enableCmasExtremeAlerts) {
                            manager.enableCellBroadcast(
                                    SmsEnvelope.SERVICE_CATEGORY_CMAS_EXTREME_THREAT);
                        }
                        if (enableCmasSevereAlerts) {
                            manager.enableCellBroadcast(
                                    SmsEnvelope.SERVICE_CATEGORY_CMAS_SEVERE_THREAT);
                        }
                        if (enableCmasAmberAlerts) {
                            manager.enableCellBroadcast(
                                    SmsEnvelope.SERVICE_CATEGORY_CMAS_CHILD_ABDUCTION_EMERGENCY);
                        }
                        if (enableCmasTestAlerts) {
                            manager.enableCellBroadcast(
                                    SmsEnvelope.SERVICE_CATEGORY_CMAS_TEST_MESSAGE);
                        }

// CMAS Presidential must be on.
manager.enableCellBroadcast(
SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT);
} else {
                        // No emergency channel system property, disable all emergency channels
                        // except for CMAS Presidential (See 3GPP TS 22.268 Section 6.2)
manager.enableCellBroadcastRange(
                                SmsCbConstants.MESSAGE_ID_ETWS_EARTHQUAKE_WARNING,
                                SmsCbConstants.MESSAGE_ID_ETWS_EARTHQUAKE_AND_TSUNAMI_WARNING);
                        if (enableEtwsTestAlerts) {
                            manager.enableCellBroadcast(
                                    SmsCbConstants.MESSAGE_ID_ETWS_TEST_MESSAGE);
                        }
                        manager.enableCellBroadcast(
                                SmsCbConstants.MESSAGE_ID_ETWS_OTHER_EMERGENCY_TYPE);
                        if (enableCmasExtremeAlerts) {
                            manager.enableCellBroadcastRange(
                                    SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED,
                                    SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY);
                        }
                        if (enableCmasSevereAlerts) {
                            manager.enableCellBroadcastRange(
                                    SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED,
                                    SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY);
                        }
                        if (enableCmasAmberAlerts) {
                            manager.enableCellBroadcast(
                                    SmsCbConstants.MESSAGE_ID_CMAS_ALERT_CHILD_ABDUCTION_EMERGENCY);
                        }
                        if (enableCmasTestAlerts) {
                            manager.enableCellBroadcastRange(
                                    SmsCbConstants.MESSAGE_ID_CMAS_ALERT_REQUIRED_MONTHLY_TEST,
                                    SmsCbConstants.MESSAGE_ID_CMAS_ALERT_OPERATOR_DEFINED_USE);
                        }
// CMAS Presidential must be on (See 3GPP TS 22.268 Section 6.2).
manager.enableCellBroadcast(
                                SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
}
if (DBG) log("enabled emergency cell broadcast channels");
} else {
//Synthetic comment -- @@ -186,9 +240,13 @@
setChannelRange(manager, emergencyIdRange, false);
} else if (isCdma) {
// No emergency channel system property, disable all emergency channels
                        manager.disableCellBroadcast(
                                SmsEnvelope.SERVICE_CATEGORY_CMAS_EXTREME_THREAT);
                        manager.disableCellBroadcast(
                                SmsEnvelope.SERVICE_CATEGORY_CMAS_SEVERE_THREAT);
                        manager.disableCellBroadcast(
                                SmsEnvelope.SERVICE_CATEGORY_CMAS_CHILD_ABDUCTION_EMERGENCY);
                        manager.enableCellBroadcast(SmsEnvelope.SERVICE_CATEGORY_CMAS_TEST_MESSAGE);

// CMAS Presidential must be on.
manager.enableCellBroadcast(
//Synthetic comment -- @@ -197,9 +255,23 @@
// No emergency channel system property, disable all emergency channels
// except for CMAS Presidential (See 3GPP TS 22.268 Section 6.2)
manager.disableCellBroadcastRange(
                                SmsCbConstants.MESSAGE_ID_ETWS_EARTHQUAKE_WARNING,
                                SmsCbConstants.MESSAGE_ID_ETWS_EARTHQUAKE_AND_TSUNAMI_WARNING);
                        manager.disableCellBroadcast(
                                SmsCbConstants.MESSAGE_ID_ETWS_TEST_MESSAGE);
                        manager.disableCellBroadcast(
                                SmsCbConstants.MESSAGE_ID_ETWS_OTHER_EMERGENCY_TYPE);
                        manager.disableCellBroadcastRange(
                                SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED,
                                SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY);
                        manager.disableCellBroadcastRange(
                                SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED,
                                SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY);
                        manager.disableCellBroadcast(
                                SmsCbConstants.MESSAGE_ID_CMAS_ALERT_CHILD_ABDUCTION_EMERGENCY);
                        manager.disableCellBroadcastRange(
                                SmsCbConstants.MESSAGE_ID_CMAS_ALERT_REQUIRED_MONTHLY_TEST,
                                SmsCbConstants.MESSAGE_ID_CMAS_ALERT_OPERATOR_DEFINED_USE);
manager.enableCellBroadcast(
SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL);
}
//Synthetic comment -- @@ -217,6 +289,36 @@
manager.disableCellBroadcast(50);
if (DBG) log("disabled cell broadcast channel 50");
}

                // in case if emergencyIdRange was used, disable per user preference:
                if (!enableEtwsTestAlerts) {
                    if (DBG) Log.d(TAG, "disabling cell broadcast ETWS test messages");
                    manager.disableCellBroadcast(
                            SmsCbConstants.MESSAGE_ID_ETWS_TEST_MESSAGE);
                }
                if (!enableCmasExtremeAlerts) {
                    if (DBG) Log.d(TAG, "disabling cell broadcast CMAS extreme");
                    manager.disableCellBroadcastRange(
                            SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED,
                            SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY);
                }
                if (!enableCmasSevereAlerts) {
                    if (DBG) Log.d(TAG, "disabling cell broadcast CMAS severe");
                    manager.disableCellBroadcastRange(
                            SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED,
                            SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY);
                }
                if (!enableCmasAmberAlerts) {
                    if (DBG) Log.d(TAG, "disabling cell broadcast CMAS amber");
                    manager.disableCellBroadcast(
                            SmsCbConstants.MESSAGE_ID_CMAS_ALERT_CHILD_ABDUCTION_EMERGENCY);
                }
                if (!enableCmasTestAlerts) {
                    if (DBG) Log.d(TAG, "disabling cell broadcast CMAS test messages");
                    manager.disableCellBroadcastRange(
                            SmsCbConstants.MESSAGE_ID_CMAS_ALERT_REQUIRED_MONTHLY_TEST,
                            SmsCbConstants.MESSAGE_ID_CMAS_ALERT_OPERATOR_DEFINED_USE);
                }
} catch (Exception ex) {
Log.e(TAG, "exception enabling cell broadcast channels", ex);
}








//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastSettings.java b/src/com/android/cellbroadcastreceiver/CellBroadcastSettings.java
//Synthetic comment -- index a7c7482..27d77f3 100644

//Synthetic comment -- @@ -174,6 +174,28 @@
if (enableChannel50Alerts != null) {
enableChannel50Alerts.setOnPreferenceChangeListener(startConfigServiceListener);
}
            Preference enableEtwsAlerts = findPreference(KEY_ENABLE_ETWS_TEST_ALERTS);
            if (enableEtwsAlerts != null) {
                enableEtwsAlerts.setOnPreferenceChangeListener(startConfigServiceListener);
            }
            Preference enableCmasExtremeAlerts =
                    findPreference(KEY_ENABLE_CMAS_EXTREME_THREAT_ALERTS);
            if (enableCmasExtremeAlerts != null) {
                enableCmasExtremeAlerts.setOnPreferenceChangeListener(startConfigServiceListener);
            }
            Preference enableCmasSevereAlerts =
                    findPreference(KEY_ENABLE_CMAS_SEVERE_THREAT_ALERTS);
            if (enableCmasSevereAlerts != null) {
                enableCmasSevereAlerts.setOnPreferenceChangeListener(startConfigServiceListener);
            }
            Preference enableCmasAmberAlerts = findPreference(KEY_ENABLE_CMAS_AMBER_ALERTS);
            if (enableCmasAmberAlerts != null) {
                enableCmasAmberAlerts.setOnPreferenceChangeListener(startConfigServiceListener);
            }
            Preference enableCmasTestAlerts = findPreference(KEY_ENABLE_CMAS_TEST_ALERTS);
            if (enableCmasTestAlerts != null) {
                enableCmasTestAlerts.setOnPreferenceChangeListener(startConfigServiceListener);
            }
}
}
}







