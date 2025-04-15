/*Add SELinux enforcing status and booleans keys.

Addition to secure system settings is required by
SEAndroidManager app.
Signed-off-by: rpcraig <rpcraig@tycho.ncsc.mil>

Change-Id:Ib9b8fc6f3666adcbde8fa66eaf8e7c1c3900dd1d*/
//Synthetic comment -- diff --git a/core/java/android/provider/Settings.java b/core/java/android/provider/Settings.java
//Synthetic comment -- index d7fab37..66fe195e 100644

//Synthetic comment -- @@ -4082,6 +4082,25 @@
"contacts_preauth_uri_expiration";

/**
* This are the settings to be backed up.
*
* NOTE: Settings are backed up and restored in the order they appear
//Synthetic comment -- @@ -4119,7 +4138,9 @@
MOUNT_UMS_NOTIFY_ENABLED,
UI_NIGHT_MODE,
LOCK_SCREEN_OWNER_INFO,
            LOCK_SCREEN_OWNER_INFO_ENABLED
};

/**







