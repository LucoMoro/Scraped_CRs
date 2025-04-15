/*Added support for library /assets resource directories.

This patch adds capability to build list of all /assets directories
that are used in libraries. And then this list is passed to aapt tool
using multiple -A options. This allows to compile /assets from
all referenced libraries into main application.

Depends on changeI4192d978:
Added support for multiple raw asset directories (-A option) to aapt.

Change-Id:I4475e5cb79e5d25a0ea0758913dd903c09e9dd25Signed-off-by: Ievgenii Nazaruk <ievgenii.nazaruk@gmail.com>*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecLoopTask.java b/anttasks/src/com/android/ant/AaptExecLoopTask.java
//Synthetic comment -- index d7d53c9..78d9d58 100644

//Synthetic comment -- @@ -405,6 +405,19 @@
task.createArg().setValue(mAssets);
}

// android.jar
if (mAndroidJar != null) {
task.createArg().setValue("-I");








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AntConstants.java b/anttasks/src/com/android/ant/AntConstants.java
//Synthetic comment -- index a87d0d5..ab2b747 100644

//Synthetic comment -- @@ -49,6 +49,8 @@
public static final String PROP_PROJECT_LIBS_LIBS_REF = "project.libraries.libs";
/** ant property ref to the list of res folder for the project libraries */
public static final String PROP_PROJECT_LIBS_RES_REF = "project.libraries.res";
/** ant property for semi-colon separated packages for the project libraries */
public static final String PROP_PROJECT_LIBS_PKG = "project.libraries.package";
/** ant property for the test project directory */








//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/SetupTask.java b/anttasks/src/com/android/ant/SetupTask.java
//Synthetic comment -- index e15f77b..dde0b75 100644

//Synthetic comment -- @@ -377,6 +377,7 @@
// prepare several paths for future tasks
Path sourcePath = new Path(antProject);
Path resPath = new Path(antProject);
Path libsPath = new Path(antProject);
Path jarsPath = new Path(antProject);
StringBuilder sb = new StringBuilder();
//Synthetic comment -- @@ -419,6 +420,10 @@
element = resPath.createPathElement();
element.setPath(path + "/" + SdkConstants.FD_RESOURCES);

// get the libs path. Always $PROJECT/libs
element = libsPath.createPathElement();
element.setPath(path + "/" + SdkConstants.FD_NATIVE_LIBS);
//Synthetic comment -- @@ -462,6 +467,7 @@
// the rest is done only if there's a library.
if (sourcePath.list().length > 0) {
antProject.addReference(AntConstants.PROP_PROJECT_LIBS_RES_REF, resPath);
antProject.setProperty(AntConstants.PROP_PROJECT_LIBS_PKG, sb.toString());
}
}







