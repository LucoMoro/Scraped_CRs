```
<<Beginning of snippet n. 0>>
public AndroidVersion(int apiLevel, String codename) {
    mApiLevel = apiLevel;
    mCodename = (codename == null || codename.isEmpty()) ? "defaultCodename" : codename;
}

public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
    if (properties == null) {
        mApiLevel = defaultApiLevel;
        mCodename = defaultCodeName;
    } else {
        String apiCodename = properties.getProperty(PROP_VERSION_CODENAME);
        mApiLevel = defaultApiLevel;
        mCodename = (apiCodename == null || apiCodename.isEmpty()) ? defaultCodeName : (apiCodename.equals("REL") ? null : apiCodename);
    }
}
<<End of snippet n. 0>>