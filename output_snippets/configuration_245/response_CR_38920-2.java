<|vq_11684|><<Beginning of snippet n. 0>>
public static final String SMS_SHORT_CODES_PREFIX = "sms_short_codes_";

public static class SELinuxSettings {
    public boolean enforcing;
    public Map<String, Boolean> booleanKeys;

    public SELinuxSettings(boolean enforcing, Map<String, Boolean> booleanKeys) {
        this.enforcing = enforcing;
        this.booleanKeys = booleanKeys;
    }
}

/**
* These are the settings to be backed up.
*/
public static final String[] BACKUP_SETTINGS = {
    "MOUNT_UMS_NOTIFY_ENABLED",
    "UI_NIGHT_MODE",
    "LOCK_SCREEN_OWNER_INFO",
    "LOCK_SCREEN_OWNER_INFO_ENABLED",
    "SELINUX_ENFORCING_STATUS",
    "SELINUX_BOOLEAN_KEYS"
};

//<End of snippet n. 0>>