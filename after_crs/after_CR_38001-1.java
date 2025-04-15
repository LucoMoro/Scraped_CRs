/*SDK Manager: API to list extras and their revisions.

Change-Id:I4989af2e987cf2b58f6da6a6a88f11a9351289e0*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 5c570ce..0f55eeb 100644

//Synthetic comment -- @@ -45,6 +45,7 @@
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -332,6 +333,37 @@
return samples;
}

    /**
     * Returns a map of all the extras found in the <em>local</em> SDK with their major revision.
     * <p/>
     * Map keys are in the form "vendor-id/path-id". These ids uniquely identify an extra package.
     * The version is the incremental integer major revision of the package.
     *
     * @return A non-null possibly empty map of { string "vendor/path" => integer major revision }
     */
    public @NonNull Map<String, Integer> getExtrasVersions() {
        LocalSdkParser parser = new LocalSdkParser();
        Package[] packages = parser.parseSdk(mOsSdkPath,
                                             this,
                                             LocalSdkParser.PARSE_EXTRAS,
                                             new NullTaskMonitor(new NullSdkLog()));

        Map<String, Integer> extraVersions = new TreeMap<String, Integer>();

        for (Package pkg : packages) {
            if (pkg instanceof ExtraPackage && pkg.isLocal()) {
                ExtraPackage ep = (ExtraPackage) pkg;
                String vendor = ep.getVendorId();
                String path = ep.getPath();
                int majorRev = ep.getRevision().getMajor();

                extraVersions.put(vendor + '/' + path, majorRev);
            }
        }

        return extraVersions;
    }


// -------- private methods ----------








