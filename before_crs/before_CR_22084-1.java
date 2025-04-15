/*Improve view cookie handling

The layout editor is passed a ViewInfo hierarchy by the layout
library. For older versions of the layout library, it can be handed
hierarchies where the view cookies (which point back to XML model
objects corresponding to the rendered views) that are missing or
ambiguous. For that reason, it has various algorithms to try to piece
things back together, and for example handle <merge> scenarios as best
it can.

This isn't necessary with layout lib version 5 and higher, since as of
version 5 these scenarios are handled on the layout lib side and the
layout editor is passed back special cookies like the MergeCookie to
properly handle the various scenarios.

This fix makes the layout editor look up the layoutlib version, and if
dealing with version 5 or higher, it takes a simpler path to build up
the hierarchy.

This is also necessary to deal with the latest version of layoutlib
which passes a new type of view cookie which the older algorithm
couldn't handle.

Change-Id:I98c3ba5d17ad9d639eb118e4709c0b6bbf815b0a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 2bb8901..2a70dd0 100755

//Synthetic comment -- @@ -526,19 +526,41 @@
* This method will build up a set of {@link CanvasViewInfo} that corresponds to the
* actual <b>selectable</b> views (which are also shown in the Outline).
*
* @param root the root {@link ViewInfo} to build from
* @return a {@link CanvasViewInfo} hierarchy
*/
    public static Pair<CanvasViewInfo,List<Rectangle>> create(ViewInfo root) {
        return new Builder().create(root);
}

/** Builder object which walks over a tree of {@link ViewInfo} objects and builds
* up a corresponding {@link CanvasViewInfo} hierarchy. */
private static class Builder {
        private Map<UiViewElementNode,List<CanvasViewInfo>> mMergeNodeMap;

        public Pair<CanvasViewInfo,List<Rectangle>> create(ViewInfo root) {
Object cookie = root.getCookie();
if (cookie == null) {
// Special case: If the root-most view does not have a view cookie,
//Synthetic comment -- @@ -717,10 +739,24 @@
parentX += viewInfo.getLeft();
parentY += viewInfo.getTop();

// See if we have any missing keys at this level
int missingNodes = 0;
int mergeNodes = 0;
            List<ViewInfo> children = viewInfo.getChildren();
for (ViewInfo child : children) {
// Only use children which have a ViewKey of the correct type.
// We can't interact with those when they have a null key or
//Synthetic comment -- @@ -751,7 +787,9 @@
// embedded_layout rendering, or we are including a view with a <merge>
// as the root element.

                String containerName = view.getUiViewNode().getDescriptor().getXmlLocalName();
if (containerName.equals(LayoutDescriptors.VIEW_INCLUDE)) {
// This is expected -- we don't WANT to get node keys for the content
// of an include since it's in a different file and should be treated
//Synthetic comment -- @@ -764,9 +802,11 @@
// that there are <merge> tags which are doing surprising things
// to the view hierarchy
LinkedList<UiViewElementNode> unused = new LinkedList<UiViewElementNode>();
                    for (UiElementNode child : view.getUiViewNode().getUiChildren()) {
                        if (child instanceof UiViewElementNode) {
                            unused.addLast((UiViewElementNode) child);
}
}
for (ViewInfo child : children) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index ad3292e..0a65865 100644

//Synthetic comment -- @@ -1053,7 +1053,7 @@
new StaticRenderSession(
Result.Status.SUCCESS.createResult(),
null /*rootViewInfo*/, null /*image*/),
                        null /*explodeNodes*/);
return;
}

//Synthetic comment -- @@ -1339,7 +1339,8 @@
explodeNodes, null /*custom background*/, false /*no decorations*/, logger,
mIncludedWithin, renderingMode);

        canvas.setSession(session, explodeNodes);

// update the UiElementNode with the layout info.
if (session != null && session.getResult().isSuccess() == false) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 321ad01..b54e3d1 100755

//Synthetic comment -- @@ -553,11 +553,12 @@
*            {@link #showInvisibleViews(boolean)}) where individual invisible nodes
*            are padded during certain interactions.
*/
    /* package */ void setSession(RenderSession session, Set<UiElementNode> explodedNodes) {
// disable any hover
clearHover();

        mViewHierarchy.setSession(session, explodedNodes);
if (mViewHierarchy.isValid() && session != null) {
Image image = mImageOverlay.setImage(session.getImage(), session.isAlphaChannelImage());









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index 8624cd3..10625bb 100644

//Synthetic comment -- @@ -136,7 +136,8 @@
*            {@link LayoutCanvas#showInvisibleViews}) where individual invisible
*            nodes are padded during certain interactions.
*/
    /* package */ void setSession(RenderSession session, Set<UiElementNode> explodedNodes) {
// replace the previous scene, so the previous scene must be disposed.
if (mSession != null) {
mSession.dispose();
//Synthetic comment -- @@ -157,7 +158,7 @@
// via drag & drop, etc.
if (hasMergeRoot()) {
ViewInfo mergeRoot = createMergeInfo(session);
                    infos = CanvasViewInfo.create(mergeRoot);
} else {
infos = null;
}
//Synthetic comment -- @@ -165,11 +166,12 @@
if (rootList.size() > 1 && hasMergeRoot()) {
ViewInfo mergeRoot = createMergeInfo(session);
mergeRoot.setChildren(rootList);
                    infos = CanvasViewInfo.create(mergeRoot);
} else {
ViewInfo root = rootList.get(0);
if (root != null) {
                        infos = CanvasViewInfo.create(root);
if (DUMP_INFO) {
dump(root, 0);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/RefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/RefactoringTest.java
//Synthetic comment -- index 18d56a1..661b553 100644

//Synthetic comment -- @@ -258,7 +258,7 @@

UiViewElementNode model = createModel(null, element);
ViewInfo info = createInfos(model, relativePath);
        CanvasViewInfo rootView = CanvasViewInfo.create(info).getFirst();
TestLayoutEditor layoutEditor = new TestLayoutEditor(file, structuredDocument, null);

TestContext testInfo = createTestContext();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index 494346a..d336b35 100644

//Synthetic comment -- @@ -18,7 +18,9 @@

import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.MergeCookie;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
//Synthetic comment -- @@ -65,6 +67,15 @@
}

public void testNormalCreate() throws Exception {
// Normal view hierarchy, no null keys anywhere

UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
//Synthetic comment -- @@ -75,7 +86,7 @@
ViewInfo child2 = new ViewInfo("Button", child2Node, 0, 20, 70, 25);
root.setChildren(Arrays.asList(child1, child2));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -100,6 +111,15 @@
}

public void testShowIn() throws Exception {
// Test rendering of "Show Included In" (included content rendered
// within an outer content that has null keys)

//Synthetic comment -- @@ -112,7 +132,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", child21Node, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -137,6 +157,8 @@
}

public void testIncludeTag() throws Exception {
// Test rendering of included views on layoutlib 5+ (e.g. has <include> tag)

UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
//Synthetic comment -- @@ -149,7 +171,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", null, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -176,9 +198,10 @@
}

public void testNoIncludeTag() throws Exception {
// Test rendering of included views on layoutlib 4- (e.g. no <include> tag cookie
        // in
        // view info)

UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
ViewInfo root = new ViewInfo("LinearLayout", rootNode, 10, 10, 100, 100);
//Synthetic comment -- @@ -190,7 +213,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", null, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -217,6 +240,8 @@
}

public void testMergeMatching() throws Exception {
// Test rendering of MULTIPLE included views or when there is no simple match
// between view info and ui element node children

//Synthetic comment -- @@ -232,7 +257,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", null, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -272,6 +297,8 @@
}

public void testMerge() throws Exception {
// Test rendering of MULTIPLE included views or when there is no simple match
// between view info and ui element node children

//Synthetic comment -- @@ -286,7 +313,7 @@
ViewInfo child21 = new ViewInfo("RadioButton", null, 0, 20, 70, 25);
child2.setChildren(Arrays.asList(child21));

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("LinearLayout", rootView.getName());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
//Synthetic comment -- @@ -313,6 +340,8 @@
}

public void testInsertMerge() throws Exception {
// Test rendering of MULTIPLE included views or when there is no simple match
// between view info and ui element node children

//Synthetic comment -- @@ -320,7 +349,7 @@
UiViewElementNode rootNode = createNode(mergeNode, "android.widget.Button", false);
ViewInfo root = new ViewInfo("Button", rootNode, 10, 10, 100, 100);

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("merge", rootView.getName());
assertSame(rootView.getUiViewNode(), mergeNode);
//Synthetic comment -- @@ -340,6 +369,8 @@
}

public void testUnmatchedMissing() throws Exception {
UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
ViewInfo root = new ViewInfo("LinearLayout", rootNode, 0, 0, 100, 100);
List<ViewInfo> children = new ArrayList<ViewInfo>();
//Synthetic comment -- @@ -387,7 +418,7 @@
}
root.setChildren(children);

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);

// dump(root, 0);
//Synthetic comment -- @@ -412,6 +443,8 @@
}

public void testMergeCookies() throws Exception {
UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
ViewInfo root = new ViewInfo("LinearLayout", rootNode, 0, 0, 100, 100);

//Synthetic comment -- @@ -431,7 +464,7 @@
}
root.setChildren(children);

        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);

assertEquals("LinearLayout", rootView.getName());
//Synthetic comment -- @@ -446,6 +479,8 @@
}

public void testMergeCookies2() throws Exception {
UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
ViewInfo root = new ViewInfo("LinearLayout", rootNode, 0, 0, 100, 100);

//Synthetic comment -- @@ -460,12 +495,13 @@
ArrayList<ViewInfo> children = new ArrayList<ViewInfo>();
for (int i = 0; i < 10; i++) {
Object cookie = (i % 2) == 0 ? cookie1 : cookie2;
            ViewInfo childView = new ViewInfo("childView" + i, cookie, 0, i * 20, 50, (i + 1) * 20);
children.add(childView);
}
root.setChildren(children);

        Pair<CanvasViewInfo, List<Rectangle>> result = CanvasViewInfo.create(root);
CanvasViewInfo rootView = result.getFirst();
List<Rectangle> bounds = result.getSecond();
assertNull(bounds);
//Synthetic comment -- @@ -495,6 +531,8 @@
}

public void testIncludeBounds() throws Exception {
UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
ViewInfo root = new ViewInfo("included", null, 0, 0, 100, 100);

//Synthetic comment -- @@ -509,12 +547,13 @@
ArrayList<ViewInfo> children = new ArrayList<ViewInfo>();
for (int i = 0; i < 10; i++) {
Object cookie = (i % 2) == 0 ? cookie1 : cookie2;
            ViewInfo childView = new ViewInfo("childView" + i, cookie, 0, i * 20, 50, (i + 1) * 20);
children.add(childView);
}
root.setChildren(children);

        Pair<CanvasViewInfo, List<Rectangle>> result = CanvasViewInfo.create(root);
CanvasViewInfo rootView = result.getFirst();
List<Rectangle> bounds = result.getSecond();
assertNotNull(rootView);
//Synthetic comment -- @@ -548,21 +587,27 @@
}

public void testIncludeBounds2() throws Exception {
UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
ViewInfo root = new ViewInfo("included", null, 0, 0, 100, 100);

UiViewElementNode node1 = createNode(rootNode, "childNode1", false);
UiViewElementNode node2 = createNode(rootNode, "childNode2", false);

        // Sets alternating merge cookies and checks whether the node sibling lists are
        // okay and merged correctly

ViewInfo childView1 = new ViewInfo("childView1", node1, 0, 20, 50, 40);
ViewInfo childView2 = new ViewInfo("childView2", node2, 0, 40, 50, 60);

root.setChildren(Arrays.asList(childView1, childView2));

        Pair<CanvasViewInfo, List<Rectangle>> result = CanvasViewInfo.create(root);
CanvasViewInfo rootView = result.getFirst();
List<Rectangle> bounds = result.getSecond();
assertNotNull(rootView);
//Synthetic comment -- @@ -580,6 +625,8 @@
}

public void testGestureOverlayView() throws Exception {
// Test rendering of included views on layoutlib 5+ (e.g. has <include> tag)

UiViewElementNode rootNode = createNode("android.gesture.GestureOverlayView", true);
//Synthetic comment -- @@ -590,7 +637,7 @@
root.setChildren(Collections.singletonList(child));
ViewInfo grandChild = new ViewInfo("Button", grandChildNode, 0, 20, 70, 25);
child.setChildren(Collections.singletonList(grandChild));
        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);
assertEquals("GestureOverlayView", rootView.getName());

//Synthetic comment -- @@ -611,6 +658,46 @@
assertFalse(grandChildView.isRoot());
}

/**
* Dumps out the given {@link ViewInfo} hierarchy to standard out.
* Useful during development.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManagerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManagerTest.java
//Synthetic comment -- index db18762..b29f9f3 100644

//Synthetic comment -- @@ -53,7 +53,7 @@
"android.widget.Button", false);
ViewInfo child2 = new ViewInfo("Button2", child2Node, 0, 20, 70, 25);
root.setChildren(Arrays.asList(child1, child2));
        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);

manager.selectMultiple(Arrays.asList(rootView, rootView.getChildren().get(0), rootView
//Synthetic comment -- @@ -104,7 +104,7 @@
"android.widget.Button", false);
ViewInfo child2 = new ViewInfo("Button2", child2Node, 0, 20, 70, 25);
root.setChildren(Arrays.asList(child1, child2));
        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
assertNotNull(rootView);

manager.selectMultiple(Arrays.asList(rootView.getChildren().get(0)));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java
//Synthetic comment -- index 277089f..9f670cc 100755

//Synthetic comment -- @@ -48,7 +48,7 @@
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
        CanvasViewInfo cvi = CanvasViewInfo.create(lvi).getFirst();

// Create a NodeProxy.
NodeProxy proxy = m.create(cvi);
//Synthetic comment -- @@ -95,7 +95,7 @@
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
        CanvasViewInfo cvi = CanvasViewInfo.create(lvi).getFirst();

// NodeProxies are cached. Creating the same one twice returns the same proxy.
NodeProxy proxy1 = m.create(cvi);
//Synthetic comment -- @@ -107,7 +107,7 @@
ViewElementDescriptor ved = new ViewElementDescriptor("xml", "com.example.MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
ViewInfo lvi = new ViewInfo("name", uiv, 10, 12, 110, 120);
        CanvasViewInfo cvi = CanvasViewInfo.create(lvi).getFirst();

// NodeProxies are cached. Creating the same one twice returns the same proxy.
NodeProxy proxy1 = m.create(cvi);








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java
//Synthetic comment -- index 2f79038..6620571 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
/** Ability to render at full size, as required by the layout, and unbound by the screen */
UNBOUND_RENDERING,
/** Ability to override the background of the rendering with transparency using
     * {@link SessionParams#setCustomBackgroundColor(int)} */
CUSTOM_BACKGROUND_COLOR,
/** Ability to call {@link RenderSession#render()} and {@link RenderSession#render(long)}. */
RENDER,







