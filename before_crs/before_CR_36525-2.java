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

                // Precompute maps from name to ResourceItem such that when we find
                // a public item's name we can quickly locate it. Without this,
                // it's a linear search for each item, n times -- O(n^2).
                // Precomputing a map is O(n) and looking up n times in the map is
                // also O(n).
                Map<ResourceType, Map<String, ResourceItem>> nameMap =
                        new HashMap<ResourceType, Map<String, ResourceItem>>();
                for (Entry<ResourceType, List<ResourceItem>> entry: mResourceMap.entrySet()) {
                    ResourceType type = entry.getKey();
                    if (type == ResourceType.PUBLIC || type == ResourceType.DECLARE_STYLEABLE) {
                        // These are large maps (in android-15 for example the "public"
                        // ResourceType has 1734 items and declare-styleable has 210) that
                        // currently have no public exported names. Therefore, don't bother
                        // creating name lookup maps for these. (However, if by chance a future
                        // public.xml file does specify these, it will be found by the sequential
                        // search if map=null below.)
                        continue;
                    }
                    List<ResourceItem> items = entry.getValue();
                    int size = items.size();
                    Map<String, ResourceItem> map = new HashMap<String, ResourceItem>(size);
                    for (ResourceItem item : items) {
                        map.put(item.getName(), item);
                    }
                    nameMap.put(type, map);
                }

while (true) {
int event = parser.next();
if (event == XmlPullParser.START_TAG) {
//Synthetic comment -- @@ -183,23 +155,9 @@
}
if (type != null) {
ResourceItem match = null;
                                Map<String, ResourceItem> map = nameMap.get(type);
if (map != null) {
match = map.get(name);
                                } else {
                                    // We skipped computing name maps for some large lists
                                    // that currently don't have any public names, but
                                    // on the off chance that they will show up, leave the
                                    // old iteration based lookup here
                                    List<ResourceItem> typeList = mResourceMap.get(type);
                                    if (typeList != null) {
                                        for (ResourceItem item : typeList) {
                                            if (name.equals(item.getName())) {
                                                match = item;
                                                break;
                                            }
                                        }
                                    }
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
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

//Synthetic comment -- @@ -58,11 +60,12 @@
protected final Map<ResourceFolderType, List<ResourceFolder>> mFolderMap =
new EnumMap<ResourceFolderType, List<ResourceFolder>>(ResourceFolderType.class);

    protected final Map<ResourceType, List<ResourceItem>> mResourceMap =
        new EnumMap<ResourceType, List<ResourceItem>>(ResourceType.class);

    private final Map<List<ResourceItem>, List<ResourceItem>> mReadOnlyListMap =
        new IdentityHashMap<List<ResourceItem>, List<ResourceItem>>();

private final boolean mFrameworkRepository;

//Synthetic comment -- @@ -193,13 +196,13 @@
* @return true if the resource is known
*/
public boolean hasResourceItem(ResourceType type, String name) {
        List<ResourceItem> list = mResourceMap.get(type);

        if (list != null) {
            for (ResourceItem item : list) {
                if (name.equals(item.getName())) {
                    return true;
                }
}
}

//Synthetic comment -- @@ -225,16 +228,19 @@

item = createResourceItem(name);

            List<ResourceItem> list = mResourceMap.get(type);
            if (list == null) {
                list = new ArrayList<ResourceItem>();
                mResourceMap.put(type, list);
}

            list.add(item);

if (oldItem != null) {
                list.remove(oldItem);
}
}

//Synthetic comment -- @@ -324,16 +330,16 @@
* @return a non null collection of resource items
*/
public Collection<ResourceItem> getResourceItemsOfType(ResourceType type) {
        List<ResourceItem> list = mResourceMap.get(type);

        if (list == null) {
return Collections.emptyList();
}

        List<ResourceItem> roList = mReadOnlyListMap.get(list);
if (roList == null) {
            roList = Collections.unmodifiableList(list);
            mReadOnlyListMap.put(list, roList);
}

return roList;
//Synthetic comment -- @@ -345,7 +351,7 @@
* @return true if the repository contains resources of the given type, false otherwise.
*/
public boolean hasResourcesOfType(ResourceType type) {
        List<ResourceItem> items = mResourceMap.get(type);
return (items != null && items.size() > 0);
}

//Synthetic comment -- @@ -567,10 +573,10 @@
}

protected void removeFile(ResourceType type, ResourceFile file) {
        List<ResourceItem> list = mResourceMap.get(type);
        if (list != null) {
            for (int i = 0 ; i < list.size(); i++) {
                ResourceItem item = list.get(i);
item.removeFile(file);
}
}
//Synthetic comment -- @@ -587,7 +593,7 @@
FolderConfiguration referenceConfig) {

// get the resource item for the given type
        List<ResourceItem> items = mResourceMap.get(type);
if (items == null) {
return new HashMap<String, ResourceValue>();
}
//Synthetic comment -- @@ -595,7 +601,7 @@
// create the map
HashMap<String, ResourceValue> map = new HashMap<String, ResourceValue>(items.size());

        for (ResourceItem item : items) {
ResourceValue value = item.getResourceValue(type, referenceConfig,
isFrameworkRepository());
if (value != null) {
//Synthetic comment -- @@ -614,13 +620,15 @@
// Since removed files/folders remove source files from existing ResourceItem, loop through
// all resource items and remove the ones that have no source files.

        Collection<List<ResourceItem>> lists = mResourceMap.values();
        for (List<ResourceItem> list : lists) {
            for (int i = 0 ; i < list.size() ;) {
                if (list.get(i).hasNoSourceFile()) {
                    list.remove(i);
                } else {
                    i++;
}
}
}
//Synthetic comment -- @@ -634,17 +642,16 @@
* @return the existing ResourceItem or null if no match was found.
*/
private ResourceItem findDeclaredResourceItem(ResourceType type, String name) {
        List<ResourceItem> list = mResourceMap.get(type);

        if (list != null) {
            for (ResourceItem item : list) {
                // ignore inline
                if (name.equals(item.getName()) && item.isDeclaredInline() == false) {
                    return item;
                }
}
}

return null;
}
}







