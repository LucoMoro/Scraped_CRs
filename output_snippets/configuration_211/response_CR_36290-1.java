//<Beginning of snippet n. 0>
public AndroidVersion(int apiLevel, String codename) {
    mApiLevel = apiLevel;
    mCodename = (codename == null || codename.isEmpty()) ? "defaultCodename" : codename;
}

/**
 * {@link #saveProperties(Properties)}.
 */
public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
    if (properties == null || properties.isEmpty()) {
        mApiLevel = defaultApiLevel;
        mCodename = defaultCodeName;
    } else {
        String apiCodename = properties.getProperty(PROP_VERSION_CODENAME);
        if (apiCodename == null || apiCodename.isEmpty()) {
            mCodename = defaultCodeName;
        } else if (apiCodename.equals("REL")) {
            mCodename = null; // REL means it's a release version and therefore the
                              // codename is irrelevant at this point.
        } else {
            mCodename = apiCodename;
        }
    }
//<End of snippet n. 0>