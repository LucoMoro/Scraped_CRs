/*Add SELinux enforcing status and booleans keys.

Addition to secure system settings is required by
SEAndroidManager app.
Signed-off-by: rpcraig <rpcraig@tycho.ncsc.mil>

Change-Id:Ib9b8fc6f3666adcbde8fa66eaf8e7c1c3900dd1d*/




//Synthetic comment -- diff --git a/core/java/android/provider/Settings.java b/core/java/android/provider/Settings.java
//Synthetic comment -- index 74c0a97..44b4bf5 100644

//Synthetic comment -- @@ -4296,6 +4296,25 @@
public static final String SMS_SHORT_CODES_PREFIX = "sms_short_codes_";

/**
         * SELinux enforcing status.
         * 1 - SELinux is in enforcing mode.
         * 0 - SELinux is in permissive mode.
         *
         * @hide
         */
        public static final String SELINUX_ENFORCING = "selinux_enforcing";

        /**
         * Stores the values of the SELinux booleans. Stored as a comma
         * seperated list of values, each value being of the form
         * {@code boolean_name:value} where value is 1 if the boolean is set
         * and 0 otherwise. Example: {@code bool1:1,bool2:0}.
         *
         * @hide
         */
        public static final String SELINUX_BOOLEANS = "selinux_booleans";

        /**
* This are the settings to be backed up.
*
* NOTE: Settings are backed up and restored in the order they appear
//Synthetic comment -- @@ -4334,7 +4353,9 @@
MOUNT_UMS_NOTIFY_ENABLED,
UI_NIGHT_MODE,
LOCK_SCREEN_OWNER_INFO,
            LOCK_SCREEN_OWNER_INFO_ENABLED,
            SELINUX_ENFORCING,
            SELINUX_BOOLEANS
};

/**







