/*Adding property and profile viewers + misc

Change-Id:I04b7e6045047d303b3434dc64662ffeb36c48c24*/




//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java
//Synthetic comment -- index 4dd990b..63881a2 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.hierarchyvieweruilib.PixelPerfect;
import com.android.hierarchyvieweruilib.PixelPerfectLoupe;
import com.android.hierarchyvieweruilib.PixelPerfectTree;
import com.android.hierarchyvieweruilib.ProfileViewer;
import com.android.hierarchyvieweruilib.PropertyViewer;
import com.android.hierarchyvieweruilib.TreeView;
import com.android.hierarchyvieweruilib.TreeViewOverview;

//Synthetic comment -- @@ -85,8 +87,12 @@
shell2.open();
Shell shell3 = new Shell(display);
shell3.setLayout(new FillLayout());
        PropertyViewer propertyViewer = new PropertyViewer(shell3);
shell3.open();
        Shell shell4 = new Shell(display);
        shell4.setLayout(new FillLayout());
        ProfileViewer profileViewer = new ProfileViewer(shell4);
        shell4.open();
// ComponentRegistry.getDirector().loadViewTreeData(null);
while (!shell.isDisposed() && !shell2.isDisposed() && !shell3.isDisposed()) {
if (!display.readAndDispatch()) {
//Synthetic comment -- @@ -99,7 +105,7 @@
if (!shell2.isDisposed()) {
shell2.dispose();
}
        if(!shell3.isDisposed()) {
shell3.dispose();
}









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index c9244d2..af3f9f1 100644

//Synthetic comment -- @@ -440,7 +440,15 @@
connection = new DeviceConnection(window.getDevice());
connection.sendCommand("PROFILE " + window.encode() + " " + viewNode.toString());
BufferedReader in = connection.getInputStream();
            int protocol;
            synchronized (viewServerInfo) {
                protocol = viewServerInfo.get(window.getDevice()).protocolVersion;
            }
            if (protocol < 3) {
                return loadProfileData(viewNode, in);
            } else {
                return loadProfileDataRecursive(viewNode, in);
            }
} catch (IOException e) {
Log.e(TAG, "Unable to load profiling data for window " + window.getTitle()
+ " on device " + window.getDevice());
//Synthetic comment -- @@ -452,8 +460,7 @@
return false;
}

    private static boolean loadProfileData(ViewNode node, BufferedReader in) throws IOException {
String line;
if ((line = in.readLine()) == null || line.equalsIgnoreCase("-1 -1 -1")
|| line.equalsIgnoreCase("DONE.")) {
//Synthetic comment -- @@ -463,6 +470,14 @@
node.measureTime = (Long.parseLong(data[0]) / 1000.0) / 1000.0;
node.layoutTime = (Long.parseLong(data[1]) / 1000.0) / 1000.0;
node.drawTime = (Long.parseLong(data[2]) / 1000.0) / 1000.0;
        return true;
    }

    private static boolean loadProfileDataRecursive(ViewNode node, BufferedReader in)
            throws IOException {
        if (!loadProfileData(node, in)) {
            return false;
        }
for (int i = 0; i < node.children.size(); i++) {
if (!loadProfileDataRecursive(node.children.get(i), in)) {
return false;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java
//Synthetic comment -- index 2e22b56..2dd9b61 100644

//Synthetic comment -- @@ -22,8 +22,12 @@
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ViewNode {
    public static final String MISCELLANIOUS = "miscellaneous";

public String id;

public String name;
//Synthetic comment -- @@ -82,6 +86,8 @@

public double drawTime;

    public Set<String> categories = new TreeSet<String>();

public ViewNode(ViewNode parent, String data) {
this.parent = parent;
index = this.parent == null ? 0 : this.parent.children.size();
//Synthetic comment -- @@ -130,28 +136,61 @@

id = namedProperties.get("mID").value;

        left = namedProperties.containsKey("mLeft") ?
                getInt("mLeft", 0) : getInt("layout:mLeft", 0);
        top = namedProperties.containsKey("mTop") ?
                getInt("mTop", 0) : getInt("layout:mTop", 0);
        width = namedProperties.containsKey("getWidth()") ?
                getInt("getWidth()", 0) : getInt("measurement:getWidth()", 0);
        height = namedProperties.containsKey("getHeight()") ?
                getInt("getHeight()", 0) : getInt("measurement:getHeight()", 0);
        scrollX = namedProperties.containsKey("mScrollX") ?
                getInt("mScrollX", 0) : getInt("scrolling:mScrollX", 0);
        scrollY = namedProperties.containsKey("mScrollY") ?
                getInt("mScrollY", 0) : getInt("scrolling:mScrollY", 0);
        paddingLeft = namedProperties.containsKey("mPaddingLeft") ?
                getInt("mPaddingLeft", 0) : getInt("padding:mPaddingLeft", 0);
        paddingRight = namedProperties.containsKey("mPaddingRight") ?
                getInt("mPaddingRight", 0) : getInt("padding:mPaddingRight", 0);
        paddingTop = namedProperties.containsKey("mPaddingTop") ?
                getInt("mPaddingTop", 0) : getInt("padding:mPaddingTop", 0);
        paddingBottom = namedProperties.containsKey("mPaddingBottom") ?
                getInt("mPaddingBottom", 0) : getInt("padding:mPaddingBottom", 0);
        marginLeft = namedProperties.containsKey("layout_leftMargin") ?
                getInt("layout_leftMargin", Integer.MIN_VALUE) :
                getInt("layout:leftMargin", Integer.MIN_VALUE);
        marginRight = namedProperties.containsKey("layout_rightMargin") ?
                getInt("layout_rightMargin", Integer.MIN_VALUE) :
                getInt("layout:rightMargin", Integer.MIN_VALUE);
        marginTop = namedProperties.containsKey("layout_topMargin") ?
                getInt("layout_topMargin", Integer.MIN_VALUE) :
                getInt("layout:topMargin", Integer.MIN_VALUE);
        marginBottom = namedProperties.containsKey("layout_bottomMargin") ?
                getInt("layout_bottomMargin", Integer.MIN_VALUE) :
                getInt("layout:bottomMargin", Integer.MIN_VALUE);
        baseline = namedProperties.containsKey("getBaseline()") ?
                getInt("getBaseline()", 0) :
                getInt("measurement:getBaseline()", 0);
        willNotDraw = namedProperties.containsKey("willNotDraw()") ?
                getBoolean("willNotDraw()", false) :
                getBoolean("drawing:willNotDraw()", false);
        hasFocus = namedProperties.containsKey("hasFocus()") ?
                getBoolean("hasFocus()", false) :
                getBoolean("focus:hasFocus()", false);

hasMargins =
marginLeft != Integer.MIN_VALUE && marginRight != Integer.MIN_VALUE
&& marginTop != Integer.MIN_VALUE && marginBottom != Integer.MIN_VALUE;

        for(String name : namedProperties.keySet()) {
            int index = name.indexOf(':');
            if(index != -1) {
                categories.add(name.substring(0, index));
            }
        }
        if(categories.size() != 0) {
            categories.add(MISCELLANIOUS);
        }
}

private boolean getBoolean(String name, boolean defaultValue) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java
//Synthetic comment -- index 890b88c..f6279df 100644

//Synthetic comment -- @@ -33,6 +33,8 @@

private DrawableViewNode tree;

    private DrawableViewNode selectedNode;

private Rectangle viewport;

private double zoom;
//Synthetic comment -- @@ -48,10 +50,18 @@
tree.placeRoot();
viewport = null;
zoom = 1;
            selectedNode = null;
}
notifyTreeChanged();
}

    public void setSelection(DrawableViewNode selectedNode) {
        synchronized (this) {
            this.selectedNode = selectedNode;
        }
        notifySelectionChanged();
    }

public void setViewport(Rectangle viewport) {
synchronized (this) {
this.viewport = viewport;
//Synthetic comment -- @@ -113,9 +123,17 @@
}
}

    public DrawableViewNode getSelection() {
        synchronized (this) {
            return selectedNode;
        }
    }

public static interface TreeChangeListener {
public void treeChanged();

        public void selectionChanged();

public void viewportChanged();

public void zoomChanged();
//Synthetic comment -- @@ -142,6 +160,15 @@
}
}

    public void notifySelectionChanged() {
        TreeChangeListener[] listeners = getTreeChangeListenerList();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].selectionChanged();
            }
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
import org.eclipse.swt.widgets.TreeItem;

public class DeviceSelector extends Composite implements WindowChangeListener, SelectionListener {
//Synthetic comment -- @@ -153,10 +152,6 @@
treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

tree = treeViewer.getTree();
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
                        int nodeMarginLeft = selectedNode.marginLeft;
                        int nodeMarginTop = selectedNode.marginTop;
                        int nodeMarginRight = selectedNode.marginRight;
                        int nodeMarginBottom = selectedNode.marginBottom;
int nodePadLeft = selectedNode.paddingLeft;
int nodePadTop = selectedNode.paddingTop;
int nodePadRight = selectedNode.paddingRight;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java
//Synthetic comment -- index 30a7b9e..2c30857 100644

//Synthetic comment -- @@ -136,10 +136,6 @@
treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

tree = treeViewer.getTree();
tree.addSelectionListener(this);

loadResources();








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/ProfileViewer.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/ProfileViewer.java
new file mode 100644
//Synthetic comment -- index 0000000..400318c

//Synthetic comment -- @@ -0,0 +1,179 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.hierarchyvieweruilib;

import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.TreeChangeListener;
import com.android.hierarchyviewerlib.scene.DrawableViewNode;
import com.android.hierarchyvieweruilib.util.TreeColumnResizer;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import java.text.DecimalFormat;

public class ProfileViewer extends Composite implements TreeChangeListener {
    private TreeViewModel model;

    private TreeViewer treeViewer;

    private Tree tree;

    private DrawableViewNode selectedNode;

    private class ContentProvider implements ITreeContentProvider, ITableLabelProvider {

        public Object[] getChildren(Object parentElement) {
            synchronized (ProfileViewer.this) {
                return new Object[0];
            }
        }

        public Object getParent(Object element) {
            synchronized (ProfileViewer.this) {
                return new Object[0];
            }
        }

        public boolean hasChildren(Object element) {
            synchronized (ProfileViewer.this) {
                return false;
            }
        }

        public Object[] getElements(Object inputElement) {
            synchronized (ProfileViewer.this) {
                if (selectedNode != null && selectedNode.viewNode.measureTime != -1) {
                    return new String[] {
                            "measure", "layout", "draw"
                    };
                }
                return new Object[0];
            }
        }

        public void dispose() {
            // pass
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // pass
        }

        public Image getColumnImage(Object element, int column) {
            return null;
        }

        public String getColumnText(Object element, int column) {
            synchronized (ProfileViewer.this) {
                if (selectedNode != null) {
                    if (column == 0) {
                        return (String) element;
                    } else if (column == 1) {
                        DecimalFormat formatter = new DecimalFormat("0.000");
                        if(((String)element).equals("measure")) {
                            return formatter.format(selectedNode.viewNode.measureTime);
                        } else if (((String) element).equals("layout")) {
                            return formatter.format(selectedNode.viewNode.layoutTime);
                        } else {
                            return formatter.format(selectedNode.viewNode.drawTime);
                        }
                    }
                }
                return "";
            }
        }

        public void addListener(ILabelProviderListener listener) {
            // pass
        }

        public boolean isLabelProperty(Object element, String property) {
            // pass
            return false;
        }

        public void removeListener(ILabelProviderListener listener) {
            // pass
        }
    }

    public ProfileViewer(Composite parent) {
        super(parent, SWT.NONE);
        setLayout(new FillLayout());
        treeViewer = new TreeViewer(this, SWT.NONE);

        tree = treeViewer.getTree();
        tree.setLinesVisible(true);
        tree.setHeaderVisible(true);

        TreeColumn operationColumn = new TreeColumn(tree, SWT.NONE);
        operationColumn.setText("Operation");
        TreeColumn durationColumn = new TreeColumn(tree, SWT.NONE);
        durationColumn.setText("Duration (ms)");

        model = ComponentRegistry.getTreeViewModel();
        ContentProvider contentProvider = new ContentProvider();
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(contentProvider);
        treeViewer.setInput(model);
        model.addTreeChangeListener(this);

        new TreeColumnResizer(this, operationColumn, durationColumn);
    }

    public void selectionChanged() {
        synchronized (this) {
            selectedNode = model.getSelection();
        }
        doRefresh();
    }

    public void treeChanged() {
        synchronized (this) {
            selectedNode = model.getSelection();
        }
        doRefresh();
    }

    public void viewportChanged() {
        // pass
    }

    public void zoomChanged() {
        // pass
    }

    private void doRefresh() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                treeViewer.refresh();
            }
        });
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PropertyViewer.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PropertyViewer.java
new file mode 100644
//Synthetic comment -- index 0000000..da4997b

//Synthetic comment -- @@ -0,0 +1,232 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.hierarchyvieweruilib;

import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.device.ViewNode.Property;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.TreeChangeListener;
import com.android.hierarchyviewerlib.scene.DrawableViewNode;
import com.android.hierarchyvieweruilib.util.TreeColumnResizer;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import java.util.ArrayList;

public class PropertyViewer extends Composite implements TreeChangeListener {
    private TreeViewModel model;

    private TreeViewer treeViewer;

    private Tree tree;

    private DrawableViewNode selectedNode;

    private class ContentProvider implements ITreeContentProvider, ITableLabelProvider {

        public Object[] getChildren(Object parentElement) {
            synchronized (PropertyViewer.this) {
                if (selectedNode != null && parentElement instanceof String) {
                    String category = (String) parentElement;
                    ArrayList<Property> returnValue = new ArrayList<Property>();
                    for (Property property : selectedNode.viewNode.properties) {
                        if (category.equals(ViewNode.MISCELLANIOUS)) {
                            if (property.name.indexOf(':') == -1) {
                                returnValue.add(property);
                            }
                        } else {
                            if (property.name.startsWith(((String) parentElement) + ":")) {
                                returnValue.add(property);
                            }
                        }
                    }
                    return returnValue.toArray(new Property[returnValue.size()]);
                }
                return new Object[0];
            }
        }

        public Object getParent(Object element) {
            synchronized (PropertyViewer.this) {
                if (selectedNode != null && element instanceof Property) {
                    if (selectedNode.viewNode.categories.size() == 0) {
                        return null;
                    }
                    String name = ((Property) element).name;
                    int index = name.indexOf(':');
                    if (index == -1) {
                        return ViewNode.MISCELLANIOUS;
                    }
                    return name.substring(0, index);
                }
                return null;
            }
        }

        public boolean hasChildren(Object element) {
            synchronized (PropertyViewer.this) {
                if (selectedNode != null && element instanceof String) {
                    String category = (String) element;
                    for (String name : selectedNode.viewNode.namedProperties.keySet()) {
                        if (category.equals(ViewNode.MISCELLANIOUS)) {
                            if (name.indexOf(':') == -1) {
                                return true;
                            }
                        } else {
                            if (name.startsWith(((String) element) + ":")) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        }

        public Object[] getElements(Object inputElement) {
            synchronized (PropertyViewer.this) {
                if (selectedNode != null && inputElement instanceof TreeViewModel) {
                    if (selectedNode.viewNode.categories.size() == 0) {
                        return selectedNode.viewNode.properties
                                .toArray(new Property[selectedNode.viewNode.properties.size()]);
                    } else {
                        return selectedNode.viewNode.categories
                                .toArray(new String[selectedNode.viewNode.categories.size()]);
                    }
                }
                return new Object[0];
            }
        }

        public void dispose() {
            // pass
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // pass
        }

        public Image getColumnImage(Object element, int column) {
            return null;
        }

        public String getColumnText(Object element, int column) {
            synchronized (PropertyViewer.this) {
                if (selectedNode != null) {
                    if (element instanceof String && column == 0) {
                        String category = (String) element;
                        return Character.toUpperCase(category.charAt(0)) + category.substring(1);
                    } else if (element instanceof Property) {
                        if (column == 0) {
                            String returnValue = ((Property) element).name;
                            int index = returnValue.indexOf(':');
                            if (index != -1) {
                                return returnValue.substring(index + 1);
                            }
                            return returnValue;
                        } else if (column == 1) {
                            return ((Property) element).value;
                        }
                    }
                }
                return "";
            }
        }

        public void addListener(ILabelProviderListener listener) {
            // pass
        }

        public boolean isLabelProperty(Object element, String property) {
            // pass
            return false;
        }

        public void removeListener(ILabelProviderListener listener) {
            // pass
        }
    }

    public PropertyViewer(Composite parent) {
        super(parent, SWT.NONE);
        setLayout(new FillLayout());
        treeViewer = new TreeViewer(this, SWT.NONE);

        tree = treeViewer.getTree();
        tree.setLinesVisible(true);
        tree.setHeaderVisible(true);

        TreeColumn propertyColumn = new TreeColumn(tree, SWT.NONE);
        propertyColumn.setText("Property");
        TreeColumn valueColumn = new TreeColumn(tree, SWT.NONE);
        valueColumn.setText("Value");

        model = ComponentRegistry.getTreeViewModel();
        ContentProvider contentProvider = new ContentProvider();
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(contentProvider);
        treeViewer.setInput(model);
        model.addTreeChangeListener(this);

        new TreeColumnResizer(this, propertyColumn, valueColumn);
    }


    public void selectionChanged() {
        synchronized (this) {
            selectedNode = model.getSelection();
        }
        doRefresh();
    }

    public void treeChanged() {
        synchronized (this) {
            selectedNode = model.getSelection();
        }
        doRefresh();
    }

    public void viewportChanged() {
        // pass
    }

    public void zoomChanged() {
        // pass
    }

    private void doRefresh() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                treeViewer.refresh();
            }
        });
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeView.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeView.java
//Synthetic comment -- index 2bf4012..f5d7ae0 100644

//Synthetic comment -- @@ -46,6 +46,8 @@

private DrawableViewNode tree;

    private DrawableViewNode selectedNode;

private Rectangle viewport;

private Transform transform;
//Synthetic comment -- @@ -112,10 +114,15 @@
}

public void mouseDown(MouseEvent e) {
            boolean selectionChanged = false;
synchronized (this) {
if (tree != null && viewport != null) {
Point pt = transformPoint(e.x, e.y);
draggedNode = tree.getSelected(pt.x, pt.y);
                    if (draggedNode != null && draggedNode != selectedNode) {
                        selectedNode = draggedNode;
                        selectionChanged = true;
                    }
if (draggedNode == tree) {
draggedNode = null;
}
//Synthetic comment -- @@ -126,6 +133,9 @@
}
}
}
            if (selectionChanged) {
                model.setSelection(selectedNode);
            }
}

public void mouseUp(MouseEvent e) {
//Synthetic comment -- @@ -260,15 +270,21 @@
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
if (tree != null && viewport != null) {
e.gc.setTransform(transform);
                    e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
paintRecursive(e.gc, tree);
}
}
}
};

    private void paintRecursive(GC gc, DrawableViewNode node) {
        if (selectedNode == node) {
            gc.fillRectangle(node.left, (int) Math.round(node.top), DrawableViewNode.NODE_WIDTH,
                    DrawableViewNode.NODE_HEIGHT);
        } else {
            gc.drawRectangle(node.left, (int) Math.round(node.top), DrawableViewNode.NODE_WIDTH,
                    DrawableViewNode.NODE_HEIGHT);
        }
int N = node.children.size();
for (int i = 0; i < N; i++) {
DrawableViewNode child = node.children.get(i);
//Synthetic comment -- @@ -290,6 +306,7 @@
public void treeChanged() {
synchronized (this) {
tree = model.getTree();
            selectedNode = model.getSelection();
if (tree == null) {
viewport = null;
} else {
//Synthetic comment -- @@ -334,4 +351,11 @@
public void zoomChanged() {
viewportChanged();
}

    public void selectionChanged() {
        synchronized (this) {
            selectedNode = model.getSelection();
        }
        doRedraw();
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeViewOverview.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeViewOverview.java
//Synthetic comment -- index d299717..feed1af 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
//Synthetic comment -- @@ -179,13 +180,12 @@
private PaintListener paintListener = new PaintListener() {
public void paintControl(PaintEvent e) {
if (tree != null && viewport != null) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
                e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
                e.gc.setTransform(transform);
                paintRecursive(e.gc, tree);

                e.gc.setAlpha(80);
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
e.gc.fillRectangle((int) viewport.x, (int) viewport.y, (int) Math
.ceil(viewport.width), (int) Math.ceil(viewport.height));
//Synthetic comment -- @@ -199,6 +199,19 @@
}
};

    private void paintRecursive(GC gc, DrawableViewNode node) {
        gc.drawRectangle(node.left, (int) Math.round(node.top), DrawableViewNode.NODE_WIDTH,
                DrawableViewNode.NODE_HEIGHT);
        int N = node.children.size();
        for (int i = 0; i < N; i++) {
            DrawableViewNode child = node.children.get(i);
            paintRecursive(gc, child);
            gc.drawLine(node.left + DrawableViewNode.NODE_WIDTH, (int) Math.round(node.top)
                    + DrawableViewNode.NODE_HEIGHT / 2, child.left, (int) Math.round(child.top)
                    + DrawableViewNode.NODE_HEIGHT / 2);
        }
    }

private void doRedraw() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
//Synthetic comment -- @@ -270,4 +283,7 @@
viewportChanged();
}

    public void selectionChanged() {
        // pass
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/util/TreeColumnResizer.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/util/TreeColumnResizer.java
new file mode 100644
//Synthetic comment -- index 0000000..09851c3

//Synthetic comment -- @@ -0,0 +1,92 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.hierarchyvieweruilib.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeColumn;

public class TreeColumnResizer {

    private TreeColumn column1;
    private TreeColumn column2;

    private Composite control;
    private int column1Width;
    private int column2Width;
    public TreeColumnResizer(Composite control, TreeColumn column1, TreeColumn column2) {
        this.control = control;
        this.column1 = column1;
        this.column2 = column2;
        control.addListener(SWT.Resize, resizeListener);
        column1.addListener(SWT.Resize, column1ResizeListener);
        column2.setResizable(false);
    }

    private Listener resizeListener = new Listener() {
        public void handleEvent(Event e) {
            if (column1Width == 0 && column2Width == 0) {
                column1Width = (control.getBounds().width - 18) / 2;
                column2Width = (control.getBounds().width - 18) / 2;
            } else {
                int dif = control.getBounds().width - 18 - (column1Width + column2Width);
                int columnDif = Math.abs(column1Width - column2Width);
                int mainColumnChange = Math.min(Math.abs(dif), columnDif);
                int left = Math.max(0, Math.abs(dif) - columnDif);
                if (dif < 0) {
                    if (column1Width > column2Width) {
                        column1Width -= mainColumnChange;
                    } else {
                        column2Width -= mainColumnChange;
                    }
                    column1Width -= left / 2;
                    column2Width -= left - left / 2;
                } else {
                    if (column1Width > column2Width) {
                        column2Width += mainColumnChange;
                    } else {
                        column1Width += mainColumnChange;
                    }
                    column1Width += left / 2;
                    column2Width += left - left / 2;
                }
            }
            column1.removeListener(SWT.Resize, column1ResizeListener);
            column1.setWidth(column1Width);
            column2.setWidth(column2Width);
            column1.addListener(SWT.Resize, column1ResizeListener);
        }
    };

    private Listener column1ResizeListener = new Listener() {
        public void handleEvent(Event e) {
            int widthDif = column1Width - column1.getWidth();
            column1Width -= widthDif;
            column2Width += widthDif;
            if (column2Width < 0) {
                column1Width += column2Width;
                column2Width = 0;
                column1.removeListener(SWT.Resize, this);
                column1.setWidth(column1Width);
                column1.addListener(SWT.Resize, this);
            }
            column2.setWidth(column2Width);
        }
    };
}







