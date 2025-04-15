/*Fix drag image mouse coordinate handling

Instead of processing drag events using the top left corner of the
drag image, use the mouse cursor coordinate, and fix the absolute
layout rule to compute the top left corner instead.

Change-Id:I4d5096bfd04ac0abb2148196ac7e6d9d52797712*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java
//Synthetic comment -- index 18ea968..708b67b 100644

//Synthetic comment -- @@ -84,8 +84,8 @@
// At least the first element has a bound. Draw rectangles
// for all dropped elements with valid bounds, offset at
// the drop point.
            int offsetX = x - be.x;
            int offsetY = y - be.y;
gc.useStyle(DrawingStyle.DROP_PREVIEW);
for (IDragElement element : elements) {
drawElement(gc, element, offsetX, offsetY);
//Synthetic comment -- @@ -150,8 +150,8 @@
// Copy all the attributes, modifying them as needed.
addAttributes(newChild, element, idMap, DEFAULT_ATTR_FILTER);

                    int x = p.x - b.x;
                    int y = p.y - b.y;

if (first) {
first = false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java
//Synthetic comment -- index ab39563..7aee9bc 100755

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.swt.dnd.DragSourceEffect;

/**
* This singleton is used to keep track of drag'n'drops initiated within this
//Synthetic comment -- @@ -45,8 +44,6 @@
private Object mSourceCanvas = null;
private Runnable mRemoveSourceHandler;

    private ControlPoint mImageOffset;

/** Private constructor. Use {@link #getInstance()} to retrieve the singleton. */
private GlobalCanvasDragInfo() {
// pass
//Synthetic comment -- @@ -117,26 +114,4 @@
mRemoveSourceHandler = null;
}
}

    /**
     * Returns an image offset set on this drag info. The image offset is the distance
     * between the top left corner of the dragged image, and the mouse position. It is
     * typically the negative distance of the offsets set on a {@link DragSourceEffect}
     * image in effect during the drag and drop.
     *
     * @return The image offset, or null if none apply
     */
    public ControlPoint getImageOffset() {
        return mImageOffset;
    }

    /**
     * Sets the image offset for this drag. See the {@link #getImageOffset()}
     * documentation for details.
     *
     * @param imageOffset a new offset to apply
     */
    public void setImageOffset(ControlPoint imageOffset) {
        this.mImageOffset = imageOffset;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index 907359f..c11d00b 100644

//Synthetic comment -- @@ -472,24 +472,13 @@
}

/**
     * Returns the top left corner location of the drop target event.
*
* @param event the drop target event
* @return a {@link ControlPoint} location corresponding to the top left corner
*/
private ControlPoint getDropLocation(DropTargetEvent event) {
        ControlPoint p = ControlPoint.create(mCanvas, event);

        // Is the image offset from the mouse pointer?
        GlobalCanvasDragInfo dragInfo = GlobalCanvasDragInfo.getInstance();
        if (dragInfo != null) {
            ControlPoint imageOffset = dragInfo.getImageOffset();
            if (imageOffset != null) {
                p = ControlPoint.create(mCanvas, p.x + imageOffset.x, p.y + imageOffset.y);
            }
        }

        return p;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index ec40d53..32bf15b 100755

//Synthetic comment -- @@ -51,6 +51,7 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
//Synthetic comment -- @@ -606,31 +607,22 @@
// Shift the drag feedback image up such that it's centered under the
// mouse pointer

                // TODO quick'ndirty fix. swt.dnd in 3.4 doesn't have offsetX/Y.
                ControlPoint offset = ControlPoint.create(null, 0, 0);
try {
                    Field xField = event.getClass().getDeclaredField("offsetX");
                    Field yField = event.getClass().getDeclaredField("offsetY");

                    int offsetX = mImage.getBounds().width / 2;
                    int offsetY = mImage.getBounds().height / 2;
xField.set(event, Integer.valueOf(offsetX));
yField.set(event, Integer.valueOf(offsetY));

                    // ...and record this info in the drag state object such that we can
                    // account for it when performing the drop, since we want to place the newly
                    // inserted object where it is currently shown, not with its top left corner
                    // in the center where the mouse cursor was (this mostly matters for
                    // the AbsoluteLayout).
                    offset = ControlPoint.create(null, -offsetX, -offsetY);

} catch (SecurityException e) {
} catch (NoSuchFieldException e) {
} catch (IllegalArgumentException e) {
} catch (IllegalAccessException e) {
}

                GlobalCanvasDragInfo.getInstance().setImageOffset(offset);
}
}








