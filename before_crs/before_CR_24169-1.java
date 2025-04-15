/*Add support to Ant for packaging dependency checks

This change adds in support for making aapt generate a dependency
file for the .ap_ package generated during resource packaging. Ant
will then check this dependency file before calling aapt again and
will only repackage resources if the dependencies have been modified
in some way.

Change-Id:I56462163c5dd064c1416bc43913f044df8ee9be1*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecLoopTask.java b/anttasks/src/com/android/ant/AaptExecLoopTask.java
//Synthetic comment -- index 579b4b5..f7c8a6b 100644

//Synthetic comment -- @@ -281,34 +281,57 @@
// Get whether we have libraries
Object libResRef = taskProject.getReference(AntConstants.PROP_PROJECT_LIBS_RES_REF);

if (generateRClass) {
            // If the only reason we're here is to generate R.java and that doesn't need updating
            // we can skip what comes next. First we grab the dependency file.
            // Then query to see if an update is needed.
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
if (initDependencies(mRFolder + File.separator + "R.d", watchPaths)
&& dependenciesHaveChanged() == false) {
System.out.println("No changed resources. R.java and Manifest.java untouched.");
return;
}
        } else if (mResourceFilter == null) {
            System.out.println("Creating full resource package...");
} else {
            System.out.println(String.format(
                    "Creating resource package with filter: (%1$s)...",
                    mResourceFilter));
}

// create a task for the default apk.
//Synthetic comment -- @@ -440,10 +463,11 @@
if (generateRClass) {
task.createArg().setValue("-J");
task.createArg().setValue(mRFolder);
            // Use dependency generation
            task.createArg().setValue("--generate-dependencies");
}

// final setup of the task
task.setProject(taskProject);
task.setOwningTarget(getOwningTarget());







