/*Clean up the previous fix to ApkBuilder with regard to the keystore.

Change-Id:I620269355e176d9167ceb733191ee5ea8908d06a*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
//Synthetic comment -- index ce64689..2bc65c4 100644

//Synthetic comment -- @@ -25,6 +25,7 @@

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
//Synthetic comment -- @@ -232,32 +233,34 @@
* @param apkFile the file to create
* @param resFile the file representing the packaged resource file.
* @param dexFile the file representing the dex file. This can be null for apk with no code.
     * @param storeOsPath the OS path to the debug keystore, if needed or null.
* @param verboseStream the stream to which verbose output should go. If null, verbose mode
*                      is not enabled.
* @throws ApkCreationException
*/
    public ApkBuilder(File apkFile, File resFile, File dexFile, String storeOsPath,
PrintStream verboseStream) throws ApkCreationException {
        checkOutputFile(mApkFile = apkFile);
        checkInputFile(mResFile = resFile, true /*throwIfDoesntExist*/);
        if (dexFile != null) {
            checkInputFile(mDexFile = dexFile, true /*throwIfDoesntExist*/);
        } else {
            mDexFile = null;
        }
        mVerboseStream = verboseStream;

try {
            File storeFile = null;
            if (storeOsPath != null) {
                storeFile = new File(storeOsPath);
                checkInputFile(storeFile, false /*throwIfDoesntExist*/);
}

            if (storeFile != null) {
// get the debug key
                verbosePrintln("Using keystore: %s", storeOsPath);

IKeyGenOutput keygenOutput = null;
if (mVerboseStream != null) {
//Synthetic comment -- @@ -273,7 +276,7 @@
}

DebugKeyProvider keyProvider = new DebugKeyProvider(
                        storeOsPath, null /*store type*/, keygenOutput);

PrivateKey key = keyProvider.getDebugKey();
X509Certificate certificate = (X509Certificate)keyProvider.getCertificate();
//Synthetic comment -- @@ -293,6 +296,7 @@
new FileOutputStream(mApkFile, false /* append */), key,
certificate);
} else {
mBuilder = new SignedJarBuilder(
new FileOutputStream(mApkFile, false /* append */),
null /* key */, null /* certificate */);
//Synthetic comment -- @@ -320,11 +324,9 @@
"\nUpdate it if necessary, or manually execute the following command:\n" +
e.getCommandLine());
}
} catch (Exception e) {
            if (e instanceof ApkCreationException) {
                throw (ApkCreationException)e;
            }

throw new ApkCreationException(e);
}
}
//Synthetic comment -- @@ -685,11 +687,10 @@
* - that the file exists (if <var>throwIfDoesntExist</var> is <code>false</code>) and can
*    be read.
* @param file the File to check
     * @param indicates whether the method should throw {@link ApkCreationException} if the file
     *        does not exist at all.
     * @throws ApkCreationException If the check fails
*/
    private void checkInputFile(File file, boolean throwIfDoesntExist) throws ApkCreationException {
if (file.isDirectory()) {
throw new ApkCreationException("%s is a directory!", file);
}
//Synthetic comment -- @@ -698,8 +699,8 @@
if (file.canRead() == false) {
throw new ApkCreationException("Cannot read %s", file);
}
        } else if (throwIfDoesntExist) {
            throw new ApkCreationException("%s does not exist", file);
}
}








