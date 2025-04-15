/*Populate ResourceFolder contents on demand

There are scenarios where clients call getFile(String name) on a
ResourceFolder before the ResourceFolder is updated after a file
change. For example, creating a new layout file can result in this
scenario, which causes a number of "Current file is not a match for
the given config." error messages in the layout editor.

This changeset fixes this such that if an existing file is not found
for a given name, it also checks the file system to see if the file
really exists, and if so, it processes it and adds it to the list.

It also makes some performance improvements. First, the code which
processes a resource folder would check each added file to see if
already existed in the list of resource files. This resource file list
was just stored as a list, so when adding files from a large folder
(such as the framework drawable-mdpi folder which has 1,200 files,
each successive added file would be compared against the previous n-1
files, O(n^2).) This changeset now stores a separate name map such
that it can quickly determine whether a file is already known.

Second, since some of the folders in the framework are large (and
their sizes are roughly known up front), it creates the data
structures with larger up front sizes (depending on the folder type)
to avoid expensive rehashing and array copying.

Change-Id:Ieaab955d7f165e790e8a8dccebf4d0878fde6fce*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceFolder.java b/ide_common/src/com/android/ide/common/resources/ResourceFolder.java
//Synthetic comment -- index 03b6eb4..d0d91c7 100644

//Synthetic comment -- @@ -28,7 +28,9 @@

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
* Resource Folder class. Contains list of {@link ResourceFile}s,
//Synthetic comment -- @@ -38,10 +40,10 @@
final ResourceFolderType mType;
final FolderConfiguration mConfiguration;
IAbstractFolder mFolder;
    ArrayList<ResourceFile> mFiles = null;
private final ResourceRepository mRepository;


/**
* Creates a new {@link ResourceFolder}
* @param type The type of the folder
//Synthetic comment -- @@ -69,32 +71,13 @@
public ResourceFile processFile(IAbstractFile file, ResourceDeltaKind kind,
ScanningContext context) {
// look for this file if it's already been created
        ResourceFile resFile = getFile(file);

if (resFile == null) {
if (kind != ResourceDeltaKind.REMOVED) {
// create a ResourceFile for it.

                // check if that's a single or multi resource type folder. For now we define this by
                // the number of possible resource type output by files in the folder.
                // We have a special case for layout/menu folders which can also generate IDs.
                // This does
                // not make the difference between several resource types from a single file or
                // the ability to have 2 files in the same folder generating 2 different types of
                // resource. The former is handled by MultiResourceFile properly while we don't
                // handle the latter. If we were to add this behavior we'd have to change this call.
                List<ResourceType> types = FolderTypeRelationship.getRelatedResourceTypes(mType);

                if (types.size() == 1) {
                    resFile = new SingleResourceFile(file, this);
                } else if (types.contains(ResourceType.LAYOUT)) {
                    resFile = new IdGeneratingResourceFile(file, this, ResourceType.LAYOUT);
                } else if (types.contains(ResourceType.MENU)) {
                    resFile = new IdGeneratingResourceFile(file, this, ResourceType.MENU);
                } else {
                    resFile = new MultiResourceFile(file, this);
                }

resFile.load(context);

// add it to the folder
//Synthetic comment -- @@ -111,6 +94,29 @@
return resFile;
}


/**
* Adds a {@link ResourceFile} to the folder.
//Synthetic comment -- @@ -119,15 +125,68 @@
@VisibleForTesting(visibility=Visibility.PROTECTED)
public void addFile(ResourceFile file) {
if (mFiles == null) {
            mFiles = new ArrayList<ResourceFile>();
}

mFiles.add(file);
}

protected void removeFile(ResourceFile file, ScanningContext context) {
file.dispose(context);
mFiles.remove(file);
}

protected void dispose(ScanningContext context) {
//Synthetic comment -- @@ -137,6 +196,7 @@
}

mFiles.clear();
}
}

//Synthetic comment -- @@ -191,22 +251,43 @@
* @param name the name of the file.
*/
public boolean hasFile(String name) {
return mFolder.hasFile(name);
}

/**
* Returns the {@link ResourceFile} matching a {@link IAbstractFile} object.
* @param file The {@link IAbstractFile} object.
* @return the {@link ResourceFile} or null if no match was found.
*/
    private ResourceFile getFile(IAbstractFile file) {
        if (mFiles != null) {
            for (ResourceFile f : mFiles) {
                if (f.getFile().equals(file)) {
                    return f;
                }
}
}
return null;
}

//Synthetic comment -- @@ -216,13 +297,23 @@
* @return the {@link ResourceFile} or <code>null</code> if no match was found.
*/
public ResourceFile getFile(String filename) {
        if (mFiles != null) {
            for (ResourceFile f : mFiles) {
                if (f.getFile().getName().equals(filename)) {
                    return f;
                }
}
}
return null;
}








