/*Misc fixes from previous comments.

Change-Id:I00e1f1d12b8f8659e24972991840d72ffd734d2e*/
//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/DefaultSdkParser.java b/builder/src/main/java/com/android/builder/DefaultSdkParser.java
//Synthetic comment -- index ebf7ef7..3d1fab4 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.builder;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkManager;
//Synthetic comment -- @@ -32,6 +31,12 @@
import java.io.Reader;
import java.util.Properties;

/**
* Default implementation of {@link SdkParser} for a normal Android SDK distribution.
*/
//Synthetic comment -- @@ -49,7 +54,7 @@
}

@Override
    public IAndroidTarget resolveTarget(String target, ILogger logger) {
if (mManager == null) {
mManager = SdkManager.createManager(mSdkLocation, logger);
if (mManager == null) {
//Synthetic comment -- @@ -62,14 +67,14 @@

@Override
public String getAnnotationsJar() {
        return mSdkLocation + SdkConstants.FD_TOOLS +
                '/' + SdkConstants.FD_SUPPORT +
                '/' + SdkConstants.FN_ANNOTATIONS_JAR;
}

@Override
public FullRevision getPlatformToolsRevision() {
        File platformTools = new File(mSdkLocation, SdkConstants.FD_PLATFORM_TOOLS);
if (!platformTools.isDirectory()) {
return null;
}
//Synthetic comment -- @@ -77,7 +82,7 @@

Reader reader = null;
try {
            reader = new FileReader(new File(platformTools, SdkConstants.FN_SOURCE_PROP));
Properties props = new Properties();
props.load(reader);









//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/internal/CommandLineRunner.java b/builder/src/main/java/com/android/builder/internal/CommandLineRunner.java
//Synthetic comment -- index bcc3a6a..fdad415 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.annotations.Nullable;
import com.android.sdklib.util.GrabProcessOutput;
import com.android.utils.ILogger;

import java.io.IOException;
import java.util.List;
//Synthetic comment -- @@ -96,11 +97,6 @@
}

private void printCommand(String[] command) {
        StringBuilder sb = new StringBuilder("command: ");
        for (String arg : command) {
            sb.append(arg).append(' ');
        }

        mLogger.info(sb.toString());
}
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceMerger.java b/builder/src/main/java/com/android/builder/resources/ResourceMerger.java
//Synthetic comment -- index a351d86..647beb2 100755

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.builder.resources;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
//Synthetic comment -- @@ -54,6 +53,10 @@
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
* Merges {@link ResourceSet}s and writes a resource folder that can be fed to aapt.
*
//Synthetic comment -- @@ -296,7 +299,7 @@

if (mustWriteFile) {
String folderName = key.length() > 0 ?
                        ResourceFolderType.VALUES.getName() + SdkConstants.RES_QUALIFIER_SEP + key :
ResourceFolderType.VALUES.getName();

File valuesFolder = new File(rootFolder, folderName);
//Synthetic comment -- @@ -313,7 +316,7 @@
builder = factory.newDocumentBuilder();
Document document = builder.newDocument();

                    Node rootNode = document.createElement(SdkConstants.TAG_RESOURCES);
document.appendChild(rootNode);

for (Resource item : items) {
//Synthetic comment -- @@ -333,7 +336,7 @@
// now remove empty values files.
for (String key : qualifierWithDeletedValues) {
String folderName = key != null && key.length() > 0 ?
                    ResourceFolderType.VALUES.getName() + SdkConstants.RES_QUALIFIER_SEP + key :
ResourceFolderType.VALUES.getName();

removeOutFile(rootFolder, folderName, FN_VALUES_XML);
//Synthetic comment -- @@ -419,7 +422,7 @@
String folderName = resource.getType().getName();
String qualifiers = resourceFile.getQualifiers();
if (qualifiers != null && qualifiers.length() > 0) {
                            folderName = folderName + SdkConstants.RES_QUALIFIER_SEP + qualifiers;
}

File typeFolder = new File(rootFolder, folderName);
//Synthetic comment -- @@ -427,7 +430,7 @@

File outFile = new File(typeFolder, filename);

                        if (aaptRunner != null && filename.endsWith(".9.png")) {
// run aapt in single crunch mode on the original file to write the
// destination file.
aaptRunner.crunchPng(file, outFile);







