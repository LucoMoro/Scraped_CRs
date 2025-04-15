/*Improvements to RenderScript support:

- change dependency file location to be in the gen folder
  in the same package as the source file. This prevents collisions
  if there are two classes named the same in different packages
  or source folders.

- Now that the dependency files are visible, track their deletion
  and force recompilation if this happens

- Missing dependency files during generator initialization force
  a recompilation of the source file.

- Prevent packaging from taking .rs, .rsh, and .d files from the
  source folders. This also impacts the Ant build system.

Change-Id:Iede9193f67370e1784bd8ba661198bbf99238074*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java
//Synthetic comment -- index 0ed318a..85e265c 100644

//Synthetic comment -- @@ -162,6 +162,8 @@
public final static String RE_AIDL_EXT = "\\" + DOT_AIDL + "$"; //$NON-NLS-1$ //$NON-NLS-2$
/** Regexp for rs extension, i.e. "\.rs$" */
public final static String RE_RS_EXT = "\\" + DOT_RS + "$"; //$NON-NLS-1$ //$NON-NLS-2$
    /** Regexp for .d extension, i.e. "\.d$" */
    public final static String RE_DEP_EXT = "\\" + DOT_DEP + "$"; //$NON-NLS-1$ //$NON-NLS-2$

/**
* Namespace pattern for the custom resource XML, i.e. "http://schemas.android.com/apk/res/%s"








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlGenerator.java
//Synthetic comment -- index 3b28107..a3c587f 100644

//Synthetic comment -- @@ -56,9 +56,9 @@

/**
* Single line aidl error<br>
     * {@code <path>:<line>: <error>}<br>
     * or<br>
     * {@code <path>:<line> <error>}<br>
*/
private static Pattern sAidlPattern1 = Pattern.compile("^(.+?):(\\d+):?\\s(.+)$"); //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/GeneratorDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/GeneratorDeltaVisitor.java
//Synthetic comment -- index aeda16b..01cd13e 100644

//Synthetic comment -- @@ -90,7 +90,7 @@
return false;
}

    protected void addFileToCompile(IFile file) {
mToCompile.add(file);
}

//Synthetic comment -- @@ -98,7 +98,7 @@
return mToCompile;
}

    protected void addRemovedFile(IFile file) {
mRemoved.add(file);
}

//Synthetic comment -- @@ -111,6 +111,10 @@
mRemoved.clear();
}

    protected JavaGenerator getGenerator() {
        return mGenerator;
    }

void init(JavaGenerator generator) {
mGenerator = generator;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/JavaGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/JavaGenerator.java
//Synthetic comment -- index 33f3947..9e38031 100644

//Synthetic comment -- @@ -176,6 +176,8 @@
buildSourceFileList();

mToCompile.addAll(mFiles.keySet());

        saveState(project);
}

public final void doneVisiting(IProject project) {
//Synthetic comment -- @@ -281,7 +283,7 @@
public final void saveState(IProject project) {
// TODO: Optimize by saving only the files that need compilation
ProjectHelper.saveStringProperty(project, getSavePropertyName(),
                Boolean.toString(mToCompile.size() > 0));
}

protected abstract void loadOutputAndDependencies();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptGenerator.java
//Synthetic comment -- index 4a8abe5..776fa52 100644

//Synthetic comment -- @@ -33,6 +33,9 @@
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
//Synthetic comment -- @@ -56,12 +59,55 @@
private static final String PROPERTY_COMPILE_RS = "compileRenderScript"; //$NON-NLS-1$

/**
     * Single line llvm-rs-cc error: {@code <path>:<line>:<col>: <error>}
*/
private static Pattern sLlvmPattern1 = Pattern.compile("^(.+?):(\\d+):(\\d+):\\s(.+)$"); //$NON-NLS-1$

    private static class RsDeltaVisitor extends GeneratorDeltaVisitor {

        @Override
        public boolean handleGeneratedFile(IFile file, int kind) {
            boolean r = super.handleGeneratedFile(file, kind);
            if (r == false &&
                    kind == IResourceDelta.REMOVED &&
                    AndroidConstants.EXT_DEP.equalsIgnoreCase(file.getFileExtension())) {
                // This looks to be an extension file.
                // For futureproofness let's make sure this dependency file was generated by
                // this generator even if it's the only generator using them for now.

                // look for the original file.
                JavaGenerator generator = getGenerator();
                // We know we are in the gen folder
                IFolder genFolder = generator.getGenFolder();
                // make a path to the generated file relative to the gen folder.
                IPath relative = file.getFullPath().makeRelativeTo(genFolder.getFullPath());
                // remove the file name segment
                relative = relative.removeLastSegments(1);
                // add the file name of a renderscript file.
                relative = relative.append(file.getName().replaceAll(AndroidConstants.RE_DEP_EXT,
                        AndroidConstants.DOT_RS));

                // now look for a match in the source files.
                List<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(
                        generator.getJavaProject());
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

                for (IPath sourceFolderPath : sourceFolders) {
                    IFolder sourceFolder = root.getFolder(sourceFolderPath);
                    // we don't look in the 'gen' source folder as there will be no source in there.
                    if (sourceFolder.exists() && sourceFolder.equals(genFolder) == false) {
                        IFile sourceFile = sourceFolder.getFile(relative);
                        NonJavaFileBundle bundle = generator.getBundle(sourceFile);
                        if (bundle != null) {
                            addFileToCompile(sourceFile);
                            return true;
                        }
                    }
                }
            }

            return r;
        }

@Override
protected boolean filterResourceFolder(IContainer folder) {
//Synthetic comment -- @@ -70,7 +116,7 @@
}

public RenderScriptGenerator(IJavaProject javaProject, IFolder genFolder) {
        super(javaProject, genFolder, new RsDeltaVisitor());
}

@Override
//Synthetic comment -- @@ -100,23 +146,25 @@
IFolder rawFolder = project.getFolder(
new Path(SdkConstants.FD_RES).append(SdkConstants.FD_RAW));

        int depIndex;

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
        command[index++] = "-MD";  //$NON-NLS-1$

boolean verbose = AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE;
boolean someSuccess = false;
//Synthetic comment -- @@ -148,6 +196,7 @@
String osSourcePath = sourcePath.toOSString();

// finish to set the command line.
            command[depIndex] = getDependencyFolder(sourceFile).getLocation().toOSString();
command[index] = osSourcePath;

// launch the process
//Synthetic comment -- @@ -323,11 +372,14 @@
protected void loadOutputAndDependencies() {
Collection<NonJavaFileBundle> bundles = getBundles();
for (NonJavaFileBundle bundle : bundles) {
            // parse the dependency file. If this fails, force compilation of the file.
            if (parseDependencyFileFor(bundle.getSourceFile()) == false) {
                addFileToCompile(bundle.getSourceFile());
            }
}
}

    private boolean parseDependencyFileFor(IFile sourceFile) {
IFile depFile = getDependencyFileFor(sourceFile);
File f = depFile.getLocation().toFile();
if (f.exists()) {
//Synthetic comment -- @@ -337,15 +389,31 @@
addBundle(bundle);
}
parseDependencyFile(bundle, f);
            return true;
}

        return false;
}

    private IFolder getDependencyFolder(IFile sourceFile) {
        IPath sourceFolderPath = getSourceFolderFor(sourceFile);

        // this really shouldn't happen since the sourceFile must be in a source folder
        // since it comes from the delta visitor
        if (sourceFolderPath != null) {
            // make a path to the source file relative to the source folder.
            IPath relative = sourceFile.getFullPath().makeRelativeTo(sourceFolderPath);
            // remove the file name. This is now the destination folder.
            relative = relative.removeLastSegments(1);

            return getGenFolder().getFolder(relative);
        }

        return null;
}

private IFile getDependencyFileFor(IFile sourceFile) {
        IFolder depFolder = getDependencyFolder(sourceFile);
return depFolder.getFile(sourceFile.getName().replaceAll(AndroidConstants.RE_RS_EXT,
AndroidConstants.DOT_DEP));
}
//Synthetic comment -- @@ -365,11 +433,11 @@
// output1 output2 [...]: dep1 dep2 [...]
// expect it's likely split on several lines. So let's move it back on a single line
// first
        String[] lines = content.split("\n"); //$NON-NLS-1$
StringBuilder sb = new StringBuilder();
for (String line : lines) {
line = line.trim();
            if (line.endsWith("\\")) { //$NON-NLS-1$
line = line.substring(0, line.length() - 1);
}

//Synthetic comment -- @@ -377,13 +445,13 @@
}

// split the left and right part
        String[] files = sb.toString().split(":"); //$NON-NLS-1$

// get the output files:
        String[] outputs = files[0].trim().split(" "); //$NON-NLS-1$

// and the dependency files:
        String[] dependencies = files[1].trim().split(" "); //$NON-NLS-1$

List<IFile> outputFiles = new ArrayList<IFile>();
List<IFile> dependencyFiles = new ArrayList<IFile>();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 5df7f22..8dd92a7 100644

//Synthetic comment -- @@ -446,7 +446,6 @@
// remove some aapt_package only markers.
removeMarkersFromContainer(project, AndroidConstants.MARKER_AAPT_PACKAGE);

try {
helper.packageResources(manifestFile, libProjects, null /*resfilter*/,
0 /*versionCode */, osBinPath,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerDeltaVisitor.java
//Synthetic comment -- index b39236a..f7365e5 100644

//Synthetic comment -- @@ -272,13 +272,11 @@
// we want a warning
outputWarning = true;
} else {
                    // look to see if this file was generated by a generator.
                    for (GeneratorDeltaVisitor dv : mGeneratorDeltaVisitors) {
                        if (dv.handleGeneratedFile(file, kind)) {
                            outputWarning = true;
                            break; // there shouldn't be 2 generators that handle the same file.
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index 8c18c3a..c066dff 100644

//Synthetic comment -- @@ -872,12 +872,14 @@
* @return true if the file should be packaged as standard java resources.
*/
public static boolean checkFileForPackaging(String fileName, String extension) {
if (fileName.charAt(0) == '.') { // ignore hidden files.
return false;
}

return "aidl".equalsIgnoreCase(extension) == false &&       // Aidl files
            "rs".equalsIgnoreCase(extension) == false &&            // RenderScript files
            "rsh".equalsIgnoreCase(extension) == false &&           // RenderScript header files
            "d".equalsIgnoreCase(extension) == false &&             // Dependency files
"java".equalsIgnoreCase(extension) == false &&          // Java files
"class".equalsIgnoreCase(extension) == false &&         // Java class files
"scc".equalsIgnoreCase(extension) == false &&           // VisualSourceSafe







