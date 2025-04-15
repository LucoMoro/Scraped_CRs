/*ADT GLE unit tests.

There isn't as much as would want to here.
I need to refactor the RulesEngine to make it more testable,
the major block is that it uses the static AdtPlugin to find
the groovy files path and that doesn't exist in the UT.

Change-Id:I96c0821252d5b777665ed68153fde63d140d0ee5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 12ac0b3..2fcff2b 100755

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.editors.layout.gscripts.DropFeedback;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IClientRulesEngine;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IDragElement;
//Synthetic comment -- @@ -81,7 +83,8 @@
* The project folder where the scripts are located.
* This is for both our unique ADT project folder and the user projects folders.
*/
    private static final String FD_GSCRIPTS = "gscripts";                       //$NON-NLS-1$
/**
* The extension we expect for the groovy scripts.
*/
//Synthetic comment -- @@ -98,6 +101,13 @@
private ProjectFolderListener mProjectFolderListener;


public RulesEngine(IProject project) {
mProject = project;
ClassLoader cl = getClass().getClassLoader();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactoryTest.java
//Synthetic comment -- index c5dd8a4..64b6a63 100755

//Synthetic comment -- @@ -45,7 +45,7 @@
}

public final void testCreateCanvasViewInfo() {
        ViewElementDescriptor ved = new ViewElementDescriptor("xml", "MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
MockLayoutViewInfo lvi = new MockLayoutViewInfo(uiv, "name", 10, 12, 110, 120);
CanvasViewInfo cvi = new CanvasViewInfo(lvi);
//Synthetic comment -- @@ -61,14 +61,14 @@
INode inode = proxy;
assertEquals(new Rect(10, 12, 110-10-1, 120-12-1), inode.getBounds());
assertTrue(Arrays.equals(new INode[0], inode.getChildren()));
        assertEquals("MyJavaClass", inode.getFqcn());
assertNull(inode.getParent());
assertSame(inode, inode.getRoot());

}

public final void testCreateUiViewElementNode() {
        ViewElementDescriptor ved = new ViewElementDescriptor("xml", "MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);

// Create a NodeProxy.
//Synthetic comment -- @@ -86,13 +86,13 @@
assertFalse(inode.getBounds().isValid());
// All the other properties should be set correctly.
assertTrue(Arrays.equals(new INode[0], inode.getChildren()));
        assertEquals("MyJavaClass", inode.getFqcn());
assertNull(inode.getParent());
assertSame(inode, inode.getRoot());
}

public final void testCreateDup() {
        ViewElementDescriptor ved = new ViewElementDescriptor("xml", "MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
MockLayoutViewInfo lvi = new MockLayoutViewInfo(uiv, "name", 10, 12, 110, 120);
CanvasViewInfo cvi = new CanvasViewInfo(lvi);
//Synthetic comment -- @@ -104,7 +104,7 @@
}

public final void testClear() {
        ViewElementDescriptor ved = new ViewElementDescriptor("xml", "MyJavaClass");
UiViewElementNode uiv = new UiViewElementNode(ved);
MockLayoutViewInfo lvi = new MockLayoutViewInfo(uiv, "name", 10, 12, 110, 120);
CanvasViewInfo cvi = new CanvasViewInfo(lvi);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngineTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngineTest.java
new file mode 100755
//Synthetic comment -- index 0000000..5ba0092

//Synthetic comment -- @@ -0,0 +1,68 @@







