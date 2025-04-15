/*Adjust hint feedback location

When you select items in the relative layout, the layout attachments
are displayed below the canvas.

This changeset tweaks the positioning of these hints: they are
displayed to the right of the window (instead of below it) if the
canvas is taller than it is wide.

Change-Id:I7e12ed2f1749d4d3e529bab1a765b6a3279ca084*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index dc42b1b..8a5e198 100755

//Synthetic comment -- @@ -235,7 +235,7 @@
// mOutlineOverlay and mEmptyOverlay are initialized lazily
mHoverOverlay = new HoverOverlay(mHScale, mVScale);
mHoverOverlay.create(display);
        mSelectionOverlay = new SelectionOverlay();
mSelectionOverlay.create(display);
mImageOverlay = new ImageOverlay(this, mHScale, mVScale);
mIncludeOverlay = new IncludeOverlay(this);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java
//Synthetic comment -- index eb0601f..90aeebf 100644

//Synthetic comment -- @@ -29,10 +29,15 @@
* The {@link SelectionOverlay} paints the current selection as an overlay.
*/
public class SelectionOverlay extends Overlay {
/**
* Constructs a new {@link SelectionOverlay} tied to the given canvas.
*/
    public SelectionOverlay() {
}

/**
//Synthetic comment -- @@ -92,8 +97,23 @@
List<String> infos = rulesEngine.callGetSelectionHint(parentNode, node);
if (infos != null && infos.size() > 0) {
gcWrapper.useStyle(DrawingStyle.HELP);
                    int x = b.x + 10;
                    int y = b.y + b.h + 10;
gcWrapper.drawBoxedStrings(x, y, infos);
}
}







