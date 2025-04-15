/*Minor layoutlib API fix: rename a Capability

TRANSPARENT -> CUSTOM_BACKGROUND_COLOR

Change-Id:Iaa521231983de4d1008a7bb86b923f295d295a65*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 69f2867..f8b3109 100755

//Synthetic comment -- @@ -324,10 +324,10 @@

/** Returns true if previews in the palette should be made available */
private boolean previewsAvailable() {
        // Not layoutlib 5 -- we require transparency/custom background support to do
// a decent job with previews
LayoutLibrary layoutLibrary = mEditor.getLayoutLibrary();
        return layoutLibrary != null && layoutLibrary.supports(Capability.TRANSPARENCY);
}

/**
//Synthetic comment -- @@ -805,7 +805,8 @@
Integer overrideBgColor = null;
boolean hasTransparency = false;
LayoutLibrary layoutLibrary = editor.getLayoutLibrary();
            if (layoutLibrary != null && layoutLibrary.supports(Capability.TRANSPARENCY)) {
// It doesn't matter what the background color is as long as the alpha
// is 0 (fully transparent). We're using red to make it more obvious if
// for some reason the background is painted when it shouldn't be.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index e3e9e05..1b48c7c 100644

//Synthetic comment -- @@ -187,7 +187,7 @@
LayoutLibrary layoutLibrary = editor.getLayoutLibrary();
Integer overrideBgColor = null;
if (layoutLibrary != null) {
            if (layoutLibrary.supports(Capability.TRANSPARENCY)) {
Pair<RGB, RGB> themeColors = getColorsFromTheme();
RGB bg = themeColors.getFirst();
RGB fg = themeColors.getSecond();








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java
//Synthetic comment -- index fc390db..ff6777b 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
UNBOUND_RENDERING,
/** Ability to override the background of the rendering with transparency using
* {@link SceneParams#setCustomBackgroundColor(int)} */
    TRANSPARENCY,
/** Ability to call {@link LayoutScene#render()} and {@link LayoutScene#render(long)}. */
RENDER,
/**







