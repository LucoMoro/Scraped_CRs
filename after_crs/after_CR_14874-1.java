/*Refactored most of the multi-apk export out of the ant task into sdklib.

This common code will be used by ADT when processing multi-apk export.

I also had to improve the API for IAbstractFile/Folder.

Change-Id:Ied76f0a9f068223ceaba36124e841f804febfaf8*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/MultiApkExportTask.java b/anttasks/src/com/android/ant/MultiApkExportTask.java
//Synthetic comment -- index f515076..b7ca0c0 100644

//Synthetic comment -- @@ -16,14 +16,12 @@

package com.android.ant;

import com.android.sdklib.internal.export.ApkData;
import com.android.sdklib.internal.export.MultiApkExportHelper;
import com.android.sdklib.internal.export.MultiApkExportHelper.ExportException;
import com.android.sdklib.internal.export.MultiApkExportHelper.Target;
import com.android.sdklib.io.FileWrapper;
import com.android.sdklib.io.IAbstractFile;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
//Synthetic comment -- @@ -34,18 +32,10 @@
import org.apache.tools.ant.types.FileSet;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
//Synthetic comment -- @@ -58,37 +48,6 @@
*/
public class MultiApkExportTask extends Task {

private Target mTarget;

public void setTarget(String target) {
//Synthetic comment -- @@ -134,170 +93,154 @@
canSign = keyStore != null && keyAlias != null;
}

        // get the list of projects
        String projects = getValidatedProperty(antProject,"projects");

// look to see if there's an export log from a previous export
        IAbstractFile log = getBuildLog(appPackage, versionCode);

        MultiApkExportHelper helper = new MultiApkExportHelper(appPackage, versionCode, mTarget);
        try {
            ApkData[] apks = helper.getProjects(projects, log);

            // some temp var used by the project loop
            HashSet<String> compiledProject = new HashSet<String>();
            XPathFactory xPathFactory = XPathFactory.newInstance();

            File exportProjectOutput = new File(getValidatedProperty(antProject, "out.absolute.dir"));

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
                // this output is prepended by "[android-export] " (17 chars), so we put 61 stars
                System.out.println("\n*************************************************************");
                System.out.println("Exporting project: " + apk.getRelativePath());

                SubAnt subAnt = new SubAnt();
                subAnt.setTarget(mTarget.getTarget());
                subAnt.setProject(antProject);

                File subProjectFolder = new File(antProject.getBaseDir(), apk.getRelativePath());

                FileSet fileSet = new FileSet();
                fileSet.setProject(antProject);
                fileSet.setDir(subProjectFolder);
                fileSet.setIncludes("build.xml");
                subAnt.addFileset(fileSet);

    //            subAnt.setVerbose(true);

                if (mTarget == Target.RELEASE) {
                    // only do the compilation part if it's the first time we export
                    // this project.
                    // (projects can be export multiple time if some properties are set up to
                    // generate more than one APK (for instance ABI split).
                    if (compiledProject.contains(apk.getRelativePath()) == false) {
                        compiledProject.add(apk.getRelativePath());
                    } else {
                        addProp(subAnt, "do.not.compile", "true");
                    }

                    // set the version code, and filtering
                    String compositeVersionCode = getVersionCodeString(versionCode, apk);
                    addProp(subAnt, "version.code", compositeVersionCode);
                    System.out.println("Composite versionCode: " + compositeVersionCode);
                    String abi = apk.getAbi();
                    if (abi != null) {
                        addProp(subAnt, "filter.abi", abi);
                        System.out.println("ABI Filter: " + abi);
                    }

                    // end of the output by this task. Everything that follows will be output
                    // by the subant.
                    System.out.println("Calling to project's Ant file...");
                    System.out.println("----------\n");

                    // set the output file names/paths. Keep all the temporary files in the project
                    // folder, and only put the final file (which is different depending on whether
                    // the file can be signed) locally.

                    // read the base name from the build.xml file.
                    String name = null;
                    try {
                        File buildFile = new File(subProjectFolder, "build.xml");
                        XPath xPath = xPathFactory.newXPath();
                        name = xPath.evaluate("/project/@name",
                                new InputSource(new FileInputStream(buildFile)));
                    } catch (XPathExpressionException e) {
                        throw new BuildException("Failed to read build.xml", e);
                    } catch (FileNotFoundException e) {
                        throw new BuildException("build.xml is missing.", e);
                    }

                    // override the resource pack file.
                    addProp(subAnt, "resource.package.file.name",
                            name + "-" + apk.getBuildInfo() + ".ap_");

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

                        // final file is stored locally.
                        apk.setOutputName(name + "-" + compositeVersionCode + "-release.apk");
                        addProp(subAnt, "out.release.file", new File(exportProjectOutput,
                                apk.getOutputName()).getAbsolutePath());

                    } else {
                        // put some empty prop. This is to override possible ones defined in the
                        // project. The reason is that if there's more than one project, we don't
                        // want some to signed and some not to be (and we don't want each project
                        // to prompt for password.)
                        addProp(subAnt, "key.store", "");
                        addProp(subAnt, "key.alias", "");
                        // final file is the unsigned version. It gets stored locally.
                        apk.setOutputName(name + "-" + compositeVersionCode + "-unsigned.apk");
                        addProp(subAnt, "out.unsigned.file", new File(exportProjectOutput,
                                apk.getOutputName()).getAbsolutePath());
                    }
}

                subAnt.execute();
}

if (mTarget == Target.RELEASE) {
                helper.makeBuildLog(log, apks);
}
        } catch (ExportException e) {
            throw new BuildException(e);
}
}

//Synthetic comment -- @@ -318,174 +261,6 @@
return value;
}


/**
* Adds a property to a {@link SubAnt} task.
//Synthetic comment -- @@ -507,8 +282,8 @@
* @return the composite versionCode to be used in the manifest.
*/
private String getVersionCodeString(int versionCode, ApkData apkData) {
        int trueVersionCode = versionCode * MultiApkExportHelper.OFFSET_VERSION_CODE;
        trueVersionCode += apkData.getBuildInfo() * MultiApkExportHelper.OFFSET_BUILD_INFO;
trueVersionCode += apkData.getMinor();

return Integer.toString(trueVersionCode);
//Synthetic comment -- @@ -518,119 +293,9 @@
* Returns the {@link File} for the build log.
* @param appPackage
* @param versionCode
     * @return A new non-null {@link IAbstractFile} mapping to the build log.
*/
    private IAbstractFile getBuildLog(String appPackage, int versionCode) {
        return new FileWrapper(appPackage + "." + versionCode + ".log");
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectState.java
//Synthetic comment -- index 2a55f74..93c77f9 100644

//Synthetic comment -- @@ -494,19 +494,24 @@
/**
* Saves the default.properties file and refreshes it to make sure that it gets reloaded
* by Eclipse
     * @throws Exception
*/
    public void saveProperties() throws CoreException {
try {
mProperties.save();

IResource defaultProp = mProject.findMember(SdkConstants.FN_DEFAULT_PROPERTIES);
defaultProp.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
        } catch (Exception e) {
            String msg = String.format(
                    "Failed to save %1$s for project %2$s",
                    SdkConstants.FN_DEFAULT_PROPERTIES, mProject.getName());
            AdtPlugin.log(e, msg);
            if (e instanceof CoreException) {
                throw (CoreException)e;
            } else {
                throw new CoreException(new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, msg, e));
            }
}
}

//Synthetic comment -- @@ -524,11 +529,12 @@
mProperties.setProperty(propName, newValue);
try {
mProperties.save();
                } catch (Exception e) {
                    return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, String.format(
                            "Failed to save %1$s for project %2$s",
                                    mProperties.getType() .getFilename(), mProject.getName()),
e);

}
return Status.OK_STATUS;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java
//Synthetic comment -- index f31e318..4621431 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.sdkuilib.internal.widgets.SdkTargetSelector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//Synthetic comment -- @@ -155,7 +156,11 @@
// TODO: update ApkSettings.

if (mustSaveProp) {
                try {
                    state.saveProperties();
                } catch (CoreException e) {
                    // pass
                }
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 76c057e..2503185 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.io.StreamException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -289,8 +290,10 @@
* @param project the project to intialize
* @param target the project's target.
* @throws IOException if creating the file failed in any way.
     * @throws StreamException
*/
    public void initProject(IProject project, IAndroidTarget target)
            throws IOException, StreamException {
if (project == null || target == null) {
return;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java
//Synthetic comment -- index 901bafe..89baa1c 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewTestProjectCreationPage.TestInfo;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.resources.Density;

import org.eclipse.core.resources.IContainer;
//Synthetic comment -- @@ -592,6 +593,8 @@
throw new InvocationTargetException(e);
} catch (IOException e) {
throw new InvocationTargetException(e);
        } catch (StreamException e) {
            throw new InvocationTargetException(e);
} finally {
monitor.done();
}
//Synthetic comment -- @@ -607,13 +610,14 @@
* @param parameters Template parameters.
* @param dictionary String definition.
* @return The project newly created
     * @throws StreamException
*/
private IProject createEclipseProject(IProgressMonitor monitor,
IProject project,
IProjectDescription description,
Map<String, Object> parameters,
Map<String, String> dictionary)
                throws CoreException, IOException, StreamException {

// get the project target
IAndroidTarget target = (IAndroidTarget) parameters.get(PARAM_SDK_TARGET);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFileWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFileWrapper.java
//Synthetic comment -- index 4b96789..dc022c7 100644

//Synthetic comment -- @@ -17,13 +17,19 @@
package com.android.ide.eclipse.adt.io;

import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.StreamException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
* An implementation of {@link IAbstractFile} on top of an {@link IFile} object.
//Synthetic comment -- @@ -52,6 +58,26 @@
}
}

    public OutputStream getOutputStream() throws StreamException {
        return new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                super.close();

                byte[] data = toByteArray();
                try {
                    setContents(new ByteArrayInputStream(data));
                } catch (StreamException e) {
                    throw new IOException();
                }
            }
        };
    }

    public PreferredWriteMode getPreferredWriteMode() {
        return PreferredWriteMode.INPUTSTREAM;
    }

public String getOsLocation() {
return mFile.getLocation().toOSString();
}
//Synthetic comment -- @@ -88,4 +114,13 @@
public int hashCode() {
return mFile.hashCode();
}

    public IAbstractFolder getParentFolder() {
        IContainer p = mFile.getParent();
        if (p != null) {
            return new IFolderWrapper(p);
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFolderWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFolderWrapper.java
//Synthetic comment -- index 158fd8b..265ea33 100644

//Synthetic comment -- @@ -27,6 +27,8 @@
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import java.util.ArrayList;

/**
* An implementation of {@link IAbstractFolder} on top of either an {@link IFolder} or an
* {@link IContainer} object.
//Synthetic comment -- @@ -126,4 +128,47 @@
public int hashCode() {
return mContainer.hashCode();
}

    public IAbstractFolder getFolder(String name) {
        if (mFolder != null) {
            IFolder folder = mFolder.getFolder(name);
            return new IFolderWrapper(folder);
        }

        IFolder folder = mContainer.getFolder(new Path(name));
        return new IFolderWrapper(folder);
    }

    public String getOsLocation() {
        return mContainer.getLocation().toOSString();
    }

    public String[] list(FilenameFilter filter) {
        try {
            IResource[] members = mContainer.members();
            if (members.length > 0) {
                ArrayList<String> list = new ArrayList<String>();
                for (IResource res : members) {
                    if (filter.accept(this, res.getName())) {
                        list.add(res.getName());
                    }
                }

                return list.toArray(new String[list.size()]);
            }
        } catch (CoreException e) {
            // can't read the members? return empty list below.
        }

        return new String[0];
    }

    public IAbstractFolder getParentFolder() {
        IContainer p = mContainer.getParent();
        if (p != null) {
            return new IFolderWrapper(p);
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 519e8fb..71fcee5 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.AndroidVersion.AndroidVersionException;
import com.android.sdklib.io.FileWrapper;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.StreamException;

import java.io.BufferedReader;
import java.io.File;
//Synthetic comment -- @@ -613,13 +616,26 @@
* @param propFile the property file to parse
* @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
* @return the map of (key,value) pairs, or null if the parsing failed.
     * @deprecated Use {@link #parsePropertyFile(IAbstractFile, ISdkLog)}
*/
public static Map<String, String> parsePropertyFile(File propFile, ISdkLog log) {
        IAbstractFile wrapper = new FileWrapper(propFile);
        return parsePropertyFile(wrapper, log);
    }

    /**
     * Parses a property file (using UTF-8 encoding) and returns a map of the content.
     * <p/>If the file is not present, null is returned with no error messages sent to the log.
     *
     * @param propFile the property file to parse
     * @param log the ISdkLog object receiving warning/error from the parsing. Cannot be null.
     * @return the map of (key,value) pairs, or null if the parsing failed.
     */
    public static Map<String, String> parsePropertyFile(IAbstractFile propFile, ISdkLog log) {
BufferedReader reader = null;
try {
            reader = new BufferedReader(new InputStreamReader(propFile.getContents(),
                    SdkConstants.INI_CHARSET));

String line = null;
Map<String, String> map = new HashMap<String, String>();
//Synthetic comment -- @@ -631,7 +647,7 @@
map.put(m.group(1), m.group(2));
} else {
log.warning("Error parsing '%1$s': \"%2$s\" is not a valid syntax",
                                propFile.getOsLocation(),
line);
return null;
}
//Synthetic comment -- @@ -645,7 +661,11 @@
// Return null below.
} catch (IOException e) {
log.warning("Error parsing '%1$s': %2$s.",
                    propFile.getOsLocation(),
                    e.getMessage());
        } catch (StreamException e) {
            log.warning("Error parsing '%1$s': %2$s.",
                    propFile.getOsLocation(),
e.getMessage());
} finally {
if (reader != null) {
//Synthetic comment -- @@ -655,13 +675,6 @@
// pass
}
}
}

return null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ApkData.java
//Synthetic comment -- index 50acebc..9c3a8c5 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
* Class representing one apk that needs to be generated. This contains
//Synthetic comment -- @@ -174,12 +175,12 @@
/**
* Writes the apk description in the given writer. a single line is used to write
* everything.
     * @param writer The {@link OutputStreamWriter} to write to.
* @throws IOException
*
* @see {@link #read(String)}
*/
    public void write(OutputStreamWriter writer) throws IOException {
for (int i = 0 ; i < ApkData.INDEX_MAX ; i++) {
write(i, writer);
}
//Synthetic comment -- @@ -198,7 +199,7 @@
}
}

    private void write(int index, OutputStreamWriter writer) throws IOException {
switch (index) {
case INDEX_OUTPUTNAME:
writeValue(writer, mOutputName);
//Synthetic comment -- @@ -240,11 +241,11 @@
}
}

    private static void writeValue(OutputStreamWriter writer, String value) throws IOException {
writer.append(value).append(',');
}

    private static void writeValue(OutputStreamWriter writer, int value) throws IOException {
writeValue(writer, Integer.toString(value));
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/MultiApkExportHelper.java
new file mode 100644
//Synthetic comment -- index 0000000..e2600bb

//Synthetic comment -- @@ -0,0 +1,407 @@
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
import com.android.sdklib.internal.project.ApkSettings;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.io.FileWrapper;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.IAbstractResource;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.io.IAbstractFolder.FilenameFilter;
import com.android.sdklib.xml.AndroidManifest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

public class MultiApkExportHelper {

    private final String mAppPackage;
    private final int mVersionCode;
    private final Target mTarget;

    public final static int MAX_MINOR = 100;
    public final static int MAX_BUILDINFO = 100;
    public final static int OFFSET_BUILD_INFO = MAX_MINOR;
    public final static int OFFSET_VERSION_CODE = OFFSET_BUILD_INFO * MAX_BUILDINFO;

    public static final class ExportException extends Exception {
        private static final long serialVersionUID = 1L;

        public ExportException(String message) {
            super(message);
        }

        public ExportException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static enum Target {
        RELEASE("release"), CLEAN("clean");

        private final String mName;

        Target(String name) {
            mName = name;
        }

        public String getTarget() {
            return mName;
        }

        public static Target getTarget(String value) {
            for (Target t : values()) {
                if (t.mName.equals(value)) {
                    return t;
                }

            }

            return null;
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
                        "Any change in the multi-apk configuration require a increment of the versionCode.",
                        mVersionCode));
            }

            for (int i = 0 ; i < previousApks.length ; i++) {
                // update the minor value from what is in the log file.
                apks[i].setMinor(previousApks[i].getMinor());
                if (apks[i].compareTo(previousApks[i]) != 0) {
                    throw new ExportException(String.format(
                            "Project export is setup differently from previous export at versionCode %d.\n" +
                            "Any change in the multi-apk configuration require a increment of the versionCode.",
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

            writer.append("# Multi-APK BUILD log.\n");
            writer.append("# Only edit manually to change minor versions.\n");

            writeValue(writer, "package", mAppPackage);
            writeValue(writer, "versionCode", mVersionCode);

            writer.append("# what follows is one line per generated apk with its description.\n");
            writer.append("# the format is CSV in the following order:\n");
            writer.append("# apkname,project,minor, minsdkversion, abi filter,\n");

            for (ApkData apk : apks) {
                apk.write(writer);
                writer.append('\n');
            }

            writer.flush();
        } catch (Exception e) {
            throw new ExportException("Failed to write build log", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new ExportException("Failed to write build log", e);
            }
        }
    }

    private void writeValue(OutputStreamWriter writer, String name, String value)
            throws IOException {
        writer.append(name).append('=').append(value).append('\n');
    }

    private void writeValue(OutputStreamWriter writer, String name, int value) throws IOException {
        writeValue(writer, name, Integer.toString(value));
    }

    /**
     * gets the projects to export from the property, checks they exist, validates them,
     * loads their export info and return it.
     * If a project does not exist or is not valid, this will throw a {@link BuildException}.
     * @param projects the Ant project.
     * @throws ExportException
     */
    private ApkData[] getProjects(String projects) throws ExportException {
        String[] paths = projects.split("\\:");

        ArrayList<ApkData> datalist = new ArrayList<ApkData>();

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

                ArrayList<ApkData> datalist2 = checkManifest(androidManifest);

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
     * Checks a manifest of the project for inclusion in the list of exported APK.
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
            data.setMinSdkVersion(minSdkVersion);

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
                        current = null;
                    }
                }
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
            int apkIndex = 0;
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
                        data.setBuildInfo(apkIndex++);
                        datalist.add(data);
                        data.read(line);
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java
//Synthetic comment -- index 7b22e3d..ff32a6c 100644

//Synthetic comment -- @@ -391,9 +391,7 @@
installTemplate("build.template",
new File(projectFolder, SdkConstants.FN_BUILD_XML),
keywords);
        } catch (Exception e) {
mLog.error(e, null);
}
}
//Synthetic comment -- @@ -517,7 +515,7 @@
try {
props.save();
println("Updated %1$s", PropertyType.DEFAULT.getFilename());
            } catch (Exception e) {
mLog.error(e, "Failed to write %1$s file in '%2$s'",
PropertyType.DEFAULT.getFilename(),
folderPath);
//Synthetic comment -- @@ -538,7 +536,7 @@
try {
props.save();
println("Updated %1$s", PropertyType.LOCAL.getFilename());
        } catch (Exception e) {
mLog.error(e, "Failed to write %1$s file in '%2$s'",
PropertyType.LOCAL.getFilename(),
folderPath);
//Synthetic comment -- @@ -718,7 +716,7 @@
try {
buildProps.save();
println("Updated %1$s", PropertyType.BUILD.getFilename());
        } catch (Exception e) {
mLog.error(e, "Failed to write %1$s file in '%2$s'",
PropertyType.BUILD.getFilename(),
folderPath);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index 734efda..d05c9f6 100644

//Synthetic comment -- @@ -18,9 +18,11 @@

import com.android.sdklib.SdkConstants;
import com.android.sdklib.SdkManager;
import com.android.sdklib.io.FolderWrapper;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.StreamException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
//Synthetic comment -- @@ -129,7 +131,7 @@
"# Used by the 'uninstall' rule.\n");
}

    private final IAbstractFolder mProjectFolder;
private final Map<String, String> mProperties;
private final PropertyType mType;

//Synthetic comment -- @@ -141,13 +143,24 @@
* @param type One the possible {@link PropertyType}s.
*/
public static ProjectProperties load(String projectFolderOsPath, PropertyType type) {
        IAbstractFolder wrapper = new FolderWrapper(projectFolderOsPath);
        return load(wrapper, type);
    }

    /**
     * Loads a project properties file and return a {@link ProjectProperties} object
     * containing the properties
     *
     * @param projectFolder the project folder.
     * @param type One the possible {@link PropertyType}s.
     */
    public static ProjectProperties load(IAbstractFolder projectFolder, PropertyType type) {
        if (projectFolder.exists()) {
            IAbstractFile propFile = projectFolder.getFile(type.mFilename);
            if (propFile.exists()) {
                Map<String, String> map = SdkManager.parsePropertyFile(propFile, null /* log */);
if (map != null) {
                    return new ProjectProperties(projectFolder, map, type);
}
}
}
//Synthetic comment -- @@ -172,11 +185,10 @@
* @return this object, for chaining.
*/
public synchronized ProjectProperties merge(PropertyType type) {
        if (mProjectFolder.exists()) {
            IAbstractFile propFile = mProjectFolder.getFile(type.mFilename);
            if (propFile.exists()) {
                Map<String, String> map = SdkManager.parsePropertyFile(propFile, null /* log */);
if (map != null) {
for(Entry<String, String> entry : map.entrySet()) {
String key = entry.getKey();
//Synthetic comment -- @@ -195,11 +207,23 @@
* Creates a new project properties object, with no properties.
* <p/>The file is not created until {@link #save()} is called.
* @param projectFolderOsPath the project folder.
     * @param type the type of property file to create
*/
public static ProjectProperties create(String projectFolderOsPath, PropertyType type) {
// create and return a ProjectProperties with an empty map.
        IAbstractFolder folder = new FolderWrapper(projectFolderOsPath);
        return create(folder, type);
    }

    /**
     * Creates a new project properties object, with no properties.
     * <p/>The file is not created until {@link #save()} is called.
     * @param projectFolder the project folder.
     * @param type the type of property file to create
     */
    public static ProjectProperties create(IAbstractFolder projectFolder, PropertyType type) {
        // create and return a ProjectProperties with an empty map.
        return new ProjectProperties(projectFolder, new HashMap<String, String>(), type);
}

/**
//Synthetic comment -- @@ -249,11 +273,10 @@
* Reloads the properties from the underlying file.
*/
public synchronized void reload() {
        if (mProjectFolder.exists()) {
            IAbstractFile propFile = mProjectFolder.getFile(mType.mFilename);
            if (propFile.exists()) {
                Map<String, String> map = SdkManager.parsePropertyFile(propFile, null /* log */);
if (map != null) {
mProperties.clear();
mProperties.putAll(map);
//Synthetic comment -- @@ -265,11 +288,12 @@
/**
* Saves the property file, using UTF-8 encoding.
* @throws IOException
     * @throws StreamException
*/
    public synchronized void save() throws IOException, StreamException {
        IAbstractFile toSave = mProjectFolder.getFile(mType.mFilename);

        OutputStreamWriter writer = new OutputStreamWriter(toSave.getOutputStream(),
SdkConstants.INI_CHARSET);

// write the header
//Synthetic comment -- @@ -298,9 +322,9 @@
* Use {@link #load(String, PropertyType)} or {@link #create(String, PropertyType)}
* to instantiate.
*/
    private ProjectProperties(IAbstractFolder projectFolder, Map<String, String> map,
PropertyType type) {
        mProjectFolder = projectFolder;
mProperties = map;
mType = type;
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FileWrapper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FileWrapper.java
//Synthetic comment -- index 13dea12..9a0a4a6 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
//Synthetic comment -- @@ -115,6 +116,17 @@
}
}

    public OutputStream getOutputStream() throws StreamException {
        try {
            return new FileOutputStream(this);
        } catch (FileNotFoundException e) {
            throw new StreamException(e);
        }
    }

    public PreferredWriteMode getPreferredWriteMode() {
        return PreferredWriteMode.OUTPUTSTREAM;
    }

public String getOsLocation() {
return getAbsolutePath();
//Synthetic comment -- @@ -124,4 +136,12 @@
public boolean exists() {
return isFile();
}

    public IAbstractFolder getParentFolder() {
        String p = this.getParent();
        if (p == null) {
            return null;
        }
        return new FolderWrapper(p);
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FolderWrapper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/FolderWrapper.java
//Synthetic comment -- index 97cfad2..d009b7f 100644

//Synthetic comment -- @@ -18,8 +18,8 @@


import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
* An implementation of {@link IAbstractFolder} extending {@link File}.
//Synthetic comment -- @@ -100,7 +100,7 @@

public boolean hasFile(final String name) {
String[] match = list(new FilenameFilter() {
            public boolean accept(IAbstractFolder dir, String filename) {
return name.equals(filename);
}
});
//Synthetic comment -- @@ -112,8 +112,41 @@
return new FileWrapper(this, name);
}

    public IAbstractFolder getFolder(String name) {
        return new FolderWrapper(this, name);
    }

    public IAbstractFolder getParentFolder() {
        String p = this.getParent();
        if (p == null) {
            return null;
        }
        return new FolderWrapper(p);
    }

    public String getOsLocation() {
        return getAbsolutePath();
    }

@Override
public boolean exists() {
return isDirectory();
}

    public String[] list(FilenameFilter filter) {
        File[] files = listFiles();
        if (files.length > 0) {
            ArrayList<String> list = new ArrayList<String>();

            for (File file : files) {
                if (filter.accept(this, file.getName())) {
                    list.add(file.getName());
                }
            }

            return list.toArray(new String[list.size()]);
        }

        return new String[0];
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IAbstractFile.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IAbstractFile.java
//Synthetic comment -- index a1fe667..2ff1fc8 100644

//Synthetic comment -- @@ -17,11 +17,15 @@
package com.android.sdklib.io;

import java.io.InputStream;
import java.io.OutputStream;

/**
* A file.
*/
public interface IAbstractFile extends IAbstractResource {
    public static enum PreferredWriteMode {
        INPUTSTREAM, OUTPUTSTREAM;
    }

/**
* Returns an {@link InputStream} object on the file content.
//Synthetic comment -- @@ -37,7 +41,13 @@
void setContents(InputStream source) throws StreamException;

/**
     * Returns an {@link OutputStream} to write into the file.
     * @throws StreamException
*/
    OutputStream getOutputStream() throws StreamException;

    /**
     * Returns the preferred mode to write into the file.
     */
    PreferredWriteMode getPreferredWriteMode();
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IAbstractFolder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IAbstractFolder.java
//Synthetic comment -- index 80a6f84..bfbc86d 100644

//Synthetic comment -- @@ -16,11 +16,25 @@

package com.android.sdklib.io;

/**
*  A folder.
*/
public interface IAbstractFolder extends IAbstractResource {
    /**
     * Instances of classes that implement this interface are used to
     * filter filenames.
     */
    public interface FilenameFilter {
        /**
         * Tests if a specified file should be included in a file list.
         *
         * @param   dir    the directory in which the file was found.
         * @param   name   the name of the file.
         * @return  <code>true</code> if and only if the name should be
         * included in the file list; <code>false</code> otherwise.
         */
        boolean accept(IAbstractFolder dir, String name);
    }

/**
* Returns true if the receiver contains a file with a given name
//Synthetic comment -- @@ -37,7 +51,16 @@
IAbstractFile getFile(String name);

/**
     * returns an {@link IAbstractFolder} representing a child of the current folder with the
     * given name. The folder may not actually exist.
     * @param name the name of the folder.
     */
    IAbstractFolder getFolder(String name);

    /**
* returns a list of existing members in this folder.
*/
IAbstractResource[] listMembers();

    String[] list(FilenameFilter filter);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IAbstractResource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/io/IAbstractResource.java
//Synthetic comment -- index ccc4988..0ccb107 100644

//Synthetic comment -- @@ -29,7 +29,17 @@
String getName();

/**
     * Returns the OS path of the folder location.
     */
    String getOsLocation();

    /**
* Returns whether the resource actually exists.
*/
boolean exists();

    /**
     * Returns the parent folder or null if there is no parent.
     */
    IAbstractFolder getParentFolder();
}







