/*Only show SELinux status on non-user build

Change-Id:I502867e7036f5b9a453eb809e34bee8fb87d9fa1*/
//Synthetic comment -- diff --git a/src/com/android/settings/DeviceInfoSettings.java b/src/com/android/settings/DeviceInfoSettings.java
//Synthetic comment -- index 8254233..63ed0a7 100644

//Synthetic comment -- @@ -54,7 +54,6 @@
private static final String KEY_COPYRIGHT = "copyright";
private static final String KEY_SYSTEM_UPDATE_SETTINGS = "system_update_settings";
private static final String PROPERTY_URL_SAFETYLEGAL = "ro.url.safetylegal";
    private static final String PROPERTY_SELINUX_STATUS = "ro.build.selinux";
private static final String KEY_KERNEL_VERSION = "kernel_version";
private static final String KEY_BUILD_NUMBER = "build_number";
private static final String KEY_DEVICE_MODEL = "device_model";
//Synthetic comment -- @@ -90,9 +89,8 @@
setStringSummary(KEY_SELINUX_STATUS, status);
}

        // Remove selinux information if property is not present
        removePreferenceIfPropertyMissing(getPreferenceScreen(), KEY_SELINUX_STATUS,
                PROPERTY_SELINUX_STATUS);

// Remove Safety information preference if PROPERTY_URL_SAFETYLEGAL is not set
removePreferenceIfPropertyMissing(getPreferenceScreen(), "safetylegal",
//Synthetic comment -- @@ -158,16 +156,25 @@
return super.onPreferenceTreeClick(preferenceScreen, preference);
}

private void removePreferenceIfPropertyMissing(PreferenceGroup preferenceGroup,
            String preference, String property ) {
        if (SystemProperties.get(property).equals(""))
        {
// Property is missing so remove preference from group
try {
preferenceGroup.removePreference(findPreference(preference));
} catch (RuntimeException e) {
                Log.d(LOG_TAG, "Property '" + property + "' missing and no '"
                        + preference + "' preference");
}
}
}







