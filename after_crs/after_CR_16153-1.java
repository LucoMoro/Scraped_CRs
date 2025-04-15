/*Clean up the previous fix to ApkBuilder with regard to the keystore.

Change-Id:I620269355e176d9167ceb733191ee5ea8908d06a*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index ce64689..2bc65c4 100644

//Synthetic comment -- @@ -25,6 +25,7 @@

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
//Synthetic comment -- @@ -232,32 +233,34 @@
* @param apkFile the file to create
* @param resFile the file representing the packaged resource file.
* @param dexFile the file representing the dex file. This can be null for apk with no code.
     * @param debugStoreOsPath the OS path to the debug keystore, if needed or null.
* @param verboseStream the stream to which verbose output should go. If null, verbose mode
*                      is not enabled.
* @throws ApkCreationException
*/
    public ApkBuilder(File apkFile, File resFile, File dexFile, String debugStoreOsPath,
PrintStream verboseStream) throws ApkCreationException {

try {
            checkOutputFile(mApkFile = apkFile);
            checkInputFile(mResFile = resFile);
            if (dexFile != null) {
                checkInputFile(mDexFile = dexFile);
            } else {
                mDexFile = null;
}
            mVerboseStream = verboseStream;

            if (debugStoreOsPath != null) {
                File storeFile = new File(debugStoreOsPath);
                try {
                    checkInputFile(storeFile);
                } catch (FileNotFoundException e) {
                    // ignore these since the debug store can be created on the fly anyway.
                }

// get the debug key
                verbosePrintln("Using keystore: %s", debugStoreOsPath);

IKeyGenOutput keygenOutput = null;
if (mVerboseStream != null) {
//Synthetic comment -- @@ -273,7 +276,7 @@
}

DebugKeyProvider keyProvider = new DebugKeyProvider(
                        debugStoreOsPath, null /*store type*/, keygenOutput);

PrivateKey key = keyProvider.getDebugKey();
X509Certificate certificate = (X509Certificate)keyProvider.getCertificate();
//Synthetic comment -- @@ -293,6 +296,7 @@
new FileOutputStream(mApkFile, false /* append */), key,
certificate);
} else {
                // no debug keystore? build without signing.
mBuilder = new SignedJarBuilder(
new FileOutputStream(mApkFile, false /* append */),
null /* key */, null /* certificate */);
//Synthetic comment -- @@ -320,11 +324,9 @@
"\nUpdate it if necessary, or manually execute the following command:\n" +
e.getCommandLine());
}
        } catch (ApkCreationException e) {
            throw e;
} catch (Exception e) {
throw new ApkCreationException(e);
}
}
//Synthetic comment -- @@ -685,11 +687,10 @@
* - that the file exists (if <var>throwIfDoesntExist</var> is <code>false</code>) and can
*    be read.
* @param file the File to check
     * @throws FileNotFoundException if the file is not here.
     * @throws ApkCreationException If the file is a folder or a file that cannot be read.
*/
    private void checkInputFile(File file) throws FileNotFoundException, ApkCreationException {
if (file.isDirectory()) {
throw new ApkCreationException("%s is a directory!", file);
}
//Synthetic comment -- @@ -698,8 +699,8 @@
if (file.canRead() == false) {
throw new ApkCreationException("Cannot read %s", file);
}
        } else {
            throw new FileNotFoundException(String.format("%s does not exist", file));
}
}








