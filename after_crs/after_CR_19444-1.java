/*Fix threading issue with layout reload

The layout change listener (which can be called on any thread) must
not access SWT state unless it's on the SWT thread. There were some
existing SWT-redispatch blocks to handle this, but one code path was
missing. This changeset moves the entire method under a single SWT
redispatch block instead.

Change-Id:I7d802d78fbdd4811c68de830e1a54b1a97d76b5f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index ce21726..0ae9c3c 100644

//Synthetic comment -- @@ -1307,6 +1307,8 @@
return null;
}

        assert mConfigComposite.getDisplay().getThread() == Thread.currentThread();

// attempt to get a target from the configuration selector.
IAndroidTarget renderingTarget = mConfigComposite.getRenderingTarget();
if (renderingTarget != null) {
//Synthetic comment -- @@ -1578,16 +1580,28 @@
}

private class ReloadListener implements ILayoutReloadListener {
        /**
* Called when the file changes triggered a redraw of the layout
*/
        public void reloadLayout(final ChangeFlags flags, final boolean libraryChanged) {
            Display display = mConfigComposite.getDisplay();
            display.asyncExec(new Runnable() {
                public void run() {
                    reloadLayoutSwt(flags, libraryChanged);
                }
            });
        }

        /** Reload layout. <b>Must be called on the SWT thread</b> */
        private void reloadLayoutSwt(ChangeFlags flags, boolean libraryChanged) {
            assert mConfigComposite.getDisplay().getThread() == Thread.currentThread();

            boolean recompute = false;
if (flags.rClass) {
recompute = true;
if (mEditedFile != null) {
                    ResourceManager manager = ResourceManager.getInstance();
                    ProjectResources projectRes = manager.getProjectResources(
mEditedFile.getProject());

if (projectRes != null) {
//Synthetic comment -- @@ -1602,11 +1616,7 @@
// However there's no recompute, as it could not be needed
// (for instance a new layout)
// If a resource that's not a layout changed this will trigger a recompute anyway.
                mConfigComposite.updateLocales();
}

// if a resources was modified.
//Synthetic comment -- @@ -1614,7 +1624,8 @@
if (flags.resources || (libraryChanged && flags.layout)) {
recompute = true;

                // TODO: differentiate between single and multi resource file changed, and whether
                // the resource change affects the cache.

// force a reparse in case a value XML file changed.
mConfiguredProjectRes = null;
//Synthetic comment -- @@ -1637,15 +1648,11 @@
}

if (recompute) {
                if (mLayoutEditor.isGraphicalEditorActive()) {
                    recomputeLayout();
                } else {
                    mNeedsRecompute = true;
                }
}
}
}







