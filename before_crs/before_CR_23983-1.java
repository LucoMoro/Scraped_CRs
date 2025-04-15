/*Add Dependency/Multi R.java generation to Ant/ADT

This change changes ant and adt's calls to aapt to take advantage
of its ability to create multiple R.java files in one pass, thus
saving time when compiling projects with libraries. It also introduces
dependency handling for these R.java files speeding up builds when
no resources have been modified.

Change-Id:Ib0ef9ad0d600c5569db8714931a59c0e545614e3*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecLoopTask.java b/anttasks/src/com/android/ant/AaptExecLoopTask.java
//Synthetic comment -- index d7d53c9..bf85301 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.types.Path;

//Synthetic comment -- @@ -51,7 +50,7 @@
* <tr><td></td><td></td><td></td></tr>
* </table>
*/
public final class AaptExecLoopTask extends Task {

/**
* Class representing a &lt;nocompress&gt; node in the main task XML.
//Synthetic comment -- @@ -252,43 +251,39 @@
@Override
public void execute() throws BuildException {
Project taskProject = getProject();

        // first do a full resource package
        callAapt(null /*customPackage*/);

// if the parameters indicate generation of the R class, check if
// more R classes need to be created for libraries.
if (mRFolder != null && new File(mRFolder).isDirectory()) {
            String libPkgProp = taskProject.getProperty(AntConstants.PROP_PROJECT_LIBS_PKG);
            if (libPkgProp != null) {
                // get the main package to compare in case the libraries use the same
                String mainPackage = taskProject.getProperty(AntConstants.PROP_MANIFEST_PACKAGE);

                String[] libPkgs = libPkgProp.split(";");
                for (String libPkg : libPkgs) {
                    if (libPkg.length() > 0 && mainPackage.equals(libPkg) == false) {
                        // FIXME: instead of recreating R.java from scratch, maybe copy
                        // the files (R.java and manifest.java)? This would force to replace
                        // the package line on the fly.
                        callAapt(libPkg);
                    }
                }
            }
}
}

/**
* Calls aapt with the given parameters.
* @param resourceFilter the resource configuration filter to pass to aapt (if configName is
* non null)
     * @param customPackage an optional custom package.
*/
    private void callAapt(String customPackage) {
Project taskProject = getProject();

final boolean generateRClass = mRFolder != null && new File(mRFolder).isDirectory();

if (generateRClass) {
} else if (mResourceFilter == null) {
System.out.println("Creating full resource package...");
} else {
//Synthetic comment -- @@ -351,9 +346,9 @@
}
}

        if (customPackage != null) {
            task.createArg().setValue("--custom-package");
            task.createArg().setValue(customPackage);
}

// if the project contains libraries, force auto-add-overlay
//Synthetic comment -- @@ -427,6 +422,9 @@
if (generateRClass) {
task.createArg().setValue("-J");
task.createArg().setValue(mRFolder);
}

// final setup of the task








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/BaseTask.java b/anttasks/src/com/android/ant/BaseTask.java
new file mode 100644
//Synthetic comment -- index 0000000..2c6c4fc

//Synthetic comment -- @@ -0,0 +1,69 @@








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/DependencyGraph.java b/anttasks/src/com/android/ant/DependencyGraph.java
new file mode 100644
//Synthetic comment -- index 0000000..1deb9da

//Synthetic comment -- @@ -0,0 +1,224 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 943dcfe..5a7f0f6 100644

//Synthetic comment -- @@ -612,7 +612,7 @@
// handle libraries
ArrayList<IFolder> libResFolders = new ArrayList<IFolder>();
ArrayList<IFolder> libOutputFolders = new ArrayList<IFolder>();
            ArrayList<String> libJavaPackages = new ArrayList<String>();
if (libProjects != null) {
for (IProject lib : libProjects) {
IFolder libResFolder = lib.getFolder(SdkConstants.FD_RES);
//Synthetic comment -- @@ -623,26 +623,19 @@
try {
String libJavaPackage = AndroidManifest.getPackage(new IFolderWrapper(lib));
if (libJavaPackage.equals(javaPackage) == false) {
                            libJavaPackages.add(libJavaPackage);
                            libOutputFolders.add(getGenManifestPackageFolder(libJavaPackage));
}
} catch (Exception e) {
}
}
}

execAapt(project, projectTarget, osOutputPath, osResPath, osManifestPath,
                    mainPackageFolder, libResFolders, null /* custom java package */);

            final int count = libOutputFolders.size();
            if (count > 0) {
                for (int i = 0 ; i < count ; i++) {
                    IFolder libFolder = libOutputFolders.get(i);
                    String libJavaPackage = libJavaPackages.get(i);
                    execAapt(project, projectTarget, osOutputPath, osResPath, osManifestPath,
                            libFolder, libResFolders, libJavaPackage);
                }
            }
}
}

//Synthetic comment -- @@ -686,7 +679,7 @@
}

if (customJavaPackage != null) {
            array.add("--custom-package"); //$NON-NLS-1$
array.add(customJavaPackage);
}








