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
            int offsetX = x - be.x - be.w / 2;
            int offsetY = y - be.y - be.h / 2;
gc.useStyle(DrawingStyle.DROP_PREVIEW);
for (IDragElement element : elements) {
drawElement(gc, element, offsetX, offsetY);
//Synthetic comment -- @@ -150,8 +150,8 @@
// Copy all the attributes, modifying them as needed.
addAttributes(newChild, element, idMap, DEFAULT_ATTR_FILTER);

                    int x = p.x - b.x - (be.isValid() ? be.w / 2 : 0);
                    int y = p.y - b.y - (be.isValid() ? be.h / 2 : 0);

if (first) {
first = false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java
//Synthetic comment -- index ab39563..7aee9bc 100755

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;


/**
* This singleton is used to keep track of drag'n'drops initiated within this
//Synthetic comment -- @@ -45,8 +44,6 @@
private Object mSourceCanvas = null;
private Runnable mRemoveSourceHandler;

/** Private constructor. Use {@link #getInstance()} to retrieve the singleton. */
private GlobalCanvasDragInfo() {
// pass
//Synthetic comment -- @@ -117,26 +114,4 @@
mRemoveSourceHandler = null;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index 907359f..c11d00b 100644

//Synthetic comment -- @@ -472,24 +472,13 @@
}

/**
     * Returns the mouse location of the drop target event.
*
* @param event the drop target event
* @return a {@link ControlPoint} location corresponding to the top left corner
*/
private ControlPoint getDropLocation(DropTargetEvent event) {
        return ControlPoint.create(mCanvas, event);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index ec40d53..32bf15b 100755

//Synthetic comment -- @@ -51,6 +51,7 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
//Synthetic comment -- @@ -606,31 +607,22 @@
// Shift the drag feedback image up such that it's centered under the
// mouse pointer

                // Eclipse 3.4 does not support drag image offsets
                // TODO: Replace by direct field access when we drop Eclipse 3.4 support.
try {
                    Field xField = event.getClass().getDeclaredField("offsetX"); //$NON-NLS-1$
                    Field yField = event.getClass().getDeclaredField("offsetY"); //$NON-NLS-1$

                    Rectangle imageBounds = mImage.getBounds();
                    int offsetX = imageBounds.width / 2;
                    int offsetY = imageBounds.height / 2;
xField.set(event, Integer.valueOf(offsetX));
yField.set(event, Integer.valueOf(offsetY));
} catch (SecurityException e) {
} catch (NoSuchFieldException e) {
} catch (IllegalArgumentException e) {
} catch (IllegalAccessException e) {
}
}
}








