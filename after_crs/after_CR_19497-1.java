/*Various layout fixes

First, fix absolute layout such that it properly handles both screen
scaling as well as various screen resolution densities when it
computes the dip positions.

Second, pass the bounds surrounding the mouse position when a drag is
initiated to the view rules. This is used by both absolute layout and
relative layout to properly handle dragging bounds. In particular, in
relative layout this is used to match a border not only when the mouse
cursor gets near the edge, but when the bound edges also get near the
edge. In absolute layout this is used to show a correct bound
rectangle that has the proper offset from the original drag position
(which may not be the center, which until now it was assuming).

Third, in RelativeLayout, when there are no children, offer a left
alignment regardless of where you are within the rectangle. This is
similar to how LinearLayout works.

In addition, two internal changes:
* Factor the various RelativeLayout string constants into the
  LayoutConstants class. This had the nice side effect of revealing a
  typo where we were referencing a non-existing value!  Constants FTW!
* Make the RulesEngine log the exceptions, not just the error
  messages, thrown by IViewRule calls. That way the full stack trace
  is available in the Error Viewer, including line numbers etc.

Change-Id:I0b83df71b36741e65a1eb2003ed044157eb6f0cd*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/DropFeedback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/DropFeedback.java
//Synthetic comment -- index 9926571..232ab5b 100755

//Synthetic comment -- @@ -87,6 +87,17 @@
public boolean isCopy;

/**
     * The bounds of the drag, relative to the starting mouse position. For example, if
     * you have a rectangular view of size 100x80, and you start dragging at position
     * (15,20) from the top left corner of this rectangle, then the drag bounds would be
     * (-15,-20, 100x80).
     * <p>
     * NOTE: The coordinate units will be in layout/view coordinates. In other words, they
     * are unaffected by the canvas zoom.
     */
    public Rect dragBounds;

    /**
* Set to true when the drag'n'drop starts and ends in the same canvas of the
* same Eclipse instance.
* <p/>
//Synthetic comment -- @@ -95,6 +106,12 @@
public boolean sameCanvas;

/**
     * Density scale for pixels. To compute the dip (device independent pixel) in the
     * view from a layout coordinate, apply this scale.
     */
    public double dipScale = 1.0;

    /**
* Initializes the drop feedback with the given user data and paint
* callback. A paint is requested if the paint callback is non-null.
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java
//Synthetic comment -- index ffa43d9..559c3bf 100644

//Synthetic comment -- @@ -86,8 +86,8 @@
// At least the first element has a bound. Draw rectangles
// for all dropped elements with valid bounds, offset at
// the drop point.
            int offsetX = x - be.x + (feedback.dragBounds != null ? feedback.dragBounds.x : 0);
            int offsetY = y - be.y + (feedback.dragBounds != null ? feedback.dragBounds.y : 0);
gc.useStyle(DrawingStyle.DROP_PREVIEW);
for (IDragElement element : elements) {
drawElement(gc, element, offsetX, offsetY);
//Synthetic comment -- @@ -152,8 +152,11 @@
// Copy all the attributes, modifying them as needed.
addAttributes(newChild, element, idMap, DEFAULT_ATTR_FILTER);

                    int deltaX = (feedback.dragBounds != null ? feedback.dragBounds.x : 0);
                    int deltaY = (feedback.dragBounds != null ? feedback.dragBounds.y : 0);

                    int x = p.x - b.x + deltaX;
                    int y = p.y - b.y + deltaY;

if (first) {
first = false;
//Synthetic comment -- @@ -168,6 +171,12 @@
y += be.isValid() ? be.h : 10;
}

                    double scale = feedback.dipScale;
                    if (scale != 1.0) {
                        x *= scale;
                        y *= scale;
                    }

newChild.setAttribute(ANDROID_URI, "layout_x", //$NON-NLS-1$
x + "dip"); //$NON-NLS-1$
newChild.setAttribute(ANDROID_URI, "layout_y", //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index 1bc511b..5b90c7f 100644

//Synthetic comment -- @@ -68,6 +68,25 @@
public static final String VALUE_TRUE = "true";                             //$NON-NLS-1$
public static final String VALUE_N_DIP = "%ddip";                           //$NON-NLS-1$

    public static final String VALUE_CENTER_VERTICAL = "centerVertical";        //$NON-NLS-1$
    public static final String VALUE_CENTER_IN_PARENT = "centerInParent";       //$NON-NLS-1$
    public static final String VALUE_CENTER_HORIZONTAL = "centerHorizontal";    //$NON-NLS-1$
    public static final String VALUE_ALIGN_PARENT_RIGHT = "alignParentRight";    //$NON-NLS-1$
    public static final String VALUE_ALIGN_PARENT_LEFT = "alignParentLeft";      //$NON-NLS-1$
    public static final String VALUE_ALIGN_PARENT_BOTTOM = "alignParentBottom";  //$NON-NLS-1$
    public static final String VALUE_ALIGN_PARENT_TOP = "alignParentTop";        //$NON-NLS-1$
    public static final String VALUE_ALIGN_RIGHT = "alignRight";                 //$NON-NLS-1$
    public static final String VALUE_ALIGN_LEFT = "alignLeft";                   //$NON-NLS-1$
    public static final String VALUE_ALIGN_BOTTOM = "alignBottom";               //$NON-NLS-1$
    public static final String VALUE_ALIGN_TOP = "alignTop";                     //$NON-NLS-1$
    public static final String VALUE_ALIGN_BASELINE = "alignBaseline";           //$NON-NLS-1$
    public static final String VAUE_TO_RIGHT_OF = "toRightOf";                   //$NON-NLS-1$
    public static final String VALUE_TO_LEFT_OF = "toLeftOf";                    //$NON-NLS-1$
    public static final String VALUE_BELOW = "below";                            //$NON-NLS-1$
    public static final String VALUE_ABOVE = "above";                            //$NON-NLS-1$
    public static final String VALUE_ALIGN_WITH_PARENT_MISSING =
        "alignWithParentMissing"; //$NON-NLS-1$

/**
* Namespace for the Android resource XML, i.e.
* "http://schemas.android.com/apk/res/android"








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index bf708a3..15d18fc 100755

//Synthetic comment -- @@ -18,20 +18,37 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_PARENT_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_PARENT_LEFT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_PARENT_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_PARENT_TOP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_TOP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ALIGN_WITH_PARENT_MISSING;
import static com.android.ide.common.layout.LayoutConstants.VALUE_BELOW;
import static com.android.ide.common.layout.LayoutConstants.VALUE_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_CENTER_IN_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_CENTER_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.VAUE_TO_RIGHT_OF;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IFeedbackPainter;
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.api.INode.IAttribute;

import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -52,23 +69,23 @@
@Override
public List<String> getSelectionHint(INode parentNode, INode childNode) {
List<String> infos = new ArrayList<String>(18);
        addAttr(VALUE_ABOVE, childNode, infos);
        addAttr(VALUE_BELOW, childNode, infos);
        addAttr(VALUE_TO_LEFT_OF, childNode, infos);
        addAttr(VAUE_TO_RIGHT_OF, childNode, infos);
        addAttr(VALUE_ALIGN_BASELINE, childNode, infos);
        addAttr(VALUE_ALIGN_TOP, childNode, infos);
        addAttr(VALUE_ALIGN_BOTTOM, childNode, infos);
        addAttr(VALUE_ALIGN_LEFT, childNode, infos);
        addAttr(VALUE_ALIGN_RIGHT, childNode, infos);
        addAttr(VALUE_ALIGN_PARENT_TOP, childNode, infos);
        addAttr(VALUE_ALIGN_PARENT_BOTTOM, childNode, infos);
        addAttr(VALUE_ALIGN_PARENT_LEFT, childNode, infos);
        addAttr(VALUE_ALIGN_PARENT_RIGHT, childNode, infos);
        addAttr(VALUE_ALIGN_WITH_PARENT_MISSING, childNode, infos);
        addAttr(VALUE_CENTER_HORIZONTAL, childNode, infos);
        addAttr(VALUE_CENTER_IN_PARENT, childNode, infos);
        addAttr(VALUE_CENTER_VERTICAL, childNode, infos);

return infos;
}
//Synthetic comment -- @@ -185,8 +202,7 @@
data.setIndex(-1);
data.setCurr(null);

                DropZone zone = computeBorderDropZone(targetNode, p, feedback);
if (zone == null) {
data.setZones(null);
} else {
//Synthetic comment -- @@ -207,6 +223,24 @@
break;
}
}

            // Look to see if there's a border match if we didn't find anything better;
            // a border match isn't required to have the mouse cursor within it since we
            // do edge matching in the code which -adds- the border zones.
            if (currZone == null && feedback.dragBounds != null) {
                for (DropZone zone : data.getZones()) {
                    if (zone.isBorderZone()) {
                        currZone = zone;
                        break;
                    }
                }
            }
        }

        // Look for border match when there are no children: always offer one in this case
        if (currZone == null && targetNode.getChildren().length == 0 && data.getZones() != null
                && data.getZones().size() > 0) {
            currZone = data.getZones().get(0);
}

if (currZone != data.getCurr()) {
//Synthetic comment -- @@ -286,7 +320,8 @@
return ids;
}

    private DropZone computeBorderDropZone(INode targetNode, Point p, DropFeedback feedback) {
        Rect bounds = targetNode.getBounds();
int x = p.x;
int y = p.y;

//Synthetic comment -- @@ -297,34 +332,50 @@
int x2 = x1 + w;
int y2 = y1 + h;

        // Default border zone size
int n = 10;
        int n2 = 2*n;

        // Size of -matched- border zone (not painted, but we detect edge overlaps here)
        int hn = 0;
        int vn = 0;
        if (feedback.dragBounds != null) {
            hn = feedback.dragBounds.w / 2;
            vn = feedback.dragBounds.h / 2;
        }
boolean vertical = false;

Rect r = null;
String attr = null;

        if (x <= x1 + n + hn && y >= y1 && y <= y2) {
r = new Rect(x1 - n, y1, n2, h);
            attr = VALUE_ALIGN_PARENT_LEFT;
vertical = true;

        } else if (x >= x2 - hn - n && y >= y1 && y <= y2) {
r = new Rect(x2 - n, y1, n2, h);
            attr = VALUE_ALIGN_PARENT_RIGHT;
vertical = true;

        } else if (y <= y1 + n + vn && x >= x1 && x <= x2) {
r = new Rect(x1, y1 - n, w, n2);
            attr = VALUE_ALIGN_PARENT_TOP;

        } else if (y >= y2 - vn - n && x >= x1 && x <= x2) {
r = new Rect(x1, y2 - n, w, n2);
            attr = VALUE_ALIGN_PARENT_BOTTOM;

} else {
            // We're nowhere near a border.
            // If there are no children, we will offer one anyway:
            if (targetNode.getChildren().length == 0) {
                r = new Rect(x1 - n, y1, n2, h);
                attr = VALUE_ALIGN_PARENT_LEFT;
                vertical = true;
            } else {
                return null;
            }
}

return new DropZone(r, Collections.singletonList(attr), r.getCenter(), vertical);
//Synthetic comment -- @@ -371,41 +422,41 @@
Rect bounds = new Rect(x1, y1, wt, ht);

List<DropZone> zones = new ArrayList<DropZone>(16);
        String a = VALUE_ABOVE;
int x = x1;
int y = y1;

        x = addx(w1, a, x, y, h1, zones, VALUE_TO_LEFT_OF);
        x = addx(w2, a, x, y, h1, zones, VALUE_ALIGN_LEFT);
        x = addx(w2, a, x, y, h1, zones, VALUE_ALIGN_LEFT, VALUE_ALIGN_RIGHT);
        x = addx(w2, a, x, y, h1, zones, VALUE_ALIGN_RIGHT);
        x = addx(w1, a, x, y, h1, zones, VAUE_TO_RIGHT_OF);

        a = VALUE_BELOW;
x = x1;
y = y1 + ht - h1;

        x = addx(w1, a, x, y, h1, zones, VALUE_TO_LEFT_OF);
        x = addx(w2, a, x, y, h1, zones, VALUE_ALIGN_LEFT);
        x = addx(w2, a, x, y, h1, zones, VALUE_ALIGN_LEFT, VALUE_ALIGN_RIGHT);
        x = addx(w2, a, x, y, h1, zones, VALUE_ALIGN_RIGHT);
        x = addx(w1, a, x, y, h1, zones, VAUE_TO_RIGHT_OF);

        a = VALUE_TO_LEFT_OF;
x = x1;
y = y1 + h1;

        y = addy(h2, a, x, y, w1, zones, VALUE_ALIGN_TOP);
        y = addy(h2, a, x, y, w1, zones, VALUE_ALIGN_TOP, VALUE_ALIGN_BOTTOM);
        y = addy(h2, a, x, y, w1, zones, VALUE_ALIGN_BOTTOM);

        a = VAUE_TO_RIGHT_OF;
x = x1 + wt - w1;
y = y1 + h1;

        y = addy(h2, a, x, y, w1, zones, VALUE_ALIGN_TOP);
        y = addy(h2, a, x, y, w1, zones, VALUE_ALIGN_TOP, VALUE_ALIGN_BOTTOM);
        y = addy(h2, a, x, y, w1, zones, VALUE_ALIGN_BOTTOM);

return Pair.of(bounds, zones);
}
//Synthetic comment -- @@ -511,20 +562,20 @@
int offsetX = x - be.x;
int offsetY = y - be.y;

                if (data.getCurr().getAttr().contains(VALUE_ALIGN_TOP)
                        && data.getCurr().getAttr().contains(VALUE_ALIGN_BOTTOM)) {
offsetY -= be.h / 2;
                } else if (data.getCurr().getAttr().contains(VALUE_ABOVE)
                        || data.getCurr().getAttr().contains(VALUE_ALIGN_TOP)
                        || data.getCurr().getAttr().contains(VALUE_ALIGN_PARENT_BOTTOM)) {
offsetY -= be.h;
}
                if (data.getCurr().getAttr().contains(VALUE_ALIGN_RIGHT)
                        && data.getCurr().getAttr().contains(VALUE_ALIGN_LEFT)) {
offsetX -= be.w / 2;
                } else if (data.getCurr().getAttr().contains(VALUE_TO_LEFT_OF)
                        || data.getCurr().getAttr().contains(VALUE_ALIGN_LEFT)
                        || data.getCurr().getAttr().contains(VALUE_ALIGN_PARENT_RIGHT)) {
offsetX -= be.w;
}

//Synthetic comment -- @@ -719,5 +770,9 @@
private boolean isVertical() {
return mVertical;
}

        private boolean isBorderZone() {
            return mMark != null;
        }
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index d702255..0875642 100755

//Synthetic comment -- @@ -24,7 +24,6 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -425,10 +424,11 @@
IMenuCallback c = ((MenuAction.Action) a2).getCallback();
if (c != null) {
try {
                                        c.action(a2, null /* no valueId for a toggle */,
                                                !isChecked);
} catch (Exception e) {
                                        AdtPlugin.log(e, "XML edit operation failed: %s",
                                                e.toString());
}
}
}
//Synthetic comment -- @@ -488,8 +488,8 @@
// Values do not apply for plain actions
c.action(a2, null /* valueId */, null /* newValue */);
} catch (Exception e) {
                                        AdtPlugin.log(e, "XML edit operation failed: %s",
                                                e.toString());
}
}
}
//Synthetic comment -- @@ -614,8 +614,8 @@
((MenuAction.Action) a2).getCallback().action(a2, key,
!isChecked);
} catch (Exception e) {
                                        AdtPlugin.log(e, "XML edit operation failed: %s",
                                                e.toString());
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index d3a7820..314719a 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.Rect;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
//Synthetic comment -- @@ -612,6 +614,16 @@
int deltaX = (int) (scale * (boundingBox.x - p.x));
int deltaY = (int) (scale * (boundingBox.y - p.y));
SwtUtils.setDragImageOffsets(e, -deltaX, -deltaY);

                        // View rules may need to know it as well
                        GlobalCanvasDragInfo dragInfo = GlobalCanvasDragInfo.getInstance();
                        Rect dragBounds = null;
                        if (dragInfo != null) {
                            int width = (int) (scale * boundingBox.width);
                            int height = (int) (scale * boundingBox.height);
                            dragBounds = new Rect(deltaX, deltaY, width, height);
                            dragInfo.setDragBounds(dragBounds);
                        }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java
//Synthetic comment -- index 3fd98a3..9315967 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.Rect;


/**
* This singleton is used to keep track of drag'n'drops initiated within this
//Synthetic comment -- @@ -43,6 +45,7 @@
private SelectionItem[] mCurrentSelection;
private Object mSourceCanvas = null;
private Runnable mRemoveSourceHandler;
    private Rect mDragBounds;

/** Private constructor. Use {@link #getInstance()} to retrieve the singleton. */
private GlobalCanvasDragInfo() {
//Synthetic comment -- @@ -114,4 +117,30 @@
mRemoveSourceHandler = null;
}
}

    /**
     * Get the bounds of the drag, relative to the starting mouse position. For example,
     * if you have a rectangular view of size 100x80, and you start dragging at position
     * (15,20) from the top left corner of this rectangle, then the drag bounds would be
     * (-15,-20, 100x80).
     * <p>
     * NOTE: The coordinate units will be in SWT/control pixels, not Android view pixels.
     * In other words, they are affected by the canvas zoom: If you zoom the view and the
     * bounds of a view grow, the drag bounds will be larger.
     *
     * @return the drag bounds, or null if there are no bounds for the current drag
     */
    public Rect getDragBounds() {
        return mDragBounds;
    }

    /**
     * Set the bounds of the drag, relative to the starting mouse position. See
     * {@link #getDragBounds()} for details on the semantics of the drag bounds.
     *
     * @param dragBounds the new drag bounds, or null if there are no drag bounds
     */
    public void setDragBounds(Rect dragBounds) {
        mDragBounds = dragBounds;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index ede9115..cce6c2a 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.sdklib.resources.Density.DEFAULT_DENSITY;

import com.android.ide.common.layoutlib.BasicLayoutScene;
import com.android.ide.common.layoutlib.LayoutLibrary;
import com.android.ide.common.sdk.LoadStatus;
//Synthetic comment -- @@ -1176,7 +1178,15 @@
return mConfigComposite.getScreenBounds();
}

    /**
     * Returns the scale to multiply pixels in the layout coordinate space with to obtain
     * the corresponding dip (device independent pixel)
     *
     * @return the scale to multiple layout coordinates with to obtain the dip position
     */
    public float getDipScale() {
        return DEFAULT_DENSITY / (float) mConfigComposite.getDensity().getDpiValue();
    }

// --- private methods ---









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index f047cbc..863b250 100644

//Synthetic comment -- @@ -446,6 +446,28 @@
}
df.sameCanvas = mCanvas == mGlobalDragInfo.getSourceCanvas();
df.invalidTarget = false;
        df.dipScale = mCanvas.getLayoutEditor().getGraphicalEditor().getDipScale();

        // Set the drag bounds, after converting it from control coordinates to
        // layout coordinates
        GlobalCanvasDragInfo dragInfo = GlobalCanvasDragInfo.getInstance();
        Rect dragBounds = null;
        if (dragInfo != null) {
            Rect controlDragBounds = dragInfo.getDragBounds();
            if (controlDragBounds != null) {
                CanvasTransform ht = mCanvas.getHorizontalTransform();
                CanvasTransform vt = mCanvas.getVerticalTransform();
                double horizScale = ht.getScale();
                double verticalScale = vt.getScale();
                int x = (int) (controlDragBounds.x / horizScale);
                int y = (int) (controlDragBounds.y / verticalScale);
                int w = (int) (controlDragBounds.w / horizScale);
                int h = (int) (controlDragBounds.h / verticalScale);

                dragBounds = new Rect(x, y, w, h);
            }
        }
        df.dragBounds = dragBounds;
}

/**
//Synthetic comment -- @@ -560,6 +582,7 @@
// guideline computations in onDropMove (since only onDropMove is handed
// the -position- of the mouse), and we want this computation to happen
// before we ask the view to draw its feedback.
                        updateDropFeedback(df, event);
df = mCanvas.getRulesEngine().callOnDropMove(targetNode,
mCurrentDragElements, df, new Point(p.x, p.y));
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index f5aaeed..e3bbd00 100755

//Synthetic comment -- @@ -510,12 +510,16 @@
Rect bounds = null;
DragSource dragSource = mItem.getDragSource();
DragSourceEffect dragSourceEffect = dragSource.getDragSourceEffect();
            Rect dragBounds = null;
if (dragSourceEffect instanceof PreviewDragSourceEffect) {
PreviewDragSourceEffect preview = (PreviewDragSourceEffect) dragSourceEffect;
Image previewImage = preview.getPreviewImage();
if (previewImage != null && !preview.isPlaceholder()) {
ImageData data = previewImage.getImageData();
                    int width = data.width;
                    int height = data.height;
                    bounds = new Rect(0, 0, width, height);
                    dragBounds = new Rect(-width / 2, -height / 2, width, height);
}
}

//Synthetic comment -- @@ -527,11 +531,13 @@
mElements = new SimpleElement[] { se };

// Register this as the current dragged data
            GlobalCanvasDragInfo dragInfo = GlobalCanvasDragInfo.getInstance();
            dragInfo.startDrag(
mElements,
null /* selection */,
null /* canvas */,
null /* removeSource */);
            dragInfo.setDragBounds(dragBounds);
}

public void dragSetData(DragSourceEvent e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index ff6cbfe..ae894e6 100755

//Synthetic comment -- @@ -192,7 +192,7 @@
return rule.getDisplayName();

} catch (Exception e) {
                AdtPlugin.log(e, "%s.getDisplayName() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -218,7 +218,7 @@
return rule.getContextMenu(selectedNode);

} catch (Exception e) {
                AdtPlugin.log(e, "%s.getContextMenu() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -244,7 +244,7 @@
return rule.getSelectionHint(parentNode, childNode);

} catch (Exception e) {
                AdtPlugin.log(e, "%getSelectionHint() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -269,7 +269,7 @@
return rule.onDropEnter(targetNode, elements);

} catch (Exception e) {
                AdtPlugin.log(e, "%s.onDropEnter() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -295,7 +295,7 @@
return rule.onDropMove(targetNode, elements, feedback, where);

} catch (Exception e) {
                AdtPlugin.log(e, "%s.onDropMove() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -318,7 +318,7 @@
rule.onDropLeave(targetNode, elements, feedback);

} catch (Exception e) {
                AdtPlugin.log(e, "%s.onDropLeave() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -342,7 +342,7 @@
rule.onDropped(targetNode, elements, feedback, where);

} catch (Exception e) {
                AdtPlugin.log(e, "%s.onDropped() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -359,7 +359,7 @@
try {
feedback.painter.paint(gc, targetNode, feedback);
} catch (Exception e) {
                AdtPlugin.log(e, "DropFeedback.painter failed: %s",
e.toString());
}
}
//Synthetic comment -- @@ -381,7 +381,7 @@
rule.onPaste(targetNode, pastedElements);

} catch (Exception e) {
                AdtPlugin.log(e, "%s.onPaste() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -490,7 +490,7 @@
try {
rule.onDispose();
} catch (Exception e) {
                    AdtPlugin.log(e, "%s.onDispose() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -649,10 +649,10 @@
// class.
} catch (InstantiationException e) {
// This is NOT an expected error: fail.
            AdtPlugin.log(e, "load rule error (%s): %s", realFqcn, e.toString());
} catch (IllegalAccessException e) {
// This is NOT an expected error: fail.
            AdtPlugin.log(e, "load rule error (%s): %s", realFqcn, e.toString());
}

// Memorize in the cache that we couldn't find a rule for this real FQCN
//Synthetic comment -- @@ -687,7 +687,7 @@
rule.onDispose();
}
} catch (Exception e) {
            AdtPlugin.log(e, "%s.onInit() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -696,18 +696,6 @@
}

/**
* Implementation of {@link IClientRulesEngine}. This provide {@link IViewRule} clients
* with a few methods they can use to use functionality from this {@link RulesEngine}.
*/
//Synthetic comment -- @@ -750,7 +738,7 @@
try {
return filter.validate(newText);
} catch (Exception e) {
                            AdtPlugin.log(e, "Custom validator failed: %s", e.toString());
return ""; //$NON-NLS-1$
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbsoluteLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbsoluteLayoutRuleTest.java
//Synthetic comment -- index a6d6384..f4092eb 100644

//Synthetic comment -- @@ -52,14 +52,16 @@
4,
// Not dragging one of the existing children
-1,


// Bounds rectangle
"useStyle(DROP_RECIPIENT), drawRect(Rect[0,0,240,480])",

// Drop preview
                "useStyle(DROP_PREVIEW), drawRect(Rect[30,-10,105,80])");

        assertEquals("30dip", inserted.getStringAttr(ANDROID_URI, "layout_x"));
        assertEquals("-10dip", inserted.getStringAttr(ANDROID_URI, "layout_y"));

// Without drag bounds we should just draw guide lines instead
inserted = dragInto(new Rect(0, 0, 0, 0), new Point(30, -10), 4, -1,







