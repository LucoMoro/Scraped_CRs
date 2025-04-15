/*Fix issue with generation of R classes for libraries.

When a clean action happen, the order of compilation of
the project isn't dictated by the dependencies between the
project. It is therefore important to detect changed in R.txt
for each library and recompile the project when that happens.

Also fixed a potential NPE when building libraries!

Change-Id:I6a87e9c3a4984e5aa6401270cf83eaea74044c9f*/
//Synthetic comment -- diff --git a/common/src/com/android/SdkConstants.java b/common/src/com/android/SdkConstants.java
//Synthetic comment -- index 833acdc..7cbbf68 100644

//Synthetic comment -- @@ -897,6 +897,8 @@
public static final String FN_RESOURCE_CLASS = FN_RESOURCE_BASE + DOT_JAVA;
/** Resource class file  filename, i.e. "R.class" */
public static final String FN_COMPILED_RESOURCE_CLASS = FN_RESOURCE_BASE + DOT_CLASS;
/** Manifest java class filename, i.e. "Manifest.java" */
public static final String FN_MANIFEST_CLASS = "Manifest.java"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 5fb6660..2ad8a09 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.build;

import com.android.SdkConstants;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -132,9 +133,10 @@
* @param verbose
* @throws CoreException
*/
    public BuildHelper(IProject project, AndroidPrintStream outStream,
            AndroidPrintStream errStream, boolean debugMode, boolean verbose,
            ResourceMarker resMarker) throws CoreException {
mProject = project;
mOutStream = outStream;
mErrStream = errStream;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/ChangedFileSetHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/ChangedFileSetHelper.java
//Synthetic comment -- index 67c7e8a..529dad2 100644

//Synthetic comment -- @@ -111,6 +111,22 @@
}

/**
* Returns a {@link ChangedFileSet} for a project's javac output.
* @param project the project
* @return a ChangedFileSet








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index e5b5a43..c91b629 100644

//Synthetic comment -- @@ -342,6 +342,15 @@
// delta changes.
abortOnBadSetup(javaProject);

// remove older packaging markers.
removeMarkersFromContainer(javaProject.getProject(), AdtConstants.MARKER_PACKAGING);

//Synthetic comment -- @@ -468,15 +477,6 @@
ic.refreshLocal(IResource.DEPTH_ONE, monitor);
}

            // Get the DX output stream. Since the builder is created for the life of the
            // project, they can be kept around.
            if (mOutStream == null) {
                mOutStream = new AndroidPrintStream(project, null /*prefix*/,
                        AdtPlugin.getOutStream());
                mErrStream = new AndroidPrintStream(project, null /*prefix*/,
                        AdtPlugin.getOutStream());
            }

// we need to test all three, as we may need to make the final package
// but not the intermediary ones.
if (mPackageResources || mConvertToDex || mBuildFinalPackage) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 2e66295..bd4dd4a 100644

//Synthetic comment -- @@ -258,6 +258,8 @@
return null;
}

IAndroidTarget projectTarget = projectState.getTarget();

// get the libraries
//Synthetic comment -- @@ -362,7 +364,7 @@

// if the main manifest didn't change, then we check for the library
// ones (will trigger manifest merging too)
            if (mMustMergeManifest == false && libProjects.size() > 0) {
for (IProject libProject : libProjects) {
IResourceDelta delta = getDelta(libProject);
if (delta != null) {
//Synthetic comment -- @@ -371,12 +373,24 @@
"PRE:LibManifest"); //$NON-NLS-1$
visitor.addSet(ChangedFileSetHelper.MANIFEST);

delta.accept(visitor);

mMustMergeManifest |= visitor.checkSet(ChangedFileSetHelper.MANIFEST);

                        // no need to test others.
                        if (mMustMergeManifest) {
break;
}
}
//Synthetic comment -- @@ -665,7 +679,7 @@
}

handleResources(project, javaPackage, projectTarget, manifestFile, libProjects,
                        projectState.isLibrary(), proguardFile);
}

if (processorStatus == SourceProcessor.COMPILE_STATUS_NONE &&
//Synthetic comment -- @@ -1111,16 +1125,23 @@
// now if the project has libraries, R needs to be created for each libraries
// unless this is a library.
if (isLibrary == false && !libRFiles.isEmpty()) {
                SymbolLoader symbolValues = new SymbolLoader(new File(outputFolder, "R.txt"));
                symbolValues.load();

                for (Pair<File, String> libData : libRFiles) {
                    SymbolLoader symbols = new SymbolLoader(libData.getFirst());
                    symbols.load();

                    SymbolWriter writer = new SymbolWriter(osOutputPath, libData.getSecond(),
                            symbols, symbolValues);
                    writer.write();
}
}








