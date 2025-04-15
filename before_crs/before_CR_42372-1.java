/*Add Translation dialog

This CL adds a dialog to the locale menu in the configuration chooser
which makes it easy to add a new language into the set of languages
used by the project.

Also add some null annotations.

Change-Id:I70ea2f623e6c56684e7b2a51b391f472bf31529b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index 1e55274..178d4be 100644

//Synthetic comment -- @@ -1089,7 +1089,8 @@
}

/**
     * Ensure that a given folder (and all its parents) are created
*
* @param container the container to ensure exists
* @throws CoreException if an error occurs








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 1e80ce1..4324b10 100644

//Synthetic comment -- @@ -57,6 +57,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.Density;
import com.android.resources.NightMode;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -105,6 +106,7 @@
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorPart;
//Synthetic comment -- @@ -1745,6 +1747,21 @@
});
}

Rectangle bounds = combo.getBounds();
Point location = new Point(bounds.x, bounds.y + bounds.height);
location = combo.getParent().toDisplay(location);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LocaleManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LocaleManager.java
//Synthetic comment -- index e4da6e8..0d30011 100644

//Synthetic comment -- @@ -269,6 +269,16 @@
}

/**
* Populate the various maps.
* <p>
* The language to region mapping was constructed by using the ISO 639-1 table from








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintList.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintList.java
//Synthetic comment -- index 3b01e05..ccb04bb 100644

//Synthetic comment -- @@ -166,7 +166,7 @@
mTree.addPaintListener(new PaintListener() {
@Override
public void paintControl(PaintEvent e) {
                treePainted = true;
mTreeViewer.getTree().removePaintListener(this);
}
});
//Synthetic comment -- @@ -212,7 +212,7 @@
});
}

    private boolean treePainted;

private void updateColumnWidths() {
Rectangle r = mTree.getClientArea();
//Synthetic comment -- @@ -631,7 +631,7 @@
LintColumn column = (LintColumn) treeColumn.getData(KEY_COLUMN);
// Workaround for TeeColumn.getWidth() returning 0 in some cases,
// see https://bugs.eclipse.org/341865 for details.
            int width = getColumnWidth(column, treePainted);
columnEntry.putInteger(getKey(treeColumn), width);
columns[positions[i]] = column;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index e6e3acb..ccd1666 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.IntArrayWrapper;
import com.android.ide.common.resources.ResourceFolder;
//Synthetic comment -- @@ -81,8 +82,9 @@
* @return a map with guaranteed to contain an entry for each {@link ResourceType}
*/
@Override
public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
            FolderConfiguration referenceConfig) {

Map<ResourceType, Map<String, ResourceValue>> resultMap =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);
//Synthetic comment -- @@ -232,7 +234,8 @@
}

@Override
    protected ResourceItem createResourceItem(String name) {
return new ResourceItem(name);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/AddTranslationDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/AddTranslationDialog.java
new file mode 100644
//Synthetic comment -- index 0000000..b4ce446

//Synthetic comment -- @@ -0,0 +1,649 @@








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/FrameworkResources.java b/ide_common/src/com/android/ide/common/resources/FrameworkResources.java
//Synthetic comment -- index 18bbc10..2115cdc 100755

//Synthetic comment -- @@ -71,7 +71,8 @@
* @return a collection of items, possible empty.
*/
@Override
    public List<ResourceItem> getResourceItemsOfType(ResourceType type) {
return mPublicResourceMap.get(type);
}

//Synthetic comment -- @@ -81,12 +82,13 @@
* @return true if the repository contains resources of the given type, false otherwise.
*/
@Override
    public boolean hasResourcesOfType(ResourceType type) {
return mPublicResourceMap.get(type).size() > 0;
}

@Override
    protected ResourceItem createResourceItem(String name) {
return new FrameworkResourceItem(name);
}









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceRepository.java b/ide_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 7e0338f..6fb8457 100755

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ide.common.resources;

import com.android.AndroidConstants;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.configuration.Configurable;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -90,8 +92,10 @@
* @param folder The workspace folder object.
* @return the {@link ResourceFolder} object associated to this folder.
*/
    private ResourceFolder add(ResourceFolderType type, FolderConfiguration config,
            IAbstractFolder folder) {
// get the list for the resource type
List<ResourceFolder> list = mFolderMap.get(type);

//Synthetic comment -- @@ -128,10 +132,14 @@
* Removes a {@link ResourceFolder} associated with the specified {@link IAbstractFolder}.
* @param type The type of the folder
* @param removedFolder the IAbstractFolder object.
* @return the {@link ResourceFolder} that was removed, or null if no matches were found.
*/
    public ResourceFolder removeFolder(ResourceFolderType type, IAbstractFolder removedFolder,
            ScanningContext context) {
// get the list of folders for the resource type.
List<ResourceFolder> list = mFolderMap.get(type);

//Synthetic comment -- @@ -162,7 +170,7 @@
* @param url the resource URL
* @return true if the resource is known
*/
    public boolean hasResourceItem(String url) {
assert url.startsWith("@") : url;

int typeEnd = url.indexOf('/', 1);
//Synthetic comment -- @@ -195,7 +203,7 @@
* @param name the name of the resource
* @return true if the resource is known
*/
    public boolean hasResourceItem(ResourceType type, String name) {
Map<String, ResourceItem> map = mResourceMap.get(type);

if (map != null) {
//Synthetic comment -- @@ -217,7 +225,8 @@
* @param name the name of the resource.
* @return A resource item matching the type and name.
*/
    protected ResourceItem getResourceItem(ResourceType type, String name) {
// looking for an existing ResourceItem with this type and name
ResourceItem item = findDeclaredResourceItem(type, name);

//Synthetic comment -- @@ -289,14 +298,16 @@
* @param name the name of the resource
* @return a new ResourceItem (or child class) instance.
*/
    protected abstract ResourceItem createResourceItem(String name);

/**
* Processes a folder and adds it to the list of existing folders.
* @param folder the folder to process
* @return the ResourceFolder created from this folder, or null if the process failed.
*/
    public ResourceFolder processFolder(IAbstractFolder folder) {
// split the name of the folder in segments.
String[] folderSegments = folder.getName().split(AndroidConstants.RES_QUALIFIER_SEP);

//Synthetic comment -- @@ -319,10 +330,12 @@
* Returns a list of {@link ResourceFolder} for a specific {@link ResourceFolderType}.
* @param type The {@link ResourceFolderType}
*/
    public List<ResourceFolder> getFolders(ResourceFolderType type) {
return mFolderMap.get(type);
}

public List<ResourceType> getAvailableResourceTypes() {
List<ResourceType> list = new ArrayList<ResourceType>();

//Synthetic comment -- @@ -366,7 +379,8 @@
* @param type the type of the resource items to return
* @return a non null collection of resource items
*/
    public Collection<ResourceItem> getResourceItemsOfType(ResourceType type) {
Map<String, ResourceItem> map = mResourceMap.get(type);

if (map == null) {
//Synthetic comment -- @@ -387,7 +401,7 @@
* @param type the type of resource to check.
* @return true if the repository contains resources of the given type, false otherwise.
*/
    public boolean hasResourcesOfType(ResourceType type) {
Map<String, ResourceItem> items = mResourceMap.get(type);
return (items != null && items.size() > 0);
}
//Synthetic comment -- @@ -397,7 +411,8 @@
* @param folder The {@link IAbstractFolder} object.
* @return the {@link ResourceFolder} or null if it was not found.
*/
    public ResourceFolder getResourceFolder(IAbstractFolder folder) {
Collection<List<ResourceFolder>> values = mFolderMap.values();

if (values.isEmpty()) { // This shouldn't be necessary, but has been observed
//Synthetic comment -- @@ -427,8 +442,9 @@
* layouts, bitmap based drawable, xml, anims).
* @return the matching file or <code>null</code> if no match was found.
*/
    public ResourceFile getMatchingFile(String name, ResourceFolderType type,
            FolderConfiguration config) {
// get the folders for the given type
List<ResourceFolder> folders = mFolderMap.get(type);

//Synthetic comment -- @@ -467,8 +483,9 @@
*
* @return a list of files generating this resource or null if it was not found.
*/
    public List<ResourceFile> getSourceFiles(ResourceType type, String name,
            FolderConfiguration referenceConfig) {

Collection<ResourceItem> items = getResourceItemsOfType(type);

//Synthetic comment -- @@ -497,8 +514,9 @@
* @param referenceConfig the configuration that each value must match.
* @return a map with guaranteed to contain an entry for each {@link ResourceType}
*/
public Map<ResourceType, Map<String, ResourceValue>> getConfiguredResources(
            FolderConfiguration referenceConfig) {
return doGetConfiguredResources(referenceConfig);
}

//Synthetic comment -- @@ -509,8 +527,9 @@
* @param referenceConfig the configuration that each value must match.
* @return a map with guaranteed to contain an entry for each {@link ResourceType}
*/
protected final Map<ResourceType, Map<String, ResourceValue>> doGetConfiguredResources(
            FolderConfiguration referenceConfig) {

Map<ResourceType, Map<String, ResourceValue>> map =
new EnumMap<ResourceType, Map<String, ResourceValue>>(ResourceType.class);
//Synthetic comment -- @@ -526,6 +545,7 @@
/**
* Returns the sorted list of languages used in the resources.
*/
public SortedSet<String> getLanguages() {
SortedSet<String> set = new TreeSet<String>();

//Synthetic comment -- @@ -547,7 +567,8 @@
* Returns the sorted list of regions used in the resources with the given language.
* @param currentLanguage the current language the region must be associated with.
*/
    public SortedSet<String> getRegions(String currentLanguage) {
SortedSet<String> set = new TreeSet<String>();

Collection<List<ResourceFolder>> folderList = mFolderMap.values();
//Synthetic comment -- @@ -577,7 +598,7 @@
* resource folder (res/)
* @throws IOException
*/
    public void loadResources(IAbstractFolder rootFolder)
throws IOException {
ScanningContext context = new ScanningContext(this);

//Synthetic comment -- @@ -603,13 +624,14 @@
}


    protected void removeFile(Collection<ResourceType> types, ResourceFile file) {
for (ResourceType type : types) {
removeFile(type, file);
}
}

    protected void removeFile(ResourceType type, ResourceFile file) {
Map<String, ResourceItem> map = mResourceMap.get(type);
if (map != null) {
Collection<ResourceItem> values = map.values();
//Synthetic comment -- @@ -626,8 +648,9 @@
* @param type the type of the resources.
* @param referenceConfig the configuration to best match.
*/
    private Map<String, ResourceValue> getConfiguredResource(ResourceType type,
            FolderConfiguration referenceConfig) {

// get the resource item for the given type
Map<String, ResourceItem> items = mResourceMap.get(type);
//Synthetic comment -- @@ -678,7 +701,9 @@
* @param name the Resource name.
* @return the existing ResourceItem or null if no match was found.
*/
    private ResourceItem findDeclaredResourceItem(ResourceType type, String name) {
Map<String, ResourceItem> map = mResourceMap.get(type);

if (map != null) {







