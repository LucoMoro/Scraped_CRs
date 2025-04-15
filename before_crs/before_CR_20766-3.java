/*Cluster of improvements for merge tag views

This changeset contains various improvements around usage of the
<merge> tag. Some of these fixes require layoutlib 5.

* Use the new layoutlib support for rendering multiple children at the
  root level - they now show up in the Outline (provided you are
  running layoutlib 5), can be selected in the layout editor, etc.

* Add a drop handler such that you can drag into the <merge> view and
  get drop feedback (similar to the FrameLayout)

* If the <merge> is empty, we don't get any ViewInfos, so in that case
  manufacture a dummy view sized to the screen. Similarly, if we get
  back ViewInfos that are children of a <merge> tag in the UI model,
  create a <merge> view initialized to the bounding rectangle of these
  views and reparent the views to it.

* Support highlighting multiple views simultaneously when you select
  an include tag that renders into multiple views (because the root of
  the included layout was a <merge> tag).  Similarly, make "Show
  Included In" work properly for <merge> views, and make the overlay
  mask used to hide all included content also reveal only the primary
  selected views (when a view is included more than once.) (Also tweak
  the visual appearance of the mask, and use better icon for the view
  root in the included-root scenario.)

* Improve the algorithm which deals with render results with null
  keys. Use adjacent children that -do- have keys as constraints when
  attempting to match up views without keys and unreferenced model
  nodes. This fixes issuehttp://code.google.com/p/android/issues/detail?id=14188* Improve the way we pick views under the mouse. This used to search
  down the view hierarchy in sibling order. Instead, search in reverse
  sibling order since this will match what is drawn in the layout. For
  views like FrameLayout and <merge> views, the children are painted
  on top of ech other, so clicking on whatever is on top should choose
  that view, not some earlier sibling below it.

* Fix such that when you drag into the canvas, we *always* target the
  root node, even if it is not under the mouse. This is particularly
  important with <merge> tags, but this also helps if you for example
  have a LinearLayout as the root element, and the layout_height
  property is wrap_content instead of match_parent. In that case, the
  LinearLayout will *only* cover its children, so if you drag over the
  visual screen, it looks like you should be able to drop into the
  layout, but you cannot since it only covers its children. With this
  fix, all positions outside the root element's actual bounds are also
  considered targetting the root.

* Fix broken unit test, add new unit tests.

Change-Id:Id96a06a8763d02845af4531a47fe32afe703df2f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MergeRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MergeRule.java
new file mode 100644
//Synthetic comment -- index 0000000..77f5c22

//Synthetic comment -- @@ -0,0 +1,37 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 84647bd..0585f4f 100755

//Synthetic comment -- @@ -16,8 +16,11 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.UiElementPullParser;
//Synthetic comment -- @@ -25,6 +28,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
//Synthetic comment -- @@ -34,8 +38,11 @@
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
* Maps a {@link ViewInfo} in a structure more adapted to our needs.
//Synthetic comment -- @@ -66,7 +73,7 @@
private final String mName;
private final Object mViewObject;
private final UiViewElementNode mUiViewNode;
    private final CanvasViewInfo mParent;
private final ArrayList<CanvasViewInfo> mChildren = new ArrayList<CanvasViewInfo>();

/**
//Synthetic comment -- @@ -77,6 +84,17 @@
private boolean mExploded;

/**
* Constructs a {@link CanvasViewInfo} initialized with the given initial values.
*/
private CanvasViewInfo(CanvasViewInfo parent, String name,
//Synthetic comment -- @@ -142,6 +160,77 @@
}

/**
* Returns true if the specific {@link CanvasViewInfo} is a parent
* of this {@link CanvasViewInfo}. It can be a direct parent or any
* grand-parent higher in the hierarchy.
//Synthetic comment -- @@ -376,6 +465,32 @@
return null;
}

// ---- Factory functionality ----

/**
//Synthetic comment -- @@ -402,234 +517,488 @@
* @param root the root {@link ViewInfo} to build from
* @return a {@link CanvasViewInfo} hierarchy
*/
    public static CanvasViewInfo create(ViewInfo root) {
        if (root.getCookie() == null) {
            // Special case: If the root-most view does not have a view cookie,
            // then we are rendering some outer layout surrounding this layout, and in
            // that case we must search down the hierarchy for the (possibly multiple)
            // sub-roots that correspond to elements in this layout, and place them inside
            // an outer view that has no node. In the outline this item will be used to
            // show the inclusion-context.
            CanvasViewInfo rootView = createView(null, root, 0, 0);
            addKeyedSubtrees(rootView, root, 0, 0);
            return rootView;
        } else {
            // We have a view key at the top, so just go and create {@link CanvasViewInfo}
            // objects for each {@link ViewInfo} until we run into a null key.
            return addKeyedSubtrees(null, root, 0, 0);
        }
}

    /** Creates a {@link CanvasViewInfo} for a given {@link ViewInfo} but does not recurse */
    private static CanvasViewInfo createView(CanvasViewInfo parent, ViewInfo root, int parentX,
            int parentY) {
        Object cookie = root.getCookie();
        UiViewElementNode node = null;
        if (cookie instanceof UiViewElementNode) {
            node = (UiViewElementNode) cookie;
        }

        return createView(parent, root, parentX, parentY, node);
    }

    /**
     * Creates a {@link CanvasViewInfo} for a given {@link ViewInfo} but does not recurse.
     * This method specifies an explicit {@link UiViewElementNode} to use rather than
     * relying on the view cookie in the info object.
     */
    private static CanvasViewInfo createView(CanvasViewInfo parent, ViewInfo root, int parentX,
            int parentY, UiViewElementNode node) {

        int x = root.getLeft();
        int y = root.getTop();
        int w = root.getRight() - x;
        int h = root.getBottom() - y;

        x += parentX;
        y += parentY;

        Rectangle absRect = new Rectangle(x, y, w - 1, h - 1);

        if (w < SELECTION_MIN_SIZE) {
            int d = (SELECTION_MIN_SIZE - w) / 2;
            x -= d;
            w += SELECTION_MIN_SIZE - w;
        }

        if (h < SELECTION_MIN_SIZE) {
            int d = (SELECTION_MIN_SIZE - h) / 2;
            y -= d;
            h += SELECTION_MIN_SIZE - h;
        }

        Rectangle selectionRect = new Rectangle(x, y, w - 1, h - 1);

        return new CanvasViewInfo(parent, root.getClassName(), root.getViewObject(), node, absRect,
                selectionRect);
    }

    /** Create a subtree recursively until you run out of keys */
    private static CanvasViewInfo createSubtree(CanvasViewInfo parent, ViewInfo viewInfo,
            int parentX, int parentY) {
        assert viewInfo.getCookie() != null;

        CanvasViewInfo view = createView(parent, viewInfo, parentX, parentY);

        // Process children:
        parentX += viewInfo.getLeft();
        parentY += viewInfo.getTop();

        // See if we have any missing keys at this level
        int missingNodes = 0;
        List<ViewInfo> children = viewInfo.getChildren();
        for (ViewInfo child : children) {
            // Only use children which have a ViewKey of the correct type.
            // We can't interact with those when they have a null key or
            // an incompatible type.
            Object cookie = child.getCookie();
            if (!(cookie instanceof UiViewElementNode)) {
                missingNodes++;
            }
        }

        if (missingNodes == 0) {
            // No missing nodes; this is the normal case, and we can just continue to
            // recursively add our children
            for (ViewInfo child : children) {
                CanvasViewInfo childView = createSubtree(view, child, parentX, parentY);
                view.addChild(childView);
            }
        } else {
            // We don't have keys for one or more of the ViewInfos. There are many
            // possible causes: we are on an SDK platform that does not support
            // embedded_layout rendering, or we are including a view with a <merge>
            // as the root element.

            String containerName = view.getUiViewNode().getDescriptor().getXmlLocalName();
            if (containerName.equals(LayoutDescriptors.VIEW_INCLUDE)) {
                // This is expected -- we don't WANT to get node keys for the content
                // of an include since it's in a different file and should be treated
                // as a single unit that cannot be edited (hence, no CanvasViewInfo
                // children)
            } else {
                // We are getting children with null keys where we don't expect it;
                // this usually means that we are dealing with an Android platform
                // that does not support {@link Capability#EMBEDDED_LAYOUT}, or
                // that there are <merge> tags which are doing surprising things
                // to the view hierarchy
                LinkedList<UiViewElementNode> unused = new LinkedList<UiViewElementNode>();
                for (UiElementNode child : view.getUiViewNode().getUiChildren()) {
                    if (child instanceof UiViewElementNode) {
                        unused.addLast((UiViewElementNode) child);
}
}
                for (ViewInfo child : children) {
                    Object cookie = child.getCookie();
                    if (cookie != null) {
                        unused.remove(cookie);
}
                }
                if (unused.size() > 0) {
                    if (unused.size() == missingNodes) {
                        // The number of unmatched elements and ViewInfos are identical;
                        // it's very likely that they match one to one, so just use these
                        for (ViewInfo child : children) {
                            if (child.getCookie() == null) {
                                // Only create a flat (non-recursive) view
                                CanvasViewInfo childView = createView(view, child, parentX,
                                        parentY, unused.removeFirst());
                                view.addChild(childView);
} else {
                                CanvasViewInfo childView = createSubtree(view, child, parentX,
                                        parentY);
view.addChild(childView);
}
}
                    } else {
                        // We have an uneven match. In this case we might be dealing
                        // with <merge> etc.
                        // We have no way to associate elements back with the
                        // corresponding <include> tags if there are more than one of
                        // them. That's not a huge tragedy since visually you are not
                        // allowed to edit these anyway; we just need to make a visual
                        // block for these for selection and outline purposes.
                        UiViewElementNode reference = unused.get(0);
                        addBoundingView(view, children, reference, parentX, parentY);
}
}
}
}

        return view;
    }

    /**
     * Add a single bounding view for all the non-keyed children with dimensions that span
     * the bounding rectangle of all these children, and associate it with the given node
     * reference. Keyed children are added in the normal way.
     */
    private static void addBoundingView(CanvasViewInfo parentView, List<ViewInfo> children,
            UiViewElementNode reference, int parentX, int parentY) {
        Rectangle absRect = null;
        int insertIndex = -1;
        for (int index = 0, size = children.size(); index < size; index++) {
            ViewInfo child = children.get(index);
            if (child.getCookie() == null) {
                int x = child.getLeft();
                int y = child.getTop();
                int width = child.getRight() - x;
                int height = child.getBottom() - y;
                Rectangle rect = new Rectangle(x, y, width, height);
                if (absRect == null) {
                    absRect = rect;
                    insertIndex = index;
} else {
                    absRect = absRect.union(rect);
                }
            } else {
                CanvasViewInfo childView = createSubtree(parentView, child, parentX, parentY);
                parentView.addChild(childView);
            }
        }
        if (absRect != null) {
            absRect.x += parentX;
            absRect.y += parentY;
            String name = reference.getDescriptor().getXmlLocalName();
            CanvasViewInfo childView = new CanvasViewInfo(parentView, name, null, reference,
                    absRect, absRect);
            parentView.addChild(childView, insertIndex);
        }
    }

    /** Search for a subtree with valid keys and add those subtrees */
    private static CanvasViewInfo addKeyedSubtrees(CanvasViewInfo parent, ViewInfo viewInfo,
            int parentX, int parentY) {
        if (viewInfo.getCookie() != null) {
            CanvasViewInfo subtree = createSubtree(parent, viewInfo, parentX, parentY);
            if (parent != null) {
                parent.mChildren.add(subtree);
}
            return subtree;
        } else {
            for (ViewInfo child : viewInfo.getChildren()) {
                addKeyedSubtrees(parent, child, parentX + viewInfo.getLeft(), parentY
                        + viewInfo.getTop());
}

return null;
}
    }

    /** Adds the given {@link CanvasViewInfo} as a new last child of this view */
    private void addChild(CanvasViewInfo child) {
        mChildren.add(child);
    }

    /** Adds the given {@link CanvasViewInfo} as a new child at the given index */
    private void addChild(CanvasViewInfo child, int index) {
        if (index < 0) {
            index = mChildren.size();
}
        mChildren.add(index, child);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 8e94358..cb5c849 100644

//Synthetic comment -- @@ -1760,6 +1760,9 @@
* Called when the file changes triggered a redraw of the layout
*/
public void reloadLayout(final ChangeFlags flags, final boolean libraryChanged) {
Display display = mConfigComposite.getDisplay();
display.asyncExec(new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeOverlay.java
//Synthetic comment -- index 84f3e01..66adad8 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
*/
public class IncludeOverlay extends Overlay {
/** Mask transparency - 0 is transparent, 255 is opaque */
    private static final int MASK_TRANSPARENCY = 208;

/** The associated {@link LayoutCanvas}. */
private LayoutCanvas mCanvas;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index 16f6fba..07ab41c 100644

//Synthetic comment -- @@ -521,6 +521,15 @@
vi = mCurrentView;
} else {
vi = mCanvas.getViewHierarchy().findViewInfoAt(p);
}

boolean isMove = true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index d7bc412..02f98b1 100755

//Synthetic comment -- @@ -407,7 +407,7 @@
}
}
if (element instanceof CanvasViewInfo) {
                List<CanvasViewInfo> children = ((CanvasViewInfo) element).getChildren();
if (children != null) {
return children.toArray();
}
//Synthetic comment -- @@ -497,6 +497,8 @@
element = vi.getUiViewNode();
}

if (element instanceof UiElementNode) {
UiElementNode node = (UiElementNode) element;
styledString = node.getStyledDescription();
//Synthetic comment -- @@ -549,6 +551,7 @@
if (includedWithin != null) {
styledString = new StyledString();
styledString.append(includedWithin.getDisplayName(), QUALIFIER_STYLER);
}
}

//Synthetic comment -- @@ -559,7 +562,7 @@

cell.setText(styledString.toString());
cell.setStyleRanges(styledString.getStyleRanges());
           cell.setImage(getImage(element));
super.update(cell);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionOverlay.java
//Synthetic comment -- index 90aeebf..817f2f9 100644

//Synthetic comment -- @@ -63,7 +63,7 @@
NodeProxy node = s.getNode();
if (node != null) {
String name = s.getName();
                    paintSelection(gcWrapper, node, name, isMultipleSelection);
}
}

//Synthetic comment -- @@ -121,7 +121,7 @@
}

/** Called by the canvas when a view is being selected. */
    private void paintSelection(IGraphics gc, INode selectedNode, String displayName,
boolean isMultipleSelection) {
Rect r = selectedNode.getBounds();

//Synthetic comment -- @@ -133,6 +133,18 @@
gc.fillRect(r);
gc.drawRect(r);

/* Label hidden pending selection visual design
if (displayName == null || isMultipleSelection) {
return;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index 738da30..01487b9 100644

//Synthetic comment -- @@ -16,21 +16,27 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.INode;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
//Synthetic comment -- @@ -52,7 +58,7 @@
}

/**
     * The CanvasViewInfo root created by the last call to {@link #setResult}
* with a valid layout.
* <p/>
* This <em>can</em> be null to indicate we're dealing with an empty document with
//Synthetic comment -- @@ -63,7 +69,7 @@
private CanvasViewInfo mLastValidViewInfoRoot;

/**
     * True when the last {@link #setResult} provided a valid {@link LayoutScene}.
* <p/>
* When false this means the canvas is displaying an out-dated result image & bounds and some
* features should be disabled accordingly such a drag'n'drop.
//Synthetic comment -- @@ -137,21 +143,42 @@
mSession = session;
mIsResultValid = (session != null && session.getResult().isSuccess());
mExplodedParents = false;
        mIncludedBounds = null;

if (mIsResultValid && session != null) {
            ViewInfo root = null;

List<ViewInfo> rootList = session.getRootViews();

            if (rootList != null && rootList.size() > 0) {
                root = rootList.get(0);
            }

            if (root == null) {
                mLastValidViewInfoRoot = null;
} else {
                mLastValidViewInfoRoot = CanvasViewInfo.create(root);
}

updateNodeProxies(mLastValidViewInfoRoot, null);
//Synthetic comment -- @@ -167,10 +194,41 @@
// Update the selection
mCanvas.getSelectionManager().sync(mLastValidViewInfoRoot);
} else {
mInvisibleParents.clear();
}
}

/**
* Creates or updates the node proxy for this canvas view info.
* <p/>
//Synthetic comment -- @@ -189,14 +247,6 @@

if (key != null) {
mCanvas.getNodeFactory().create(vi);

            if (parentKey == null && vi.getParent() != null) {
                // This is an included view root
                if (mIncludedBounds == null) {
                    mIncludedBounds = new ArrayList<Rectangle>();
                }
                mIncludedBounds.add(vi.getAbsRect());
            }
}

for (CanvasViewInfo child : vi.getChildren()) {
//Synthetic comment -- @@ -411,7 +461,15 @@
if (r.contains(p.x, p.y)) {

// try to find a matching child first
            for (CanvasViewInfo child : canvasViewInfo.getChildren()) {
CanvasViewInfo v = findViewInfoAt_Recursive(p, child);
if (v != null) {
return v;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index c5dbec5..fa05c37 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gre;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IDragElement;
//Synthetic comment -- @@ -614,6 +616,7 @@
String ruleClassName;
ClassLoader classLoader;
if (realFqcn.startsWith("android.") || //$NON-NLS-1$
// FIXME: Remove this special case as soon as we pull
// the MapViewRule out of this code base and bundle it
// with the add ons
//Synthetic comment -- @@ -628,6 +631,10 @@
classLoader = RulesEngine.class.getClassLoader();
int dotIndex = realFqcn.lastIndexOf('.');
String baseName = realFqcn.substring(dotIndex+1);
ruleClassName = packageName + "." + //$NON-NLS-1$
baseName + "Rule"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index 657a7f8..547db8b 100644

//Synthetic comment -- @@ -17,15 +17,23 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;

import org.eclipse.swt.graphics.Rectangle;

import java.util.Arrays;

import junit.framework.TestCase;

//Synthetic comment -- @@ -66,7 +74,7 @@
ViewInfo child2 = new ViewInfo("Button", child2Node, 0, 20, 70, 25);
root.setChildren(Arrays.asList(child1, child2));

        CanvasViewInfo rootView = CanvasViewInfo.create(root);
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -103,7 +111,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", child21Node, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root);
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -140,7 +148,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", null, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root);
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -181,7 +189,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", null, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root);
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -207,6 +215,57 @@
assertEquals(0, includedView.getChildren().size());
}

public void testMerge() throws Exception {
// Test rendering of MULTIPLE included views or when there is no simple match
// between view info and ui element node children
//Synthetic comment -- @@ -222,7 +281,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", null, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root);
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -240,14 +299,249 @@
assertEquals(new Rectangle(10, 10, 49, 19), childView1.getSelectionRect());
assertSame(childView1.getUiViewNode(), child1Node);

        assertEquals("foo", includedView.getName());
assertSame(rootView, includedView.getParent());
        assertEquals(new Rectangle(10, 30, 70, 5), includedView.getAbsRect());
        assertEquals(new Rectangle(10, 30, 70, 5), includedView.getSelectionRect());
assertEquals(0, includedView.getChildren().size());
assertSame(multiChildNode, includedView.getUiViewNode());
}

/**
* Dumps out the given {@link ViewInfo} hierarchy to standard out.
* Useful during development.
//Synthetic comment -- @@ -261,11 +555,10 @@
System.out.println("Supports Embedded Layout=" + supportsEmbedding);
System.out.println("Rendering context=" + graphicalEditor.getIncludedWithin());
dump(root, 0);

}

/** Helper for {@link #dump(GraphicalEditorPart, ViewInfo)} */
    private static void dump(ViewInfo info, int depth) {
StringBuilder sb = new StringBuilder();
for (int i = 0; i < depth; i++) {
sb.append("    ");
//Synthetic comment -- @@ -285,7 +578,7 @@
sb.append(" ");
UiViewElementNode node = (UiViewElementNode) cookie;
sb.append("<");
            sb.append(node.getXmlNode().getNodeName());
sb.append("> ");
} else if (cookie != null) {
sb.append(" cookie=" + cookie);
//Synthetic comment -- @@ -297,4 +590,24 @@
dump(child, depth + 1);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java
//Synthetic comment -- index 08f191a..277089f 100755

//Synthetic comment -- @@ -48,7 +48,7 @@
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
        CanvasViewInfo cvi = CanvasViewInfo.create(lvi);

// Create a NodeProxy.
NodeProxy proxy = m.create(cvi);
//Synthetic comment -- @@ -95,7 +95,7 @@
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
        CanvasViewInfo cvi = CanvasViewInfo.create(lvi);

// NodeProxies are cached. Creating the same one twice returns the same proxy.
NodeProxy proxy1 = m.create(cvi);
//Synthetic comment -- @@ -107,7 +107,7 @@
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
        CanvasViewInfo cvi = CanvasViewInfo.create(lvi);

// NodeProxies are cached. Creating the same one twice returns the same proxy.
NodeProxy proxy1 = m.create(cvi);







