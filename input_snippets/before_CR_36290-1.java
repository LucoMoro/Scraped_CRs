
//<Beginning of snippet n. 0>


*/
public AndroidVersion(int apiLevel, String codename) {
mApiLevel = apiLevel;
mCodename = codename;
}

* {@link #saveProperties(Properties)}.
*/
public AndroidVersion(Properties properties, int defaultApiLevel, String defaultCodeName) {
if (properties == null) {
mApiLevel = defaultApiLevel;
mCodename = defaultCodeName;

//<End of snippet n. 0>










//<Beginning of snippet n. 1>

new file mode 100755


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


}
}

            // codename (optional)
String apiCodename = platformProp.get(PROP_VERSION_CODENAME);
            if (apiCodename != null && apiCodename.equals("REL")) {
                apiCodename = null; // REL means it's a release version and therefore the
                                    // codename is irrelevant at this point.
}

// version string

//<End of snippet n. 2>








