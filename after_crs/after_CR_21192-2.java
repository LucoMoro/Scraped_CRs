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
public boolean rClass = false;
public boolean localeList = false;
public boolean manifest = false;
//Synthetic comment -- @@ -363,11 +362,7 @@
mProjectFlags.put(project, changeFlags);
}

                changeFlags.resources = true;
}
}
};








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 39a9172..5e811bf 100644

//Synthetic comment -- @@ -1641,7 +1641,8 @@
assert mConfigComposite.getDisplay().getThread() == Thread.currentThread();

boolean recompute = false;
            // we only care about the r class of the main project.
            if (flags.rClass && libraryChanged == false) {
recompute = true;
if (mEditedFile != null) {
ResourceManager manager = ResourceManager.getInstance();
//Synthetic comment -- @@ -1664,8 +1665,7 @@
}

// if a resources was modified.
            if (flags.resources) {
recompute = true;

// TODO: differentiate between single and multi resource file changed, and whether







