/*Add multi assets folder support + assets in libraries.

Change-Id:I75d0a95aa330ab9ce25132800fe9ba2e57710b87*/
//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/Asset.java b/builder/src/main/java/com/android/builder/resources/Asset.java
new file mode 100644
//Synthetic comment -- index 0000000..37972c2

//Synthetic comment -- @@ -0,0 +1,39 @@








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/AssetFile.java b/builder/src/main/java/com/android/builder/resources/AssetFile.java
new file mode 100644
//Synthetic comment -- index 0000000..5f0e4ec

//Synthetic comment -- @@ -0,0 +1,39 @@








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/AssetSet.java b/builder/src/main/java/com/android/builder/resources/AssetSet.java
new file mode 100644
//Synthetic comment -- index 0000000..7c1e91a

//Synthetic comment -- @@ -0,0 +1,74 @@








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/DataFile.java b/builder/src/main/java/com/android/builder/resources/DataFile.java
new file mode 100644
//Synthetic comment -- index 0000000..0f68c61

//Synthetic comment -- @@ -0,0 +1,120 @@








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/DataItem.java b/builder/src/main/java/com/android/builder/resources/DataItem.java
new file mode 100644
//Synthetic comment -- index 0000000..94eafd7

//Synthetic comment -- @@ -0,0 +1,185 @@








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/DataMap.java b/builder/src/main/java/com/android/builder/resources/DataMap.java
new file mode 100644
//Synthetic comment -- index 0000000..f8f6ce0

//Synthetic comment -- @@ -0,0 +1,41 @@








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/DataSet.java b/builder/src/main/java/com/android/builder/resources/DataSet.java
new file mode 100755
//Synthetic comment -- index 0000000..cd8e468

//Synthetic comment -- @@ -0,0 +1,508 @@








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/DuplicateResourceException.java b/builder/src/main/java/com/android/builder/resources/DuplicateDataException.java
similarity index 75%
rename from builder/src/main/java/com/android/builder/resources/DuplicateResourceException.java
rename to builder/src/main/java/com/android/builder/resources/DuplicateDataException.java
//Synthetic comment -- index 3bfdfdc..448eedc 100644

//Synthetic comment -- @@ -17,14 +17,14 @@
package com.android.builder.resources;

/**
 * Exception when a resource is declared more than once in a {@link ResourceSet}
*/
public class DuplicateResourceException extends Exception {

    private Resource mOne;
    private Resource mTwo;

    DuplicateResourceException(Resource one, Resource two) {
super(String.format("Duplicate resources: %1s:%2s, %3s:%4s",
one.getSource().getFile().getAbsolutePath(), one.getKey(),
two.getSource().getFile().getAbsolutePath(), two.getKey()));
//Synthetic comment -- @@ -32,11 +32,11 @@
mTwo = two;
}

    public Resource getOne() {
return mOne;
}

    public Resource getTwo() {
return mTwo;
}
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/NewResourceSet.java b/builder/src/main/java/com/android/builder/resources/NewResourceSet.java
new file mode 100644
//Synthetic comment -- index 0000000..b0d6fa3

//Synthetic comment -- @@ -0,0 +1,144 @@








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/Resource.java b/builder/src/main/java/com/android/builder/resources/Resource.java
deleted file mode 100644
//Synthetic comment -- index 65f476c..0000000

//Synthetic comment -- @@ -1,245 +0,0 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.builder.resources;

import com.android.annotations.NonNull;
import com.android.resources.ResourceType;
import org.w3c.dom.Node;

/**
 * A resource.
 *
 * This includes the name, type, source file as a {@link ResourceFile} and an optional {@link Node}
 * in case of a resource coming from a value file.
 *
 */
class Resource {

    private static final int MASK_TOUCHED = 0x01;
    private static final int MASK_REMOVED = 0x02;
    private static final int MASK_WRITTEN = 0x10;

    private final String mName;
    private final ResourceType mType;

    private Node mValue;
    private ResourceFile mSource;

    /**
     * The status of the Resource. It's a bit mask as opposed to an enum
     * to differentiate removed and removed+written
     */
    private int mStatus = 0;

    /**
     * Constructs the object with a name, type and optional value.
     *
     * Note that the object is not fully usable as-is. It must be added to a ResourceFile first.
     *
     * @param name the name of the resource
     * @param type the type of the resource
     * @param value an optional Node that represents the resource value.
     */
    Resource(@NonNull String name, @NonNull ResourceType type, Node value) {
        mName = name;
        mType = type;
        mValue = value;
    }

    /**
     * Returns the name of the resource.
     * @return the name.
     */
    @NonNull
    public String getName() {
        return mName;
    }

    /**
     * Returns the type of the resource.
     * @return the type.
     */
    @NonNull
    public ResourceType getType() {
        return mType;
    }

    /**
     * Returns the ResourceFile the resource is coming from. Can be null.
     * @return the resource file.
     */
    public ResourceFile getSource() {
        return mSource;
    }

    /**
     * Returns the optional value of the resource. Can be null
     * @return the value or null.
     */
    public Node getValue() {
        return mValue;
    }

    /**
     * Sets the value of the resource and set its state to TOUCHED.
     * @param from the resource to copy the value from.
     */
    void setValue(Resource from) {
        mValue = from.mValue;
        setTouched();
    }


    /**
     * Sets the ResourceFile
     * @param sourceFile the ResourceFile
     */
    void setSource(ResourceFile sourceFile) {
        mSource = sourceFile;
    }

    /**
     * Resets the state of the resource be WRITTEN. All other states are removed.
     * @return this
     *
     * @see #isWritten()
     */
    Resource resetStatusToWritten() {
        mStatus = MASK_WRITTEN;
        return this;
    }

    /**
     * Sets the resource be WRITTEN. Other states are kept.
     * @return this
     *
     * @see #isWritten()
     */
    Resource setWritten() {
        mStatus |= MASK_WRITTEN;
        return this;
    }

    /**
     * Sets the resource be REMOVED. Other states are kept.
     * @return this
     *
     * @see #isRemoved()
     */
    Resource setRemoved() {
        mStatus |= MASK_REMOVED;
        return this;
    }

    /**
     * Sets the resource be TOUCHED. Other states are kept.
     * @return this
     *
     * @see #isTouched()
     */
    Resource setTouched() {
        mStatus |= MASK_TOUCHED;
        return this;
    }

    /**
     * Returns whether the resource is REMOVED
     * @return true if removed
     */
    boolean isRemoved() {
        return (mStatus & MASK_REMOVED) != 0;
    }

    /**
     * Returns whether the resource is TOUCHED
     * @return true if touched
     */
    boolean isTouched() {
        return (mStatus & MASK_TOUCHED) != 0;
    }

    /**
     * Returns whether the resource is WRITTEN
     * @return true if written
     */
    boolean isWritten() {
        return (mStatus & MASK_WRITTEN) != 0;
    }

    /**
     * Returns a key for this resource. They key uniquely identifies this resource by combining
     * resource type, qualifiers, and name.
     *
     * If the resource has not been added to a {@link ResourceFile}, this will throw an
     * {@link IllegalStateException}.
     *
     * @return the key for this resource.
     *
     * @throws IllegalStateException if the resource is not added to a ResourceFile
     */
    String getKey() {
        if (mSource == null) {
            throw new IllegalStateException(
                    "Resource.getKey called on object with no ResourceFile: " + this);
        }
        String qualifiers = mSource.getQualifiers();
        if (qualifiers != null && qualifiers.length() > 0) {
            return mType.getName() + "-" + qualifiers + "/" + mName;
        }

        return mType.getName() + "/" + mName;
    }

    /**
     * Compares the Resource {@link #getValue()} together and returns true if they are the same.
     * @param resource The Resource object to compare to.
     * @return true if equal
     */
    public boolean compareValueWith(Resource resource) {
        if (mValue != null && resource.mValue != null) {
            return NodeUtils.compareElementNode(mValue, resource.mValue);
        }

        return mValue == resource.mValue;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "mName='" + mName + '\'' +
                ", mType=" + mType +
                ", mStatus=" + mStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        return mName.equals(resource.mName) && mType == resource.mType;
    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + mType.hashCode();
        return result;
    }
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceFile.java b/builder/src/main/java/com/android/builder/resources/ResourceFile.java
//Synthetic comment -- index 42ba9bc..623abe7 100644

//Synthetic comment -- @@ -18,38 +18,31 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* Represents a file in a resource folders.
*
* It contains a link to the {@link File}, the qualifier string (which is the name of the folder
 * after the first '-' character), a list of {@link Resource} and a type.
*
* The type of the file is based on whether the file is located in a values folder (FileType.MULTI)
* or in another folder (FileType.SINGLE).
*/
class ResourceFile {

    static enum FileType {
        SINGLE, MULTI
    }

    private final FileType mType;
    private final File mFile;
    private final Map<String, Resource> mItems;
private final String mQualifiers;

/**
* Creates a resource file with a single resource item.
*
     * The source file is set on the item with {@link Resource#setSource(ResourceFile)}
*
* The type of the ResourceFile will by {@link FileType#SINGLE}.
*
//Synthetic comment -- @@ -57,19 +50,15 @@
* @param item the resource item
* @param qualifiers the qualifiers.
*/
    ResourceFile(@NonNull File file, @NonNull Resource item, @Nullable String qualifiers) {
        mType = FileType.SINGLE;
        mFile = file;
mQualifiers = qualifiers;

        item.setSource(this);
        mItems = Collections.singletonMap(item.getKey(), item);
}

/**
* Creates a resource file with a list of resource items.
*
     * The source file is set on the items with {@link Resource#setSource(ResourceFile)}
*
* The type of the ResourceFile will by {@link FileType#MULTI}.
*
//Synthetic comment -- @@ -77,52 +66,19 @@
* @param items the resource items
* @param qualifiers the qualifiers.
*/
    ResourceFile(@NonNull File file, @NonNull List<Resource> items, @Nullable String qualifiers) {
        mType = FileType.MULTI;
        mFile = file;
mQualifiers = qualifiers;

        mItems = Maps.newHashMapWithExpectedSize(items.size());
        for (Resource item : items) {
            item.setSource(this);
            mItems.put(item.getKey(), item);
        }
}

    @NonNull
    FileType getType() {
        return mType;
    }

    @NonNull
    File getFile() {
        return mFile;
    }

@Nullable
String getQualifiers() {
return mQualifiers;
}

    Resource getItem() {
        assert mItems.size() == 1;
        return mItems.values().iterator().next();
    }

    @NonNull
    Collection<Resource> getItems() {
        return mItems.values();
    }

    @NonNull
    Map<String, Resource> getItemMap() {
        return mItems;
    }

    void addItems(Collection<Resource> items) {
        for (Resource item : items) {
            mItems.put(item.getKey(), item);
            item.setSource(this);
        }
}
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceItem.java b/builder/src/main/java/com/android/builder/resources/ResourceItem.java
new file mode 100644
//Synthetic comment -- index 0000000..47c898c

//Synthetic comment -- @@ -0,0 +1,156 @@








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceMap.java b/builder/src/main/java/com/android/builder/resources/ResourceMap.java
//Synthetic comment -- index cd1f5d9..022211d 100644

//Synthetic comment -- @@ -20,8 +20,8 @@
import com.google.common.collect.ListMultimap;

/**
 * A Resource Map able to provide a {@link ListMultimap} of Resources where the keys are
 * the value returned by {@link Resource#getKey()}
*/
interface ResourceMap {

//Synthetic comment -- @@ -33,9 +33,9 @@

/**
* a Multi map of (key, resource) where key is the result of
     * {@link com.android.builder.resources.Resource#getKey()}
* @return a non null map
*/
@NonNull
    ListMultimap<String, Resource> getResourceMap();
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceMerger.java b/builder/src/main/java/com/android/builder/resources/ResourceMerger.java
//Synthetic comment -- index 647beb2..843cb67 100755

//Synthetic comment -- @@ -114,7 +114,7 @@
Set<String> keys = Sets.newHashSet();

for (ResourceSet resourceSet : mResourceSets) {
            ListMultimap<String, Resource> map = resourceSet.getResourceMap();
keys.addAll(map.keySet());
}

//Synthetic comment -- @@ -129,14 +129,14 @@
*/
@NonNull
@Override
    public ListMultimap<String, Resource> getResourceMap() {
// put all the sets in a multimap. The result is that for each key,
// there is a sorted list of items from all the layers, including removed ones.
        ListMultimap<String, Resource> fullItemMultimap = ArrayListMultimap.create();

for (ResourceSet resourceSet : mResourceSets) {
            ListMultimap<String, Resource> map = resourceSet.getResourceMap();
            for (Map.Entry<String, Collection<Resource>> entry : map.asMap().entrySet()) {
fullItemMultimap.putAll(entry.getKey(), entry.getValue());
}
}
//Synthetic comment -- @@ -167,13 +167,13 @@
for (ResourceSet resourceSet : mResourceSets) {
// quick check on duplicates in the resource set.
resourceSet.checkItems();
            ListMultimap<String, Resource> map = resourceSet.getResourceMap();
resourceKeys.addAll(map.keySet());
}

// map of XML values files to write after parsing all the files.
// the key is the qualifier.
        ListMultimap<String, Resource> valuesResMap = ArrayListMultimap.create();
// set of qualifier that was a previously written resource disappear. This is to keep track
// of which file to write if no other resources are touched.
Set<String> qualifierWithDeletedValues = Sets.newHashSet();
//Synthetic comment -- @@ -182,8 +182,8 @@
for (String resourceKey : resourceKeys) {
// for each resource, look in the resource sets, starting from the end of the list.

            Resource previouslyWritten = null;
            Resource toWrite = null;

/*
* We are looking for what to write/delete: the last non deleted item, and the
//Synthetic comment -- @@ -194,9 +194,9 @@
ResourceSet resourceSet = mResourceSets.get(i);

// look for the resource key in the set
                ListMultimap<String, Resource> resourceMap = resourceSet.getResourceMap();

                List<Resource> resources = resourceMap.get(resourceKey);
if (resources.isEmpty()) {
continue;
}
//Synthetic comment -- @@ -205,7 +205,7 @@
// More than one deleted means there was more than one which isn't possible
// More than one touched means there is more than one and this isn't possible.
for (int ii = resources.size() - 1 ; ii >= 0 ; ii--) {
                    Resource resource = resources.get(ii);

if (resource.isWritten()) {
assert previouslyWritten == null;
//Synthetic comment -- @@ -285,11 +285,11 @@
boolean mustWriteFile = qualifierWithDeletedValues.remove(key);

// get the list of items to write
            Collection<Resource> items = valuesResMap.get(key);

// now check if we really have to write it
if (!mustWriteFile) {
                for (Resource item : items) {
if (item.isTouched()) {
mustWriteFile = true;
break;
//Synthetic comment -- @@ -319,7 +319,7 @@
Node rootNode = document.createElement(TAG_RESOURCES);
document.appendChild(rootNode);

                    for (Resource item : items) {
Node adoptedNode = NodeUtils.adoptNode(document, item.getValue());
rootNode.appendChild(adoptedNode);
}
//Synthetic comment -- @@ -378,8 +378,8 @@
}

/**
     * Writes a given Resource to a given root res folder.
     * If the Resource is to be written in a "Values" folder, then it is added to a map instead.
*
* @param rootFolder the root res folder
* @param valuesResMap a map of existing values-type resources where the key is the qualifiers
//Synthetic comment -- @@ -390,8 +390,8 @@
* @throws IOException
*/
private void writeResource(@NonNull final File rootFolder,
                               @NonNull ListMultimap<String, Resource> valuesResMap,
                               @NonNull final Resource resource,
@NonNull WaitableExecutor executor,
@Nullable final AaptRunner aaptRunner) throws IOException {
ResourceFile.FileType type = resource.getSource().getType();
//Synthetic comment -- @@ -554,20 +554,20 @@
/**
* Sets all existing resources to have their state be WRITTEN.
*
     * @see com.android.builder.resources.Resource#isWritten()
*/
private void setResourcesToWritten() {
        ListMultimap<String, Resource> resources = ArrayListMultimap.create();

for (ResourceSet resourceSet : mResourceSets) {
            ListMultimap<String, Resource> map = resourceSet.getResourceMap();
            for (Map.Entry<String, Collection<Resource>> entry : map.asMap().entrySet()) {
resources.putAll(entry.getKey(), entry.getValue());
}
}

for (String key : resources.keySet()) {
            List<Resource> resourceList = resources.get(key);
resourceList.get(resourceList.size() - 1).resetStatusToWritten();
}
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceSet.java b/builder/src/main/java/com/android/builder/resources/ResourceSet.java
//Synthetic comment -- index d537042..0ee5c8f 100755

//Synthetic comment -- @@ -42,7 +42,7 @@
* Represents a set of resources.
*
* The resources can be coming from multiple source folders. Duplicates are detected (either
 * from the same source folder -- same resource in values files -- or across the source folders.
*
* Each source folders is considered to be at the same level. To use overlays, a
* {@link ResourceMerger} must be used.
//Synthetic comment -- @@ -74,11 +74,11 @@
private final List<File> mSourceFiles = Lists.newArrayList();

/**
     * The key is the {@link com.android.builder.resources.Resource#getKey()}.
* This is a multimap to support moving a resource from one file to another (values file)
* during incremental update.
*/
    private final ListMultimap<String, Resource> mItems = ArrayListMultimap.create();

/**
* Map of source files to ResourceFiles. This is a multimap because the key is the source
//Synthetic comment -- @@ -86,6 +86,7 @@
* File for the resource file itself.
*/
private final ListMultimap<File, ResourceFile> mSourceFileToResourceFilesMap = ArrayListMultimap.create();
/**
* Map from a File to its ResourceFile.
*/
//Synthetic comment -- @@ -169,7 +170,7 @@
@Override
public int size() {
// returns the number of keys, not the size of the multimap which would include duplicate
        // Resource objects.
return mItems.keySet().size();
}

//Synthetic comment -- @@ -189,7 +190,7 @@
*/
@NonNull
@Override
    public ListMultimap<String, Resource> getResourceMap() {
return mItems;
}

//Synthetic comment -- @@ -249,12 +250,12 @@
resourceFile.getQualifiers());

if (resourceFile.getType() == ResourceFile.FileType.MULTI) {
                    for (Resource item : resourceFile.getItems()) {
Node adoptedNode = NodeUtils.adoptNode(document, item.getValue());
fileNode.appendChild(adoptedNode);
}
} else {
                    Resource item = resourceFile.getItem();
NodeUtils.addAttribute(document, fileNode, null, ATTR_TYPE,
item.getType().getName());
NodeUtils.addAttribute(document, fileNode, null, ATTR_NAME, item.getName());
//Synthetic comment -- @@ -319,7 +320,7 @@
Attr typeAttr = (Attr) fileNode.getAttributes().getNamedItem(ATTR_TYPE);
if (typeAttr == null) {
// multi res file
                    List<Resource> resourceList = Lists.newArrayList();

// loop on each node that represent a resource
NodeList resNodes = fileNode.getChildNodes();
//Synthetic comment -- @@ -330,7 +331,7 @@
continue;
}

                        Resource r = ValueResourceParser.getResource(resNode);
if (r != null) {
resourceList.add(r);
}
//Synthetic comment -- @@ -340,7 +341,7 @@
resourceList, qualifier);
resourceSet.addResourceFile(sourceFolder, resourceFile);

                    for (Resource item : resourceList) {
resourceSet.mItems.put(item.getKey(), item);
}

//Synthetic comment -- @@ -356,7 +357,7 @@
continue;
}

                    Resource item = new Resource(nameAttr.getValue(), type, null);
ResourceFile resourceFile = new ResourceFile(new File(pathAttr.getValue()),
item, qualifier);

//Synthetic comment -- @@ -446,13 +447,13 @@
continue;
}
if (folderData.type != null) {
                    Resource item = handleSingleResFile(sourceFolder,
folderData.qualifiers, folderData.type, file);
item.setTouched();
} else {
                    Collection<Resource> items = handleMultiResFile(sourceFolder,
folderData.qualifiers, file);
                    for (Resource item : items) {
item.setTouched();
}
}
//Synthetic comment -- @@ -461,16 +462,16 @@
}

/**
     * Handles a single resource file (ie not located in "values") and create a Resource from it.
*
* @param sourceFolder the top res folder for the file
* @param qualifiers the qualifiers associated with the file
* @param type the ResourceType read from the parent folder name
* @param file the single resource file
     * @return a Resource object
*/
@NonNull
    private Resource handleSingleResFile(File sourceFolder, String qualifiers,
ResourceType type, File file) {
int pos;// get the resource name based on the filename
String name = file.getName();
//Synthetic comment -- @@ -479,7 +480,7 @@
name = name.substring(0, pos);
}

        Resource item = new Resource(name, type, null);
ResourceFile resourceFile = new ResourceFile(file, item, qualifiers);
addResourceFile(sourceFolder, resourceFile);

//Synthetic comment -- @@ -489,25 +490,25 @@
}

/**
     * Handles a multi res file (in a "values" folder) and create Resource object from it.
*
* @param sourceFolder the top res folder for the file
* @param qualifiers the qualifiers associated with the file
* @param file the single resource file
     * @return a list of created Resource objects.
*
* @throws IOException
*/
@NonNull
    private Collection<Resource> handleMultiResFile(File sourceFolder, String qualifiers, File file)
throws IOException {
ValueResourceParser parser = new ValueResourceParser(file);
        List<Resource> items = parser.parseFile();

ResourceFile resourceFile = new ResourceFile(file, items, qualifiers);
addResourceFile(sourceFolder, resourceFile);

        for (Resource item : items) {
mItems.put(item.getKey(), item);
}

//Synthetic comment -- @@ -532,12 +533,12 @@
*/
void checkItems() throws DuplicateResourceException {
// check a list for duplicate, ignoring removed items.
        for (Map.Entry<String, Collection<Resource>> entry : mItems.asMap().entrySet()) {
            Collection<Resource> items = entry.getValue();

// there can be several version of the same key if some are "removed"
            Resource lastItem = null;
            for (Resource item : items) {
if (!item.isRemoved()) {
if (lastItem == null) {
lastItem = item;
//Synthetic comment -- @@ -570,20 +571,20 @@
} else {
// multi res. Need to parse the file and compare the items one by one.
ValueResourceParser parser = new ValueResourceParser(changedFile);
                    List<Resource> parsedItems = parser.parseFile();

                    Map<String, Resource> oldItems = Maps.newHashMap(resourceFile.getItemMap());

                    Map<String, Resource> newItems  = Maps.newHashMap();

// create a fake ResourceFile to be able to call resource.getKey();
// It's ok because we never use this instance anyway.
ResourceFile fakeResourceFile = new ResourceFile(changedFile, parsedItems,
resourceFile.getQualifiers());

                    for (Resource newItem : parsedItems) {
String newKey = newItem.getKey();
                        Resource oldItem = oldItems.get(newKey);

if (oldItem == null) {
// this is a new item
//Synthetic comment -- @@ -605,13 +606,13 @@

// at this point oldItems is left with the deleted items.
// just update their status to removed.
                    for (Resource deletedItem : oldItems.values()) {
deletedItem.setRemoved();
}

// Now we need to add the new items to the resource file and the main map
resourceFile.addItems(newItems.values());
                    for (Map.Entry<String, Resource> entry : newItems.entrySet()) {
mItems.put(entry.getKey(), entry.getValue());
}
}
//Synthetic comment -- @@ -621,13 +622,13 @@
folderData = getFolderData(changedFile.getParentFile());

if (folderData.type != null) {
                    Resource item = handleSingleResFile(sourceFolder, folderData.qualifiers,
folderData.type, changedFile);
item.setTouched();
} else {
                    Collection<Resource> items = handleMultiResFile(sourceFolder,
folderData.qualifiers, changedFile);
                    for (Resource item : items) {
item.setTouched();
}
}
//Synthetic comment -- @@ -637,7 +638,7 @@
resourceFile = mResourceFileMap.get(changedFile);

// flag all resource items are removed
                for (Resource item : resourceFile.getItems()) {
item.setRemoved();
}
return true;








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ValueResourceParser.java b/builder/src/main/java/com/android/builder/resources/ValueResourceParser.java
//Synthetic comment -- index 9adda0a..f7d5f53 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
/**
* Parser for "values" files.
*
 * This parses the file and returns a list of {@link Resource} object.
*/
class ValueResourceParser {

//Synthetic comment -- @@ -59,13 +59,13 @@
}

/**
     * Parses the file and returns a list of {@link Resource} objects.
* @return a list of resources.
*
* @throws IOException
*/
@NonNull
    List<Resource> parseFile() throws IOException {
Document document = parseDocument(mFile);

// get the root node
//Synthetic comment -- @@ -75,7 +75,7 @@
}
NodeList nodes = rootNode.getChildNodes();

        List<Resource> resources = Lists.newArrayListWithExpectedSize(nodes.getLength());

for (int i = 0, n = nodes.getLength(); i < n; i++) {
Node node = nodes.item(i);
//Synthetic comment -- @@ -84,7 +84,7 @@
continue;
}

            Resource resource = getResource(node);
if (resource != null) {
resources.add(resource);
}
//Synthetic comment -- @@ -94,23 +94,23 @@
}

/**
     * Returns a new Resource object for a given node.
* @param node the node representing the resource.
     * @return a Resource object or null.
*/
    static Resource getResource(Node node) {
ResourceType type = getType(node);
String name = getName(node);

if (type != null && name != null) {
            return new Resource(name, type, node);
}

return null;
}

/**
     * Returns the type of the Resource based on a node's attributes.
* @param node the node
* @return the ResourceType or null if it could not be inferred.
*/








//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/resources/BaseTestCase.java b/builder/src/test/java/com/android/builder/resources/BaseTestCase.java
//Synthetic comment -- index a18d69c..0643985 100644

//Synthetic comment -- @@ -24,10 +24,10 @@
public abstract class BaseTestCase extends TestCase {

protected void verifyResourceExists(ResourceMap resourceMap, String... resourceKeys) {
        ListMultimap<String, Resource> map = resourceMap.getResourceMap();

for (String resKey : resourceKeys) {
            List<Resource> resources = map.get(resKey);
assertTrue("resource '" + resKey + "' is missing!", resources.size() > 0);
}
}
//Synthetic comment -- @@ -48,11 +48,11 @@
assertEquals(resourceMap1.size(), resourceMap2.size());

// compare the resources are all the same
        ListMultimap<String, Resource> map1 = resourceMap1.getResourceMap();
        ListMultimap<String, Resource> map2 = resourceMap2.getResourceMap();
for (String key : map1.keySet()) {
            List<Resource> items1 = map1.get(key);
            List<Resource> items2 = map2.get(key);
if (fullCompare) {
assertEquals("Wrong size for " + key, items1.size(), items2.size());
} else {








//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/resources/ResourceMergerTest.java b/builder/src/test/java/com/android/builder/resources/ResourceMergerTest.java
//Synthetic comment -- index f056413..644ef97 100755

//Synthetic comment -- @@ -86,13 +86,13 @@

public void testReplacedLayout() throws Exception {
ResourceMerger merger = getResourceMerger();
        ListMultimap<String, Resource> mergedMap = merger.getResourceMap();

        List<Resource> values = mergedMap.get("layout/main");

// the overlay means there's 2 versions of this resource.
assertEquals(2, values.size());
        Resource mainLayout = values.get(1);

ResourceFile sourceFile = mainLayout.getSource();
assertTrue(sourceFile.getFile().getAbsolutePath()
//Synthetic comment -- @@ -101,14 +101,14 @@

public void testReplacedAlias() throws Exception {
ResourceMerger merger = getResourceMerger();
        ListMultimap<String, Resource> mergedMap = merger.getResourceMap();


        List<Resource> values = mergedMap.get("layout/alias_replaced_by_file");

// the overlay means there's 2 versions of this resource.
assertEquals(2, values.size());
        Resource layout = values.get(1);

// since it's replaced by a file, there's no node.
assertNull(layout.getValue());
//Synthetic comment -- @@ -116,13 +116,13 @@

public void testReplacedFile() throws Exception {
ResourceMerger merger = getResourceMerger();
        ListMultimap<String, Resource> mergedMap = merger.getResourceMap();

        List<Resource> values = mergedMap.get("layout/file_replaced_by_alias");

// the overlay means there's 2 versions of this resource.
assertEquals(2, values.size());
        Resource layout = values.get(1);

// since it's replaced by a file, there's no node.
assertNotNull(layout.getValue());
//Synthetic comment -- @@ -138,7 +138,7 @@
writtenSet.loadFromFiles();

// compare the two maps, but not using the full map as the set loaded from the output
        // won't contains all versions of each Resource item.
compareResourceMaps(merger, writtenSet, false /*full compare*/);
}

//Synthetic comment -- @@ -225,40 +225,40 @@
resourceMerger.validateResourceSets();

// check the content.
        ListMultimap<String, Resource> mergedMap = resourceMerger.getResourceMap();

// check unchanged file is WRITTEN
        List<Resource> drawableUntouched = mergedMap.get("drawable/untouched");
assertEquals(1, drawableUntouched.size());
assertTrue(drawableUntouched.get(0).isWritten());
assertFalse(drawableUntouched.get(0).isTouched());
assertFalse(drawableUntouched.get(0).isRemoved());

// check replaced file is TOUCHED
        List<Resource> drawableTouched = mergedMap.get("drawable/touched");
assertEquals(1, drawableTouched.size());
assertTrue(drawableTouched.get(0).isWritten());
assertTrue(drawableTouched.get(0).isTouched());
assertFalse(drawableTouched.get(0).isRemoved());

// check removed file is REMOVED
        List<Resource> drawableRemoved = mergedMap.get("drawable/removed");
assertEquals(1, drawableRemoved.size());
assertTrue(drawableRemoved.get(0).isWritten());
assertTrue(drawableRemoved.get(0).isRemoved());

// check new overlay: two objects, last one is TOUCHED
        List<Resource> drawableNewOverlay = mergedMap.get("drawable/new_overlay");
assertEquals(2, drawableNewOverlay.size());
        Resource newOverlay = drawableNewOverlay.get(1);
assertEquals(overlayDrawableNewOverlay, newOverlay.getSource().getFile());
assertFalse(newOverlay.isWritten());
assertTrue(newOverlay.isTouched());

// check new alternate: one objects, last one is TOUCHED
        List<Resource> drawableHdpiNewAlternate = mergedMap.get("drawable-hdpi/new_alternate");
assertEquals(1, drawableHdpiNewAlternate.size());
        Resource newAlternate = drawableHdpiNewAlternate.get(0);
assertEquals(overlayDrawableHdpiNewAlternate, newAlternate.getSource().getFile());
assertFalse(newAlternate.isWritten());
assertTrue(newAlternate.isTouched());
//Synthetic comment -- @@ -325,24 +325,24 @@
resourceMerger.validateResourceSets();

// check the content.
        ListMultimap<String, Resource> mergedMap = resourceMerger.getResourceMap();

// check unchanged string is WRITTEN
        List<Resource> valuesUntouched = mergedMap.get("string/untouched");
assertEquals(1, valuesUntouched.size());
assertTrue(valuesUntouched.get(0).isWritten());
assertFalse(valuesUntouched.get(0).isTouched());
assertFalse(valuesUntouched.get(0).isRemoved());

// check replaced file is TOUCHED
        List<Resource> valuesTouched = mergedMap.get("string/touched");
assertEquals(1, valuesTouched.size());
assertTrue(valuesTouched.get(0).isWritten());
assertTrue(valuesTouched.get(0).isTouched());
assertFalse(valuesTouched.get(0).isRemoved());

// check removed file is REMOVED
        List<Resource> valuesRemoved = mergedMap.get("string/removed");
assertEquals(1, valuesRemoved.size());
assertTrue(valuesRemoved.get(0).isWritten());
assertTrue(valuesRemoved.get(0).isRemoved());
//Synthetic comment -- @@ -353,16 +353,16 @@
assertTrue(valuesRemoved.get(0).isRemoved());

// check new overlay: two objects, last one is TOUCHED
        List<Resource> valuesNewOverlay = mergedMap.get("string/new_overlay");
assertEquals(2, valuesNewOverlay.size());
        Resource newOverlay = valuesNewOverlay.get(1);
assertFalse(newOverlay.isWritten());
assertTrue(newOverlay.isTouched());

// check new alternate: one objects, last one is TOUCHED
        List<Resource> valuesFrNewAlternate = mergedMap.get("string-fr/new_alternate");
assertEquals(1, valuesFrNewAlternate.size());
        Resource newAlternate = valuesFrNewAlternate.get(0);
assertFalse(newAlternate.isWritten());
assertTrue(newAlternate.isTouched());

//Synthetic comment -- @@ -419,17 +419,17 @@
resourceMerger.validateResourceSets();

// check the content.
        ListMultimap<String, Resource> mergedMap = resourceMerger.getResourceMap();

// check unchanged string is WRITTEN
        List<Resource> valuesUntouched = mergedMap.get("string/untouched");
assertEquals(1, valuesUntouched.size());
assertTrue(valuesUntouched.get(0).isWritten());
assertFalse(valuesUntouched.get(0).isTouched());
assertFalse(valuesUntouched.get(0).isRemoved());

// check removed_overlay is present twice.
        List<Resource> valuesRemovedOverlay = mergedMap.get("string/removed_overlay");
assertEquals(2, valuesRemovedOverlay.size());
// first is untouched
assertFalse(valuesRemovedOverlay.get(0).isWritten());
//Synthetic comment -- @@ -487,20 +487,20 @@
resourceMerger.validateResourceSets();

// check the content.
        ListMultimap<String, Resource> mergedMap = resourceMerger.getResourceMap();

// check layout/main is unchanged
        List<Resource> layoutMain = mergedMap.get("layout/main");
assertEquals(1, layoutMain.size());
assertTrue(layoutMain.get(0).isWritten());
assertFalse(layoutMain.get(0).isTouched());
assertFalse(layoutMain.get(0).isRemoved());

// check file_replaced_by_alias has 2 version, 2nd is TOUCHED, and contains a Node
        List<Resource> layoutReplacedByAlias = mergedMap.get("layout/file_replaced_by_alias");
assertEquals(2, layoutReplacedByAlias.size());
// 1st one is removed version, as it already existed in the item multimap
        Resource replacedByAlias = layoutReplacedByAlias.get(0);
assertTrue(replacedByAlias.isWritten());
assertFalse(replacedByAlias.isTouched());
assertTrue(replacedByAlias.isRemoved());
//Synthetic comment -- @@ -515,10 +515,10 @@
assertEquals("values.xml", replacedByAlias.getSource().getFile().getName());

// check alias_replaced_by_file has 2 version, 2nd is TOUCHED, and contains a Node
        List<Resource> layoutReplacedByFile = mergedMap.get("layout/alias_replaced_by_file");
// 1st one is removed version, as it already existed in the item multimap
assertEquals(2, layoutReplacedByFile.size());
        Resource replacedByFile = layoutReplacedByFile.get(0);
assertTrue(replacedByFile.isWritten());
assertFalse(replacedByFile.isTouched());
assertTrue(replacedByFile.isRemoved());








//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/resources/ValueResourceParserTest.java b/builder/src/test/java/com/android/builder/resources/ValueResourceParserTest.java
//Synthetic comment -- index 96dabfd..9cd84d7 100644

//Synthetic comment -- @@ -28,18 +28,18 @@
*/
public class ValueResourceParserTest extends BaseTestCase {

    private static List<Resource> sResources = null;

public void testParsedResourcesByCount() throws Exception {
        List<Resource> resources = getParsedResources();

assertEquals(18, resources.size());
}

public void testParsedResourcesByName() throws Exception {
        List<Resource> resources = getParsedResources();
        Map<String, Resource> resourceMap = Maps.newHashMapWithExpectedSize(resources.size());
        for (Resource item : resources) {
resourceMap.put(item.getKey(), item);
}

//Synthetic comment -- @@ -68,7 +68,7 @@
}
}

    private static List<Resource> getParsedResources() throws IOException {
if (sResources == null) {
File root = TestUtils.getRoot("baseResourceSet");
File values = new File(root, "values");
//Synthetic comment -- @@ -77,7 +77,7 @@
ValueResourceParser parser = new ValueResourceParser(valuesXml);
sResources = parser.parseFile();

            // create a fake resource file to allow calling Resource.getKey()
new ResourceFile(valuesXml, sResources, "");
}








