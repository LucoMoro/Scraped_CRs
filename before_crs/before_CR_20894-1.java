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

/**
* Namespace pattern for the custom resource XML, i.e. "http://schemas.android.com/apk/res/%s"








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlGenerator.java
//Synthetic comment -- index 3b28107..a3c587f 100644

//Synthetic comment -- @@ -56,9 +56,9 @@

/**
* Single line aidl error<br>
     * "&lt;path&gt;:&lt;line&gt;: &lt;error&gt;"
     * or
     * "&lt;path&gt;:&lt;line&gt; &lt;error&gt;"
*/
private static Pattern sAidlPattern1 = Pattern.compile("^(.+?):(\\d+):?\\s(.+)$"); //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/GeneratorDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/GeneratorDeltaVisitor.java
//Synthetic comment -- index aeda16b..01cd13e 100644

//Synthetic comment -- @@ -90,7 +90,7 @@
return false;
}

    public void addFileToCompile(IFile file) {
mToCompile.add(file);
}

//Synthetic comment -- @@ -98,7 +98,7 @@
return mToCompile;
}

    public void addRemovedFile(IFile file) {
mRemoved.add(file);
}

//Synthetic comment -- @@ -111,6 +111,10 @@
mRemoved.clear();
}

void init(JavaGenerator generator) {
mGenerator = generator;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/JavaGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/JavaGenerator.java
//Synthetic comment -- index 33f3947..9e38031 100644

//Synthetic comment -- @@ -176,6 +176,8 @@
buildSourceFileList();

mToCompile.addAll(mFiles.keySet());
}

public final void doneVisiting(IProject project) {
//Synthetic comment -- @@ -281,7 +283,7 @@
public final void saveState(IProject project) {
// TODO: Optimize by saving only the files that need compilation
ProjectHelper.saveStringProperty(project, getSavePropertyName(),
                Boolean.toString(getToCompile().size() > 0));
}

protected abstract void loadOutputAndDependencies();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptGenerator.java
//Synthetic comment -- index 4a8abe5..776fa52 100644

//Synthetic comment -- @@ -33,6 +33,9 @@
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
//Synthetic comment -- @@ -56,12 +59,55 @@
private static final String PROPERTY_COMPILE_RS = "compileRenderScript"; //$NON-NLS-1$

/**
     * Single line llvm-rs-cc error<br>
     * "&lt;path&gt;:&lt;line&gt;:&lt;col&gt;: &lt;error&gt;"
*/
private static Pattern sLlvmPattern1 = Pattern.compile("^(.+?):(\\d+):(\\d+):\\s(.+)$"); //$NON-NLS-1$

    private static class RSDeltaVisitor extends GeneratorDeltaVisitor {

@Override
protected boolean filterResourceFolder(IContainer folder) {
//Synthetic comment -- @@ -70,7 +116,7 @@
}

public RenderScriptGenerator(IJavaProject javaProject, IFolder genFolder) {
        super(javaProject, genFolder, new RSDeltaVisitor());
}

@Override
//Synthetic comment -- @@ -100,23 +146,25 @@
IFolder rawFolder = project.getFolder(
new Path(SdkConstants.FD_RES).append(SdkConstants.FD_RAW));

// create the command line
String[] command = new String[13];
int index = 0;
command[index++] = sdkOsPath + SdkConstants.OS_SDK_PLATFORM_TOOLS_FOLDER
+ SdkConstants.FN_RENDERSCRIPT;
        command[index++] = "-I";
command[index++] = projectTarget.getPath(IAndroidTarget.ANDROID_RS_CLANG);
        command[index++] = "-I";
command[index++] = projectTarget.getPath(IAndroidTarget.ANDROID_RS);
        command[index++] = "-p";
command[index++] = genFolder.getLocation().toOSString();
        command[index++] = "-o";
command[index++] = rawFolder.getLocation().toOSString();

        command[index++] = "-d";
        command[index++] = getDependencyFolder().getLocation().toOSString();
        command[index++] = "-MD";

boolean verbose = AdtPrefs.getPrefs().getBuildVerbosity() == BuildVerbosity.VERBOSE;
boolean someSuccess = false;
//Synthetic comment -- @@ -148,6 +196,7 @@
String osSourcePath = sourcePath.toOSString();

// finish to set the command line.
command[index] = osSourcePath;

// launch the process
//Synthetic comment -- @@ -323,11 +372,14 @@
protected void loadOutputAndDependencies() {
Collection<NonJavaFileBundle> bundles = getBundles();
for (NonJavaFileBundle bundle : bundles) {
            parseDependencyFileFor(bundle.getSourceFile());
}
}

    private void parseDependencyFileFor(IFile sourceFile) {
IFile depFile = getDependencyFileFor(sourceFile);
File f = depFile.getLocation().toFile();
if (f.exists()) {
//Synthetic comment -- @@ -337,15 +389,31 @@
addBundle(bundle);
}
parseDependencyFile(bundle, f);
}
}

    private IFolder getDependencyFolder() {
        return getJavaProject().getProject().getFolder(SdkConstants.FD_OUTPUT);
}

private IFile getDependencyFileFor(IFile sourceFile) {
        IFolder depFolder = getDependencyFolder();
return depFolder.getFile(sourceFile.getName().replaceAll(AndroidConstants.RE_RS_EXT,
AndroidConstants.DOT_DEP));
}
//Synthetic comment -- @@ -365,11 +433,11 @@
// output1 output2 [...]: dep1 dep2 [...]
// expect it's likely split on several lines. So let's move it back on a single line
// first
        String[] lines = content.split("\n");
StringBuilder sb = new StringBuilder();
for (String line : lines) {
line = line.trim();
            if (line.endsWith("\\")) {
line = line.substring(0, line.length() - 1);
}

//Synthetic comment -- @@ -377,13 +445,13 @@
}

// split the left and right part
        String[] files = sb.toString().split(":");

// get the output files:
        String[] outputs = files[0].trim().split(" ");

// and the dependency files:
        String[] dependencies = files[1].trim().split(" ");

List<IFile> outputFiles = new ArrayList<IFile>();
List<IFile> dependencyFiles = new ArrayList<IFile>();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 5df7f22..8dd92a7 100644

//Synthetic comment -- @@ -446,7 +446,6 @@
// remove some aapt_package only markers.
removeMarkersFromContainer(project, AndroidConstants.MARKER_AAPT_PACKAGE);

                    // need to figure out some path before we can execute aapt;
try {
helper.packageResources(manifestFile, libProjects, null /*resfilter*/,
0 /*versionCode */, osBinPath,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerDeltaVisitor.java
//Synthetic comment -- index b39236a..f7365e5 100644

//Synthetic comment -- @@ -272,13 +272,11 @@
// we want a warning
outputWarning = true;
} else {
                    // look to see if this java file was generated by a generator.
                    if (AndroidConstants.EXT_JAVA.equalsIgnoreCase(file.getFileExtension())) {
                        for (GeneratorDeltaVisitor dv : mGeneratorDeltaVisitors) {
                            if (dv.handleGeneratedFile(file, kind)) {
                                outputWarning = true;
                                break; // there shouldn't be 2 generators that handle the same file.
                            }
}
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index 8c18c3a..c066dff 100644

//Synthetic comment -- @@ -872,12 +872,14 @@
* @return true if the file should be packaged as standard java resources.
*/
public static boolean checkFileForPackaging(String fileName, String extension) {
        // Note: this method is used by com.android.ide.eclipse.adt.internal.build.ApkBuilder
if (fileName.charAt(0) == '.') { // ignore hidden files.
return false;
}

return "aidl".equalsIgnoreCase(extension) == false &&       // Aidl files
"java".equalsIgnoreCase(extension) == false &&          // Java files
"class".equalsIgnoreCase(extension) == false &&         // Java class files
"scc".equalsIgnoreCase(extension) == false &&           // VisualSourceSafe







