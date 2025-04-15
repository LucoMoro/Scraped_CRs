/*New ApkBuilder class.

this is meant to replace the one previously in apkbuilder.jar and the
one in ADT, while being part of the public sdklib API so that other
tools can use it if needed (to deprecate the command line version)

Change-Id:I13f2a09d8d507a85be33af3fe659d175819cb641*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java
//Synthetic comment -- index d5d2185..73f6fcb 100644

//Synthetic comment -- @@ -101,8 +101,6 @@
public final static String FN_COMPILED_RESOURCE_CLASS = FN_RESOURCE_BASE + DOT_CLASS;
/** Manifest java class filename, i.e. "Manifest.java" */
public final static String FN_MANIFEST_CLASS = "Manifest.java"; //$NON-NLS-1$
    /** Dex conversion output filname, i.e. "classes.dex" */
    public final static String FN_CLASSES_DEX = "classes.dex"; //$NON-NLS-1$
/** Temporary packaged resources file name, i.e. "resources.ap_" */
public final static String FN_RESOURCES_AP_ = "resources.ap_"; //$NON-NLS-1$
/** Temporary packaged resources file name for a specific set of configuration */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/ApkBuilderHelper.java
//Synthetic comment -- index 04acf44..298b32a 100644

//Synthetic comment -- @@ -780,7 +780,6 @@
// lets check it's not already in the list of path added to the archive
if (list.indexOf(zipPath) != -1) {
AdtPlugin.printErrorToConsole(mProject,
                                    String.format(
Messages.ApkBuilder_s_Conflict_with_file_s,
fullPath, zipPath));
} else {
//Synthetic comment -- @@ -838,15 +837,11 @@

// check the name ends with .jar
if (AndroidConstants.EXT_JAR.equalsIgnoreCase(path.getFileExtension())) {
                        boolean local = false;
IResource resource = wsRoot.findMember(path);
if (resource != null && resource.exists() &&
resource.getType() == IResource.FILE) {
                            local = true;
oslibraryList.add(resource.getLocation().toOSString());
                        }

                        if (local == false) {
// if the jar path doesn't match a workspace resource,
// then we get an OSString and check if this links to a valid file.
String osFullPath = path.toOSString();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 36afc5b..824719a 100644

//Synthetic comment -- @@ -48,6 +48,9 @@

/** An SDK Project's AndroidManifest.xml file */
public static final String FN_ANDROID_MANIFEST_XML= "AndroidManifest.xml";
/** An SDK Project's build.xml file */
public final static String FN_BUILD_XML = "build.xml";

//Synthetic comment -- @@ -132,6 +135,11 @@
/** properties file for the SDK */
public final static String FN_SDK_PROP = "sdk.properties"; //$NON-NLS-1$

/* Folder Names for Android Projects . */

/** Resources folder name, i.e. "res". */








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/build/ApkBuilder.java
new file mode 100644
//Synthetic comment -- index 0000000..e69b6b7

//Synthetic comment -- @@ -0,0 +1,808 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/ApkBuilderHelper.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/ApkBuilderHelper.java
//Synthetic comment -- index ba1c878..85439c4 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdklib.internal.build;

import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.sdklib.internal.build.DebugKeyProvider.KeytoolException;

import java.io.File;
//Synthetic comment -- @@ -73,7 +74,6 @@
}
}

    private JavaResourceFilter mResourceFilter = new JavaResourceFilter();
private boolean mVerbose = false;
private boolean mSignedPackage = true;
private boolean mDebugMode = false;
//Synthetic comment -- @@ -236,7 +236,7 @@
Collection<ApkFile> javaResources) {
if (file.isDirectory()) {
// a directory? we check it
            if (JavaResourceFilter.checkFolderForPackaging(file.getName())) {
// if it's valid, we append its name to the current path.
if (path == null) {
path = file.getName();
//Synthetic comment -- @@ -252,7 +252,7 @@
}
} else {
// a file? we check it
            if (JavaResourceFilter.checkFileForPackaging(file.getName())) {
// we append its name to the current path
if (path == null) {
path = file.getName();
//Synthetic comment -- @@ -398,7 +398,7 @@

// add the java resource from jar files.
for (FileInputStream input : resourcesJars) {
                builder.writeZip(input, mResourceFilter);
}

// add the native files








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/JavaResourceFilter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/JavaResourceFilter.java
deleted file mode 100644
//Synthetic comment -- index aac1e55..0000000

//Synthetic comment -- @@ -1,103 +0,0 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.internal.build;

import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter;

/**
 * A basic implementation of {@link IZipEntryFilter} to filter out anything that is not a
 * java resource.
 */
public class JavaResourceFilter implements IZipEntryFilter {

    public boolean checkEntry(String name) {
        // split the path into segments.
        String[] segments = name.split("/");

        // empty path? skip to next entry.
        if (segments.length == 0) {
            return false;
        }

        // Check each folders to make sure they should be included.
        // Folders like CVS, .svn, etc.. should already have been excluded from the
        // jar file, but we need to exclude some other folder (like /META-INF) so
        // we check anyway.
        for (int i = 0 ; i < segments.length - 1; i++) {
            if (checkFolderForPackaging(segments[i]) == false) {
                return false;
            }
        }

        // get the file name from the path
        String fileName = segments[segments.length-1];

        return checkFileForPackaging(fileName);
    }

    /**
     * Checks whether a folder and its content is valid for packaging into the .apk as
     * standard Java resource.
     * @param folderName the name of the folder.
     */
    public static boolean checkFolderForPackaging(String folderName) {
        return folderName.equals("CVS") == false &&
            folderName.equals(".svn") == false &&
            folderName.equals("SCCS") == false &&
            folderName.equals("META-INF") == false &&
            folderName.startsWith("_") == false;
    }

    /**
     * Checks a file to make sure it should be packaged as standard resources.
     * @param fileName the name of the file (including extension)
     * @return true if the file should be packaged as standard java resources.
     */
    public static boolean checkFileForPackaging(String fileName) {
        String[] fileSegments = fileName.split("\\.");
        String fileExt = "";
        if (fileSegments.length > 1) {
            fileExt = fileSegments[fileSegments.length-1];
        }

        return checkFileForPackaging(fileName, fileExt);
    }

    /**
     * Checks a file to make sure it should be packaged as standard resources.
     * @param fileName the name of the file (including extension)
     * @param extension the extension of the file (excluding '.')
     * @return true if the file should be packaged as standard java resources.
     */
    public  static boolean checkFileForPackaging(String fileName, String extension) {
        // Note: this method is used by com.android.ide.eclipse.adt.internal.build.ApkBuilder
        if (fileName.charAt(0) == '.') { // ignore hidden files.
            return false;
        }

        return "aidl".equalsIgnoreCase(extension) == false &&       // Aidl files
            "java".equalsIgnoreCase(extension) == false &&          // Java files
            "class".equalsIgnoreCase(extension) == false &&         // Java class files
            "scc".equalsIgnoreCase(extension) == false &&           // VisualSourceSafe
            "swp".equalsIgnoreCase(extension) == false &&           // vi swap file
            "package.html".equalsIgnoreCase(fileName) == false &&   // Javadoc
            "overview.html".equalsIgnoreCase(fileName) == false &&  // Javadoc
            ".cvsignore".equalsIgnoreCase(fileName) == false &&     // CVS
            ".DS_Store".equals(fileName) == false &&                // Mac resources
            fileName.charAt(fileName.length()-1) != '~';            // Backup files
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SignedJarBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SignedJarBuilder.java
//Synthetic comment -- index 79e4be2..81131bc 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.build;

import sun.misc.BASE64Encoder;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
//Synthetic comment -- @@ -100,14 +102,42 @@
* be added to a Jar file.
*/
public interface IZipEntryFilter {
/**
* Checks a file for inclusion in a Jar archive.
         * @param name the archive file path of the entry
* @return <code>true</code> if the file should be included.
*/
        public boolean checkEntry(String name);
}
    
/**
* Creates a {@link SignedJarBuilder} with a given output stream, and signing information.
* <p/>If either <code>key</code> or <code>certificate</code> is <code>null</code> then
//Synthetic comment -- @@ -125,18 +155,18 @@
mOutputJar.setLevel(9);
mKey = key;
mCertificate = certificate;
        
if (mKey != null && mCertificate != null) {
mManifest = new Manifest();
Attributes main = mManifest.getMainAttributes();
main.putValue("Manifest-Version", "1.0");
main.putValue("Created-By", "1.0 (Android)");
    
mBase64Encoder = new BASE64Encoder();
mMessageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
}
}
    
/**
* Writes a new {@link File} into the archive.
* @param inputFile the {@link File} to write.
//Synthetic comment -- @@ -147,7 +177,7 @@
// Get an input stream on the file.
FileInputStream fis = new FileInputStream(inputFile);
try {
            
// create the zip entry
JarEntry entry = new JarEntry(jarPath);
entry.setTime(inputFile.lastModified());
//Synthetic comment -- @@ -166,8 +196,11 @@
* @param input the {@link InputStream} for the Jar/Zip to copy.
* @param filter the filter or <code>null</code>
* @throws IOException
*/
    public void writeZip(InputStream input, IZipEntryFilter filter) throws IOException {
ZipInputStream zis = new ZipInputStream(input);

try {
//Synthetic comment -- @@ -175,19 +208,19 @@
ZipEntry entry;
while ((entry = zis.getNextEntry()) != null) {
String name = entry.getName();
                
// do not take directories or anything inside a potential META-INF folder.
if (entry.isDirectory() || name.startsWith("META-INF/")) {
continue;
}
    
// if we have a filter, we check the entry against it
if (filter != null && filter.checkEntry(name) == false) {
continue;
}
    
JarEntry newEntry;
    
// Preserve the STORED method of the input entry.
if (entry.getMethod() == JarEntry.STORED) {
newEntry = new JarEntry(entry);
//Synthetic comment -- @@ -195,9 +228,9 @@
// Create a new entry so that the compressed len is recomputed.
newEntry = new JarEntry(name);
}
                
writeEntry(zis, newEntry);
    
zis.closeEntry();
}
} finally {
//Synthetic comment -- @@ -206,7 +239,7 @@
}

/**
     * Closes the Jar archive by creating the manifest, and signing the archive. 
* @throws IOException
* @throws GeneralSecurityException
*/
//Synthetic comment -- @@ -215,21 +248,21 @@
// write the manifest to the jar file
mOutputJar.putNextEntry(new JarEntry(JarFile.MANIFEST_NAME));
mManifest.write(mOutputJar);
            
// CERT.SF
Signature signature = Signature.getInstance("SHA1with" + mKey.getAlgorithm());
signature.initSign(mKey);
mOutputJar.putNextEntry(new JarEntry("META-INF/CERT.SF"));
writeSignatureFile(new SignatureOutputStream(mOutputJar, signature));
    
// CERT.*
mOutputJar.putNextEntry(new JarEntry("META-INF/CERT." + mKey.getAlgorithm()));
writeSignatureBlock(signature, mCertificate, mKey);
}
        
mOutputJar.close();
}
    
/**
* Adds an entry to the output jar, and write its content from the {@link InputStream}
* @param input The input stream from where to write the entry content.
//Synthetic comment -- @@ -241,10 +274,10 @@
mOutputJar.putNextEntry(entry);

// read the content of the entry from the input stream, and write it into the archive.
        int count; 
while ((count = input.read(mBuffer)) != -1) {
mOutputJar.write(mBuffer, 0, count);
            
// update the digest
if (mMessageDigest != null) {
mMessageDigest.update(mBuffer, 0, count);
//Synthetic comment -- @@ -253,7 +286,7 @@

// close the entry for this file
mOutputJar.closeEntry();
        
if (mManifest != null) {
// update the manifest for this entry.
Attributes attr = mManifest.getAttributes(entry.getName());
//Synthetic comment -- @@ -264,7 +297,7 @@
attr.putValue(DIGEST_ATTR, mBase64Encoder.encode(mMessageDigest.digest()));
}
}
    
/** Writes a .SF file with a digest to the manifest. */
private void writeSignatureFile(OutputStream out)
throws IOException, GeneralSecurityException {







