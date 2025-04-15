/*Fix keybindings in manifest editor (issue 36540)

Unbind the text editor keybindings when any page other
than the text editor itself is shown.

Change-Id:I1681de20113b3c0627a23872100632f8d28bb698*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index ea3f30b..2df069f 100644

//Synthetic comment -- @@ -362,11 +362,11 @@
}

/**
     * Creates undo redo (etc) actions for the editor site (so that it works for any page of this
* multi-page editor) by re-using the actions defined by the {@link StructuredTextEditor}
* (aka the XML text editor.)
*/
    protected void updateActionBindings() {
IActionBars bars = getEditorSite().getActionBars();
if (bars != null) {
IAction action = mTextEditor.getAction(ActionFactory.UNDO.getId());
//Synthetic comment -- @@ -395,6 +395,35 @@
}

/**
     * Clears the action bindings for the editor site.
     */
    protected void clearActionBindings(boolean includeUndoRedo) {
        IActionBars bars = getEditorSite().getActionBars();
        if (bars != null) {
            // For some reason, undo/redo doesn't seem to work in the form editor.
            // This appears to be the case for pure Eclipse form editors too, e.g. see
            //      https://bugs.eclipse.org/bugs/show_bug.cgi?id=68423
            // However, as a workaround we can use the *text* editor's underlying undo
            // to revert operations being done in the UI, and the form automatically updates.
            // Therefore, to work around this, we simply leave the text editor bindings
            // in place if {@code includeUndoRedo} is not set
            if (includeUndoRedo) {
                bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), null);
                bars.setGlobalActionHandler(ActionFactory.REDO.getId(), null);
            }
            bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
            bars.setGlobalActionHandler(ActionFactory.CUT.getId(), null);
            bars.setGlobalActionHandler(ActionFactory.COPY.getId(), null);
            bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), null);
            bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), null);
            bars.setGlobalActionHandler(ActionFactory.FIND.getId(), null);
            bars.setGlobalActionHandler(IDEActionFactory.BOOKMARK.getId(), null);

            bars.updateActionBars();
        }
    }

    /**
* Selects the default active page.
* @param defaultPageId the id of the page to show. If <code>null</code> the editor attempts to
* find the default page in the properties of the {@link IResource} object being edited.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java
//Synthetic comment -- index 87fc307..3e815f5 100644

//Synthetic comment -- @@ -99,6 +99,28 @@
GlobalProjectMonitor.getMonitor().removeFileListener(mMarkerMonitor);
}

    @Override
    public void activated() {
        super.activated();
        clearActionBindings(false);
    }

    @Override
    public void deactivated() {
        super.deactivated();
        updateActionBindings();
    }

    @Override
    protected void pageChange(int newPageIndex) {
        super.pageChange(newPageIndex);
        if (newPageIndex == mTextPageIndex) {
            updateActionBindings();
        } else {
            clearActionBindings(false);
        }
    }

/**
* Return the root node of the UI element hierarchy, which here
* is the "manifest" node.







