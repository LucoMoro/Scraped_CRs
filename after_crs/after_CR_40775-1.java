/*Fix build.

Broken byIc5992773207c92426a9f37409536d74b81dd645bChange-Id:I10d12a356dd50674378f16cb543089348c163def*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java
//Synthetic comment -- index 00f835a..426dbae 100644

//Synthetic comment -- @@ -1142,7 +1142,7 @@
actionBarOptions.theme = mValues.holoDark
? ActionBarIconGenerator.Theme.HOLO_DARK
: ActionBarIconGenerator.Theme.HOLO_LIGHT;
                actionBarOptions.sourceIsClipart = (mValues.sourceType == SourceType.CLIPART);

options = actionBarOptions;
break;







