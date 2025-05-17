//<Beginning of snippet n. 0>
public AndroidVersion(int apiLevel, String codename) {
    mApiLevel = apiLevel;
    mCodename = (codename == null || codename.isEmpty() || codename.equals("REL")) ? "INVALID_CODENAME" : codename;
}

/* {@link #saveProperties(Properties)}. */
public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
    if (properties == null) {
        mApiLevel = defaultApiLevel;
        mCodename = (defaultCodeName == null || defaultCodeName.isEmpty()) ? "INVALID_CODENAME" : defaultCodeName;
    } else {
        String apiCodename = properties.getProperty(PROP_VERSION_CODENAME);
        if (apiCodename == null || apiCodename.isEmpty() || apiCodename.equals("REL")) {
            mCodename = "INVALID_CODENAME";
        } else {
            mCodename = apiCodename;
        }
    }
}
//<End of snippet n. 0>