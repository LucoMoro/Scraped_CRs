/*Rework the multi-apk log file(s).

Move away from a single log file used for:
- tell the dev what file was created with that properties
- used to increment minor versionCode for specific apks
- used to detect config change from what export to another.

There are now three files for each case, with the last two
using a never changing filename. Only a new build log file
is created at each export.

Change-Id:Ia9b464e6ffefe24463a537ee48d0a20a7a004af7*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index d22b1e1..9e36df3 100644

//Synthetic comment -- @@ -18,10 +18,9 @@

import com.android.sdklib.internal.export.ApkData;
import com.android.sdklib.internal.export.MultiApkExportHelper;
import com.android.sdklib.internal.export.MultiApkExportHelper.ExportException;
import com.android.sdklib.internal.export.MultiApkExportHelper.Target;
import com.android.sdklib.io.FileWrapper;
import com.android.sdklib.io.IAbstractFile;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
//Synthetic comment -- @@ -35,7 +34,9 @@
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
//Synthetic comment -- @@ -86,79 +87,81 @@
}
System.out.println("versionCode: " + version);

        // checks whether the projects can be signed.
        boolean canSign = false;
        String keyStore = null, keyAlias = null;
        if (mTarget == Target.RELEASE) {
            String value = antProject.getProperty("key.store");
            keyStore = value != null && value.length() > 0 ? value : null;
            value = antProject.getProperty("key.alias");
            keyAlias = value != null && value.length() > 0 ? value : null;
            canSign = keyStore != null && keyAlias != null;
        }

// get the list of projects
        String projects = getValidatedProperty(antProject, "projects");

        // look to see if there's an export log from a previous export
        IAbstractFile log = getBuildLog(appPackage, versionCode);

        MultiApkExportHelper helper = new MultiApkExportHelper(appPackage, versionCode, mTarget);
try {
            ApkData[] apks = helper.getProjects(projects, log);

            // some temp var used by the project loop
            HashSet<String> compiledProject = new HashSet<String>();
            mXPathFactory = XPathFactory.newInstance();

            File exportProjectOutput = new File(getValidatedProperty(antProject,
                    "out.absolute.dir"));

            // if there's no error, and we can sign, prompt for the passwords.
            String keyStorePassword = null;
            String keyAliasPassword = null;
            if (canSign) {
                System.out.println("Found signing keystore and key alias. Need passwords.");

                Input input = new Input();
                input.setProject(antProject);
                input.setAddproperty("key.store.password");
                input.setMessage(String.format("Please enter keystore password (store: %1$s):",
                        keyStore));
                input.execute();

                input = new Input();
                input.setProject(antProject);
                input.setAddproperty("key.alias.password");
                input.setMessage(String.format("Please enter password for alias '%1$s':",
                        keyAlias));
                input.execute();

                // and now read the property so that they can be set into the sub ant task.
                keyStorePassword = getValidatedProperty(antProject, "key.store.password");
                keyAliasPassword = getValidatedProperty(antProject, "key.alias.password");
            }

            for (ApkData apk : apks) {

                Map<String, String> variantMap = apk.getSoftVariantMap();

                // first, do the full export.
                makeSubAnt(antProject, appPackage, versionCode, apk, null,
                        exportProjectOutput, canSign, keyStore, keyAlias,
                        keyStorePassword, keyAliasPassword, compiledProject);

                // then do the soft variants.
                for (Entry<String, String> entry : variantMap.entrySet()) {
                    makeSubAnt(antProject, appPackage, versionCode, apk, entry,
                            exportProjectOutput, canSign, keyStore, keyAlias,
                            keyStorePassword, keyAliasPassword, compiledProject);
}

            }

            if (mTarget == Target.RELEASE) {
                helper.makeBuildLog(log, apks);
}
} catch (ExportException e) {
// we only want to have Ant display the message, not the stack trace, since
//Synthetic comment -- @@ -189,15 +192,17 @@
boolean canSign, String keyStore, String keyAlias,
String keyStorePassword, String keyAliasPassword, Set<String> compiledProject) {

// this output is prepended by "[android-export] " (17 chars), so we put 61 stars
System.out.println("\n*************************************************************");
        System.out.println("Exporting project: " + apk.getRelativePath());

SubAnt subAnt = new SubAnt();
subAnt.setTarget(mTarget.getTarget());
subAnt.setProject(antProject);

        File subProjectFolder = new File(antProject.getBaseDir(), apk.getRelativePath());

FileSet fileSet = new FileSet();
fileSet.setProject(antProject);
//Synthetic comment -- @@ -213,8 +218,8 @@
// this project.
// (projects can be export multiple time if some properties are set up to
// generate more than one APK (for instance ABI split).
            if (compiledProject.contains(apk.getRelativePath()) == false) {
                compiledProject.add(apk.getRelativePath());
} else {
addProp(subAnt, "do.not.compile", "true");
}
//Synthetic comment -- @@ -323,7 +328,6 @@
return value;
}


/**
* Adds a property to a {@link SubAnt} task.
* @param task the task.
//Synthetic comment -- @@ -336,14 +340,4 @@
prop.setValue(value);
task.addProperty(prop);
}

    /**
     * Returns the {@link File} for the build log.
     * @param appPackage
     * @param versionCode
     * @return A new non-null {@link IAbstractFile} mapping to the build log.
     */
    private IAbstractFile getBuildLog(String appPackage, int versionCode) {
        return new FileWrapper(appPackage + "." + versionCode + ".log");
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index 471070a..ee7810c 100644

//Synthetic comment -- @@ -16,15 +16,10 @@

package com.android.sdklib.internal.export;

import com.android.sdklib.internal.project.ApkSettings;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.SupportsScreens;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
* Class representing one apk that needs to be generated. This contains
//Synthetic comment -- @@ -33,17 +28,13 @@
* This class is meant to be sortable in a way that allows generation of the buildInfo
* value that goes in the composite versionCode.
*/
public class ApkData implements Comparable<ApkData> {

    private static final String PROP_SCREENS = "screens";
    private static final String PROP_ABI = "abi";
    private static final String PROP_GL = "gl";
    private static final String PROP_API = "api";
private static final String PROP_PROJECT = "project";
    private static final String PROP_MINOR = "minor";
private static final String PROP_BUILDINFO = "buildinfo";
    private static final String PROP_DENSITY = "splitDensity";
    private static final String PROP_LOCALEFILTERS = "localeFilters";

/**
* List of ABI order.
//Synthetic comment -- @@ -57,51 +48,37 @@
new String[] { "armeabi", "armeabi-v7a" }
};

    /**
     * List of densities and their associated aapt filter.
     */
    private static final String[][] DENSITY_LIST = new String[][] {
        new String[] { "hdpi", "hdpi,nodpi" },
        new String[] { "mdpi", "mdpi,nodpi" },
        new String[] { "ldpi", "ldpi,nodpi" },
    };

private final HashMap<String, String> mOutputNames = new HashMap<String, String>();
    private String mRelativePath;
    private File mProject;
private int mBuildInfo;
private int mMinor;

// the following are used to sort the export data and generate buildInfo
    private int mMinSdkVersion;
    private String mAbi;
    private int mGlVersion = ManifestData.GL_ES_VERSION_NOT_SET;
    private SupportsScreens mSupportsScreens;

    // additional apk generation that doesn't impact the build info.
    private boolean mSplitDensity;
    private final HashMap<String, String> mLocaleFilters = new HashMap<String, String>();
    private Map<String, String> mSoftVariantMap;

    ApkData() {
        // do nothing.
}

    ApkData(int minSdkVersion, SupportsScreens supportsScreens, int glEsVersion) {
        mMinSdkVersion = minSdkVersion;
        mSupportsScreens = supportsScreens;
        mGlVersion = glEsVersion;
}

    ApkData(ApkData data) {
        mRelativePath = data.mRelativePath;
        mProject = data.mProject;
        mBuildInfo = data.mBuildInfo;
        mMinor = data.mBuildInfo;
        mMinSdkVersion = data.mMinSdkVersion;
        mAbi = data.mAbi;
        mGlVersion = data.mGlVersion;
        mSupportsScreens = data.mSupportsScreens;
}

public String getOutputName(String key) {
//Synthetic comment -- @@ -112,22 +89,6 @@
mOutputNames.put(key, outputName);
}

    public String getRelativePath() {
        return mRelativePath;
    }

    void setRelativePath(String relativePath) {
        mRelativePath = relativePath;
    }

    public File getProject() {
        return mProject;
    }

    void setProject(File project) {
        mProject = project;
    }

public int getBuildInfo() {
return mBuildInfo;
}
//Synthetic comment -- @@ -144,24 +105,12 @@
mMinor = minor;
}

    public int getMinSdkVersion() {
        return mMinSdkVersion;
    }

public String getAbi() {
return mAbi;
}

    void setAbi(String abi) {
        mAbi = abi;
    }

    public int getGlVersion() {
        return mGlVersion;
    }

    public SupportsScreens getSupportsScreens() {
        return mSupportsScreens;
}

/**
//Synthetic comment -- @@ -177,50 +126,6 @@
return trueVersionCode;
}

    synchronized void setSplitDensity(boolean splitDensity) {
        mSplitDensity = splitDensity;
        mSoftVariantMap = null;

    }

    synchronized void setLocaleFilters(Map<String, String> localeFilters) {
        mLocaleFilters.clear();
        mLocaleFilters.putAll(localeFilters);
        mSoftVariantMap = null;
    }

    /**
     * Returns a map of pair values (apk name suffix, aapt res filter) to be used to generate
     * multiple soft apk variants.
     */
    public synchronized Map<String, String> getSoftVariantMap() {
        if (mSoftVariantMap == null) {
            HashMap<String, String> map = new HashMap<String, String>();

            if (mSplitDensity && mLocaleFilters.size() > 0) {
                for (String[] density : DENSITY_LIST) {
                    for (Entry<String,String> entry : mLocaleFilters.entrySet()) {
                        map.put(density[0] + "-" + entry.getKey(),
                                density[1] + "," + entry.getValue());
                    }
                }

            } else if (mSplitDensity) {
                for (String[] density : DENSITY_LIST) {
                    map.put(density[0], density[1]);
                }

            } else if (mLocaleFilters.size() > 0) {
                map.putAll(mLocaleFilters);

            }

            mSoftVariantMap = Collections.unmodifiableMap(map);
        }

        return mSoftVariantMap;
    }

@Override
public String toString() {
return getLogLine(null);
//Synthetic comment -- @@ -229,69 +134,62 @@
public String getLogLine(String key) {
StringBuilder sb = new StringBuilder();
sb.append(getOutputName(key)).append(':');
        if (key == null) {
            write(sb, PROP_BUILDINFO, mBuildInfo);
            write(sb, PROP_MINOR, mMinor);
            write(sb, PROP_PROJECT, mRelativePath);
            write(sb, PROP_API, mMinSdkVersion);

            if (mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
                write(sb, PROP_GL, "0x" + Integer.toHexString(mGlVersion));
            }

            if (mAbi != null) {
                write(sb, PROP_ABI, mAbi);
            }

            if (mSplitDensity) {
                write(sb, PROP_DENSITY, Boolean.toString(true));
            }

            if (mLocaleFilters.size() > 0) {
                write(sb, PROP_LOCALEFILTERS, ApkSettings.writeLocaleFilters(mLocaleFilters));
            }

            write(sb, PROP_SCREENS, mSupportsScreens.getEncodedValues());
        } else {
            write(sb, "resources", getSoftVariantMap().get(key));
}

return sb.toString();
}

public int compareTo(ApkData o) {
        int minSdkDiff = mMinSdkVersion - o.mMinSdkVersion;
if (minSdkDiff != 0) {
return minSdkDiff;
}

// only compare if they have don't have the same size support. This is because
// this compare method throws an exception if the values cannot be compared.
        if (mSupportsScreens.hasSameScreenSupportAs(o.mSupportsScreens) == false) {
            return mSupportsScreens.compareScreenSizesWith(o.mSupportsScreens);
}

int comp;
        if (mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
            if (o.mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
                comp = mGlVersion - o.mGlVersion;
if (comp != 0) return comp;
} else {
return -1;
}
        } else if (o.mGlVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
return 1;
}

// here the returned value is only important if both abi are non null.
if (mAbi != null && o.mAbi != null) {
comp = compareAbi(mAbi, o.mAbi);
if (comp != 0) return comp;
}

        // Do not compare mSplitDensity or mLocaleFilter because they do not generate build info,
        // and also, we should already have a difference at this point.

return 0;
}

//Synthetic comment -- @@ -317,89 +215,4 @@

return 0;
}

    public boolean hasSameApkProperties(ApkData apk) {
        if (mMinSdkVersion != apk.mMinSdkVersion ||
                mSupportsScreens.equals(apk.mSupportsScreens) == false ||
                mGlVersion != apk.mGlVersion ||
                mSplitDensity != apk.mSplitDensity ||
                mLocaleFilters.equals(apk.mLocaleFilters) == false) {
            return false;
        }

        if (mAbi != null) {
            if (mAbi.equals(apk.mAbi) == false) {
                return false;
            }
        } else if (apk.mAbi != null) {
            return false;
        }

        return true;
    }

    /**
     * reads the apk description from a log line.
     * @param line The fields to read, comma-separated.
     *
     * @see #getLogLine()
     */
    public void initFromLogLine(String line) {
        int colon = line.indexOf(':');
        mOutputNames.put(null, line.substring(0, colon));
        String[] properties = line.substring(colon+1).split(";");
        HashMap<String, String> map = new HashMap<String, String>();
        for (String prop : properties) {
            colon = prop.indexOf('=');
            map.put(prop.substring(0, colon), prop.substring(colon+1));
        }
        setValues(map);
    }

    private synchronized void setValues(Map<String, String> values) {
        String tmp;
        try {
            mBuildInfo = Integer.parseInt(values.get(PROP_BUILDINFO));
            mMinor = Integer.parseInt(values.get(PROP_MINOR));
            mRelativePath = values.get(PROP_PROJECT);
            mMinSdkVersion = Integer.parseInt(values.get(PROP_API));

            tmp = values.get(PROP_GL);
            if (tmp != null) {
                    mGlVersion = Integer.decode(tmp);
            }
        } catch (NumberFormatException e) {
            // pass. This is probably due to a manual edit, and it'll most likely
            // generate an error when matching the log to the current setup.
        }

        tmp = values.get(PROP_DENSITY);
        if (tmp != null) {
            mSplitDensity = Boolean.valueOf(tmp);
        }

        tmp = values.get(PROP_ABI);
        if (tmp != null) {
            mAbi = tmp;
        }

        tmp = values.get(PROP_SCREENS);
        if (tmp != null) {
            mSupportsScreens = new SupportsScreens(tmp);
        }

        tmp = values.get(PROP_LOCALEFILTERS);
        if (tmp != null) {
            mLocaleFilters.putAll(ApkSettings.readLocaleFilters(tmp));
        }
        mSoftVariantMap = null;
    }

    private void write(StringBuilder sb, String name, Object value) {
        sb.append(name + "=").append(value).append(';');
    }

    private void write(StringBuilder sb, String name, int value) {
        sb.append(name + "=").append(value).append(';');
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/LogHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/LogHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..b324b4d

//Synthetic comment -- @@ -0,0 +1,38 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index dad57e7..aa0d81b 100644

//Synthetic comment -- @@ -17,15 +17,9 @@
package com.android.sdklib.internal.export;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ApkSettings;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.io.FileWrapper;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.IAbstractResource;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.io.IAbstractFolder.FilenameFilter;
import com.android.sdklib.xml.AndroidManifestParser;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.SupportsScreens;
//Synthetic comment -- @@ -34,11 +28,14 @@

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//Synthetic comment -- @@ -46,18 +43,29 @@

/**
* Helper to export multiple APKs from 1 or or more projects.
*/
public class MultiApkExportHelper {

private final String mAppPackage;
private final int mVersionCode;
private final Target mTarget;

final static int MAX_MINOR = 100;
final static int MAX_BUILDINFO = 100;
final static int OFFSET_BUILD_INFO = MAX_MINOR;
final static int OFFSET_VERSION_CODE = OFFSET_BUILD_INFO * MAX_BUILDINFO;

public static final class ExportException extends Exception {
private static final long serialVersionUID = 1L;

//Synthetic comment -- @@ -65,6 +73,14 @@
super(message);
}

public ExportException(String message, Throwable cause) {
super(message, cause);
}
//Synthetic comment -- @@ -95,101 +111,109 @@
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
mTarget = target;
}

    public ApkData[] getProjects(String projects, IAbstractFile buildLog) throws ExportException {
        // get the list of apk to export and their configuration.
        ApkData[] apks = getProjects(projects);

        // look to see if there's an export log from a previous export
        if (mTarget == Target.RELEASE && buildLog != null && buildLog.exists()) {
            // load the log and compare to current export list.
            // Any difference will force a new versionCode.
            ApkData[] previousApks = getProjects(buildLog);

            if (previousApks.length != apks.length) {
                throw new ExportException(String.format(
                        "Project export is setup differently from previous export at versionCode %d.\n" +
                        "Any change in the multi-apk configuration requires an increment of the versionCode in export.properties.",
                        mVersionCode));
            }

            for (int i = 0 ; i < previousApks.length ; i++) {
                // update the minor value from what is in the log file.
                apks[i].setMinor(previousApks[i].getMinor());
                if (apks[i].hasSameApkProperties(previousApks[i]) == false) {
                    throw new ExportException(String.format(
                            "Project export is setup differently from previous export at versionCode %d.\n" +
                            "Any change in the multi-apk configuration requires an increment of the versionCode.",
                            mVersionCode));
                }
            }
}

        return apks;
}

/**
     * Writes the build log for a given list of {@link ApkData}.
     * @param buildLog the build log file into which to write the log.
     * @param apks the list of apks that were exported.
* @throws ExportException
*/
    public void makeBuildLog(IAbstractFile buildLog, ApkData[] apks) throws ExportException {
OutputStreamWriter writer = null;
try {
            writer = new OutputStreamWriter(buildLog.getOutputStream());

            writer.append(
                    "# Multi-APK BUILD LOG.\n" +
                    "# This file serves two purpose:\n" +
                    "# - A log of what was built, showing what went in each APK and their properties.\n" +
                    "#   You can refer to this if you get a bug report for a specific versionCode.\n" +
                    "# - A way to update builds through minor revisions for specific APKs.\n" +
                    "# Only edit manually to change the minor properties for build you wish to respin.\n" +
                    "# Note that all APKs will be regenerated all the time.\n");

writeValue(writer, "package", mAppPackage);
writeValue(writer, "versionCode", mVersionCode);

            writer.append(
                    "# The format of the following lines is:\n" +
                    "# <filename>:<property1>;<property2>;<property3>;...\n" +
                    "# Properties are written as <name>=<value>\n");

            for (ApkData apk : apks) {
                writer.append(apk.getLogLine(null));
                writer.append('\n');

                // display the soft variants for this apkData as comments to the log file.
                // since they all share the same Build Info and cannot be modified by the dev
                // and we won't read them back from the log.
Map<String, String> softVariants = apk.getSoftVariantMap();
if (softVariants.size() > 0) {
                    writer.append(" # Soft Variants -- DO NOT UNCOMMENT:\n");
                }

                for (String softVariant : softVariants.keySet()) {
                    writer.append(" # ");
                    writer.append(apk.getLogLine(softVariant));
writer.append('\n');
}
}
//Synthetic comment -- @@ -217,109 +241,98 @@
writeValue(writer, name, Integer.toString(value));
}

    /**
     * gets the projects to export from the property, checks they exist, validates them,
     * loads their export info and return it.
     * If a project does not exist or is not valid, this will throw a {@link BuildException}.
     * @param projects the string containing all the relative paths to the projects. This is
     * usually read from export.properties.
     * @throws ExportException
     */
    private ApkData[] getProjects(String projects) throws ExportException {
        String[] paths = projects.split("\\:");

        ArrayList<ApkData> datalist = new ArrayList<ApkData>();
        ArrayList<Manifest> manifests = new ArrayList<Manifest>();

        for (String path : paths) {
            File projectFolder = new File(path);

            // resolve the path (to remove potential ..)
            try {
                projectFolder = projectFolder.getCanonicalFile();

                // project folder must exist and be a directory
                if (projectFolder.isDirectory() == false) {
                    throw new ExportException(String.format(
                            "Project folder '%1$s' is not a valid directory.",
                            projectFolder.getAbsolutePath()));
                }

                // Check AndroidManifest.xml is present
                FileWrapper androidManifest = new FileWrapper(projectFolder,
                        SdkConstants.FN_ANDROID_MANIFEST_XML);

                if (androidManifest.isFile() == false) {
                    throw new ExportException(String.format(
                            "%1$s is not a valid project (%2$s not found).",
                            projectFolder.getAbsolutePath(),
                            SdkConstants.FN_ANDROID_MANIFEST_XML));
                }

                ArrayList<ApkData> datalist2 = checkProject(androidManifest, manifests);

                // if the method returns without throwing, this is a good project to
                // export.
                for (ApkData data : datalist2) {
                    data.setRelativePath(path);
                    data.setProject(projectFolder);
                }

                datalist.addAll(datalist2);

            } catch (IOException e) {
                throw new ExportException(String.format("Failed to resolve path %1$s", path), e);
            }
}

// sort the projects and assign buildInfo
        Collections.sort(datalist);
int buildInfo = 0;
        for (ApkData data : datalist) {
data.setBuildInfo(buildInfo++);
}

        return datalist.toArray(new ApkData[datalist.size()]);
}

/**
     * Checks a project inclusion in the list of exported APK.
* <p/>This performs a check on the manifest, as well as gathers more information about
* mutli-apk from the project's default.properties file.
* If the manifest is correct, a list of apk to export is created and returned.
*
     * @param androidManifest the manifest to check
     * @param manifests list of manifests that were already parsed. Must be filled with the current
     * manifest being checked.
     * @return A new non-null {@link ArrayList} of {@link ApkData}.
* @throws ExportException in case of error.
*/
    private ArrayList<ApkData> checkProject(IAbstractFile androidManifest,
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
if (minSdkVersion == ManifestData.MIN_SDK_CODENAME) {
throw new ExportException(
"Codename in minSdkVersion is not supported by multi-apk export.");
}

            // compare to other existing manifest.
            for (Manifest previousManifest : manifests) {
// Multiple apk export support difference in:
// - min SDK Version
// - Screen version
//Synthetic comment -- @@ -327,26 +340,27 @@
// - ABI (not managed at the Manifest level).
// if those values are the same between 2 manifest, then it's an error.

// first the minSdkVersion.
                if (minSdkVersion == previousManifest.data.getMinSdkVersion()) {
// if it's the same compare the rest.
SupportsScreens currentSS = manifestData.getSupportsScreensValues();
                    SupportsScreens previousSS = previousManifest.data.getSupportsScreensValues();
boolean sameSupportsScreens = currentSS.hasSameScreenSupportAs(previousSS);

// if it's the same, then it's an error. Can't export 2 projects that have the
// same approved (for multi-apk export) hard-properties.
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
//Synthetic comment -- @@ -361,176 +375,175 @@
//   (ie APK1 supports small/large and APK2 supports normal).
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

            // add the current manifest to the list
            manifests.add(new Manifest(androidManifest, manifestData));

            ArrayList<ApkData> dataList = new ArrayList<ApkData>();
            ApkData data = new ApkData(minSdkVersion, manifestData.getSupportsScreensValues(),
                    manifestData.getGlEsVersion());
            dataList.add(data);

            // only look for more exports if the target is not clean.
            if (mTarget != Target.CLEAN) {
                // load the project properties
                IAbstractFolder projectFolder = androidManifest.getParentFolder();
                ProjectProperties projectProp = ProjectProperties.load(projectFolder,
                        PropertyType.DEFAULT);
                if (projectProp == null) {
                    throw new ExportException(String.format(
                            "%1$s is missing.", PropertyType.DEFAULT.getFilename()));
                }

                ApkSettings apkSettings = new ApkSettings(projectProp);

                // get the density/locale values
                boolean splitByDensity = apkSettings.isSplitByDensity();
                Map<String, String> localeFilters = apkSettings.getLocaleFilters();

                if (apkSettings.isSplitByAbi()) {
                    // need to find the available ABIs.
                    List<String> abis = findAbis(projectFolder);
                    ApkData current = data;
                    for (String abi : abis) {
                        if (current == null) {
                            current = new ApkData(data);
                            dataList.add(current);
                        }

                        current.setAbi(abi);
                        current.setSplitDensity(splitByDensity);
                        current.setLocaleFilters(localeFilters);

                        current = null;
                    }
                }
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
     * Loads and returns a list of {@link ApkData} from a build log.
     * @param log
     * @return A new non-null, possibly empty, array of {@link ApkData}.
     * @throws ExportException
     * @throws BuildException in case of error.
*/
    private ApkData[] getProjects(IAbstractFile buildLog) throws ExportException {
        ArrayList<ApkData> datalist = new ArrayList<ApkData>();

InputStreamReader reader = null;
BufferedReader bufferedReader = null;
try {
            reader = new InputStreamReader(buildLog.getContents());
bufferedReader = new BufferedReader(reader);
String line;
            int lineIndex = 0;
while ((line = bufferedReader.readLine()) != null) {
line = line.trim();
if (line.length() == 0 || line.startsWith("#")) {
continue;
}

                switch (lineIndex) {
                    case 0:
                        // read package value
                        lineIndex++;
                        break;
                    case 1:
                        // read versionCode value
                        lineIndex++;
                        break;
                    default:
                        // read apk description
                        ApkData data = new ApkData();
                        datalist.add(data);
                        data.initFromLogLine(line);
                        if (data.getMinor() >= MAX_MINOR) {
                            throw new ExportException(
                                    "Valid minor version code values are 0-" + (MAX_MINOR-1));
                        }
                        break;
}
}
} catch (IOException e) {
            throw new ExportException("Failed to read existing build log", e);
        } catch (StreamException e) {
            throw new ExportException("Failed to read existing build log", e);
} finally {
try {
if (reader != null) {
reader.close();
}
} catch (IOException e) {
                throw new ExportException("Failed to read existing build log", e);
}
}

        return datalist.toArray(new ApkData[datalist.size()]);
}

    /**
     * Finds ABIs in a project folder. This is based on the presence of libs/<abi>/ folder.
     *
     * @param projectPath The OS path of the project.
     * @return A new non-null, possibly empty, list of ABI strings.
     */
    private List<String> findAbis(IAbstractFolder projectFolder) {
        ArrayList<String> abiList = new ArrayList<String>();
        IAbstractFolder libs = projectFolder.getFolder(SdkConstants.FD_NATIVE_LIBS);
        if (libs.exists()) {
            IAbstractResource[] abis = libs.listMembers();
            for (IAbstractResource abi : abis) {
                if (abi instanceof IAbstractFolder && abi.exists()) {
                    // only add the abi folder if there are .so files in it.
                    String[] content = ((IAbstractFolder)abi).list(new FilenameFilter() {
                        public boolean accept(IAbstractFolder dir, String name) {
                            return name.toLowerCase().endsWith(".so");
                        }
                    });

                    if (content.length > 0) {
                        abiList.add(abi.getName());
                    }
                }
            }
        }

        return abiList;
    }


}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java
new file mode 100644
//Synthetic comment -- index 0000000..b413ea0

//Synthetic comment -- @@ -0,0 +1,300 @@







