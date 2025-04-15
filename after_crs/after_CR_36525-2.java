/*Performance improvement

List changed to map

Change-Id:I8145a2e177937cbeb1f6b06b2e313faf8e3a1176*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/FrameworkResources.java b/ide_common/src/com/android/ide/common/resources/FrameworkResources.java
old mode 100644
new mode 100755
//Synthetic comment -- index 381516c..4d8e681

//Synthetic comment -- @@ -120,34 +120,6 @@

ResourceType lastType = null;
String lastTypeName = "";
while (true) {
int event = parser.next();
if (event == XmlPullParser.START_TAG) {
//Synthetic comment -- @@ -183,23 +155,9 @@
}
if (type != null) {
ResourceItem match = null;
                                Map<String, ResourceItem> map = mResourceMap.get(type);
if (map != null) {
match = map.get(name);
}

if (match != null) {
//Synthetic comment -- @@ -276,3 +234,4 @@
}
}
}









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceRepository.java b/ide_common/src/com/android/ide/common/resources/ResourceRepository.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3040dc0..48c9690

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

//Synthetic comment -- @@ -58,11 +60,12 @@
protected final Map<ResourceFolderType, List<ResourceFolder>> mFolderMap =
new EnumMap<ResourceFolderType, List<ResourceFolder>>(ResourceFolderType.class);

    protected final Map<ResourceType, Map<String, ResourceItem>> mResourceMap = 
            new EnumMap<ResourceType, Map<String, ResourceItem>>(
            ResourceType.class);

    private final Map<Map<String, ResourceItem>, Collection<ResourceItem>> mReadOnlyListMap = 
            new IdentityHashMap<Map<String, ResourceItem>, Collection<ResourceItem>>();

private final boolean mFrameworkRepository;

//Synthetic comment -- @@ -193,13 +196,13 @@
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

//Synthetic comment -- @@ -225,16 +228,19 @@

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

//Synthetic comment -- @@ -324,16 +330,16 @@
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
//Synthetic comment -- @@ -345,7 +351,7 @@
* @return true if the repository contains resources of the given type, false otherwise.
*/
public boolean hasResourcesOfType(ResourceType type) {
        Map<String, ResourceItem> items = mResourceMap.get(type);
return (items != null && items.size() > 0);
}

//Synthetic comment -- @@ -567,10 +573,10 @@
}

protected void removeFile(ResourceType type, ResourceFile file) {
        Map<String, ResourceItem> map = mResourceMap.get(type);
        if (map != null) {
            Collection<ResourceItem> values = map.values();
            for (ResourceItem item : values) {
item.removeFile(file);
}
}
//Synthetic comment -- @@ -587,7 +593,7 @@
FolderConfiguration referenceConfig) {

// get the resource item for the given type
        Map<String, ResourceItem> items = mResourceMap.get(type);
if (items == null) {
return new HashMap<String, ResourceValue>();
}
//Synthetic comment -- @@ -595,7 +601,7 @@
// create the map
HashMap<String, ResourceValue> map = new HashMap<String, ResourceValue>(items.size());

        for (ResourceItem item : items.values()) {
ResourceValue value = item.getResourceValue(type, referenceConfig,
isFrameworkRepository());
if (value != null) {
//Synthetic comment -- @@ -614,13 +620,15 @@
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
//Synthetic comment -- @@ -634,17 +642,16 @@
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








