/*Performance improvement

List changed to map
Signed-off-by: Michal Sova <michalsova77@gmail.com>

Change-Id:I8145a2e177937cbeb1f6b06b2e313faf8e3a1176*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/FrameworkResources.java b/ide_common/src/com/android/ide/common/resources/FrameworkResources.java
old mode 100644
new mode 100755
//Synthetic comment -- index 381516c..2ebedbd

//Synthetic comment -- @@ -36,39 +36,36 @@
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Framework resources repository. This behaves the same as
 * {@link ResourceRepository} except that it differentiates between resources
 * that are public and non public. {@link #getResources(ResourceType)} and
 * {@link #hasResourcesOfType(ResourceType)} only return public resources. This
 * is typically used to display resource lists in the UI.
* {@link #getConfiguredResources(com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration)}
 * returns all resources, even the non public ones so that this can be used for
 * rendering.
*/
public class FrameworkResources extends ResourceRepository {

/**
     * Map of {@link ResourceType} to list of items. It is guaranteed to contain
     * a list for all possible values of ResourceType.
*/
    protected final Map<ResourceType, List<ResourceItem>> mPublicResourceMap = new EnumMap<ResourceType, List<ResourceItem>>(
            ResourceType.class);

public FrameworkResources() {
        super(true /* isFrameworkRepository */);
}

/**
     * Returns a {@link Collection} (always non null, but can be empty) of
     * <b>public</b> {@link ResourceItem} matching a given {@link ResourceType}.
     * 
* @param type the type of the resources to return
* @return a collection of items, possible empty.
*/
//Synthetic comment -- @@ -78,9 +75,12 @@
}

/**
     * Returns whether the repository has <b>public</b> resources of a given
     * {@link ResourceType}.
     * 
* @param type the type of resource to check.
     * @return true if the repository contains resources of the given type,
     *         false otherwise.
*/
@Override
public boolean hasResourcesOfType(ResourceType type) {
//Synthetic comment -- @@ -93,16 +93,16 @@
}

/**
     * Reads the public.xml file in data/res/values/ for a given resource folder
     * and builds up a map of public resources. This map is a subset of the full
     * resource map that only contains framework resources that are public.
     * 
* @param resFolder The root folder of the resources
* @param logger a logger to report issues to
*/
    public void loadPublicResources(@NonNull
    IAbstractFolder resFolder, @Nullable
    ILogger logger) {
IAbstractFolder valueFolder = resFolder.getFolder(FD_RES_VALUES);
if (valueFolder.exists() == false) {
return;
//Synthetic comment -- @@ -120,38 +120,11 @@

ResourceType lastType = null;
String lastTypeName = "";
while (true) {
int event = parser.next();
if (event == XmlPullParser.START_TAG) {
                        // As of API 15 there are a number of "java-symbol"
                        // entries here
if (!parser.getName().equals("public")) { //$NON-NLS-1$
continue;
}
//Synthetic comment -- @@ -183,40 +156,39 @@
}
if (type != null) {
ResourceItem match = null;
                                Map<String, ResourceItem> map = mResourceMap.get(type);
if (map != null) {
match = map.get(name);
}

if (match != null) {
List<ResourceItem> publicList = mPublicResourceMap.get(type);
if (publicList == null) {
                                        // Pick initial size for the list to
                                        // hold the public
                                        // resources. We could just use
                                        // map.size() here,
                                        // but they're usually much bigger; for
                                        // example,
                                        // in one platform version, there are
                                        // 1500 drawables
                                        // and 1200 strings but only 175 and 25
                                        // public ones
// respectively.
int size;
switch (type) {
                                            case STYLE:
                                                size = 500;
                                                break;
                                            case ATTR:
                                                size = 1000;
                                                break;
                                            case DRAWABLE:
                                                size = 200;
                                                break;
                                            case ID:
                                                size = 50;
                                                break;
case LAYOUT:
case COLOR:
case STRING:
//Synthetic comment -- @@ -234,11 +206,13 @@

publicList.add(match);
} else {
                                    // log that there's a public resource that
                                    // doesn't actually
// exist?
}
} else {
                                // log that there was a reference to a typo that
                                // doesn't actually
// exist?
}
}
//Synthetic comment -- @@ -255,7 +229,8 @@
try {
reader.close();
} catch (IOException e) {
                        // Nothing to be done here - we don't care if it closed
                        // or not.
}
}
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceRepository.java b/ide_common/src/com/android/ide/common/resources/ResourceRepository.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3040dc0..2ff3391

//Synthetic comment -- @@ -36,8 +36,10 @@
import java.util.EnumMap;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

//Synthetic comment -- @@ -58,11 +60,10 @@
protected final Map<ResourceFolderType, List<ResourceFolder>> mFolderMap =
new EnumMap<ResourceFolderType, List<ResourceFolder>>(ResourceFolderType.class);

    protected final Map<ResourceType, Map<String, ResourceItem>> mResourceMap = new EnumMap<ResourceType, Map<String, ResourceItem>>(
            ResourceType.class);

    private final Map<Map<String, ResourceItem>, Collection<ResourceItem>> mReadOnlyListMap = new IdentityHashMap<Map<String, ResourceItem>, Collection<ResourceItem>>();

private final boolean mFrameworkRepository;

//Synthetic comment -- @@ -193,13 +194,13 @@
* @return true if the resource is known
*/
public boolean hasResourceItem(ResourceType type, String name) {
        Map<String, ResourceItem> map = mResourceMap.get(type);

        if (map != null) {

            ResourceItem resourceItem = map.get(name);
            if (resourceItem != null) {
                return true;
}
}

//Synthetic comment -- @@ -225,16 +226,19 @@

item = createResourceItem(name);

            Map<String, ResourceItem> map = mResourceMap.get(type);

            if (map == null) {
                map = new HashMap<String, ResourceItem>();
                mResourceMap.put(type, map);

}

            map.put(item.getName(), item);

if (oldItem != null) {
                map.remove(oldItem.getName());

}
}

//Synthetic comment -- @@ -324,16 +328,16 @@
* @return a non null collection of resource items
*/
public Collection<ResourceItem> getResourceItemsOfType(ResourceType type) {
        Map<String, ResourceItem> map = mResourceMap.get(type);

        if (map == null) {
return Collections.emptyList();
}

        Collection<ResourceItem> roList = mReadOnlyListMap.get(map);
if (roList == null) {
            roList = Collections.unmodifiableCollection(map.values());
            mReadOnlyListMap.put(map, roList);
}

return roList;
//Synthetic comment -- @@ -345,7 +349,7 @@
* @return true if the repository contains resources of the given type, false otherwise.
*/
public boolean hasResourcesOfType(ResourceType type) {
        Map<String, ResourceItem> items = mResourceMap.get(type);
return (items != null && items.size() > 0);
}

//Synthetic comment -- @@ -559,7 +563,7 @@
}
}

    
protected void removeFile(Collection<ResourceType> types, ResourceFile file) {
for (ResourceType type : types) {
removeFile(type, file);
//Synthetic comment -- @@ -567,10 +571,10 @@
}

protected void removeFile(ResourceType type, ResourceFile file) {
        Map<String, ResourceItem> map = mResourceMap.get(type);
        if (map != null) {
            Collection<ResourceItem> values = map.values();
            for (ResourceItem item : values) {
item.removeFile(file);
}
}
//Synthetic comment -- @@ -587,7 +591,7 @@
FolderConfiguration referenceConfig) {

// get the resource item for the given type
        Map<String, ResourceItem> items = mResourceMap.get(type);
if (items == null) {
return new HashMap<String, ResourceValue>();
}
//Synthetic comment -- @@ -595,7 +599,7 @@
// create the map
HashMap<String, ResourceValue> map = new HashMap<String, ResourceValue>(items.size());

        for (ResourceItem item : items.values()) {
ResourceValue value = item.getResourceValue(type, referenceConfig,
isFrameworkRepository());
if (value != null) {
//Synthetic comment -- @@ -606,21 +610,22 @@
return map;
}

    
/**
* Cleans up the repository of resource items that have no source file anymore.
*/
public void postUpdateCleanUp() {
// Since removed files/folders remove source files from existing ResourceItem, loop through
// all resource items and remove the ones that have no source files.
        Collection<Map<String, ResourceItem>> maps = mResourceMap.values();
        for (Map<String, ResourceItem> map : maps) {
            Set<String> keySet = map.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String name = iterator.next();
                ResourceItem resourceItem = map.get(name);
                if (resourceItem.hasNoSourceFile()) {
                    iterator.remove();
}
}
}
//Synthetic comment -- @@ -634,17 +639,13 @@
* @return the existing ResourceItem or null if no match was found.
*/
private ResourceItem findDeclaredResourceItem(ResourceType type, String name) {
        Map<String, ResourceItem> map = mResourceMap.get(type);
        if (map != null) {
            ResourceItem resourceItem = map.get(name);
            if (resourceItem != null && !resourceItem.isDeclaredInline()) {
                return resourceItem;
}
}
return null;
}
}







