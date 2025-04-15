/*Only allow palette previews when layoutlib 5 is used

Disable palette previews when the target SDK does not have layoutlib 5
(or more accurately, the custom background support required by the
palette, which will be backported to earlier SDKs.)

Change-Id:Ib572fd070eae995541a680eaccd58411d35066b2*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index fb20d7b..69f2867 100755

//Synthetic comment -- @@ -322,6 +322,14 @@
return mCurrentDevice;
}

    /** Returns true if previews in the palette should be made available */
    private boolean previewsAvailable() {
        // Not layoutlib 5 -- we require transparency/custom background support to do
        // a decent job with previews
        LayoutLibrary layoutLibrary = mEditor.getLayoutLibrary();
        return layoutLibrary != null && layoutLibrary.supports(Capability.TRANSPARENCY);
    }

/**
* Loads or reloads the palette elements by using the layout and view descriptors from the
* given target data.
//Synthetic comment -- @@ -369,6 +377,13 @@
assert mPaletteMode != null;
}

        // Ensure that the palette mode is supported on this version of the layout library
        if (!previewsAvailable()) {
            if (mPaletteMode.isPreview()) {
                mPaletteMode = PaletteMode.ICON_TEXT;
            }
        }

if (mPaletteMode.isPreview()) {
if (mForeground != null) {
mForeground.dispose();
//Synthetic comment -- @@ -995,8 +1010,12 @@
return true;
}
};
                boolean previews = previewsAvailable();
for (PaletteMode mode : PaletteMode.values()) {
                    if (mode.isPreview() && !previews) {
                        continue;
                    }
                    manager.add(new PaletteModeAction(mode));
}
if (mPaletteMode.isPreview()) {
manager.add(new Separator());







