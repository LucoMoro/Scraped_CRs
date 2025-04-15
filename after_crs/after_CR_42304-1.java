/*Do not set the network mode differently for LTE+CDMA

ro.telephony.default_network can be set if needed to set a different
value for LTE+CDMA. No need to hardcode preferred network mode in the source code.

Change-Id:Iae65f72a6a3bc31d500c5ecec87368a6f2a5d117*/




//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java b/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java
//Synthetic comment -- index de078d20..c06be40 100644

//Synthetic comment -- @@ -1592,12 +1592,8 @@

// Set the preferred network mode to 0 = Global, CDMA default
int type;
            type = SystemProperties.getInt("ro.telephony.default_network",
RILConstants.PREFERRED_NETWORK_MODE);
loadSetting(stmt, Settings.Secure.PREFERRED_NETWORK_MODE, type);

// Enable or disable Cell Broadcast SMS







