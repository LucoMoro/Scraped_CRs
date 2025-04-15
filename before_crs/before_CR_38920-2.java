/*Add SELinux enforcing status and booleans keys.

Addition to secure system settings is required by
SEAndroidManager app.
Signed-off-by: rpcraig <rpcraig@tycho.ncsc.mil>

Change-Id:Ib9b8fc6f3666adcbde8fa66eaf8e7c1c3900dd1d*/
//Synthetic comment -- diff --git a/core/java/android/provider/Settings.java b/core/java/android/provider/Settings.java
//Synthetic comment -- index 74c0a97..5acbf1c 100644

//Synthetic comment -- @@ -4295,6 +4295,25 @@
*/
public static final String SMS_SHORT_CODES_PREFIX = "sms_short_codes_";

/**
* This are the settings to be backed up.
*
//Synthetic comment -- @@ -4334,7 +4353,9 @@
MOUNT_UMS_NOTIFY_ENABLED,
UI_NIGHT_MODE,
LOCK_SCREEN_OWNER_INFO,
            LOCK_SCREEN_OWNER_INFO_ENABLED
};

/**







