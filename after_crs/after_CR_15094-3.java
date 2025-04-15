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
//Synthetic comment -- index d22b1e1..7666fbc 100644

//Synthetic comment -- @@ -18,10 +18,9 @@

import com.android.sdklib.internal.export.ApkData;
import com.android.sdklib.internal.export.MultiApkExportHelper;
import com.android.sdklib.internal.export.ProjectConfig;
import com.android.sdklib.internal.export.MultiApkExportHelper.ExportException;
import com.android.sdklib.internal.export.MultiApkExportHelper.Target;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
//Synthetic comment -- @@ -36,6 +35,7 @@
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
//Synthetic comment -- @@ -86,79 +86,84 @@
}
System.out.println("versionCode: " + version);

// get the list of projects
        String projectList = getValidatedProperty(antProject, "projects");

        File rootFolder = antProject.getBaseDir();
        MultiApkExportHelper helper = new MultiApkExportHelper(rootFolder.getAbsolutePath(),
                appPackage, versionCode, mTarget);

try {
            if (mTarget == Target.CLEAN) {
                // for a clean, we don't need the list of ApkData, we only need the list of
                // projects
                List<ProjectConfig> projects = helper.getProjects(projectList);
                for (ProjectConfig projectConfig : projects) {
                    executeCleanSubAnt(antProject, projectConfig);
                }
            } else {
                // checks whether the projects can be signed.
                String value = antProject.getProperty("key.store");
                String keyStore = value != null && value.length() > 0 ? value : null;
                value = antProject.getProperty("key.alias");
                String keyAlias = value != null && value.length() > 0 ? value : null;
                boolean canSign = keyStore != null && keyAlias != null;

                List<ApkData> apks = helper.getApkData(projectList);

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

                    if (variantMap.size() > 0) {
                        // if there are soft variants, only export those.
                        for (Entry<String, String> entry : variantMap.entrySet()) {
                            executeReleaseSubAnt(antProject, appPackage, versionCode, apk, entry,
                                    exportProjectOutput, canSign, keyStore, keyAlias,
                                    keyStorePassword, keyAliasPassword, compiledProject);
                        }
                    } else {
                        // do the full export.
                        executeReleaseSubAnt(antProject, appPackage, versionCode, apk, null,
                                exportProjectOutput, canSign, keyStore, keyAlias,
                                keyStorePassword, keyAliasPassword, compiledProject);

                    }
                }

                helper.writeLogs();
}
} catch (ExportException e) {
// we only want to have Ant display the message, not the stack trace, since
//Synthetic comment -- @@ -169,7 +174,43 @@
}

/**
     * Creates and execute a clean sub ant task.
     * @param antProject the current Ant project
     * @param projectConfig the project to clean.
     */
    private void executeCleanSubAnt(Project antProject, ProjectConfig projectConfig) {

        String relativePath = projectConfig.getRelativePath();

        // this output is prepended by "[android-export] " (17 chars), so we put 61 stars
        System.out.println("\n*************************************************************");
        System.out.println("Cleaning project: " + relativePath);

        SubAnt subAnt = new SubAnt();
        subAnt.setTarget(mTarget.getTarget());
        subAnt.setProject(antProject);

        File subProjectFolder = projectConfig.getProjectFolder();

        FileSet fileSet = new FileSet();
        fileSet.setProject(antProject);
        fileSet.setDir(subProjectFolder);
        fileSet.setIncludes("build.xml");
        subAnt.addFileset(fileSet);

        // TODO: send the verbose flag from the main build.xml to the subAnt project.
        //subAnt.setVerbose(true);

        // end of the output by this task. Everything that follows will be output
        // by the subant.
        System.out.println("Calling to project's Ant file...");
        System.out.println("----------\n");

        subAnt.execute();
    }

    /**
     * Creates and executes a release sub ant task.
* @param antProject the current Ant project
* @param appPackage the application package string.
* @param versionCode the current version of the application
//Synthetic comment -- @@ -184,20 +225,22 @@
* @param keyAliasPassword the password of the key alias for signing
* @param compiledProject a list of projects that have already been compiled.
*/
    private void executeReleaseSubAnt(Project antProject, String appPackage, int versionCode,
ApkData apk, Entry<String, String> softVariant, File exportProjectOutput,
boolean canSign, String keyStore, String keyAlias,
String keyStorePassword, String keyAliasPassword, Set<String> compiledProject) {

        String relativePath = apk.getProjectConfig().getRelativePath();

// this output is prepended by "[android-export] " (17 chars), so we put 61 stars
System.out.println("\n*************************************************************");
        System.out.println("Exporting project: " + relativePath);

SubAnt subAnt = new SubAnt();
subAnt.setTarget(mTarget.getTarget());
subAnt.setProject(antProject);

        File subProjectFolder = apk.getProjectConfig().getProjectFolder();

FileSet fileSet = new FileSet();
fileSet.setProject(antProject);
//Synthetic comment -- @@ -208,94 +251,92 @@
// TODO: send the verbose flag from the main build.xml to the subAnt project.
//subAnt.setVerbose(true);

        // only do the compilation part if it's the first time we export
        // this project.
        // (projects can be export multiple time if some properties are set up to
        // generate more than one APK (for instance ABI split).
        if (compiledProject.contains(relativePath) == false) {
            compiledProject.add(relativePath);
        } else {
            addProp(subAnt, "do.not.compile", "true");
        }

        // set the version code, and filtering
        int compositeVersionCode = apk.getCompositeVersionCode(versionCode);
        addProp(subAnt, "version.code", Integer.toString(compositeVersionCode));
        System.out.println("Composite versionCode: " + compositeVersionCode);
        String abi = apk.getAbi();
        if (abi != null) {
            addProp(subAnt, "filter.abi", abi);
            System.out.println("ABI Filter: " + abi);
        }

        // set the output file names/paths. Keep all the temporary files in the project
        // folder, and only put the final file (which is different depending on whether
        // the file can be signed) locally.

        // read the base name from the build.xml file.
        String name = null;
        try {
            File buildFile = new File(subProjectFolder, "build.xml");
            XPath xPath = mXPathFactory.newXPath();
            name = xPath.evaluate("/project/@name",
                    new InputSource(new FileInputStream(buildFile)));
        } catch (XPathExpressionException e) {
            throw new BuildException("Failed to read build.xml", e);
        } catch (FileNotFoundException e) {
            throw new BuildException("build.xml is missing.", e);
        }

        // override the resource pack file as well as the final name
        String pkgName = name + "-" + apk.getBuildInfo();
        String finalNameRoot = appPackage + "-" + compositeVersionCode;
        if (softVariant != null) {
            String tmp = "-" + softVariant.getKey();
            pkgName += tmp;
            finalNameRoot += tmp;

            // set the resource filter.
            addProp(subAnt, "aapt.resource.filter", softVariant.getValue());
            System.out.println("res Filter: " + softVariant.getValue());
        }

        // set the resource pack file name.
        addProp(subAnt, "resource.package.file.name", pkgName + ".ap_");


        if (canSign) {
            // set the properties for the password.
            addProp(subAnt, "key.store", keyStore);
            addProp(subAnt, "key.alias", keyAlias);
            addProp(subAnt, "key.store.password", keyStorePassword);
            addProp(subAnt, "key.alias.password", keyAliasPassword);

            // temporary file only get a filename change (still stored in the project
            // bin folder).
            addProp(subAnt, "out.unsigned.file.name",
                    name + "-" + apk.getBuildInfo() + "-unsigned.apk");
            addProp(subAnt, "out.unaligned.file",
                    name + "-" + apk.getBuildInfo() + "-unaligned.apk");

            // final file is stored locally with a name based on the package
            String outputName = finalNameRoot + "-release.apk";
            apk.setOutputName(softVariant != null ? softVariant.getKey() : null, outputName);
            addProp(subAnt, "out.release.file",
                    new File(exportProjectOutput, outputName).getAbsolutePath());

        } else {
            // put some empty prop. This is to override possible ones defined in the
            // project. The reason is that if there's more than one project, we don't
            // want some to signed and some not to be (and we don't want each project
            // to prompt for password.)
            addProp(subAnt, "key.store", "");
            addProp(subAnt, "key.alias", "");
            // final file is the unsigned version. It gets stored locally.
            String outputName = finalNameRoot + "-unsigned.apk";
            apk.setOutputName(softVariant != null ? softVariant.getKey() : null, outputName);
            addProp(subAnt, "out.unsigned.file",
                    new File(exportProjectOutput, outputName).getAbsolutePath());
}

// end of the output by this task. Everything that follows will be output
//Synthetic comment -- @@ -323,7 +364,6 @@
return value;
}

/**
* Adds a property to a {@link SubAnt} task.
* @param task the task.
//Synthetic comment -- @@ -336,14 +376,4 @@
prop.setValue(value);
task.addProperty(prop);
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index 471070a..b743477 100644

//Synthetic comment -- @@ -16,34 +16,25 @@

package com.android.sdklib.internal.export;

import com.android.sdklib.xml.ManifestData;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing one apk (or more if there are soft variants) that needs to be generated.
 * This contains a link to the project used for the export, and which extra filters should be used.
*
* This class is meant to be sortable in a way that allows generation of the buildInfo
* value that goes in the composite versionCode.
*/
public final class ApkData implements Comparable<ApkData> {

private static final String PROP_PROJECT = "project";
    private static final String PROP_BUILDINFO = "buildInfo";
    private static final String PROP_MINOR = "minorCode";
    private static final String PROP_ABI = "abi";
    private static final String PROP_RESOURCES = "resources";

/**
* List of ABI order.
//Synthetic comment -- @@ -57,51 +48,37 @@
new String[] { "armeabi", "armeabi-v7a" }
};

    private final ProjectConfig mProjectConfig;
private final HashMap<String, String> mOutputNames = new HashMap<String, String>();
private int mBuildInfo;
    private int mMinorCode;

// the following are used to sort the export data and generate buildInfo
    private final String mAbi;
    private final Map<String, String> mSoftVariantMap = new HashMap<String, String>();

    ApkData(ProjectConfig projectConfig, String abi, Map<String, String> softVariants) {
        mProjectConfig = projectConfig;
        mAbi = abi;
        if (softVariants != null) {
            mSoftVariantMap.putAll(softVariants);
        }
}

    ApkData(ProjectConfig projectConfig, String abi) {
        this(projectConfig, abi, null /*softVariants*/);
}

    ApkData(ProjectConfig projectConfig, Map<String, String> softVariants) {
        this(projectConfig, null /*abi*/, softVariants);
    }

    ApkData(ProjectConfig projectConfig) {
        this(projectConfig, null /*abi*/, null /*softVariants*/);
    }

    public ProjectConfig getProjectConfig() {
        return mProjectConfig;
}

public String getOutputName(String key) {
//Synthetic comment -- @@ -112,22 +89,6 @@
mOutputNames.put(key, outputName);
}

public int getBuildInfo() {
return mBuildInfo;
}
//Synthetic comment -- @@ -136,32 +97,20 @@
mBuildInfo = buildInfo;
}

    public int getMinorCode() {
        return mMinorCode;
}

    void setMinorCode(int minor) {
        mMinorCode = minor;
}

public String getAbi() {
return mAbi;
}

    public Map<String, String> getSoftVariantMap() {
        return mSoftVariantMap;
}

/**
//Synthetic comment -- @@ -172,55 +121,11 @@
public int getCompositeVersionCode(int versionCode) {
int trueVersionCode = versionCode * MultiApkExportHelper.OFFSET_VERSION_CODE;
trueVersionCode += getBuildInfo() * MultiApkExportHelper.OFFSET_BUILD_INFO;
        trueVersionCode += getMinorCode();

return trueVersionCode;
}

@Override
public String toString() {
return getLogLine(null);
//Synthetic comment -- @@ -229,69 +134,62 @@
public String getLogLine(String key) {
StringBuilder sb = new StringBuilder();
sb.append(getOutputName(key)).append(':');

        LogHelper.write(sb, PROP_BUILDINFO, mBuildInfo);
        LogHelper.write(sb, PROP_MINOR, mMinorCode);
        LogHelper.write(sb, PROP_PROJECT, mProjectConfig.getRelativePath());
        sb.append(mProjectConfig.getConfigString(true /*onlyManifestData*/));

        if (mAbi != null) {
            LogHelper.write(sb, PROP_ABI, mAbi);
        }

        String filter = mSoftVariantMap.get(key);
        if (filter != null) {
            LogHelper.write(sb, PROP_RESOURCES, filter);
}

return sb.toString();
}

public int compareTo(ApkData o) {
        // compare only the hard properties, and in a specific order:

        // 1. minSdkVersion
        int minSdkDiff = mProjectConfig.getMinSdkVersion() - o.mProjectConfig.getMinSdkVersion();
if (minSdkDiff != 0) {
return minSdkDiff;
}

        // 2. <supports-screens>
// only compare if they have don't have the same size support. This is because
// this compare method throws an exception if the values cannot be compared.
        if (mProjectConfig.getSupportsScreens().hasSameScreenSupportAs(
                o.mProjectConfig.getSupportsScreens()) == false) {
            return mProjectConfig.getSupportsScreens().compareScreenSizesWith(
                    o.mProjectConfig.getSupportsScreens());
}

        // 3. glEsVersion
int comp;
        if (mProjectConfig.getGlEsVersion() != ManifestData.GL_ES_VERSION_NOT_SET) {
            if (o.mProjectConfig.getGlEsVersion() != ManifestData.GL_ES_VERSION_NOT_SET) {
                comp = mProjectConfig.getGlEsVersion() - o.mProjectConfig.getGlEsVersion();
if (comp != 0) return comp;
} else {
return -1;
}
        } else if (o.mProjectConfig.getGlEsVersion() != ManifestData.GL_ES_VERSION_NOT_SET) {
return 1;
}

        // 4. ABI
// here the returned value is only important if both abi are non null.
if (mAbi != null && o.mAbi != null) {
comp = compareAbi(mAbi, o.mAbi);
if (comp != 0) return comp;
}

return 0;
}

//Synthetic comment -- @@ -317,89 +215,4 @@

return 0;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/LogHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/LogHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..b324b4d

//Synthetic comment -- @@ -0,0 +1,38 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.export;

class LogHelper {

    /**
     * Separator for putting multiple properties in a single {@link String}.
     */
    final static char PROP_SEPARATOR = ';';
    /**
     * Equal sign between the name and value of a property
     */
    final static char PROPERTY_EQUAL = '=';

    static void write(StringBuilder sb, String name, Object value) {
        sb.append(name).append(PROPERTY_EQUAL).append(value).append(PROP_SEPARATOR);
    }

    static void write(StringBuilder sb, String name, int value) {
        sb.append(name).append(PROPERTY_EQUAL).append(value).append(PROP_SEPARATOR);
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
//Synthetic comment -- index dad57e7..697ce8b 100644

//Synthetic comment -- @@ -17,15 +17,9 @@
package com.android.sdklib.internal.export;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.FileWrapper;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.xml.AndroidManifestParser;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.SupportsScreens;
//Synthetic comment -- @@ -34,11 +28,16 @@

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Synthetic comment -- @@ -46,18 +45,30 @@

/**
* Helper to export multiple APKs from 1 or or more projects.
 * <strong>This class is not meant to be accessed from multiple threads</strong>
*/
public class MultiApkExportHelper {

    private final static String PROP_VERSIONCODE = "versionCode";
    private final static String PROP_PACKAGE = "package";

    private final String mExportProjectRoot;
private final String mAppPackage;
private final int mVersionCode;
private final Target mTarget;

    private ArrayList<ProjectConfig> mProjectList;
    private ArrayList<ApkData> mApkDataList;

final static int MAX_MINOR = 100;
final static int MAX_BUILDINFO = 100;
final static int OFFSET_BUILD_INFO = MAX_MINOR;
final static int OFFSET_VERSION_CODE = OFFSET_BUILD_INFO * MAX_BUILDINFO;

    private final static String FILE_CONFIG = "projects.config";
    private final static String FILE_MINOR_CODE = "minor.codes";
    private final static String FOLDER_LOG = "logs";

public static final class ExportException extends Exception {
private static final long serialVersionUID = 1L;

//Synthetic comment -- @@ -65,6 +76,14 @@
super(message);
}

        public ExportException(String format, Object... args) {
            super(String.format(format, args));
        }

        public ExportException(Throwable cause, String format, Object... args) {
            super(String.format(format, args), cause);
        }

public ExportException(String message, Throwable cause) {
super(message, cause);
}
//Synthetic comment -- @@ -95,101 +114,168 @@
}
}

    public MultiApkExportHelper(String exportProjectRoot, String appPackage,
            int versionCode, Target target) {
        mExportProjectRoot = exportProjectRoot;
mAppPackage = appPackage;
mVersionCode = versionCode;
mTarget = target;
}

    public List<ApkData> getApkData(String projectList) throws ExportException {
        if (mTarget != Target.RELEASE) {
            throw new IllegalArgumentException("getApkData must only be called for Target.RELEASE");
}

        // get the list of apk to export and their configuration.
        List<ProjectConfig> projects = getProjects(projectList);

        // look to see if there's a config file from a previous export
        File configProp = new File(mExportProjectRoot, FILE_CONFIG);
        if (configProp.isFile()) {
            compareProjectsToConfigFile(projects, configProp);
        }

        // look to see if there's a minor properties file
        File minorCodeProp = new File(mExportProjectRoot, FILE_MINOR_CODE);
        Map<Integer, Integer> minorCodeMap = null;
        if (minorCodeProp.isFile()) {
            minorCodeMap = getMinorCodeMap(minorCodeProp);
        }

        // get the apk from the projects.
        return getApkData(projects, minorCodeMap);
}

/**
     * Returns the list of projects defined by the <var>projectList</var> string.
     * The projects are checked to be valid Android project and to represent a valid set
     * of projects for multi-apk export.
     * If a project does not exist or is not valid, the method will throw a {@link BuildException}.
     * The string must be a list of paths, relative to the export project path (given to
     * {@link #MultiApkExportHelper(String, String, int, Target)}), separated by the colon (':')
     * character. The path separator is expected to be forward-slash ('/') on all platforms.
     * @param projects the string containing all the relative paths to the projects. This is
     * usually read from export.properties.
* @throws ExportException
*/
    public List<ProjectConfig> getProjects(String projectList) throws ExportException {
        String[] paths = projectList.split("\\:");

        mProjectList = new ArrayList<ProjectConfig>();

        for (String path : paths) {
            path = path.replaceAll("\\/", File.separator);
            processProject(path, mProjectList);
        }

        return mProjectList;
    }

    /**
     * Writes post-export logs and other files.
     * @throws ExportException if writing the files failed.
     */
    public void writeLogs() throws ExportException {
        writeConfigProperties();
        writeMinorVersionProperties();
        writeApkLog();
    }

    private void writeConfigProperties() throws ExportException {
OutputStreamWriter writer = null;
try {
            writer = new OutputStreamWriter(
                    new FileOutputStream(new File(mExportProjectRoot, FILE_CONFIG)));

            writer.append("# PROJECT CONFIG -- DO NOT DELETE.\n");
            writeValue(writer, PROP_VERSIONCODE, mVersionCode);

            for (ProjectConfig project : mProjectList) {
                writeValue(writer,project.getRelativePath(),
                        project.getConfigString(false /*onlyManifestData*/));
            }

            writer.flush();
        } catch (Exception e) {
            throw new ExportException("Failed to write config log", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new ExportException("Failed to write config log", e);
            }
        }
    }

    private void writeMinorVersionProperties() throws ExportException {
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(
                    new FileOutputStream(new File(mExportProjectRoot, FILE_MINOR_CODE)));

writer.append(
                    "# Minor version codes.\n" +
                    "# To create update to select APKs without updating the main versionCode\n" +
                    "# edit this file and manually increase the minor version for the select\n" +
                    "# build info.\n" +
                    "# Format of the file is <buildinfo>:<minor>\n");
            writeValue(writer, PROP_VERSIONCODE, mVersionCode);

            for (ApkData apk : mApkDataList) {
                writeValue(writer, Integer.toString(apk.getBuildInfo()), apk.getMinorCode());
            }

            writer.flush();
        } catch (Exception e) {
            throw new ExportException("Failed to write minor log", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new ExportException("Failed to write minor log", e);
            }
        }
    }

    private void writeApkLog() throws ExportException {
        OutputStreamWriter writer = null;
        try {
            File logFolder = new File(mExportProjectRoot, FOLDER_LOG);
            if (logFolder.isFile()) {
                throw new ExportException("Cannot create folder '%1$s', file is in the way!",
                        FOLDER_LOG);
            } else if (logFolder.exists() == false) {
                logFolder.mkdir();
            }

            Formatter formatter = new Formatter();
            formatter.format("%1$s.%2$d-%3$tY%3$tm%3$td-%3$tH%3$tM.log",
                    mAppPackage, mVersionCode,
                    Calendar.getInstance().getTime());

            writer = new OutputStreamWriter(
                    new FileOutputStream(new File(logFolder, formatter.toString())));

            writer.append("# Multi-APK BUILD LOG.\n");
            writeValue(writer, PROP_PACKAGE, mAppPackage);
            writeValue(writer, PROP_VERSIONCODE, mVersionCode);

            for (ApkData apk : mApkDataList) {
                // if there are soft variant, do not display the main log line, as it's not actually
                // exported.
Map<String, String> softVariants = apk.getSoftVariantMap();
if (softVariants.size() > 0) {
                    for (String softVariant : softVariants.keySet()) {
                        writer.append(apk.getLogLine(softVariant));
                        writer.append('\n');
                    }
                } else {
                    writer.append(apk.getLogLine(null));
writer.append('\n');
}
}
//Synthetic comment -- @@ -210,116 +296,114 @@

private void writeValue(OutputStreamWriter writer, String name, String value)
throws IOException {
        writer.append(name).append(':').append(value).append('\n');
}

private void writeValue(OutputStreamWriter writer, String name, int value) throws IOException {
writeValue(writer, name, Integer.toString(value));
}

    private List<ApkData> getApkData(List<ProjectConfig> projects,
            Map<Integer, Integer> minorCodes) {
        mApkDataList = new ArrayList<ApkData>();

        // get all the apkdata from all the projects
        for (ProjectConfig config : projects) {
            mApkDataList.addAll(config.getApkDataList());
}

// sort the projects and assign buildInfo
        Collections.sort(mApkDataList);
int buildInfo = 0;
        for (ApkData data : mApkDataList) {
            data.setBuildInfo(buildInfo);
            if (minorCodes != null) {
                Integer minorCode = minorCodes.get(buildInfo);
                if (minorCode != null) {
                    data.setMinorCode(minorCode);
                }
            }

            buildInfo++;
}

        return mApkDataList;
}

/**
     * Checks a project for inclusion in the list of exported APK.
* <p/>This performs a check on the manifest, as well as gathers more information about
* mutli-apk from the project's default.properties file.
* If the manifest is correct, a list of apk to export is created and returned.
*
     * @param projectFolder the folder of the project to check
     * @param projects the list of project to file with the project if it passes validation.
* @throws ExportException in case of error.
*/
    private void processProject(String relativePath,
            ArrayList<ProjectConfig> projects) throws ExportException {

        // resolve the relative path
        File projectFolder;
try {
            File path = new File(mExportProjectRoot, relativePath);

            projectFolder = path.getCanonicalFile();

            // project folder must exist and be a directory
            if (projectFolder.isDirectory() == false) {
                throw new ExportException(
                        "Project folder '%1$s' is not a valid directory.",
                        projectFolder.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new ExportException(
                    e, "Failed to resolve path %1$s", relativePath);
        }

        try {
            // Check AndroidManifest.xml is present
            IAbstractFile androidManifest = new FileWrapper(projectFolder,
                    SdkConstants.FN_ANDROID_MANIFEST_XML);

            if (androidManifest.exists() == false) {
                throw new ExportException(String.format(
                        "%1$s is not a valid project (%2$s not found).",
                        relativePath, androidManifest.getOsLocation()));
            }

            // output the relative path resolution.
            System.out.println(String.format("%1$s => %2$s", relativePath,
                    projectFolder.getAbsolutePath()));

            // parse the manifest of the project.
ManifestData manifestData = AndroidManifestParser.parse(androidManifest);

            // validate the application package name
String manifestPackage = manifestData.getPackage();
if (mAppPackage.equals(manifestPackage) == false) {
                throw new ExportException(
"%1$s package value is not valid. Found '%2$s', expected '%3$s'.",
                        androidManifest.getOsLocation(), manifestPackage, mAppPackage);
}

            // validate that the manifest has no versionCode set.
if (manifestData.getVersionCode() != null) {
                throw new ExportException(
"%1$s is not valid: versionCode must not be set for multi-apk export.",
                        androidManifest.getOsLocation());
}

            // validate that the minSdkVersion is not a codename
int minSdkVersion = manifestData.getMinSdkVersion();
if (minSdkVersion == ManifestData.MIN_SDK_CODENAME) {
throw new ExportException(
"Codename in minSdkVersion is not supported by multi-apk export.");
}

            // compare to other projects already processed to make sure that they are not
            // identical.
            for (ProjectConfig otherProject : projects) {
// Multiple apk export support difference in:
// - min SDK Version
// - Screen version
//Synthetic comment -- @@ -327,26 +411,27 @@
// - ABI (not managed at the Manifest level).
// if those values are the same between 2 manifest, then it's an error.


// first the minSdkVersion.
                if (minSdkVersion == otherProject.getMinSdkVersion()) {
// if it's the same compare the rest.
SupportsScreens currentSS = manifestData.getSupportsScreensValues();
                    SupportsScreens previousSS = otherProject.getSupportsScreens();
boolean sameSupportsScreens = currentSS.hasSameScreenSupportAs(previousSS);

// if it's the same, then it's an error. Can't export 2 projects that have the
// same approved (for multi-apk export) hard-properties.
                    if (manifestData.getGlEsVersion() == otherProject.getGlEsVersion() &&
sameSupportsScreens) {

                        throw new ExportException(
"Android manifests must differ in at least one of the following values:\n" +
"- minSdkVersion\n" +
"- SupportsScreen (screen sizes only)\n" +
"- GL ES version.\n" +
"%1$s and %2$s are considered identical for multi-apk export.",
                                relativePath,
                                otherProject.getRelativePath());
}

// At this point, either supports-screens or GL are different.
//Synthetic comment -- @@ -361,176 +446,264 @@
//   (ie APK1 supports small/large and APK2 supports normal).
if (sameSupportsScreens == false) {
if (currentSS.hasStrictlyDifferentScreenSupportAs(previousSS) == false) {
                            throw new ExportException(
"APK differentiation by Supports-Screens cannot support different APKs supporting the same screen size.\n" +
"%1$s supports %2$s\n" +
"%3$s supports %4$s\n",
                                    relativePath, currentSS.toString(),
                                    otherProject.getRelativePath(), previousSS.toString());
}

if (currentSS.overlapWith(previousSS)) {
                            throw new ExportException(
"Unable to compute APK priority due to incompatible difference in Supports-Screens values.\n" +
"%1$s supports %2$s\n" +
"%3$s supports %4$s\n",
                                    relativePath, currentSS.toString(),
                                    otherProject.getRelativePath(), previousSS.toString());
}
}
}
}

            // project passes first validation. Attempt to create a ProjectConfig object.

            ProjectConfig config = ProjectConfig.create(projectFolder, relativePath, manifestData);
            projects.add(config);
} catch (SAXException e) {
            throw new ExportException(e, "Failed to validate %1$s", relativePath);
} catch (IOException e) {
            throw new ExportException(e, "Failed to validate %1$s", relativePath);
} catch (StreamException e) {
            throw new ExportException(e, "Failed to validate %1$s", relativePath);
} catch (ParserConfigurationException e) {
            throw new ExportException(e, "Failed to validate %1$s", relativePath);
}
}

/**
     * Checks an existing list of {@link ProjectConfig} versus a config file.
     * @param projects the list of projects to check
     * @param configProp the config file (must have been generated from a previous export)
     * @return true if the projects and config file match
     * @throws ExportException in case of error
*/
    private void compareProjectsToConfigFile(List<ProjectConfig> projects, File configProp)
            throws ExportException {
InputStreamReader reader = null;
BufferedReader bufferedReader = null;
try {
            reader = new InputStreamReader(new FileInputStream(configProp));
bufferedReader = new BufferedReader(reader);
String line;

            // List of the ProjectConfig that need to be checked. This is to detect
            // new Projects added to the setup.
            // removed projects are detected when an entry in the config file doesn't match
            // any ProjectConfig in the list.
            ArrayList<ProjectConfig> projectsToCheck = new ArrayList<ProjectConfig>();
            projectsToCheck.addAll(projects);

            // store the project that doesn't match.
            ProjectConfig badMatch = null;

            // recorded whether we checked the version code. this is for when we compare
            // a project config
            boolean checkedVersion = false;

            int lineNumber = 0;
while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
line = line.trim();
if (line.length() == 0 || line.startsWith("#")) {
continue;
}

                // read the name of the property
                int colonPos = line.indexOf(':');
                if (colonPos == -1) {
                    // looks like there's an invalid line!
                    throw new ExportException(
                            "Failed to read existing build log. Line %d is not a property line.",
                            lineNumber);
}

                String name = line.substring(0, colonPos);
                String value = line.substring(colonPos + 1);

                if (PROP_VERSIONCODE.equals(name)) {
                    try {
                        int versionCode = Integer.parseInt(value);
                        if (versionCode < mVersionCode) {
                            // this means this config file is obsolete and we can ignore it.
                            return;
                        } else if (versionCode > mVersionCode) {
                            // we're exporting at a lower versionCode level than the config file?
                            throw new ExportException(
                                    "Incompatible versionCode: Exporting at versionCode %1$d but %2$s file indicate previous export with versionCode %3$d.",
                                    mVersionCode, FILE_CONFIG, versionCode);
                        } else if (badMatch != null) {
                            // looks like versionCode is a match, but a project
                            // isn't compatible.
                            break;
                        } else {
                            // record that we did check the versionCode
                            checkedVersion = true;
                        }
                    } catch (NumberFormatException e) {
                        throw new ExportException(
                                "Failed to read integer property %1$s at line %2$d.",
                                PROP_VERSIONCODE, lineNumber);
                    }
                } else {
                    // looks like this is (or should be) a project line.
                    // name of the property is the relative path.
                    // look for a matching project.
                    ProjectConfig found = null;
                    for (int i = 0 ; i < projectsToCheck.size() ; i++) {
                        ProjectConfig p = projectsToCheck.get(i);
                        if (p.getRelativePath().equals(name)) {
                            found = p;
                            projectsToCheck.remove(i);
                            break;
                        }
                    }

                    if (found == null) {
                        // deleted project!
                        throw new ExportException(
                                "Project %1$s has been removed from the list of projects to export.\n" +
                                "Any change in the multi-apk configuration requires an increment of the versionCode in export.properties.",
                                name);
                    } else {
                        // make a map of properties
                        HashMap<String, String> map = new HashMap<String, String>();
                        String[] properties = value.split(";");
                        for (String prop : properties) {
                            int equalPos = prop.indexOf('=');
                            map.put(prop.substring(0, equalPos), prop.substring(equalPos + 1));
                        }

                        if (found.compareToProperties(map) == false) {
                            // bad project config, record the project
                            badMatch = found;

                            // if we've already checked that the versionCode didn't already change
                            // we stop right away.
                            if (checkedVersion) {
                                break;
                            }
                        }
                    }

                }

}

            if (badMatch != null) {
                throw new ExportException(
                        "Config for project %1$s has changed from previous export with versionCode %2$d.\n" +
                        "Any change in the multi-apk configuration requires an increment of the versionCode in export.properties.",
                        badMatch.getRelativePath(), mVersionCode);
            } else if (projectsToCheck.size() > 0) {
                throw new ExportException(
                        "Project %1$s was not part of the previous export with versionCode %2$d.\n" +
                        "Any change in the multi-apk configuration requires an increment of the versionCode in export.properties.",
                        projectsToCheck.get(0).getRelativePath(), mVersionCode);
            }

} catch (IOException e) {
            throw new ExportException(e, "Failed to read existing config log: %s", FILE_CONFIG);
} finally {
try {
if (reader != null) {
reader.close();
}
} catch (IOException e) {
                throw new ExportException(e, "Failed to read existing config log: %s", FILE_CONFIG);
}
}
}

    private Map<Integer, Integer> getMinorCodeMap(File minorProp) throws ExportException {
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(minorProp));
            bufferedReader = new BufferedReader(reader);
            String line;

            boolean foundVersionCode = false;
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();

            int lineNumber = 0;
            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }

                // read the name of the property
                int colonPos = line.indexOf(':');
                if (colonPos == -1) {
                    // looks like there's an invalid line!
                    throw new ExportException(
                            "Failed to read existing build log. Line %d is not a property line.",
                            lineNumber);
                }

                String name = line.substring(0, colonPos);
                String value = line.substring(colonPos + 1);

                if (PROP_VERSIONCODE.equals(name)) {
                    try {
                        int versionCode = Integer.parseInt(value);
                        if (versionCode < mVersionCode) {
                            // this means this minor file is obsolete and we can ignore it.
                            return null;
                        } else if (versionCode > mVersionCode) {
                            // we're exporting at a lower versionCode level than the minor file?
                            throw new ExportException(
                                    "Incompatible versionCode: Exporting at versionCode %1$d but %2$s file indicate previous export with versionCode %3$d.",
                                    mVersionCode, FILE_MINOR_CODE, versionCode);
                        }
                        foundVersionCode = true;
                    } catch (NumberFormatException e) {
                        throw new ExportException(
                                "Failed to read integer property %1$s at line %2$d.",
                                PROP_VERSIONCODE, lineNumber);
                    }
                } else {
                    try {
                        map.put(Integer.valueOf(name), Integer.valueOf(value));
                    } catch (NumberFormatException e) {
                        throw new ExportException(
                                "Failed to read buildInfo property '%1$s' at line %2$d.",
                                line, lineNumber);
}
}
}

            // if there was no versionCode found, we can't garantee that the minor version
            // found are for this versionCode
            if (foundVersionCode == false) {
                throw new ExportException(
                        "%1$s property missing from file %2$s.", PROP_VERSIONCODE, FILE_MINOR_CODE);
            }

            return map;
        } catch (IOException e) {
            throw new ExportException(e, "Failed to read existing minor log: %s", FILE_MINOR_CODE);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new ExportException(e, "Failed to read existing minor log: %s",
                        FILE_MINOR_CODE);
            }
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java
new file mode 100644
//Synthetic comment -- index 0000000..8b7d653

//Synthetic comment -- @@ -0,0 +1,305 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.export;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.export.MultiApkExportHelper.ExportException;
import com.android.sdklib.internal.project.ApkSettings;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.resources.Density;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.SupportsScreens;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class representing an Android project and its properties.
 *
 * Only the properties that pertain to the multi-apk export are present.
 */
public final class ProjectConfig {

    private static final String PROP_API = "api";
    private static final String PROP_SCREENS = "screens";
    private static final String PROP_GL = "gl";
    private static final String PROP_ABI = "splitByAbi";
    private static final String PROP_DENSITY = "splitByDensity";
    private static final String PROP_LOCALEFILTERS = "localeFilters";

    /**
     * List of densities and their associated aapt filter.
     */
    private static final String[][] DENSITY_LIST = new String[][] {
        new String[] { Density.HIGH.getValue(),
                Density.HIGH.getValue() + "," + Density.NODPI.getValue() },
                new String[] { Density.MEDIUM.getValue(),
                        Density.MEDIUM.getValue() + "," + Density.NODPI.getValue() },
                        new String[] { Density.MEDIUM.getValue(),
                Density.MEDIUM.getValue() + "," + Density.NODPI.getValue() },
    };

    private final File mProjectFolder;
    private final String mRelativePath;

    private final int mMinSdkVersion;
    private final int mGlEsVersion;
    private final SupportsScreens mSupportsScreens;
    private final boolean mSplitByAbi;
    private final boolean mSplitByDensity;
    private final Map<String, String> mLocaleFilters;

    static ProjectConfig create(File projectFolder, String relativePath,
            ManifestData manifestData) throws ExportException {
        // load the project properties
        ProjectProperties projectProp = ProjectProperties.load(projectFolder.getAbsolutePath(),
                PropertyType.DEFAULT);
        if (projectProp == null) {
            throw new ExportException(String.format("%1$s is missing for project %2$s",
                    PropertyType.DEFAULT.getFilename(), relativePath));
        }

        ApkSettings apkSettings = new ApkSettings(projectProp);

        return new ProjectConfig(projectFolder,
                relativePath,
                manifestData.getMinSdkVersion(),
                manifestData.getGlEsVersion(),
                manifestData.getSupportsScreensValues(),
                apkSettings.isSplitByAbi(),
                apkSettings.isSplitByDensity(),
                apkSettings.getLocaleFilters());
    }


    private ProjectConfig(File projectFolder, String relativePath,
            int minSdkVersion, int glEsVersion,
            SupportsScreens supportsScreens, boolean splitByAbi, boolean splitByDensity,
            Map<String, String> localeFilters) {
        mProjectFolder = projectFolder;
        mRelativePath = relativePath;
        mMinSdkVersion = minSdkVersion;
        mGlEsVersion = glEsVersion;
        mSupportsScreens = supportsScreens;
        mSplitByAbi = splitByAbi;
        mSplitByDensity = splitByDensity;
        mLocaleFilters = localeFilters;
    }

    public File getProjectFolder() {
        return mProjectFolder;
    }


    public String getRelativePath() {
        return mRelativePath;
    }

    List<ApkData> getApkDataList() {
        // there are 3 cases:
        // 1. ABI split generate multiple apks with different build info, so they are different
        //    ApkData for all of them. Special case: split by abi but no native code => 1 ApkData.
        // 2. split by density or locale filters generate soft variant only, so they all go
        //    in the same ApkData.
        // 3. Both 1. and 2. means that more than one ApkData are created and they all get soft
        //    variants.

        ArrayList<ApkData> list = new ArrayList<ApkData>();

        Map<String, String> softVariants = computeSoftVariantMap();

        if (mSplitByAbi) {
            List<String> abis = findAbis();
            if (abis.size() > 0) {
                for (String abi : abis) {
                    list.add(new ApkData(this, abi, softVariants));
                }
            } else {
                // if there are no ABIs, then just generate a single ApkData with no specific ABI.
                list.add(new ApkData(this, softVariants));
            }
        } else {
            // create a single ApkData.
            list.add(new ApkData(this, softVariants));
        }

        return list;
    }

    int getMinSdkVersion() {
        return mMinSdkVersion;
    }

    SupportsScreens getSupportsScreens() {
        return mSupportsScreens;
    }

    int getGlEsVersion() {
        return mGlEsVersion;
    }

    boolean isSplitByDensity() {
        return mSplitByDensity;
    }

    boolean isSplitByAbi() {
        return mSplitByAbi;
    }

    /**
     * Returns a map of pair values (apk name suffix, aapt res filter) to be used to generate
     * multiple soft apk variants.
     */
    private Map<String, String> computeSoftVariantMap() {
        HashMap<String, String> map = new HashMap<String, String>();

        if (mSplitByDensity && mLocaleFilters.size() > 0) {
            for (String[] density : DENSITY_LIST) {
                for (Entry<String,String> entry : mLocaleFilters.entrySet()) {
                    map.put(density[0] + "-" + entry.getKey(),
                            density[1] + "," + entry.getValue());
                }
            }

        } else if (mSplitByDensity) {
            for (String[] density : DENSITY_LIST) {
                map.put(density[0], density[1]);
            }

        } else if (mLocaleFilters.size() > 0) {
            map.putAll(mLocaleFilters);

        }

        return map;
    }

    /**
     * Finds ABIs in a project folder. This is based on the presence of libs/<abi>/ folder.
     *
     * @param projectPath The OS path of the project.
     * @return A new non-null, possibly empty, list of ABI strings.
     */
    private List<String> findAbis() {
        ArrayList<String> abiList = new ArrayList<String>();
        File libs = new File(mProjectFolder, SdkConstants.FD_NATIVE_LIBS);
        if (libs.isDirectory()) {
            File[] abis = libs.listFiles();
            for (File abi : abis) {
                if (abi.isDirectory()) {
                    // only add the abi folder if there are .so files in it.
                    String[] content = abi.list(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
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

    String getConfigString(boolean onlyManifestData) {
        StringBuilder sb = new StringBuilder();
        LogHelper.write(sb, PROP_API, mMinSdkVersion);
        LogHelper.write(sb, PROP_SCREENS, mSupportsScreens.getEncodedValues());

        if (mGlEsVersion != ManifestData.GL_ES_VERSION_NOT_SET) {
            LogHelper.write(sb, PROP_GL, "0x" + Integer.toHexString(mGlEsVersion));
        }

        if (onlyManifestData == false) {
            LogHelper.write(sb, PROP_ABI, mSplitByAbi);
            LogHelper.write(sb, PROP_DENSITY, Boolean.toString(mSplitByDensity));

            if (mLocaleFilters.size() > 0) {
                LogHelper.write(sb, PROP_LOCALEFILTERS, ApkSettings.writeLocaleFilters(mLocaleFilters));
            }
        }

        return sb.toString();
    }

    /**
     * Compares the current project config to a list of properties.
     * These properties are in the format output by {@link #getConfigString()}.
     * @param values the properties to compare to.
     * @return true if the properties exactly match the current config.
     */
    boolean compareToProperties(Map<String, String> values) {
        String tmp;
        // Note that most properties must always be present in the map.
        try {
            // api must always be there
            if (mMinSdkVersion != Integer.parseInt(values.get(PROP_API))) {
                return false;
            }

            tmp = values.get(PROP_GL); // GL is optional in the config string.
            if (tmp != null) {
                if (mGlEsVersion != Integer.decode(tmp)) {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            // failed to convert an integer? consider the configs not equal.
            return false;
        }

        tmp = values.get(PROP_DENSITY);
        if (tmp == null || mSplitByDensity != Boolean.valueOf(tmp)) {
            return false;
        }

        tmp = values.get(PROP_ABI);
        if (tmp == null || mSplitByAbi != Boolean.valueOf(tmp)) {
            return false;
        }

        tmp = values.get(PROP_SCREENS);
        if (tmp != null) {
            SupportsScreens supportsScreens = new SupportsScreens(tmp);
            if (supportsScreens.equals(mSupportsScreens) == false) {
                return false;
            }
        } else {
            return false;
        }

        tmp = values.get(PROP_LOCALEFILTERS);
        if (tmp != null) {
            if (mLocaleFilters.equals(ApkSettings.readLocaleFilters(tmp)) == false) {
                return false;
            }
        } else {
            // do nothing. locale filter is optional in the config string.
        }

        return true;
    }
}







