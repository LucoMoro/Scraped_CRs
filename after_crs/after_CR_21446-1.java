/*Rename AndroidConstants -> AdtConstants.

These are constants specific to ADT.

There'll be an AndroidConstants class in common.jar with
more generic android constant values.

Change-Id:I8368920f92c28cbfb87098087bf01f2d2cdee095*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java
similarity index 99%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java
//Synthetic comment -- index 85e265c..0b14358 100644

//Synthetic comment -- @@ -43,7 +43,7 @@
* </ul>
*
*/
public class AdtConstants {
/**
* The old Editors Plugin ID. It is still used in some places for compatibility.
* Please do not use for new features.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index fd8218c..28244b9 100644

//Synthetic comment -- @@ -354,7 +354,7 @@
/** Returns the absolute traceview path */
public static String getOsAbsoluteTraceview() {
return getOsSdkFolder() + SdkConstants.OS_SDK_TOOLS_FOLDER +
                AdtConstants.FN_TRACEVIEW;
}

/** Returns the absolute emulator path */
//Synthetic comment -- @@ -364,7 +364,7 @@

public static String getOsAbsoluteHprofConv() {
return getOsSdkFolder() + SdkConstants.OS_SDK_TOOLS_FOLDER +
                AdtConstants.FN_HPROF_CONV;
}

/** Returns the absolute proguard path */
//Synthetic comment -- @@ -377,7 +377,7 @@
*/
public static String getUrlDoc() {
return ProjectHelper.getJavaDocPath(
                getOsSdkFolder() + AdtConstants.WS_JAVADOC_FOLDER_LEAF);
}

/**
//Synthetic comment -- @@ -778,7 +778,7 @@
public static InputStream readEmbeddedFileAsStream(String filepath) {
// attempt to read an embedded file
try {
            URL url = getEmbeddedFileUrl(AdtConstants.WS_SEP + filepath);
if (url != null) {
return url.openStream();
}
//Synthetic comment -- @@ -811,8 +811,8 @@

// attempt to get a file to one of the template.
String path = filepath;
        if (!path.startsWith(AdtConstants.WS_SEP)) {
            path = AdtConstants.WS_SEP + path;
}

URL url = bundle.getEntry(path);
//Synthetic comment -- @@ -1416,7 +1416,7 @@
* @see IFileListener#fileChanged
*/
public void fileChanged(IFile file, IMarkerDelta[] markerDeltas, int kind) {
                if (AdtConstants.EXT_XML.equals(file.getFileExtension())) {
// The resources files must have a file path similar to
//    project/res/.../*.xml
// There is no support for sub folders, so the segment count must be 4
//Synthetic comment -- @@ -1454,7 +1454,7 @@
// as the default editor.
IEditorDescriptor desc = IDE.getDefaultEditor(file);
String editorId = desc.getId();
                                if (editorId.startsWith(AdtConstants.EDITORS_NAMESPACE)) {
// reset the default editor.
IDE.setDefaultEditor(file, null);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/ConvertToAndroidAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/ConvertToAndroidAction.java
//Synthetic comment -- index 92604fb..cf46192 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.actions;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;

import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -107,7 +107,7 @@

// check if the project already has the android nature.
for (int i = 0; i < natures.length; ++i) {
                        if (AdtConstants.NATURE_DEFAULT.equals(natures[i])) {
// we shouldn't be here as the visibility of the item
// is dependent on the project.
return new Status(Status.WARNING, AdtPlugin.PLUGIN_ID,
//Synthetic comment -- @@ -121,7 +121,7 @@

String[] newNatures = new String[natures.length + 1];
System.arraycopy(natures, 0, newNatures, 1, natures.length);
                    newNatures[0] = AdtConstants.NATURE_DEFAULT;

// set the new nature list in the project
description.setNatureIds(newNatures);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java
//Synthetic comment -- index 504cf0b..acf150a 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.build;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;

import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -180,7 +180,7 @@

// check the values and attempt to mark the file.
if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AdtConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
return true;
}
continue;
//Synthetic comment -- @@ -204,7 +204,7 @@

// display the error
if (checkAndMark(location, null, msg, osRoot, project,
                        AdtConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
return true;
}

//Synthetic comment -- @@ -228,7 +228,7 @@

// check the values and attempt to mark the file.
if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AdtConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
return true;
}
continue;
//Synthetic comment -- @@ -242,7 +242,7 @@

// check the values and attempt to mark the file.
if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AdtConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
return true;
}

//Synthetic comment -- @@ -266,7 +266,7 @@

// check the values and attempt to mark the file.
if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AdtConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
return true;
}

//Synthetic comment -- @@ -282,7 +282,7 @@

// check the values and attempt to mark the file.
if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AdtConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_WARNING) == false) {
return true;
}

//Synthetic comment -- @@ -298,7 +298,7 @@

// check the values and attempt to mark the file.
if (checkAndMark(location, lineStr, msg, osRoot, project,
                        AdtConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
return true;
}

//Synthetic comment -- @@ -313,7 +313,7 @@

// check the values and attempt to mark the file.
if (checkAndMark(location, null, msg, osRoot, project,
                        AdtConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
return true;
}

//Synthetic comment -- @@ -331,7 +331,7 @@

// check the values and attempt to mark the file.
if (checkAndMark(null /*location*/, null, msg, osRoot, project,
                        AdtConstants.MARKER_AAPT_PACKAGE, IMarker.SEVERITY_ERROR) == false) {
return true;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java
//Synthetic comment -- index 9d6a819..e05f8b7 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.build;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.builders.BaseBuilder;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
//Synthetic comment -- @@ -81,7 +81,7 @@

@Override
protected String getExtension() {
        return AdtConstants.EXT_AIDL;
}

@Override
//Synthetic comment -- @@ -111,7 +111,7 @@
boolean verbose = AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE;

// remove the generic marker from the project
        builder.removeMarkersFromResource(project, AdtConstants.MARKER_AIDL);

// loop until we've compile them all
for (IFile sourceFile : sources) {
//Synthetic comment -- @@ -127,7 +127,7 @@
}

// Remove the AIDL error markers from the aidl file
            builder.removeMarkersFromResource(sourceFile, AdtConstants.MARKER_AIDL);

// get the path of the source file.
IPath sourcePath = sourceFile.getLocation();
//Synthetic comment -- @@ -222,7 +222,7 @@
AdtPlugin.printErrorToConsole(project, results.toArray());

// mark the project
                        BaseProjectHelper.markResource(project, AdtConstants.MARKER_AIDL,
Messages.Unparsed_AIDL_Errors, IMarker.SEVERITY_ERROR);
} else {
AdtPlugin.printToConsole(project, results.toArray());
//Synthetic comment -- @@ -233,13 +233,13 @@
} catch (IOException e) {
// mark the project and exit
String msg = String.format(Messages.AIDL_Exec_Error, command[0]);
            BaseProjectHelper.markResource(project, AdtConstants.MARKER_AIDL, msg,
IMarker.SEVERITY_ERROR);
return false;
} catch (InterruptedException e) {
// mark the project and exit
String msg = String.format(Messages.AIDL_Exec_Error, command[0]);
            BaseProjectHelper.markResource(project, AdtConstants.MARKER_AIDL, msg,
IMarker.SEVERITY_ERROR);
return false;
}
//Synthetic comment -- @@ -282,7 +282,7 @@
}

// mark the file
                BaseProjectHelper.markResource(file, AdtConstants.MARKER_AIDL, msg, line,
IMarker.SEVERITY_ERROR);

// success, go to the next line
//Synthetic comment -- @@ -329,7 +329,7 @@

// Build the Java file name from the aidl name.
String javaName = sourceFile.getName().replaceAll(
                    AdtConstants.RE_AIDL_EXT, AdtConstants.DOT_JAVA);

// get the resource for the java file.
IFile javaFile = destinationFolder.getFile(javaName);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 545d0f9..1d6bad3 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.build;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
//Synthetic comment -- @@ -145,10 +145,10 @@
// need to figure out some path before we can execute aapt;

// get the resource folder
        IFolder resFolder = mProject.getFolder(AdtConstants.WS_RESOURCES);

// and the assets folder
        IFolder assetsFolder = mProject.getFolder(AdtConstants.WS_ASSETS);

// we need to make sure this one exists.
if (assetsFolder.exists() == false) {
//Synthetic comment -- @@ -747,7 +747,7 @@
// (This is to handle the case of reference Android projects in the context of
// instrumentation projects that need to reference the projects to be tested).
if (referencedJavaProject.getProject().hasNature(
                    AdtConstants.NATURE_DEFAULT) == false) {
writeStandardProjectResources(apkBuilder, referencedJavaProject, wsRoot, list);
}
}
//Synthetic comment -- @@ -842,7 +842,7 @@

IResource resource = wsRoot.findMember(path);
// case of a jar file (which could be relative to the workspace or a full path)
                    if (AdtConstants.EXT_JAR.equalsIgnoreCase(path.getFileExtension())) {
if (resource != null && resource.exists() &&
resource.getType() == IResource.FILE) {
oslibraryList.add(resource.getLocation().toOSString());
//Synthetic comment -- @@ -908,7 +908,7 @@
// only include output from non android referenced project
// (This is to handle the case of reference Android projects in the context of
// instrumentation projects that need to reference the projects to be tested).
            if (javaProject.getProject().hasNature(AdtConstants.NATURE_DEFAULT) == false) {
// get the output folder
IPath path = null;
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptProcessor.java
//Synthetic comment -- index 30e3d5c..e1a4fa0 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.build;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.builders.BaseBuilder;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
//Synthetic comment -- @@ -70,7 +70,7 @@
boolean r = super.handleGeneratedFile(file, kind);
if (r == false &&
kind == IResourceDelta.REMOVED &&
                    AdtConstants.EXT_DEP.equalsIgnoreCase(file.getFileExtension())) {
// This looks to be an extension file.
// For futureproofness let's make sure this dependency file was generated by
// this processor even if it's the only processor using them for now.
//Synthetic comment -- @@ -85,8 +85,8 @@
// remove the file name segment
relative = relative.removeLastSegments(1);
// add the file name of a Renderscript file.
                relative = relative.append(file.getName().replaceAll(AdtConstants.RE_DEP_EXT,
                        AdtConstants.DOT_RS));

// now look for a match in the source folders.
List<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(
//Synthetic comment -- @@ -122,7 +122,7 @@

@Override
protected String getExtension() {
        return AdtConstants.EXT_RS;
}

@Override
//Synthetic comment -- @@ -166,7 +166,7 @@
boolean someSuccess = false;

// remove the generic marker from the project
        builder.removeMarkersFromResource(project, AdtConstants.MARKER_RENDERSCRIPT);

// loop until we've compile them all
for (IFile sourceFile : sources) {
//Synthetic comment -- @@ -182,11 +182,11 @@
}

// Remove the RS error markers from the source file and the dependencies
            builder.removeMarkersFromResource(sourceFile, AdtConstants.MARKER_RENDERSCRIPT);
SourceFileData data = getFileData(sourceFile);
if (data != null) {
for (IFile dep : data.getDependencyFiles()) {
                    builder.removeMarkersFromResource(dep, AdtConstants.MARKER_RENDERSCRIPT);
}
}

//Synthetic comment -- @@ -254,7 +254,7 @@

// mark the project
BaseProjectHelper.markResource(project,
                                AdtConstants.MARKER_RENDERSCRIPT,
"Unparsed Renderscript error! Check the console for output.",
IMarker.SEVERITY_ERROR);
} else {
//Synthetic comment -- @@ -268,7 +268,7 @@
String msg = String.format(
"Error executing Renderscript. Please check llvm-rs-cc is present at %1$s",
command[0]);
            BaseProjectHelper.markResource(project, AdtConstants.MARKER_RENDERSCRIPT, msg,
IMarker.SEVERITY_ERROR);
return false;
} catch (InterruptedException e) {
//Synthetic comment -- @@ -276,7 +276,7 @@
String msg = String.format(
"Error executing Renderscript. Please check llvm-rs-cc is present at %1$s",
command[0]);
            BaseProjectHelper.markResource(project, AdtConstants.MARKER_RENDERSCRIPT, msg,
IMarker.SEVERITY_ERROR);
return false;
}
//Synthetic comment -- @@ -344,7 +344,7 @@
}

// mark the file
                BaseProjectHelper.markResource(f, AdtConstants.MARKER_RENDERSCRIPT, msg, line,
IMarker.SEVERITY_ERROR);

// success, go to the next line
//Synthetic comment -- @@ -417,8 +417,8 @@

private IFile getDependencyFileFor(IFile sourceFile) {
IFolder depFolder = getDependencyFolder(sourceFile);
        return depFolder.getFile(sourceFile.getName().replaceAll(AdtConstants.RE_RS_EXT,
                AdtConstants.DOT_DEP));
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java
//Synthetic comment -- index af4d0ab..293b340 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.Messages;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
//Synthetic comment -- @@ -151,7 +151,7 @@
IFile file = (IFile)resource;

// remove previous markers
            removeMarkersFromResource(file, AdtConstants.MARKER_XML);

// create  the error handler
XmlErrorHandler reporter = new XmlErrorHandler(file, visitor);
//Synthetic comment -- @@ -309,9 +309,9 @@
}

// abort if there are TARGET or ADT type markers
        stopOnMarker(iProject, AdtConstants.MARKER_TARGET, IResource.DEPTH_ZERO,
false /*checkSeverity*/);
        stopOnMarker(iProject, AdtConstants.MARKER_ADT, IResource.DEPTH_ZERO,
false /*checkSeverity*/);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 1d3d13b..71c38f8 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.build.AaptExecException;
import com.android.ide.eclipse.adt.internal.build.AaptParser;
//Synthetic comment -- @@ -134,7 +134,7 @@
int type = resource.getType();
if (type == IResource.FILE) {
String ext = resource.getFileExtension();
                    if (AdtConstants.EXT_CLASS.equals(ext)) {
mConvertToDex = true;
}
}
//Synthetic comment -- @@ -178,7 +178,7 @@

private ResourceMarker mResourceMarker = new ResourceMarker() {
public void setWarning(IResource resource, String message) {
            BaseProjectHelper.markResource(resource, AdtConstants.MARKER_PACKAGING,
message, IMarker.SEVERITY_WARNING);
}
};
//Synthetic comment -- @@ -196,8 +196,8 @@
IProject project = getProject();

// Clear the project of the generic markers
        removeMarkersFromContainer(project, AdtConstants.MARKER_AAPT_PACKAGE);
        removeMarkersFromContainer(project, AdtConstants.MARKER_PACKAGING);
}

// build() returns a list of project from which this project depends for future compilation.
//Synthetic comment -- @@ -332,11 +332,11 @@
}

// remove older packaging markers.
            removeMarkersFromContainer(javaProject.getProject(), AdtConstants.MARKER_PACKAGING);

if (outputFolder == null) {
// mark project and exit
                markProject(AdtConstants.MARKER_PACKAGING, Messages.Failed_To_Get_Output,
IMarker.SEVERITY_ERROR);
return allRefProjects;
}
//Synthetic comment -- @@ -357,7 +357,7 @@

if (mPackageResources == false) {
// check the full resource package
                tmp = outputFolder.findMember(AdtConstants.FN_RESOURCES_AP_);
if (tmp == null || tmp.exists() == false) {
mPackageResources = true;
mBuildFinalPackage = true;
//Synthetic comment -- @@ -419,13 +419,13 @@
// mark project and exit
String msg = String.format(Messages.s_File_Missing,
SdkConstants.FN_ANDROID_MANIFEST_XML);
                    markProject(AdtConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
return allRefProjects;
}

IPath binLocation = outputFolder.getLocation();
if (binLocation == null) {
                    markProject(AdtConstants.MARKER_PACKAGING, Messages.Output_Missing,
IMarker.SEVERITY_ERROR);
return allRefProjects;
}
//Synthetic comment -- @@ -445,14 +445,14 @@
// first we check if we need to package the resources.
if (mPackageResources) {
// remove some aapt_package only markers.
                    removeMarkersFromContainer(project, AdtConstants.MARKER_AAPT_PACKAGE);

try {
helper.packageResources(manifestFile, libProjects, null /*resfilter*/,
0 /*versionCode */, osBinPath,
                                AdtConstants.FN_RESOURCES_AP_);
} catch (AaptExecException e) {
                        BaseProjectHelper.markResource(project, AdtConstants.MARKER_PACKAGING,
e.getMessage(), IMarker.SEVERITY_ERROR);
return allRefProjects;
} catch (AaptResultException e) {
//Synthetic comment -- @@ -468,7 +468,7 @@
// therefore not all files that should have been marked, were marked),
// we put a generic marker on the project and abort.
BaseProjectHelper.markResource(project,
                                    AdtConstants.MARKER_PACKAGING,
Messages.Unparsed_AAPT_Errors,
IMarker.SEVERITY_ERROR);
}
//Synthetic comment -- @@ -493,7 +493,7 @@
String message = e.getMessage();

AdtPlugin.printErrorToConsole(project, message);
                        BaseProjectHelper.markResource(project, AdtConstants.MARKER_PACKAGING,
message, IMarker.SEVERITY_ERROR);

Throwable cause = e.getCause();
//Synthetic comment -- @@ -523,7 +523,7 @@
SdkConstants.FN_APK_CLASSES_DEX;
try {
helper.finalDebugPackage(
                        osBinPath + File.separator + AdtConstants.FN_RESOURCES_AP_,
classesDexPath, osFinalPackagePath,
javaProject, libProjects, referencedJavaProjects, mResourceMarker);
} catch (KeytoolException e) {
//Synthetic comment -- @@ -531,7 +531,7 @@

// mark the project with the standard message
String msg = String.format(Messages.Final_Archive_Error_s, eMessage);
                    BaseProjectHelper.markResource(project, AdtConstants.MARKER_PACKAGING, msg,
IMarker.SEVERITY_ERROR);

// output more info in the console
//Synthetic comment -- @@ -547,19 +547,19 @@

// mark the project with the standard message
String msg = String.format(Messages.Final_Archive_Error_s, eMessage);
                    BaseProjectHelper.markResource(project, AdtConstants.MARKER_PACKAGING, msg,
IMarker.SEVERITY_ERROR);
} catch (AndroidLocationException e) {
String eMessage = e.getMessage();

// mark the project with the standard message
String msg = String.format(Messages.Final_Archive_Error_s, eMessage);
                    BaseProjectHelper.markResource(project, AdtConstants.MARKER_PACKAGING, msg,
IMarker.SEVERITY_ERROR);
} catch (NativeLibInJarException e) {
String msg = e.getMessage();

                    BaseProjectHelper.markResource(project, AdtConstants.MARKER_PACKAGING,
msg, IMarker.SEVERITY_ERROR);

AdtPlugin.printErrorToConsole(project, (Object[]) e.getAdditionalInfo());
//Synthetic comment -- @@ -567,7 +567,7 @@
// mark project and return
String msg = String.format(Messages.Final_Archive_Error_s, e.getMessage());
AdtPlugin.printErrorToConsole(project, msg);
                    BaseProjectHelper.markResource(project, AdtConstants.MARKER_PACKAGING, msg,
IMarker.SEVERITY_ERROR);
} catch (DuplicateFileException e) {
String msg1 = String.format(
//Synthetic comment -- @@ -575,7 +575,7 @@
e.getArchivePath(), e.getFile1(), e.getFile2());
String msg2 = String.format(Messages.Final_Archive_Error_s, msg1);
AdtPlugin.printErrorToConsole(project, msg2);
                    BaseProjectHelper.markResource(project, AdtConstants.MARKER_PACKAGING, msg2,
IMarker.SEVERITY_ERROR);
}

//Synthetic comment -- @@ -618,7 +618,7 @@

msg = String.format("Unknown error: %1$s", msg);
AdtPlugin.logAndPrintError(exception, project.getName(), msg);
            markProject(AdtConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
}

return allRefProjects;
//Synthetic comment -- @@ -643,13 +643,13 @@

// do a (hopefully quick) search for Precompiler type markers. Those are always only
// errors.
        stopOnMarker(iProject, AdtConstants.MARKER_AAPT_COMPILE, IResource.DEPTH_INFINITE,
false /*checkSeverity*/);
        stopOnMarker(iProject, AdtConstants.MARKER_AIDL, IResource.DEPTH_INFINITE,
false /*checkSeverity*/);
        stopOnMarker(iProject, AdtConstants.MARKER_RENDERSCRIPT, IResource.DEPTH_INFINITE,
false /*checkSeverity*/);
        stopOnMarker(iProject, AdtConstants.MARKER_ANDROID, IResource.DEPTH_ZERO,
false /*checkSeverity*/);

// do a search for JDT markers. Those can be errors or warnings








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerDeltaVisitor.java
//Synthetic comment -- index 7df8ef4..7e426fd 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.builders.BaseBuilder.BaseDeltaVisitor;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -177,7 +177,7 @@
// just check this is a .class file. Any modification will
// trigger a change in the classes.dex file
String ext = resource.getFileExtension();
                if (AdtConstants.EXT_CLASS.equalsIgnoreCase(ext)) {
mConvertToDex = true;
mMakeFinalPackage = true;

//Synthetic comment -- @@ -198,8 +198,8 @@
mConvertToDex = true;
mMakeFinalPackage = true;
} else if (resourceName.equalsIgnoreCase(
                                AdtConstants.FN_RESOURCES_AP_) ||
                                AdtConstants.PATTERN_RESOURCES_S_AP_.matcher(
resourceName).matches()) {
// or if the default resources.ap_ or a configured version
// (resources-###.ap_) was removed.
//Synthetic comment -- @@ -237,7 +237,7 @@
} else if (mLibFolder != null && mLibFolder.isPrefixOf(path)) {
// inside the native library folder. Test if the changed resource is a .so file.
if (type == IResource.FILE &&
                    (AdtConstants.EXT_NATIVE_LIB.equalsIgnoreCase(path.getFileExtension())
|| SdkConstants.FN_GDBSERVER.equals(resource.getName()))) {
mMakeFinalPackage = true;
return false; // return false for file.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index fd3a07d..167f39f 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.AaptParser;
import com.android.ide.eclipse.adt.internal.build.AidlProcessor;
import com.android.ide.eclipse.adt.internal.build.SourceProcessor;
//Synthetic comment -- @@ -303,7 +303,7 @@
String msg = String.format(Messages.s_File_Missing,
SdkConstants.FN_ANDROID_MANIFEST_XML);
AdtPlugin.printErrorToConsole(project, msg);
                markProject(AdtConstants.MARKER_ADT, msg, IMarker.SEVERITY_ERROR);

return result;

//Synthetic comment -- @@ -350,7 +350,7 @@
AndroidVersion projectVersion = projectTarget.getVersion();

// remove earlier marker from the manifest
                removeMarkersFromResource(manifestFile, AdtConstants.MARKER_ADT);

if (minSdkValue != -1) {
String codename = projectVersion.getCodename();
//Synthetic comment -- @@ -360,7 +360,7 @@
"Platform %1$s is a preview and requires application manifest to set %2$s to '%1$s'",
codename, AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION);
AdtPlugin.printErrorToConsole(project, msg);
                        BaseProjectHelper.markResource(manifestFile, AdtConstants.MARKER_ADT,
msg, IMarker.SEVERITY_ERROR);
return result;
} else if (minSdkValue < projectVersion.getApiLevel()) {
//Synthetic comment -- @@ -370,7 +370,7 @@
AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION,
minSdkValue, projectVersion.getApiLevel());
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project, msg);
                        BaseProjectHelper.markResource(manifestFile, AdtConstants.MARKER_ADT,
msg, IMarker.SEVERITY_WARNING);
} else if (minSdkValue > projectVersion.getApiLevel()) {
// integer minSdk is too high for the target => warning
//Synthetic comment -- @@ -379,7 +379,7 @@
AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION,
minSdkValue, projectVersion.getApiLevel());
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project, msg);
                        BaseProjectHelper.markResource(manifestFile, AdtConstants.MARKER_ADT,
msg, IMarker.SEVERITY_WARNING);
}
} else {
//Synthetic comment -- @@ -392,7 +392,7 @@
"Manifest attribute '%1$s' is set to '%2$s'. Integer is expected.",
AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION, minSdkVersion);
AdtPlugin.printErrorToConsole(project, msg);
                        BaseProjectHelper.markResource(manifestFile, AdtConstants.MARKER_ADT,
msg, IMarker.SEVERITY_ERROR);
return result;
} else if (codename.equals(minSdkVersion) == false) {
//Synthetic comment -- @@ -401,7 +401,7 @@
"Value of manifest attribute '%1$s' does not match platform codename '%2$s'",
AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION, codename);
AdtPlugin.printErrorToConsole(project, msg);
                        BaseProjectHelper.markResource(manifestFile, AdtConstants.MARKER_ADT,
msg, IMarker.SEVERITY_ERROR);
return result;
}
//Synthetic comment -- @@ -414,7 +414,7 @@
"Platform %1$s is a preview and requires application manifests to set %2$s to '%1$s'",
codename, AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION);
AdtPlugin.printErrorToConsole(project, msg);
                BaseProjectHelper.markResource(manifestFile, AdtConstants.MARKER_ADT, msg,
IMarker.SEVERITY_ERROR);
return result;
}
//Synthetic comment -- @@ -424,7 +424,7 @@
String msg = String.format(Messages.s_Doesnt_Declare_Package_Error,
SdkConstants.FN_ANDROID_MANIFEST_XML);
AdtPlugin.printErrorToConsole(project, msg);
                BaseProjectHelper.markResource(manifestFile, AdtConstants.MARKER_ADT,
msg, IMarker.SEVERITY_ERROR);

return result;
//Synthetic comment -- @@ -434,7 +434,7 @@
"Application package '%1$s' must have a minimum of 2 segments.",
SdkConstants.FN_ANDROID_MANIFEST_XML);
AdtPlugin.printErrorToConsole(project, msg);
                BaseProjectHelper.markResource(manifestFile, AdtConstants.MARKER_ADT,
msg, IMarker.SEVERITY_ERROR);

return result;
//Synthetic comment -- @@ -530,11 +530,11 @@
}

// Clear the project of the generic markers
        removeMarkersFromContainer(project, AdtConstants.MARKER_AAPT_COMPILE);
        removeMarkersFromContainer(project, AdtConstants.MARKER_XML);
        removeMarkersFromContainer(project, AdtConstants.MARKER_AIDL);
        removeMarkersFromContainer(project, AdtConstants.MARKER_RENDERSCRIPT);
        removeMarkersFromContainer(project, AdtConstants.MARKER_ANDROID);
}

@Override
//Synthetic comment -- @@ -576,7 +576,7 @@
private void handleResources(IProject project, String javaPackage, IAndroidTarget projectTarget,
IFile manifest, List<IProject> libProjects) throws CoreException, AbortBuildException {
// get the resource folder
        IFolder resFolder = project.getFolder(AdtConstants.WS_RESOURCES);

// get the file system path
IPath outputLocation = mGenFolder.getLocation();
//Synthetic comment -- @@ -591,8 +591,8 @@
String osManifestPath = manifestLocation.toOSString();

// remove the aapt markers
            removeMarkersFromResource(manifest, AdtConstants.MARKER_AAPT_COMPILE);
            removeMarkersFromContainer(resFolder, AdtConstants.MARKER_AAPT_COMPILE);

AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project,
Messages.Preparing_Generated_Files);
//Synthetic comment -- @@ -661,7 +661,7 @@
// We actually need to delete the manifest.java as it may become empty and
// in this case aapt doesn't generate an empty one, but instead doesn't
// touch it.
        IFile manifestJavaFile = packageFolder.getFile(AdtConstants.FN_MANIFEST_CLASS);
manifestJavaFile.getLocation().toFile().delete();

// launch aapt: create the command line
//Synthetic comment -- @@ -737,7 +737,7 @@
// (and therefore not all files that should have been marked,
// were marked), we put a generic marker on the project and abort.
if (parsingError) {
                    markProject(AdtConstants.MARKER_ADT,
Messages.Unparsed_AAPT_Errors, IMarker.SEVERITY_ERROR);
}

//Synthetic comment -- @@ -751,7 +751,7 @@
// something happen while executing the process,
// mark the project and exit
String msg = String.format(Messages.AAPT_Exec_Error, array.get(0));
            markProject(AdtConstants.MARKER_ADT, msg, IMarker.SEVERITY_ERROR);

// This interrupts the build.
throw new AbortBuildException();
//Synthetic comment -- @@ -759,7 +759,7 @@
// we got interrupted waiting for the process to end...
// mark the project and exit
String msg = String.format(Messages.AAPT_Exec_Error, array.get(0));
            markProject(AdtConstants.MARKER_ADT, msg, IMarker.SEVERITY_ERROR);

// This interrupts the build.
throw new AbortBuildException();
//Synthetic comment -- @@ -783,11 +783,11 @@
*/
private IPath getJavaPackagePath(String javaPackageName) {
// convert the java package into path
        String[] segments = javaPackageName.split(AdtConstants.RE_DOT);

StringBuilder path = new StringBuilder();
for (String s : segments) {
           path.append(AdtConstants.WS_SEP_CHAR);
path.append(s);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerDeltaVisitor.java
//Synthetic comment -- index 6660cd2..cd99fbe 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.SourceChangeHandler;
import com.android.ide.eclipse.adt.internal.build.SourceProcessor;
import com.android.ide.eclipse.adt.internal.build.Messages;
//Synthetic comment -- @@ -199,9 +199,9 @@
IFile manifestFile = (IFile)resource;

if (manifestFile.exists()) {
                        manifestFile.deleteMarkers(AdtConstants.MARKER_XML, true,
IResource.DEPTH_ZERO);
                        manifestFile.deleteMarkers(AdtConstants.MARKER_ANDROID, true,
IResource.DEPTH_ZERO);
}

//Synthetic comment -- @@ -262,8 +262,8 @@
String fileName = resource.getName();

// Special case of R.java/Manifest.java.
                if (AdtConstants.FN_RESOURCE_CLASS.equals(fileName) ||
                        AdtConstants.FN_MANIFEST_CLASS.equals(fileName)) {
// if it was removed, there's a possibility that it was removed due to a
// package change, or an aidl that was removed, but the only thing
// that will happen is that we'll have an extra build. Not much of a problem.
//Synthetic comment -- @@ -324,17 +324,17 @@
case IResourceDelta.CHANGED:
// display verbose message
message = String.format(Messages.s_Modified_Recreating_s, p,
                            AdtConstants.FN_RESOURCE_CLASS);
break;
case IResourceDelta.ADDED:
// display verbose message
message = String.format(Messages.Added_s_s_Needs_Updating, p,
                            AdtConstants.FN_RESOURCE_CLASS);
break;
case IResourceDelta.REMOVED:
// display verbose message
message = String.format(Messages.s_Removed_s_Needs_Updating, p,
                            AdtConstants.FN_RESOURCE_CLASS);
break;
}
if (message != null) {
//Synthetic comment -- @@ -346,7 +346,7 @@
handler.handleResourceFile((IFile)resource, kind);
}

            if (AdtConstants.EXT_XML.equalsIgnoreCase(ext)) {
if (kind != IResourceDelta.REMOVED) {
// check xml Validity
mBuilder.checkXML(resource, this);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/ResourceManagerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/ResourceManagerBuilder.java
//Synthetic comment -- index 0908260..8c4127e 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.Messages;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
//Synthetic comment -- @@ -64,7 +64,7 @@
IProject project = getProject();

// Clear the project of the generic markers
        removeMarkersFromContainer(project, AdtConstants.MARKER_ADT);
}

// build() returns a list of project from which this project depends for future compilation.
//Synthetic comment -- @@ -77,7 +77,7 @@
IJavaProject javaProject = JavaCore.create(project);

// Clear the project of the generic markers
        removeMarkersFromContainer(project, AdtConstants.MARKER_ADT);

// check for existing target marker, in which case we abort.
// (this means: no SDK, no target, or unresolvable target.)
//Synthetic comment -- @@ -104,7 +104,7 @@
}

if (errorMessage != null) {
            markProject(AdtConstants.MARKER_ADT, errorMessage, IMarker.SEVERITY_ERROR);
AdtPlugin.printErrorToConsole(project, errorMessage);

return null;
//Synthetic comment -- @@ -115,7 +115,7 @@

if (osSdkFolder == null || osSdkFolder.length() == 0) {
AdtPlugin.printErrorToConsole(project, Messages.No_SDK_Setup_Error);
            markProject(AdtConstants.MARKER_ADT, Messages.No_SDK_Setup_Error,
IMarker.SEVERITY_ERROR);

return null;
//Synthetic comment -- @@ -166,7 +166,7 @@
}

AdtPlugin.printErrorToConsole(project, message);
            markProject(AdtConstants.MARKER_ADT, message, IMarker.SEVERITY_ERROR);

return null;
} else if (hasGenSrcFolder == false || genFolderPresent == false) {
//Synthetic comment -- @@ -214,12 +214,12 @@
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project, Messages.Refreshing_Res);

// refresh the res folder.
            IFolder resFolder = project.getFolder(AdtConstants.WS_RESOURCES);
resFolder.refreshLocal(IResource.DEPTH_INFINITE, monitor);

// Also refresh the assets folder to make sure the ApkBuilder
// will now it's changed and will force a new resource packaging.
            IFolder assetsFolder = project.getFolder(AdtConstants.WS_ASSETS);
assetsFolder.refreshLocal(IResource.DEPTH_INFINITE, monitor);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 970e93c..1fea25a 100644

//Synthetic comment -- @@ -34,7 +34,7 @@

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -64,7 +64,7 @@
* The path in the online documentation for the manifest description.
* <p/>
* This is NOT a complete URL. To be used, it needs to be appended
     * to {@link AdtConstants#CODESITE_BASE_URL} or to the local SDK
* documentation.
*/
public static final String MANIFEST_SDK_URL = "/reference/android/R.styleable.html#";  //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/export/ExportEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/export/ExportEditor.java
//Synthetic comment -- index 50d9fd8..769f74e 100755

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.export;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidTextEditor;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -32,7 +32,7 @@
*/
public class ExportEditor extends AndroidTextEditor {

    public static final String ID = AdtConstants.EDITORS_NAMESPACE + ".text.ExportEditor"; //$NON-NLS-1$

private ExportPropertiesPage mExportPropsPage;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index 6be764e..aed740f 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -65,7 +65,7 @@
*/
public class LayoutEditor extends AndroidXmlEditor implements IShowEditorInput, IPartListener {

    public static final String ID = AdtConstants.EDITORS_NAMESPACE + ".layout.LayoutEditor"; //$NON-NLS-1$

/** Root node of the UI element hierarchy */
private UiDocumentNode mUiRootNode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java
//Synthetic comment -- index a19a4db..81dc818 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
//Synthetic comment -- @@ -173,7 +173,7 @@

boolean hasAndroidNature = false;
try {
                hasAndroidNature = project.hasNature(AdtConstants.NATURE_DEFAULT);
} catch (CoreException e) {
// do nothing if the nature cannot be queried.
return;
//Synthetic comment -- @@ -191,7 +191,7 @@

for (IProject p : referencingProjects) {
try {
                        hasAndroidNature = p.hasNature(AdtConstants.NATURE_DEFAULT);
} catch (CoreException e) {
// do nothing if the nature cannot be queried.
continue;
//Synthetic comment -- @@ -220,7 +220,7 @@

// here we only care about code change (so change for .class files).
// Resource changes is handled by the IResourceListener.
            if (AdtConstants.EXT_CLASS.equals(file.getFileExtension())) {
if (file.getName().matches("R[\\$\\.](.*)")) {
// this is a R change!
if (changeFlags == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 976dfaf..6ce4d6b 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.legacy.LegacyCallback;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectClassLoader;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
//Synthetic comment -- @@ -199,7 +199,7 @@
ManifestData manifestData = AndroidManifestHelper.parseForData(mProject);
if (manifestData != null) {
String javaPackage = manifestData.getPackage();
                mNamespace = String.format(AdtConstants.NS_CUSTOM_RESOURCES, javaPackage);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/WidgetPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/WidgetPullParser.java
//Synthetic comment -- index 153a2d2..07e6a91 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -43,7 +43,7 @@
public WidgetPullParser(ViewElementDescriptor descriptor) {
mDescriptor = descriptor;

        String[] segments = mDescriptor.getFullClassName().split(AdtConstants.RE_DOT);
mAttributes[0][1] = segments[segments.length-1];
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index aef4bc4..a1a653f 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import static com.android.ide.common.layout.LayoutConstants.ANDROID_STRING_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.SCROLL_VIEW;
import static com.android.ide.common.layout.LayoutConstants.STRING_PREFIX;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.StaticRenderSession;
//Synthetic comment -- @@ -35,7 +35,7 @@
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.IPageImageProvider;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.ContextPullParser;
//Synthetic comment -- @@ -1821,7 +1821,7 @@
if (severity == IMarker.SEVERITY_ERROR) {
hasJavaErrors = true;
}
                        } else if (markerType.equals(AdtConstants.MARKER_AAPT_COMPILE)) {
int severity = marker.getAttribute(IMarker.SEVERITY, -1);
if (severity == IMarker.SEVERITY_ERROR) {
hasAaptErrors = true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index bec5945..080afce 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_LAYOUTS;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.sdklib.SdkConstants.FD_LAYOUT;

import static org.eclipse.core.resources.IResourceDelta.ADDED;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java
//Synthetic comment -- index 3fcae86..58ae8ff 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.sdklib.SdkConstants.FD_ANIMATOR;
import static com.android.sdklib.SdkConstants.FD_RESOURCES;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 1b48c7c..e8b4fba 100644

//Synthetic comment -- @@ -17,8 +17,8 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutRefactoring.java
//Synthetic comment -- index c49e13d..a83d41f 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
import static com.android.ide.common.layout.LayoutConstants.TABLE_ROW;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FALSE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;

import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoring.java
//Synthetic comment -- index 21779dc..fcd6ff6 100644

//Synthetic comment -- @@ -24,15 +24,15 @@
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor.XMLNS;
import static com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor.XMLNS_COLON;
import static com.android.resources.ResourceType.LAYOUT;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.CanvasViewInfo;
//Synthetic comment -- @@ -260,7 +260,7 @@
IPath parentPath = parent.getProjectRelativePath();
final IFile file = project.getFile(new Path(parentPath + WS_SEP + newFileName));
TextFileChange addFile = new TextFileChange("Create new separate layout", file);
        addFile.setTextType(AdtConstants.EXT_XML);
changes.add(addFile);
addFile.setEdit(new InsertEdit(0, sb.toString()));









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/VisualRefactoringAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/VisualRefactoringAction.java
//Synthetic comment -- index e175da2..627f1b1 100644

//Synthetic comment -- @@ -16,7 +16,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.CanvasViewInfo;

//Synthetic comment -- @@ -136,7 +136,7 @@
if (file.exists()) {
IProject proj = file.getProject();
try {
                        if (proj != null && proj.hasNature(AdtConstants.NATURE_DEFAULT)) {
return file;
}
} catch (CoreException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInRefactoring.java
//Synthetic comment -- index 0aea363..4a73361 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;

import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java
//Synthetic comment -- index fb742a7..ac9d83d 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.manifest;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
//Synthetic comment -- @@ -56,7 +56,7 @@
*/
public final class ManifestEditor extends AndroidXmlEditor {

    public static final String ID = AdtConstants.EDITORS_NAMESPACE + ".manifest.ManifestEditor"; //$NON-NLS-1$

private final static String EMPTY = ""; //$NON-NLS-1$

//Synthetic comment -- @@ -256,7 +256,7 @@
try {
// get the markers for the file
IMarker[] markers = inputFile.findMarkers(
                    AdtConstants.MARKER_ANDROID, true, IResource.DEPTH_ZERO);

AndroidManifestDescriptors desc = getManifestDescriptors();
if (desc != null) {
//Synthetic comment -- @@ -304,12 +304,12 @@
*/
private void processMarker(IMarker marker, List<UiElementNode> nodeList, int kind) {
// get the data from the marker
        String nodeType = marker.getAttribute(AdtConstants.MARKER_ATTR_TYPE, EMPTY);
if (nodeType == EMPTY) {
return;
}

        String className = marker.getAttribute(AdtConstants.MARKER_ATTR_CLASS, EMPTY);
if (className == EMPTY) {
return;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiClassAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiClassAttributeNode.java
//Synthetic comment -- index 348e0a3..7b1f4fa 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.manifest.model;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
//Synthetic comment -- @@ -394,7 +394,7 @@
if (className.startsWith(".")) { //$NON-NLS-1$
fullClassName = packageName + className;
} else {
                String[] segments = className.split(AdtConstants.RE_DOT);
if (segments.length == 1) {
fullClassName = packageName + "." + className; //$NON-NLS-1$
}
//Synthetic comment -- @@ -503,7 +503,7 @@
// look for how many segments we have left.
// if one, just write it that way.
// if more than one, write it with a leading dot.
            String[] packages = name.split(AdtConstants.RE_DOT);
if (packages.length == 1) {
text.setText(name);
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/MenuEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/MenuEditor.java
//Synthetic comment -- index 4ddb1e9..a06d7be 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.menu;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor.Mandatory;
//Synthetic comment -- @@ -42,7 +42,7 @@
*/
public class MenuEditor extends AndroidXmlEditor {

    public static final String ID = AdtConstants.EDITORS_NAMESPACE + ".menu.MenuEditor"; //$NON-NLS-1$

/** Root node of the UI element hierarchy */
private UiElementNode mUiRootNode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/ResourcesEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/ResourcesEditor.java
//Synthetic comment -- index 79d11ed..53ea513 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.resources;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
//Synthetic comment -- @@ -42,7 +42,7 @@
*/
public class ResourcesEditor extends AndroidXmlEditor {

    public static final String ID = AdtConstants.EDITORS_NAMESPACE + ".resources.ResourcesEditor"; //$NON-NLS-1$

/** Root node of the UI element hierarchy */
private UiElementNode mUiResourcesNode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 469d184..1456b47 100644

//Synthetic comment -- @@ -21,10 +21,10 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_ON_CLICK;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VIEW;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.FN_RESOURCE_BASE;
import static com.android.ide.eclipse.adt.AdtConstants.FN_RESOURCE_CLASS;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.NAME_ATTR;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.ROOT_ELEMENT;
import static com.android.sdklib.SdkConstants.FD_DOCS;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/XmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/XmlEditor.java
//Synthetic comment -- index 177cb14..6ec52f1 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.xml;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.FirstElementParser;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
//Synthetic comment -- @@ -41,7 +41,7 @@
*/
public class XmlEditor extends AndroidXmlEditor {

    public static final String ID = AdtConstants.EDITORS_NAMESPACE + ".xml.XmlEditor"; //$NON-NLS-1$

/** Root node of the UI element hierarchy */
private UiDocumentNode mUiRootNode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/JUnitLaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/JUnitLaunchConfigDelegate.java
//Synthetic comment -- index cbfcb24..442d356 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.launch;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.runtime.CoreException;
//Synthetic comment -- @@ -149,7 +149,7 @@
if (bundle == null) {
throw new IOException("Cannot find org.junit bundle");
}
        URL jarUrl = bundle.getEntry(AdtConstants.WS_SEP + JUNIT_JAR);
return FileLocator.resolve(jarUrl).getFile();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigDelegate.java
//Synthetic comment -- index e747e7e..6364dcd 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration.TargetMode;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
//Synthetic comment -- @@ -380,7 +380,7 @@
return false;
}

        if (project.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
String msg = String.format("%1$s is not an Android project!", project.getName());
AdtPlugin.displayError("Android Launch", msg);
return false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java
//Synthetic comment -- index 1f14605..2384f3b 100755

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.launch.junit;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunch;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration;
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchController;
//Synthetic comment -- @@ -188,7 +188,7 @@
LaunchMessages.AndroidJUnitDelegate_NoRunnerConsoleMsg_4s,
project.getName(),
SdkConstants.CLASS_INSTRUMENTATION_RUNNER,
                    AdtConstants.LIBRARY_TEST_RUNNER,
SdkConstants.FN_ANDROID_MANIFEST_XML));
return null;
} catch (CoreException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigurationTab.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigurationTab.java
//Synthetic comment -- index e5957d7..51db066 100644

//Synthetic comment -- @@ -16,7 +16,7 @@
package com.android.ide.eclipse.adt.internal.launch.junit;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.launch.LaunchMessages;
import com.android.ide.eclipse.adt.internal.launch.MainLaunchConfigTab;
//Synthetic comment -- @@ -659,7 +659,7 @@
validateJavaProject(javaProject);

try {
            if (!project.hasNature(AdtConstants.NATURE_DEFAULT)) {
setErrorMessage(
LaunchMessages.NonAndroidProjectError);
return;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/InstrumentationRunnerValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/InstrumentationRunnerValidator.java
//Synthetic comment -- index bb75bcc..3234b38 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.launch.junit;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.launch.LaunchMessages;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
//Synthetic comment -- @@ -88,7 +88,7 @@
*/
private boolean hasTestRunnerLibrary(ManifestData manifestData) {
for (UsesLibrary lib : manifestData.getUsesLibraries()) {
           if (AdtConstants.LIBRARY_TEST_RUNNER.equals(lib.getName())) {
return true;
}
}
//Synthetic comment -- @@ -130,7 +130,7 @@
String validateInstrumentationRunner(String instrumentation) {
if (!mHasRunnerLibrary) {
return String.format(LaunchMessages.InstrValidator_NoTestLibMsg_s,
                    AdtConstants.LIBRARY_TEST_RUNNER);
}
// check if this instrumentation is the standard test runner
if (!instrumentation.equals(SdkConstants.CLASS_INSTRUMENTATION_RUNNER)) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidClasspathContainerInitializer.java
//Synthetic comment -- index c5f1fff..9ae60c0 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.AndroidVersion;
//Synthetic comment -- @@ -295,7 +295,7 @@
}

try {
                    BaseProjectHelper.markProject(iProject, AdtConstants.MARKER_TARGET,
markerMessage, IMarker.SEVERITY_ERROR, IMarker.PRIORITY_HIGH);
} catch (CoreException e) {
// In some cases, the workspace may be locked for modification when we
//Synthetic comment -- @@ -307,7 +307,7 @@
protected IStatus run(IProgressMonitor monitor) {
try {
BaseProjectHelper.markProject(iProject,
                                        AdtConstants.MARKER_TARGET,
fmessage, IMarker.SEVERITY_ERROR,
IMarker.PRIORITY_HIGH);
} catch (CoreException e2) {
//Synthetic comment -- @@ -326,7 +326,7 @@
// no error, remove potential MARKER_TARGETs.
try {
if (iProject.exists()) {
                        iProject.deleteMarkers(AdtConstants.MARKER_TARGET, true,
IResource.DEPTH_INFINITE);
}
} catch (CoreException ce) {
//Synthetic comment -- @@ -336,7 +336,7 @@
@Override
protected IStatus run(IProgressMonitor monitor) {
try {
                                iProject.deleteMarkers(AdtConstants.MARKER_TARGET, true,
IResource.DEPTH_INFINITE);
} catch (CoreException e2) {
return e2.getStatus();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidNature.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidNature.java
//Synthetic comment -- index 5e05c99..dddc7a0 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.builders.PostCompilerBuilder;
import com.android.ide.eclipse.adt.internal.build.builders.PreCompilerBuilder;
import com.android.ide.eclipse.adt.internal.build.builders.ResourceManagerBuilder;
//Synthetic comment -- @@ -127,7 +127,7 @@
// Adding the java nature after the android one, would place the java builder before the
// android builders.
addNatureToProjectDescription(project, JavaCore.NATURE_ID, monitor);
        addNatureToProjectDescription(project, AdtConstants.NATURE_DEFAULT, monitor);
}

/**
//Synthetic comment -- @@ -151,7 +151,7 @@
String[] newNatures = new String[natures.length + 1];

// Android natures always come first.
            if (natureId.equals(AdtConstants.NATURE_DEFAULT)) {
System.arraycopy(natures, 0, newNatures, 1, natures.length);
newNatures[0] = natureId;
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseProjectHelper.java
//Synthetic comment -- index 6485f96..7d00830 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -402,7 +402,7 @@

// check if it's an android project based on its nature
try {
                if (project.hasNature(AdtConstants.NATURE_DEFAULT)) {
if (filter == null || filter.accept(project)) {
androidProjectList.add(javaProject);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index 3b0622a..3d0e088 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.DexException;
//Synthetic comment -- @@ -124,7 +124,7 @@

// tmp file for the packaged resource file. To not disturb the incremental builders
// output, all intermediary files are created in tmp files.
            File resourceFile = File.createTempFile(TEMP_PREFIX, AdtConstants.DOT_RES);
resourceFile.deleteOnExit();

// package the resources.
//Synthetic comment -- @@ -139,7 +139,7 @@
// Step 2. Convert the byte code to Dalvik bytecode

// tmp file for the packaged resource file.
            File dexFile = File.createTempFile(TEMP_PREFIX, AdtConstants.DOT_DEX);
dexFile.deleteOnExit();

ProjectState state = Sdk.getProjectState(project);
//Synthetic comment -- @@ -163,7 +163,7 @@
String[] projectOutputs = helper.getProjectOutputs();

// create a jar from the output of these projects
                File inputJar = File.createTempFile(TEMP_PREFIX, AdtConstants.DOT_JAR);
inputJar.deleteOnExit();

JarOutputStream jos = new JarOutputStream(new FileOutputStream(inputJar));
//Synthetic comment -- @@ -180,7 +180,7 @@
null /*resourceMarker*/);

// destination file for proguard
                File obfuscatedJar = File.createTempFile(TEMP_PREFIX, AdtConstants.DOT_JAR);
obfuscatedJar.deleteOnExit();

// run proguard
//Synthetic comment -- @@ -274,7 +274,7 @@
IPath binLocation = outputFolder.getLocation();

// make the full path to the package
            String fileName = project.getName() + AdtConstants.DOT_ANDROID_PACKAGE;

File file = new File(binLocation.toOSString() + File.separator + fileName);

//Synthetic comment -- @@ -348,7 +348,7 @@
} else if (file.isFile()) {
// check the extension
String name = file.getName();
            if (name.toLowerCase().endsWith(AdtConstants.DOT_CLASS) == false) {
return;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/FolderDecorator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/FolderDecorator.java
//Synthetic comment -- index 7ca244c..394338b 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -54,7 +54,7 @@
}

try {
                if (project.hasNature(AdtConstants.NATURE_DEFAULT)) {
// check the folder is directly under the project.
if (folder.getParent().getType() == IResource.PROJECT) {
String name = folder.getName();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java
//Synthetic comment -- index 91e2380..ca988a2 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.ManifestData;

//Synthetic comment -- @@ -116,11 +116,11 @@
public static String getJavaDocPath(String javaDocOSLocation) {
// first thing we do is convert the \ into /
String javaDoc = javaDocOSLocation.replaceAll("\\\\", //$NON-NLS-1$
                AdtConstants.WS_SEP);

// then we add file: at the beginning for unix path, and file:/ for non
// unix path
        if (javaDoc.startsWith(AdtConstants.WS_SEP)) {
return "file:" + javaDoc; //$NON-NLS-1$
}

//Synthetic comment -- @@ -365,11 +365,11 @@
if (checkCompilerCompliance(javaProject) != COMPILER_COMPLIANCE_OK) {
// setup the preferred compiler compliance level.
javaProject.setOption(JavaCore.COMPILER_COMPLIANCE,
                    AdtConstants.COMPILER_COMPLIANCE_PREFERRED);
javaProject.setOption(JavaCore.COMPILER_SOURCE,
                    AdtConstants.COMPILER_COMPLIANCE_PREFERRED);
javaProject.setOption(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
                    AdtConstants.COMPILER_COMPLIANCE_PREFERRED);

// clean the project to make sure we recompile
try {
//Synthetic comment -- @@ -402,7 +402,7 @@
for (IProject p : projects) {
if (p.isOpen()) {
try {
                    if (p.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
// ignore non android projects
continue;
}
//Synthetic comment -- @@ -451,16 +451,16 @@
String[] natures = description.getNatureIds();

// if the android nature is not the first one, we reorder them
        if (AdtConstants.NATURE_DEFAULT.equals(natures[0]) == false) {
// look for the index
for (int i = 0 ; i < natures.length ; i++) {
                if (AdtConstants.NATURE_DEFAULT.equals(natures[i])) {
// if we try to just reorder the array in one pass, this doesn't do
// anything. I guess JDT check that we are actually adding/removing nature.
// So, first we'll remove the android nature, and then add it back.

// remove the android nature
                    removeNature(project, AdtConstants.NATURE_DEFAULT);

// now add it back at the first index.
description = project.getDescription();
//Synthetic comment -- @@ -469,7 +469,7 @@
String[] newNatures = new String[natures.length + 1];

// first one is android
                    newNatures[0] = AdtConstants.NATURE_DEFAULT;

// next the rest that was before the android nature
System.arraycopy(natures, 0, newNatures, 1, natures.length);
//Synthetic comment -- @@ -675,7 +675,7 @@
* @return true if the option value is supproted.
*/
private static boolean checkCompliance(String optionValue) {
        for (String s : AdtConstants.COMPILER_COMPLIANCE) {
if (s != null && s.equals(optionValue)) {
return true;
}
//Synthetic comment -- @@ -691,10 +691,10 @@
*/
public static String getApkFilename(IProject project, String config) {
if (config != null) {
            return project.getName() + "-" + config + AdtConstants.DOT_ANDROID_PACKAGE; //$NON-NLS-1$
}

        return project.getName() + AdtConstants.DOT_ANDROID_PACKAGE;
}

/**
//Synthetic comment -- @@ -718,7 +718,7 @@

//Verify that the project has also the Android Nature
try {
                if (!androidJavaProject.getProject().hasNature(AdtConstants.NATURE_DEFAULT)) {
continue;
}
} catch (CoreException e) {
//Synthetic comment -- @@ -750,7 +750,7 @@


// get the package path
        String packageName = project.getName() + AdtConstants.DOT_ANDROID_PACKAGE;
IResource r = outputLocation.findMember(packageName);

// check the package is present
//Synthetic comment -- @@ -772,7 +772,7 @@
*         is missing.
*/
public static IFile getManifest(IProject project) {
        IResource r = project.findMember(AdtConstants.WS_SEP
+ SdkConstants.FN_ANDROID_MANIFEST_XML);

if (r == null || r.exists() == false || (r instanceof IFile) == false) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/XmlErrorHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/XmlErrorHandler.java
//Synthetic comment -- index d5f6f15..7cd0161 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.sdklib.xml.AndroidManifestParser.ManifestErrorHandler;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -97,7 +97,7 @@
public void warning(SAXParseException exception) throws SAXException {
if (mFile != null) {
BaseProjectHelper.markResource(mFile,
                    AdtConstants.MARKER_XML,
exception.getMessage(),
exception.getLineNumber(),
IMarker.SEVERITY_WARNING);
//Synthetic comment -- @@ -125,7 +125,7 @@

if (mFile != null) {
BaseProjectHelper.markResource(mFile,
                    AdtConstants.MARKER_XML,
message,
lineNumber,
IMarker.SEVERITY_ERROR);
//Synthetic comment -- @@ -156,14 +156,14 @@

// mark the file
IMarker marker = BaseProjectHelper.markResource(getFile(),
                    AdtConstants.MARKER_ANDROID, result, line, IMarker.SEVERITY_ERROR);

// add custom attributes to be used by the manifest editor.
if (marker != null) {
try {
                    marker.setAttribute(AdtConstants.MARKER_ATTR_TYPE,
                            AdtConstants.MARKER_ATTR_TYPE_ACTIVITY);
                    marker.setAttribute(AdtConstants.MARKER_ATTR_CLASS, className);
} catch (CoreException e) {
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidPackageRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidPackageRenameParticipant.java
//Synthetic comment -- index 43cb09d..2fbb4a3 100755

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.refactoring.changes.AndroidLayoutChange;
import com.android.ide.eclipse.adt.internal.refactoring.changes.AndroidLayoutChangeDescription;
//Synthetic comment -- @@ -203,7 +203,7 @@
IJavaProject javaProject = (IJavaProject) mPackageFragment
.getAncestor(IJavaElement.JAVA_PROJECT);
IProject project = javaProject.getProject();
                IResource manifestResource = project.findMember(AdtConstants.WS_SEP
+ SdkConstants.FN_ANDROID_MANIFEST_XML);

if (manifestResource == null || !manifestResource.exists()








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeMoveParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeMoveParticipant.java
//Synthetic comment -- index e7c3e1a..48da579 100755

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.refactoring.changes.AndroidLayoutChange;
import com.android.ide.eclipse.adt.internal.refactoring.changes.AndroidLayoutChangeDescription;
//Synthetic comment -- @@ -174,7 +174,7 @@
IType type = (IType) element;
IJavaProject javaProject = (IJavaProject) type.getAncestor(IJavaElement.JAVA_PROJECT);
IProject project = javaProject.getProject();
            IResource manifestResource = project.findMember(AdtConstants.WS_SEP
+ SdkConstants.FN_ANDROID_MANIFEST_XML);

if (manifestResource == null || !manifestResource.exists()








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeRenameParticipant.java
//Synthetic comment -- index dc393e3..53c941a 100755

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.refactoring.changes.AndroidLayoutChange;
import com.android.ide.eclipse.adt.internal.refactoring.changes.AndroidLayoutChangeDescription;
//Synthetic comment -- @@ -129,7 +129,7 @@
IType type = (IType) element;
IJavaProject javaProject = (IJavaProject) type.getAncestor(IJavaElement.JAVA_PROJECT);
IProject project = javaProject.getProject();
            IResource manifestResource = project.findMember(AdtConstants.WS_SEP
+ SdkConstants.FN_ANDROID_MANIFEST_XML);

if (manifestResource == null || !manifestResource.exists()








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringAction.java
//Synthetic comment -- index ffa4089..e005f1c 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactorings.extractstring;

import com.android.ide.eclipse.adt.AdtConstants;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -165,7 +165,7 @@
if (file.exists()) {
IProject proj = file.getProject();
try {
                        if (proj != null && proj.hasNature(AdtConstants.NATURE_DEFAULT)) {
return file;
}
} catch (CoreException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java
//Synthetic comment -- index 8dab07e..1c5c56c 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.refactorings.extractstring;


import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector;
//Synthetic comment -- @@ -83,10 +83,10 @@
"/res/[a-z][a-zA-Z0-9_-]+/[^.]+\\.xml");  //$NON-NLS-1$
/** Absolute destination folder root, e.g. "/res/" */
private static final String RES_FOLDER_ABS =
        AdtConstants.WS_RESOURCES + AdtConstants.WS_SEP;
/** Relative destination folder root, e.g. "res/" */
private static final String RES_FOLDER_REL =
        SdkConstants.FD_RESOURCES + AdtConstants.WS_SEP;

private static final String DEFAULT_RES_FILE_PATH = "/res/values/strings.xml";  //$NON-NLS-1$

//Synthetic comment -- @@ -470,7 +470,7 @@
mConfigSelector.getConfiguration(mTempConfig);
StringBuffer sb = new StringBuffer(RES_FOLDER_ABS);
sb.append(mTempConfig.getFolderName(ResourceFolderType.VALUES));
            sb.append(AdtConstants.WS_SEP);

String newPath = sb.toString();

//Synthetic comment -- @@ -571,7 +571,7 @@
if (wsFolderPath.startsWith(RES_FOLDER_ABS)) {
wsFolderPath = wsFolderPath.substring(RES_FOLDER_ABS.length());

                int pos = wsFolderPath.indexOf(AdtConstants.WS_SEP_CHAR);
if (pos >= 0) {
wsFolderPath = wsFolderPath.substring(0, pos);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index 1a41caf..48a0544 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import static com.android.ide.common.layout.LayoutConstants.STRING_PREFIX;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ReferenceAttributeDescriptor;
//Synthetic comment -- @@ -424,7 +424,7 @@
}

// Check this a Layout XML file and get the selection and its context.
            if (mFile != null && AdtConstants.EXT_XML.equals(mFile.getFileExtension())) {

// Currently we only support Android resource XML files, so they must have a path
// similar to
//Synthetic comment -- @@ -1016,7 +1016,7 @@
// Add all /res folders (technically we don't need to process /res/values
// XML files that contain resources/string elements, but it's easier to
// not filter them out.)
                        IFolder f = mProject.getFolder(AdtConstants.WS_RESOURCES);
if (f.exists()) {
try {
mFolders.addAll(
//Synthetic comment -- @@ -1055,7 +1055,7 @@
if (res.exists() && !res.isDerived() && res instanceof IFile) {
IFile file = (IFile) res;
// Must have an XML extension
                                if (AdtConstants.EXT_XML.equals(file.getFileExtension())) {
IPath p = file.getFullPath();
// And not be either paths we want to filter out
if ((mFilterPath1 != null && mFilterPath1.equals(p)) ||
//Synthetic comment -- @@ -1101,7 +1101,7 @@
SubMonitor monitor) {

TextFileChange xmlChange = new TextFileChange(getName(), targetXml);
        xmlChange.setTextType(AdtConstants.EXT_XML);

String error = "";                  //$NON-NLS-1$
TextEdit edit = null;
//Synthetic comment -- @@ -1478,7 +1478,7 @@
HashSet<IFile> files = new HashSet<IFile>();
files.add(sourceFile);

        if (allConfigurations && AdtConstants.EXT_XML.equals(sourceFile.getFileExtension())) {
IPath path = sourceFile.getFullPath();
if (path.segmentCount() == 4 && path.segment(1).equals(SdkConstants.FD_RESOURCES)) {
IProject project = sourceFile.getProject();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java
//Synthetic comment -- index 9097f97..3df35bc 100755

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.refactorings.renamepackage;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.AndroidManifest;
//Synthetic comment -- @@ -153,7 +153,7 @@

ImportRewrite irw = ImportRewrite.create(cu, true);
irw.addImport(mNewPackageName.getFullyQualifiedName() + '.'
                    + AdtConstants.FN_RESOURCE_BASE);

try {
rewrittenImports.addChild( irw.rewriteImports(null) );
//Synthetic comment -- @@ -210,14 +210,14 @@
}

TextFileChange xmlChange = new TextFileChange("XML resource file edit", file);
        xmlChange.setTextType(AdtConstants.EXT_XML);

MultiTextEdit multiEdit = new MultiTextEdit();
ArrayList<TextEditGroup> editGroups = new ArrayList<TextEditGroup>();

        final String oldAppNamespaceString = String.format(AdtConstants.NS_CUSTOM_RESOURCES,
mOldPackageName.getFullyQualifiedName());
        final String newAppNamespaceString = String.format(AdtConstants.NS_CUSTOM_RESOURCES,
mNewPackageName.getFullyQualifiedName());

// Prepare the change set
//Synthetic comment -- @@ -301,7 +301,7 @@
}

TextFileChange xmlChange = new TextFileChange("Make Manifest edits", file);
        xmlChange.setTextType(AdtConstants.EXT_XML);

MultiTextEdit multiEdit = new MultiTextEdit();
ArrayList<TextEditGroup> editGroups = new ArrayList<TextEditGroup>();
//Synthetic comment -- @@ -417,7 +417,7 @@
public boolean visit(IResource resource) throws CoreException {
if (resource instanceof IFile) {
IFile file = (IFile) resource;
                if (AdtConstants.EXT_JAVA.equals(file.getFileExtension())) {

ICompilationUnit icu = JavaCore.createCompilationUnitFrom(file);

//Synthetic comment -- @@ -430,7 +430,7 @@
edit.addChild(text_edit);

TextFileChange text_file_change = new TextFileChange(file.getName(), file);
                        text_file_change.setTextType(AdtConstants.EXT_JAVA);
text_file_change.setEdit(edit);
mChanges.add(text_file_change);
}
//Synthetic comment -- @@ -438,7 +438,7 @@
// XXX Partially taken from ExtractStringRefactoring.java
// Check this a Layout XML file and get the selection and
// its context.
                } else if (AdtConstants.EXT_XML.equals(file.getFileExtension())) {

if (SdkConstants.FN_ANDROID_MANIFEST_XML.equals(file.getName())) {

//Synthetic comment -- @@ -510,7 +510,7 @@
QualifiedName qualifiedImportName = (QualifiedName) importName;

if (qualifiedImportName.getName().getIdentifier()
                        .equals(AdtConstants.FN_RESOURCE_BASE)) {
mRewriter.replace(qualifiedImportName.getQualifier(), mNewPackageName,
null);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index 2ef0beb..fd8ad9e 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.eclipse.adt.internal.resources;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.resources.manager.FolderTypeRelationship;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
//Synthetic comment -- @@ -70,7 +70,7 @@
newText = newText.substring(0, newText.length() - DOT_XML.length());
}

            if (newText.indexOf('.') != -1 && !newText.endsWith(AdtConstants.DOT_XML)) {
return String.format("The filename must end with %1$s.", DOT_XML);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/CompiledResourcesMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/CompiledResourcesMonitor.java
//Synthetic comment -- index 295fd4c..467fad0 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
//Synthetic comment -- @@ -77,7 +77,7 @@
* @see IFileListener#fileChanged
*/
public void fileChanged(IFile file, IMarkerDelta[] markerDeltas, int kind) {
        if (file.getName().equals(AdtConstants.FN_COMPILED_RESOURCE_CLASS)) {
loadAndParseRClass(file.getProject());
}
}
//Synthetic comment -- @@ -115,7 +115,7 @@
public void projectOpenedWithWorkspace(IProject project) {
try {
// check this is an android project
            if (project.hasNature(AdtConstants.NATURE_DEFAULT)) {
loadAndParseRClass(project);
}
} catch (CoreException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java
//Synthetic comment -- index 0deb89c..dc543b8 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;

//Synthetic comment -- @@ -262,7 +262,7 @@
IPath path = e.getPath();

// check the name ends with .jar
                    if (AdtConstants.EXT_JAR.equalsIgnoreCase(path.getFileExtension())) {
boolean local = false;
IResource resource = wsRoot.findMember(path);
if (resource != null && resource.exists() &&








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index 8037d7a..9293f33 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
//Synthetic comment -- @@ -167,7 +167,7 @@
final IProject project = folder.getProject();

try {
                if (project.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
return;
}
} catch (CoreException e) {
//Synthetic comment -- @@ -256,7 +256,7 @@
final IProject project = file.getProject();

try {
                if (project.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
return;
}
} catch (CoreException e) {
//Synthetic comment -- @@ -481,7 +481,7 @@
private void createProject(IProject project) {
if (project.isOpen()) {
try {
                if (project.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
return;
}
} catch (CoreException e1) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidJarLoader.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidJarLoader.java
//Synthetic comment -- index 3bb125a..807acfc 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.AdtConstants;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
//Synthetic comment -- @@ -155,7 +155,7 @@
// get the name of the entry.
String entryPath = entry.getName();

            if (!entryPath.endsWith(AdtConstants.DOT_CLASS)) {
// only accept class files
continue;
}
//Synthetic comment -- @@ -219,7 +219,7 @@
while ((entry = zis.getNextEntry()) != null) {
// get the name of the entry and convert to a class binary name
String entryPath = entry.getName();
            if (!entryPath.endsWith(AdtConstants.DOT_CLASS)) {
// only accept class files
continue;
}
//Synthetic comment -- @@ -341,7 +341,7 @@

// The name is a binary name. Something like "android.R", or "android.R$id".
// Make a path out of it.
        String entryName = className.replaceAll("\\.", "/") + AdtConstants.DOT_CLASS; //$NON-NLS-1$ //$NON-NLS-2$

// create streams to read the intermediary archive
FileInputStream fis = new FileInputStream(mOsFrameworkLocation);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index aa22ff4..8d3ee7b 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.DexWrapper;
import com.android.ide.eclipse.adt.internal.project.AndroidClasspathContainerInitializer;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
//Synthetic comment -- @@ -743,7 +743,7 @@

private void onProjectRemoved(IProject project, boolean deleted) {
try {
                if (project.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
return;
}
} catch (CoreException e) {
//Synthetic comment -- @@ -830,7 +830,7 @@

private void onProjectOpened(final IProject openedProject) {
try {
                if (openedProject.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
return;
}
} catch (CoreException e) {
//Synthetic comment -- @@ -897,7 +897,7 @@

public void projectRenamed(IProject project, IPath from) {
try {
                if (project.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
return;
}
} catch (CoreException e) {
//Synthetic comment -- @@ -964,7 +964,7 @@
// the target.
IProject iProject = file.getProject();

                    if (iProject.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
return;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index e27ea5b..2c0ac44 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.ui;

import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.NAME_ATTR;
import static com.android.sdklib.SdkConstants.FD_RESOURCES;
import static com.android.sdklib.SdkConstants.FD_VALUES;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceExplorerView.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceExplorerView.java
//Synthetic comment -- index 45880ca..081b6b6 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
//Synthetic comment -- @@ -76,9 +76,9 @@
// Note: keep using the obsolete AndroidConstants.EDITORS_NAMESPACE (which used
// to be the Editors Plugin ID) to keep existing preferences functional.
private final static String PREFS_COLUMN_RES =
        AdtConstants.EDITORS_NAMESPACE + "ResourceExplorer.Col1"; //$NON-NLS-1$
private final static String PREFS_COLUMN_2 =
        AdtConstants.EDITORS_NAMESPACE + "ResourceExplorer.Col2"; //$NON-NLS-1$

private Tree mTree;
private TreeViewer mTreeViewer;
//Synthetic comment -- @@ -240,7 +240,7 @@
try {
// if it's an android project, then we get its resources, and feed them
// to the tree viewer.
            if (project.hasNature(AdtConstants.NATURE_DEFAULT)) {
if (mCurrentProject != project) {
ProjectResources projRes = ResourceManager.getInstance().getProjectResources(
project);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ProjectCheckPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ProjectCheckPage.java
//Synthetic comment -- index 052fc50..3326c6f 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.wizards.export;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
//Synthetic comment -- @@ -160,7 +160,7 @@
mHasMessage = true;
} else {
try {
                if (project.hasNature(AdtConstants.NATURE_DEFAULT) == false) {
addError(mErrorComposite, "Project is not an Android project.");
} else {
// check for errors
//Synthetic comment -- @@ -173,7 +173,7 @@
if (outputIFolder != null) {
String outputOsPath =  outputIFolder.getLocation().toOSString();
String apkFilePath =  outputOsPath + File.separator + project.getName() +
                                AdtConstants.DOT_ANDROID_PACKAGE;

File f = new File(apkFilePath);
if (f.isFile() == false) {
//Synthetic comment -- @@ -181,7 +181,7 @@
String.format("%1$s/%2$s/%1$s%3$s does not exists!",
project.getName(),
outputIFolder.getName(),
                                            AdtConstants.DOT_ANDROID_PACKAGE));
}
} else {
addError(mErrorComposite,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index 027b94e..195c41f 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
package com.android.ide.eclipse.adt.internal.wizards.newproject;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
//Synthetic comment -- @@ -1063,7 +1063,7 @@
// name as a default. If the activity name has dots, it's a part of a
// package specification and only the last identifier must be used.
if (activityName.indexOf('.') != -1) {
                String[] ids = activityName.split(AdtConstants.RE_DOT);
activityName = ids[ids.length - 1];
}
if (mProjectNameField.getText().length() == 0 || !mProjectNameModifiedByUser) {
//Synthetic comment -- @@ -1611,7 +1611,7 @@
} else if (osTarget.indexOf('.') == 0) {
osTarget = mInfo.getPackageName() + osTarget;
}
        osTarget = osTarget.replace('.', File.separatorChar) + AdtConstants.DOT_JAVA;

String projectPath = getProjectLocation();
File projectDir = new File(projectPath);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java
//Synthetic comment -- index 19118dd..ebccf25 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidNature;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
//Synthetic comment -- @@ -134,25 +134,25 @@
private static final String PH_TEST_INSTRUMENTATION = "TEST-INSTRUMENTATION";   //$NON-NLS-1$

private static final String BIN_DIRECTORY =
        SdkConstants.FD_OUTPUT + AdtConstants.WS_SEP;
private static final String RES_DIRECTORY =
        SdkConstants.FD_RESOURCES + AdtConstants.WS_SEP;
private static final String ASSETS_DIRECTORY =
        SdkConstants.FD_ASSETS + AdtConstants.WS_SEP;
private static final String DRAWABLE_DIRECTORY =
        SdkConstants.FD_DRAWABLE + AdtConstants.WS_SEP;
private static final String DRAWABLE_HDPI_DIRECTORY =
        SdkConstants.FD_DRAWABLE + "-" + Density.HIGH.getResourceValue() + AdtConstants.WS_SEP;   //$NON-NLS-1$
private static final String DRAWABLE_MDPI_DIRECTORY =
        SdkConstants.FD_DRAWABLE + "-" + Density.MEDIUM.getResourceValue() + AdtConstants.WS_SEP; //$NON-NLS-1$
private static final String DRAWABLE_LDPI_DIRECTORY =
        SdkConstants.FD_DRAWABLE + "-" + Density.LOW.getResourceValue() + AdtConstants.WS_SEP;    //$NON-NLS-1$
private static final String LAYOUT_DIRECTORY =
        SdkConstants.FD_LAYOUT + AdtConstants.WS_SEP;
private static final String VALUES_DIRECTORY =
        SdkConstants.FD_VALUES + AdtConstants.WS_SEP;
private static final String GEN_SRC_DIRECTORY =
        SdkConstants.FD_GEN_SOURCES + AdtConstants.WS_SEP;

private static final String TEMPLATES_DIRECTORY = "templates/"; //$NON-NLS-1$
private static final String TEMPLATE_MANIFEST = TEMPLATES_DIRECTORY
//Synthetic comment -- @@ -661,12 +661,12 @@
AndroidNature.setupProjectNatures(project, monitor);

// Create folders in the project if they don't already exist
        addDefaultDirectories(project, AdtConstants.WS_ROOT, DEFAULT_DIRECTORIES, monitor);
String[] sourceFolders = new String[] {
(String) parameters.get(PARAM_SRC_FOLDER),
GEN_SRC_DIRECTORY
};
        addDefaultDirectories(project, AdtConstants.WS_ROOT, sourceFolders, monitor);

// Create the resource folders in the project if they don't already exist.
if (legacy) {
//Synthetic comment -- @@ -891,8 +891,8 @@
throws CoreException, IOException {

// create the IFile object and check if the file doesn't already exist.
        IFile file = project.getFile(RES_DIRECTORY + AdtConstants.WS_SEP
                                     + VALUES_DIRECTORY + AdtConstants.WS_SEP + STRINGS_FILE);
if (!file.exists()) {
// get the Strings.xml template
String stringDefinitionTemplate = AdtPlugin.readEmbeddedTextFile(TEMPLATE_STRINGS);
//Synthetic comment -- @@ -944,8 +944,8 @@
throws CoreException {
if (legacy) { // density support
// do medium density icon only, in the default drawable folder.
            IFile file = project.getFile(RES_DIRECTORY + AdtConstants.WS_SEP
                    + DRAWABLE_DIRECTORY + AdtConstants.WS_SEP + PROJECT_ICON);
if (!file.exists()) {
addFile(file, AdtPlugin.readEmbeddedFile(TEMPLATES_DIRECTORY + ICON_MDPI), monitor);
}
//Synthetic comment -- @@ -954,22 +954,22 @@
IFile file;

// high density
            file = project.getFile(RES_DIRECTORY + AdtConstants.WS_SEP
                    + DRAWABLE_HDPI_DIRECTORY + AdtConstants.WS_SEP + PROJECT_ICON);
if (!file.exists()) {
addFile(file, AdtPlugin.readEmbeddedFile(TEMPLATES_DIRECTORY + ICON_HDPI), monitor);
}

// medium density
            file = project.getFile(RES_DIRECTORY + AdtConstants.WS_SEP
                    + DRAWABLE_MDPI_DIRECTORY + AdtConstants.WS_SEP + PROJECT_ICON);
if (!file.exists()) {
addFile(file, AdtPlugin.readEmbeddedFile(TEMPLATES_DIRECTORY + ICON_MDPI), monitor);
}

// low density
            file = project.getFile(RES_DIRECTORY + AdtConstants.WS_SEP
                    + DRAWABLE_LDPI_DIRECTORY + AdtConstants.WS_SEP + PROJECT_ICON);
if (!file.exists()) {
addFile(file, AdtPlugin.readEmbeddedFile(TEMPLATES_DIRECTORY + ICON_LDPI), monitor);
}
//Synthetic comment -- @@ -1029,7 +1029,7 @@

// Resource class
if (lastDotIndex > 0) {
                    resourcePackageClass = packageName + "." + AdtConstants.FN_RESOURCE_BASE; //$NON-NLS-1$
}

// Package name
//Synthetic comment -- @@ -1051,7 +1051,7 @@
}
}

        String[] components = packageName.split(AdtConstants.RE_DOT);
for (String component : components) {
pkgFolder = pkgFolder.getFolder(component);
if (!pkgFolder.exists()) {
//Synthetic comment -- @@ -1062,7 +1062,7 @@

if (activityName != null) {
// create the main activity Java file
            String activityJava = activityName + AdtConstants.DOT_JAVA;
IFile file = pkgFolder.getFile(activityJava);
if (!file.exists()) {
copyFile(JAVA_ACTIVITY_TEMPLATE, file, java_activity_parameters, monitor);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index aea01b9..1cf41fd 100644

//Synthetic comment -- @@ -18,7 +18,7 @@
package com.android.ide.eclipse.adt.internal.wizards.newxmlfile;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
//Synthetic comment -- @@ -313,9 +313,9 @@
final static int NUM_COL = 4;

/** Absolute destination folder root, e.g. "/res/" */
    private static final String RES_FOLDER_ABS = AdtConstants.WS_RESOURCES + AdtConstants.WS_SEP;
/** Relative destination folder root, e.g. "res/" */
    private static final String RES_FOLDER_REL = SdkConstants.FD_RESOURCES + AdtConstants.WS_SEP;

private IProject mProject;
private Text mProjectTextField;
//Synthetic comment -- @@ -433,7 +433,7 @@
} else {
fileName = mFileNameTextField.getText().trim();
if (fileName.length() > 0 && fileName.indexOf('.') == -1) {
                fileName = fileName + AdtConstants.DOT_XML;
}
}

//Synthetic comment -- @@ -755,7 +755,7 @@

// Is this an Android project?
try {
                    if (project == null || !project.hasNature(AdtConstants.NATURE_DEFAULT)) {
continue;
}
} catch (CoreException e) {
//Synthetic comment -- @@ -1018,7 +1018,7 @@
if (wsFolderPath.startsWith(RES_FOLDER_ABS)) {
wsFolderPath = wsFolderPath.substring(RES_FOLDER_ABS.length());

            int pos = wsFolderPath.indexOf(AdtConstants.WS_SEP_CHAR);
if (pos >= 0) {
wsFolderPath = wsFolderPath.substring(0, pos);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/AdtTestData.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/AdtTestData.java
//Synthetic comment -- index 281170e..6ff02ea 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.tests;

import com.android.ide.eclipse.adt.AdtConstants;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
//Synthetic comment -- @@ -64,7 +64,7 @@
}
}

        if (mOsRootDataPath.equals(AdtConstants.WS_SEP)) {
sLogger.warning("Resource data not found using class loader!, Defaulting to no path");
}








