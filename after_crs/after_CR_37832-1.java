/*Sorting extension of LauncherActivity

Make it possible to define your own sort algoritm of entries
instead of a hardcoded alphabetic sort. Also make it possible
to control which entries that is added.

Change-Id:Ie11089bb06cd1529b29793df30d3ab2c4efd8d95*/




//Synthetic comment -- diff --git a/core/java/android/app/LauncherActivity.java b/core/java/android/app/LauncherActivity.java
//Synthetic comment -- index 8eb9ba4..8bb35d3 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;


/**
//Synthetic comment -- @@ -437,7 +438,7 @@
* implementation queries for activities.
*/
protected List<ResolveInfo> onQueryPackageManager(Intent queryIntent) {
        return mPackageManager.queryIntentActivities(queryIntent, getQueryFlags());
}

/**
//Synthetic comment -- @@ -446,13 +447,15 @@
public List<ListItem> makeListItems() {
// Load all matching activities and sort correctly
List<ResolveInfo> list = onQueryPackageManager(mIntent);
        Collections.sort(list, getComparator());

ArrayList<ListItem> result = new ArrayList<ListItem>(list.size());
int listSize = list.size();
for (int i = 0; i < listSize; i++) {
ResolveInfo resolveInfo = list.get(i);
            if (checkActivityForAdding(resolveInfo)) {
                result.add(new ListItem(mPackageManager, resolveInfo, null));
            }
}

return result;
//Synthetic comment -- @@ -466,4 +469,26 @@
protected boolean onEvaluateShowIcons() {
return true;
}

    /**
     * Get the comparator object to use for list item comparisons
     */
    protected Comparator<ResolveInfo> getComparator() {
        return new ResolveInfo.DisplayNameComparator(mPackageManager);
    }

    /**
     * Get the query flags use when calling
     * {@link PackageManager#queryIntentActivities(Intent, int)}.
     */
    protected int getQueryFlags() {
        return 0; /* no flags */
    }

    /**
     * Preforms a check if an entry should be added to the list.
     */
    protected boolean checkActivityForAdding(ResolveInfo ri) {
        return true;
    }
}







