/*Properly handle failure to create folders.

Change-Id:Idbe33f803cc11856cffcb759e813417ccdca9208*/




//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/internal/incremental/ChangeManager.java b/builder/src/main/java/com/android/builder/internal/incremental/ChangeManager.java
//Synthetic comment -- index aca723e..8f6a0ee 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.builder.resources.FileStatus;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

//Synthetic comment -- @@ -62,9 +63,13 @@
/**
* Writes the incremental data to a given folder.
* @param incrementalFolder the name of the folder to write to.
     *
     * @throws IOException
*/
    public void write(File incrementalFolder) throws IOException {
        if (!incrementalFolder.isDirectory() && !incrementalFolder.mkdirs()) {
            throw new IOException("Failed to create directory " + incrementalFolder);
        }

mInputs.write(new File(incrementalFolder, FN_INPUTS_DATA));
mOutputs.write(new File(incrementalFolder, FN_OUTPUTS_DATA));
//Synthetic comment -- @@ -76,8 +81,10 @@
*/
public static void delete(File incrementalFolder) {
File file = new File(incrementalFolder, FN_INPUTS_DATA);
        //noinspection ResultOfMethodCallIgnored
file.delete();
file = new File(incrementalFolder, FN_OUTPUTS_DATA);
        //noinspection ResultOfMethodCallIgnored
file.delete();
}









//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/internal/incremental/FileManager.java b/builder/src/main/java/com/android/builder/internal/incremental/FileManager.java
//Synthetic comment -- index 2dbebd8..07543fe 100644

//Synthetic comment -- @@ -116,14 +116,18 @@
* Writes the state to a file
* @param stateFile the file to write the state to.
*
     * @throws IOException
     *
* @see #load(java.io.File)
*/
    public void write(File stateFile) throws IOException {
OutputStreamWriter writer = null;
try {
// first make sure the folders exist!
File parentFolder = stateFile.getParentFile();
            if (!parentFolder.isDirectory() && !parentFolder.mkdirs()) {
                throw new IOException("Failed to create directory " + parentFolder);
            }

// then write the file.
writer = new OutputStreamWriter(new FileOutputStream(stateFile), Charsets.UTF_8);
//Synthetic comment -- @@ -144,7 +148,6 @@
sha1,
entity.getFile().getAbsolutePath()));
}
} finally {
Closeables.closeQuietly(writer);
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceMerger.java b/builder/src/main/java/com/android/builder/resources/ResourceMerger.java
//Synthetic comment -- index 3f3b043..96768c0 100644

//Synthetic comment -- @@ -288,7 +288,7 @@
ResourceFolderType.VALUES.getName();

File valuesFolder = new File(rootFolder, folderName);
                createDir(valuesFolder);
File outFile = new File(valuesFolder, FN_VALUES_XML);

DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//Synthetic comment -- @@ -332,7 +332,7 @@
* Removes a file that already exists in the out res folder.
* @param outFolder the out res folder
* @param sourceFile the source file that created the file to remove.
     * @return true if success.
*/
private static boolean removeOutFile(File outFolder, ResourceFile sourceFile) {
if (sourceFile.getType() == ResourceFile.FileType.MULTI) {
//Synthetic comment -- @@ -401,9 +401,7 @@
}

File typeFolder = new File(rootFolder, folderName);
                createDir(typeFolder);

File outFile = new File(typeFolder, filename);
Files.copy(file, outFile);
//Synthetic comment -- @@ -443,7 +441,7 @@

String content = XmlPrettyPrinter.prettyPrint(document);

            createDir(blobRootFolder);
Files.write(content, new File(blobRootFolder, FN_MERGER_XML), Charsets.UTF_8);
} catch (ParserConfigurationException e) {
throw new IOException(e);
//Synthetic comment -- @@ -454,7 +452,7 @@
* Loads the merger state from a blob file.
*
* @param blobRootFolder the folder containing the blob.
     * @return true if the blob was loaded.
* @throws IOException
*
* @see #writeBlobTo(java.io.File)
//Synthetic comment -- @@ -600,4 +598,10 @@

return null;
}

    private static void createDir(File folder) throws IOException {
        if (!folder.isDirectory() && !folder.mkdirs()) {
            throw new IOException("Failed to create directory: " + folder);
        }
    }
}







