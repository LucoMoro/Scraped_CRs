/*SdkMan2: sort Tool category at the top.

Change-Id:I8bc57a04a48cd540e6457a0b69b4911475989d8c*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 6c9cd89..df5dafa 100755

//Synthetic comment -- @@ -666,7 +666,7 @@
}

// Remove any unused items in the category.
            int apikey = cat.getKey();
Pair<PkgCategory, HashSet<PkgItem>> mapEntry = unusedItemsMap.get(apikey);
assert mapEntry != null;
HashSet<PkgItem> unusedItems = mapEntry.getSecond();
//Synthetic comment -- @@ -682,17 +682,18 @@

// Fix the category name for any API where we might not have found a platform package.
if (cat.getLabel() == null) {
                int api = cat.getKey();
String name = String.format("API %1$d", api);
cat.setLabel(name);
}
}

        // Sort the categories list.
Collections.sort(mCategories, new Comparator<PkgCategory>() {
public int compare(PkgCategory cat1, PkgCategory cat2) {
                // We always want categories in order tools..platforms..extras.
                // For platform, we compare in descending order (o2-o1).
                return cat2.getKey() - cat1.getKey();
}
});

//Synthetic comment -- @@ -764,7 +765,7 @@
}

/**
     * Decide whether to keep an item in the current tree based on user-chosen filter options.
*/
private boolean keepItem(PkgItem item) {
if (!mCheckFilterObsolete.getSelection()) {
//Synthetic comment -- @@ -790,7 +791,7 @@
}

/**
     * Performs the initial expansion of the tree. This expands categories that contain
* at least one installed item and collapses the ones with nothing installed.
*/
private void expandInitial(Object elem) {
//Synthetic comment -- @@ -1257,23 +1258,27 @@
}

private static class PkgCategory {
        private final int mKey;
private final Object mIconRef;
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
mLabel = label;
mIconRef = iconRef;
}

        public int getKey() {
return mKey;
}








