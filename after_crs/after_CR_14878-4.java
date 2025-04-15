/*Add Manifest checks for multi-apk export.

Change-Id:Ifd0bb892582888b7b6ef6ceb634871c1fb24d6e5*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index dbc1a1d..3d1131b 100644

//Synthetic comment -- @@ -240,7 +240,10 @@
helper.makeBuildLog(log, apks);
}
} catch (ExportException e) {
            // we only want to have Ant display the message, not the stack trace, since
            // we use Exceptions to report errors, so we build the BuildException only
            // with the message and not the cause exception.
            throw new BuildException(e.getMessage());
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java
//Synthetic comment -- index 50660d2..a65b33a 100644

//Synthetic comment -- @@ -355,7 +355,7 @@

// get the java package from the parser
javaPackage = parser.getPackage();
                minSdkVersion = parser.getMinSdkVersionString();
}

if (minSdkVersion != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerDeltaVisitor.java
//Synthetic comment -- index e151698..3d7a7d8 100644

//Synthetic comment -- @@ -234,7 +234,7 @@

if (manifestData != null) {
mJavaPackage = manifestData.getPackage();
                        mMinSdkVersion = manifestData.getMinSdkVersionString();
}

mCheckedManifestXml = true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index a901b8a..f122e1a 100644

//Synthetic comment -- @@ -956,7 +956,7 @@
launchInfo.getLaunchAction(),
apk,
manifestData.getDebuggable(),
                    manifestData.getMinSdkVersionString(),
launchInfo.getLaunch(),
launchInfo.getMonitor());









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigDelegate.java
//Synthetic comment -- index a573e6f..70a9c26 100644

//Synthetic comment -- @@ -316,7 +316,7 @@
// the rest
controller.launch(project, mode, applicationPackage,manifestData.getPackage(),
manifestData.getPackage(), manifestData.getDebuggable(),
                manifestData.getMinSdkVersionString(), launchAction, config, androidLaunch,
monitor);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java
//Synthetic comment -- index c3054c2..1f14605 100755

//Synthetic comment -- @@ -90,7 +90,7 @@
IAndroidLaunchAction junitLaunch = new AndroidJUnitLaunchAction(junitLaunchInfo);

controller.launch(project, mode, applicationPackage, testAppPackage, targetAppPackage,
                manifestData.getDebuggable(), manifestData.getMinSdkVersionString(),
junitLaunch, config, androidLaunch, monitor);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index b424c49..bd50e16 100644

//Synthetic comment -- @@ -1004,7 +1004,7 @@
String minSdkVersion = null;
try {
packageName = manifestData.getPackage();
            minSdkVersion = manifestData.getMinSdkVersionString();

// try to get the first launcher activity. If none, just take the first activity.
activity = manifestData.getLauncherActivity();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java
//Synthetic comment -- index 49aceb5..3531013 100755

//Synthetic comment -- @@ -833,7 +833,7 @@
if (manifestData != null) {
String appName = String.format("%1$sTest", project.getName());
String packageName = manifestData.getPackage();
                String minSdkVersion = manifestData.getMinSdkVersionString();
IAndroidTarget sdkTarget = null;
if (Sdk.getCurrent() != null) {
sdkTarget = Sdk.getCurrent().getTarget(project);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index 72014e4..5e37c08 100644

//Synthetic comment -- @@ -26,7 +26,10 @@
import com.android.sdklib.io.IAbstractResource;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.io.IAbstractFolder.FilenameFilter;
import com.android.sdklib.xml.AndroidManifestParser;
import com.android.sdklib.xml.ManifestData;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
//Synthetic comment -- @@ -37,8 +40,11 @@
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Helper to export multiple APKs from 1 or or more projects.
 */
public class MultiApkExportHelper {

private final String mAppPackage;
//Synthetic comment -- @@ -87,6 +93,20 @@
}
}

    /**
     * Simple class to hold a {@link ManifestData} and the {@link IAbstractFile} representing
     * the parsed manifest file.
     */
    private static class Manifest {
        final IAbstractFile file;
        final ManifestData data;

        Manifest(IAbstractFile file, ManifestData data) {
            this.file = file;
            this.data = data;
        }
    }

public MultiApkExportHelper(String appPackage, int versionCode, Target target) {
mAppPackage = appPackage;
mVersionCode = versionCode;
//Synthetic comment -- @@ -186,6 +206,7 @@
String[] paths = projects.split("\\:");

ArrayList<ApkData> datalist = new ArrayList<ApkData>();
        ArrayList<Manifest> manifests = new ArrayList<Manifest>();

for (String path : paths) {
File projectFolder = new File(path);
//Synthetic comment -- @@ -212,7 +233,7 @@
SdkConstants.FN_ANDROID_MANIFEST_XML));
}

                ArrayList<ApkData> datalist2 = checkManifest(androidManifest, manifests);

// if the method returns without throwing, this is a good project to
// export.
//Synthetic comment -- @@ -243,30 +264,61 @@
* If the manifest is correct, a list of apk to export is created and returned.
*
* @param androidManifest the manifest to check
     * @param manifests list of manifests that were already parsed. Must be filled with the current
     * manifest being checked.
* @return A new non-null {@link ArrayList} of {@link ApkData}.
* @throws ExportException in case of error.
*/
    private ArrayList<ApkData> checkManifest(IAbstractFile androidManifest,
            ArrayList<Manifest> manifests) throws ExportException {
try {
            ManifestData manifestData = AndroidManifestParser.parse(androidManifest);

            String manifestPackage = manifestData.getPackage();
if (mAppPackage.equals(manifestPackage) == false) {
throw new ExportException(String.format(
"%1$s package value is not valid. Found '%2$s', expected '%3$s'.",
androidManifest.getOsLocation(), manifestPackage, mAppPackage));
}

            if (manifestData.getVersionCode() != null) {
throw new ExportException(String.format(
"%1$s is not valid: versionCode must not be set for multi-apk export.",
androidManifest.getOsLocation()));
}

            int minSdkVersion = manifestData.getMinSdkVersion();
            if (minSdkVersion == 0) { // means it's a codename
throw new ExportException(
"Codename in minSdkVersion is not supported by multi-apk export.");
}

            // compare to other existing manifest.
            for (Manifest previousManifest : manifests) {
                // Multiple apk export support difference in:
                // - min SDK Version
                // - Screen version
                // - GL version
                // - ABI (not managed at the Manifest level).
                // if those values are the same between 2 manifest, then it's an error.
                if (minSdkVersion == previousManifest.data.getMinSdkVersion() &&
                        compareObjects(manifestData.getSupportsScreens(),
                                previousManifest.data.getSupportsScreens()) &&
                        manifestData.getGlEsVersion() == previousManifest.data.getGlEsVersion()) {
                    throw new ExportException(String.format(
                            "Android manifests must differ in at least one of the following values:\n" +
                            "- minSdkVersion\n" +
                            "- SupportsScreen\n" +
                            "- GL ES version.\n" +
                            "%1$s and %2$s are considered identical for multi-apk export.",
                            androidManifest.getOsLocation(),
                            previousManifest.file.getOsLocation()));
                }
            }

            // add the current manifest to the list
            manifests.add(new Manifest(androidManifest, manifestData));

ArrayList<ApkData> dataList = new ArrayList<ApkData>();
ApkData data = new ApkData();
dataList.add(data);
//Synthetic comment -- @@ -301,16 +353,33 @@
}

return dataList;
        } catch (SAXException e) {
            throw new ExportException(
                    String.format("Failed to validate %1$s", androidManifest.getOsLocation()), e);
        } catch (IOException e) {
throw new ExportException(
String.format("Failed to validate %1$s", androidManifest.getOsLocation()), e);
} catch (StreamException e) {
throw new ExportException(
String.format("Failed to validate %1$s", androidManifest.getOsLocation()), e);
        } catch (ParserConfigurationException e) {
            throw new ExportException(
                    String.format("Failed to validate %1$s", androidManifest.getOsLocation()), e);
}
}

/**
     * Returns whether the two objects are equal, handling cases where one or both are null.
     */
    private boolean compareObjects(Object obj1, Object obj2) {
        if (obj1 == null) {
            return obj2 == null;
        }

        return obj1.equals(obj2);
    }

    /**
* Loads and returns a list of {@link ApkData} from a build log.
* @param log
* @return A new non-null, possibly empty, array of {@link ApkData}.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java
//Synthetic comment -- index 5d65356..ee027d4 100644

//Synthetic comment -- @@ -174,9 +174,9 @@

mValidLevel++;
} else if (AndroidManifest.NODE_USES_SDK.equals(localName)) {
                                mManifestData.setMinSdkVersionString(getAttributeValue(attributes,
AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION,
                                        true /* hasNamespace */));
} else if (AndroidManifest.NODE_INSTRUMENTATION.equals(localName)) {
processInstrumentationNode(attributes);

//Synthetic comment -- @@ -472,21 +472,21 @@
private void processSupportsScreensNode(Attributes attributes) {
mManifestData.mSupportsScreens = new SupportsScreens();

            mManifestData.mSupportsScreens.setResizeable(Boolean.valueOf(
getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_RESIZEABLE, true /*hasNamespace*/)));
            mManifestData.mSupportsScreens.setAnyDensity(Boolean.valueOf(
getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_ANYDENSITY, true /*hasNamespace*/)));
            mManifestData.mSupportsScreens.setSmallScreens(Boolean.valueOf(
getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_SMALLSCREENS, true /*hasNamespace*/)));
            mManifestData.mSupportsScreens.setNormalScreens(Boolean.valueOf(
getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_NORMALSCREENS, true /*hasNamespace*/)));
            mManifestData.mSupportsScreens.setLargeScreens(Boolean.valueOf(
getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_LARGESCREENS, true /*hasNamespace*/)));
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
//Synthetic comment -- index 71eaba5..5b232c6 100644

//Synthetic comment -- @@ -41,7 +41,10 @@
/** debuggable attribute value. If null, the attribute is not present. */
Boolean mDebuggable = null;
/** API level requirement. if null the attribute was not present. */
    private String mMinSdkVersionString = null;
    /** API level requirement. Default is 1 even if missing. If value is a codename, then it'll be
     * 0 instead. */
    private int mMinSdkVersion = 1;
/** List of all instrumentations declared by the manifest */
final ArrayList<Instrumentation> mInstrumentations =
new ArrayList<Instrumentation>();
//Synthetic comment -- @@ -137,12 +140,12 @@
/**
* Class representing the <code>supports-screens</code> node in the manifest.
*/
    public final static class SupportsScreens implements Comparable<SupportsScreens> {
        private Boolean mResizeable;
        private Boolean mAnyDensity;
        private Boolean mSmallScreens;
        private Boolean mLargeScreens;
        private Boolean mNormalScreens;

/**
* returns the value of the <code>resizeable</code> attribute or null if not present.
//Synthetic comment -- @@ -151,6 +154,10 @@
return mResizeable;
}

        void setResizeable(Boolean resizeable) {
            mResizeable = getConstantBoolean(resizeable);
        }

/**
* returns the value of the <code>anyDensity</code> attribute or null if not present.
*/
//Synthetic comment -- @@ -158,6 +165,10 @@
return mAnyDensity;
}

        void setAnyDensity(Boolean anyDensity) {
            mAnyDensity = getConstantBoolean(anyDensity);
        }

/**
* returns the value of the <code>smallScreens</code> attribute or null if not present.
*/
//Synthetic comment -- @@ -165,6 +176,10 @@
return mSmallScreens;
}

        void setSmallScreens(Boolean smallScreens) {
            mSmallScreens = getConstantBoolean(smallScreens);
        }

/**
* returns the value of the <code>normalScreens</code> attribute or null if not present.
*/
//Synthetic comment -- @@ -172,12 +187,57 @@
return mNormalScreens;
}

        void setNormalScreens(Boolean normalScreens) {
            mNormalScreens = getConstantBoolean(normalScreens);
        }

/**
* returns the value of the <code>largeScreens</code> attribute or null if not present.
*/
public Boolean getLargeScreens() {
return mLargeScreens;
}

        void setLargeScreens(Boolean largeScreens) {
            mLargeScreens = getConstantBoolean(largeScreens);
        }

        /**
         * Returns either {@link Boolean#TRUE} or {@link Boolean#FALSE} based on the value of
         * the given Boolean object.
         */
        private Boolean getConstantBoolean(Boolean v) {
            if (v != null) {
                if (v.equals(Boolean.TRUE)) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }

            return null;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SupportsScreens) {
                SupportsScreens support = (SupportsScreens)obj;
                // since all the fields are guaranteed to be either Boolean.TRUE or Boolean.FALSE
                // (or null), we can simply check they are identical and not bother with
                // calling equals (which would require to check != null.
                // see #getConstanntBoolean(Boolean)
                return mResizeable == support.mResizeable && mAnyDensity == support.mAnyDensity &&
                        mSmallScreens == support.mSmallScreens &&
                        mNormalScreens == support.mNormalScreens &&
                        mLargeScreens == support.mLargeScreens;
            }

            return false;
        }

        public int compareTo(SupportsScreens o) {
            return 0;
        }
}

/**
//Synthetic comment -- @@ -320,8 +380,29 @@
/**
* Returns the <code>minSdkVersion</code> attribute, or null if it's not set.
*/
    public String getMinSdkVersionString() {
        return mMinSdkVersionString;
    }

    /**
     * Sets the value of the <code>minSdkVersion</code> attribute.
     * @param apiLevelRequirement
     */
    public void setMinSdkVersionString(String minSdkVersion) {
        mMinSdkVersionString = minSdkVersion;
        try {
            mMinSdkVersion = Integer.parseInt(mMinSdkVersionString);
        } catch (NumberFormatException e) {
            mMinSdkVersion = 0; // 0 means it's a codename.
        }
    }

    /**
     * Returns the <code>minSdkVersion</code> attribute, or 0 if it's not set or is a codename.
     * @see #getMinSdkVersionString()
     */
    public int getMinSdkVersion() {
        return mMinSdkVersion;
}

/**
//Synthetic comment -- @@ -349,6 +430,15 @@
return mFeatures.toArray(new UsesFeature[mFeatures.size()]);
}

    public int getGlEsVersion() {
        for (UsesFeature feature : mFeatures) {
            if (feature.mGlEsVersion > 0) {
                return feature.mGlEsVersion;
            }
        }
        return -1;
    }

/**
* Returns the {@link SupportsScreens} object representing the <code>supports-screens</code>
* node, or null if the node doesn't exist at all.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java
//Synthetic comment -- index 1936af4..b54f8ef 100644

//Synthetic comment -- @@ -80,7 +80,7 @@
}

public void testMinSdkVersion() {
        assertEquals(7, mManifestTestApp.getMinSdkVersion());
}

public void testGetActivities() {







