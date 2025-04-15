/*ADT builder clean up.

- SourceProcessor didn't empty is toCompile/removed file list
  when doing a full build, meaning some files were compiled
  several times.

- renderscript processor would always indicate that it generated
  resources even if that wasn't the case (no file or broken files)
  triggering a res compilation even if needed. Res compilation
  typically trigger a post compile refresh that triggers another
  build (which normally has no impact) but this new build
  also triggered a res compilation through the same issue ending
  with a build loop if a rs file was broken.
  The SourceProcessors now don't return a static compilation type
  but a true value based on what they did (or failed to do).

- the post compiler build now properly stops executing if there are
  problem markers from previous builders, including the JDT one.

Change-Id:Ida610dbe793f0df40b586572c52d33e4c93adff2*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java
//Synthetic comment -- index 699b7f3..8747f81 100644

//Synthetic comment -- @@ -90,11 +90,6 @@
}

@Override
protected void doCompileFiles(List<IFile> sources, BaseBuilder builder,
IProject project, IAndroidTarget projectTarget,
List<IPath> sourceFolders, List<IFile> notCompiledOut, IProgressMonitor monitor)
//Synthetic comment -- @@ -115,6 +110,9 @@

boolean verbose = AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE;

        // remove the generic marker from the project
        builder.removeMarkersFromResource(project, AndroidConstants.MARKER_AIDL);

// loop until we've compile them all
for (IFile sourceFile : sources) {
if (verbose) {
//Synthetic comment -- @@ -129,7 +127,7 @@
}

// Remove the AIDL error markers from the aidl file
            builder.removeMarkersFromResource(sourceFile, AndroidConstants.MARKER_AIDL);

// get the path of the source file.
IPath sourcePath = sourceFile.getLocation();
//Synthetic comment -- @@ -152,6 +150,9 @@
// aidl failed. File should be marked. We add the file to the list
// of file that will need compilation again.
notCompiledOut.add(sourceFile);
            } else {
                // Success. we'll return that we generated code and resources.
                setCompilationStatus(COMPILE_STATUS_CODE);
}
}
}
//Synthetic comment -- @@ -172,7 +173,6 @@
}
}

/**
* Execute the aidl command line, parse the output, and mark the aidl file
* with any reported errors.
//Synthetic comment -- @@ -217,7 +217,7 @@
AdtPlugin.printErrorToConsole(project, results.toArray());

// mark the project
                        BaseProjectHelper.markResource(project, AndroidConstants.MARKER_AIDL,
Messages.Unparsed_AIDL_Errors, IMarker.SEVERITY_ERROR);
} else {
AdtPlugin.printToConsole(project, results.toArray());
//Synthetic comment -- @@ -228,13 +228,13 @@
} catch (IOException e) {
// mark the project and exit
String msg = String.format(Messages.AIDL_Exec_Error, command[0]);
            BaseProjectHelper.markResource(project, AndroidConstants.MARKER_AIDL, msg,
IMarker.SEVERITY_ERROR);
return false;
} catch (InterruptedException e) {
// mark the project and exit
String msg = String.format(Messages.AIDL_Exec_Error, command[0]);
            BaseProjectHelper.markResource(project, AndroidConstants.MARKER_AIDL, msg,
IMarker.SEVERITY_ERROR);
return false;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptProcessor.java
//Synthetic comment -- index 1967237..30e3d5c 100644

//Synthetic comment -- @@ -131,11 +131,6 @@
}

@Override
protected void doCompileFiles(List<IFile> sources, BaseBuilder builder,
IProject project, IAndroidTarget projectTarget, List<IPath> sourceFolders,
List<IFile> notCompiledOut, IProgressMonitor monitor) throws CoreException {
//Synthetic comment -- @@ -170,6 +165,9 @@
boolean verbose = AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE;
boolean someSuccess = false;

        // remove the generic marker from the project
        builder.removeMarkersFromResource(project, AndroidConstants.MARKER_RENDERSCRIPT);

// loop until we've compile them all
for (IFile sourceFile : sources) {
if (verbose) {
//Synthetic comment -- @@ -184,11 +182,11 @@
}

// Remove the RS error markers from the source file and the dependencies
            builder.removeMarkersFromResource(sourceFile, AndroidConstants.MARKER_RENDERSCRIPT);
SourceFileData data = getFileData(sourceFile);
if (data != null) {
for (IFile dep : data.getDependencyFiles()) {
                    builder.removeMarkersFromResource(dep, AndroidConstants.MARKER_RENDERSCRIPT);
}
}

//Synthetic comment -- @@ -206,6 +204,9 @@
// of file that will need compilation again.
notCompiledOut.add(sourceFile);
} else {
                // Success. we'll return that we generated code and resources.
                setCompilationStatus(COMPILE_STATUS_CODE | COMPILE_STATUS_RES);

// need to parse the .d file to figure out the dependencies and the generated file
parseDependencyFileFor(sourceFile);
someSuccess = true;
//Synthetic comment -- @@ -252,7 +253,8 @@
AdtPlugin.printErrorToConsole(project, results.toArray());

// mark the project
                        BaseProjectHelper.markResource(project,
                                AndroidConstants.MARKER_RENDERSCRIPT,
"Unparsed Renderscript error! Check the console for output.",
IMarker.SEVERITY_ERROR);
} else {
//Synthetic comment -- @@ -266,7 +268,7 @@
String msg = String.format(
"Error executing Renderscript. Please check llvm-rs-cc is present at %1$s",
command[0]);
            BaseProjectHelper.markResource(project, AndroidConstants.MARKER_RENDERSCRIPT, msg,
IMarker.SEVERITY_ERROR);
return false;
} catch (InterruptedException e) {
//Synthetic comment -- @@ -274,7 +276,7 @@
String msg = String.format(
"Error executing Renderscript. Please check llvm-rs-cc is present at %1$s",
command[0]);
            BaseProjectHelper.markResource(project, AndroidConstants.MARKER_RENDERSCRIPT, msg,
IMarker.SEVERITY_ERROR);
return false;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/SourceProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/SourceProcessor.java
//Synthetic comment -- index 177eb7d..dcacbf6 100644

//Synthetic comment -- @@ -66,6 +66,8 @@
/** List of removed source files pending cleaning at the next build. */
private final List<IFile> mRemoved = new ArrayList<IFile>();

    private int mLastCompilationStatus = COMPILE_STATUS_NONE;

/**
* Quotes a path inside "". If the platform is not windows, the path is returned as is.
* @param path the path to quote
//Synthetic comment -- @@ -183,6 +185,9 @@
public final void prepareFullBuild(IProject project) {
mDeltaVisitor.reset();

        mToCompile.clear();
        mRemoved.clear();

// get all the source files
buildSourceFileList();

//Synthetic comment -- @@ -209,16 +214,18 @@
protected abstract String getSavePropertyName();

/**
     * Compiles the source files and return a status bitmask of the type of file that was generated.
*
*/
public final int compileFiles(BaseBuilder builder,
IProject project, IAndroidTarget projectTarget,
List<IPath> sourceFolders, IProgressMonitor monitor) throws CoreException {

        mLastCompilationStatus = COMPILE_STATUS_NONE;

if (mToCompile.size() == 0 && mRemoved.size() == 0) {
            return
            mLastCompilationStatus;
}

// if a source file is being removed before we managed to compile it, it'll be in
//Synthetic comment -- @@ -260,7 +267,7 @@
// before the project is closed/re-opened.)
saveState(project);

        return mLastCompilationStatus;
}

protected abstract void doCompileFiles(
//Synthetic comment -- @@ -270,12 +277,14 @@
throws CoreException;

/**
     * Adds a compilation status. It can be any of (in combination too):
* <p/>
* {@link #COMPILE_STATUS_CODE} means this processor created source code files.
* {@link #COMPILE_STATUS_RES} means this process created resources.
*/
    protected void setCompilationStatus(int status) {
        mLastCompilationStatus |= status;
    }

protected void doRemoveFiles(SourceFileData data) throws CoreException {
List<IFile> outputFiles = data.getOutputFiles();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/BaseBuilder.java
//Synthetic comment -- index 2dacf07..451b5aa 100644

//Synthetic comment -- @@ -151,7 +151,7 @@
IFile file = (IFile)resource;

// remove previous markers
            removeMarkersFromResource(file, AndroidConstants.MARKER_XML);

// create  the error handler
XmlErrorHandler reporter = new XmlErrorHandler(file, visitor);
//Synthetic comment -- @@ -190,18 +190,18 @@
}

/**
     * Removes markers from a resource and only the resource (not its children).
* @param file The file from which to delete the markers.
* @param markerId The id of the markers to remove. If null, all marker of
* type <code>IMarker.PROBLEM</code> will be removed.
*/
    public final void removeMarkersFromResource(IResource resource, String markerId) {
try {
            if (resource.exists()) {
                resource.deleteMarkers(markerId, true, IResource.DEPTH_ZERO);
}
} catch (CoreException ce) {
            String msg = String.format(Messages.Marker_Delete_Error, markerId, resource.toString());
AdtPlugin.printErrorToConsole(getProject(), msg);
}
}
//Synthetic comment -- @@ -224,24 +224,6 @@
}

/**
* Get the stderr output of a process and return when the process is done.
* @param process The process to get the ouput from
* @param results The array to store the stderr output








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 8dd92a7..1e818d5 100644

//Synthetic comment -- @@ -52,6 +52,7 @@
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
//Synthetic comment -- @@ -195,8 +196,8 @@
IProject project = getProject();

// Clear the project of the generic markers
        removeMarkersFromContainer(project, AndroidConstants.MARKER_AAPT_PACKAGE);
        removeMarkersFromContainer(project, AndroidConstants.MARKER_PACKAGING);
}

// build() returns a list of project from which this project depends for future compilation.
//Synthetic comment -- @@ -331,7 +332,7 @@
}

// remove older packaging markers.
            removeMarkersFromContainer(javaProject.getProject(), AndroidConstants.MARKER_PACKAGING);

if (outputFolder == null) {
// mark project and exit
//Synthetic comment -- @@ -640,14 +641,16 @@

IProject iProject = getProject();

        // do a (hopefully quick) search for Precompiler type markers.
        stopOnMarker(iProject, AndroidConstants.MARKER_AAPT_COMPILE, IResource.DEPTH_INFINITE);
        stopOnMarker(iProject, AndroidConstants.MARKER_AIDL, IResource.DEPTH_INFINITE);
        stopOnMarker(iProject, AndroidConstants.MARKER_RENDERSCRIPT, IResource.DEPTH_INFINITE);
stopOnMarker(iProject, AndroidConstants.MARKER_ANDROID, IResource.DEPTH_ZERO);

        // do a search for JDT markers.
        stopOnMarker(iProject, IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER,
                IResource.DEPTH_INFINITE);
        stopOnMarker(iProject, IJavaModelMarker.BUILDPATH_PROBLEM_MARKER,
                IResource.DEPTH_INFINITE);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index c1ba22c..55978dc 100644

//Synthetic comment -- @@ -350,7 +350,7 @@
AndroidVersion projectVersion = projectTarget.getVersion();

// remove earlier marker from the manifest
                removeMarkersFromResource(manifestFile, AndroidConstants.MARKER_ADT);

if (minSdkValue != -1) {
String codename = projectVersion.getCodename();
//Synthetic comment -- @@ -529,11 +529,11 @@
}

// Clear the project of the generic markers
        removeMarkersFromContainer(project, AndroidConstants.MARKER_AAPT_COMPILE);
        removeMarkersFromContainer(project, AndroidConstants.MARKER_XML);
        removeMarkersFromContainer(project, AndroidConstants.MARKER_AIDL);
        removeMarkersFromContainer(project, AndroidConstants.MARKER_RENDERSCRIPT);
        removeMarkersFromContainer(project, AndroidConstants.MARKER_ANDROID);
}

@Override
//Synthetic comment -- @@ -590,7 +590,7 @@
String osManifestPath = manifestLocation.toOSString();

// remove the aapt markers
            removeMarkersFromResource(manifest, AndroidConstants.MARKER_AAPT_COMPILE);
removeMarkersFromContainer(resFolder, AndroidConstants.MARKER_AAPT_COMPILE);

AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/ResourceManagerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/ResourceManagerBuilder.java
//Synthetic comment -- index 7d76ea1..0908260 100644

//Synthetic comment -- @@ -64,7 +64,7 @@
IProject project = getProject();

// Clear the project of the generic markers
        removeMarkersFromContainer(project, AndroidConstants.MARKER_ADT);
}

// build() returns a list of project from which this project depends for future compilation.
//Synthetic comment -- @@ -77,7 +77,7 @@
IJavaProject javaProject = JavaCore.create(project);

// Clear the project of the generic markers
        removeMarkersFromContainer(project, AndroidConstants.MARKER_ADT);

// check for existing target marker, in which case we abort.
// (this means: no SDK, no target, or unresolvable target.)







