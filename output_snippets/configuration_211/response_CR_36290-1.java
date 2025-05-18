//<Beginning of snippet n. 0>
public AndroidVersion(int apiLevel, String codename) {
    mApiLevel = apiLevel;
    mCodename = (codename != null && !codename.isEmpty() && !codename.equals("REL")) ? codename : defaultCodeName;
}

/* {@link #saveProperties(Properties)}. */
public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
    if (properties == null) {
        mApiLevel = defaultApiLevel;
        mCodename = defaultCodeName;
    } else {
        String apiCodename = properties.getProperty(PROP_VERSION_CODENAME);
        mCodename = (apiCodename != null && !apiCodename.isEmpty() && !apiCodename.equals("REL")) ? apiCodename : defaultCodeName;
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
// No changes needed in this snippet
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
// No changes needed in this snippet
//<End of snippet n. 2>