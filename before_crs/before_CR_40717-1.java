/*Fix build.

Broken byIc5992773207c92426a9f37409536d74b81dd645bChange-Id:Ifd19f71c1ebfa1a3aaf8f812eccbadaee27c8f90*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java
//Synthetic comment -- index 00f835a..426dbae 100644

//Synthetic comment -- @@ -1142,7 +1142,7 @@
actionBarOptions.theme = mValues.holoDark
? ActionBarIconGenerator.Theme.HOLO_DARK
: ActionBarIconGenerator.Theme.HOLO_LIGHT;
                actionBarOptions.sourceIsClipart = (mValues.sourceType == CLIPART);

options = actionBarOptions;
break;







