/*Add support to Ant for packaging dependency checks

This change adds in support for making aapt generate a dependency
file for the .ap_ package generated during resource packaging. Ant
will then check this dependency file before calling aapt again and
will only repackage resources if the dependencies have been modified
in some way.

Change-Id:I56462163c5dd064c1416bc43913f044df8ee9be1*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecLoopTask.java b/anttasks/src/com/android/ant/AaptExecLoopTask.java
//Synthetic comment -- index ed0277a..66417cd 100644

//Synthetic comment -- @@ -282,34 +282,57 @@
// Get whether we have libraries
Object libResRef = taskProject.getReference(AntConstants.PROP_PROJECT_LIBS_RES_REF);

        // Set up our folders to check for changed files
        ArrayList<File> watchPaths = new ArrayList<File>();
        // We need to watch for changes in the main project res folder
        for (Path pathList : mResources) {
            for (String path : pathList.list()) {
                watchPaths.add(new File(path));
            }
        }
        // and if libraries exist, in their res folders
        if (libResRef instanceof Path) {
            for (String path : ((Path)libResRef).list()) {
                watchPaths.add(new File(path));
            }
        }
        // If we're here to generate a .ap_ file we need to watch assets as well
        if (!generateRClass) {
            File assetsDir = new File(mAssets);
            if (mAssets != null && assetsDir.isDirectory()) {
                watchPaths.add(assetsDir);
            }
        }

        // Now we figure out what we need to do
if (generateRClass) {
            // Check to see if our dependencies have changed. If not, then skip
if (initDependencies(mRFolder + File.separator + "R.d", watchPaths)
&& dependenciesHaveChanged() == false) {
System.out.println("No changed resources. R.java and Manifest.java untouched.");
return;
}
} else {
            // Find our dependency file. It should have the same name as our target .ap_ but
            // with a .d extension
            String dependencyFilePath = mApkFolder + File.separator + mApkName;
            dependencyFilePath = dependencyFilePath.substring(0, dependencyFilePath.indexOf("."))
                                        + ".d";

            // Check to see if our dependencies have changed
            if (initDependencies(dependencyFilePath , watchPaths)
                            && dependenciesHaveChanged() == false) {
                System.out.println("No changed resources or assets. " + dependencyFilePath
                                    + " remains untouched");
                return;
            }
            if (mResourceFilter == null) {
                System.out.println("Creating full resource package...");
            } else {
                System.out.println(String.format(
                        "Creating resource package with filter: (%1$s)...",
                        mResourceFilter));
            }
}

// create a task for the default apk.
//Synthetic comment -- @@ -441,10 +464,11 @@
if (generateRClass) {
task.createArg().setValue("-J");
task.createArg().setValue(mRFolder);
}

        // Use dependency generation
        task.createArg().setValue("--generate-dependencies");

// final setup of the task
task.setProject(taskProject);
task.setOwningTarget(getOwningTarget());







