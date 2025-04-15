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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Resource Folder class. Contains list of {@link ResourceFile}s,
//Synthetic comment -- @@ -38,10 +40,10 @@
final ResourceFolderType mType;
final FolderConfiguration mConfiguration;
IAbstractFolder mFolder;
    List<ResourceFile> mFiles = null;
    Map<String, ResourceFile> mNames = null;
private final ResourceRepository mRepository;

/**
* Creates a new {@link ResourceFolder}
* @param type The type of the folder
//Synthetic comment -- @@ -69,32 +71,13 @@
public ResourceFile processFile(IAbstractFile file, ResourceDeltaKind kind,
ScanningContext context) {
// look for this file if it's already been created
        ResourceFile resFile = getFile(file, context);

if (resFile == null) {
if (kind != ResourceDeltaKind.REMOVED) {
// create a ResourceFile for it.

                resFile = createResourceFile(file);
resFile.load(context);

// add it to the folder
//Synthetic comment -- @@ -111,6 +94,29 @@
return resFile;
}

    private ResourceFile createResourceFile(IAbstractFile file) {
        // check if that's a single or multi resource type folder. For now we define this by
        // the number of possible resource type output by files in the folder.
        // We have a special case for layout/menu folders which can also generate IDs.
        // This does
        // not make the difference between several resource types from a single file or
        // the ability to have 2 files in the same folder generating 2 different types of
        // resource. The former is handled by MultiResourceFile properly while we don't
        // handle the latter. If we were to add this behavior we'd have to change this call.
        List<ResourceType> types = FolderTypeRelationship.getRelatedResourceTypes(mType);

        ResourceFile resFile = null;
        if (types.size() == 1) {
            resFile = new SingleResourceFile(file, this);
        } else if (types.contains(ResourceType.LAYOUT)) {
            resFile = new IdGeneratingResourceFile(file, this, ResourceType.LAYOUT);
        } else if (types.contains(ResourceType.MENU)) {
            resFile = new IdGeneratingResourceFile(file, this, ResourceType.MENU);
        } else {
            resFile = new MultiResourceFile(file, this);
        }
        return resFile;
    }

/**
* Adds a {@link ResourceFile} to the folder.
//Synthetic comment -- @@ -119,15 +125,68 @@
@VisibleForTesting(visibility=Visibility.PROTECTED)
public void addFile(ResourceFile file) {
if (mFiles == null) {
            int initialSize = 16;
            if (mRepository.isFrameworkRepository()) {
                String name = mFolder.getName();
                // Pick some reasonable initial sizes for framework data structures
                // since they are typically (a) large and (b) their sizes are roughly known
                // in advance
                switch (mType) {
                    case DRAWABLE: {
                        // See if it's one of the -mdpi, -hdpi etc folders which
                        // are large (~1250 items)
                        int index = name.indexOf('-');
                        if (index == -1) {
                            initialSize = 230; // "drawable" folder
                        } else {
                            index = name.indexOf('-', index + 1);
                            if (index == -1) {
                                // One of the "drawable-<density>" folders
                                initialSize = 1260;
                            } else {
                                // "drawable-sw600dp-hdpi" etc
                                initialSize = 30;
                            }
                        }
                        break;
                    }
                    case LAYOUT: {
                        // The main layout folder has about ~185 layouts in it;
                        // the others are small
                        if (name.indexOf('-') == -1) {
                            initialSize = 200;
                        }
                        break;
                    }
                    case VALUES: {
                        if (name.indexOf('-') == -1) {
                            initialSize = 32;
                        } else {
                            initialSize = 4;
                        }
                        break;
                    }
                    case ANIM: initialSize = 85; break;
                    case COLOR: initialSize = 32; break;
                    case RAW: initialSize = 4; break;
                    default:
                        // Stick with the 16 default
                        break;
                }
            }

            mFiles = new ArrayList<ResourceFile>(initialSize);
            mNames = new HashMap<String, ResourceFile>(initialSize, 2.0f);
}

mFiles.add(file);
        mNames.put(file.getFile().getName(), file);
}

protected void removeFile(ResourceFile file, ScanningContext context) {
file.dispose(context);
mFiles.remove(file);
        mNames.remove(file.getFile().getName());
}

protected void dispose(ScanningContext context) {
//Synthetic comment -- @@ -137,6 +196,7 @@
}

mFiles.clear();
            mNames.clear();
}
}

//Synthetic comment -- @@ -191,22 +251,43 @@
* @param name the name of the file.
*/
public boolean hasFile(String name) {
        if (mNames.containsKey(name)) {
            return true;
        }

        // Note: mNames.containsKey(name) is faster, but doesn't give the same result; this
        // method seems to be called on this ResourceFolder before it has been processed,
        // so we need to use the file system check instead:
return mFolder.hasFile(name);
}

/**
* Returns the {@link ResourceFile} matching a {@link IAbstractFile} object.
     *
* @param file The {@link IAbstractFile} object.
     * @param context a context object with state for the current update, such
     *            as a place to stash errors encountered
* @return the {@link ResourceFile} or null if no match was found.
*/
    private ResourceFile getFile(IAbstractFile file, ScanningContext context) {
        assert mFolder.equals(file.getParentFolder());

        if (mNames != null) {
            ResourceFile resFile = mNames.get(file.getName());
            if (resFile != null) {
                return resFile;
}
}

        // If the file actually exists, the resource folder  may not have been
        // scanned yet; add it lazily
        if (file.exists()) {
            ResourceFile resFile = createResourceFile(file);
            resFile.load(context);
            addFile(resFile);
            return resFile;
        }

return null;
}

//Synthetic comment -- @@ -216,13 +297,23 @@
* @return the {@link ResourceFile} or <code>null</code> if no match was found.
*/
public ResourceFile getFile(String filename) {
        if (mNames != null) {
            ResourceFile resFile = mNames.get(filename);
            if (resFile != null) {
                return resFile;
}
}

        // If the file actually exists, the resource folder  may not have been
        // scanned yet; add it lazily
        IAbstractFile file = mFolder.getFile(filename);
        if (file != null && file.exists()) {
            ResourceFile resFile = createResourceFile(file);
            resFile.load(new ScanningContext(mRepository));
            addFile(resFile);
            return resFile;
        }

return null;
}








