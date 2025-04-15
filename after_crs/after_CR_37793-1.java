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
                if (isFrameworkRepository()) {
                    // Pick initial size for the maps. Also change the load factor to 1.0
                    // to avoid rehashing the whole table when we (as expected) get near
                    // the known rough size of each resource type map.
                    int size;
                    switch (type) {
                        // Based on counts in API 16. Going back to API 10, the counts
                        // are roughly 25-50% smaller (e.g. compared to the top 5 types below
                        // the fractions are 1107 vs 1734, 831 vs 1508, 895 vs 1255,
                        // 733 vs 1064 and 171 vs 783.
                        case PUBLIC:           size = 1734; break;
                        case DRAWABLE:         size = 1508; break;
                        case STRING:           size = 1255; break;
                        case ATTR:             size = 1064; break;
                        case STYLE:             size = 783; break;
                        case ID:                size = 347; break;
                        case DECLARE_STYLEABLE: size = 210; break;
                        case LAYOUT:            size = 187; break;
                        case COLOR:             size = 120; break;
                        case ANIM:               size = 95; break;
                        case DIMEN:              size = 81; break;
                        case BOOL:               size = 54; break;
                        case INTEGER:            size = 52; break;
                        case ARRAY:              size = 51; break;
                        case PLURALS:            size = 20; break;
                        case XML:                size = 14; break;
                        case INTERPOLATOR :      size = 13; break;
                        case ANIMATOR:            size = 8; break;
                        case RAW:                 size = 4; break;
                        case MENU:                size = 2; break;
                        case MIPMAP:              size = 2; break;
                        case FRACTION:            size = 1; break;
                        default:
                            size = 2;
                    }
                    map = new HashMap<String, ResourceItem>(size, 1.0f);
                } else {
                    map = new HashMap<String, ResourceItem>();
                }
mResourceMap.put(type, map);
}

map.put(item.getName(), item);







