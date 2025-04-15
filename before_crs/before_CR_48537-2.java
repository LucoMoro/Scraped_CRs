/*Properly handle failure to create folders.

Change-Id:Idbe33f803cc11856cffcb759e813417ccdca9208*/
//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/internal/incremental/ChangeManager.java b/builder/src/main/java/com/android/builder/internal/incremental/ChangeManager.java
//Synthetic comment -- index aca723e..0cadce5 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.builder.resources.FileStatus;

import java.io.File;
import java.util.Collection;
import java.util.Map;

//Synthetic comment -- @@ -62,9 +63,13 @@
/**
* Writes the incremental data to a given folder.
* @param incrementalFolder the name of the folder to write to.
*/
    public void write(File incrementalFolder) {
        incrementalFolder.mkdirs();

mInputs.write(new File(incrementalFolder, FN_INPUTS_DATA));
mOutputs.write(new File(incrementalFolder, FN_OUTPUTS_DATA));
//Synthetic comment -- @@ -76,8 +81,10 @@
*/
public static void delete(File incrementalFolder) {
File file = new File(incrementalFolder, FN_INPUTS_DATA);
file.delete();
file = new File(incrementalFolder, FN_OUTPUTS_DATA);
file.delete();
}









//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/internal/incremental/FileManager.java b/builder/src/main/java/com/android/builder/internal/incremental/FileManager.java
//Synthetic comment -- index 2dbebd8..14300df 100644

//Synthetic comment -- @@ -116,14 +116,18 @@
* Writes the state to a file
* @param stateFile the file to write the state to.
*
* @see #load(java.io.File)
*/
    public void write(File stateFile) {
OutputStreamWriter writer = null;
try {
// first make sure the folders exist!
File parentFolder = stateFile.getParentFile();
            parentFolder.mkdirs();

// then write the file.
writer = new OutputStreamWriter(new FileOutputStream(stateFile), Charsets.UTF_8);
//Synthetic comment -- @@ -144,7 +148,6 @@
sha1,
entity.getFile().getAbsolutePath()));
}
        } catch (IOException ignored) {
} finally {
Closeables.closeQuietly(writer);
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceMerger.java b/builder/src/main/java/com/android/builder/resources/ResourceMerger.java
//Synthetic comment -- index 3f3b043..67d090d 100644

//Synthetic comment -- @@ -288,7 +288,9 @@
ResourceFolderType.VALUES.getName();

File valuesFolder = new File(rootFolder, folderName);
                valuesFolder.mkdirs();
File outFile = new File(valuesFolder, FN_VALUES_XML);

DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//Synthetic comment -- @@ -332,7 +334,7 @@
* Removes a file that already exists in the out res folder.
* @param outFolder the out res folder
* @param sourceFile the source file that created the file to remove.
     * @return true if sucess.
*/
private static boolean removeOutFile(File outFolder, ResourceFile sourceFile) {
if (sourceFile.getType() == ResourceFile.FileType.MULTI) {
//Synthetic comment -- @@ -402,7 +404,9 @@

File typeFolder = new File(rootFolder, folderName);
if (!typeFolder.isDirectory()) {
                    typeFolder.mkdirs();
}

File outFile = new File(typeFolder, filename);
//Synthetic comment -- @@ -443,7 +447,9 @@

String content = XmlPrettyPrinter.prettyPrint(document);

            blobRootFolder.mkdirs();
Files.write(content, new File(blobRootFolder, FN_MERGER_XML), Charsets.UTF_8);
} catch (ParserConfigurationException e) {
throw new IOException(e);
//Synthetic comment -- @@ -454,7 +460,7 @@
* Loads the merger state from a blob file.
*
* @param blobRootFolder the folder containing the blob.
     * @return
* @throws IOException
*
* @see #writeBlobTo(java.io.File)







