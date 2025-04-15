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
     * Returns a list of strings that will be displayed when a single child is being
     * selected in a layout corresponding to this rule. This gives the container a chance
     * to describe the child's layout attributes or other relevant information.
* <p/>
* Note that this is called only for single selections.
* <p/>
*
* @param parentNode The parent of the node selected. Never null.
* @param childNode The child node that was selected. Never null.
     * @return a list of strings to be displayed, or null or empty to display nothing
*/
    List<String> getSelectionHint(INode parentNode, INode childNode);

// ==== Drag'n'drop support ====









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java
//Synthetic comment -- index 045f36d..92f0543 100644

//Synthetic comment -- @@ -16,12 +16,10 @@

package com.android.ide.common.layout;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
//Synthetic comment -- @@ -29,13 +27,11 @@
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.IAttributeInfo.Format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
//Synthetic comment -- @@ -416,53 +412,8 @@

// ==== Selection ====

    public List<String> getSelectionHint(INode parentNode, INode childNode) {
        return null;
}

// ==== Drag'n'drop support ====








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index f88bd95..96e6aa6 100755

//Synthetic comment -- @@ -46,28 +46,8 @@

// ==== Selection ====

@Override
    public List<String> getSelectionHint(INode parentNode, INode childNode) {
List<String> infos = new ArrayList<String>(18);
addAttr("above", childNode, infos);                   //$NON-NLS-1$
addAttr("below", childNode, infos);                   //$NON-NLS-1$
//Synthetic comment -- @@ -87,12 +67,7 @@
addAttr("centerInParent", childNode, infos);          //$NON-NLS-1$
addAttr("centerVertical", childNode, infos);          //$NON-NLS-1$

        return infos;
}

private void addAttr(String propertyName, INode childNode, List<String> infos) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasSelection.java
//Synthetic comment -- index 26e960e..3648258 100755

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

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

    /** Returns the node associated with this selection (may be null) */
    /* package */ NodeProxy getNode() {
        return mNodeProxy;
}

//----








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 3cb51d9..8a7dd7a 100755

//Synthetic comment -- @@ -556,7 +556,7 @@
}

mHoverOverlay.paint(gc);
            mSelectionOverlay.paint(mSelectionManager, mGCWrapper, mRulesEngine);
mGestureManager.paint(gc);

} finally {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java
//Synthetic comment -- index 05e405a..696ca49 100644

//Synthetic comment -- @@ -16,70 +16,114 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;

import java.util.Collections;
import java.util.List;

/**
* The {@link SelectionOverlay} paints the current selection as an overlay.
*/
public class SelectionOverlay extends Overlay {
/**
* Constructs a new {@link SelectionOverlay} tied to the given canvas.
*/
public SelectionOverlay() {
}

/**
* Paints the selection.
*
* @param selectionManager The {@link SelectionManager} holding the
*            selection.
* @param gcWrapper The graphics context wrapper for the layout rules to use.
* @param rulesEngine The {@link RulesEngine} holding the rules.
*/
    public void paint(SelectionManager selectionManager, GCWrapper gcWrapper,
RulesEngine rulesEngine) {
List<CanvasSelection> selections = selectionManager.getSelections();
int n = selections.size();
if (n > 0) {
boolean isMultipleSelection = n > 1;
for (CanvasSelection s : selections) {
if (s.isRoot()) {
// The root selection is never painted
continue;
}

                NodeProxy node = s.getNode();
                if (node != null) {
                    String name = s.getName();
                    paintSelection(gcWrapper, node, name, isMultipleSelection);
                }
            }

            if (n == 1) {
                NodeProxy node = selections.get(0).getNode();
                if (node != null) {
                    paintHints(gcWrapper, node, rulesEngine);
                }
}
}
}

    /** Paint hint for current selection */
    private void paintHints(GCWrapper gcWrapper, NodeProxy node, RulesEngine rulesEngine) {
        INode parent = node.getParent();
        if (parent instanceof NodeProxy) {
            NodeProxy parentNode = (NodeProxy) parent;

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
            if (b.isValid()) {
                List<String> infos = rulesEngine.callGetSelectionHint(parentNode, node);
                if (infos != null && infos.size() > 0) {
                    gcWrapper.useStyle(DrawingStyle.HELP);
                    int x = b.x + 10;
                    int y = b.y + b.h + 10;
                    gcWrapper.drawBoxedStrings(x, y, infos);
                }
            }
        }
    }

    /** Called by the canvas when a view is being selected. */
    private void paintSelection(IGraphics gc, INode selectedNode, String displayName,
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
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 95f56e1..d84ed0d 100755

//Synthetic comment -- @@ -228,55 +228,30 @@
}

/**
     * Invokes {@link IViewRule#getSelectionHint(INode, INode)}
* on the rule matching the specified element.
*
* @param parentNode The parent of the node selected. Never null.
* @param childNode The child node that was selected. Never null.
     * @return a list of strings to be displayed, or null or empty to display nothing
*/
    public List<String> callGetSelectionHint(NodeProxy parentNode, NodeProxy childNode) {
// try to find a rule for this element's FQCN
IViewRule rule = loadRule(parentNode.getNode());

if (rule != null) {
try {
                return rule.getSelectionHint(parentNode, childNode);

} catch (Exception e) {
                logError("%getSelectionHint() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
}

        return null;
    }

/**
* Called when the d'n'd starts dragging over the target node.







