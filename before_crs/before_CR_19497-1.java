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
* Set to true when the drag'n'drop starts and ends in the same canvas of the
* same Eclipse instance.
* <p/>
//Synthetic comment -- @@ -95,6 +106,12 @@
public boolean sameCanvas;

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
            int offsetX = x - be.x - be.w / 2;
            int offsetY = y - be.y - be.h / 2;
gc.useStyle(DrawingStyle.DROP_PREVIEW);
for (IDragElement element : elements) {
drawElement(gc, element, offsetX, offsetY);
//Synthetic comment -- @@ -152,8 +152,11 @@
// Copy all the attributes, modifying them as needed.
addAttributes(newChild, element, idMap, DEFAULT_ATTR_FILTER);

                    int x = p.x - b.x - (be.isValid() ? be.w / 2 : 0);
                    int y = p.y - b.y - (be.isValid() ? be.h / 2 : 0);

if (first) {
first = false;
//Synthetic comment -- @@ -168,6 +171,12 @@
y += be.isValid() ? be.h : 10;
}

newChild.setAttribute(ANDROID_URI, "layout_x", //$NON-NLS-1$
x + "dip"); //$NON-NLS-1$
newChild.setAttribute(ANDROID_URI, "layout_y", //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index 1bc511b..5b90c7f 100644

//Synthetic comment -- @@ -68,6 +68,25 @@
public static final String VALUE_TRUE = "true";                             //$NON-NLS-1$
public static final String VALUE_N_DIP = "%ddip";                           //$NON-NLS-1$

/**
* Namespace for the Android resource XML, i.e.
* "http://schemas.android.com/apk/res/android"








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index bf708a3..15d18fc 100755

//Synthetic comment -- @@ -18,20 +18,37 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IFeedbackPainter;
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INode.IAttribute;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -52,23 +69,23 @@
@Override
public List<String> getSelectionHint(INode parentNode, INode childNode) {
List<String> infos = new ArrayList<String>(18);
        addAttr("above", childNode, infos);                   //$NON-NLS-1$
        addAttr("below", childNode, infos);                   //$NON-NLS-1$
        addAttr("toLeftOf", childNode, infos);                //$NON-NLS-1$
        addAttr("toRightOf", childNode, infos);               //$NON-NLS-1$
        addAttr("alignBaseline", childNode, infos);           //$NON-NLS-1$
        addAttr("alignTop", childNode, infos);                //$NON-NLS-1$
        addAttr("alignBottom", childNode, infos);             //$NON-NLS-1$
        addAttr("alignLeft", childNode, infos);               //$NON-NLS-1$
        addAttr("alignRight", childNode, infos);              //$NON-NLS-1$
        addAttr("alignParentTop", childNode, infos);          //$NON-NLS-1$
        addAttr("alignParentBottom", childNode, infos);       //$NON-NLS-1$
        addAttr("alignParentLeft", childNode, infos);         //$NON-NLS-1$
        addAttr("alignParentRight", childNode, infos);        //$NON-NLS-1$
        addAttr("alignWithParentMissing", childNode, infos);  //$NON-NLS-1$
        addAttr("centerHorizontal", childNode, infos);        //$NON-NLS-1$
        addAttr("centerInParent", childNode, infos);          //$NON-NLS-1$
        addAttr("centerVertical", childNode, infos);          //$NON-NLS-1$

return infos;
}
//Synthetic comment -- @@ -185,8 +202,7 @@
data.setIndex(-1);
data.setCurr(null);

                DropZone zone = computeBorderDropZone(targetNode.getBounds(), p);

if (zone == null) {
data.setZones(null);
} else {
//Synthetic comment -- @@ -207,6 +223,24 @@
break;
}
}
}

if (currZone != data.getCurr()) {
//Synthetic comment -- @@ -286,7 +320,8 @@
return ids;
}

    private DropZone computeBorderDropZone(Rect bounds, Point p) {
int x = p.x;
int y = p.y;

//Synthetic comment -- @@ -297,34 +332,50 @@
int x2 = x1 + w;
int y2 = y1 + h;

int n = 10;
        int n2 = n * 2;
boolean vertical = false;

Rect r = null;
String attr = null;

        if (x <= x1 + n && y >= y1 && y <= y2) {
r = new Rect(x1 - n, y1, n2, h);
            attr = "alignParentLeft";              //$NON-NLS-1$
vertical = true;

        } else if (x >= x2 - n && y >= y1 && y <= y2) {
r = new Rect(x2 - n, y1, n2, h);
            attr = "alignParentRight";             //$NON-NLS-1$
vertical = true;

        } else if (y <= y1 + n && x >= x1 && x <= x2) {
r = new Rect(x1, y1 - n, w, n2);
            attr = "alignParentTop";               //$NON-NLS-1$

        } else if (y >= y2 - n && x >= x1 && x <= x2) {
r = new Rect(x1, y2 - n, w, n2);
            attr = "alignParentBottom";            //$NON-NLS-1$

} else {
            // we're nowhere near a border
            return null;
}

return new DropZone(r, Collections.singletonList(attr), r.getCenter(), vertical);
//Synthetic comment -- @@ -371,41 +422,41 @@
Rect bounds = new Rect(x1, y1, wt, ht);

List<DropZone> zones = new ArrayList<DropZone>(16);
        String a = "above"; //$NON-NLS-1$
int x = x1;
int y = y1;

        x = addx(w1, a, x, y, h1, zones, "toLeftOf");   //$NON-NLS-1$
        x = addx(w2, a, x, y, h1, zones, "alignLeft");  //$NON-NLS-1$
        x = addx(w2, a, x, y, h1, zones, "alignLeft", "alignRight"); //$NON-NLS-1$ //$NON-NLS-2$
        x = addx(w2, a, x, y, h1, zones, "alignRight"); //$NON-NLS-1$
        x = addx(w1, a, x, y, h1, zones, "toRightOf");  //$NON-NLS-1$

        a = "below"; //$NON-NLS-1$
x = x1;
y = y1 + ht - h1;

        x = addx(w1, a, x, y, h1, zones, "toLeftOf");    //$NON-NLS-1$
        x = addx(w2, a, x, y, h1, zones, "alignLeft");   //$NON-NLS-1$
        x = addx(w2, a, x, y, h1, zones, "alignLeft", "alignRight"); //$NON-NLS-1$ //$NON-NLS-2$
        x = addx(w2, a, x, y, h1, zones, "alignRight");  //$NON-NLS-1$
        x = addx(w1, a, x, y, h1, zones, "toRightOf");   //$NON-NLS-1$

        a = "toLeftOf"; //$NON-NLS-1$
x = x1;
y = y1 + h1;

        y = addy(h2, a, x, y, w1, zones, "alignTop");    //$NON-NLS-1$
        y = addy(h2, a, x, y, w1, zones, "alignTop", "alignBottom"); //$NON-NLS-1$ //$NON-NLS-2$
        y = addy(h2, a, x, y, w1, zones, "alignBottom"); //$NON-NLS-1$

        a = "toRightOf"; //$NON-NLS-1$
x = x1 + wt - w1;
y = y1 + h1;

        y = addy(h2, a, x, y, w1, zones, "alignTop");    //$NON-NLS-1$
        y = addy(h2, a, x, y, w1, zones, "alignTop", "alignBottom"); //$NON-NLS-1$ //$NON-NLS-2$
        y = addy(h2, a, x, y, w1, zones, "alignBottom"); //$NON-NLS-1$

return Pair.of(bounds, zones);
}
//Synthetic comment -- @@ -511,20 +562,20 @@
int offsetX = x - be.x;
int offsetY = y - be.y;

                if (data.getCurr().getAttr().contains("alignTop")                    //$NON-NLS-1$
                        && data.getCurr().getAttr().contains("alignBottom")) {       //$NON-NLS-1$
offsetY -= be.h / 2;
                } else if (data.getCurr().getAttr().contains("above")                //$NON-NLS-1$
                        || data.getCurr().getAttr().contains("alignTop")             //$NON-NLS-1$
                        || data.getCurr().getAttr().contains("alignParentBottom")) { //$NON-NLS-1$
offsetY -= be.h;
}
                if (data.getCurr().getAttr().contains("alignRight")                  //$NON-NLS-1$
                        && data.getCurr().getAttr().contains("alignLeft")) {         //$NON-NLS-1$
offsetX -= be.w / 2;
                } else if (data.getCurr().getAttr().contains("toLeftOft")            //$NON-NLS-1$
                        || data.getCurr().getAttr().contains("alignLeft")            //$NON-NLS-1$
                        || data.getCurr().getAttr().contains("alignParentRight")) {  //$NON-NLS-1$
offsetX -= be.w;
}

//Synthetic comment -- @@ -719,5 +770,9 @@
private boolean isVertical() {
return mVertical;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index d702255..0875642 100755

//Synthetic comment -- @@ -24,7 +24,6 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -425,10 +424,11 @@
IMenuCallback c = ((MenuAction.Action) a2).getCallback();
if (c != null) {
try {
                                        c.action(a2, null /* no valueId for a toggle */, !isChecked);
} catch (Exception e) {
                                        RulesEngine gre = mCanvas.getRulesEngine();
                                        gre.logError("XML edit operation failed: %s", e.toString());
}
}
}
//Synthetic comment -- @@ -488,8 +488,8 @@
// Values do not apply for plain actions
c.action(a2, null /* valueId */, null /* newValue */);
} catch (Exception e) {
                                        RulesEngine gre = mCanvas.getRulesEngine();
                                        gre.logError("XML edit operation failed: %s", e.toString());
}
}
}
//Synthetic comment -- @@ -614,8 +614,8 @@
((MenuAction.Action) a2).getCallback().action(a2, key,
!isChecked);
} catch (Exception e) {
                                        RulesEngine gre = mCanvas.getRulesEngine();
                                        gre.logError("XML edit operation failed: %s", e.toString());
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index d3a7820..314719a 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
//Synthetic comment -- @@ -612,6 +614,16 @@
int deltaX = (int) (scale * (boundingBox.x - p.x));
int deltaY = (int) (scale * (boundingBox.y - p.y));
SwtUtils.setDragImageOffsets(e, -deltaX, -deltaY);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java
//Synthetic comment -- index 3fd98a3..9315967 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;


/**
* This singleton is used to keep track of drag'n'drops initiated within this
//Synthetic comment -- @@ -43,6 +45,7 @@
private SelectionItem[] mCurrentSelection;
private Object mSourceCanvas = null;
private Runnable mRemoveSourceHandler;

/** Private constructor. Use {@link #getInstance()} to retrieve the singleton. */
private GlobalCanvasDragInfo() {
//Synthetic comment -- @@ -114,4 +117,30 @@
mRemoveSourceHandler = null;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index ede9115..cce6c2a 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.layoutlib.BasicLayoutScene;
import com.android.ide.common.layoutlib.LayoutLibrary;
import com.android.ide.common.sdk.LoadStatus;
//Synthetic comment -- @@ -1176,7 +1178,15 @@
return mConfigComposite.getScreenBounds();
}



// --- private methods ---









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index f047cbc..863b250 100644

//Synthetic comment -- @@ -446,6 +446,28 @@
}
df.sameCanvas = mCanvas == mGlobalDragInfo.getSourceCanvas();
df.invalidTarget = false;
}

/**
//Synthetic comment -- @@ -560,6 +582,7 @@
// guideline computations in onDropMove (since only onDropMove is handed
// the -position- of the mouse), and we want this computation to happen
// before we ask the view to draw its feedback.
df = mCanvas.getRulesEngine().callOnDropMove(targetNode,
mCurrentDragElements, df, new Point(p.x, p.y));
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index f5aaeed..e3bbd00 100755

//Synthetic comment -- @@ -510,12 +510,16 @@
Rect bounds = null;
DragSource dragSource = mItem.getDragSource();
DragSourceEffect dragSourceEffect = dragSource.getDragSourceEffect();
if (dragSourceEffect instanceof PreviewDragSourceEffect) {
PreviewDragSourceEffect preview = (PreviewDragSourceEffect) dragSourceEffect;
Image previewImage = preview.getPreviewImage();
if (previewImage != null && !preview.isPlaceholder()) {
ImageData data = previewImage.getImageData();
                    bounds = new Rect(0, 0, data.width, data.height);
}
}

//Synthetic comment -- @@ -527,11 +531,13 @@
mElements = new SimpleElement[] { se };

// Register this as the current dragged data
            GlobalCanvasDragInfo.getInstance().startDrag(
mElements,
null /* selection */,
null /* canvas */,
null /* removeSource */);
}

public void dragSetData(DragSourceEvent e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index ff6cbfe..ae894e6 100755

//Synthetic comment -- @@ -192,7 +192,7 @@
return rule.getDisplayName();

} catch (Exception e) {
                logError("%s.getDisplayName() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -218,7 +218,7 @@
return rule.getContextMenu(selectedNode);

} catch (Exception e) {
                logError("%s.getContextMenu() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -244,7 +244,7 @@
return rule.getSelectionHint(parentNode, childNode);

} catch (Exception e) {
                logError("%getSelectionHint() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -269,7 +269,7 @@
return rule.onDropEnter(targetNode, elements);

} catch (Exception e) {
                logError("%s.onDropEnter() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -295,7 +295,7 @@
return rule.onDropMove(targetNode, elements, feedback, where);

} catch (Exception e) {
                logError("%s.onDropMove() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -318,7 +318,7 @@
rule.onDropLeave(targetNode, elements, feedback);

} catch (Exception e) {
                logError("%s.onDropLeave() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -342,7 +342,7 @@
rule.onDropped(targetNode, elements, feedback, where);

} catch (Exception e) {
                logError("%s.onDropped() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -359,7 +359,7 @@
try {
feedback.painter.paint(gc, targetNode, feedback);
} catch (Exception e) {
                logError("DropFeedback.painter failed: %s",
e.toString());
}
}
//Synthetic comment -- @@ -381,7 +381,7 @@
rule.onPaste(targetNode, pastedElements);

} catch (Exception e) {
                logError("%s.onPaste() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -490,7 +490,7 @@
try {
rule.onDispose();
} catch (Exception e) {
                    logError("%s.onDispose() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -649,10 +649,10 @@
// class.
} catch (InstantiationException e) {
// This is NOT an expected error: fail.
            logError("load rule error (%s): %s", realFqcn, e.toString());
} catch (IllegalAccessException e) {
// This is NOT an expected error: fail.
            logError("load rule error (%s): %s", realFqcn, e.toString());
}

// Memorize in the cache that we couldn't find a rule for this real FQCN
//Synthetic comment -- @@ -687,7 +687,7 @@
rule.onDispose();
}
} catch (Exception e) {
            logError("%s.onInit() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -696,18 +696,6 @@
}

/**
     * Logs an error to the console.
     *
     * @param format A format string following the format specified by
     *            {@link String#format}
     * @param params An optional set of parameters to the format string
     */
    public void logError(String format, Object...params) {
        String s = String.format(format, params);
        AdtPlugin.printErrorToConsole(mProject, s);
    }

    /**
* Implementation of {@link IClientRulesEngine}. This provide {@link IViewRule} clients
* with a few methods they can use to use functionality from this {@link RulesEngine}.
*/
//Synthetic comment -- @@ -750,7 +738,7 @@
try {
return filter.validate(newText);
} catch (Exception e) {
                            logError("Custom validator failed: %s", e.toString());
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
                "useStyle(DROP_PREVIEW), drawRect(Rect[-22,-50,105,80])");

        assertEquals("-22dip", inserted.getStringAttr(ANDROID_URI, "layout_x"));
        assertEquals("-50dip", inserted.getStringAttr(ANDROID_URI, "layout_y"));

// Without drag bounds we should just draw guide lines instead
inserted = dragInto(new Rect(0, 0, 0, 0), new Point(30, -10), 4, -1,







