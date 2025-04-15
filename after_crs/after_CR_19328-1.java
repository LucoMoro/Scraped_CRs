/*ADT: Add support for some new resource types.

They are not technically new, but we never really
needed them. Turns out we do now (some are used by some widgets).

Also added an assert to detect new types of resources, and a test
to make sure that all resource types and folder types are added
to the folder-res relationship maps.

Change-Id:Ie4b6786ea6de9c69776794750732303b8d68b7bb*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceType.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceType.java
//Synthetic comment -- index f09719c..4555913 100644

//Synthetic comment -- @@ -23,17 +23,25 @@
ANIM("anim", "Animation"), //$NON-NLS-1$
ARRAY("array", "Array", "string-array", "integer-array"), //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$
ATTR("attr", "Attr"), //$NON-NLS-1$
    BOOL("bool", "Boolean"), //$NON-NLS-1$
COLOR("color", "Color"), //$NON-NLS-1$
    DECLARE_STYLEABLE("declare-styleable", "Declare Stylable"), //$NON-NLS-1$
DIMEN("dimen", "Dimension"), //$NON-NLS-1$
DRAWABLE("drawable", "Drawable"), //$NON-NLS-1$
    FRACTION("fraction", "Fraction"), //$NON-NLS-1$
ID("id", "ID"), //$NON-NLS-1$
    INTEGER("integer", "Integer"), //$NON-NLS-1$
LAYOUT("layout", "Layout"), //$NON-NLS-1$
MENU("menu", "Menu"), //$NON-NLS-1$
    PLURALS("plurals", "Plurals"), //$NON-NLS-1$
RAW("raw", "Raw"), //$NON-NLS-1$
STRING("string", "String"), //$NON-NLS-1$
STYLE("style", "Style"), //$NON-NLS-1$
STYLEABLE("styleable", "Styleable"), //$NON-NLS-1$
    XML("xml", "XML"), //$NON-NLS-1$
    // this is not actually used. Only there because they get parsed and since we want to
    // detect new resource type, we need to have this one exist.
    PUBLIC("public", "###"); //$NON-NLS-1$ //$NON-NLS-2$

private final String mName;
private final String mDisplayName;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationship.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationship.java
//Synthetic comment -- index 4b45f6e..c96182d 100644

//Synthetic comment -- @@ -28,39 +28,48 @@
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
add(ResourceType.LAYOUT, ResourceFolderType.LAYOUT, typeToFolderMap, folderToTypeMap);
add(ResourceType.MENU, ResourceFolderType.MENU, typeToFolderMap, folderToTypeMap);
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
//Synthetic comment -- @@ -74,7 +83,7 @@
}
return new ResourceType[0];
}

/**
* Returns a list of {@link ResourceFolderType} that can contain files generating resources
* of the specified type.
//Synthetic comment -- @@ -88,7 +97,7 @@
}
return new ResourceFolderType[0];
}

/**
* Returns true if the {@link ResourceType} and the {@link ResourceFolderType} values match.
* @param resType the resource type.
//Synthetic comment -- @@ -98,7 +107,7 @@
*/
public static boolean match(ResourceType resType, ResourceFolderType folderType) {
ResourceFolderType[] array = mTypeToFolderMap.get(resType);

if (array != null && array.length > 0) {
for (ResourceFolderType fType : array) {
if (fType == folderType) {
//Synthetic comment -- @@ -106,17 +115,17 @@
}
}
}

return false;
}

/**
* Adds a {@link ResourceType} - {@link ResourceFolderType} relationship. this indicates that
* a file in the folder can generate a resource of the specified type.
* @param type The resourceType
* @param folder The {@link ResourceFolderType}
     * @param folderToTypeMap
     * @param typeToFolderMap
*/
private static void add(ResourceType type, ResourceFolderType folder,
HashMap<ResourceType, List<ResourceFolderType>> typeToFolderMap,
//Synthetic comment -- @@ -130,7 +139,7 @@
if (folderList.indexOf(folder) == -1) {
folderList.add(folder);
}

// now we add the type to the list associated with the folder.
List<ResourceType> typeList = folderToTypeMap.get(folder);
if (typeList == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/MultiResourceFile.java
//Synthetic comment -- index ae56f67..a96a4e8 100644

//Synthetic comment -- @@ -136,6 +136,7 @@
*/
public void addResourceValue(String resType, ResourceValue value) {
ResourceType type = ResourceType.getEnum(resType);
        assert type != null;
if (type != null) {
HashMap<String, ResourceValue> list = mResourceItems.get(type);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationShipTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationShipTest.java
new file mode 100644
//Synthetic comment -- index 0000000..a865ded

//Synthetic comment -- @@ -0,0 +1,43 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.internal.resources.ResourceType;

import junit.framework.TestCase;

public class FolderTypeRelationShipTest extends TestCase {

    public void testResourceType() {
        // all resource type should be in the FolderTypeRelationShip map.
        // loop on all the enum, and make sure there's at least one folder type for it.
        for (ResourceType type : ResourceType.values()) {
            assertTrue(type.getDisplayName(),
                    FolderTypeRelationship.getRelatedFolders(type).length > 0);
        }
    }

    public void testResourceFolderType() {
        // all resource folder type should generate at least one type of resource.
        // loop on all the enum, and make sure there's at least one res type for it.
        for (ResourceFolderType type : ResourceFolderType.values()) {
            assertTrue(type.getName(),
                    FolderTypeRelationship.getRelatedResourceTypes(type).length > 0);
        }
    }

}







