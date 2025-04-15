/*Added Benchmarking code

Added benchmarks to the ADT plugin's builders.
Benchmarking is disabled by default and can be
Enabled by setting the BENCHMARK_FLAG  in
BuildHelper.java

Patched: Replaced tabs with spaces and removed trailing
spaces. Changed naming to be consistent.
Signed-off-by: Josiah Gaskin <josiahgaskin@google.com>

Change-Id:I2b855b627f8970f2a18a2ad1899c2b54b22f242a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 5aa9647..2eeac42 100644

//Synthetic comment -- @@ -16,27 +16,18 @@

package com.android.ide.eclipse.adt.internal.build;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidPrintStream;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
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
//Synthetic comment -- @@ -57,18 +48,27 @@
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPreferenceStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
* Helper with methods for the last 3 steps of the generation of an APK.
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
//Synthetic comment -- @@ -362,6 +382,8 @@
} catch (SealedApkException e) {
// this won't happen as we control when the apk is sealed.
}
}

public String[] getProjectOutputs() throws CoreException {
//Synthetic comment -- @@ -754,6 +776,14 @@
AdtPlugin.printToConsole(mProject, sb.toString());
}

// launch
int execError = 1;
try {
//Synthetic comment -- @@ -782,6 +812,12 @@
String msg = String.format(Messages.AAPT_Exec_Error, command[0]);
throw new AaptExecException(msg, e);
}
}

/**
//Synthetic comment -- @@ -1085,8 +1121,16 @@
while (true) {
String line = outReader.readLine();
if (line != null) {
                            AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE,
                                    project, line);
} else {
break;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java
//Synthetic comment -- index 293b340..174b46e 100644

//Synthetic comment -- @@ -16,17 +16,11 @@

package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.build.Messages;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.project.XmlErrorHandler;
import com.android.ide.eclipse.adt.internal.project.XmlErrorHandler.XmlErrorListener;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -41,11 +35,17 @@
import org.eclipse.jdt.core.IJavaProject;
import org.xml.sax.SAXException;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
* Base builder for XML files. This class allows for basic XML parsing with
//Synthetic comment -- @@ -53,9 +53,8 @@
*/
public abstract class BaseBuilder extends IncrementalProjectBuilder {


/** SAX Parser factory. */
    private SAXParserFactory mParserFactory;

/**
* Base Resource Delta Visitor to handle XML error








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 71c38f8..cd1f67f 100644

//Synthetic comment -- @@ -16,29 +16,10 @@

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
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.build.ApkCreationException;
import com.android.sdklib.build.DuplicateFileException;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -57,10 +38,29 @@
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostCompilerBuilder extends BaseBuilder {

//Synthetic comment -- @@ -176,7 +176,7 @@
}
}

    private ResourceMarker mResourceMarker = new ResourceMarker() {
public void setWarning(IResource resource, String message) {
BaseProjectHelper.markResource(resource, AdtConstants.MARKER_PACKAGING,
message, IMarker.SEVERITY_WARNING);
//Synthetic comment -- @@ -208,6 +208,20 @@
// get a project object
IProject project = getProject();

// list of referenced projects. This is a mix of java projects and library projects
// and is computed below.
IProject[] allRefProjects = null;
//Synthetic comment -- @@ -621,6 +635,18 @@
markProject(AdtConstants.MARKER_PACKAGING, msg, IMarker.SEVERITY_ERROR);
}

return allRefProjects;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 943dcfe..39ff73d 100644

//Synthetic comment -- @@ -16,6 +16,24 @@

package com.android.ide.eclipse.adt.internal.build.builders;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.AaptParser;
//Synthetic comment -- @@ -40,24 +58,6 @@
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* Pre Java Compiler.
* This incremental builder performs 2 tasks:
//Synthetic comment -- @@ -187,9 +187,22 @@
@Override
protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
throws CoreException {
// get a project object
IProject project = getProject();

// For the PreCompiler, only the library projects are considered Referenced projects,
// as only those projects have an impact on what is generated by this builder.
IProject[] result = null;
//Synthetic comment -- @@ -507,6 +520,16 @@
mGenFolder.refreshLocal(IResource.DEPTH_INFINITE, mDerivedProgressMonitor);
}

return result;
}

//Synthetic comment -- @@ -714,6 +737,14 @@
AdtPlugin.printToConsole(project, cmd_line);
}

// launch
int execError = 1;
try {
//Synthetic comment -- @@ -773,6 +804,13 @@
throw new AbortBuildException();
}

// if the return code was OK, we refresh the folder that
// contains R.java to force a java recompile.
if (execError == 0) {







