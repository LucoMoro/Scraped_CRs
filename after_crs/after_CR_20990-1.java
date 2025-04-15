/*Guard against NPE

Change-Id:I081c057fd7c87239cdd5b4b6ddccf949a9cb2832*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 31bb948..e3e9e05 100644

//Synthetic comment -- @@ -191,9 +191,10 @@
Pair<RGB, RGB> themeColors = getColorsFromTheme();
RGB bg = themeColors.getFirst();
RGB fg = themeColors.getSecond();
                if (bg != null) {
                    storeBackground(imageDir, bg, fg);
                    overrideBgColor = Integer.valueOf(ImageUtils.rgbToInt(bg, 0xFF));
                }
}
}








