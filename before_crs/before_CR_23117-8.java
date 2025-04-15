/*Added Benchmarking code

Added benchmarks to the ADT plugin's builders.
Benchmarking is disabled by default and can be
Enabled by setting the BENCHMARK_FLAG  in
BuildHelper.java

Signed-off-by: Josiah Gaskin <josiahgaskin@google.com>

Change-Id:I2b855b627f8970f2a18a2ad1899c2b54b22f242a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 5aa9647..26355e5 100644

//Synthetic comment -- @@ -26,17 +26,17 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.IAndroidTarget.IOptionalLibrary;
import com.android.sdklib.build.ApkBuilder;
import com.android.sdklib.build.ApkCreationException;
import com.android.sdklib.build.DuplicateFileException;
import com.android.sdklib.build.SealedApkException;
import com.android.sdklib.build.ApkBuilder.JarStatus;
import com.android.sdklib.build.ApkBuilder.SigningInfo;
import com.android.sdklib.internal.build.DebugKeyProvider;
import com.android.sdklib.internal.build.SignedJarBuilder;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -102,6 +102,10 @@
private final boolean mVerbose;
private final boolean mDebugMode;

/**
* An object able to put a marker on a resource.
*/
//Synthetic comment -- @@ -127,7 +131,7 @@
}

/**
     * Packages the resources of the projet into a .ap_ file.
* @param manifestFile the manifest of the project.
* @param libProjects the list of library projects that this project depends on.
* @param resFilter an optional resource filter to be used with the -c option of aapt. If null
//Synthetic comment -- @@ -142,6 +146,15 @@
public void packageResources(IFile manifestFile, List<IProject> libProjects, String resFilter,
int versionCode, String outputFolder, String outputFilename)
throws AaptExecException, AaptResultException {
// need to figure out some path before we can execute aapt;

// get the resource folder
//Synthetic comment -- @@ -185,6 +198,13 @@
outputFolder + File.separator + outputFilename, resFilter,
versionCode);
}
}

/**
//Synthetic comment -- @@ -301,7 +321,9 @@
libName));

if (jarStatus.hasNativeLibsConflicts()) {
                            consoleMsgs.add("Additionally some of those libraries will interfer with the installation of the application because of their location in lib/");
consoleMsgs.add("lib/ is reserved for NDK libraries.");
}

//Synthetic comment -- @@ -362,6 +384,8 @@
} catch (SealedApkException e) {
// this won't happen as we control when the apk is sealed.
}
}

public String[] getProjectOutputs() throws CoreException {
//Synthetic comment -- @@ -754,6 +778,14 @@
AdtPlugin.printToConsole(mProject, sb.toString());
}

// launch
int execError = 1;
try {
//Synthetic comment -- @@ -782,6 +814,13 @@
String msg = String.format(Messages.AAPT_Exec_Error, command[0]);
throw new AaptExecException(msg, e);
}
}

/**
//Synthetic comment -- @@ -1085,8 +1124,15 @@
while (true) {
String line = outReader.readLine();
if (line != null) {
                            AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE,
                                    project, line);
} else {
break;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java
//Synthetic comment -- index 293b340..c993f05 100644

//Synthetic comment -- @@ -17,8 +17,8 @@
package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.Messages;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
//Synthetic comment -- @@ -53,9 +53,8 @@
*/
public abstract class BaseBuilder extends IncrementalProjectBuilder {


/** SAX Parser factory. */
    private SAXParserFactory mParserFactory;

/**
* Base Resource Delta Visitor to handle XML error








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 71c38f8..82849b2 100644

//Synthetic comment -- @@ -16,17 +16,17 @@

package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.build.AaptExecException;
import com.android.ide.eclipse.adt.internal.build.AaptParser;
import com.android.ide.eclipse.adt.internal.build.AaptResultException;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.DexException;
import com.android.ide.eclipse.adt.internal.build.Messages;
import com.android.ide.eclipse.adt.internal.build.NativeLibInJarException;
import com.android.ide.eclipse.adt.internal.build.BuildHelper.ResourceMarker;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.ApkInstallManager;
//Synthetic comment -- @@ -176,7 +176,7 @@
}
}

    private ResourceMarker mResourceMarker = new ResourceMarker() {
public void setWarning(IResource resource, String message) {
BaseProjectHelper.markResource(resource, AdtConstants.MARKER_PACKAGING,
message, IMarker.SEVERITY_WARNING);
//Synthetic comment -- @@ -208,6 +208,18 @@
// get a project object
IProject project = getProject();

// list of referenced projects. This is a mix of java projects and library projects
// and is computed below.
IProject[] allRefProjects = null;
//Synthetic comment -- @@ -621,6 +633,17 @@
markProject(AdtConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
}

return allRefProjects;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 943dcfe..91ab165 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.AaptParser;
import com.android.ide.eclipse.adt.internal.build.AidlProcessor;
import com.android.ide.eclipse.adt.internal.build.Messages;
import com.android.ide.eclipse.adt.internal.build.RenderScriptProcessor;
import com.android.ide.eclipse.adt.internal.build.SourceProcessor;
//Synthetic comment -- @@ -187,9 +188,22 @@
@Override
protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
throws CoreException {
// get a project object
IProject project = getProject();

// For the PreCompiler, only the library projects are considered Referenced projects,
// as only those projects have an impact on what is generated by this builder.
IProject[] result = null;
//Synthetic comment -- @@ -507,6 +521,17 @@
mGenFolder.refreshLocal(IResource.DEPTH_INFINITE, mDerivedProgressMonitor);
}

return result;
}

//Synthetic comment -- @@ -714,6 +739,14 @@
AdtPlugin.printToConsole(project, cmd_line);
}

// launch
int execError = 1;
try {
//Synthetic comment -- @@ -773,6 +806,14 @@
throw new AbortBuildException();
}

// if the return code was OK, we refresh the folder that
// contains R.java to force a java recompile.
if (execError == 0) {







