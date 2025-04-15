/*Only allow drag images for canvas drags

The outline view "reuses" the drag source listener for the canvas,
doing some tricks with mouse events to make it look like the drag is
originating within the canvas. However, the image previews of drags do
not work well in this case. Therefore, stash data on the drag souce
such that the drag source handler can only do image previews when the
drag source is really the canvas instead of the outline page.

Change-Id:I919264f001fa232053a0b9831ebc32ee22beb877*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index 314719a..6cc65bb 100644

//Synthetic comment -- @@ -49,6 +49,9 @@
* gestures and in order to update the gestures along the way.
*/
public class GestureManager {
/** The canvas which owns this GestureManager. */
private final LayoutCanvas mCanvas;

//Synthetic comment -- @@ -468,6 +471,28 @@
}

/**
* Our canvas {@link DragSourceListener}. Handles drag being started and
* finished and generating the drag data.
*/
//Synthetic comment -- @@ -583,7 +608,12 @@

// Render drag-images: Copy portions of the full screen render.
Image image = mCanvas.getImageOverlay().getImage();
                if (image != null) {
/**
* Transparency of the dragged image ([0-255]). We're using 30%
* translucency to make the image faint and not obscure the drag








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 389b25f..4114327 100755

//Synthetic comment -- @@ -284,6 +284,7 @@

mDropTarget = createDropTarget(this);
mDragSource = createDragSource(this);
mGestureManager.registerListeners(mDragSource, mDropTarget);

if (mLayoutEditor == null) {







