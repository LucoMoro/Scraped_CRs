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
//Synthetic comment -- index 5efdda7..83c2304 100755

//Synthetic comment -- @@ -246,7 +246,7 @@
* The data is being dropped.
* {@inheritDoc}
*/
    public void drop(final DropTargetEvent event) {
if (DEBUG) AdtPlugin.printErrorToConsole("DEBUG", "dropped");

SimpleElement[] elements = null;
//Synthetic comment -- @@ -279,7 +279,7 @@
return;
}

        final Point where = mCanvas.displayToCanvasPoint(event.x, event.y);

// Record children of the target right before the drop (such that we can
// find out after the drop which exact children were inserted)
//Synthetic comment -- @@ -289,10 +289,21 @@
}

updateDropFeedback(mFeedback, event);

        final SimpleElement[] elementsFinal = elements;
        String label = computeUndoLabel(mTargetNode, elements, event.detail);
        mCanvas.getLayoutEditor().wrapUndoEditXmlModel(label, new Runnable() {
            public void run() {
                mCanvas.getRulesEngine().callOnDropped(mTargetNode,
                        elementsFinal,
                        mFeedback,
                        where);
                // Clean up drag if applicable
                if (event.detail == DND.DROP_MOVE) {
                    GlobalCanvasDragInfo.getInstance().removeSource();
                }
            }
        });

// Now find out which nodes were added, and look up their corresponding
// CanvasViewInfos
//Synthetic comment -- @@ -345,6 +356,51 @@
}

/**
     * Computes a suitable Undo label to use for a drop operation, such as
     * "Drop Button in LinearLayout" and "Move Widgets in RelativeLayout"
     *
     * @param targetNode The target of the drop
     * @param elements The dragged widgets
     * @param detail The DnD mode, as used in {@link DropTargetEvent#detail}.
     * @return A string suitable as an undo-label for the drop event
     */
    private String computeUndoLabel(NodeProxy targetNode, SimpleElement[] elements, int detail) {
        // Decide whether it's a move or a copy; we'll label moves specifically
        // as a move and consider everything else a "Drop"
        String verb = (detail == DND.DROP_MOVE) ? "Move" : "Drop";

        // Get the type of widget being dropped/moved, IF there is only one. If
        // there is more than one, just reference it as "Widgets".
        String object;
        if (elements != null && elements.length == 1) {
            object = getSimpleName(elements[0].getFqcn());
        } else {
            object = "Widgets";
        }

        String where = getSimpleName(targetNode.getFqcn());

        // When we localize this: $1 is the verb (Move or Drop), $2 is the
        // object (such as "Button"), and $3 is the place we are doing it (such
        // as "LinearLayout").
        return String.format("%1$s %2$s in %3$s", verb, object, where);
    }

    /**
     * Returns simple name (basename, following last dot) of a fully qualified
     * class name.
     *
     * @param fcqn The fqcn to reduce
     * @return The base name of the fqcn
     */
    private String getSimpleName(String fqcn) {
        // Note that the following works even when there is no dot, since
        // lastIndexOf will return -1 so we get fcqn.substring(-1+1) =
        // fcqn.substring(0) = fcqn
        return fqcn.substring(fqcn.lastIndexOf('.') + 1);
    }

    /**
* Updates the {@link DropFeedback#isCopy} and {@link DropFeedback#sameCanvas} fields
* of the given {@link DropFeedback}. This is generally called right before invoking
* one of the callOnXyz methods of GRE to refresh the fields.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java
//Synthetic comment -- index a9c9735..d49ff40 100755

//Synthetic comment -- @@ -41,6 +41,7 @@
private SimpleElement[] mCurrentElements = null;
private CanvasSelection[] mCurrentSelection;
private Object mSourceCanvas = null;
    private Runnable mRemoveSourceHandler;

/** Private constructor. Use {@link #getInstance()} to retrieve the singleton. */
private GlobalCanvasDragInfo() {
//Synthetic comment -- @@ -52,17 +53,31 @@
return sInstance;
}

    /**
     * Registers the XML elements being dragged.
     *
     * @param elements The elements being dragged
     * @param selection The selection (which can be null, for example when the
     *            user drags from the palette)
     * @param sourceCanvas An object representing the source we are dragging
     *            from (used for identity comparisons only)
     * @param removeSourceHandler A runnable (or null) which can clean up the
     *            source. It should only be invoked if the drag operation is a
     *            move, not a copy.
     */
    public void startDrag(SimpleElement[] elements, CanvasSelection[] selection,
            Object sourceCanvas, Runnable removeSourceHandler) {
mCurrentElements = elements;
mCurrentSelection = selection;
mSourceCanvas = sourceCanvas;
        mRemoveSourceHandler = removeSourceHandler;
}

/** Unregisters elements being dragged. */
public void stopDrag() {
mCurrentElements = null;
mSourceCanvas = null;
        mRemoveSourceHandler = null;
}

/** Returns the elements being dragged. */
//Synthetic comment -- @@ -87,4 +102,15 @@
public Object getSourceCanvas() {
return mSourceCanvas;
}

    /**
     * Removes source of the drag. This should only be called when the drag and
     * drop operation is a move (not a copy).
     */
    public void removeSource() {
        if (mRemoveSourceHandler != null) {
            mRemoveSourceHandler.run();
            mRemoveSourceHandler = null;
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index bb24005..e945ba2 100755

//Synthetic comment -- @@ -492,6 +492,13 @@
}

/**
     * Returns the {@link LayoutEditor} associated with this canvas.
     */
    /* package */ LayoutEditor getLayoutEditor() {
        return mLayoutEditor;
    }

    /**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
*
//Synthetic comment -- @@ -1613,7 +1620,13 @@
GlobalCanvasDragInfo.getInstance().startDrag(
mDragElements,
mDragSelection.toArray(new CanvasSelection[mDragSelection.size()]),
                        LayoutCanvas.this,
                        new Runnable() {
                            public void run() {
                                deleteSelection("Remove", mDragSelection);
                            }
                        }
                );
}
}

//Synthetic comment -- @@ -1645,20 +1658,6 @@
* On a successful move, remove the originating elements.
*/
public void dragFinished(DragSourceEvent e) {
// Clear the selection
mDragSelection.clear();
mDragElements = null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index 0e16105..a1f0b10 100755

//Synthetic comment -- @@ -443,7 +443,8 @@
GlobalCanvasDragInfo.getInstance().startDrag(
mElements,
null /* selection */,
                    null /*canvas*/,
                    null /* removeSource */);
}

public void dragSetData(DragSourceEvent e) {







