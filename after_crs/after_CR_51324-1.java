/*Minor fix + more verbose assert in test to debug issue.

Change-Id:If201822e568f670f41866f490651b7060a8416e1*/




//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/DefaultSdkParser.java b/builder/src/main/java/com/android/builder/DefaultSdkParser.java
//Synthetic comment -- index bb07854..ac3041f 100644

//Synthetic comment -- @@ -23,12 +23,14 @@
import com.android.sdklib.internal.repository.packages.FullRevision;
import com.android.sdklib.repository.PkgProps;
import com.android.utils.ILogger;
import com.google.common.base.Charsets;
import com.google.common.io.Closeables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

//Synthetic comment -- @@ -82,7 +84,9 @@

Reader reader = null;
try {
            reader = new InputStreamReader(
                    new FileInputStream(new File(platformTools, FN_SOURCE_PROP)),
                    Charsets.UTF_8);
Properties props = new Properties();
props.load(reader);









//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/resources/BaseTestCase.java b/builder/src/test/java/com/android/builder/resources/BaseTestCase.java
//Synthetic comment -- index c82c587..3d1b68a 100644

//Synthetic comment -- @@ -122,11 +122,11 @@
DataMerger<? extends DataItem, ? extends DataFile, ? extends DataSet> dataMerger) {

// Loop on all the data sets.
        for (DataSet<? extends DataItem, ? extends DataFile> set : dataMerger.getDataSets()) {
// get the source files and verify they exists.
List<File> files = set.getSourceFiles();
for (File file : files) {
                assertTrue("Not a folder: " + file.getAbsolutePath(), file.isDirectory());
}

// for each source file, also check that the files inside are in fact inside
//Synthetic comment -- @@ -138,7 +138,8 @@
DataFile dataFile = item.getSource();
File file = dataFile.getFile();

                assertNotNull("Not in source file: " + file.getAbsolutePath(),
                        set.findMatchingSourceFile(file));
}
}
}








//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/resources/ResourceMergerTest.java b/builder/src/test/java/com/android/builder/resources/ResourceMergerTest.java
//Synthetic comment -- index 844506b..0e8c07c 100755

//Synthetic comment -- @@ -403,9 +403,6 @@

// ----------------
// first set is the main one, no change here

// ----------------
// second set is the overlay one







