/*Sdkman2: Simplify packages names, fix extra package sort.

Simplify package names when sorted by API, that is
don't repeat "API nn" or "Android x.y" in the package
names if it matches to category.

Fix sorting of packages to properly sort extras
by vendor+path. The previous sort was broken as
it was making the revision number more important
than the vendor+path for extras.

Change-Id:I51ed0114ab9c77ef5d94710fc2449e5be9cd2ad9*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
//Synthetic comment -- index c142faf..793c98d 100755

//Synthetic comment -- @@ -526,6 +526,24 @@
return false;
}

// ---

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index baa6706..faf5a00 100755

//Synthetic comment -- @@ -557,46 +557,83 @@
* <p/>
* This {@link #compareTo(Package)} method is purely an implementation detail to
* perform the right ordering of the packages in the list of available or installed packages.
*/
public int compareTo(Package other) {
        int s1 = this.sortingScore();
        int s2 = other.sortingScore();
        return s1 - s2;
}

/**
     * Computes the score for each package used by {@link #compareTo(Package)}.
*/
    private int sortingScore() {
        // up to 31 bits (for signed stuff)
        int type = 0;             // max type=7 => 3 bits
        int rev = getRevision();  // 12 bits... 4095
        int offset = 0;           // 16 bits...
if (this instanceof ToolPackage) {
            type = 6;
} else if (this instanceof PlatformToolPackage) {
            type = 5;
} else if (this instanceof DocPackage) {
            type = 4;
} else if (this instanceof PlatformPackage) {
            type = 3;
} else if (this instanceof SamplePackage) {
            type = 2;
} else if (this instanceof AddonPackage) {
            type = 1;
} else {
// extras and everything else
            type = 0;
}

if (this instanceof IPackageVersion) {
AndroidVersion v = ((IPackageVersion) this).getVersion();
            offset = v.getApiLevel();
            offset = offset * 2 + (v.isPreview() ? 1 : 0);
        }

        int n = (type << 28) + (offset << 12) + rev;
        return 0 - n;
}

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 570f769..2b7a0db 100755

//Synthetic comment -- @@ -147,6 +147,7 @@
private TreeViewerColumn mColumnStatus;
private Font mTreeFontItalic;
private TreeColumn mTreeColumnName;

public PackagesPage(Composite parent, int swtStyle, UpdaterData updaterData) {
super(parent, swtStyle);
//Synthetic comment -- @@ -532,7 +533,7 @@
}

private void sortPackages(boolean updateButtons) {
        if (mCheckSortApi != null && !mCheckSortApi.isDisposed() && mCheckSortApi.getSelection()) {
sortByApiLevel();
} else {
sortBySource();
//Synthetic comment -- @@ -543,6 +544,10 @@
}
}

/**
* Recompute the tree by sorting all the packages by API.
* This does an update in-place of the mCategories list so that the table
//Synthetic comment -- @@ -556,34 +561,44 @@
mTreeColumnName.setImage(getImage(ICON_SORT_BY_API));
}

// keep a map of the initial state so that we can detect which items or categories are
// no longer being used, so that we can removed them at the end of the in-place update.
        final Map<Integer, Pair<PkgCategory, HashSet<PkgItem>> > unusedItemsMap =
            new HashMap<Integer, Pair<PkgCategory, HashSet<PkgItem>> >();
        final Set<PkgCategory> unusedCatSet = new HashSet<PkgCategory>();

// get existing categories
for (PkgCategory cat : mCategories) {
            unusedCatSet.add(cat);
            unusedItemsMap.put(cat.getKey(), Pair.of(cat, new HashSet<PkgItem>(cat.getItems())));
}

// always add the tools & extras categories, even if empty (unlikely anyway)
        if (!unusedItemsMap.containsKey(PkgCategory.KEY_TOOLS)) {
            PkgCategory cat = new PkgCategory(
                    PkgCategory.KEY_TOOLS,
                    "Tools",
imgFactory.getImageByName(ICON_CAT_OTHER));
            unusedItemsMap.put(PkgCategory.KEY_TOOLS, Pair.of(cat, new HashSet<PkgItem>()));
mCategories.add(cat);
}

        if (!unusedItemsMap.containsKey(PkgCategory.KEY_EXTRA)) {
            PkgCategory cat = new PkgCategory(
                    PkgCategory.KEY_EXTRA,
                    "Extras",
imgFactory.getImageByName(ICON_CAT_OTHER));
            unusedItemsMap.put(PkgCategory.KEY_EXTRA, Pair.of(cat, new HashSet<PkgItem>()));
mCategories.add(cat);
}

//Synthetic comment -- @@ -597,13 +612,13 @@
if (apiKey < 1) {
Package p = item.getPackage();
if (p instanceof ToolPackage || p instanceof PlatformToolPackage) {
                    apiKey = PkgCategory.KEY_TOOLS;
} else {
                    apiKey = PkgCategory.KEY_EXTRA;
}
}

            Pair<PkgCategory, HashSet<PkgItem>> mapEntry = unusedItemsMap.get(apiKey);

if (mapEntry == null) {
// This is a new category. Create it and add it to the map.
//Synthetic comment -- @@ -613,28 +628,25 @@
// If we don't (e.g. when installing a new platform that isn't yet available
// locally in the SDK Manager), it's OK we'll try to find the first platform
// package available.
                String label = null;
if (apiKey != -1) {
for (IAndroidTarget target : mUpdaterData.getSdkManager().getTargets()) {
if (target.isPlatform() && target.getVersion().getApiLevel() == apiKey) {
                            label = target.getVersionName();
                            if (label != null) {
                                label = String.format("Android %1$s (API %2$d)", label, apiKey);
                                break;
                            }
}
}
}

                PkgCategory cat = new PkgCategory(
apiKey,
                        label,
imgFactory.getImageByName(ICON_CAT_PLATFORM));
mapEntry = Pair.of(cat, new HashSet<PkgItem>());
unusedItemsMap.put(apiKey, mapEntry);
mCategories.add(0, cat);
}
            PkgCategory cat = mapEntry.getFirst();
assert cat != null;
unusedCatSet.remove(cat);

//Synthetic comment -- @@ -644,14 +656,13 @@
cat.getItems().add(item);
}

            if (apiKey != -1 && cat.getLabel() == null) {
// Check whether we can get the actual platform version name (e.g. "1.5")
// from the first Platform package we find in this category.
Package p = item.getPackage();
if (p instanceof PlatformPackage) {
                    String vn = ((PlatformPackage) p).getVersionName();
                    String name = String.format("Android %1$s (API %2$d)", vn, apiKey);
                    cat.setLabel(name);
}
}
}
//Synthetic comment -- @@ -667,7 +678,10 @@

// Remove any unused items in the category.
int apikey = cat.getKey();
            Pair<PkgCategory, HashSet<PkgItem>> mapEntry = unusedItemsMap.get(apikey);
assert mapEntry != null;
HashSet<PkgItem> unusedItems = mapEntry.getSecond();
for (Iterator<PkgItem> iterItem = cat.getItems().iterator(); iterItem.hasNext(); ) {
//Synthetic comment -- @@ -679,13 +693,6 @@

// Sort the items
Collections.sort(cat.getItems());

            // Fix the category name for any API where we might not have found a platform package.
            if (cat.getLabel() == null) {
                int api = cat.getKey();
                String name = String.format("API %1$d", api);
                cat.setLabel(name);
            }
}

// Sort the categories list.
//Synthetic comment -- @@ -716,6 +723,7 @@
mTreeColumnName.setImage(getImage(ICON_SORT_BY_SOURCE));
}

mCategories.clear();

Set<SdkSource> sourceSet = new HashSet<SdkSource>();
//Synthetic comment -- @@ -1075,7 +1083,7 @@
if (element instanceof PkgCategory) {
return ((PkgCategory) element).getLabel();
} else if (element instanceof PkgItem) {
                    return ((PkgItem) element).getName();
} else if (element instanceof IDescription) {
return ((IDescription) element).getShortDescription();
}
//Synthetic comment -- @@ -1133,6 +1141,41 @@
return "";  //$NON-NLS-1$
}

@Override
public Image getImage(Object element) {
ImageFactory imgFactory = mUpdaterData.getImageFactory();
//Synthetic comment -- @@ -1263,14 +1306,9 @@
private final List<PkgItem> mItems = new ArrayList<PkgItem>();
private String mLabel;


// When sorting by Source, key is the hash of the source's name.
// When storing by API, key is the API level (>=1). Tools and extra have the
        // special values so they get naturally sorted the way we want them.
        // (Note: don't use max to avoid integers wrapping in comparisons. We can
        // revisit the day we get 2^30 platforms.)
        public final static int KEY_TOOLS = Integer.MAX_VALUE / 2;
        public final static int KEY_EXTRA = -1;

public PkgCategory(int key, String label, Object iconRef) {
mKey = key;
//Synthetic comment -- @@ -1299,6 +1337,72 @@
}
}

private class PackageManagerImpl extends PackageManager {

public PackageManagerImpl(UpdaterData updaterData) {







