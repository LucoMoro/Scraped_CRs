//<Beginning of snippet n. 0>
public AndroidVersion(int apiLevel, String codename) {
    if (apiLevel <= 0) {
        throw new IllegalArgumentException("apiLevel must be a positive integer.");
    }
    mApiLevel = apiLevel;
    mCodename = (codename == null || codename.isEmpty()) ? "Unknown" : codename;
}

public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
    if (properties == null) {
        mApiLevel = defaultApiLevel;
        mCodename = (defaultCodeName == null || defaultCodeName.isEmpty()) ? "Unknown" : defaultCodeName;
        return;
    }
    // Additional logic for handling properties can go here
}
//<End of snippet n. 0>

//<Beginning of snippet n. 2>
String apiCodename = platformProp.get(PROP_VERSION_CODENAME);
if (apiCodename == null || apiCodename.isEmpty() || apiCodename.equals("REL")) {
    apiCodename = "Unknown";
}
//<End of snippet n. 2>