/*Enable code specific to Eclipse 3.5

Change-Id:I29f6d1d74ce2979882988c18bc3e79d995445015*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index 8365a37..a284c0a 100644

//Synthetic comment -- @@ -613,7 +613,8 @@
// the dragged view
int deltaX = (int) (scale * (boundingBox.x - p.x));
int deltaY = (int) (scale * (boundingBox.y - p.y));
                        SwtUtils.setDragImageOffsets(e, -deltaX, -deltaY);

// View rules may need to know it as well
GlobalCanvasDragInfo dragInfo = GlobalCanvasDragInfo.getInstance();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java
//Synthetic comment -- index e70687e..ecf152f 100644

//Synthetic comment -- @@ -28,8 +28,6 @@
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
//Synthetic comment -- @@ -57,7 +55,7 @@

@Override
public boolean performDrop(Object data) {
        final DropTargetEvent event = getEvent();
if (event == null) {
return false;
}
//Synthetic comment -- @@ -166,7 +164,7 @@
@Override
public boolean validateDrop(Object target, int operation,
TransferData transferType) {
        DropTargetEvent event = getEvent();
if (event == null) {
return false;
}
//Synthetic comment -- @@ -219,23 +217,4 @@
}
return false;
}

    // Eclipse 3.4 workaround for lack of #getCurrentEvent()
    private DropTargetEvent getEvent() {
        // Eclipse 3.4 does not provide ViewerDropAdapter#getCurrentEvent
        // FIXME: Replace the below code with just "getCurrentEvent()" when
        // we drop Eclipse 3.4 support.
        try {
            Class<ViewerDropAdapter> clz = ViewerDropAdapter.class;
            Method m = clz.getDeclaredMethod("getCurrentEvent"); //$NON-NLS-1$
            return (DropTargetEvent) m.invoke(this, (Object[]) null);
        } catch (SecurityException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 9c7c472..5b00a70 100755

//Synthetic comment -- @@ -664,9 +664,9 @@
// mouse pointer

Rectangle imageBounds = mImage.getBounds();
                int offsetX = imageBounds.width / 2;
                int offsetY = imageBounds.height / 2;
                SwtUtils.setDragImageOffsets(event, offsetX, offsetY);
}

event.doit = true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java
//Synthetic comment -- index 29a6059..3d872c7 100644

//Synthetic comment -- @@ -16,9 +16,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.Rect;
import com.android.ide.common.layout.Pair;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
//Synthetic comment -- @@ -28,7 +26,6 @@
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.lang.reflect.Field;
import java.util.List;

/**
//Synthetic comment -- @@ -173,58 +170,6 @@
}

/**
     * Sets the DragSourceEvent's offsetX and offsetY fields.
     *
     * @param event the {@link DragSourceEvent}
     * @param offsetX the offset X value
     * @param offsetY the offset Y value
     */
    public static void setDragImageOffsets(DragSourceEvent event, int offsetX, int offsetY) {
        // Eclipse 3.4 does not support drag image offsets
        //     event.offsetX = offsetX;
        //     event.offsetY= offsetY;
        // FIXME: Replace by direct field access when we drop Eclipse 3.4 support.
        try {
            Class<DragSourceEvent> clz = DragSourceEvent.class;
            Field xField = clz.getDeclaredField("offsetX"); //$NON-NLS-1$
            Field yField = clz.getDeclaredField("offsetY"); //$NON-NLS-1$
            xField.set(event, Integer.valueOf(offsetX));
            yField.set(event, Integer.valueOf(offsetY));
        } catch (SecurityException e) {
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    /**
     * Returns the DragSourceEvent's offsetX and offsetY fields.
     *
     * @param event the {@link DragSourceEvent}
     * @return A pair of the offset X and Y values, or null if it fails (e.g. on Eclipse
     *         3.4)
     */
    public static Pair<Integer,Integer> getDragImageOffsets(DragSourceEvent event) {
        // Eclipse 3.4 does not support drag image offsets:
        //     return Pair.of(event.offsetX, event.offsetY);
        // FIXME: Replace by direct field access when we drop Eclipse 3.4 support.
        try {
            Class<DragSourceEvent> clz = DragSourceEvent.class;
            Field xField = clz.getDeclaredField("offsetX"); //$NON-NLS-1$
            Field yField = clz.getDeclaredField("offsetY"); //$NON-NLS-1$
            int offsetX = xField.getInt(event);
            int offsetY = yField.getInt(event);
            return Pair.of(offsetX, offsetY);
        } catch (SecurityException e) {
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

        return null;
    }

    /**
* Creates a new image from a source image where the contents from a given set of
* bounding boxes are copied into the new image and the rest is left transparent. A
* scale can be applied to make the resulting image larger or smaller than the source







