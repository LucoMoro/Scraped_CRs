/*Fix broken generation of default keystore.

The location of the debug keystore was validated (to make sure
it was not a directory or an unreadable file), but it also threw
an exception if the file didn't exist, preventing the creation
from happening.

Change-Id:I2fc7d7be10ee4b85db3eab5ef4ee429681c8c5e9*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index cd6d37c..ce64689 100644

//Synthetic comment -- @@ -240,9 +240,9 @@
public ApkBuilder(File apkFile, File resFile, File dexFile, String storeOsPath,
PrintStream verboseStream) throws ApkCreationException {
checkOutputFile(mApkFile = apkFile);
        checkInputFile(mResFile = resFile);
if (dexFile != null) {
            checkInputFile(mDexFile = dexFile);
} else {
mDexFile = null;
}
//Synthetic comment -- @@ -252,7 +252,7 @@
File storeFile = null;
if (storeOsPath != null) {
storeFile = new File(storeOsPath);
                checkInputFile(storeFile);
}

if (storeFile != null) {
//Synthetic comment -- @@ -321,6 +321,10 @@
e.getCommandLine());
}
} catch (Exception e) {
throw new ApkCreationException(e);
}
}
//Synthetic comment -- @@ -678,11 +682,14 @@
* Checks an input {@link File} object.
* This checks the following:
* - the file is not an existing directory.
     * - that the file exists and can be read.
* @param file the File to check
* @throws ApkCreationException If the check fails
*/
    private void checkInputFile(File file) throws ApkCreationException {
if (file.isDirectory()) {
throw new ApkCreationException("%s is a directory!", file);
}
//Synthetic comment -- @@ -691,7 +698,7 @@
if (file.canRead() == false) {
throw new ApkCreationException("Cannot read %s", file);
}
        } else {
throw new ApkCreationException("%s does not exist", file);
}
}







