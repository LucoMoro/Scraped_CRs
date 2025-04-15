/*Add multi assets folder support + assets in libraries.

Change-Id:I75d0a95aa330ab9ce25132800fe9ba2e57710b87*/




//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/Asset.java b/builder/src/main/java/com/android/builder/resources/Asset.java
new file mode 100644
//Synthetic comment -- index 0000000..37972c2

//Synthetic comment -- @@ -0,0 +1,39 @@
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

/**
 * An asset.
 *
 * This includes the name and source file as a {@link AssetFile}.
 *
 */
class Asset extends DataItem<AssetFile> {

    /**
     * Constructs the object with a name, type and optional value.
     *
     * Note that the object is not fully usable as-is. It must be added to a ResourceFile first.
     *
     * @param name the name of the resource
     */
    Asset(@NonNull String name) {
        super(name);
    }
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/AssetFile.java b/builder/src/main/java/com/android/builder/resources/AssetFile.java
new file mode 100644
//Synthetic comment -- index 0000000..5f0e4ec

//Synthetic comment -- @@ -0,0 +1,39 @@
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

import java.io.File;

/**
 * Represents a file in an asset folder.
 */
class AssetFile extends DataFile<Asset> {

    /**
     * Creates a resource file with a single resource item.
     *
     * The source file is set on the item with {@link Asset#setSource(DataFile)}
     *
     * @param file the File
     * @param item the resource item
     */
    AssetFile(@NonNull File file, @NonNull Asset item) {
        super(file, item);
    }
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/AssetSet.java b/builder/src/main/java/com/android/builder/resources/AssetSet.java
new file mode 100644
//Synthetic comment -- index 0000000..7c1e91a

//Synthetic comment -- @@ -0,0 +1,74 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
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

import java.io.File;
import java.io.IOException;

/**
 * Represents a set of Assets.
 */
public class AssetSet extends DataSet<Asset, AssetFile> {

    /**
     * Creates an asset set with a given configName. The name is used to identify the set
     * across sessions.
     *
     * @param configName the name of the config this set is associated with.
     */
    public AssetSet(String configName) {
        super(configName);
    }

    @Override
    protected DataSet<Asset, AssetFile> createSet(String name) {
        return new AssetSet(name);
    }

    @Override
    protected AssetFile createFileAndItems(File file) {
        int pos;// get the resource name based on the filename
        String name = file.getName();
        pos = name.indexOf('.');
        if (pos >= 0) {
            name = name.substring(0, pos);
        }

        return new AssetFile(file, new Asset(name));
    }

    @Override
    protected boolean isValidSourceFile(File sourceFolder, File file) {
        // valid files are right under the source folder
        return file.getParentFile().equals(sourceFolder);
    }

    @Override
    protected void readSourceFolder(File sourceFolder) throws DuplicateDataException, IOException {
        // get the files
        File[] files = sourceFolder.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (!file.isFile() || !checkFileForAndroidRes(file)) {
                    continue;
                }

                handleNewFile(sourceFolder, file);
            }
        }
    }
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/DataFile.java b/builder/src/main/java/com/android/builder/resources/DataFile.java
new file mode 100644
//Synthetic comment -- index 0000000..0f68c61

//Synthetic comment -- @@ -0,0 +1,120 @@
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
import com.google.common.collect.Maps;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a data file.
 *
 * It contains a link to its {@link java.io.File}, and the {@DataItem} it generates.
 *
 */
class DataFile<I extends DataItem> {

    static enum FileType {
        SINGLE, MULTI
    }

    private final FileType mType;
    private final File mFile;
    private final Map<String, I> mItems;

    /**
     * Creates a resource file with a single resource item.
     *
     * The source file is set on the item with {@link DataItem#setSource(DataFile)}
     *
     * The type of the ResourceFile will by {@link FileType#SINGLE}.
     *
     * @param file the File
     * @param item the resource item
     */
    DataFile(@NonNull File file, @NonNull I item) {
        mType = FileType.SINGLE;
        mFile = file;

        item.setSource(this);
        mItems = Collections.singletonMap(item.getKey(), item);
    }

    /**
     * Creates a resource file with a list of resource items.
     *
     * The source file is set on the items with {@link DataItem#setSource(DataFile)}
     *
     * The type of the ResourceFile will by {@link FileType#MULTI}.
     *
     * @param file the File
     * @param items the resource items
     */
    DataFile(@NonNull File file, @NonNull List<I> items) {
        mType = FileType.MULTI;
        mFile = file;

        mItems = Maps.newHashMapWithExpectedSize(items.size());
        for (I item : items) {
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

    I getItem() {
        assert mItems.size() == 1;
        return mItems.values().iterator().next();
    }

    @NonNull
    Collection<I> getItems() {
        return mItems.values();
    }

    @NonNull
    Map<String, I> getItemMap() {
        return mItems;
    }

    void addItems(Collection<I> items) {
        for (I item : items) {
            mItems.put(item.getKey(), item);
            item.setSource(this);
        }
    }

    void addExtraAttributes(Document document, Node node, String namespaceUri) {
        // nothing
    }
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/DataItem.java b/builder/src/main/java/com/android/builder/resources/DataItem.java
new file mode 100644
//Synthetic comment -- index 0000000..94eafd7

//Synthetic comment -- @@ -0,0 +1,185 @@
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Base item.
 *
 * This includes its name and source file as a {@link com.android.builder.resources.DataFile}.
 *
 */
class DataItem<F extends DataFile> {

    private static final int MASK_TOUCHED = 0x01;
    private static final int MASK_REMOVED = 0x02;
    private static final int MASK_WRITTEN = 0x10;

    private final String mName;
    private F mSource;

    /**
     * The status of the Assets. It's a bit mask as opposed to an enum
     * to differentiate removed and removed+written
     */
    private int mStatus = 0;

    /**
     * Constructs the object with a name, type and optional value.
     *
     * Note that the object is not fully usable as-is. It must be added to a ResourceFile first.
     *
     * @param name the name of the resource
     */
    DataItem(@NonNull String name) {
        mName = name;
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
     * Returns the ResourceFile the resource is coming from. Can be null.
     * @return the resource file.
     */
    public F getSource() {
        return mSource;
    }

    /**
     * Sets the ResourceFile
     * @param sourceFile the ResourceFile
     */
    void setSource(F sourceFile) {
        mSource = sourceFile;
    }

    /**
     * Resets the state of the asset be WRITTEN. All other states are removed.
     * @return this
     *
     * @see #isWritten()
     */
    DataItem resetStatusToWritten() {
        mStatus = MASK_WRITTEN;
        return this;
    }

    /**
     * Sets the asset be WRITTEN. Other states are kept.
     * @return this
     *
     * @see #isWritten()
     */
    DataItem setWritten() {
        mStatus |= MASK_WRITTEN;
        return this;
    }

    /**
     * Sets the asset be REMOVED. Other states are kept.
     * @return this
     *
     * @see #isRemoved()
     */
    DataItem setRemoved() {
        mStatus |= MASK_REMOVED;
        return this;
    }

    /**
     * Sets the asset be TOUCHED. Other states are kept.
     * @return this
     *
     * @see #isTouched()
     */
    DataItem setTouched() {
        mStatus |= MASK_TOUCHED;
        return this;
    }

    /**
     * Returns whether the asset is REMOVED
     * @return true if removed
     */
    boolean isRemoved() {
        return (mStatus & MASK_REMOVED) != 0;
    }

    /**
     * Returns whether the asset is TOUCHED
     * @return true if touched
     */
    boolean isTouched() {
        return (mStatus & MASK_TOUCHED) != 0;
    }

    /**
     * Returns whether the asset is WRITTEN
     * @return true if written
     */
    boolean isWritten() {
        return (mStatus & MASK_WRITTEN) != 0;
    }

    protected int getStatus() {
        return mStatus;
    }

    /**
     * Returns a key for this asset. They key uniquely identifies this asset. This is the name.
     *
     * @return the key for this asset.
     *
     */
    String getKey() {
        return mName;
    }

    void addExtraAttributes(Document document, Node node, String namespaceUri) {
        // nothing
    }

    Node getAdoptedNode(Document document) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataItem resource = (DataItem) o;

        return mName.equals(resource.mName);
    }

    @Override
    public int hashCode() {
        return mName.hashCode();
    }

}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/DataMap.java b/builder/src/main/java/com/android/builder/resources/DataMap.java
new file mode 100644
//Synthetic comment -- index 0000000..f8f6ce0

//Synthetic comment -- @@ -0,0 +1,41 @@
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
import com.google.common.collect.ListMultimap;

/**
 * A ResourceItem Map able to provide a {@link com.google.common.collect.ListMultimap} of Resources where the keys are
 * the value returned by {@link ResourceItem#getKey()}
 */
interface DataMap<T extends DataItem> {

    /**
     * Returns the number of resources.
     * @return the number of resources.
     */
    int size();

    /**
     * a Multi map of (key, resource) where key is the result of
     * {@link ResourceItem#getKey()}
     * @return a non null map
     */
    @NonNull
    ListMultimap<String, T> getDataMap();
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/DataSet.java b/builder/src/main/java/com/android/builder/resources/DataSet.java
new file mode 100755
//Synthetic comment -- index 0000000..cd8e468

//Synthetic comment -- @@ -0,0 +1,508 @@
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
import com.android.builder.internal.packaging.PackagingUtils;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceConstants;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Represents a set of resources.
 *
 * The resources can be coming from multiple source folders. Duplicates are detected (either
 * from the same source folder -- same resource in values files -- or across the source folders.)
 *
 * Each source folders is considered to be at the same level. To use overlays, a
 * {@link com.android.builder.resources.ResourceMerger} must be used.
 *
 * Creating the set and adding folders does not load the data.
 * The data can be loaded from the files, or from a blob which is generated by the set itself.
 *
 * Upon loading the data from the blob, the data can be updated with fresher files. Each resource
 * that is updated is flagged as such, in order to manage incremental update.
 *
 * Writing/Loading the blob is not done through this class directly, but instead through the
 * {@link com.android.builder.resources.ResourceMerger} which contains ResourceSet objects.
 */
public abstract class DataSet<I extends DataItem<F>, F extends DataFile<I>> implements SourceSet, DataMap<I> {

    private static final String NODE_SOURCE = "source";
    private static final String ATTR_CONFIG = "config";
    private static final String ATTR_PATH = "path";
    private static final String NODE_FILE = "file";
    private static final String ATTR_NAME = "name";

    private final String mConfigName;

    /**
     * List of source files. The may not have been loaded yet.
     */
    private final List<File> mSourceFiles = Lists.newArrayList();

    /**
     * The key is the {@link ResourceItem#getKey()}.
     * This is a multimap to support moving a resource from one file to another (values file)
     * during incremental update.
     */
    private final ListMultimap<String, I> mItems = ArrayListMultimap.create();

    /**
     * Map of source files to ResourceFiles. This is a multimap because the key is the source
     * file/folder, not the
     * File for the resource file itself.
     */
    private final ListMultimap<File, F> mSourceFileToResourceFilesMap = ArrayListMultimap.create();

    /**
     * Map from a File to its ResourceFile.
     */
    private final Map<File, F> mResourceFileMap = Maps.newHashMap();

    /**
     * Creates a resource set with a given configName. The name is used to identify the set
     * across sessions.
     *
     * @param configName the name of the config this set is associated with.
     */
    public DataSet(String configName) {
        mConfigName = configName;
    }

    /**
     * Adds a collection of source files.
     * @param files the source files to add.
     */
    public void addSources(Collection<File> files) {
        mSourceFiles.addAll(files);
    }

    /**
     * Adds a new source file
     * @param file the source file.
     */
    public void addSource(File file) {
        mSourceFiles.add(file);
    }

    /**
     * Get the list of source files.
     * @return the source files.
     */
    @NonNull
    @Override
    public List<File> getSourceFiles() {
        return mSourceFiles;
    }

    /**
     * Returns the config name.
     * @return the config name.
     */
    public String getConfigName() {
        return mConfigName;
    }

    /**
     * Returns a matching Source file that contains a given file.
     *
     * "contains" means that the source file/folder is the root folder
     * of this file. The folder and/or file doesn't have to exist.
     *
     * @param file the file to search for
     * @return the Source file or null if no match is found.
     */
    @Override
    public File findMatchingSourceFile(File file) {
        for (File sourceFile : mSourceFiles) {
            if (sourceFile.equals(file)) {
                return sourceFile;
            } else if (sourceFile.isDirectory()) {
                String sourcePath = sourceFile.getAbsolutePath() + File.separator;
                if (file.getAbsolutePath().startsWith(sourcePath)) {
                    return sourceFile;
                }
            }
        }

        return null;
    }

    /**
     * Returns the number of resources.
     * @return the number of resources.
     *
     * @see com.android.builder.resources.ResourceMap
     */
    @Override
    public int size() {
        // returns the number of keys, not the size of the multimap which would include duplicate
        // ResourceItem objects.
        return mItems.keySet().size();
    }

    /**
     * Returns whether the set is empty of resources.
     * @return true if the set contains no resources.
     */
    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    /**
     * Returns a map of the resources.
     * @return a map of items.
     *
     * @see com.android.builder.resources.ResourceMap
     */
    @NonNull
    @Override
    public ListMultimap<String, I> getDataMap() {
        return mItems;
    }

    /**
     * Loads the resource set from the file its source folder contains.
     *
     * All loaded resources are set to TOUCHED. This is so that after loading the resources from
     * the files, they can be written directly (since touched force them to be written).
     *
     * This also checks for duplicates resources.
     *
     * @throws DuplicateDataException
     * @throws java.io.IOException
     */
    public void loadFromFiles() throws DuplicateDataException, IOException {
        for (File file : mSourceFiles) {
            if (file.isDirectory()) {
                readSourceFolder(file);

            } else if (file.isFile()) {
                // TODO support resource bundle
            }
        }
        checkItems();
    }

    /**
     * Appends the resourceSet to a given DOM object.
     *
     * @param resourceSetNode the root node for this resource set.
     * @param document The root XML document
     */
    void appendToXml(Node resourceSetNode, Document document) {
        // add the config name attribute
        NodeUtils.addAttribute(document, resourceSetNode, null, ATTR_CONFIG, mConfigName);

        // add the source files.
        // we need to loop on the source files themselves and not the map to ensure we
        // write empty resourceSets
        for (File sourceFile : mSourceFiles) {

            // the node for the source and its path attribute
            Node sourceNode = document.createElement(NODE_SOURCE);
            resourceSetNode.appendChild(sourceNode);
            NodeUtils.addAttribute(document, sourceNode, null, ATTR_PATH,
                    sourceFile.getAbsolutePath());

            Collection<F> resourceFiles = mSourceFileToResourceFilesMap.get(sourceFile);

            for (F resourceFile : resourceFiles) {
                // the node for the file and its path and qualifiers attribute
                Node fileNode = document.createElement(NODE_FILE);
                sourceNode.appendChild(fileNode);
                NodeUtils.addAttribute(document, fileNode, null, ATTR_PATH,
                        resourceFile.getFile().getAbsolutePath());
                resourceFile.addExtraAttributes(document, fileNode, null);

                if (resourceFile.getType() == DataFile.FileType.MULTI) {
                    for (I item : resourceFile.getItems()) {
                        Node adoptedNode = item.getAdoptedNode(document);
                        if (adoptedNode != null) {
                            fileNode.appendChild(adoptedNode);
                        }
                    }
                } else {
                    I item = resourceFile.getItem();
                    NodeUtils.addAttribute(document, fileNode, null, ATTR_NAME, item.getName());
                    item.addExtraAttributes(document, fileNode, null);
                }
            }
        }
    }

    protected abstract DataSet<I, F> createSet(String name);

    /**
     * Creates a new ResourceSet from an XML node that was created with
     * {@link #appendToXml(org.w3c.dom.Node, org.w3c.dom.Document)}
     *
     * @param resourceSetNode the node to read from.
     * @return a new ResourceSet object or null.
     */
    DataSet createFromXml(Node resourceSetNode) {
        // get the config name
        Attr configNameAttr = (Attr) resourceSetNode.getAttributes().getNamedItem(ATTR_CONFIG);
        if (configNameAttr == null) {
            return null;
        }

        // create the ResourceSet that will be filled with the content of the XML.
        DataSet<I, F> resourceSet = createSet(configNameAttr.getValue());

        // loop on the source nodes
        NodeList sourceNodes = resourceSetNode.getChildNodes();
        for (int i = 0, n = sourceNodes.getLength(); i < n; i++) {
            Node sourceNode = sourceNodes.item(i);

            if (sourceNode.getNodeType() != Node.ELEMENT_NODE ||
                    !NODE_SOURCE.equals(sourceNode.getLocalName())) {
                continue;
            }

            Attr pathAttr = (Attr) sourceNode.getAttributes().getNamedItem(ATTR_PATH);
            if (pathAttr == null) {
                continue;
            }

            File sourceFolder = new File(pathAttr.getValue());
            resourceSet.mSourceFiles.add(sourceFolder);

            // now loop on the files inside the source folder.
            NodeList fileNodes = sourceNode.getChildNodes();
            for (int j = 0, m = fileNodes.getLength(); j < m; j++) {
                Node fileNode = fileNodes.item(j);

                if (fileNode.getNodeType() != Node.ELEMENT_NODE ||
                        !NODE_FILE.equals(fileNode.getLocalName())) {
                    continue;
                }

                pathAttr = (Attr) fileNode.getAttributes().getNamedItem(ATTR_PATH);
                if (pathAttr == null) {
                    continue;
                }

//                Attr qualifierAttr = (Attr) fileNode.getAttributes().getNamedItem(ATTR_QUALIFIER);
//                String qualifier = qualifierAttr != null ? qualifierAttr.getValue() : null;

                Attr typeAttr = (Attr) fileNode.getAttributes().getNamedItem(ATTR_TYPE);
                if (typeAttr == null) {
                    // multi res file
                    List<ResourceItem> resourceList = Lists.newArrayList();

                    // loop on each node that represent a resource
                    NodeList resNodes = fileNode.getChildNodes();
                    for (int iii = 0, nnn = resNodes.getLength(); iii < nnn; iii++) {
                        Node resNode = resNodes.item(iii);

                        if (resNode.getNodeType() != Node.ELEMENT_NODE) {
                            continue;
                        }

                        ResourceItem r = ValueResourceParser.getResource(resNode);
                        if (r != null) {
                            resourceList.add(r);
                        }
                    }

                    ResourceFile resourceFile = new ResourceFile(new File(pathAttr.getValue()),
                            resourceList, qualifier);
                    resourceSet.addResourceFile(sourceFolder, resourceFile);

                    for (ResourceItem item : resourceList) {
                        resourceSet.mItems.put(item.getKey(), item);
                    }

                } else {
                    // single res file
                    ResourceType type = ResourceType.getEnum(typeAttr.getValue());
                    if (type == null) {
                        continue;
                    }

                    Attr nameAttr = (Attr) fileNode.getAttributes().getNamedItem(ATTR_NAME);
                    if (nameAttr == null) {
                        continue;
                    }

                    ResourceItem item = new ResourceItem(nameAttr.getValue(), type, null);
                    ResourceFile resourceFile = new ResourceFile(new File(pathAttr.getValue()),
                            item, qualifier);

                    resourceSet.addResourceFile(sourceFolder, resourceFile);
                    resourceSet.mItems.put(item.getKey(), item);
                }
            }
        }

        return resourceSet;
    }

    /**
     * Reads the content of a resource folders and loads the resources.
     *
     * This should generate DataFile, and process them with
     * {@link #processNewDataFile(java.io.File, DataFile)}.
     *
     * @param sourceFolder the source folder to load the resources from.
     *
     * @throws com.android.builder.resources.DuplicateDataException
     * @throws java.io.IOException
     */
    protected abstract void readSourceFolder(File sourceFolder)
            throws DuplicateDataException, IOException;

    protected abstract F createFileAndItems(File file);

    /**
     * Checks for duplicate resources across all source files.
     *
     * @throws com.android.builder.resources.DuplicateDataException if a duplicated item is found.
     */
    void checkItems() throws DuplicateDataException {
        // check a list for duplicate, ignoring removed items.
        for (Map.Entry<String, Collection<I>> entry : mItems.asMap().entrySet()) {
            Collection<I> items = entry.getValue();

            // there can be several version of the same key if some are "removed"
            I lastItem = null;
            for (I item : items) {
                if (!item.isRemoved()) {
                    if (lastItem == null) {
                        lastItem = item;
                    } else {
                        throw new DuplicateDataException(item, lastItem);
                    }
                }
            }
        }
    }

    /**
     * Update the ResourceSet with a given file.
     *
     * @param sourceFolder the sourceFile containing the changedFile
     * @param changedFile The changed file
     * @param fileStatus the change state
     * @return true if the set was properly updated, false otherwise
     */
    public boolean updateWith(File sourceFolder, File changedFile, FileStatus fileStatus)
            throws IOException {
        switch (fileStatus) {
            case NEW:
                if (isValidSourceFile(sourceFolder, changedFile)) {
                    return handleNewFile(sourceFolder, changedFile);
                }
                return true;
            case CHANGED:
                return handleChangedFile(sourceFolder, changedFile);
            case REMOVED:
                F dataFile = mResourceFileMap.get(changedFile);

                // flag all resource items are removed
                for (I dataItem : dataFile.getItems()) {
                    dataItem.setRemoved();
                }
                return true;
        }

        return false;
    }

    protected abstract boolean isValidSourceFile(File sourceFolder, File changedFile);

    protected boolean handleNewFile(File sourceFolder, File file) {
        F dataFile = createFileAndItems(file);
        processNewDataFile(sourceFolder, dataFile);
        return true;
    }

    protected void processNewDataFile(File sourceFolder, F dataFile) {
        Collection<I> dataItems = dataFile.getItems();

        addResourceFile(sourceFolder, dataFile);

        for (I dataItem : dataItems) {
            mItems.put(dataItem.getKey(), dataItem);
            dataItem.setTouched();
        }
    }

    protected boolean handleChangedFile(File sourceFolder, File changedFile) {
        F dataFile = mResourceFileMap.get(changedFile);
        dataFile.getItem().setTouched();
        return true;
    }

    /**
     * Adds a new ResourceFile to this.
     *
     * @param sourceFile the parent source file.
     * @param dataFile the ResourceFile
     */
    private void addResourceFile(File sourceFile, F dataFile) {
        mSourceFileToResourceFilesMap.put(sourceFile, dataFile);
        mResourceFileMap.put(dataFile.getFile(), dataFile);
    }


    @Override
    public String toString() {
        return Arrays.toString(mSourceFiles.toArray());
    }

    /**
     * Checks a file to make sure it is a valid file in the android res folder.
     * @param file the file to check
     * @return true if it is a valid file, false if it should be ignored.
     */
    protected boolean checkFileForAndroidRes(File file) {
        // TODO: use the aapt ignore pattern value.

        String name = file.getName();
        int pos = name.lastIndexOf('.');
        String extension = "";
        if (pos != -1) {
            extension = name.substring(pos + 1);
        }

        // ignore hidden files and backup files
        return !(name.charAt(0) == '.' || name.charAt(name.length() - 1) == '~') &&
                !"scc".equalsIgnoreCase(extension) &&     // VisualSourceSafe
                !"swp".equalsIgnoreCase(extension) &&     // vi swap file
                !"thumbs.db".equalsIgnoreCase(name) &&    // image index file
                !"picasa.ini".equalsIgnoreCase(name);     // image index file
    }
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/DuplicateResourceException.java b/builder/src/main/java/com/android/builder/resources/DuplicateDataException.java
similarity index 75%
rename from builder/src/main/java/com/android/builder/resources/DuplicateResourceException.java
rename to builder/src/main/java/com/android/builder/resources/DuplicateDataException.java
//Synthetic comment -- index 3bfdfdc..448eedc 100644

//Synthetic comment -- @@ -17,14 +17,14 @@
package com.android.builder.resources;

/**
 * Exception when a {@link DataItem} is declared more than once in a {@link DataSet}
*/
public class DuplicateDataException extends Exception {

    private DataItem mOne;
    private DataItem mTwo;

    DuplicateDataException(DataItem one, DataItem two) {
super(String.format("Duplicate resources: %1s:%2s, %3s:%4s",
one.getSource().getFile().getAbsolutePath(), one.getKey(),
two.getSource().getFile().getAbsolutePath(), two.getKey()));
//Synthetic comment -- @@ -32,11 +32,11 @@
mTwo = two;
}

    public DataItem getOne() {
return mOne;
}

    public DataItem getTwo() {
return mTwo;
}
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/NewResourceSet.java b/builder/src/main/java/com/android/builder/resources/NewResourceSet.java
new file mode 100644
//Synthetic comment -- index 0000000..b0d6fa3

//Synthetic comment -- @@ -0,0 +1,144 @@
package com.android.builder.resources;

import com.android.annotations.NonNull;
import com.android.builder.internal.packaging.PackagingUtils;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceConstants;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 */
public class NewResourceSet extends DataSet<ResourceItem, ResourceFile> {

    public NewResourceSet(String name) {
        super(name);
    }

    @Override
    protected DataSet<ResourceItem, ResourceFile> createSet(String name) {
        return new NewResourceSet(name);
    }

    @Override
    protected ResourceFile createFileAndItems(File file) {
        // get the type.
        FolderData folderData = getFolderData(file.getParentFile());

        return createResourceFile(file, folderData);
    }

    @Override
    protected void readSourceFolder(File sourceFolder) throws DuplicateDataException, IOException {
        File[] folders = sourceFolder.listFiles();
        if (folders != null) {
            for (File folder : folders) {
                // TODO: use the aapt ignore pattern value.
                if (folder.isDirectory() &&
                        PackagingUtils.checkFolderForPackaging(folder.getName())) {
                    FolderData folderData = getFolderData(folder);
                    if (folderData.folderType != null) {
                        parseFolder(sourceFolder, folder, folderData);
                    }
                }
            }
        }
    }

    @Override
    protected boolean isValidSourceFile(File sourceFolder, File file) {
        File resFolder = file.getParentFile();
        // valid files are right under a resource folder under the source folder
        return resFolder.getParentFile().equals(sourceFolder) &&
                ResourceFolderType.getFolderType(resFolder.getName()) != null;
    }

    /**
     * Reads the content of a typed resource folder (sub folder to the root of res folder), and
     * loads the resources from it.
     *
     * @param sourceFolder the main res folder
     * @param folder the folder to read.
     * @param folderData the folder Data
     *
     * @throws IOException
     */
    private void parseFolder(File sourceFolder, File folder, FolderData folderData)
            throws IOException {
        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (!file.isFile() || !checkFileForAndroidRes(file)) {
                    continue;
                }

                ResourceFile resourceFile = createResourceFile(file, folderData);
                processNewDataFile(sourceFolder, resourceFile);
            }
        }
    }

    private ResourceFile createResourceFile(File file, FolderData folderData) {
        if (folderData.type != null) {
            int pos;// get the resource name based on the filename
            String name = file.getName();
            pos = name.indexOf('.');
            if (pos >= 0) {
                name = name.substring(0, pos);
            }

            return new ResourceFile(
                    file,
                    new ResourceItem(name, folderData.type, null),
                    folderData.qualifiers);
        } else {
            try {
                ValueResourceParser parser = new ValueResourceParser(file);
                List<ResourceItem> items = parser.parseFile();

                return new ResourceFile(file, items, folderData.qualifiers);
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * temp structure containing a qualifier string and a {@link com.android.resources.ResourceType}.
     */
    private static class FolderData {
        String qualifiers = null;
        ResourceType type = null;
        ResourceFolderType folderType = null;
    }

    /**
     * Returns a FolderData for the given folder
     * @param folder the folder.
     * @return the FolderData object.
     */
    @NonNull
    private static FolderData getFolderData(File folder) {
        FolderData fd = new FolderData();

        String folderName = folder.getName();
        int pos = folderName.indexOf(ResourceConstants.RES_QUALIFIER_SEP);
        ResourceFolderType folderType;
        if (pos != -1) {
            fd.folderType = ResourceFolderType.getTypeByName(folderName.substring(0, pos));
            fd.qualifiers = folderName.substring(pos + 1);
        } else {
            fd.folderType = ResourceFolderType.getTypeByName(folderName);
        }

        if (fd.folderType != ResourceFolderType.VALUES) {
            fd.type = FolderTypeRelationship.getRelatedResourceTypes(fd.folderType).get(0);
        }

        return fd;
    }
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/Resource.java b/builder/src/main/java/com/android/builder/resources/Resource.java
deleted file mode 100644
//Synthetic comment -- index 65f476c..0000000

//Synthetic comment -- @@ -1,245 +0,0 @@








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceFile.java b/builder/src/main/java/com/android/builder/resources/ResourceFile.java
//Synthetic comment -- index 42ba9bc..623abe7 100644

//Synthetic comment -- @@ -18,38 +18,31 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.util.List;

/**
* Represents a file in a resource folders.
*
* It contains a link to the {@link File}, the qualifier string (which is the name of the folder
 * after the first '-' character), a list of {@link ResourceItem} and a type.
*
* The type of the file is based on whether the file is located in a values folder (FileType.MULTI)
* or in another folder (FileType.SINGLE).
*/
class ResourceFile extends DataFile<ResourceItem> {

    private static final String ATTR_QUALIFIER = "qualifiers";

private final String mQualifiers;

/**
* Creates a resource file with a single resource item.
*
     * The source file is set on the item with {@link ResourceItem#setSource(ResourceFile)}
*
* The type of the ResourceFile will by {@link FileType#SINGLE}.
*
//Synthetic comment -- @@ -57,19 +50,15 @@
* @param item the resource item
* @param qualifiers the qualifiers.
*/
    ResourceFile(@NonNull File file, @NonNull ResourceItem item, @Nullable String qualifiers) {
        super(file, item);
mQualifiers = qualifiers;
}

/**
* Creates a resource file with a list of resource items.
*
     * The source file is set on the items with {@link ResourceItem#setSource(ResourceFile)}
*
* The type of the ResourceFile will by {@link FileType#MULTI}.
*
//Synthetic comment -- @@ -77,52 +66,19 @@
* @param items the resource items
* @param qualifiers the qualifiers.
*/
    ResourceFile(@NonNull File file, @NonNull List<ResourceItem> items,
                 @Nullable String qualifiers) {
        super(file, items);
mQualifiers = qualifiers;
}
@Nullable
String getQualifiers() {
return mQualifiers;
}

    @Override
    void addExtraAttributes(Document document, Node node, String namespaceUri) {
        NodeUtils.addAttribute(document, node, namespaceUri, ATTR_QUALIFIER,
                getQualifiers());
}
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceItem.java b/builder/src/main/java/com/android/builder/resources/ResourceItem.java
new file mode 100644
//Synthetic comment -- index 0000000..47c898c

//Synthetic comment -- @@ -0,0 +1,156 @@
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * A resource.
 *
 * This includes the name, type, source file as a {@link ResourceFile} and an optional {@link Node}
 * in case of a resource coming from a value file.
 *
 */
class ResourceItem extends DataItem<ResourceFile> {

    private static final String ATTR_TYPE = "type";


    private final ResourceType mType;
    private Node mValue;

    /**
     * Constructs the object with a name, type and optional value.
     *
     * Note that the object is not fully usable as-is. It must be added to a ResourceFile first.
     *
     * @param name the name of the resource
     * @param type the type of the resource
     * @param value an optional Node that represents the resource value.
     */
    ResourceItem(@NonNull String name, @NonNull ResourceType type, Node value) {
        super(name);
        mType = type;
        mValue = value;
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
    void setValue(ResourceItem from) {
        mValue = from.mValue;
        setTouched();
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
    @Override
    String getKey() {
        if (getSource() == null) {
            throw new IllegalStateException(
                    "ResourceItem.getKey called on object with no ResourceFile: " + this);
        }
        String qualifiers = getSource().getQualifiers();
        if (qualifiers != null && qualifiers.length() > 0) {
            return mType.getName() + "-" + qualifiers + "/" + getName();
        }

        return mType.getName() + "/" + getName();
    }

    @Override
    void addExtraAttributes(Document document, Node node, String namespaceUri) {
        NodeUtils.addAttribute(document, node, null, ATTR_TYPE, mType.getName());
    }

    @Override
    Node getAdoptedNode(Document document) {
        return NodeUtils.adoptNode(document, mValue);
    }

    /**
     * Compares the ResourceItem {@link #getValue()} together and returns true if they are the same.
     * @param resource The ResourceItem object to compare to.
     * @return true if equal
     */
    public boolean compareValueWith(ResourceItem resource) {
        if (mValue != null && resource.mValue != null) {
            return NodeUtils.compareElementNode(mValue, resource.mValue);
        }

        return mValue == resource.mValue;
    }

    @Override
    public String toString() {
        return "ResourceItem{" +
                "mName='" + getName() + '\'' +
                ", mType=" + mType +
                ", mStatus=" + getStatus() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResourceItem that = (ResourceItem) o;

        if (mType != that.mType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mType.hashCode();
        return result;
    }
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceMap.java b/builder/src/main/java/com/android/builder/resources/ResourceMap.java
//Synthetic comment -- index cd1f5d9..022211d 100644

//Synthetic comment -- @@ -20,8 +20,8 @@
import com.google.common.collect.ListMultimap;

/**
 * A ResourceItem Map able to provide a {@link ListMultimap} of Resources where the keys are
 * the value returned by {@link ResourceItem#getKey()}
*/
interface ResourceMap {

//Synthetic comment -- @@ -33,9 +33,9 @@

/**
* a Multi map of (key, resource) where key is the result of
     * {@link ResourceItem#getKey()}
* @return a non null map
*/
@NonNull
    ListMultimap<String, ResourceItem> getResourceMap();
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceMerger.java b/builder/src/main/java/com/android/builder/resources/ResourceMerger.java
//Synthetic comment -- index 647beb2..843cb67 100755

//Synthetic comment -- @@ -114,7 +114,7 @@
Set<String> keys = Sets.newHashSet();

for (ResourceSet resourceSet : mResourceSets) {
            ListMultimap<String, ResourceItem> map = resourceSet.getResourceMap();
keys.addAll(map.keySet());
}

//Synthetic comment -- @@ -129,14 +129,14 @@
*/
@NonNull
@Override
    public ListMultimap<String, ResourceItem> getResourceMap() {
// put all the sets in a multimap. The result is that for each key,
// there is a sorted list of items from all the layers, including removed ones.
        ListMultimap<String, ResourceItem> fullItemMultimap = ArrayListMultimap.create();

for (ResourceSet resourceSet : mResourceSets) {
            ListMultimap<String, ResourceItem> map = resourceSet.getResourceMap();
            for (Map.Entry<String, Collection<ResourceItem>> entry : map.asMap().entrySet()) {
fullItemMultimap.putAll(entry.getKey(), entry.getValue());
}
}
//Synthetic comment -- @@ -167,13 +167,13 @@
for (ResourceSet resourceSet : mResourceSets) {
// quick check on duplicates in the resource set.
resourceSet.checkItems();
            ListMultimap<String, ResourceItem> map = resourceSet.getResourceMap();
resourceKeys.addAll(map.keySet());
}

// map of XML values files to write after parsing all the files.
// the key is the qualifier.
        ListMultimap<String, ResourceItem> valuesResMap = ArrayListMultimap.create();
// set of qualifier that was a previously written resource disappear. This is to keep track
// of which file to write if no other resources are touched.
Set<String> qualifierWithDeletedValues = Sets.newHashSet();
//Synthetic comment -- @@ -182,8 +182,8 @@
for (String resourceKey : resourceKeys) {
// for each resource, look in the resource sets, starting from the end of the list.

            ResourceItem previouslyWritten = null;
            ResourceItem toWrite = null;

/*
* We are looking for what to write/delete: the last non deleted item, and the
//Synthetic comment -- @@ -194,9 +194,9 @@
ResourceSet resourceSet = mResourceSets.get(i);

// look for the resource key in the set
                ListMultimap<String, ResourceItem> resourceMap = resourceSet.getResourceMap();

                List<ResourceItem> resources = resourceMap.get(resourceKey);
if (resources.isEmpty()) {
continue;
}
//Synthetic comment -- @@ -205,7 +205,7 @@
// More than one deleted means there was more than one which isn't possible
// More than one touched means there is more than one and this isn't possible.
for (int ii = resources.size() - 1 ; ii >= 0 ; ii--) {
                    ResourceItem resource = resources.get(ii);

if (resource.isWritten()) {
assert previouslyWritten == null;
//Synthetic comment -- @@ -285,11 +285,11 @@
boolean mustWriteFile = qualifierWithDeletedValues.remove(key);

// get the list of items to write
            Collection<ResourceItem> items = valuesResMap.get(key);

// now check if we really have to write it
if (!mustWriteFile) {
                for (ResourceItem item : items) {
if (item.isTouched()) {
mustWriteFile = true;
break;
//Synthetic comment -- @@ -319,7 +319,7 @@
Node rootNode = document.createElement(TAG_RESOURCES);
document.appendChild(rootNode);

                    for (ResourceItem item : items) {
Node adoptedNode = NodeUtils.adoptNode(document, item.getValue());
rootNode.appendChild(adoptedNode);
}
//Synthetic comment -- @@ -378,8 +378,8 @@
}

/**
     * Writes a given ResourceItem to a given root res folder.
     * If the ResourceItem is to be written in a "Values" folder, then it is added to a map instead.
*
* @param rootFolder the root res folder
* @param valuesResMap a map of existing values-type resources where the key is the qualifiers
//Synthetic comment -- @@ -390,8 +390,8 @@
* @throws IOException
*/
private void writeResource(@NonNull final File rootFolder,
                               @NonNull ListMultimap<String, ResourceItem> valuesResMap,
                               @NonNull final ResourceItem resource,
@NonNull WaitableExecutor executor,
@Nullable final AaptRunner aaptRunner) throws IOException {
ResourceFile.FileType type = resource.getSource().getType();
//Synthetic comment -- @@ -554,20 +554,20 @@
/**
* Sets all existing resources to have their state be WRITTEN.
*
     * @see ResourceItem#isWritten()
*/
private void setResourcesToWritten() {
        ListMultimap<String, ResourceItem> resources = ArrayListMultimap.create();

for (ResourceSet resourceSet : mResourceSets) {
            ListMultimap<String, ResourceItem> map = resourceSet.getResourceMap();
            for (Map.Entry<String, Collection<ResourceItem>> entry : map.asMap().entrySet()) {
resources.putAll(entry.getKey(), entry.getValue());
}
}

for (String key : resources.keySet()) {
            List<ResourceItem> resourceList = resources.get(key);
resourceList.get(resourceList.size() - 1).resetStatusToWritten();
}
}








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ResourceSet.java b/builder/src/main/java/com/android/builder/resources/ResourceSet.java
//Synthetic comment -- index d537042..0ee5c8f 100755

//Synthetic comment -- @@ -42,7 +42,7 @@
* Represents a set of resources.
*
* The resources can be coming from multiple source folders. Duplicates are detected (either
 * from the same source folder -- same resource in values files -- or across the source folders.)
*
* Each source folders is considered to be at the same level. To use overlays, a
* {@link ResourceMerger} must be used.
//Synthetic comment -- @@ -74,11 +74,11 @@
private final List<File> mSourceFiles = Lists.newArrayList();

/**
     * The key is the {@link ResourceItem#getKey()}.
* This is a multimap to support moving a resource from one file to another (values file)
* during incremental update.
*/
    private final ListMultimap<String, ResourceItem> mItems = ArrayListMultimap.create();

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
        // ResourceItem objects.
return mItems.keySet().size();
}

//Synthetic comment -- @@ -189,7 +190,7 @@
*/
@NonNull
@Override
    public ListMultimap<String, ResourceItem> getResourceMap() {
return mItems;
}

//Synthetic comment -- @@ -249,12 +250,12 @@
resourceFile.getQualifiers());

if (resourceFile.getType() == ResourceFile.FileType.MULTI) {
                    for (ResourceItem item : resourceFile.getItems()) {
Node adoptedNode = NodeUtils.adoptNode(document, item.getValue());
fileNode.appendChild(adoptedNode);
}
} else {
                    ResourceItem item = resourceFile.getItem();
NodeUtils.addAttribute(document, fileNode, null, ATTR_TYPE,
item.getType().getName());
NodeUtils.addAttribute(document, fileNode, null, ATTR_NAME, item.getName());
//Synthetic comment -- @@ -319,7 +320,7 @@
Attr typeAttr = (Attr) fileNode.getAttributes().getNamedItem(ATTR_TYPE);
if (typeAttr == null) {
// multi res file
                    List<ResourceItem> resourceList = Lists.newArrayList();

// loop on each node that represent a resource
NodeList resNodes = fileNode.getChildNodes();
//Synthetic comment -- @@ -330,7 +331,7 @@
continue;
}

                        ResourceItem r = ValueResourceParser.getResource(resNode);
if (r != null) {
resourceList.add(r);
}
//Synthetic comment -- @@ -340,7 +341,7 @@
resourceList, qualifier);
resourceSet.addResourceFile(sourceFolder, resourceFile);

                    for (ResourceItem item : resourceList) {
resourceSet.mItems.put(item.getKey(), item);
}

//Synthetic comment -- @@ -356,7 +357,7 @@
continue;
}

                    ResourceItem item = new ResourceItem(nameAttr.getValue(), type, null);
ResourceFile resourceFile = new ResourceFile(new File(pathAttr.getValue()),
item, qualifier);

//Synthetic comment -- @@ -446,13 +447,13 @@
continue;
}
if (folderData.type != null) {
                    ResourceItem item = handleSingleResFile(sourceFolder,
folderData.qualifiers, folderData.type, file);
item.setTouched();
} else {
                    Collection<ResourceItem> items = handleMultiResFile(sourceFolder,
folderData.qualifiers, file);
                    for (ResourceItem item : items) {
item.setTouched();
}
}
//Synthetic comment -- @@ -461,16 +462,16 @@
}

/**
     * Handles a single resource file (ie not located in "values") and create a ResourceItem from it.
*
* @param sourceFolder the top res folder for the file
* @param qualifiers the qualifiers associated with the file
* @param type the ResourceType read from the parent folder name
* @param file the single resource file
     * @return a ResourceItem object
*/
@NonNull
    private ResourceItem handleSingleResFile(File sourceFolder, String qualifiers,
ResourceType type, File file) {
int pos;// get the resource name based on the filename
String name = file.getName();
//Synthetic comment -- @@ -479,7 +480,7 @@
name = name.substring(0, pos);
}

        ResourceItem item = new ResourceItem(name, type, null);
ResourceFile resourceFile = new ResourceFile(file, item, qualifiers);
addResourceFile(sourceFolder, resourceFile);

//Synthetic comment -- @@ -489,25 +490,25 @@
}

/**
     * Handles a multi res file (in a "values" folder) and create ResourceItem object from it.
*
* @param sourceFolder the top res folder for the file
* @param qualifiers the qualifiers associated with the file
* @param file the single resource file
     * @return a list of created ResourceItem objects.
*
* @throws IOException
*/
@NonNull
    private Collection<ResourceItem> handleMultiResFile(File sourceFolder, String qualifiers, File file)
throws IOException {
ValueResourceParser parser = new ValueResourceParser(file);
        List<ResourceItem> items = parser.parseFile();

ResourceFile resourceFile = new ResourceFile(file, items, qualifiers);
addResourceFile(sourceFolder, resourceFile);

        for (ResourceItem item : items) {
mItems.put(item.getKey(), item);
}

//Synthetic comment -- @@ -532,12 +533,12 @@
*/
void checkItems() throws DuplicateResourceException {
// check a list for duplicate, ignoring removed items.
        for (Map.Entry<String, Collection<ResourceItem>> entry : mItems.asMap().entrySet()) {
            Collection<ResourceItem> items = entry.getValue();

// there can be several version of the same key if some are "removed"
            ResourceItem lastItem = null;
            for (ResourceItem item : items) {
if (!item.isRemoved()) {
if (lastItem == null) {
lastItem = item;
//Synthetic comment -- @@ -570,20 +571,20 @@
} else {
// multi res. Need to parse the file and compare the items one by one.
ValueResourceParser parser = new ValueResourceParser(changedFile);
                    List<ResourceItem> parsedItems = parser.parseFile();

                    Map<String, ResourceItem> oldItems = Maps.newHashMap(resourceFile.getItemMap());

                    Map<String, ResourceItem> newItems  = Maps.newHashMap();

// create a fake ResourceFile to be able to call resource.getKey();
// It's ok because we never use this instance anyway.
ResourceFile fakeResourceFile = new ResourceFile(changedFile, parsedItems,
resourceFile.getQualifiers());

                    for (ResourceItem newItem : parsedItems) {
String newKey = newItem.getKey();
                        ResourceItem oldItem = oldItems.get(newKey);

if (oldItem == null) {
// this is a new item
//Synthetic comment -- @@ -605,13 +606,13 @@

// at this point oldItems is left with the deleted items.
// just update their status to removed.
                    for (ResourceItem deletedItem : oldItems.values()) {
deletedItem.setRemoved();
}

// Now we need to add the new items to the resource file and the main map
resourceFile.addItems(newItems.values());
                    for (Map.Entry<String, ResourceItem> entry : newItems.entrySet()) {
mItems.put(entry.getKey(), entry.getValue());
}
}
//Synthetic comment -- @@ -621,13 +622,13 @@
folderData = getFolderData(changedFile.getParentFile());

if (folderData.type != null) {
                    ResourceItem item = handleSingleResFile(sourceFolder, folderData.qualifiers,
folderData.type, changedFile);
item.setTouched();
} else {
                    Collection<ResourceItem> items = handleMultiResFile(sourceFolder,
folderData.qualifiers, changedFile);
                    for (ResourceItem item : items) {
item.setTouched();
}
}
//Synthetic comment -- @@ -637,7 +638,7 @@
resourceFile = mResourceFileMap.get(changedFile);

// flag all resource items are removed
                for (ResourceItem item : resourceFile.getItems()) {
item.setRemoved();
}
return true;








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/ValueResourceParser.java b/builder/src/main/java/com/android/builder/resources/ValueResourceParser.java
//Synthetic comment -- index 9adda0a..f7d5f53 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
/**
* Parser for "values" files.
*
 * This parses the file and returns a list of {@link ResourceItem} object.
*/
class ValueResourceParser {

//Synthetic comment -- @@ -59,13 +59,13 @@
}

/**
     * Parses the file and returns a list of {@link ResourceItem} objects.
* @return a list of resources.
*
* @throws IOException
*/
@NonNull
    List<ResourceItem> parseFile() throws IOException {
Document document = parseDocument(mFile);

// get the root node
//Synthetic comment -- @@ -75,7 +75,7 @@
}
NodeList nodes = rootNode.getChildNodes();

        List<ResourceItem> resources = Lists.newArrayListWithExpectedSize(nodes.getLength());

for (int i = 0, n = nodes.getLength(); i < n; i++) {
Node node = nodes.item(i);
//Synthetic comment -- @@ -84,7 +84,7 @@
continue;
}

            ResourceItem resource = getResource(node);
if (resource != null) {
resources.add(resource);
}
//Synthetic comment -- @@ -94,23 +94,23 @@
}

/**
     * Returns a new ResourceItem object for a given node.
* @param node the node representing the resource.
     * @return a ResourceItem object or null.
*/
    static ResourceItem getResource(Node node) {
ResourceType type = getType(node);
String name = getName(node);

if (type != null && name != null) {
            return new ResourceItem(name, type, node);
}

return null;
}

/**
     * Returns the type of the ResourceItem based on a node's attributes.
* @param node the node
* @return the ResourceType or null if it could not be inferred.
*/








//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/resources/BaseTestCase.java b/builder/src/test/java/com/android/builder/resources/BaseTestCase.java
//Synthetic comment -- index a18d69c..0643985 100644

//Synthetic comment -- @@ -24,10 +24,10 @@
public abstract class BaseTestCase extends TestCase {

protected void verifyResourceExists(ResourceMap resourceMap, String... resourceKeys) {
        ListMultimap<String, ResourceItem> map = resourceMap.getResourceMap();

for (String resKey : resourceKeys) {
            List<ResourceItem> resources = map.get(resKey);
assertTrue("resource '" + resKey + "' is missing!", resources.size() > 0);
}
}
//Synthetic comment -- @@ -48,11 +48,11 @@
assertEquals(resourceMap1.size(), resourceMap2.size());

// compare the resources are all the same
        ListMultimap<String, ResourceItem> map1 = resourceMap1.getResourceMap();
        ListMultimap<String, ResourceItem> map2 = resourceMap2.getResourceMap();
for (String key : map1.keySet()) {
            List<ResourceItem> items1 = map1.get(key);
            List<ResourceItem> items2 = map2.get(key);
if (fullCompare) {
assertEquals("Wrong size for " + key, items1.size(), items2.size());
} else {








//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/resources/ResourceMergerTest.java b/builder/src/test/java/com/android/builder/resources/ResourceMergerTest.java
//Synthetic comment -- index f056413..644ef97 100755

//Synthetic comment -- @@ -86,13 +86,13 @@

public void testReplacedLayout() throws Exception {
ResourceMerger merger = getResourceMerger();
        ListMultimap<String, ResourceItem> mergedMap = merger.getResourceMap();

        List<ResourceItem> values = mergedMap.get("layout/main");

// the overlay means there's 2 versions of this resource.
assertEquals(2, values.size());
        ResourceItem mainLayout = values.get(1);

ResourceFile sourceFile = mainLayout.getSource();
assertTrue(sourceFile.getFile().getAbsolutePath()
//Synthetic comment -- @@ -101,14 +101,14 @@

public void testReplacedAlias() throws Exception {
ResourceMerger merger = getResourceMerger();
        ListMultimap<String, ResourceItem> mergedMap = merger.getResourceMap();


        List<ResourceItem> values = mergedMap.get("layout/alias_replaced_by_file");

// the overlay means there's 2 versions of this resource.
assertEquals(2, values.size());
        ResourceItem layout = values.get(1);

// since it's replaced by a file, there's no node.
assertNull(layout.getValue());
//Synthetic comment -- @@ -116,13 +116,13 @@

public void testReplacedFile() throws Exception {
ResourceMerger merger = getResourceMerger();
        ListMultimap<String, ResourceItem> mergedMap = merger.getResourceMap();

        List<ResourceItem> values = mergedMap.get("layout/file_replaced_by_alias");

// the overlay means there's 2 versions of this resource.
assertEquals(2, values.size());
        ResourceItem layout = values.get(1);

// since it's replaced by a file, there's no node.
assertNotNull(layout.getValue());
//Synthetic comment -- @@ -138,7 +138,7 @@
writtenSet.loadFromFiles();

// compare the two maps, but not using the full map as the set loaded from the output
        // won't contains all versions of each ResourceItem item.
compareResourceMaps(merger, writtenSet, false /*full compare*/);
}

//Synthetic comment -- @@ -225,40 +225,40 @@
resourceMerger.validateResourceSets();

// check the content.
        ListMultimap<String, ResourceItem> mergedMap = resourceMerger.getResourceMap();

// check unchanged file is WRITTEN
        List<ResourceItem> drawableUntouched = mergedMap.get("drawable/untouched");
assertEquals(1, drawableUntouched.size());
assertTrue(drawableUntouched.get(0).isWritten());
assertFalse(drawableUntouched.get(0).isTouched());
assertFalse(drawableUntouched.get(0).isRemoved());

// check replaced file is TOUCHED
        List<ResourceItem> drawableTouched = mergedMap.get("drawable/touched");
assertEquals(1, drawableTouched.size());
assertTrue(drawableTouched.get(0).isWritten());
assertTrue(drawableTouched.get(0).isTouched());
assertFalse(drawableTouched.get(0).isRemoved());

// check removed file is REMOVED
        List<ResourceItem> drawableRemoved = mergedMap.get("drawable/removed");
assertEquals(1, drawableRemoved.size());
assertTrue(drawableRemoved.get(0).isWritten());
assertTrue(drawableRemoved.get(0).isRemoved());

// check new overlay: two objects, last one is TOUCHED
        List<ResourceItem> drawableNewOverlay = mergedMap.get("drawable/new_overlay");
assertEquals(2, drawableNewOverlay.size());
        ResourceItem newOverlay = drawableNewOverlay.get(1);
assertEquals(overlayDrawableNewOverlay, newOverlay.getSource().getFile());
assertFalse(newOverlay.isWritten());
assertTrue(newOverlay.isTouched());

// check new alternate: one objects, last one is TOUCHED
        List<ResourceItem> drawableHdpiNewAlternate = mergedMap.get("drawable-hdpi/new_alternate");
assertEquals(1, drawableHdpiNewAlternate.size());
        ResourceItem newAlternate = drawableHdpiNewAlternate.get(0);
assertEquals(overlayDrawableHdpiNewAlternate, newAlternate.getSource().getFile());
assertFalse(newAlternate.isWritten());
assertTrue(newAlternate.isTouched());
//Synthetic comment -- @@ -325,24 +325,24 @@
resourceMerger.validateResourceSets();

// check the content.
        ListMultimap<String, ResourceItem> mergedMap = resourceMerger.getResourceMap();

// check unchanged string is WRITTEN
        List<ResourceItem> valuesUntouched = mergedMap.get("string/untouched");
assertEquals(1, valuesUntouched.size());
assertTrue(valuesUntouched.get(0).isWritten());
assertFalse(valuesUntouched.get(0).isTouched());
assertFalse(valuesUntouched.get(0).isRemoved());

// check replaced file is TOUCHED
        List<ResourceItem> valuesTouched = mergedMap.get("string/touched");
assertEquals(1, valuesTouched.size());
assertTrue(valuesTouched.get(0).isWritten());
assertTrue(valuesTouched.get(0).isTouched());
assertFalse(valuesTouched.get(0).isRemoved());

// check removed file is REMOVED
        List<ResourceItem> valuesRemoved = mergedMap.get("string/removed");
assertEquals(1, valuesRemoved.size());
assertTrue(valuesRemoved.get(0).isWritten());
assertTrue(valuesRemoved.get(0).isRemoved());
//Synthetic comment -- @@ -353,16 +353,16 @@
assertTrue(valuesRemoved.get(0).isRemoved());

// check new overlay: two objects, last one is TOUCHED
        List<ResourceItem> valuesNewOverlay = mergedMap.get("string/new_overlay");
assertEquals(2, valuesNewOverlay.size());
        ResourceItem newOverlay = valuesNewOverlay.get(1);
assertFalse(newOverlay.isWritten());
assertTrue(newOverlay.isTouched());

// check new alternate: one objects, last one is TOUCHED
        List<ResourceItem> valuesFrNewAlternate = mergedMap.get("string-fr/new_alternate");
assertEquals(1, valuesFrNewAlternate.size());
        ResourceItem newAlternate = valuesFrNewAlternate.get(0);
assertFalse(newAlternate.isWritten());
assertTrue(newAlternate.isTouched());

//Synthetic comment -- @@ -419,17 +419,17 @@
resourceMerger.validateResourceSets();

// check the content.
        ListMultimap<String, ResourceItem> mergedMap = resourceMerger.getResourceMap();

// check unchanged string is WRITTEN
        List<ResourceItem> valuesUntouched = mergedMap.get("string/untouched");
assertEquals(1, valuesUntouched.size());
assertTrue(valuesUntouched.get(0).isWritten());
assertFalse(valuesUntouched.get(0).isTouched());
assertFalse(valuesUntouched.get(0).isRemoved());

// check removed_overlay is present twice.
        List<ResourceItem> valuesRemovedOverlay = mergedMap.get("string/removed_overlay");
assertEquals(2, valuesRemovedOverlay.size());
// first is untouched
assertFalse(valuesRemovedOverlay.get(0).isWritten());
//Synthetic comment -- @@ -487,20 +487,20 @@
resourceMerger.validateResourceSets();

// check the content.
        ListMultimap<String, ResourceItem> mergedMap = resourceMerger.getResourceMap();

// check layout/main is unchanged
        List<ResourceItem> layoutMain = mergedMap.get("layout/main");
assertEquals(1, layoutMain.size());
assertTrue(layoutMain.get(0).isWritten());
assertFalse(layoutMain.get(0).isTouched());
assertFalse(layoutMain.get(0).isRemoved());

// check file_replaced_by_alias has 2 version, 2nd is TOUCHED, and contains a Node
        List<ResourceItem> layoutReplacedByAlias = mergedMap.get("layout/file_replaced_by_alias");
assertEquals(2, layoutReplacedByAlias.size());
// 1st one is removed version, as it already existed in the item multimap
        ResourceItem replacedByAlias = layoutReplacedByAlias.get(0);
assertTrue(replacedByAlias.isWritten());
assertFalse(replacedByAlias.isTouched());
assertTrue(replacedByAlias.isRemoved());
//Synthetic comment -- @@ -515,10 +515,10 @@
assertEquals("values.xml", replacedByAlias.getSource().getFile().getName());

// check alias_replaced_by_file has 2 version, 2nd is TOUCHED, and contains a Node
        List<ResourceItem> layoutReplacedByFile = mergedMap.get("layout/alias_replaced_by_file");
// 1st one is removed version, as it already existed in the item multimap
assertEquals(2, layoutReplacedByFile.size());
        ResourceItem replacedByFile = layoutReplacedByFile.get(0);
assertTrue(replacedByFile.isWritten());
assertFalse(replacedByFile.isTouched());
assertTrue(replacedByFile.isRemoved());








//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/resources/ValueResourceParserTest.java b/builder/src/test/java/com/android/builder/resources/ValueResourceParserTest.java
//Synthetic comment -- index 96dabfd..9cd84d7 100644

//Synthetic comment -- @@ -28,18 +28,18 @@
*/
public class ValueResourceParserTest extends BaseTestCase {

    private static List<ResourceItem> sResources = null;

public void testParsedResourcesByCount() throws Exception {
        List<ResourceItem> resources = getParsedResources();

assertEquals(18, resources.size());
}

public void testParsedResourcesByName() throws Exception {
        List<ResourceItem> resources = getParsedResources();
        Map<String, ResourceItem> resourceMap = Maps.newHashMapWithExpectedSize(resources.size());
        for (ResourceItem item : resources) {
resourceMap.put(item.getKey(), item);
}

//Synthetic comment -- @@ -68,7 +68,7 @@
}
}

    private static List<ResourceItem> getParsedResources() throws IOException {
if (sResources == null) {
File root = TestUtils.getRoot("baseResourceSet");
File values = new File(root, "values");
//Synthetic comment -- @@ -77,7 +77,7 @@
ValueResourceParser parser = new ValueResourceParser(valuesXml);
sResources = parser.parseFile();

            // create a fake resource file to allow calling ResourceItem.getKey()
new ResourceFile(valuesXml, sResources, "");
}








