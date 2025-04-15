/*ApkBuilderTask is now more lenient with missing libs/ folders.

Change-Id:Ic40c35e7074a91fb40094d3c5e5a605ff1ea2fef*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/ApkBuilderTask.java b/anttasks/src/com/android/ant/ApkBuilderTask.java
//Synthetic comment -- index 9e3c8e4..f73739b 100644

//Synthetic comment -- @@ -254,7 +254,11 @@
// now go through the list of jar folders.
for (Path pathList : mJarfolderList) {
for (String path : pathList.list()) {
                    // it's ok if top level folders are missing
                    File folder = new File(path);
                    if (folder.isDirectory()) {
                        ApkBuilderImpl.processJar(folder, mResourcesJars);
                    }
}
}

//Synthetic comment -- @@ -268,8 +272,12 @@
// now the native lib folder.
for (Path pathList : mNativeList) {
for (String path : pathList.list()) {
                    // it's ok if top level folders are missing
                    File folder = new File(path);
                    if (folder.isDirectory()) {
                        ApkBuilderImpl.processNativeFolder(folder, mDebug,
                                mNativeLibraries, mVerbose, mAbiFilter);
                    }
}
}









//Synthetic comment -- diff --git a/apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderImpl.java b/apkbuilder/src/com/android/apkbuilder/internal/ApkBuilderImpl.java
//Synthetic comment -- index 3189c41..2bc7625 100644

//Synthetic comment -- @@ -246,9 +246,10 @@
* @param file the {@link File} to process
* @param resourcesJars the collection of FileInputStream to fill up with jar files.
* @throws FileNotFoundException
     * @throws ApkCreationException
*/
public static void processJar(File file, Collection<FileInputStream> resourcesJars)
            throws FileNotFoundException, ApkCreationException {
if (file.isDirectory()) {
String[] filenames = file.list(new FilenameFilter() {
public boolean accept(File dir, String name) {
//Synthetic comment -- @@ -260,8 +261,10 @@
File f = new File(file, filename);
processJarFile(f, resourcesJars);
}
        } else if (file.isFile()) {
processJarFile(file, resourcesJars);
        } else {
            throw new ApkCreationException(file.getAbsolutePath()+ " does not exist!");
}
}








