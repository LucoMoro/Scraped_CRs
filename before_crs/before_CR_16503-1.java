/*ADT-Tests: Unit test for GRE NodeFactory.

Change-Id:I33039b0be05f0eca51f1fd5c6f616d01f46a59b7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/INode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/INode.java
//Synthetic comment -- index c944f6c..360639c 100755

//Synthetic comment -- @@ -60,9 +60,11 @@
// ---- Hierarchy handling ----

/**
     * Returns the root element of the view hierarchy. This may be this node if this is
     * the root element. It can also be null when the current node is not yet or no
     * longer attached to the hierarchy.
*/
INode getRoot();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactory.java
//Synthetic comment -- index d6251c8..2bb01da 100755

//Synthetic comment -- @@ -46,7 +46,8 @@

/**
* Returns an {@link INode} proxy based on a given {@link UiViewElementNode} that
     * is not yet part of the canvas, typically those created
*/
public NodeProxy create(UiViewElementNode uiNode) {
return create(uiNode, null /*bounds*/);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java
//Synthetic comment -- index 6d9c98e..16b50b4 100755

//Synthetic comment -- @@ -121,6 +121,9 @@
p = p.getUiNextSibling();
}

if (p instanceof UiViewElementNode) {
return mFactory.create((UiViewElementNode) p);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java
new file mode 100755
//Synthetic comment -- index 0000000..c5dd8a4

//Synthetic comment -- @@ -0,0 +1,177 @@







