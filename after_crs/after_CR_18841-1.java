/*Don't unregister listeners from disposed widget

This changeset fixes a couple of exceptions on shutdown which stem
from the fact that once a widget is disposed it's an error to attempt
to remove listeners from it.

Change-Id:Ie4b1049668805c6e9bbedd1f7f0cf480e2d68c9d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index 2bba581..97a21ee 100644

//Synthetic comment -- @@ -207,6 +207,14 @@
*            listening to.
*/
public void unregisterListeners(DragSource dragSource, DropTarget dropTarget) {
        if (mCanvas.isDisposed()) {
            // If the LayoutCanvas is already disposed, we shouldn't try to unregister
            // the listeners; they are already not active and an attempt to remove the
            // listener will throw a widget-is-disposed exception.
            mListener = null;
            return;
        }

if (mListener != null) {
mCanvas.removeMouseMoveListener(mListener);
mCanvas.removeMouseListener(mListener);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java
//Synthetic comment -- index f1b4247..a1438d9 100755

//Synthetic comment -- @@ -119,12 +119,12 @@
}

public void dispose() {
        if (mSelectionListener != null) {
            mCanvas.getSelectionManager().removeSelectionChangedListener(mSelectionListener);
        }
if (mCanvas != null) {
mCanvas.dispose();
mCanvas = null;
}
}
}







