/*AvdManager1: remove unused code.

Change-Id:Id2f2b65d25403d57d68cb2257bc19723790ad82b*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AvdManagerWindowImpl1.java
//Synthetic comment -- index 719de4c..b3727fc 100755

//Synthetic comment -- @@ -34,7 +34,6 @@
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
//Synthetic comment -- @@ -251,16 +250,6 @@
}
}


// -- Start of internal part ----------
// Hide everything down-below from SWT designer








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 04bf422..f25927a 100755

//Synthetic comment -- @@ -573,7 +573,7 @@
if (!unusedItemsMap.containsKey(PkgCategory.KEY_EXTRA)) {
PkgCategory cat = new PkgCategory(
PkgCategory.KEY_EXTRA,
                    "Extras",
imgFactory.getImageByName(ICON_CAT_OTHER));
unusedItemsMap.put(PkgCategory.KEY_EXTRA, Pair.of(cat, new HashSet<PkgItem>()));
mCategories.add(cat);







