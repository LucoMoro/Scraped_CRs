/*Temp

Change-Id:I76d6d616c62c3effe385cd7776af502ae47ac868*/
//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java
//Synthetic comment -- index 4dd990b..a257aa2 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.hierarchyvieweruilib.PixelPerfect;
import com.android.hierarchyvieweruilib.PixelPerfectLoupe;
import com.android.hierarchyvieweruilib.PixelPerfectTree;
import com.android.hierarchyvieweruilib.TreeView;
import com.android.hierarchyvieweruilib.TreeViewOverview;

//Synthetic comment -- @@ -85,7 +86,7 @@
shell2.open();
Shell shell3 = new Shell(display);
shell3.setLayout(new FillLayout());
        TreeViewOverview treeViewOverview = new TreeViewOverview(shell3);
shell3.open();
// ComponentRegistry.getDirector().loadViewTreeData(null);
while (!shell.isDisposed() && !shell2.isDisposed() && !shell3.isDisposed()) {
//Synthetic comment -- @@ -99,7 +100,7 @@
if (!shell2.isDisposed()) {
shell2.dispose();
}
        if (!shell3.isDisposed()) {
shell3.dispose();
}









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index c9244d2..af3f9f1 100644

//Synthetic comment -- @@ -440,7 +440,15 @@
connection = new DeviceConnection(window.getDevice());
connection.sendCommand("PROFILE " + window.encode() + " " + viewNode.toString());
BufferedReader in = connection.getInputStream();
            return loadProfileDataRecursive(viewNode, in);
} catch (IOException e) {
Log.e(TAG, "Unable to load profiling data for window " + window.getTitle()
+ " on device " + window.getDevice());
//Synthetic comment -- @@ -452,8 +460,7 @@
return false;
}

    private static boolean loadProfileDataRecursive(ViewNode node, BufferedReader in)
            throws IOException {
String line;
if ((line = in.readLine()) == null || line.equalsIgnoreCase("-1 -1 -1")
|| line.equalsIgnoreCase("DONE.")) {
//Synthetic comment -- @@ -463,6 +470,14 @@
node.measureTime = (Long.parseLong(data[0]) / 1000.0) / 1000.0;
node.layoutTime = (Long.parseLong(data[1]) / 1000.0) / 1000.0;
node.drawTime = (Long.parseLong(data[2]) / 1000.0) / 1000.0;
for (int i = 0; i < node.children.size(); i++) {
if (!loadProfileDataRecursive(node.children.get(i), in)) {
return false;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java
//Synthetic comment -- index 2e22b56..dd914cd 100644

//Synthetic comment -- @@ -22,8 +22,12 @@
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewNode {
public String id;

public String name;
//Synthetic comment -- @@ -82,6 +86,8 @@

public double drawTime;

public ViewNode(ViewNode parent, String data) {
this.parent = parent;
index = this.parent == null ? 0 : this.parent.children.size();
//Synthetic comment -- @@ -130,28 +136,61 @@

id = namedProperties.get("mID").value;

        left = getInt("mLeft", 0);
        top = getInt("mTop", 0);
        width = getInt("getWidth()", 0);
        height = getInt("getHeight()", 0);
        scrollX = getInt("mScrollX", 0);
        scrollY = getInt("mScrollY", 0);
        paddingLeft = getInt("mPaddingLeft", 0);
        paddingRight = getInt("mPaddingRight", 0);
        paddingTop = getInt("mPaddingTop", 0);
        paddingBottom = getInt("mPaddingBottom", 0);
        marginLeft = getInt("layout_leftMargin", Integer.MIN_VALUE);
        marginRight = getInt("layout_rightMargin", Integer.MIN_VALUE);
        marginTop = getInt("layout_topMargin", Integer.MIN_VALUE);
        marginBottom = getInt("layout_bottomMargin", Integer.MIN_VALUE);
        baseline = getInt("getBaseline()", 0);
        willNotDraw = getBoolean("willNotDraw()", false);
        hasFocus = getBoolean("hasFocus()", false);

hasMargins =
marginLeft != Integer.MIN_VALUE && marginRight != Integer.MIN_VALUE
&& marginTop != Integer.MIN_VALUE && marginBottom != Integer.MIN_VALUE;

}

private boolean getBoolean(String name, boolean defaultValue) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java
//Synthetic comment -- index 890b88c..f6279df 100644

//Synthetic comment -- @@ -33,6 +33,8 @@

private DrawableViewNode tree;

private Rectangle viewport;

private double zoom;
//Synthetic comment -- @@ -48,10 +50,18 @@
tree.placeRoot();
viewport = null;
zoom = 1;
}
notifyTreeChanged();
}

public void setViewport(Rectangle viewport) {
synchronized (this) {
this.viewport = viewport;
//Synthetic comment -- @@ -113,9 +123,17 @@
}
}

public static interface TreeChangeListener {
public void treeChanged();

public void viewportChanged();

public void zoomChanged();
//Synthetic comment -- @@ -142,6 +160,15 @@
}
}

public void notifyViewportChanged() {
TreeChangeListener[] listeners = getTreeChangeListenerList();
if (listeners != null) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java
//Synthetic comment -- index 4add510..49eb418 100644

//Synthetic comment -- @@ -42,7 +42,6 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class DeviceSelector extends Composite implements WindowChangeListener, SelectionListener {
//Synthetic comment -- @@ -153,10 +152,6 @@
treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

tree = treeViewer.getTree();
        TreeColumn col = new TreeColumn(tree, SWT.LEFT);
        col.setText("Name");
        col.pack();
        tree.setHeaderVisible(true);
tree.setLinesVisible(true);
tree.addSelectionListener(this);

//Synthetic comment -- @@ -170,7 +165,6 @@
treeViewer.setLabelProvider(contentProvider);
treeViewer.setInput(model);
model.addWindowChangeListener(this);

}

public void loadResources() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java
//Synthetic comment -- index 5336ec3..8165c0b 100644

//Synthetic comment -- @@ -165,10 +165,10 @@
int nodeTop = selectedNode.top;
int nodeWidth = selectedNode.width;
int nodeHeight = selectedNode.height;
                        int nodeMarginLeft = Math.abs(selectedNode.marginLeft);
                        int nodeMarginTop = Math.abs(selectedNode.marginTop);
                        int nodeMarginRight = Math.abs(selectedNode.marginRight);
                        int nodeMarginBottom = Math.abs(selectedNode.marginBottom);
int nodePadLeft = selectedNode.paddingLeft;
int nodePadTop = selectedNode.paddingTop;
int nodePadRight = selectedNode.paddingRight;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java
//Synthetic comment -- index 30a7b9e..2c30857 100644

//Synthetic comment -- @@ -136,10 +136,6 @@
treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

tree = treeViewer.getTree();
        TreeColumn col = new TreeColumn(tree, SWT.LEFT);
        col.setText("Name");
        col.pack();
        tree.setHeaderVisible(true);
tree.addSelectionListener(this);

loadResources();








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PropertyViewer.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PropertyViewer.java
new file mode 100644
//Synthetic comment -- index 0000000..50c0f00

//Synthetic comment -- @@ -0,0 +1,243 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeView.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeView.java
//Synthetic comment -- index 2bf4012..f5d7ae0 100644

//Synthetic comment -- @@ -46,6 +46,8 @@

private DrawableViewNode tree;

private Rectangle viewport;

private Transform transform;
//Synthetic comment -- @@ -112,10 +114,15 @@
}

public void mouseDown(MouseEvent e) {
synchronized (this) {
if (tree != null && viewport != null) {
Point pt = transformPoint(e.x, e.y);
draggedNode = tree.getSelected(pt.x, pt.y);
if (draggedNode == tree) {
draggedNode = null;
}
//Synthetic comment -- @@ -126,6 +133,9 @@
}
}
}
}

public void mouseUp(MouseEvent e) {
//Synthetic comment -- @@ -260,15 +270,21 @@
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
if (tree != null && viewport != null) {
e.gc.setTransform(transform);
paintRecursive(e.gc, tree);
}
}
}
};

    static void paintRecursive(GC gc, DrawableViewNode node) {
        gc.drawRectangle(node.left, (int) Math.round(node.top), DrawableViewNode.NODE_WIDTH,
                DrawableViewNode.NODE_HEIGHT);
int N = node.children.size();
for (int i = 0; i < N; i++) {
DrawableViewNode child = node.children.get(i);
//Synthetic comment -- @@ -290,6 +306,7 @@
public void treeChanged() {
synchronized (this) {
tree = model.getTree();
if (tree == null) {
viewport = null;
} else {
//Synthetic comment -- @@ -334,4 +351,11 @@
public void zoomChanged() {
viewportChanged();
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeViewOverview.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeViewOverview.java
//Synthetic comment -- index d299717..545c590 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
//Synthetic comment -- @@ -183,9 +184,9 @@
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
e.gc.fillRectangle((int) bounds.x, (int) bounds.y, (int) Math.ceil(bounds.width),
(int) Math.ceil(bounds.height));
                TreeView.paintRecursive(e.gc, tree);

                e.gc.setAlpha(100);
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
e.gc.fillRectangle((int) viewport.x, (int) viewport.y, (int) Math
.ceil(viewport.width), (int) Math.ceil(viewport.height));
//Synthetic comment -- @@ -199,6 +200,19 @@
}
};

private void doRedraw() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
//Synthetic comment -- @@ -270,4 +284,7 @@
viewportChanged();
}

}







