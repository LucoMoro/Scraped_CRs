/*Improve handling for GestureOverlayView

The layout editor has special handling for the "root" element, which
for example suppresses hover-highlighting for the root and a click
will initiate a marquee selection rather than a drag.

Some layouts wrap the layout inside a GestoreOverlayView. This does
not work well since the "real" root is its child, which will not be
treated as a proper root.

This changeset fixes this situation by handling this scenario such
that both the gesture overlay and its child is treated as the root.

Also make fix such that the icon in the outline also works for the
gesture overlay.

Change-Id:Ia96d2bc402958b44f9f82646ff0fc4552ab0cf14*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java
//Synthetic comment -- index b17f5df..316f020 100644

//Synthetic comment -- @@ -234,7 +234,14 @@
IconFactory factory = IconFactory.getInstance();
int color = hasChildren() ? IconFactory.COLOR_BLUE : IconFactory.COLOR_GREEN;
int shape = hasChildren() ? IconFactory.SHAPE_RECT : IconFactory.SHAPE_CIRCLE;
        String name = mXmlName;
        if (name.indexOf('.') != -1) {
            // If the user uses a fully qualified name, such as
            // "android.gesture.GestureOverlayView" in their XML, we need to look up
            // only by basename
            name = name.substring(name.lastIndexOf('.') + 1);
        }
        Image icon = factory.getIcon(name, color, shape);
return icon != null ? icon : AdtPlugin.getAndroidLogo();
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java
//Synthetic comment -- index e3faf4b..bfc8bb0 100644

//Synthetic comment -- @@ -207,7 +207,9 @@
IAndroidTarget target = currentSdk.getTarget(project);
if (target != null) {
AndroidTargetData data = currentSdk.getTargetData(target);
                if (data != null) {
                    builtInList = data.getLayoutDescriptors().getViewDescriptors();
                }
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 0585f4f..e08d4fd 100755

//Synthetic comment -- @@ -346,6 +346,17 @@
// The root element is the one whose GRAND parent
// is null (because the parent will be a -document-
// node).

        // Special case: a gesture overlay is sometimes added as the root, but for all intents
        // and purposes it is its layout child that is the real root so treat that one as the
        // root as well (such that the whole layout canvas does not highlight as part of hovers
        // etc)
        if (mParent != null
                && mParent.mName.endsWith("GestureOverlayView") //$NON-NLS-1$
                && mParent.isRoot()) {
            return true;
        }

return mUiViewNode == null || mUiViewNode.getUiParent() == null ||
mUiViewNode.getUiParent().getUiParent() == null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionItem.java
//Synthetic comment -- index 70a4098..85490c2 100644

//Synthetic comment -- @@ -78,7 +78,7 @@
* @return True if and only if this element is at the root of the hierarchy
*/
public boolean isRoot() {
        return mCanvasViewInfo.isRoot();
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index fa05c37..751def1 100755

//Synthetic comment -- @@ -252,7 +252,7 @@
return rule.getSelectionHint(parentNode, childNode);

} catch (Exception e) {
                AdtPlugin.log(e, "%s.getSelectionHint() failed: %s",
rule.getClass().getSimpleName(),
e.toString());
}
//Synthetic comment -- @@ -566,6 +566,13 @@
targetFqcn = fqcn;
}

            if (fqcn.indexOf('.') == -1) {
                // Deal with unknown descriptors; these lack the full qualified path and
                // elements in the layout without a package are taken to be in the
                // android.widget package.
                fqcn = "android.widget." + fqcn; //$NON-NLS-1$
            }

// Try to find a rule matching the "real" FQCN. If we find it, we're done.
// If not, the for loop will move to the parent descriptor.
rule = loadRule(fqcn, targetFqcn);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index 547db8b..9c1ac7d 100644

//Synthetic comment -- @@ -29,6 +29,7 @@

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
//Synthetic comment -- @@ -52,11 +53,11 @@
boolean hasChildren) {
String name = fqn.substring(fqn.lastIndexOf('.') + 1);
ViewElementDescriptor descriptor = createDesc(name, fqn, hasChildren);
        if (parent == null) {
            // All node hierarchies should be wrapped inside a document node at the root
            parent = new UiViewElementNode(createDesc("doc", "doc", true));
}
        return (UiViewElementNode) parent.appendNewUiChild(descriptor);
}

private static UiViewElementNode createNode(String fqn, boolean hasChildren) {
//Synthetic comment -- @@ -237,12 +238,16 @@
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getAbsRect());
assertEquals(new Rectangle(10, 10, 89, 89), rootView.getSelectionRect());
assertNull(rootView.getParent());
        assertTrue(rootView.isRoot());
assertSame(rootNode, rootView.getUiViewNode());
assertEquals(3, rootView.getChildren().size());

CanvasViewInfo childView1 = rootView.getChildren().get(0);
        assertFalse(childView1.isRoot());
CanvasViewInfo includedView1 = rootView.getChildren().get(1);
        assertFalse(includedView1.isRoot());
CanvasViewInfo includedView2 = rootView.getChildren().get(2);
        assertFalse(includedView1.isRoot());

assertEquals("CheckBox", childView1.getName());
assertSame(rootView, childView1.getParent());
//Synthetic comment -- @@ -542,6 +547,38 @@
assertEquals(new Rectangle(0, 20, 49, 19), bounds.get(1));
}

    public void testGestureOverlayView() throws Exception {
        // Test rendering of included views on layoutlib 5+ (e.g. has <include> tag)

        UiViewElementNode rootNode = createNode("android.gesture.GestureOverlayView", true);
        UiViewElementNode childNode = createNode(rootNode, "android.widget.LinearLayout", false);
        UiViewElementNode grandChildNode = createNode(childNode, "android.widget.Button", false);
        ViewInfo root = new ViewInfo("GestureOverlayView", rootNode, 10, 10, 100, 100);
        ViewInfo child = new ViewInfo("LinearLayout", childNode, 0, 0, 50, 20);
        root.setChildren(Collections.singletonList(child));
        ViewInfo grandChild = new ViewInfo("Button", grandChildNode, 0, 20, 70, 25);
        child.setChildren(Collections.singletonList(grandChild));
        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
        assertNotNull(rootView);
        assertEquals("GestureOverlayView", rootView.getName());

        assertTrue(rootView.isRoot());
        assertNull(rootView.getParent());
        assertSame(rootNode, rootView.getUiViewNode());
        assertEquals(1, rootView.getChildren().size());

        CanvasViewInfo childView = rootView.getChildren().get(0);
        assertEquals("LinearLayout", childView.getName());

        // This should also be a root for the special case that the root is
        assertTrue(childView.isRoot());

        assertEquals(1, childView.getChildren().size());
        CanvasViewInfo grandChildView = childView.getChildren().get(0);
        assertEquals("Button", grandChildView.getName());
        assertFalse(grandChildView.isRoot());
    }

/**
* Dumps out the given {@link ViewInfo} hierarchy to standard out.
* Useful during development.







