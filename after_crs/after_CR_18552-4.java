/*Added support for customization of mobile data setting.

Introduced system property "mobiledata", which will be
used for default behavior of the mobile data connection
either on/off.

This is analog to how the setting for "dataroaming" is
handeled.

Change-Id:Ifae8822dedfa55a515671014e2e29d43c469e701*/




//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java b/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java
//Synthetic comment -- index a5e3483..56fe106 100644

//Synthetic comment -- @@ -1460,7 +1460,13 @@
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








