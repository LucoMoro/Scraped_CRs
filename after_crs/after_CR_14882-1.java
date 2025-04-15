/*Properly compare supports-screens.

Comparing the content of the manifest is not working. It is
important to compare the value the platform is seeing when
runnign the app, which means knowing the default values and
overriding them with what is present in the manifest.

This requires parsing targetSdkVersion as minSdkVersion is
not enough.

Change-Id:I703a8093670709e633f47170b02dec033ae29ea9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java
//Synthetic comment -- index 3531013..fe24553 100755

//Synthetic comment -- @@ -405,7 +405,6 @@
updateLocationPathField(null);
}
});
}

private final void createLocationGroup(Composite parent) {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 5e37c08..604c461 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.sdklib.io.IAbstractFolder.FilenameFilter;
import com.android.sdklib.xml.AndroidManifestParser;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.SupportsScreens;

import org.xml.sax.SAXException;

//Synthetic comment -- @@ -302,8 +303,8 @@
// - ABI (not managed at the Manifest level).
// if those values are the same between 2 manifest, then it's an error.
if (minSdkVersion == previousManifest.data.getMinSdkVersion() &&
                        manifestData.getSupportsScreensValues().equals(
                                previousManifest.data.getSupportsScreensValues()) &&
manifestData.getGlEsVersion() == previousManifest.data.getGlEsVersion()) {
throw new ExportException(String.format(
"Android manifests must differ in at least one of the following values:\n" +








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java
//Synthetic comment -- index 476418d..ed07635 100644

//Synthetic comment -- @@ -58,6 +58,7 @@
public final static String ATTRIBUTE_PROCESS = "process";
public final static String ATTRIBUTE_DEBUGGABLE = "debuggable";
public final static String ATTRIBUTE_MIN_SDK_VERSION = "minSdkVersion";
    public final static String ATTRIBUTE_TARGET_SDK_VERSION = "targetSdkVersion";
public final static String ATTRIBUTE_TARGET_PACKAGE = "targetPackage";
public final static String ATTRIBUTE_EXPORTED = "exported";
public final static String ATTRIBUTE_RESIZEABLE = "resizeable";








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java
//Synthetic comment -- index ee027d4..869138a 100644

//Synthetic comment -- @@ -177,6 +177,9 @@
mManifestData.setMinSdkVersionString(getAttributeValue(attributes,
AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION,
true /* hasNamespace */));
                                mManifestData.setTargetSdkVersionString(getAttributeValue(attributes,
                                        AndroidManifest.ATTRIBUTE_TARGET_SDK_VERSION,
                                        true /* hasNamespace */));
} else if (AndroidManifest.NODE_INSTRUMENTATION.equals(localName)) {
processInstrumentationNode(attributes);

//Synthetic comment -- @@ -470,21 +473,21 @@
* @param attributes the attributes for the supports-screens node.
*/
private void processSupportsScreensNode(Attributes attributes) {
            mManifestData.mSupportsScreensFromManifest = new SupportsScreens();

            mManifestData.mSupportsScreensFromManifest.setResizeable(Boolean.valueOf(
getAttributeValue(attributes,
AndroidManifest.ATTRIBUTE_RESIZEABLE, true /*hasNamespace*/)));
            mManifestData.mSupportsScreensFromManifest.setAnyDensity(Boolean.valueOf(
getAttributeValue(attributes,
AndroidManifest.ATTRIBUTE_ANYDENSITY, true /*hasNamespace*/)));
            mManifestData.mSupportsScreensFromManifest.setSmallScreens(Boolean.valueOf(
getAttributeValue(attributes,
AndroidManifest.ATTRIBUTE_SMALLSCREENS, true /*hasNamespace*/)));
            mManifestData.mSupportsScreensFromManifest.setNormalScreens(Boolean.valueOf(
getAttributeValue(attributes,
AndroidManifest.ATTRIBUTE_NORMALSCREENS, true /*hasNamespace*/)));
            mManifestData.mSupportsScreensFromManifest.setLargeScreens(Boolean.valueOf(
getAttributeValue(attributes,
AndroidManifest.ATTRIBUTE_LARGESCREENS, true /*hasNamespace*/)));
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
//Synthetic comment -- index 5b232c6..95ceb52 100644

//Synthetic comment -- @@ -45,6 +45,7 @@
/** API level requirement. Default is 1 even if missing. If value is a codename, then it'll be
* 0 instead. */
private int mMinSdkVersion = 1;
    private int mTargetSdkVersion = 0;
/** List of all instrumentations declared by the manifest */
final ArrayList<Instrumentation> mInstrumentations =
new ArrayList<Instrumentation>();
//Synthetic comment -- @@ -53,7 +54,8 @@
/** List of all feature in use declared by the manifest */
final ArrayList<UsesFeature> mFeatures = new ArrayList<UsesFeature>();

    SupportsScreens mSupportsScreensFromManifest;
    SupportsScreens mSupportsScreensValues;
UsesConfiguration mUsesConfiguration;

/**
//Synthetic comment -- @@ -139,13 +141,51 @@

/**
* Class representing the <code>supports-screens</code> node in the manifest.
     * By default, all the getters will return null if there was no value defined in the manifest.
     *
     * To get an instance with all the actual values, use {@link #getFullSupportsScreensValue(int)}
*/
public final static class SupportsScreens implements Comparable<SupportsScreens> {
private Boolean mResizeable;
private Boolean mAnyDensity;
private Boolean mSmallScreens;
private Boolean mNormalScreens;
        private Boolean mLargeScreens;

        /**
         * Returns an instance of {@link SupportsScreens} initialized with the default values
         * based on the given targetSdkVersion.
         * @param targetSdkVersion
         */
        public static SupportsScreens getDefaultValues(int targetSdkVersion) {
            SupportsScreens result = new SupportsScreens();

            // put some default values
            result.mNormalScreens = Boolean.TRUE;
            result.mResizeable = result.mAnyDensity = result.mSmallScreens = result.mLargeScreens =
                targetSdkVersion <= 3 ? Boolean.FALSE : Boolean.TRUE;

            return result;
        }

        /**
         * Returns a version of the receiver for which all values have been set, even if they
         * were not present in the manifest.
         * @param targetSdkVersion the target api level of the app, since this has an effect
         * on default values.
         */
        public SupportsScreens getFullSupportsScreensValue(int targetSdkVersion) {
            SupportsScreens result = getDefaultValues(targetSdkVersion);

            // put the existing values:
            if (mResizeable != null) result.mResizeable = mResizeable;
            if (mAnyDensity != null) result.mAnyDensity = mAnyDensity;
            if (mSmallScreens != null) result.mSmallScreens = mSmallScreens;
            if (mNormalScreens != null) result.mNormalScreens = mNormalScreens;
            if (mLargeScreens != null) result.mLargeScreens = mLargeScreens;

            return result;
        }

/**
* returns the value of the <code>resizeable</code> attribute or null if not present.
//Synthetic comment -- @@ -386,14 +426,16 @@

/**
* Sets the value of the <code>minSdkVersion</code> attribute.
     * @param minSdkVersion the string value of the attribute in the manifest.
*/
public void setMinSdkVersionString(String minSdkVersion) {
mMinSdkVersionString = minSdkVersion;
        if (mMinSdkVersionString != null) {
            try {
                mMinSdkVersion = Integer.parseInt(mMinSdkVersionString);
            } catch (NumberFormatException e) {
                mMinSdkVersion = 0; // 0 means it's a codename.
            }
}
}

//Synthetic comment -- @@ -405,6 +447,33 @@
return mMinSdkVersion;
}


    /**
     * Sets the value of the <code>minSdkVersion</code> attribute.
     * @param targetSdkVersion the string value of the attribute in the manifest.
     */
    public void setTargetSdkVersionString(String targetSdkVersion) {
        if (targetSdkVersion != null) {
            try {
                mTargetSdkVersion = Integer.parseInt(mMinSdkVersionString);
            } catch (NumberFormatException e) {
                // keep the value at 0.
            }
        }
    }

    /**
     * Returns the <code>targetSdkVersion</code> attribute, or the same value as
     * {@link #getMinSdkVersion()} if it was not set in the manifest.
     */
    public int getTargetSdkVersion() {
        if (mTargetSdkVersion == 0) {
            return getMinSdkVersion();
        }

        return mTargetSdkVersion;
    }

/**
* Returns the list of instrumentations found in the manifest.
* @return An array of {@link Instrumentation}, or empty if no instrumentations were
//Synthetic comment -- @@ -442,9 +511,31 @@
/**
* Returns the {@link SupportsScreens} object representing the <code>supports-screens</code>
* node, or null if the node doesn't exist at all.
     * Some values in the {@link SupportsScreens} instance maybe null, indicating that they
     * were not present in the manifest. To get an instance that contains the values, as seen
     * by the Android platform when the app is running, use {@link #getSupportsScreensValues()}.
*/
    public SupportsScreens getSupportsScreensFromManifest() {
        return mSupportsScreensFromManifest;
    }

    /**
     * Returns an always non-null instance of {@link SupportsScreens} that's been initialized with
     * the default values, and the values from the manifest.
     * The default values depends on the manifest values for minSdkVersion and targetSdkVersion.
     */
    public synchronized SupportsScreens getSupportsScreensValues() {
        if (mSupportsScreensValues == null) {
            if (mSupportsScreensFromManifest == null) {
                mSupportsScreensValues = SupportsScreens.getDefaultValues(getTargetSdkVersion());
            } else {
                // get a SupportsScreen that replace the missing values with default values.
                mSupportsScreensValues = mSupportsScreensFromManifest.getFullSupportsScreensValue(
                        getTargetSdkVersion());
            }
        }

        return mSupportsScreensValues;
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java
//Synthetic comment -- index b54f8ef..cfa6000 100644

//Synthetic comment -- @@ -101,7 +101,8 @@
}

public void testSupportsScreen() {
        ManifestData.SupportsScreens supportsScreens =
            mManifestTestApp.getSupportsScreensFromManifest();

assertNotNull(supportsScreens);
assertEquals(Boolean.TRUE, supportsScreens.getAnyDensity());







