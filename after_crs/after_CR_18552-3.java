/*Added support for customization of mobile data setting.

Introduced system property "mobiledata", which will be
used for default behavior of the mobile data connection
either on/off.

This is analog to how the setting for "dataroaming" is
handeled.

Change-Id:Ifae8822dedfa55a515671014e2e29d43c469e701*/




//Synthetic comment -- diff --git a/core/java/android/provider/Settings.java b/core/java/android/provider/Settings.java
//Synthetic comment -- index 2a13d520..9c06584 100644

//Synthetic comment -- @@ -705,6 +705,7 @@
MOVED_TO_SECURE.add(Secure.ANDROID_ID);
MOVED_TO_SECURE.add(Secure.BLUETOOTH_ON);
MOVED_TO_SECURE.add(Secure.DATA_ROAMING);
            MOVED_TO_SECURE.add(Secure.MOBILE_DATA);
MOVED_TO_SECURE.add(Secure.DEVICE_PROVISIONED);
MOVED_TO_SECURE.add(Secure.HTTP_PROXY);
MOVED_TO_SECURE.add(Secure.INSTALL_NON_MARKET_APPS);








//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java b/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java
//Synthetic comment -- index f7ec294..1a5d356 100644

//Synthetic comment -- @@ -1129,6 +1129,12 @@
"true".equalsIgnoreCase(
SystemProperties.get("ro.com.android.dataroaming",
"false")) ? 1 : 0);

            // Mobile Data default, based on build
            loadSetting(stmt, Settings.Secure.MOBILE_DATA,
                    "true".equalsIgnoreCase(
                            SystemProperties.get("ro.com.android.mobiledata",
                                    "true")) ? 1 : 0);

loadBooleanSetting(stmt, Settings.Secure.INSTALL_NON_MARKET_APPS,
R.bool.def_install_non_market_apps);







