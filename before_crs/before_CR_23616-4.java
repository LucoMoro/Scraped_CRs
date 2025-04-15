/*Adding support to ADT and ant for smart R.java gen

This change lets ADT and ant make use of the new
--extra-packages flag in aapt to allow for all the R.java
files to be generated using only one call rather than one call
per library.

Change-Id:Ibc060b1218010cfbae108f4f1aba2c25fe69a964*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecLoopTask.java b/anttasks/src/com/android/ant/AaptExecLoopTask.java
//Synthetic comment -- index d7d53c9..4110e8d 100644

//Synthetic comment -- @@ -253,37 +253,26 @@
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
//Synthetic comment -- @@ -351,9 +340,9 @@
}
}

        if (customPackage != null) {
            task.createArg().setValue("--custom-package");
            task.createArg().setValue(customPackage);
}

// if the project contains libraries, force auto-add-overlay








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 943dcfe..e2083d1 100644

//Synthetic comment -- @@ -612,7 +612,7 @@
// handle libraries
ArrayList<IFolder> libResFolders = new ArrayList<IFolder>();
ArrayList<IFolder> libOutputFolders = new ArrayList<IFolder>();
            ArrayList<String> libJavaPackages = new ArrayList<String>();
if (libProjects != null) {
for (IProject lib : libProjects) {
IFolder libResFolder = lib.getFolder(SdkConstants.FD_RES);
//Synthetic comment -- @@ -623,26 +623,18 @@
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

//Synthetic comment -- @@ -686,7 +678,7 @@
}

if (customJavaPackage != null) {
            array.add("--custom-package"); //$NON-NLS-1$
array.add(customJavaPackage);
}








