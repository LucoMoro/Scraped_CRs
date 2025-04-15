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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Framework resources repository.
 *
 * This behaves the same as {@link ResourceRepository} except that it differentiates between
 * resources that are public and non public.
 * {@link #getResources(ResourceType)} and {@link #hasResourcesOfType(ResourceType)} only return
 * public resources. This is typically used to display resource lists in the UI.
 *
* {@link #getConfiguredResources(com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration)}
 * returns all resources, even the non public ones so that this can be used for rendering.
*/
public class FrameworkResources extends ResourceRepository {

/**
     * Map of {@link ResourceType} to list of items. It is guaranteed to contain a list for all
     * possible values of ResourceType.
*/
    protected final Map<ResourceType, List<ResourceItem>> mPublicResourceMap =
        new EnumMap<ResourceType, List<ResourceItem>>(ResourceType.class);

public FrameworkResources() {
        super(true /*isFrameworkRepository*/);
}

/**
     * Returns a {@link Collection} (always non null, but can be empty) of <b>public</b>
     * {@link ResourceItem} matching a given {@link ResourceType}.
     *
* @param type the type of the resources to return
* @return a collection of items, possible empty.
*/
//Synthetic comment -- @@ -78,9 +75,12 @@
}

/**
     * Returns whether the repository has <b>public</b> resources of a given {@link ResourceType}.
* @param type the type of resource to check.
     * @return true if the repository contains resources of the given type, false otherwise.
*/
@Override
public boolean hasResourcesOfType(ResourceType type) {
//Synthetic comment -- @@ -93,16 +93,16 @@
}

/**
     * Reads the public.xml file in data/res/values/ for a given resource folder and builds up
     * a map of public resources.
     *
     * This map is a subset of the full resource map that only contains framework resources
     * that are public.
     *
* @param resFolder The root folder of the resources
* @param logger a logger to report issues to
*/
    public void loadPublicResources(@NonNull IAbstractFolder resFolder, @Nullable ILogger logger) {
IAbstractFolder valueFolder = resFolder.getFolder(FD_RES_VALUES);
if (valueFolder.exists() == false) {
return;
//Synthetic comment -- @@ -120,38 +120,11 @@

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
                        // As of API 15 there are a number of "java-symbol" entries here
if (!parser.getName().equals("public")) { //$NON-NLS-1$
continue;
}
//Synthetic comment -- @@ -183,40 +156,39 @@
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
List<ResourceItem> publicList = mPublicResourceMap.get(type);
if (publicList == null) {
                                        // Pick initial size for the list to hold the public
                                        // resources. We could just use map.size() here,
                                        // but they're usually much bigger; for example,
                                        // in one platform version, there are 1500 drawables
                                        // and 1200 strings but only 175 and 25 public ones
// respectively.
int size;
switch (type) {
                                            case STYLE: size = 500; break;
                                            case ATTR: size = 1000; break;
                                            case DRAWABLE: size = 200; break;
                                            case ID: size = 50; break;
case LAYOUT:
case COLOR:
case STRING:
//Synthetic comment -- @@ -234,11 +206,13 @@

publicList.add(match);
} else {
                                    // log that there's a public resource that doesn't actually
// exist?
}
} else {
                                // log that there was a reference to a typo that doesn't actually
// exist?
}
}
//Synthetic comment -- @@ -255,7 +229,8 @@
try {
reader.close();
} catch (IOException e) {
                        // Nothing to be done here - we don't care if it closed or not.
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
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

//Synthetic comment -- @@ -58,11 +60,10 @@
protected final Map<ResourceFolderType, List<ResourceFolder>> mFolderMap =
new EnumMap<ResourceFolderType, List<ResourceFolder>>(ResourceFolderType.class);

    protected final Map<ResourceType, List<ResourceItem>> mResourceMap =
        new EnumMap<ResourceType, List<ResourceItem>>(ResourceType.class);

    private final Map<List<ResourceItem>, List<ResourceItem>> mReadOnlyListMap =
        new IdentityHashMap<List<ResourceItem>, List<ResourceItem>>();

private final boolean mFrameworkRepository;

//Synthetic comment -- @@ -193,13 +194,13 @@
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

//Synthetic comment -- @@ -225,16 +226,19 @@

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

//Synthetic comment -- @@ -324,16 +328,16 @@
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
//Synthetic comment -- @@ -345,7 +349,7 @@
* @return true if the repository contains resources of the given type, false otherwise.
*/
public boolean hasResourcesOfType(ResourceType type) {
        List<ResourceItem> items = mResourceMap.get(type);
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
        List<ResourceItem> list = mResourceMap.get(type);
        if (list != null) {
            for (int i = 0 ; i < list.size(); i++) {
                ResourceItem item = list.get(i);
item.removeFile(file);
}
}
//Synthetic comment -- @@ -587,7 +591,7 @@
FolderConfiguration referenceConfig) {

// get the resource item for the given type
        List<ResourceItem> items = mResourceMap.get(type);
if (items == null) {
return new HashMap<String, ResourceValue>();
}
//Synthetic comment -- @@ -595,7 +599,7 @@
// create the map
HashMap<String, ResourceValue> map = new HashMap<String, ResourceValue>(items.size());

        for (ResourceItem item : items) {
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
//Synthetic comment -- @@ -634,17 +639,13 @@
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







