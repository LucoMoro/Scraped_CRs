/*Make drag and drop create a single undo operation

If you drag and drop a widget, you end up with two separate undo
operations: One which inserts the widget in its new location, and one
which deletes it from its former location. If you drag a widget
somewhere and then change your mind and press Undo, you will therefore
end up with *two* versions of the dragged widgets, since undoing just
once only undoes the source removal, not the target addition.

This changeset fixes this. It's not a very clean fix since drag and
drop is a protocol where the drag source and the drop handler are
deliberately not connected. We now create a single undo context before
calling the layout helper's drop handler, and when it has finished we
then remove the source, still within the current undo context. This is
done by having the drag source register a cleanup handler with the
global drag info object, and this handler is invoked from within the
drop target's undo operation.

This means that we end up creating the undo unit on the tool side
rather than in the layout helpers, where more specific undo labels
were assigned. To deal with this, there is a new function which
produces a suitable Undo name. It creates the name by considering
whether it's a move or copy, as well as the unqualified names of the
dragged element and the drop target. For example, when dragging from
the palette you may end up with "Drop Button in LinearLayout", and if
dragging a multiselection you may end up with "Move Widgets in
RelativeLayout", and so on.

Change-Id:I242b51e5a9c7cd9eae55aa4139c510cb26b0e8fb*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java
//Synthetic comment -- index d6d624c..bcc65c1 100755

//Synthetic comment -- @@ -27,6 +27,7 @@
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TransferData;

import java.util.Arrays;

/**
//Synthetic comment -- @@ -239,7 +240,7 @@
* The data is being dropped.
* {@inheritDoc}
*/
    public void drop(DropTargetEvent event) {
if (DEBUG) AdtPlugin.printErrorToConsole("DEBUG", "dropped");

SimpleElement[] elements = null;
//Synthetic comment -- @@ -272,14 +273,23 @@
return;
}

        Point where = mCanvas.displayToCanvasPoint(event.x, event.y);

updateDropFeedback(mFeedback, event);
        mCanvas.getRulesEngine().callOnDropped(mTargetNode,
                elements,
                mFeedback,
                where);

clearDropInfo();
mCanvas.redraw();
// Request focus: This is *necessary* when you are dragging from one canvas editor
//Synthetic comment -- @@ -289,6 +299,49 @@
}

/**
* Updates the {@link DropFeedback#isCopy} and {@link DropFeedback#sameCanvas} fields
* of the given {@link DropFeedback}. This is generally called right before invoking
* one of the callOnXyz methods of GRE to refresh the fields.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java
//Synthetic comment -- index a9c9735..9e16ac3 100755

//Synthetic comment -- @@ -41,6 +41,7 @@
private SimpleElement[] mCurrentElements = null;
private CanvasSelection[] mCurrentSelection;
private Object mSourceCanvas = null;

/** Private constructor. Use {@link #getInstance()} to retrieve the singleton. */
private GlobalCanvasDragInfo() {
//Synthetic comment -- @@ -52,11 +53,23 @@
return sInstance;
}

    /** Registers the XML elements being dragged. */
    public void startDrag(SimpleElement[] elements, CanvasSelection[] selection, Object sourceCanvas) {
mCurrentElements = elements;
mCurrentSelection = selection;
mSourceCanvas = sourceCanvas;
}

/** Unregisters elements being dragged. */
//Synthetic comment -- @@ -87,4 +100,15 @@
public Object getSourceCanvas() {
return mSourceCanvas;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 88064ce..dad280d 100755

//Synthetic comment -- @@ -491,6 +491,13 @@
}

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
*
//Synthetic comment -- @@ -1560,7 +1567,14 @@
GlobalCanvasDragInfo.getInstance().startDrag(
mDragElements,
mDragSelection.toArray(new CanvasSelection[mDragSelection.size()]),
                        LayoutCanvas.this);
}
}

//Synthetic comment -- @@ -1592,20 +1606,6 @@
* On a successful move, remove the originating elements.
*/
public void dragFinished(DragSourceEvent e) {
            if (e.detail == DND.DROP_MOVE) {
                // Remove from source. Since we know the selection, we'll simply
                // create a cut operation on the existing drag selection.

                // Create an undo edit XML wrapper, which takes a runnable
                mLayoutEditor.wrapUndoEditXmlModel(
                        "Remove drag'n'drop source elements",
                        new Runnable() {
                            public void run() {
                                deleteSelection("Remove", mDragSelection);
                            }
                        });
            }

// Clear the selection
mDragSelection.clear();
mDragElements = null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index 0e16105..a1f0b10 100755

//Synthetic comment -- @@ -443,7 +443,8 @@
GlobalCanvasDragInfo.getInstance().startDrag(
mElements,
null /* selection */,
                    null /*canvas*/);
}

public void dragSetData(DragSourceEvent e) {







