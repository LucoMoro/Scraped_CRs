/*Adding support to ADT and ant for smart R.java gen

This change lets ADT and ant make use of the new
--extra-packages flag in aapt to allow for all the R.java
files to be generated using only one call rather than one call
per library.

Change-Id:Ibc060b1218010cfbae108f4f1aba2c25fe69a964*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecLoopTask.java b/anttasks/src/com/android/ant/AaptExecLoopTask.java
//Synthetic comment -- index d7d53c9..fb07f99 100644

//Synthetic comment -- @@ -253,37 +253,24 @@
public void execute() throws BuildException {
Project taskProject = getProject();

        String libPkgProp = null;

// if the parameters indicate generation of the R class, check if
// more R classes need to be created for libraries.
if (mRFolder != null && new File(mRFolder).isDirectory()) {
            libPkgProp = taskProject.getProperty(AntConstants.PROP_PROJECT_LIBS_PKG);
}
        // Call aapt. If there are libraries, we'll pass a non-null string of libs.
        callAapt(libPkgProp);
}

/**
* Calls aapt with the given parameters.
* @param resourceFilter the resource configuration filter to pass to aapt (if configName is
* non null)
     * @param extraPackages an optional custom package.
*/
    private void callAapt(String extraPackages) {
Project taskProject = getProject();

final boolean generateRClass = mRFolder != null && new File(mRFolder).isDirectory();
//Synthetic comment -- @@ -351,9 +338,9 @@
}
}

        if (extraPackages != null) {
            task.createArg().setValue("--extra-packages");
            task.createArg().setValue(extraPackages);
}

// if the project contains libraries, force auto-add-overlay








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 943dcfe..727bfcc 100644

//Synthetic comment -- @@ -612,7 +612,7 @@
// handle libraries
ArrayList<IFolder> libResFolders = new ArrayList<IFolder>();
ArrayList<IFolder> libOutputFolders = new ArrayList<IFolder>();
            String libJavaPackages = new String();
if (libProjects != null) {
for (IProject lib : libProjects) {
IFolder libResFolder = lib.getFolder(SdkConstants.FD_RES);
//Synthetic comment -- @@ -623,26 +623,18 @@
try {
String libJavaPackage = AndroidManifest.getPackage(new IFolderWrapper(lib));
if (libJavaPackage.equals(javaPackage) == false) {
                            libJavaPackages += libJavaPackage + ";";
}
} catch (Exception e) {
}
}
}

            if (libJavaPackages.isEmpty()) {
                libJavaPackages = null;
}
            execAapt(project, projectTarget, osOutputPath, osResPath, osManifestPath,
                    mainPackageFolder, libResFolders, libJavaPackages);
}
}

//Synthetic comment -- @@ -686,7 +678,7 @@
}

if (customJavaPackage != null) {
            array.add("--extra-packages"); //$NON-NLS-1$
array.add(customJavaPackage);
}








