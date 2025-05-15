//<Beginning of snippet n. 0>
public AndroidVersion(int apiLevel, String codename) {
    mApiLevel = apiLevel;
    mCodename = codename != null && !codename.isEmpty() ? codename : "defaultCodename"; // Default value for codename
}

/* {@link #saveProperties(Properties)}. */
public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
    if (properties == null) {
        mApiLevel = defaultApiLevel;
        mCodename = defaultCodeName;
        return;
    }

    String apiCodename = properties.getProperty(PROP_VERSION_CODENAME);
    if (apiCodename == null || apiCodename.isEmpty() || apiCodename.equals("REL")) {
        apiCodename = null; // Handle empty and REL codenames
    }
    mCodename = apiCodename != null ? apiCodename : defaultCodeName; // Assigns default if apiCodename is null
}
//<End of snippet n. 0>