/*Prevent accidental dragging of the root layout

The layout editor deliberately does not reset the selection if you
start dragging within the bounding rectangle of one (of possibly
multiple) selected widgets. However, since it's pretty easy to have
the root element selected, this sometimes means you end up trying to
drag an item only to realize you can't because everything fits within
the root bounding rectangle.

This changeset fixes this usability issue by making the root widget a
special case. If you are dragging *only* the root, things work as
before. But if you are trying to drag anything else, it will ignore
the root, which means that you can drag one or more items
independently of the root whether or not it is included in the
selection.

Change-Id:Ia7497659e57dcc6431c3760a53847268a8b90815*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java
//Synthetic comment -- index 098cb8c..fe66d79 100755

//Synthetic comment -- @@ -68,6 +68,14 @@
}

/**
* Returns the selected view info. Cannot be null.
*/
public CanvasViewInfo getViewInfo() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 88064ce..0382f01 100755

//Synthetic comment -- @@ -1519,7 +1519,7 @@
boolean insideSelection = false;

for (CanvasSelection cs : mSelections) {
                    if (cs.getRect().contains(x, y)) {
insideSelection = true;
break;
}
//Synthetic comment -- @@ -1537,7 +1537,19 @@
// We should now have a proper selection that matches the cursor.
// Let's use this one. We make a copy of it since the "sanitize" pass
// below might remove some of the selected objects.
                    mDragSelection.addAll(mSelections);
}
}








