/*MUT does not vibrate in silent mode

MUT does not vibrate in silent mode even though default setting is
"Only in silent mode".
The default value for ringer in the SettingsProvider has been changed
from VIBRATE_SETTING_OFF to VIBRATE_SETTING_ONLY_SILENT to match the
default Vibrate settings value.

Change-Id:Idd617197c31591ecccef850f8c6a07cf3876cb0d*/
//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java b/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java
//Synthetic comment -- index f7ec294..d432014 100644

//Synthetic comment -- @@ -988,12 +988,12 @@
stmt = db.compileStatement("INSERT OR IGNORE INTO system(name,value)"
+ " VALUES(?,?);");

            // Vibrate off by default for ringer, on for notification
int vibrate = 0;
vibrate = AudioService.getValueForVibrateSetting(vibrate,
AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
vibrate |= AudioService.getValueForVibrateSetting(vibrate,
                    AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
loadSetting(stmt, Settings.System.VIBRATE_ON, vibrate);
} finally {
if (stmt != null) stmt.close();







