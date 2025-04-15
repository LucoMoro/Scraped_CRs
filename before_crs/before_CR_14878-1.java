/*Add Manifest checks for multi-apk export.

Change-Id:Ifd0bb892582888b7b6ef6ceb634871c1fb24d6e5*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index dbc1a1d..4c0fd20 100644

//Synthetic comment -- @@ -240,7 +240,7 @@
helper.makeBuildLog(log, apks);
}
} catch (ExportException e) {
            throw new BuildException(e);
}
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 72014e4..e18b9d0 100644

//Synthetic comment -- @@ -26,7 +26,10 @@
import com.android.sdklib.io.IAbstractResource;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.io.IAbstractFolder.FilenameFilter;
import com.android.sdklib.xml.AndroidManifest;

import java.io.BufferedReader;
import java.io.File;
//Synthetic comment -- @@ -37,8 +40,11 @@
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

public class MultiApkExportHelper {

private final String mAppPackage;
//Synthetic comment -- @@ -186,6 +192,7 @@
String[] paths = projects.split("\\:");

ArrayList<ApkData> datalist = new ArrayList<ApkData>();

for (String path : paths) {
File projectFolder = new File(path);
//Synthetic comment -- @@ -212,7 +219,7 @@
SdkConstants.FN_ANDROID_MANIFEST_XML));
}

                ArrayList<ApkData> datalist2 = checkManifest(androidManifest);

// if the method returns without throwing, this is a good project to
// export.
//Synthetic comment -- @@ -243,30 +250,65 @@
* If the manifest is correct, a list of apk to export is created and returned.
*
* @param androidManifest the manifest to check
* @return A new non-null {@link ArrayList} of {@link ApkData}.
* @throws ExportException in case of error.
*/
    private ArrayList<ApkData> checkManifest(IAbstractFile androidManifest) throws ExportException {
try {
            String manifestPackage = AndroidManifest.getPackage(androidManifest);
if (mAppPackage.equals(manifestPackage) == false) {
throw new ExportException(String.format(
"%1$s package value is not valid. Found '%2$s', expected '%3$s'.",
androidManifest.getOsLocation(), manifestPackage, mAppPackage));
}

            if (AndroidManifest.hasVersionCode(androidManifest)) {
throw new ExportException(String.format(
"%1$s is not valid: versionCode must not be set for multi-apk export.",
androidManifest.getOsLocation()));
}

            int minSdkVersion = AndroidManifest.getMinSdkVersion(androidManifest);
            if (minSdkVersion == -1) {
throw new ExportException(
"Codename in minSdkVersion is not supported by multi-apk export.");
}

ArrayList<ApkData> dataList = new ArrayList<ApkData>();
ApkData data = new ApkData();
dataList.add(data);
//Synthetic comment -- @@ -301,15 +343,29 @@
}

return dataList;
        } catch (XPathExpressionException e) {
throw new ExportException(
String.format("Failed to validate %1$s", androidManifest.getOsLocation()), e);
} catch (StreamException e) {
throw new ExportException(
String.format("Failed to validate %1$s", androidManifest.getOsLocation()), e);
}
}

/**
* Loads and returns a list of {@link ApkData} from a build log.
* @param log








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java
//Synthetic comment -- index 5d65356..ee027d4 100644

//Synthetic comment -- @@ -174,9 +174,9 @@

mValidLevel++;
} else if (AndroidManifest.NODE_USES_SDK.equals(localName)) {
                                mManifestData.mApiLevelRequirement = getAttributeValue(attributes,
AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION,
                                        true /* hasNamespace */);
} else if (AndroidManifest.NODE_INSTRUMENTATION.equals(localName)) {
processInstrumentationNode(attributes);

//Synthetic comment -- @@ -472,21 +472,21 @@
private void processSupportsScreensNode(Attributes attributes) {
mManifestData.mSupportsScreens = new SupportsScreens();

            mManifestData.mSupportsScreens.mResizeable = Boolean.valueOf(
getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_RESIZEABLE, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mAnyDensity = Boolean.valueOf(
getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_ANYDENSITY, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mSmallScreens = Boolean.valueOf(
getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_SMALLSCREENS, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mNormalScreens = Boolean.valueOf(
getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_NORMALSCREENS, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mLargeScreens = Boolean.valueOf(
getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_LARGESCREENS, true /*hasNamespace*/));
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
//Synthetic comment -- index 71eaba5..b333791 100644

//Synthetic comment -- @@ -41,7 +41,10 @@
/** debuggable attribute value. If null, the attribute is not present. */
Boolean mDebuggable = null;
/** API level requirement. if null the attribute was not present. */
    String mApiLevelRequirement = null;
/** List of all instrumentations declared by the manifest */
final ArrayList<Instrumentation> mInstrumentations =
new ArrayList<Instrumentation>();
//Synthetic comment -- @@ -138,11 +141,11 @@
* Class representing the <code>supports-screens</code> node in the manifest.
*/
public final static class SupportsScreens {
        Boolean mResizeable;
        Boolean mAnyDensity;
        Boolean mSmallScreens;
        Boolean mLargeScreens;
        Boolean mNormalScreens;

/**
* returns the value of the <code>resizeable</code> attribute or null if not present.
//Synthetic comment -- @@ -151,6 +154,10 @@
return mResizeable;
}

/**
* returns the value of the <code>anyDensity</code> attribute or null if not present.
*/
//Synthetic comment -- @@ -158,6 +165,10 @@
return mAnyDensity;
}

/**
* returns the value of the <code>smallScreens</code> attribute or null if not present.
*/
//Synthetic comment -- @@ -165,6 +176,10 @@
return mSmallScreens;
}

/**
* returns the value of the <code>normalScreens</code> attribute or null if not present.
*/
//Synthetic comment -- @@ -172,12 +187,53 @@
return mNormalScreens;
}

/**
* returns the value of the <code>largeScreens</code> attribute or null if not present.
*/
public Boolean getLargeScreens() {
return mLargeScreens;
}
}

/**
//Synthetic comment -- @@ -320,8 +376,29 @@
/**
* Returns the <code>minSdkVersion</code> attribute, or null if it's not set.
*/
    public String getApiLevelRequirement() {
        return mApiLevelRequirement;
}

/**
//Synthetic comment -- @@ -349,6 +426,15 @@
return mFeatures.toArray(new UsesFeature[mFeatures.size()]);
}

/**
* Returns the {@link SupportsScreens} object representing the <code>supports-screens</code>
* node, or null if the node doesn't exist at all.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java
//Synthetic comment -- index 1936af4..b54f8ef 100644

//Synthetic comment -- @@ -80,7 +80,7 @@
}

public void testMinSdkVersion() {
        assertEquals("7", mManifestTestApp.getApiLevelRequirement());
}

public void testGetActivities() {







