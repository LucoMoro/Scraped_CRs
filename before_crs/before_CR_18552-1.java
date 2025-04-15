/*Added support for customization of mobile data setting.

Introduced system property "mobiledata", which will be
used for default behavior of the mobile data connection
either on/off.

This is analog to how the setting for "dataroaming" is
handeled.

Change-Id:Ifae8822dedfa55a515671014e2e29d43c469e701*/
//Synthetic comment -- diff --git a/core/java/android/provider/Settings.java b/core/java/android/provider/Settings.java
//Synthetic comment -- index 7b0e560..4e41f43 100644

//Synthetic comment -- @@ -676,6 +676,7 @@
MOVED_TO_SECURE.add(Secure.ANDROID_ID);
MOVED_TO_SECURE.add(Secure.BLUETOOTH_ON);
MOVED_TO_SECURE.add(Secure.DATA_ROAMING);
MOVED_TO_SECURE.add(Secure.DEVICE_PROVISIONED);
MOVED_TO_SECURE.add(Secure.HTTP_PROXY);
MOVED_TO_SECURE.add(Secure.INSTALL_NON_MARKET_APPS);








//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java b/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java
//Synthetic comment -- index 2b4714d..0671636 100644

//Synthetic comment -- @@ -223,6 +223,7 @@
Settings.Secure.ANDROID_ID,
Settings.Secure.BLUETOOTH_ON,
Settings.Secure.DATA_ROAMING,
Settings.Secure.DEVICE_PROVISIONED,
Settings.Secure.HTTP_PROXY,
Settings.Secure.INSTALL_NON_MARKET_APPS,
//Synthetic comment -- @@ -1084,6 +1085,11 @@
"true".equalsIgnoreCase(
SystemProperties.get("ro.com.android.dataroaming",
"false")) ? 1 : 0);

loadBooleanSetting(stmt, Settings.Secure.INSTALL_NON_MARKET_APPS,
R.bool.def_install_non_market_apps);







