/*Enable or Disable broadcast messages

Send set config broadcast messages to modem when user changes
preferences from CellBroadcast App. This is necessary to reduce
power consumpution.

Change-Id:I6953aa8e07769437ccc3661f46e86faca602a4fb*/
//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastAlertService.java b/src/com/android/cellbroadcastreceiver/CellBroadcastAlertService.java
//Synthetic comment -- index 6bf5abb..e165a08 100644

//Synthetic comment -- @@ -146,6 +146,7 @@

case SmsCbCmasInfo.CMAS_CLASS_REQUIRED_MONTHLY_TEST:
case SmsCbCmasInfo.CMAS_CLASS_CMAS_EXERCISE:
return PreferenceManager.getDefaultSharedPreferences(this)
.getBoolean(CellBroadcastSettings.KEY_ENABLE_CMAS_TEST_ALERTS, false);









//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastConfigService.java b/src/com/android/cellbroadcastreceiver/CellBroadcastConfigService.java
//Synthetic comment -- index ad77dc2..584241c 100644

//Synthetic comment -- @@ -124,6 +124,21 @@
boolean enableChannel50Alerts = res.getBoolean(R.bool.show_brazil_settings) &&
prefs.getBoolean(CellBroadcastSettings.KEY_ENABLE_CHANNEL_50_ALERTS, true);

SmsManager manager = SmsManager.getDefault();
if (enableEmergencyAlerts) {
if (DBG) log("enabling emergency cell broadcast channels");
//Synthetic comment -- @@ -132,8 +147,33 @@
} else {
// No emergency channel system property, enable all emergency channels
manager.enableCellBroadcastRange(
                                SmsCbConstants.MESSAGE_ID_PWS_FIRST_IDENTIFIER,
                                SmsCbConstants.MESSAGE_ID_PWS_LAST_IDENTIFIER);
}
if (DBG) log("enabled emergency cell broadcast channels");
} else {
//Synthetic comment -- @@ -144,8 +184,23 @@
} else {
// No emergency channel system property, disable all emergency channels
manager.disableCellBroadcastRange(
                                SmsCbConstants.MESSAGE_ID_PWS_FIRST_IDENTIFIER,
                                SmsCbConstants.MESSAGE_ID_PWS_LAST_IDENTIFIER);
}
if (DBG) log("disabled emergency cell broadcast channels");
}
//Synthetic comment -- @@ -159,6 +214,35 @@
manager.disableCellBroadcast(50);
if (DBG) log("disabled cell broadcast channel 50");
}
} catch (Exception ex) {
Log.e(TAG, "exception enabling cell broadcast channels", ex);
}








//Synthetic comment -- diff --git a/src/com/android/cellbroadcastreceiver/CellBroadcastSettings.java b/src/com/android/cellbroadcastreceiver/CellBroadcastSettings.java
//Synthetic comment -- index 973cb31..667f746 100644

//Synthetic comment -- @@ -168,6 +168,28 @@
if (enableChannel50Alerts != null) {
enableChannel50Alerts.setOnPreferenceChangeListener(startConfigServiceListener);
}
}
}
}







