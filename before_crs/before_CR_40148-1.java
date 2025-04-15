/*Fix lint-on-save for .class file detectors in Java files

This changeset fixes the lint-on-save behavior in Java files such that
the classfile based checks are run after the .class files are up to
date.

It also makes lint-on-save work when Project > Build Automatically is
turned off, by adding a new resource listener, and it modifies the
IFileListener interface to make resource listening more efficient; in
particular, it passes the flag mask such that listeners can ignore
events such as markers getting added or removed from a file without
the content changing.

It also makes some improvements to the lint infrastructure. First, it
adds an indirection in the LintClient such that reading bytes from
files can be customized by the client (to for example add caching or
to read contents from memory not yet flushed to disk). It also allows
inner classes to share the contents of the source file between each
context (while debugging the above I noticed that each inner class
node had its own class context and therefore would re-read the source
file repeatedly.)

Change-Id:Ib9572cebe1269fe05c3af1369610525ea3b44061*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 83812b2..a2afc89 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.common.CommonXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.AndroidClasspathContainerInitializer;
//Synthetic comment -- @@ -1479,6 +1480,7 @@
try {
setupDefaultEditor(mResourceMonitor);
ResourceManager.setup(mResourceMonitor);
} catch (Throwable t) {
log(t, "ResourceManager.setup failed"); //$NON-NLS-1$
}
//Synthetic comment -- @@ -1500,6 +1502,8 @@

IconFactory.getInstance().dispose();

// Remove the resource listener that handles compiled resources.
IWorkspace ws = ResourcesPlugin.getWorkspace();
GlobalProjectMonitor.stopMonitoring(ws);
//Synthetic comment -- @@ -1555,8 +1559,14 @@
* @see IFileListener#fileChanged
*/
@Override
            public void fileChanged(IFile file, IMarkerDelta[] markerDeltas, int kind) {
                if (AdtConstants.EXT_XML.equals(file.getFileExtension())) {
// The resources files must have a file path similar to
//    project/res/.../*.xml
// There is no support for sub folders, so the segment count must be 4








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index a3ae46b..4cf4a00 100644

//Synthetic comment -- @@ -449,7 +449,8 @@
* @param resource the resource to look up a path for
* @return an absolute file system path to the resource
*/
    public static IPath getAbsolutePath(IResource resource) {
IPath location = resource.getRawLocation();
if (location != null) {
return location.makeAbsolute();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index ea3a325..9467702 100644

//Synthetic comment -- @@ -27,12 +27,14 @@
import com.android.ide.eclipse.adt.internal.build.DexException;
import com.android.ide.eclipse.adt.internal.build.Messages;
import com.android.ide.eclipse.adt.internal.build.NativeLibInJarException;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.ApkInstallManager;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.LibraryClasspathContainerInitializer;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
//Synthetic comment -- @@ -248,6 +250,12 @@
mConvertToDex = true;
mBuildFinalPackage = true;
} else {
PatternBasedDeltaVisitor dv = new PatternBasedDeltaVisitor(
project, project,
"POST:Main");








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 18459d2..afc8fa7 100644

//Synthetic comment -- @@ -24,7 +24,6 @@
import com.android.ide.eclipse.adt.internal.build.RenderScriptProcessor;
import com.android.ide.eclipse.adt.internal.build.SourceProcessor;
import com.android.ide.eclipse.adt.internal.lint.EclipseLintClient;
import com.android.ide.eclipse.adt.internal.lint.LintDeltaProcessor;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
//Synthetic comment -- @@ -310,11 +309,6 @@
dv = new PreCompilerDeltaVisitor(this, sourceFolderPathList, mProcessors);
delta.accept(dv);

                    // Check for errors on save/build, if enabled
                    if (AdtPrefs.getPrefs().isLintOnSave()) {
                        LintDeltaProcessor.create().process(delta);
                    }

// Check to see if Manifest.xml, Manifest.java, or R.java have changed:
mMustCompileResources |= dv.getCompileResources();
mMustMergeManifest |= dv.hasManifestChanged();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java
//Synthetic comment -- index 1ee497e..0d4bd20 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.common.resources.ResourceFile;
import com.android.ide.common.resources.ResourceFolder;
import com.android.ide.eclipse.adt.AdtConstants;
//Synthetic comment -- @@ -172,7 +174,15 @@
* This records the changes for each project, but does not notify listeners.
*/
@Override
        public void fileChanged(IFile file, IMarkerDelta[] markerDeltas, int kind) {
// get the file's project
IProject project = file.getProject();

//Synthetic comment -- @@ -187,7 +197,7 @@
if (hasAndroidNature) {
// project is an Android project, it's the one being affected
// directly by its own file change.
                processFileChanged(file, project);
} else {
// check the projects depending on it, if they are Android project, update them.
IProject[] referencingProjects = project.getReferencingProjects();
//Synthetic comment -- @@ -203,7 +213,7 @@
if (hasAndroidNature) {
// the changed project is a dependency on an Android project,
// update the main project.
                        processFileChanged(file, p);
}
}
}
//Synthetic comment -- @@ -214,7 +224,7 @@
* @param file the changed file
* @param project the project impacted by the file change.
*/
        private void processFileChanged(IFile file, IProject project) {
// if this project has already been marked as modified, we do nothing.
ChangeFlags changeFlags = mProjectFlags.get(project);
if (changeFlags != null && changeFlags.isAllTrue()) {
//Synthetic comment -- @@ -223,7 +233,7 @@

// here we only care about code change (so change for .class files).
// Resource changes is handled by the IResourceListener.
            if (AdtConstants.EXT_CLASS.equals(file.getFileExtension())) {
if (file.getName().matches("R[\\$\\.](.*)")) {
// this is a R change!
if (changeFlags == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java
//Synthetic comment -- index cf210d1..7cfd6c4 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.util.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
//Synthetic comment -- @@ -292,7 +293,8 @@

mMarkerMonitor = new IFileListener() {
@Override
                public void fileChanged(IFile file, IMarkerDelta[] markerDeltas, int kind) {
if (file.equals(inputFile)) {
processMarkerChanges(markerDeltas);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index 0ec26fe..4d1030c 100644

//Synthetic comment -- @@ -690,7 +690,7 @@

private String readPlainFile(File file) {
try {
            return LintUtils.getEncodedString(file);
} catch (IOException e) {
return ""; //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaProcessor.java
//Synthetic comment -- index 7847262..6b94232 100644

//Synthetic comment -- @@ -17,17 +17,25 @@

import static com.android.ide.eclipse.adt.AdtConstants.DOT_CLASS;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_JAVA;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.List;

/**
//Synthetic comment -- @@ -39,6 +47,16 @@
private IFile mActiveFile;

private LintDeltaProcessor() {
}

/**
//Synthetic comment -- @@ -57,17 +75,6 @@
* @param delta the delta describing recently changed files
*/
public void process(@NonNull IResourceDelta delta)  {
        // Get the active editor file, if any
        Display display = AdtPlugin.getDisplay();
        if (display == null || display.isDisposed()) {
            return;
        }
        if (display.getThread() != Thread.currentThread()) {
            display.syncExec(this);
        } else {
            run();
        }

if (mActiveFile == null || !mActiveFile.getName().endsWith(DOT_JAVA)) {
return;
}
//Synthetic comment -- @@ -82,6 +89,24 @@
}

/**
* Collect .java and .class files to be run in lint. Only collects files
* that match the active editor.
*/
//Synthetic comment -- @@ -122,4 +147,54 @@
// Get the active file: this must be run on the GUI thread
mActiveFile = AdtUtils.getActiveFile();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/CompiledResourcesMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/CompiledResourcesMonitor.java
//Synthetic comment -- index fd6e174..31207e5 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.resources.IntArrayWrapper;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -80,7 +82,13 @@
* @see IFileListener#fileChanged
*/
@Override
    public void fileChanged(IFile file, IMarkerDelta[] markerDeltas, int kind) {
IProject project = file.getProject();

if (file.getName().equals(AdtConstants.FN_COMPILED_RESOURCE_CLASS)) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/GlobalProjectMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/GlobalProjectMonitor.java
//Synthetic comment -- index ea14a5c..b623cec 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.resources.ResourceFile;
import com.android.ide.common.resources.ResourceFolder;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -65,12 +67,18 @@
public interface IFileListener {
/**
* Sent when a file changed.
* @param file The file that changed.
* @param markerDeltas The marker deltas for the file.
* @param kind The change kind. This is equivalent to
         * {@link IResourceDelta#accept(IResourceDeltaVisitor)}
*/
        public void fileChanged(IFile file, IMarkerDelta[] markerDeltas, int kind);
}

/**
//Synthetic comment -- @@ -217,7 +225,8 @@
if (bundle.kindMask == ListenerBundle.MASK_NONE
|| (bundle.kindMask & kind) != 0) {
try {
                            bundle.listener.fileChanged((IFile)r, delta.getMarkerDeltas(), kind);
} catch (Throwable t) {
AdtPlugin.log(t,"Failed to call IFileListener.fileChanged");
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 6b45f09..aa5f473 100644

//Synthetic comment -- @@ -17,9 +17,11 @@
package com.android.ide.eclipse.adt.internal.sdk;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;
import static com.android.sdklib.SdkConstants.FD_RES;

import com.android.annotations.NonNull;
import com.android.ddmlib.IDevice;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.sdk.LoadStatus;
//Synthetic comment -- @@ -1014,7 +1016,8 @@
*/
private IFileListener mFileListener = new IFileListener() {
@Override
        public void fileChanged(final IFile file, IMarkerDelta[] markerDeltas, int kind) {
if (SdkConstants.FN_PROJECT_PROPERTIES.equals(file.getName()) &&
file.getParent() == file.getProject()) {
try {
//Synthetic comment -- @@ -1075,7 +1078,8 @@
}
} else if (kind == IResourceDelta.ADDED || kind == IResourceDelta.REMOVED) {
// check if it's an add/remove on a jar files inside libs
                if (file.getProjectRelativePath().segmentCount() == 2 &&
file.getParent().getName().equals(SdkConstants.FD_NATIVE_LIBS)) {
// need to update the project and whatever depend on it.









//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/Main.java b/lint/cli/src/com/android/tools/lint/Main.java
//Synthetic comment -- index 5e51106..fbb0b16 100644

//Synthetic comment -- @@ -1112,7 +1112,7 @@
@Override
public @NonNull String readFile(@NonNull File file) {
try {
            return LintUtils.getEncodedString(file);
} catch (IOException e) {
return ""; //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 93e311e..4cca31c 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import com.android.tools.lint.detector.api.Severity;
import com.google.common.annotations.Beta;
import com.google.common.collect.Maps;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
//Synthetic comment -- @@ -177,6 +178,22 @@
public abstract String readFile(@NonNull File file);

/**
* Returns the list of source folders for Java source files
*
* @param project the project to look up Java source file locations for








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 270ffa4..d91b155 100644

//Synthetic comment -- @@ -55,7 +55,6 @@
import com.google.common.collect.Multimap;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
//Synthetic comment -- @@ -1023,7 +1022,7 @@
String path = file.getPath();
if (file.isFile() && path.endsWith(DOT_CLASS)) {
try {
                        byte[] bytes = Files.toByteArray(file);
if (bytes != null) {
for (File dir : classFolders) {
if (path.startsWith(dir.getPath())) {
//Synthetic comment -- @@ -1068,6 +1067,8 @@
if (classDetectors != null && classDetectors.size() > 0 && entries.size() > 0) {
AsmVisitor visitor = new AsmVisitor(mClient, classDetectors);

mOuterClasses = new ArrayDeque<ClassNode>();
for (ClassEntry entry : entries) {
ClassReader reader;
//Synthetic comment -- @@ -1097,9 +1098,31 @@
continue;
}

ClassContext context = new ClassContext(this, project, main,
entry.file, entry.jarFile, entry.binDir, entry.bytes,
                            classNode, scope == Scope.JAVA_LIBRARIES /*fromLibrary*/);
try {
visitor.runClassDetectors(context);
} catch (Exception e) {
//Synthetic comment -- @@ -1109,6 +1132,9 @@
if (mCanceled) {
return;
}
}

mOuterClasses = null;
//Synthetic comment -- @@ -1229,7 +1255,7 @@

for (File file : classFiles) {
try {
                        byte[] bytes = Files.toByteArray(file);
if (bytes != null) {
entries.add(new ClassEntry(file, null /* jarFile*/, binDir, bytes));
}
//Synthetic comment -- @@ -1568,6 +1594,12 @@

@Override
@NonNull
public List<File> getJavaSourceFolders(@NonNull Project project) {
return mDelegate.getJavaSourceFolders(project);
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java
//Synthetic comment -- index 4aa21d4..5f8cac9 100644

//Synthetic comment -- @@ -82,6 +82,8 @@
* @param classNode the bytecode object model
* @param fromLibrary whether this class is from a library rather than part
*            of this project
*/
public ClassContext(
@NonNull LintDriver driver,
//Synthetic comment -- @@ -92,13 +94,15 @@
@NonNull File binDir,
@NonNull byte[] bytes,
@NonNull ClassNode classNode,
            boolean fromLibrary) {
super(driver, project, main, file);
mJarFile = jarFile;
mBinDir = binDir;
mBytes = bytes;
mClassNode = classNode;
mFromLibrary = fromLibrary;
}

/**
//Synthetic comment -- @@ -217,6 +221,28 @@
}

/**
* Returns a location for the given source line number in this class file's
* source file, if available.
*








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java
//Synthetic comment -- index 9bfb7cc..ce64fe32 100644

//Synthetic comment -- @@ -27,9 +27,9 @@
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.util.PositionXmlParser;
import com.google.common.annotations.Beta;
import com.google.common.io.Files;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
//Synthetic comment -- @@ -394,13 +394,16 @@
* same as {@code Files.toString(file, Charsets.UTF8}, but if there's a UTF byte order mark
* (for UTF8, UTF_16 or UTF_16LE), use that instead.
*
* @param file the file to read from
* @return the string
* @throws IOException if the file cannot be read properly
*/
@NonNull
    public static String getEncodedString(@NonNull File file) throws IOException {
        byte[] bytes = Files.toByteArray(file);
if (endsWith(file.getName(), DOT_XML)) {
return PositionXmlParser.getXmlString(bytes);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index adee77d..da27cc9 100644

//Synthetic comment -- @@ -45,7 +45,6 @@
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.google.common.io.Files;

import org.w3c.dom.Element;

//Synthetic comment -- @@ -422,7 +421,7 @@
byte[] bits = fileContents.get(file);
if (bits == null) {
try {
                            bits = Files.toByteArray(file);
fileContents.put(file, bits);
} catch (IOException e) {
context.log(e, null);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/detector/api/LintUtilsTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/detector/api/LintUtilsTest.java
//Synthetic comment -- index 67d2dfa..4765f41 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.tools.lint.detector.api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
//Synthetic comment -- @@ -210,7 +212,7 @@
writer.write(sb.toString());
writer.close();

        String s = LintUtils.getEncodedString(file);
assertEquals(expected, s);
}








