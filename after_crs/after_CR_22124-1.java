/*Fix layout action bar selection from outline

The layout action bar needs to update its selection when the selection
origin is the outline rather than the canvas.

Change-Id:Ibe66efc9f071934989dc4bde7bea0c8342c09ae8*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index c9d8024..bf95ee6 100644

//Synthetic comment -- @@ -185,6 +185,7 @@
if (!mSelections.isEmpty()) {
mSelections.clear();
mAltSelection = null;
                        updateActionsFromSelection();
redraw();
}
return;
//Synthetic comment -- @@ -238,7 +239,7 @@
}
if (changed) {
redraw();
                    updateActionsFromSelection();
}

}
//Synthetic comment -- @@ -703,21 +704,29 @@
}
});

            updateActionsFromSelection();
} finally {
mInsideUpdateSelection = false;
}
}

/**
     * Updates menu actions and the layout action bar after a selection change - these are
     * actions that depend on the selection
     */
    private void updateActionsFromSelection() {
        LayoutEditor editor = mCanvas.getLayoutEditor();
        if (editor != null) {
            // Update menu actions that depend on the selection
            updateMenuActions();

            // Update the layout actions bar
            LayoutActionBar layoutActionBar = editor.getGraphicalEditor().getLayoutActionBar();
            layoutActionBar.updateSelection();
        }
    }

    /**
* Sanitizes the selection for a copy/cut or drag operation.
* <p/>
* Sanitizes the list to make sure all elements have a valid XML attached to it,







