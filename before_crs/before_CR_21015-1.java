/*Make sure to quote command line paths on windows.

The path of the executable should not be quoted.

Linux/MacOS actually don't work if anything is quoted so
only quote on windows.

Strangely aapt is not affected by this so don't touch it for now

Change-Id:I148f229d941baf722542ff72ee683fda212ec327*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java
//Synthetic comment -- index a4bcbed..699b7f3 100644

//Synthetic comment -- @@ -103,14 +103,14 @@
String[] command = new String[4 + sourceFolders.size()];
int index = 0;
command[index++] = projectTarget.getPath(IAndroidTarget.AIDL);
        command[index++] = "-p" + projectTarget.getPath(IAndroidTarget.ANDROID_AIDL);

// since the path are relative to the workspace and not the project itself, we need
// the workspace root.
IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
for (IPath p : sourceFolders) {
IFolder f = wsRoot.getFolder(p);
            command[index++] = "-I" + f.getLocation().toOSString(); //$NON-NLS-1$
}

boolean verbose = AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE;
//Synthetic comment -- @@ -144,8 +144,8 @@
}

// finish to set the command line.
            command[index] = osSourcePath;
            command[index + 1] = data.getOutput().getLocation().toOSString();

// launch the process
if (execAidl(builder, project, command, sourceFile, verbose) == false) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptProcessor.java
//Synthetic comment -- index 28ba66e..1967237 100644

//Synthetic comment -- @@ -152,16 +152,16 @@
// create the command line
String[] command = new String[13];
int index = 0;
        command[index++] = sdkOsPath + SdkConstants.OS_SDK_PLATFORM_TOOLS_FOLDER
                + SdkConstants.FN_RENDERSCRIPT;
command[index++] = "-I";   //$NON-NLS-1$
        command[index++] = projectTarget.getPath(IAndroidTarget.ANDROID_RS_CLANG);
command[index++] = "-I";   //$NON-NLS-1$
        command[index++] = projectTarget.getPath(IAndroidTarget.ANDROID_RS);
command[index++] = "-p";   //$NON-NLS-1$
        command[index++] = genFolder.getLocation().toOSString();
command[index++] = "-o";   //$NON-NLS-1$
        command[index++] = rawFolder.getLocation().toOSString();

command[index++] = "-d";   //$NON-NLS-1$
command[depIndex = index++] = null;
//Synthetic comment -- @@ -197,8 +197,8 @@
String osSourcePath = sourcePath.toOSString();

// finish to set the command line.
            command[depIndex] = getDependencyFolder(sourceFile).getLocation().toOSString();
            command[index] = osSourcePath;

// launch the process
if (execLlvmRsCc(builder, project, command, sourceFile, verbose) == false) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/SourceProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/SourceProcessor.java
//Synthetic comment -- index d08cdc5..177eb7d 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -65,6 +66,19 @@
/** List of removed source files pending cleaning at the next build. */
private final List<IFile> mRemoved = new ArrayList<IFile>();

protected SourceProcessor(IJavaProject javaProject, IFolder genFolder,
SourceChangeHandler deltaVisitor) {
mJavaProject = javaProject;







