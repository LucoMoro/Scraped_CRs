//<Beginning of snippet n. 0>
public AndroidVersion(int apiLevel, String codename) {
    mApiLevel = apiLevel;
    if (codename == null || codename.isEmpty()) {
        mCodename = "default";
        logDefaultCodenameAssignment();
    } else {
        mCodename = codename;
    }
}

public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
    if (properties == null) {
        mApiLevel = defaultApiLevel;
        mCodename = defaultCodeName;
        logDefaultCodenameAssignment();
    } else {
        mApiLevel = Integer.parseInt(properties.getProperty(PROP_VERSION_API_LEVEL, String.valueOf(defaultApiLevel)));
        String apiCodename = properties.getProperty(PROP_VERSION_CODENAME);
        if (apiCodename == null || apiCodename.isEmpty()) {
            mCodename = defaultCodeName;
            logDefaultCodenameAssignment();
        } else if (apiCodename.equals("REL")) {
            mCodename = defaultCodeName;
            logDefaultCodenameAssignment();
        } else {
            mCodename = apiCodename;
        }
    }
}

private void logDefaultCodenameAssignment() {
    // Implement logging logic here
}
//<End of snippet n. 0>