/*Change the FolderTypeRelationship and ResourceFile API to return list.

This is better as we can return unmodifiable list instead of
arrays that could be overriden.

Change-Id:Id473925ee6355b2bedeb2d2575bc4ca4f949acb3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java
//Synthetic comment -- index 81dc818..10ccd77 100644

//Synthetic comment -- @@ -352,10 +352,10 @@

// now check that the file is *NOT* a layout file (those automatically trigger a layout
// reload and we don't want to do it twice.)
            ResourceType[] resTypes = file.getResourceTypes();

// it's unclear why but there has been cases of resTypes being empty!
            if (resTypes.length > 0) {
// this is a resource change, that may require a layout redraw!
if (changeFlags == null) {
changeFlags = new ChangeFlags();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index c69c402..f9e103a 100644

//Synthetic comment -- @@ -419,7 +419,7 @@
* @return true if we updated the includes for the resource file
*/
private boolean updateFileIncludes(ResourceFile resourceFile, boolean singleUpdate) {
        ResourceType[] resourceTypes = resourceFile.getResourceTypes();
for (ResourceType type : resourceTypes) {
if (type == ResourceType.LAYOUT) {
ensureInitialized();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index f373ccc..3ade09d 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import org.eclipse.jface.dialogs.IInputValidator;

import java.util.HashSet;
import java.util.Set;

/**
//Synthetic comment -- @@ -182,7 +183,7 @@
* @return true if the given resource type is stored in a file named by the resource
*/
public static boolean isFileBasedResourceType(ResourceType type) {
        ResourceFolderType[] folderTypes = FolderTypeRelationship.getRelatedFolders(type);
for (ResourceFolderType folderType : folderTypes) {
if (folderType != ResourceFolderType.VALUES) {
return true;
//Synthetic comment -- @@ -205,7 +206,7 @@
*         values/ folder
*/
public static boolean isValueBasedResourceType(ResourceType type) {
        ResourceFolderType[] folderTypes = FolderTypeRelationship.getRelatedFolders(type);
for (ResourceFolderType folderType : folderTypes) {
if (folderType == ResourceFolderType.VALUES) {
return true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationship.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationship.java
//Synthetic comment -- index b0f1389..aa71af0 100644

//Synthetic comment -- @@ -20,87 +20,82 @@
import com.android.resources.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * This class gives access to the bi directional relationship between {@link ResourceType} and
* {@link ResourceFolderType}.
*/
public final class FolderTypeRelationship {

    private final static HashMap<ResourceType, ResourceFolderType[]> mTypeToFolderMap =
        new HashMap<ResourceType, ResourceFolderType[]>();

    private final static HashMap<ResourceFolderType, ResourceType[]> mFolderToTypeMap =
        new HashMap<ResourceFolderType, ResourceType[]>();

    // generate the relationships.
static {
        HashMap<ResourceType, List<ResourceFolderType>> typeToFolderMap =
            new HashMap<ResourceType, List<ResourceFolderType>>();

        HashMap<ResourceFolderType, List<ResourceType>> folderToTypeMap =
            new HashMap<ResourceFolderType, List<ResourceType>>();

        add(ResourceType.ANIM, ResourceFolderType.ANIM, typeToFolderMap, folderToTypeMap);
        add(ResourceType.ANIMATOR, ResourceFolderType.ANIMATOR, typeToFolderMap, folderToTypeMap);
        add(ResourceType.ARRAY, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.ATTR, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.BOOL, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.COLOR, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.COLOR, ResourceFolderType.COLOR, typeToFolderMap, folderToTypeMap);
        add(ResourceType.DECLARE_STYLEABLE, ResourceFolderType.VALUES, typeToFolderMap,
                folderToTypeMap);
        add(ResourceType.DIMEN, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.DRAWABLE, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.DRAWABLE, ResourceFolderType.DRAWABLE, typeToFolderMap, folderToTypeMap);
        add(ResourceType.FRACTION, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.ID, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.INTEGER, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.INTERPOLATOR, ResourceFolderType.INTERPOLATOR, typeToFolderMap,
                folderToTypeMap);
        add(ResourceType.LAYOUT, ResourceFolderType.LAYOUT, typeToFolderMap, folderToTypeMap);
        add(ResourceType.MENU, ResourceFolderType.MENU, typeToFolderMap, folderToTypeMap);
        add(ResourceType.MIPMAP, ResourceFolderType.MIPMAP, typeToFolderMap, folderToTypeMap);
        add(ResourceType.PLURALS, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.PUBLIC, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.RAW, ResourceFolderType.RAW, typeToFolderMap, folderToTypeMap);
        add(ResourceType.STRING, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.STYLE, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.STYLEABLE, ResourceFolderType.VALUES, typeToFolderMap, folderToTypeMap);
        add(ResourceType.XML, ResourceFolderType.XML, typeToFolderMap, folderToTypeMap);

        optimize(typeToFolderMap, folderToTypeMap);
}

/**
* Returns a list of {@link ResourceType}s that can be generated from files inside a folder
* of the specified type.
* @param folderType The folder type.
     * @return an array of {@link ResourceType}
*/
    public static ResourceType[] getRelatedResourceTypes(ResourceFolderType folderType) {
        ResourceType[] array = mFolderToTypeMap.get(folderType);
        if (array != null) {
            return array;
}
        return new ResourceType[0];
}

/**
* Returns a list of {@link ResourceFolderType} that can contain files generating resources
* of the specified type.
* @param resType the type of resource.
     * @return an array of {@link ResourceFolderType}
*/
    public static ResourceFolderType[] getRelatedFolders(ResourceType resType) {
        ResourceFolderType[] array = mTypeToFolderMap.get(resType);
        if (array != null) {
            return array;
}
        return new ResourceFolderType[0];
}

/**
//Synthetic comment -- @@ -111,14 +106,10 @@
* could generate a resource of the specified {@link ResourceType}
*/
public static boolean match(ResourceType resType, ResourceFolderType folderType) {
        ResourceFolderType[] array = mTypeToFolderMap.get(resType);

        if (array != null && array.length > 0) {
            for (ResourceFolderType fType : array) {
                if (fType == folderType) {
                    return true;
                }
            }
}

return false;
//Synthetic comment -- @@ -129,27 +120,23 @@
* a file in the folder can generate a resource of the specified type.
* @param type The resourceType
* @param folder The {@link ResourceFolderType}
     * @param folderToTypeMap
     * @param typeToFolderMap
*/
    private static void add(ResourceType type, ResourceFolderType folder,
            HashMap<ResourceType, List<ResourceFolderType>> typeToFolderMap,
            HashMap<ResourceFolderType, List<ResourceType>> folderToTypeMap) {
// first we add the folder to the list associated with the type.
        List<ResourceFolderType> folderList = typeToFolderMap.get(type);
if (folderList == null) {
folderList = new ArrayList<ResourceFolderType>();
            typeToFolderMap.put(type, folderList);
}
if (folderList.indexOf(folder) == -1) {
folderList.add(folder);
}

// now we add the type to the list associated with the folder.
        List<ResourceType> typeList = folderToTypeMap.get(folder);
if (typeList == null) {
typeList = new ArrayList<ResourceType>();
            folderToTypeMap.put(folder, typeList);
}
if (typeList.indexOf(type) == -1) {
typeList.add(type);
//Synthetic comment -- @@ -157,22 +144,23 @@
}

/**
     * Optimize the map to contains array instead of lists (since the api returns arrays)
     * @param typeToFolderMap
     * @param folderToTypeMap
*/
    private static void optimize(HashMap<ResourceType, List<ResourceFolderType>> typeToFolderMap,
            HashMap<ResourceFolderType, List<ResourceType>> folderToTypeMap) {
        Set<ResourceType> types = typeToFolderMap.keySet();
        for (ResourceType type : types) {
            List<ResourceFolderType> list = typeToFolderMap.get(type);
            mTypeToFolderMap.put(type, list.toArray(new ResourceFolderType[list.size()]));
}

        Set<ResourceFolderType> folders = folderToTypeMap.keySet();
        for (ResourceFolderType folder : folders) {
            List<ResourceType> list = folderToTypeMap.get(folder);
            mFolderToTypeMap.put(folder, list.toArray(new ResourceType[list.size()]));
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java
//Synthetic comment -- index 201780d..8f8e0d3 100644

//Synthetic comment -- @@ -28,8 +28,10 @@
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//Synthetic comment -- @@ -50,17 +52,24 @@
private final Map<ResourceType, HashMap<String, ResourceValue>> mResourceItems =
new EnumMap<ResourceType, HashMap<String, ResourceValue>>(ResourceType.class);

public MultiResourceFile(IAbstractFile file, ResourceFolder folder) {
super(file, folder);
}

@Override
    public ResourceType[] getResourceTypes() {
update();

        Set<ResourceType> keys = mResourceItems.keySet();

        return keys.toArray(new ResourceType[keys.size()]);
}

@Override
//Synthetic comment -- @@ -113,6 +122,8 @@
parseFile();

resetTouch();
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index 3de0f4a..5f1350c 100644

//Synthetic comment -- @@ -187,14 +187,14 @@
// (for now)

// get the lists of ResourceTypes generated by this ResourceFolderType
                        ResourceType[] resTypes = FolderTypeRelationship.getRelatedResourceTypes(
                                type);

// for each of those, make sure to find one folder to touch so that the
// list of ResourceItem associated with the type is rebuilt.
for (ResourceType resType : resTypes) {
// get the list of folder that can generate this type
                            ResourceFolderType[] folderTypes =
FolderTypeRelationship.getRelatedFolders(resType);

// we only need to touch one folder in any of those (since it's one
//Synthetic comment -- @@ -238,14 +238,14 @@
// If not, we look for the actual content to give us the resource type.

for (ResourceFolderType folderType : mFolderMap.keySet()) {
            ResourceType types[] = FolderTypeRelationship.getRelatedResourceTypes(folderType);
            if (types.length == 1) {
// before we add it we check if it's not already present, since a ResourceType
// could be created from multiple folders, even for the folders that only create
// one type of resource (drawable for instance, can be created from drawable/ and
// values/)
                if (list.indexOf(types[0]) == -1) {
                    list.add(types[0]);
}
} else {
// there isn't a single resource type out of this folder, so we look for all
//Synthetic comment -- @@ -779,7 +779,7 @@
*/
private void checkAndUpdate(ResourceType type) {
// get the list of folder that can output this type
        ResourceFolderType[] folderTypes = FolderTypeRelationship.getRelatedFolders(type);

for (ResourceFolderType folderType : folderTypes) {
List<ResourceFolder> folders = mFolderMap.get(folderType);
//Synthetic comment -- @@ -790,8 +790,8 @@
// if this folder is touched we need to update all the types that can
// be generated from a file in this folder.
// This will include 'type' obviously.
                        ResourceType[] resTypes = FolderTypeRelationship.getRelatedResourceTypes(
                                folderType);
for (ResourceType resType : resTypes) {
update(resType);
}
//Synthetic comment -- @@ -824,7 +824,7 @@
}

// get the list of folder that can output this type
        ResourceFolderType[] folderTypes = FolderTypeRelationship.getRelatedFolders(type);

for (ResourceFolderType folderType : folderTypes) {
List<ResourceFolder> folders = mFolderMap.get(folderType);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/Resource.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/Resource.java
//Synthetic comment -- index fd9005b..0ad5c39 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
*/
public abstract class Resource {
private boolean mTouched = true;
    
/**
* Returns the {@link FolderConfiguration} for this object.
*/
//Synthetic comment -- @@ -33,13 +33,13 @@
* Indicates that the underlying file was changed.
*/
public final void touch() {
       mTouched = true; 
}
    
public final boolean isTouched() {
return mTouched;
}
    
public final void resetTouch() {
mTouched = false;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFile.java
//Synthetic comment -- index 1890f41..a6e10ae 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.resources.ResourceType;

import java.util.Collection;

/**
* Represents a Resource file (a file under $Project/res/)
//Synthetic comment -- @@ -67,9 +68,9 @@
}

/**
     * Returns the list of {@link ResourceType} generated by the file.
*/
    public abstract ResourceType[] getResourceTypes();

/**
* Returns whether the file generated a resource of a specific type.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java
//Synthetic comment -- index 270fc3e..9cc36bb 100644

//Synthetic comment -- @@ -29,6 +29,7 @@

import java.util.ArrayList;
import java.util.Collection;

/**
* Resource Folder class. Contains list of {@link ResourceFile}s,
//Synthetic comment -- @@ -124,15 +125,13 @@

if (mFiles != null) {
for (ResourceFile file : mFiles) {
                ResourceType[] types = file.getResourceTypes();

// loop through those and add them to the main list,
// if they are not already present
                if (types != null) {
                    for (ResourceType resType : types) {
                        if (list.indexOf(resType) == -1) {
                            list.add(resType);
                        }
}
}
}
//Synthetic comment -- @@ -218,7 +217,7 @@
public boolean hasResources(ResourceType type) {
// Check if the folder type is able to generate resource of the type that was asked.
// this is a first check to avoid going through the files.
        ResourceFolderType[] folderTypes = FolderTypeRelationship.getRelatedFolders(type);

boolean valid = false;
for (ResourceFolderType rft : folderTypes) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index 5e5f78d..de07011 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
* The ResourceManager tracks resources for all opened projects.
//Synthetic comment -- @@ -625,9 +626,9 @@
// the ability to have 2 files in the same folder generating 2 different types of
// resource. The former is handled by MultiResourceFile properly while we don't
// handle the latter. If we were to add this behavior we'd have to change this call.
            ResourceType[] types = FolderTypeRelationship.getRelatedResourceTypes(type);

            if (types.length == 1) {
resFile = new SingleResourceFile(file, folder);
} else {
resFile = new MultiResourceFile(file, folder);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/SingleResourceFile.java
//Synthetic comment -- index 2a44e52..cd32cd4 100644

//Synthetic comment -- @@ -24,6 +24,7 @@

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.SAXParserFactory;

//Synthetic comment -- @@ -49,8 +50,8 @@

// we need to infer the type of the resource from the folder type.
// This is easy since this is a single Resource file.
        ResourceType[] types = FolderTypeRelationship.getRelatedResourceTypes(folder.getType());
        mType = types[0];

// compute the resource name
mResourceName = getResourceName(mType);
//Synthetic comment -- @@ -72,7 +73,7 @@
}

@Override
    public ResourceType[] getResourceTypes() {
return FolderTypeRelationship.getRelatedResourceTypes(getFolder().getType());
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationShipTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationShipTest.java
//Synthetic comment -- index 24ac601..c952d43 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
// loop on all the enum, and make sure there's at least one folder type for it.
for (ResourceType type : ResourceType.values()) {
assertTrue(type.getDisplayName(),
                    FolderTypeRelationship.getRelatedFolders(type).length > 0);
}
}

//Synthetic comment -- @@ -37,7 +37,7 @@
// loop on all the enum, and make sure there's at least one res type for it.
for (ResourceFolderType type : ResourceFolderType.values()) {
assertTrue(type.getName(),
                    FolderTypeRelationship.getRelatedResourceTypes(type).length > 0);
}
}








