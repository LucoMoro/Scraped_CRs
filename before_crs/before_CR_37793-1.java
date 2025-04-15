/*Set default sizes for framework resource maps to avoid resizing

Change-Id:I09b9538032dfff71f5e3b09d5271ebb286ed36c7*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceRepository.java b/ide_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 48c9690..7e0338f 100755

//Synthetic comment -- @@ -60,11 +60,11 @@
protected final Map<ResourceFolderType, List<ResourceFolder>> mFolderMap =
new EnumMap<ResourceFolderType, List<ResourceFolder>>(ResourceFolderType.class);

    protected final Map<ResourceType, Map<String, ResourceItem>> mResourceMap = 
new EnumMap<ResourceType, Map<String, ResourceItem>>(
ResourceType.class);

    private final Map<Map<String, ResourceItem>, Collection<ResourceItem>> mReadOnlyListMap = 
new IdentityHashMap<Map<String, ResourceItem>, Collection<ResourceItem>>();

private final boolean mFrameworkRepository;
//Synthetic comment -- @@ -231,9 +231,46 @@
Map<String, ResourceItem> map = mResourceMap.get(type);

if (map == null) {
                map = new HashMap<String, ResourceItem>();
mResourceMap.put(type, map);

}

map.put(item.getName(), item);







