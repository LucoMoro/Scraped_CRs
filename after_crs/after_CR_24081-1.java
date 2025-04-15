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

    /**
     * For extra packages, we want to add vendor|path to the sorting key
     * <em>before<em/> the revision number.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    protected String comparisonKey() {
        String s = super.comparisonKey();
        int pos = s.indexOf("|r:");         //$NON-NLS-1$
        assert pos > 0;
        s = s.substring(0, pos) +
            "|ve:" + getVendor() +          //$NON-NLS-1$
            "|pa:" + getPath() +            //$NON-NLS-1$
            s.substring(pos);
        return s;
    }

// ---

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/Package.java
//Synthetic comment -- index baa6706..faf5a00 100755

//Synthetic comment -- @@ -557,46 +557,83 @@
* <p/>
* This {@link #compareTo(Package)} method is purely an implementation detail to
* perform the right ordering of the packages in the list of available or installed packages.
     * <p/>
     * <em>Important</em>: Derived classes should consider overriding {@link #comparisonKey()}
     * instead of this method.
*/
public int compareTo(Package other) {
        String s1 = this.comparisonKey();
        String s2 = other.comparisonKey();

        return s1.compareTo(s2);
}

/**
     * Computes a comparison key for each package used by {@link #compareTo(Package)}.
     * The key is a string.
     * The base package class return a string that encodes the package type,
     * the revision number and the platform version, if applicable, in the form:
     * <pre>
     *      t:N|v:NNNN.P|r:NNNN|
     * </pre>
     * All fields must start by a "letter colon" prefix and end with a vertical pipe (|, ASCII 124).
     * <p/>
     * The string format <em>may</em> change between releases and clients should not
     * store them outside of the session or expect them to be consistent between
     * different releases. They are purely an internal implementation details of the
     * {@link #compareTo(Package)} method.
     * <p/>
     * Derived classes should get the string from the super class and then append
     * or <em>insert</em> their own |-separated content.
     * For example an extra vendor name & path can be inserted before the revision
     * number, since it has more sorting weight.
*/
    protected String comparisonKey() {

        StringBuilder sb = new StringBuilder();

        sb.append("t:");                                                        //$NON-NLS-1$
if (this instanceof ToolPackage) {
            sb.append(0);
} else if (this instanceof PlatformToolPackage) {
            sb.append(1);
} else if (this instanceof DocPackage) {
            sb.append(2);
} else if (this instanceof PlatformPackage) {
            sb.append(3);
} else if (this instanceof SamplePackage) {
            sb.append(4);
} else if (this instanceof AddonPackage) {
            sb.append(5);
} else {
// extras and everything else
            sb.append(9);
}
        sb.append("|v:");                                                       //$NON-NLS-1$


        // We insert the package version here because it is more important
        // than the revision number. We want package version to be sorted
        // top-down, so we'll use 10k-api as the sorting key. The day we
        // get reach 10k APIs, we'll need to revisit this.

if (this instanceof IPackageVersion) {
AndroidVersion v = ((IPackageVersion) this).getVersion();

            sb.append(String.format("%1$04d.%2$d",                              //$NON-NLS-1$
                    10000 - v.getApiLevel(),
                    v.isPreview() ? 1 : 0
                    ));
        }
        sb.append("|r:");                                                       //$NON-NLS-1$


        // Append revision number

        sb.append(String.format("%1$04d", getRevision()));                      //$NON-NLS-1$
        sb.append('|');

        return sb.toString();
}

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 570f769..2b7a0db 100755

//Synthetic comment -- @@ -147,6 +147,7 @@
private TreeViewerColumn mColumnStatus;
private Font mTreeFontItalic;
private TreeColumn mTreeColumnName;
    private boolean mLastSortWasByApi;

public PackagesPage(Composite parent, int swtStyle, UpdaterData updaterData) {
super(parent, swtStyle);
//Synthetic comment -- @@ -532,7 +533,7 @@
}

private void sortPackages(boolean updateButtons) {
        if (isSortByApi()) {
sortByApiLevel();
} else {
sortBySource();
//Synthetic comment -- @@ -543,6 +544,10 @@
}
}

    private boolean isSortByApi() {
        return mCheckSortApi != null && !mCheckSortApi.isDisposed() && mCheckSortApi.getSelection();
    }

/**
* Recompute the tree by sorting all the packages by API.
* This does an update in-place of the mCategories list so that the table
//Synthetic comment -- @@ -556,34 +561,44 @@
mTreeColumnName.setImage(getImage(ICON_SORT_BY_API));
}

        // If the sorting mode changed, clear the categories.
        if (!mLastSortWasByApi) {
            mLastSortWasByApi = true;
            mCategories.clear();
        }

// keep a map of the initial state so that we can detect which items or categories are
// no longer being used, so that we can removed them at the end of the in-place update.
        final Map<Integer, Pair<PkgApiCategory, HashSet<PkgItem>> > unusedItemsMap =
            new HashMap<Integer, Pair<PkgApiCategory, HashSet<PkgItem>> >();
        final Set<PkgApiCategory> unusedCatSet = new HashSet<PkgApiCategory>();

// get existing categories
for (PkgCategory cat : mCategories) {
            if (cat instanceof PkgApiCategory) {
                PkgApiCategory acat = (PkgApiCategory) cat;
                unusedCatSet.add(acat);
                unusedItemsMap.put(acat.getKey(),
                        Pair.of(acat, new HashSet<PkgItem>(acat.getItems())));
            }
}

// always add the tools & extras categories, even if empty (unlikely anyway)
        if (!unusedItemsMap.containsKey(PkgApiCategory.KEY_TOOLS)) {
            PkgApiCategory cat = new PkgApiCategory(
                    PkgApiCategory.KEY_TOOLS,
                    null,
imgFactory.getImageByName(ICON_CAT_OTHER));
            unusedItemsMap.put(PkgApiCategory.KEY_TOOLS, Pair.of(cat, new HashSet<PkgItem>()));
mCategories.add(cat);
}

        if (!unusedItemsMap.containsKey(PkgApiCategory.KEY_EXTRA)) {
            PkgApiCategory cat = new PkgApiCategory(
                    PkgApiCategory.KEY_EXTRA,
                    null,
imgFactory.getImageByName(ICON_CAT_OTHER));
            unusedItemsMap.put(PkgApiCategory.KEY_EXTRA, Pair.of(cat, new HashSet<PkgItem>()));
mCategories.add(cat);
}

//Synthetic comment -- @@ -597,13 +612,13 @@
if (apiKey < 1) {
Package p = item.getPackage();
if (p instanceof ToolPackage || p instanceof PlatformToolPackage) {
                    apiKey = PkgApiCategory.KEY_TOOLS;
} else {
                    apiKey = PkgApiCategory.KEY_EXTRA;
}
}

            Pair<PkgApiCategory, HashSet<PkgItem>> mapEntry = unusedItemsMap.get(apiKey);

if (mapEntry == null) {
// This is a new category. Create it and add it to the map.
//Synthetic comment -- @@ -613,28 +628,25 @@
// If we don't (e.g. when installing a new platform that isn't yet available
// locally in the SDK Manager), it's OK we'll try to find the first platform
// package available.
                String platformName = null;
if (apiKey != -1) {
for (IAndroidTarget target : mUpdaterData.getSdkManager().getTargets()) {
if (target.isPlatform() && target.getVersion().getApiLevel() == apiKey) {
                            platformName = target.getVersionName();
                            break;
}
}
}

                PkgApiCategory cat = new PkgApiCategory(
apiKey,
                        platformName,
imgFactory.getImageByName(ICON_CAT_PLATFORM));
mapEntry = Pair.of(cat, new HashSet<PkgItem>());
unusedItemsMap.put(apiKey, mapEntry);
mCategories.add(0, cat);
}
            PkgApiCategory cat = mapEntry.getFirst();
assert cat != null;
unusedCatSet.remove(cat);

//Synthetic comment -- @@ -644,14 +656,13 @@
cat.getItems().add(item);
}

            if (apiKey != -1 && cat.getPlatformName() == null) {
// Check whether we can get the actual platform version name (e.g. "1.5")
// from the first Platform package we find in this category.
Package p = item.getPackage();
if (p instanceof PlatformPackage) {
                    String platformName = ((PlatformPackage) p).getVersionName();
                    cat.setPlatformName(platformName);
}
}
}
//Synthetic comment -- @@ -667,7 +678,10 @@

// Remove any unused items in the category.
int apikey = cat.getKey();
            Pair<PkgApiCategory, HashSet<PkgItem>> mapEntry = unusedItemsMap.get(apikey);
            if (mapEntry == null) { //DEBUG
                apikey = (apikey + 1) - 1;
            }
assert mapEntry != null;
HashSet<PkgItem> unusedItems = mapEntry.getSecond();
for (Iterator<PkgItem> iterItem = cat.getItems().iterator(); iterItem.hasNext(); ) {
//Synthetic comment -- @@ -679,13 +693,6 @@

// Sort the items
Collections.sort(cat.getItems());
}

// Sort the categories list.
//Synthetic comment -- @@ -716,6 +723,7 @@
mTreeColumnName.setImage(getImage(ICON_SORT_BY_SOURCE));
}

        mLastSortWasByApi = false;
mCategories.clear();

Set<SdkSource> sourceSet = new HashSet<SdkSource>();
//Synthetic comment -- @@ -1075,7 +1083,7 @@
if (element instanceof PkgCategory) {
return ((PkgCategory) element).getLabel();
} else if (element instanceof PkgItem) {
                    return getPkgItemname((PkgItem) element);
} else if (element instanceof IDescription) {
return ((IDescription) element).getShortDescription();
}
//Synthetic comment -- @@ -1133,6 +1141,41 @@
return "";  //$NON-NLS-1$
}

        private String getPkgItemname(PkgItem item) {
            String name = item.getName().trim();

            if (isSortByApi()) {
                // When sorting by API, the package name might contains the API number
                // or the platform name at the end. If we find it, cut it out since it's
                // redundant.

                PkgApiCategory cat = (PkgApiCategory) findCategoryForItem(item);
                String apiLabel = cat.getApiLabel();
                String platLabel = cat.getPlatformName();

                if (platLabel != null && name.endsWith(platLabel)) {
                    return name.substring(0, name.length() - platLabel.length());

                } else if (apiLabel != null && name.endsWith(apiLabel)) {
                    return name.substring(0, name.length() - apiLabel.length());
                }
            }

            return name;
        }

        private PkgCategory findCategoryForItem(PkgItem item) {
            for (PkgCategory cat : mCategories) {
                for (PkgItem i : cat.getItems()) {
                    if (i == item) {
                        return cat;
                    }
                }
            }

            return null;
        }

@Override
public Image getImage(Object element) {
ImageFactory imgFactory = mUpdaterData.getImageFactory();
//Synthetic comment -- @@ -1263,14 +1306,9 @@
private final List<PkgItem> mItems = new ArrayList<PkgItem>();
private String mLabel;

// When sorting by Source, key is the hash of the source's name.
// When storing by API, key is the API level (>=1). Tools and extra have the
        // special values.

public PkgCategory(int key, String label, Object iconRef) {
mKey = key;
//Synthetic comment -- @@ -1299,6 +1337,72 @@
}
}

    private static class PkgApiCategory extends PkgCategory {

        /** Platform name, in the form "Android 1.2". Can be null if we don't have the name. */
        private String mPlatformName;

        // When sorting by Source, key is the hash of the source's name.
        // When storing by API, key is the API level (>=1). Tools and extra have the
        // special values so they get naturally sorted the way we want them.
        // (Note: don't use max to avoid integers wrapping in comparisons. We can
        // revisit the day we get 2^30 platforms.)
        public final static int KEY_TOOLS = Integer.MAX_VALUE / 2;
        public final static int KEY_EXTRA = -1;

        public PkgApiCategory(int apiKey, String platformName, Object iconRef) {
            super(apiKey, null /*label*/, iconRef);
            setPlatformName(platformName);
        }

        public String getPlatformName() {
            return mPlatformName;
        }

        public void setPlatformName(String platformName) {
            if (platformName != null) {
                // Normal case for actual platform categories
                mPlatformName = String.format("Android %1$s", platformName);
                super.setLabel(null);
            }
        }

        public String getApiLabel() {
            int api = getKey();
            if (api > 0) {
                return String.format("API %1$d", getKey());
            }
            return null;
        }

        @Override
        public String getLabel() {
            String label = super.getLabel();
            if (label == null) {
                int key = getKey();

                if (key == KEY_TOOLS) {
                    label = "Tools";
                } else if (key == KEY_EXTRA) {
                    label = "Extras";
                } else {
                    if (mPlatformName != null) {
                        label = String.format("%1$s (%2$s)", mPlatformName, getApiLabel());
                    } else {
                        label = getApiLabel();
                    }
                }
                super.setLabel(label);
            }
            return label;
        }

        @Override
        public void setLabel(String label) {
            throw new UnsupportedOperationException("Use setPlatformName() instead."); //$NON-NLS-1$
        }
    }

private class PackageManagerImpl extends PackageManager {

public PackageManagerImpl(UpdaterData updaterData) {







