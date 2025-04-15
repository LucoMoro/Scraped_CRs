/*Implement mechanics for supports-screens in the multi-apk export.

- detect overlap (apk1 is small, large; apk2 is normal)
- detect non-strictly different supports-screens
- compareTo orders based on support for higher size.

Change-Id:I76251e10c83f1508c9a5b83b638a47b1c0922693*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index 88f28f6..4c7e606 100644

//Synthetic comment -- @@ -39,7 +39,6 @@
private static final String PROP_PROJECT = "project";
private static final String PROP_MINOR = "minor";
private static final String PROP_BUILDINFO = "buildinfo";

private String mOutputName;
private String mRelativePath;
//Synthetic comment -- @@ -149,17 +148,13 @@

@Override
public String toString() {
        return getLogLine();
}

public String getLogLine() {
StringBuilder sb = new StringBuilder();
        sb.append(mOutputName).append(':');
        write(sb, PROP_BUILDINFO, mBuildInfo);
write(sb, PROP_MINOR, mMinor);
write(sb, PROP_PROJECT, mRelativePath);
write(sb, PROP_API, mMinSdkVersion);
//Synthetic comment -- @@ -172,7 +167,7 @@
write(sb, PROP_ABI, mAbi);
}

        write(sb, PROP_SCREENS, mSupportsScreens.getEncodedValues());

return sb.toString();
}
//Synthetic comment -- @@ -195,7 +190,7 @@
return 1;
}

        comp = mSupportsScreens.compareScreenSizesWith(o.mSupportsScreens);
if (comp != 0) return comp;

if (mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
//Synthetic comment -- @@ -220,7 +215,7 @@
*/
public void initFromLogLine(String line) {
int colon = line.indexOf(':');
        mOutputName = line.substring(0, colon);
String[] properties = line.substring(colon+1).split(";");
HashMap<String, String> map = new HashMap<String, String>();
for (String prop : properties) {
//Synthetic comment -- @@ -231,6 +226,7 @@
}

private void setValues(Map<String, String> values) {
        mBuildInfo = Integer.parseInt(values.get(PROP_BUILDINFO));
mMinor = Integer.parseInt(values.get(PROP_MINOR));
mRelativePath = values.get(PROP_PROJECT);
mMinSdkVersion = Integer.parseInt(values.get(PROP_API));








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 693a5f8..db8079a 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.sdklib.io.IAbstractFolder.FilenameFilter;
import com.android.sdklib.xml.AndroidManifestParser;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.SupportsScreens;

import org.xml.sax.SAXException;

//Synthetic comment -- @@ -161,7 +162,7 @@
"# Multi-APK BUILD LOG.\n" +
"# This file serves two purpose:\n" +
"# - A log of what was built, showing what went in each APK and their properties.\n" +
                    "#   You can refer to this if you get a bug report for a specific versionCode.\n" +
"# - A way to update builds through minor revisions for specific APKs.\n" +
"# Only edit manually to change the minor properties for build you wish to respin.\n" +
"# Note that all APKs will be regenerated all the time.\n");
//Synthetic comment -- @@ -171,7 +172,7 @@

writer.append(
"# The format of the following lines is:\n" +
                    "# <filename>:<property1>;<property2>;<property3>;...\n" +
"# Properties are written as <name>=<value>\n");

for (ApkData apk : apks) {
//Synthetic comment -- @@ -309,19 +310,52 @@
// - GL version
// - ABI (not managed at the Manifest level).
// if those values are the same between 2 manifest, then it's an error.

                // first the minSdkVersion.
                if (minSdkVersion == previousManifest.data.getMinSdkVersion()) {
                    // if it's the same compare the rest.
                    SupportsScreens currentSS = manifestData.getSupportsScreensValues();
                    SupportsScreens previousSS = previousManifest.data.getSupportsScreensValues();
                    boolean sameSupportsScreens = currentSS.hasSameScreenSupportAs(previousSS);
                    if (manifestData.getGlEsVersion() == previousManifest.data.getGlEsVersion() &&
                            sameSupportsScreens) {

                        throw new ExportException(String.format(
                                "Android manifests must differ in at least one of the following values:\n" +
                                "- minSdkVersion\n" +
                                "- SupportsScreen (screen sizes only)\n" +
                                "- GL ES version.\n" +
                                "%1$s and %2$s are considered identical for multi-apk export.",
                                androidManifest.getOsLocation(),
                                previousManifest.file.getOsLocation()));
                    }

                    // At this point, either supports-screens or GL are different.
                    // Because minSdkVersion is the same, we need to some validity checks on the
                    // supports-screens.
                    // If they are different, then they must follow these rules:
                    // - They must be strictly different (ie no small, normal and normal, large)
                    // - they must not overlap (ie no small, large and normal).

                    if (sameSupportsScreens == false) {
                        if (currentSS.hasStrictlyDifferentScreenSupportAs(previousSS) == false) {
                            throw new ExportException(String.format(
                                    "APK differentiation by Supports-Screens cannot support different APKs supporting the same screen size.\n" +
                                    "%1$s supports %2$s\n" +
                                    "%3$s supports %4$s\n",
                                    androidManifest.getOsLocation(), currentSS.toString(),
                                    previousManifest.file.getOsLocation(), previousSS.toString()));
                        }

                        if (currentSS.overlapWith(previousSS)) {
                            throw new ExportException(String.format(
                                    "Unable to compute APK priority due to incompatible difference in Supports-Screens values.\n" +
                                    "%1$s supports %2$s\n" +
                                    "%3$s supports %4$s\n",
                                    androidManifest.getOsLocation(), currentSS.toString(),
                                    previousManifest.file.getOsLocation(), previousSS.toString()));
                        }
                    }
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
//Synthetic comment -- index da96cd4..911c283 100644

//Synthetic comment -- @@ -158,7 +158,7 @@
*
* To get an instance with all the actual values, use {@link #resolveSupportsScreensValues(int)}
*/
    public final static class SupportsScreens {
private Boolean mResizeable;
private Boolean mAnyDensity;
private Boolean mSmallScreens;
//Synthetic comment -- @@ -168,6 +168,11 @@
public SupportsScreens() {
}

        /**
         * Instantiate an instance from a string. The string must have been created with
         * {@link #getEncodedValues()}.
         * @param value the string.
         */
public SupportsScreens(String value) {
String[] values = value.split("\\|");

//Synthetic comment -- @@ -303,14 +308,143 @@
return false;
}

        /**
         * Returns true if the two instances support the same screen sizes.
         * This is similar to {@link #equals(Object)} except that it ignores the values of
         * {@link #getAnyDensity()} and {@link #getResizeable()}.
         * @param support the other instance to compare to.
         * @return true if the two instances support the same screen sizes.
         */
        public boolean hasSameScreenSupportAs(SupportsScreens support) {
            // since all the fields are guaranteed to be either Boolean.TRUE or Boolean.FALSE
            // (or null), we can simply check they are identical and not bother with
            // calling equals (which would require to check != null.
            // see #getConstanntBoolean(Boolean)

            // This only checks that matter here are the screen sizes. resizeable and anyDensity
            // are not checked.
            return  mSmallScreens == support.mSmallScreens &&
                    mNormalScreens == support.mNormalScreens &&
                    mLargeScreens == support.mLargeScreens;
        }

        /**
         * Returns true if the two instances have strictly different screen size support.
         * This means that there is no screen size that they both support.
         * @param support the other instance to compare to.
         * @return true if they are stricly different.
         */
        public boolean hasStrictlyDifferentScreenSupportAs(SupportsScreens support) {
            // since all the fields are guaranteed to be either Boolean.TRUE or Boolean.FALSE
            // (or null), we can simply check they are identical and not bother with
            // calling equals (which would require to check != null.
            // see #getConstanntBoolean(Boolean)

            // This only checks that matter here are the screen sizes. resizeable and anyDensity
            // are not checked.
            return (mSmallScreens != Boolean.TRUE || support.mSmallScreens != Boolean.TRUE) &&
                    (mNormalScreens != Boolean.TRUE || support.mNormalScreens != Boolean.TRUE) &&
                    (mLargeScreens != Boolean.TRUE || support.mLargeScreens != Boolean.TRUE);
        }

        /**
         * Comparison of 2 Supports-screens. This only uses screen sizes (ignores resizeable and
         * anyDensity), and considers that
         * {@link #hasStrictlyDifferentScreenSupportAs(SupportsScreens)} returns true and
         * {@link #overlapWith(SupportsScreens)} returns false.
         * @throws IllegalArgumentException if the two instanced are not strictly different or
         * overlap each other
         * @see #hasStrictlyDifferentScreenSupportAs(SupportsScreens)
         * @see #overlapWith(SupportsScreens)
         */
        public int compareScreenSizesWith(SupportsScreens o) {
            if (hasStrictlyDifferentScreenSupportAs(o) == false) {
                throw new IllegalArgumentException("The two instances are not strictly different.");
            }
            if (overlapWith(o)) {
                throw new IllegalArgumentException("The two instances overlap each other.");
            }

            int comp = mLargeScreens.compareTo(o.mLargeScreens);
            if (comp != 0) return comp;

            comp = mNormalScreens.compareTo(o.mNormalScreens);
            if (comp != 0) return comp;

            comp = mSmallScreens.compareTo(o.mSmallScreens);
            if (comp != 0) return comp;

return 0;
}

        /**
         * Returns a string encoding of the content of the instance. This string can be used to
         * instantiate a {@link SupportsScreens} object through
         * {@link #SupportsScreens(String)}.
         */
        public String getEncodedValues() {
            return String.format("%1$s|%2$s|%3$s|%4$s|%5$s",
                    mAnyDensity, mResizeable, mSmallScreens, mNormalScreens, mLargeScreens);
        }

@Override
public String toString() {
            StringBuilder sb = new StringBuilder();

            boolean alreadyOutputSomething = false;

            if (mSmallScreens != null && mSmallScreens.equals(Boolean.TRUE)) {
                alreadyOutputSomething = true;
                sb.append("small");
            }

            if (mNormalScreens != null && mNormalScreens.equals(Boolean.TRUE)) {
                if (alreadyOutputSomething) {
                    sb.append(", ");
                }
                alreadyOutputSomething = true;
                sb.append("normal");
            }

            if (mLargeScreens != null && mLargeScreens.equals(Boolean.TRUE)) {
                if (alreadyOutputSomething) {
                    sb.append(", ");
                }
                alreadyOutputSomething = true;
                sb.append("large");
            }

            if (alreadyOutputSomething == false) {
                sb.append("<none>");
            }

            return sb.toString();
        }

        /**
         * Returns true if the two instance overlap with each other.
         * This can happen if one instances supports a size, when the other instance doesn't while
         * supporting a size above and a size below.
         * @param otherSS the other supports-screens to compare to.
         */
        public boolean overlapWith(SupportsScreens otherSS) {
            if (mSmallScreens == null || mNormalScreens == null || mLargeScreens == null ||
                    otherSS.mSmallScreens == null || otherSS.mNormalScreens == null ||
                    otherSS.mLargeScreens == null) {
                throw new IllegalArgumentException("Some screen sizes Boolean are not initialized");
            }

            if (mSmallScreens == Boolean.TRUE && mNormalScreens == Boolean.FALSE &&
                    mLargeScreens == Boolean.TRUE) {
                return otherSS.mNormalScreens == Boolean.TRUE;
            }

            if (otherSS.mSmallScreens == Boolean.TRUE && otherSS.mNormalScreens == Boolean.FALSE &&
                    otherSS.mLargeScreens == Boolean.TRUE) {
                return mNormalScreens == Boolean.TRUE;
            }

            return false;
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/SupportsScreensTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/SupportsScreensTest.java
//Synthetic comment -- index c0cb12f..9eda533 100644

//Synthetic comment -- @@ -57,4 +57,74 @@
assertEquals(Boolean.TRUE, supportsScreens.getNormalScreens());
assertEquals(Boolean.FALSE, supportsScreens.getLargeScreens());
}

    public void testOverlapWith() {
        SupportsScreens supportsN = new SupportsScreens("false|false|false|true|false");
        SupportsScreens supportsSL = new SupportsScreens("false|false|true|false|true");

        assertTrue(supportsN.overlapWith(supportsSL));
        assertTrue(supportsSL.overlapWith(supportsN));
    }

    public void testCompareScreenSizesWith() {
        // set instance that support all combo of the three sizes.
        SupportsScreens supportsS = new SupportsScreens("false|false|true|false|false");
        SupportsScreens supportsN = new SupportsScreens("false|false|false|true|false");
        SupportsScreens supportsL = new SupportsScreens("false|false|false|false|true");
        SupportsScreens supportsSL = new SupportsScreens("false|false|true|false|true");

        assertEquals(-1, supportsS.compareScreenSizesWith(supportsN));
        assertEquals( 1, supportsN.compareScreenSizesWith(supportsS));
        assertEquals(-1, supportsN.compareScreenSizesWith(supportsL));
        assertEquals(-1, supportsS.compareScreenSizesWith(supportsL));

        // test thrown exception for overlapWith == true
        try {
            supportsSL.compareScreenSizesWith(supportsN);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success!
        }

        // test thrown exception for hasStrictlyDifferentScreenSupportAs == false
        try {
            supportsSL.compareScreenSizesWith(supportsS);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success!
        }

    }

    public void testHasStrictlyDifferentScreenSupportAs() {
        SupportsScreens supportsS = new SupportsScreens("false|false|true|false|false");
        SupportsScreens supportsN = new SupportsScreens("false|false|false|true|false");
        SupportsScreens supportsL = new SupportsScreens("false|false|false|false|true");

        SupportsScreens supportsSN = new SupportsScreens("false|false|true|true|false");
        SupportsScreens supportsNL = new SupportsScreens("false|false|false|true|true");
        SupportsScreens supportsSL = new SupportsScreens("false|false|true|false|true");
        SupportsScreens supportsSNL = new SupportsScreens("false|false|true|true|true");

        assertTrue(supportsS.hasStrictlyDifferentScreenSupportAs(supportsN));
        assertTrue(supportsS.hasStrictlyDifferentScreenSupportAs(supportsL));
        assertTrue(supportsN.hasStrictlyDifferentScreenSupportAs(supportsS));
        assertTrue(supportsN.hasStrictlyDifferentScreenSupportAs(supportsL));
        assertTrue(supportsL.hasStrictlyDifferentScreenSupportAs(supportsS));
        assertTrue(supportsL.hasStrictlyDifferentScreenSupportAs(supportsN));


        assertFalse(supportsSN.hasStrictlyDifferentScreenSupportAs(supportsS));
        assertFalse(supportsSN.hasStrictlyDifferentScreenSupportAs(supportsN));
        assertTrue(supportsSN.hasStrictlyDifferentScreenSupportAs(supportsL));
        assertFalse(supportsSL.hasStrictlyDifferentScreenSupportAs(supportsS));
        assertTrue(supportsSL.hasStrictlyDifferentScreenSupportAs(supportsN));
        assertFalse(supportsSL.hasStrictlyDifferentScreenSupportAs(supportsL));
        assertTrue(supportsNL.hasStrictlyDifferentScreenSupportAs(supportsS));
        assertFalse(supportsNL.hasStrictlyDifferentScreenSupportAs(supportsN));
        assertFalse(supportsNL.hasStrictlyDifferentScreenSupportAs(supportsL));
        assertFalse(supportsSNL.hasStrictlyDifferentScreenSupportAs(supportsS));
        assertFalse(supportsSNL.hasStrictlyDifferentScreenSupportAs(supportsN));
        assertFalse(supportsSNL.hasStrictlyDifferentScreenSupportAs(supportsL));
    }
}







