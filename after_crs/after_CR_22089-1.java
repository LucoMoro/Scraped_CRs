/*Improve painting of combined selection and hover

When the mouse is over a rectangle, we highlight this "hover"
rectangle by painting a semitranslucent white rectangle on top of it.

When a view is selected, we highlight it by painting a semitranslucent
blue rectangle over it.

This means that if you move the mouse over the selection, you get both
of these effects added together, which dilutes the underlying selected
widget too much.

This changeset tries to improve this situation by defining a different
visual style to be used for the combination of hover and selection,
where the opacity is much lower for the hover in this case.

This changeset also reduces the existing hover by about 30% opacity.

Change-Id:I63ffe8a9d756dcae29b2009a1a1cd6b9ffb6fbe7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/DrawingStyle.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/DrawingStyle.java
//Synthetic comment -- index 0938afa..7317ebc 100644

//Synthetic comment -- @@ -44,6 +44,12 @@
HOVER,

/**
     * The style used for hovered views (e.g. when the mouse is directly on top
     * of the view), when the hover happens to be the same object as the selection
     */
    HOVER_SELECTION,

    /**
* The style used to draw anchors (lines to the other views the given view
* is anchored to)
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HoverOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HoverOverlay.java
//Synthetic comment -- index 27a6024..2f46921 100644

//Synthetic comment -- @@ -16,22 +16,35 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.SwtDrawingStyle.HOVER;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.SwtDrawingStyle.HOVER_SELECTION;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import java.util.List;

/**
* The {@link HoverOverlay} paints an optional hover on top of the layout,
* highlighting the currently hovered view.
*/
public class HoverOverlay extends Overlay {
    private final LayoutCanvas mCanvas;

/** Hover border color. Must be disposed, it's NOT a system color. */
private Color mHoverStrokeColor;

/** Hover fill color. Must be disposed, it's NOT a system color. */
private Color mHoverFillColor;

    /** Hover border select color. Must be disposed, it's NOT a system color. */
    private Color mHoverSelectStrokeColor;

    /** Hover fill select color. Must be disposed, it's NOT a system color. */
    private Color mHoverSelectFillColor;

/** Vertical scaling & scrollbar information. */
private CanvasTransform mVScale;

//Synthetic comment -- @@ -48,13 +61,14 @@
/**
* Constructs a new {@link HoverOverlay} linked to the given view hierarchy.
*
     * @param canvas the associated canvas
* @param hScale The {@link CanvasTransform} to use to transfer horizontal layout
*            coordinates to screen coordinates.
* @param vScale The {@link CanvasTransform} to use to transfer vertical layout
*            coordinates to screen coordinates.
*/
    public HoverOverlay(LayoutCanvas canvas, CanvasTransform hScale, CanvasTransform vScale) {
        mCanvas = canvas;
this.mHScale = hScale;
this.mVScale = vScale;
}
//Synthetic comment -- @@ -67,6 +81,15 @@
if (SwtDrawingStyle.HOVER.getFillColor() != null) {
mHoverFillColor = new Color(device, SwtDrawingStyle.HOVER.getFillColor());
}

        if (SwtDrawingStyle.HOVER_SELECTION.getStrokeColor() != null) {
            mHoverSelectStrokeColor = new Color(device,
                    SwtDrawingStyle.HOVER_SELECTION.getStrokeColor());
        }
        if (SwtDrawingStyle.HOVER_SELECTION.getFillColor() != null) {
            mHoverSelectFillColor = new Color(device,
                    SwtDrawingStyle.HOVER_SELECTION.getFillColor());
        }
}

@Override
//Synthetic comment -- @@ -80,6 +103,16 @@
mHoverFillColor.dispose();
mHoverFillColor = null;
}

        if (mHoverSelectStrokeColor != null) {
            mHoverSelectStrokeColor.dispose();
            mHoverSelectStrokeColor = null;
        }

        if (mHoverSelectFillColor != null) {
            mHoverSelectFillColor.dispose();
            mHoverSelectFillColor = null;
        }
}

/**
//Synthetic comment -- @@ -117,19 +150,35 @@
int w = mHScale.scale(mHoverRect.width);
int h = mVScale.scale(mHoverRect.height);


            boolean hoverIsSelected = false;
            List<SelectionItem> selections = mCanvas.getSelectionManager().getSelections();
            for (SelectionItem item : selections) {
                if (mHoverRect.equals(item.getViewInfo().getSelectionRect())) {
                    hoverIsSelected = true;
                    break;
                }
            }

            Color stroke = hoverIsSelected ? mHoverSelectStrokeColor : mHoverStrokeColor;
            Color fill = hoverIsSelected ? mHoverSelectFillColor : mHoverFillColor;

            if (stroke != null) {
int oldAlpha = gc.getAlpha();
                gc.setForeground(stroke);
                gc.setLineStyle(hoverIsSelected ?
                        HOVER_SELECTION.getLineStyle() : HOVER.getLineStyle());
                gc.setAlpha(hoverIsSelected ?
                        HOVER_SELECTION.getStrokeAlpha() : HOVER.getStrokeAlpha());
gc.drawRectangle(x, y, w, h);
gc.setAlpha(oldAlpha);
}

            if (fill != null) {
int oldAlpha = gc.getAlpha();
                gc.setAlpha(hoverIsSelected ?
                        HOVER_SELECTION.getFillAlpha() : HOVER.getFillAlpha());
                gc.setBackground(fill);
gc.fillRectangle(x, y, w, h);
gc.setAlpha(oldAlpha);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index b54e3d1..719fdbf 100755

//Synthetic comment -- @@ -261,7 +261,7 @@

// --- Set up graphic overlays
// mOutlineOverlay and mEmptyOverlay are initialized lazily
        mHoverOverlay = new HoverOverlay(this, mHScale, mVScale);
mHoverOverlay.create(display);
mSelectionOverlay = new SelectionOverlay(this);
mSelectionOverlay.create(display);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtDrawingStyle.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtDrawingStyle.java
//Synthetic comment -- index 2748297..268fce6 100644

//Synthetic comment -- @@ -44,7 +44,12 @@
/**
* The style definition corresponding to {@link DrawingStyle#HOVER}
*/
    HOVER(null, 0, new RGB(0xFF, 0xFF, 0xFF), 40, 1, SWT.LINE_DOT),

    /**
     * The style definition corresponding to {@link DrawingStyle#HOVER}
     */
    HOVER_SELECTION(null, 0, new RGB(0xFF, 0xFF, 0xFF), 10, 1, SWT.LINE_DOT),

/**
* The style definition corresponding to {@link DrawingStyle#ANCHOR}
//Synthetic comment -- @@ -199,6 +204,8 @@
return GUIDELINE;
case HOVER:
return HOVER;
            case HOVER_SELECTION:
                return HOVER_SELECTION;
case ANCHOR:
return ANCHOR;
case OUTLINE:







