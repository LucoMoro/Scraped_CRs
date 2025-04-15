/*Refactor selection painting to fix highlighting of included views

This changeset moves the painting of selection bounds and selection
hints out of the view rules and into the core IDE. The reason for this
is that the visual appearance of the selection shouldn't be up to each
rule; for one thing the selection highlight should be consistent and
not vary from view to view (and in fact there was only a single
implementation of the paint selection method among the view rules),
and for another the view rules are in theory sharable among IDEs
whereas the selection appearance is going to be IDE specific. There
was also painting of "hints" in the RelativeLayout; rather than having
the visual appearance of this dictated by the rule, this is also moved
into the IDE such that the rules only provide the hint text and the
hints are displayed by the IDE itself.

The above refactoring also fixes selection feedback for <include>'ed
views, which were not visually selectable because there was no
corresponding ViewRule, so nobody to paint selection. With these
changes selection painting is now independent of the rules.

Change-Id:I22dd926102128634a443b8bafb54d4764f1eda41*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/IViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/IViewRule.java
//Synthetic comment -- index 0ec8eb3..fba22ba 100755

//Synthetic comment -- @@ -99,38 +99,18 @@
// ==== Selection ====

/**
     * Called by the canvas when a view is being selected.
     * <p/>
     * Before the method is called, the canvas' Graphic Context is initialized
     * with a foreground color already set to the desired selection color, fully
     * opaque and with the default adequate font.
     *
     * @param gc An {@link IGraphics} instance, to perform drawing operations.
     * @param selectedNode The node selected. Never null.
     * @param displayName The name to display, as returned by {@link #getDisplayName()}.
     * @param isMultipleSelection A boolean set to true if more than one element is selected.
     */
    void onSelected(IGraphics gc,
            INode selectedNode,
            String displayName,
            boolean isMultipleSelection);

    /**
     * Called by the canvas when a single child view is being selected.
* <p/>
* Note that this is called only for single selections.
* <p/>
     * This allows a parent to draw stuff around its children, for example to display
     * layout attributes graphically.
*
     * @param gc An {@link IGraphics} instance, to perform drawing operations.
* @param parentNode The parent of the node selected. Never null.
* @param childNode The child node that was selected. Never null.
*/
    void onChildSelected(IGraphics gc,
            INode parentNode,
            INode childNode);


// ==== Drag'n'drop support ====









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java
//Synthetic comment -- index 045f36d..92f0543 100644

//Synthetic comment -- @@ -16,12 +16,10 @@

package com.android.ide.common.layout;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
//Synthetic comment -- @@ -29,13 +27,11 @@
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.IAttributeInfo.Format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
//Synthetic comment -- @@ -416,53 +412,8 @@

// ==== Selection ====

    public void onSelected(IGraphics gc, INode selectedNode, String displayName,
            boolean isMultipleSelection) {
        Rect r = selectedNode.getBounds();

        if (!r.isValid()) {
            return;
        }

        gc.useStyle(DrawingStyle.SELECTION);
        gc.fillRect(r);
        gc.drawRect(r);

        if (displayName == null || isMultipleSelection) {
            return;
        }

        int xs = r.x + 2;
        int ys = r.y - gc.getFontHeight() - 4;
        if (ys < 0) {
            ys = r.y + r.h + 3;
        }
        gc.useStyle(DrawingStyle.HELP);
        gc.drawBoxedStrings(xs, ys, Collections.singletonList(displayName));
    }

    public void onChildSelected(IGraphics gc, INode parentNode, INode childNode) {
        // @formatter:off
        /* Drawing anchor lines: temporarily disabled. Enable via context menu perhaps?
        Rect rp = parentNode.getBounds();
        Rect rc = childNode.getBounds();

        if (rp.isValid() && rc.isValid()) {
            gc.useStyle(DrawingStyle.ANCHOR);

            // top line
            int m = rc.x + rc.w / 2;
            gc.drawLine(m, rc.y, m, rp.y);
            // bottom line
            gc.drawLine(m, rc.y + rc.h, m, rp.y + rp.h);
            // left line
            m = rc.y + rc.h / 2;
            gc.drawLine(rc.x, m, rp.x, m);
            // right line
            gc.drawLine(rc.x + rc.w, m, rp.x + rp.w, m);
        }
        */
        // @formatter:on
}

// ==== Drag'n'drop support ====








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index f88bd95..96e6aa6 100755

//Synthetic comment -- @@ -46,28 +46,8 @@

// ==== Selection ====

    /**
     * Display some relation layout information on a selected child.
     */
@Override
    public void onChildSelected(IGraphics gc, INode parentNode, INode childNode) {
        super.onChildSelected(gc, parentNode, childNode);

        // Get the top parent, to display data under it
        INode topParent = parentNode;
        while (true) {
            INode p = topParent.getParent();
            if (p == null) {
                break;
            }
            topParent = p;
        }

        Rect b = topParent.getBounds();
        if (!b.isValid()) {
            return;
        }

List<String> infos = new ArrayList<String>(18);
addAttr("above", childNode, infos);                   //$NON-NLS-1$
addAttr("below", childNode, infos);                   //$NON-NLS-1$
//Synthetic comment -- @@ -87,12 +67,7 @@
addAttr("centerInParent", childNode, infos);          //$NON-NLS-1$
addAttr("centerVertical", childNode, infos);          //$NON-NLS-1$

        if (infos.size() > 0) {
            gc.useStyle(DrawingStyle.HELP);
            int x = b.x + 10;
            int y = b.y + b.h + 10;
            gc.drawBoxedStrings(x, y, infos);
        }
}

private void addAttr(String propertyName, INode childNode, List<String> infos) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java
//Synthetic comment -- index 26e960e..3648258 100755

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.INode;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
//Synthetic comment -- @@ -47,7 +46,6 @@
/** The name displayed over the selection, typically the widget class name. Can be null. */
private final String mName;


/**
* Creates a new {@link CanvasSelection} object.
* @param canvasViewInfo The view info being selected. Must not be null.
//Synthetic comment -- @@ -105,34 +103,9 @@
return mName;
}

    /**
     * Calls IViewRule.onSelected on the selected view.
     *
     * @param gre The rules engines.
     * @param gcWrapper The GC to use for drawing.
     * @param isMultipleSelection True if more than one view is selected.
     */
    /*package*/ void paintSelection(RulesEngine gre,
            GCWrapper gcWrapper,
            boolean isMultipleSelection) {
        if (mNodeProxy != null) {
            gre.callOnSelected(gcWrapper, mNodeProxy, mName, isMultipleSelection);
        }
    }

    /**
     * Calls IViewRule.onChildSelected on the parent of the selected view, if it has one.
     *
     * @param gre The rules engines.
     * @param gcWrapper The GC to use for drawing.
     */
    public void paintParentSelection(RulesEngine gre, GCWrapper gcWrapper) {
        if (mNodeProxy != null) {
            INode parent = mNodeProxy.getParent();
            if (parent instanceof NodeProxy) {
                gre.callOnChildSelected(gcWrapper, (NodeProxy) parent, mNodeProxy);
            }
        }
}

//----








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 3cb51d9..8a7dd7a 100755

//Synthetic comment -- @@ -556,7 +556,7 @@
}

mHoverOverlay.paint(gc);
            mSelectionOverlay.paint(mSelectionManager, gc, mGCWrapper, mRulesEngine);
mGestureManager.paint(gc);

} finally {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java
//Synthetic comment -- index 05e405a..696ca49 100644

//Synthetic comment -- @@ -16,70 +16,114 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;

import java.util.List;

/**
* The {@link SelectionOverlay} paints the current selection as an overlay.
*/
public class SelectionOverlay extends Overlay {
    /** CanvasSelection border color. */
    private Color mSelectionFgColor;

/**
* Constructs a new {@link SelectionOverlay} tied to the given canvas.
*/
public SelectionOverlay() {
}

    @Override
    public void create(Device device) {
        mSelectionFgColor = new Color(device, SwtDrawingStyle.SELECTION.getStrokeColor());
    }

    @Override
    public void dispose() {
        if (mSelectionFgColor != null) {
            mSelectionFgColor.dispose();
            mSelectionFgColor = null;
        }
    }

/**
* Paints the selection.
*
* @param selectionManager The {@link SelectionManager} holding the
*            selection.
     * @param gc The graphics context to draw into.
* @param gcWrapper The graphics context wrapper for the layout rules to use.
* @param rulesEngine The {@link RulesEngine} holding the rules.
*/
    public void paint(SelectionManager selectionManager, GC gc, GCWrapper gcWrapper,
RulesEngine rulesEngine) {
List<CanvasSelection> selections = selectionManager.getSelections();
int n = selections.size();
if (n > 0) {
boolean isMultipleSelection = n > 1;

            if (n == 1) {
                gc.setForeground(mSelectionFgColor);
                selections.get(0).paintParentSelection(rulesEngine, gcWrapper);
            }

for (CanvasSelection s : selections) {
if (s.isRoot()) {
// The root selection is never painted
continue;
}
                gc.setForeground(mSelectionFgColor);
                s.paintSelection(rulesEngine, gcWrapper, isMultipleSelection);
}
}
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 95f56e1..d84ed0d 100755

//Synthetic comment -- @@ -228,55 +228,30 @@
}

/**
     * Invokes {@link IViewRule#onSelected(IGraphics, INode, String, boolean)}
* on the rule matching the specified element.
*
     * @param gc An {@link IGraphics} instance, to perform drawing operations.
     * @param selectedNode The node selected. Never null.
     * @param displayName The name to display, as returned by {@link IViewRule#getDisplayName()}.
     * @param isMultipleSelection A boolean set to true if more than one element is selected.
     */
    public void callOnSelected(IGraphics gc, NodeProxy selectedNode,
            String displayName, boolean isMultipleSelection) {
        // try to find a rule for this element's FQCN
        IViewRule rule = loadRule(selectedNode.getNode());

        if (rule != null) {
            try {
                rule.onSelected(gc, selectedNode, displayName, isMultipleSelection);

            } catch (Exception e) {
                logError("%s.onSelected() failed: %s",
                        rule.getClass().getSimpleName(),
                        e.toString());
            }
        }
    }

    /**
     * Invokes {@link IViewRule#onChildSelected(IGraphics, INode, INode)}
     * on the rule matching the specified element.
     *
     * @param gc An {@link IGraphics} instance, to perform drawing operations.
* @param parentNode The parent of the node selected. Never null.
* @param childNode The child node that was selected. Never null.
*/
    public void callOnChildSelected(IGraphics gc, NodeProxy parentNode, NodeProxy childNode) {
// try to find a rule for this element's FQCN
IViewRule rule = loadRule(parentNode.getNode());

if (rule != null) {
try {
                rule.onChildSelected(gc, parentNode, childNode);

} catch (Exception e) {
                logError("%s.onChildSelected() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
}
    }


/**
* Called when the d'n'd starts dragging over the target node.







