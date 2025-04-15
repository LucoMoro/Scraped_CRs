/*All resource changes should trigger a layout rendering.

The current code didn't render if a layout changed
but <include> support was added (way back!) and this could
trigger the need for a recompile.

Change-Id:Icf34f0c03ad0b4108103e3dc12916fba988ea0b3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java
//Synthetic comment -- index 380381b..a19a4db 100644

//Synthetic comment -- @@ -62,7 +62,6 @@
public boolean code = false;
/** any non-layout resource changes */
public boolean resources = false;
        public boolean layout = false;
public boolean rClass = false;
public boolean localeList = false;
public boolean manifest = false;
//Synthetic comment -- @@ -363,11 +362,7 @@
mProjectFlags.put(project, changeFlags);
}

                if (resTypes[0] != ResourceType.LAYOUT) {
                    changeFlags.resources = true;
                } else {
                    changeFlags.layout = true;
                }
}
}
};








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 5506319..cf7e0b2 100644

//Synthetic comment -- @@ -1630,7 +1630,8 @@
assert mConfigComposite.getDisplay().getThread() == Thread.currentThread();

boolean recompute = false;
            if (flags.rClass) {
recompute = true;
if (mEditedFile != null) {
ResourceManager manager = ResourceManager.getInstance();
//Synthetic comment -- @@ -1653,8 +1654,7 @@
}

// if a resources was modified.
            // also, if a layout in a library was modified.
            if (flags.resources || (libraryChanged && flags.layout)) {
recompute = true;

// TODO: differentiate between single and multi resource file changed, and whether







