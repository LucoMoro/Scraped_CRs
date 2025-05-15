
//<Beginning of snippet n. 0>



case SmsCbCmasInfo.CMAS_CLASS_REQUIRED_MONTHLY_TEST:
case SmsCbCmasInfo.CMAS_CLASS_CMAS_EXERCISE:
                case SmsCbCmasInfo.CMAS_CLASS_OPERATOR_DEFINED_USE:
return PreferenceManager.getDefaultSharedPreferences(this)
.getBoolean(CellBroadcastSettings.KEY_ENABLE_CMAS_TEST_ALERTS, false);


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


try {
SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
Resources res = getResources();

                // boolean for each user preference checkbox, true for checked, false for unchecked
                // Note: If enableEmergencyAlerts is false, it disables ALL emergency broadcasts
                // except for cmas presidential. i.e. to receive cmas severe alerts, both
                // enableEmergencyAlerts AND enableCmasSevereAlerts must be true.
boolean enableEmergencyAlerts = prefs.getBoolean(
CellBroadcastSettings.KEY_ENABLE_EMERGENCY_ALERTS, true);

boolean enableChannel50Alerts = res.getBoolean(R.bool.show_brazil_settings) &&
prefs.getBoolean(CellBroadcastSettings.KEY_ENABLE_CHANNEL_50_ALERTS, true);

                // Note:  ETWS is for 3GPP only
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

                // set up broadcast ID ranges to be used for each category
                int cmasExtremeStart =
                        SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED;
                int cmasExtremeEnd = SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY;
                int cmasSevereStart =
                        SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED;
                int cmasSevereEnd = SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY;
                int cmasAmber = SmsCbConstants.MESSAGE_ID_CMAS_ALERT_CHILD_ABDUCTION_EMERGENCY;
                int cmasTestStart = SmsCbConstants.MESSAGE_ID_CMAS_ALERT_REQUIRED_MONTHLY_TEST;
                int cmasTestEnd = SmsCbConstants.MESSAGE_ID_CMAS_ALERT_OPERATOR_DEFINED_USE;
                int cmasPresident = SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL;

                // set to CDMA broadcast ID rage if phone is in CDMA mode.
                boolean isCdma = CellBroadcastReceiver.phoneIsCdma();
                if (isCdma) {
                    cmasExtremeStart = SmsEnvelope.SERVICE_CATEGORY_CMAS_EXTREME_THREAT;
                    cmasExtremeEnd = cmasExtremeStart;
                    cmasSevereStart = SmsEnvelope.SERVICE_CATEGORY_CMAS_SEVERE_THREAT;
                    cmasSevereEnd = cmasSevereStart;
                    cmasAmber = SmsEnvelope.SERVICE_CATEGORY_CMAS_CHILD_ABDUCTION_EMERGENCY;
                    cmasTestStart = SmsEnvelope.SERVICE_CATEGORY_CMAS_TEST_MESSAGE;
                    cmasTestEnd = cmasTestStart;
                    cmasPresident = SmsEnvelope.SERVICE_CATEGORY_CMAS_PRESIDENTIAL_LEVEL_ALERT;
                }

SmsManager manager = SmsManager.getDefault();
                // Check for system property defining the emergency channel ranges to enable
                String emergencyIdRange = isCdma ?
                        "" : SystemProperties.get(EMERGENCY_BROADCAST_RANGE_GSM);
if (enableEmergencyAlerts) {
if (DBG) log("enabling emergency cell broadcast channels");
if (!TextUtils.isEmpty(emergencyIdRange)) {
setChannelRange(manager, emergencyIdRange, true);
} else {
// No emergency channel system property, enable all emergency channels
                        // that have checkbox checked
                        if (!isCdma) {
                            manager.enableCellBroadcastRange(
                                    SmsCbConstants.MESSAGE_ID_ETWS_EARTHQUAKE_WARNING,
                                    SmsCbConstants.MESSAGE_ID_ETWS_EARTHQUAKE_AND_TSUNAMI_WARNING);
                            if (enableEtwsTestAlerts) {
                                manager.enableCellBroadcast(
                                        SmsCbConstants.MESSAGE_ID_ETWS_TEST_MESSAGE);
                            }
                            manager.enableCellBroadcast(
                                    SmsCbConstants.MESSAGE_ID_ETWS_OTHER_EMERGENCY_TYPE);
                        }
                        if (enableCmasExtremeAlerts) {
                            manager.enableCellBroadcastRange(cmasExtremeStart, cmasExtremeEnd);
                        }
                        if (enableCmasSevereAlerts) {
                            manager.enableCellBroadcastRange(cmasSevereStart, cmasSevereEnd);
                        }
                        if (enableCmasAmberAlerts) {
                            manager.enableCellBroadcast(cmasAmber);
                        }
                        if (enableCmasTestAlerts) {
                            manager.enableCellBroadcastRange(cmasTestStart, cmasTestEnd);
                        }
// CMAS Presidential must be on (See 3GPP TS 22.268 Section 6.2).
                        manager.enableCellBroadcast(cmasPresident);
}
if (DBG) log("enabled emergency cell broadcast channels");
} else {
if (DBG) log("disabling emergency cell broadcast channels");
if (!TextUtils.isEmpty(emergencyIdRange)) {
setChannelRange(manager, emergencyIdRange, false);
} else {
// No emergency channel system property, disable all emergency channels
// except for CMAS Presidential (See 3GPP TS 22.268 Section 6.2)
                        if (!isCdma) {
                            manager.disableCellBroadcastRange(
                                    SmsCbConstants.MESSAGE_ID_ETWS_EARTHQUAKE_WARNING,
                                    SmsCbConstants.MESSAGE_ID_ETWS_EARTHQUAKE_AND_TSUNAMI_WARNING);
                            manager.disableCellBroadcast(
                                    SmsCbConstants.MESSAGE_ID_ETWS_TEST_MESSAGE);
                            manager.disableCellBroadcast(
                                    SmsCbConstants.MESSAGE_ID_ETWS_OTHER_EMERGENCY_TYPE);
                        }
                        manager.disableCellBroadcastRange(cmasExtremeStart, cmasExtremeEnd);
                        manager.disableCellBroadcastRange(cmasSevereStart, cmasSevereEnd);
                        manager.disableCellBroadcast(cmasAmber);
                        manager.disableCellBroadcastRange(cmasTestStart, cmasTestEnd);

                        // CMAS Presidential must be on (See 3GPP TS 22.268 Section 6.2).
                        manager.enableCellBroadcast(cmasPresident);
}
if (DBG) log("disabled emergency cell broadcast channels");
}
manager.disableCellBroadcast(50);
if (DBG) log("disabled cell broadcast channel 50");
}

                // Disable per user preference/checkbox.
                // This takes care of the case where enableEmergencyAlerts is true,
                // but check box is unchecked to receive such as cmas severe alerts.
                if (!enableEtwsTestAlerts  && !isCdma) {
                    if (DBG) Log.d(TAG, "disabling cell broadcast ETWS test messages");
                    manager.disableCellBroadcast(
                            SmsCbConstants.MESSAGE_ID_ETWS_TEST_MESSAGE);
                }
                if (!enableCmasExtremeAlerts) {
                    if (DBG) Log.d(TAG, "disabling cell broadcast CMAS extreme");
                    manager.disableCellBroadcastRange(cmasExtremeStart, cmasExtremeEnd);
                }
                if (!enableCmasSevereAlerts) {
                    if (DBG) Log.d(TAG, "disabling cell broadcast CMAS severe");
                    manager.disableCellBroadcastRange(cmasSevereStart, cmasSevereEnd);
                }
                if (!enableCmasAmberAlerts) {
                    if (DBG) Log.d(TAG, "disabling cell broadcast CMAS amber");
                    manager.disableCellBroadcast(cmasAmber);
                }
                if (!enableCmasTestAlerts) {
                    if (DBG) Log.d(TAG, "disabling cell broadcast CMAS test messages");
                    manager.disableCellBroadcastRange(cmasTestStart, cmasTestEnd);
                }
} catch (Exception ex) {
Log.e(TAG, "exception enabling cell broadcast channels", ex);
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


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

//<End of snippet n. 2>








