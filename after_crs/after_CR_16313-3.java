/*Adding the new tree view.

Change-Id:I490f475fadaec5625352a49524ec8cb3a6e9b7d4*/




//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index 6acbdff..c3538dc 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.TreeViewModel;

public class HierarchyViewerApplication {
public static void main(String[] args) {
//Synthetic comment -- @@ -27,6 +28,7 @@
ComponentRegistry.setDirector(director);
ComponentRegistry.setDeviceSelectionModel(new DeviceSelectionModel());
ComponentRegistry.setPixelPerfectModel(new PixelPerfectModel());
        ComponentRegistry.setTreeViewModel(new TreeViewModel());
director.initDebugBridge();
director.startListenForDevices();
director.populateDeviceSelectionModel();








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java
//Synthetic comment -- index 5321ce7..8d77410 100644

//Synthetic comment -- @@ -43,6 +43,9 @@
@Override
public String getAdbLocation() {
String hvParentLocation = System.getProperty("com.android.hierarchyviewer.bindir");
        // TODO REMOVE THIS.
        hvParentLocation = "/usr/local/google/android-ext/out/host/linux-x86/bin";
        System.out.println(hvParentLocation);
if (hvParentLocation != null && hvParentLocation.length() != 0) {
return hvParentLocation + File.separator + SdkConstants.FN_ADB;
}
//Synthetic comment -- @@ -58,7 +61,9 @@
public void executeInBackground(final Runnable task) {
executor.execute(new Runnable() {
public void run() {
                System.out.println("STARTING TASK");
task.run();
                System.out.println("ENDING TASK");
}
});
}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java
//Synthetic comment -- index 485fbb5..f1965b2 100644

//Synthetic comment -- @@ -17,15 +17,21 @@
package com.android.hierarchyviewer;

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyvieweruilib.DeviceSelector;
import com.android.hierarchyvieweruilib.PixelPerfect;
import com.android.hierarchyvieweruilib.PixelPerfectLoupe;
import com.android.hierarchyvieweruilib.PixelPerfectTree;
import com.android.hierarchyvieweruilib.TreeView;
import com.android.hierarchyvieweruilib.TreeViewOverview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
//Synthetic comment -- @@ -41,23 +47,22 @@
shell.open();
Shell shell2 = new Shell(display);
shell2.setLayout(new FillLayout());
        /*


        
        PixelPerfectTree pixelPerfectTree = new PixelPerfectTree(shell2);
        Composite overview = new Composite(shell2, SWT.NONE);
        overview.setLayout(new GridLayout());
        PixelPerfect pixelPerfect = new PixelPerfect(overview);
        pixelPerfect.setLayoutData(new GridData(GridData.FILL_BOTH));
        final Slider slider = new Slider(overview, SWT.HORIZONTAL);
        slider.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
slider.setMinimum(2);
slider.setMaximum(25);
slider.setSelection(8);
slider.setThumb(1);
        final PixelPerfectLoupe pixelPerfectLoupe = new PixelPerfectLoupe(shell2);
slider.addSelectionListener(new SelectionListener() {
private int oldZoom = 8;

//Synthetic comment -- @@ -68,15 +73,22 @@
public void widgetSelected(SelectionEvent arg0) {
int newZoom = slider.getSelection();
if (newZoom != oldZoom) {
                    ComponentRegistry.getPixelPerfectModel().setZoom(newZoom);
oldZoom = newZoom;
}
}

});
        shell2.open();
        */
        TreeView treeView = new TreeView(shell2);
        shell2.open();
        Shell shell3 = new Shell(display);
        shell3.setLayout(new FillLayout());
        TreeViewOverview treeViewOverview = new TreeViewOverview(shell3);
        shell3.open();
        // ComponentRegistry.getDirector().loadViewTreeData(null);
        while (!shell.isDisposed() && !shell2.isDisposed()) {
if (!display.readAndDispatch()) {
display.sleep();
}
//Synthetic comment -- @@ -87,19 +99,9 @@
if (!shell2.isDisposed()) {
shell2.dispose();
}

// NO LONGER TESTING STUFF.

ImageLoader.dispose();
display.dispose();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ComponentRegistry.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ComponentRegistry.java
//Synthetic comment -- index 528c35c..a50478e 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;
import com.android.hierarchyviewerlib.models.TreeViewModel;

/**
* This is the central point for getting access to the various parts of the
//Synthetic comment -- @@ -32,6 +33,8 @@

private static PixelPerfectModel pixelPerfectModel;

    private static TreeViewModel treeViewModel;

public static HierarchyViewerDirector getDirector() {
return director;
}
//Synthetic comment -- @@ -55,4 +58,12 @@
public static PixelPerfectModel getPixelPerfectModel() {
return pixelPerfectModel;
}

    public static void setTreeViewModel(TreeViewModel treeViewModel) {
        ComponentRegistry.treeViewModel = treeViewModel;
    }

    public static TreeViewModel getTreeViewModel() {
        return treeViewModel;
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index c21da49..b63fba8 100644

//Synthetic comment -- @@ -109,6 +109,9 @@
DeviceBridge.removeDeviceForward(device);
DeviceBridge.removeViewServerInfo(device);
ComponentRegistry.getDeviceSelectionModel().removeDevice(device);
        if (ComponentRegistry.getPixelPerfectModel().getDevice() == device) {
            ComponentRegistry.getPixelPerfectModel().setData(null, null, null);
        }
}

public void deviceChanged(IDevice device, int changeMask) {
//Synthetic comment -- @@ -192,4 +195,17 @@
}
});
}

    public void loadViewTreeData(final Window window) {
        executeInBackground(new Runnable() {
            public void run() {

                ViewNode viewNode = DeviceBridge.loadWindowData(window);
                if (viewNode != null) {
                    DeviceBridge.loadProfileData(window, viewNode);
                    ComponentRegistry.getTreeViewModel().setData(window, viewNode);
                }
            }
        });
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 001f98b..34e7c03 100644

//Synthetic comment -- @@ -430,4 +430,41 @@
}
return null;
}

    public static boolean loadProfileData(Window window, ViewNode viewNode) {
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(window.getDevice());
            connection.sendCommand("PROFILE " + window.encode() + " " + viewNode.toString());
            BufferedReader in = connection.getInputStream();
            return loadProfileDataRecursive(viewNode, in);
        } catch (IOException e) {
            Log.e(TAG, "Unable to load profiling data for window " + window.getTitle()
                    + " on device " + window.getDevice());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return false;
    }

    private static boolean loadProfileDataRecursive(ViewNode node, BufferedReader in)
            throws IOException {
        String line;
        if ((line = in.readLine()) == null || line.equalsIgnoreCase("-1 -1 -1")
                || line.equalsIgnoreCase("DONE.")) {
            return false;
        }
        String[] data = line.split(" ");
        node.measureTime = (Long.parseLong(data[0]) / 1000.0) / 1000.0;
        node.layoutTime = (Long.parseLong(data[1]) / 1000.0) / 1000.0;
        node.drawTime = (Long.parseLong(data[2]) / 1000.0) / 1000.0;
        for (int i = 0; i < node.children.size(); i++) {
            if (!loadProfileDataRecursive(node.children.get(i), in)) {
                return false;
            }
        }
        return true;
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java
//Synthetic comment -- index 00453ee..2e22b56 100644

//Synthetic comment -- @@ -76,6 +76,12 @@

public int index;

    public double measureTime;

    public double layoutTime;

    public double drawTime;

public ViewNode(ViewNode parent, String data) {
this.parent = parent;
index = this.parent == null ? 0 : this.parent.children.size();
//Synthetic comment -- @@ -88,6 +94,10 @@
delimIndex = data.indexOf(' ');
hashCode = data.substring(0, delimIndex);
loadProperties(data.substring(delimIndex + 1).trim());

        measureTime = -1;
        layoutTime = -1;
        drawTime = -1;
}

private void loadProperties(String data) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java
//Synthetic comment -- index 6963b25..4f19368 100644

//Synthetic comment -- @@ -24,6 +24,12 @@

public class PixelPerfectModel {

    public static final int MIN_ZOOM = 2;

    public static final int MAX_ZOOM = 24;

    private static final int DEFAULT_ZOOM = 8;

public static class Point {
public int x;

//Synthetic comment -- @@ -45,6 +51,8 @@

private ViewNode selected;

    private int zoom;

private final ArrayList<ImageChangeListener> imageChangeListeners =
new ArrayList<ImageChangeListener>();

//Synthetic comment -- @@ -53,8 +61,13 @@
this.device = device;
this.image = image;
this.viewNode = viewNode;
            if (image != null) {
                this.crosshairLocation = new Point(image.width / 2, image.height / 2);
            } else {
                this.crosshairLocation = null;
            }
this.selected = null;
            zoom = DEFAULT_ZOOM;
}
notifyImageLoaded();
}
//Synthetic comment -- @@ -82,6 +95,19 @@
notifyFocusChanged();
}

    public void setZoom(int newZoom) {
        synchronized (this) {
            if (newZoom < MIN_ZOOM) {
                newZoom = MIN_ZOOM;
            }
            if (newZoom > MAX_ZOOM) {
                newZoom = MAX_ZOOM;
            }
            zoom = newZoom;
        }
        notifyZoomChanged();
    }

public ViewNode getViewNode() {
synchronized (this) {
return viewNode;
//Synthetic comment -- @@ -112,6 +138,12 @@
}
}

    public int getZoom() {
        synchronized (this) {
            return zoom;
        }
    }

public static interface ImageChangeListener {
public void imageLoaded();

//Synthetic comment -- @@ -122,6 +154,8 @@
public void selectionChanged();

public void focusChanged();

        public void zoomChanged();
}

private ImageChangeListener[] getImageChangeListenerList() {
//Synthetic comment -- @@ -182,6 +216,15 @@
}
}

    public void notifyZoomChanged() {
        ImageChangeListener[] listeners = getImageChangeListenerList();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].zoomChanged();
            }
        }
    }

public void addImageChangeListener(ImageChangeListener listener) {
synchronized (imageChangeListeners) {
imageChangeListeners.add(listener);








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java
new file mode 100644
//Synthetic comment -- index 0000000..890b88c

//Synthetic comment -- @@ -0,0 +1,174 @@
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

package com.android.hierarchyviewerlib.models;

import com.android.hierarchyviewerlib.device.ViewNode;
import com.android.hierarchyviewerlib.device.Window;
import com.android.hierarchyviewerlib.scene.DrawableViewNode;
import com.android.hierarchyviewerlib.scene.DrawableViewNode.Point;
import com.android.hierarchyviewerlib.scene.DrawableViewNode.Rectangle;

import java.util.ArrayList;

public class TreeViewModel {
    public static final double MAX_ZOOM = 2;

    public static final double MIN_ZOOM = 0.2;

    private Window window;

    private DrawableViewNode tree;

    private Rectangle viewport;

    private double zoom;

    private final ArrayList<TreeChangeListener> treeChangeListeners =
            new ArrayList<TreeChangeListener>();

    public void setData(Window window, ViewNode viewNode) {
        synchronized (this) {
            this.window = window;
            tree = new DrawableViewNode(viewNode);
            tree.setLeft();
            tree.placeRoot();
            viewport = null;
            zoom = 1;
        }
        notifyTreeChanged();
    }

    public void setViewport(Rectangle viewport) {
        synchronized (this) {
            this.viewport = viewport;
        }
        notifyViewportChanged();
    }

    public void setZoom(double newZoom) {
        Point zoomPoint = null;
        synchronized (this) {
            if (tree != null && viewport != null) {
                zoomPoint =
                        new Point(viewport.x + viewport.width / 2, viewport.y + viewport.height / 2);
            }
        }
        zoomOnPoint(newZoom, zoomPoint);
    }

    public void zoomOnPoint(double newZoom, Point zoomPoint) {
        synchronized (this) {
            if (tree != null && this.viewport != null) {
                if (newZoom < MIN_ZOOM) {
                    newZoom = MIN_ZOOM;
                }
                if (newZoom > MAX_ZOOM) {
                    newZoom = MAX_ZOOM;
                }
                viewport.x = zoomPoint.x - (zoomPoint.x - viewport.x) * zoom / newZoom;
                viewport.y = zoomPoint.y - (zoomPoint.y - viewport.y) * zoom / newZoom;
                viewport.width = viewport.width * zoom / newZoom;
                viewport.height = viewport.height * zoom / newZoom;
                zoom = newZoom;
            }
        }
        notifyZoomChanged();
    }

    public DrawableViewNode getTree() {
        synchronized (this) {
            return tree;
        }
    }

    public Window getWindow() {
        synchronized (this) {
            return window;
        }
    }

    public Rectangle getViewport() {
        synchronized (this) {
            return viewport;
        }
    }

    public double getZoom() {
        synchronized (this) {
            return zoom;
        }
    }

    public static interface TreeChangeListener {
        public void treeChanged();

        public void viewportChanged();

        public void zoomChanged();
    }

    private TreeChangeListener[] getTreeChangeListenerList() {
        TreeChangeListener[] listeners = null;
        synchronized (treeChangeListeners) {
            if (treeChangeListeners.size() == 0) {
                return null;
            }
            listeners =
                    treeChangeListeners.toArray(new TreeChangeListener[treeChangeListeners.size()]);
        }
        return listeners;
    }

    public void notifyTreeChanged() {
        TreeChangeListener[] listeners = getTreeChangeListenerList();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].treeChanged();
            }
        }
    }

    public void notifyViewportChanged() {
        TreeChangeListener[] listeners = getTreeChangeListenerList();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].viewportChanged();
            }
        }
    }

    public void notifyZoomChanged() {
        TreeChangeListener[] listeners = getTreeChangeListenerList();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].zoomChanged();
            }
        }
    }

    public void addTreeChangeListener(TreeChangeListener listener) {
        synchronized (treeChangeListeners) {
            treeChangeListeners.add(listener);
        }
    }

    public void removeTreeChangeListener(TreeChangeListener listener) {
        synchronized (treeChangeListeners) {
            treeChangeListeners.remove(listener);
        }
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/scene/DrawableViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/scene/DrawableViewNode.java
new file mode 100644
//Synthetic comment -- index 0000000..4ff8983

//Synthetic comment -- @@ -0,0 +1,234 @@

package com.android.hierarchyviewerlib.scene;

import com.android.hierarchyviewerlib.device.ViewNode;

import java.util.ArrayList;

public class DrawableViewNode {
    public ViewNode viewNode;

    public final ArrayList<DrawableViewNode> children = new ArrayList<DrawableViewNode>();

    public final static int NODE_HEIGHT = 70;

    public final static int NODE_WIDTH = 100;

    public final static int LEAF_NODE_SPACING = 5;

    public final static int NON_LEAF_NODE_SPACING = 10;

    public final static int PARENT_CHILD_SPACING = 40;

    public final static int PADDING = 30;

    public int treeHeight;

    public int treeWidth;

    public boolean leaf;

    public DrawableViewNode parent;

    public int left;

    public double top;

    public int topSpacing;

    public int bottomSpacing;

    public static class Rectangle {
        public double x, y, width, height;

        public Rectangle() {

        }

        public Rectangle(Rectangle other) {
            this.x = other.x;
            this.y = other.y;
            this.width = other.width;
            this.height = other.height;
        }

        public Rectangle(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "{" + x + ", " + y + ", " + width + ", " + height + "}";
        }

    }

    public static class Point {
        public double x, y;

        public Point() {
        }

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public Rectangle bounds = new Rectangle();

    public DrawableViewNode(ViewNode viewNode) {
        this.viewNode = viewNode;
        if (viewNode.children.size() == 0) {
            treeHeight = NODE_HEIGHT;
            treeWidth = NODE_WIDTH;
            leaf = true;
        } else {
            leaf = false;
            int N = viewNode.children.size();
            treeHeight = 0;
            treeWidth = 0;
            for (int i = 0; i < N; i++) {
                DrawableViewNode child = new DrawableViewNode(viewNode.children.get(i));
                children.add(child);
                child.parent = this;
                treeHeight += child.treeHeight;
                treeWidth = Math.max(treeWidth, child.treeWidth);
                if (i != 0) {
                    DrawableViewNode prevChild = children.get(i - 1);
                    if (prevChild.leaf && child.leaf) {
                        treeHeight += LEAF_NODE_SPACING;
                        prevChild.bottomSpacing = LEAF_NODE_SPACING;
                        child.topSpacing = LEAF_NODE_SPACING;
                    } else {
                        treeHeight += NON_LEAF_NODE_SPACING;
                        prevChild.bottomSpacing = NON_LEAF_NODE_SPACING;
                        child.topSpacing = NON_LEAF_NODE_SPACING;
                    }
                }
            }
            treeWidth += NODE_WIDTH + PARENT_CHILD_SPACING;
        }
    }

    public void setLeft() {
        if (parent == null) {
            left = PADDING;
            bounds.x = 0;
            bounds.width = treeWidth + 2 * PADDING;
        } else {
            left = parent.left + NODE_WIDTH + PARENT_CHILD_SPACING;
        }
        int N = children.size();
        for (int i = 0; i < N; i++) {
            children.get(i).setLeft();
        }
    }

    public void placeRoot() {
        top = PADDING + (treeHeight - NODE_HEIGHT) / 2.0;
        double currentTop = PADDING;
        int N = children.size();
        for (int i = 0; i < N; i++) {
            DrawableViewNode child = children.get(i);
            child.place(currentTop, top - currentTop);
            currentTop += child.treeHeight + child.bottomSpacing;
        }
        bounds.y = 0;
        bounds.height = treeHeight + 2 * PADDING;
    }

    private void place(double treeTop, double rootDistance) {
        if (treeHeight <= rootDistance) {
            top = treeTop + treeHeight - NODE_HEIGHT;
        } else if (rootDistance <= -NODE_HEIGHT) {
            top = treeTop;
        } else {
            if (children.size() == 0) {
                top = treeTop;
            } else {
                top =
                        rootDistance + treeTop - NODE_HEIGHT + (2.0 * NODE_HEIGHT)
                                / (treeHeight + NODE_HEIGHT) * (treeHeight - rootDistance);
            }
        }
        int N = children.size();
        double currentTop = treeTop;
        for (int i = 0; i < N; i++) {
            DrawableViewNode child = children.get(i);
            child.place(currentTop, rootDistance);
            currentTop += child.treeHeight + child.bottomSpacing;
            rootDistance -= child.treeHeight + child.bottomSpacing;
        }
    }

    public DrawableViewNode getSelected(double x, double y) {
        if (x >= left && x < left + NODE_WIDTH && y >= top && y <= top + NODE_HEIGHT) {
            return this;
        }
        int N = children.size();
        for (int i = 0; i < N; i++) {
            DrawableViewNode selected = children.get(i).getSelected(x, y);
            if (selected != null) {
                return selected;
            }
        }
        return null;
    }

    /*
     * Moves the node the specified distance up.
     */
    public void move(double distance) {
        top -= distance;

        // Get the root
        DrawableViewNode root = this;
        while (root.parent != null) {
            root = root.parent;
        }

        // Figure out the new tree top.
        double treeTop;
        if (top + NODE_HEIGHT <= root.top) {
            treeTop = top + NODE_HEIGHT - treeHeight;
        } else if (top >= root.top + NODE_HEIGHT) {
            treeTop = top;
        } else {
            if (leaf) {
                treeTop = top;
            } else {
                double distanceRatio = 1 - (root.top + NODE_HEIGHT - top) / (2.0 * NODE_HEIGHT);
                treeTop = root.top - treeHeight + distanceRatio * (treeHeight + NODE_HEIGHT);
            }
        }
        // Go up the tree and figure out the tree top.
        DrawableViewNode node = this;
        while (node.parent != null) {
            int index = node.viewNode.index;
            for (int i = 0; i < index; i++) {
                DrawableViewNode sibling = node.parent.children.get(i);
                treeTop -= sibling.treeHeight + sibling.bottomSpacing;
            }
            node = node.parent;
        }

        // Update the bounds.
        root.bounds.y = Math.min(root.top - PADDING, treeTop - PADDING);
        root.bounds.height =
                Math.max(treeTop + root.treeHeight + PADDING, root.top + NODE_HEIGHT + PADDING)
                        - root.bounds.y;
        // Place all the children of the root
        double currentTop = treeTop;
        int N = root.children.size();
        for (int i = 0; i < N; i++) {
            DrawableViewNode child = root.children.get(i);
            child.place(currentTop, root.top - currentTop);
            currentTop += child.treeHeight + child.bottomSpacing;
        }

    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java
//Synthetic comment -- index 60d8e6d..187a3fa 100644

//Synthetic comment -- @@ -36,13 +36,14 @@
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class DeviceSelector extends Composite implements WindowChangeListener, SelectionListener {
private TreeViewer treeViewer;

private Tree tree;
//Synthetic comment -- @@ -144,7 +145,9 @@
}

public DeviceSelector(Composite parent) {
        super(parent, SWT.NONE);
        setLayout(new FillLayout());
        treeViewer = new TreeViewer(this, SWT.SINGLE);
treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

tree = treeViewer.getTree();
//Synthetic comment -- @@ -189,13 +192,16 @@
.getSystemColor(SWT.COLOR_BLUE));
}

    @Override
    public void dispose() {
        super.dispose();
model.removeWindowChangeListener(this);
boldFont.dispose();
}

    @Override
    public boolean setFocus() {
        return tree.setFocus();
}

public void deviceConnected(final IDevice device) {
//Synthetic comment -- @@ -246,6 +252,8 @@
Object selection = ((TreeItem) e.item).getData();
if (selection instanceof IDevice) {
ComponentRegistry.getDirector().loadPixelPerfectData((IDevice) selection);
        } else if (selection instanceof Window) {
            ComponentRegistry.getDirector().loadViewTreeData((Window) selection);
}
}









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java
//Synthetic comment -- index e4345e1..3972830 100644

//Synthetic comment -- @@ -39,13 +39,11 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfect extends ScrolledComposite implements ImageChangeListener {
private Canvas canvas;

private PixelPerfectModel model;

private Image image;

private Color crosshairColor;
//Synthetic comment -- @@ -65,11 +63,11 @@
private ViewNode selectedNode;

public PixelPerfect(Composite parent) {
        super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        canvas = new Canvas(this, SWT.NONE);
        setContent(canvas);
        setExpandHorizontal(true);
        setExpandVertical(true);
model = ComponentRegistry.getPixelPerfectModel();
model.addImageChangeListener(this);

//Synthetic comment -- @@ -83,7 +81,9 @@
paddingColor = new Color(Display.getDefault(), new RGB(0, 0, 255));
}

    @Override
    public void dispose() {
        super.dispose();
if (image != null) {
image.dispose();
}
//Synthetic comment -- @@ -92,8 +92,9 @@
paddingColor.dispose();
}

    @Override
    public boolean setFocus() {
        return canvas.setFocus();
}

private MouseListener mouseListener = new MouseListener() {
//Synthetic comment -- @@ -220,7 +221,7 @@
}
};

    private void doRedraw() {
Display.getDefault().asyncExec(new Runnable() {
public void run() {
canvas.redraw();
//Synthetic comment -- @@ -252,7 +253,7 @@
}
Display.getDefault().asyncExec(new Runnable() {
public void run() {
                setMinSize(width, height);
}
});
}
//Synthetic comment -- @@ -263,28 +264,28 @@
crosshairLocation = model.getCrosshairLocation();
selectedNode = model.getSelected();
}
        doRedraw();
}

public void imageChanged() {
synchronized (this) {
loadImage();
}
        doRedraw();
}

public void crosshairMoved() {
synchronized (this) {
crosshairLocation = model.getCrosshairLocation();
}
        doRedraw();
}

public void selectionChanged() {
synchronized (this) {
selectedNode = model.getSelected();
}
        doRedraw();
}

public void focusChanged() {
//Synthetic comment -- @@ -292,6 +293,10 @@
loadImage();
selectedNode = model.getSelected();
}
        doRedraw();
    }

    public void zoomChanged() {
        // pass
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectLoupe.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectLoupe.java
//Synthetic comment -- index eb1df88..62bb8c7 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
//Synthetic comment -- @@ -39,9 +40,7 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfectLoupe extends Canvas implements ImageChangeListener {
private PixelPerfectModel model;

private Image image;
//Synthetic comment -- @@ -65,20 +64,22 @@
private int canvasHeight;

public PixelPerfectLoupe(Composite parent) {
        super(parent, SWT.NONE);
model = ComponentRegistry.getPixelPerfectModel();
model.addImageChangeListener(this);

        addPaintListener(paintListener);
        addMouseListener(mouseListener);
        addMouseWheelListener(mouseWheelListener);

crosshairColor = new Color(Display.getDefault(), new RGB(255, 94, 254));

transform = new Transform(Display.getDefault());
}

    @Override
    public void dispose() {
        super.dispose();
if (image != null) {
image.dispose();
}
//Synthetic comment -- @@ -89,23 +90,6 @@
}
}

private MouseListener mouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
//Synthetic comment -- @@ -113,13 +97,31 @@
}

public void mouseDown(MouseEvent e) {
handleMouseEvent(e);
}

        public void mouseUp(MouseEvent e) {
            //
        }

    };

    private MouseWheelListener mouseWheelListener = new MouseWheelListener() {
        public void mouseScrolled(MouseEvent e) {
            int newZoom = -1;
            synchronized (this) {
                if (image != null && crosshairLocation != null) {
                    if (e.count > 0) {
                        newZoom = zoom + 1;
                    } else {
                        newZoom = zoom - 1;
                    }
                }
            }
            if (newZoom != -1) {
                model.setZoom(newZoom);
            }
        }
};

private void handleMouseEvent(MouseEvent e) {
//Synthetic comment -- @@ -129,8 +131,8 @@
if (image == null) {
return;
}
            int zoomedX = -crosshairLocation.x * zoom - zoom / 2 + getBounds().width / 2;
            int zoomedY = -crosshairLocation.y * zoom - zoom / 2 + getBounds().height / 2;
int x = (e.x - zoomedX) / zoom;
int y = (e.y - zoomedY) / zoom;
if (x >= 0 && x < width && y >= 0 && y < height) {
//Synthetic comment -- @@ -147,12 +149,10 @@
public void paintControl(PaintEvent e) {
synchronized (this) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
                e.gc.fillRectangle(0, 0, getSize().x, getSize().y);
if (image != null && crosshairLocation != null) {
                    int zoomedX = -crosshairLocation.x * zoom - zoom / 2 + getBounds().width / 2;
                    int zoomedY = -crosshairLocation.y * zoom - zoom / 2 + getBounds().height / 2;
transform.translate(zoomedX, zoomedY);
transform.scale(zoom, zoom);
e.gc.setInterpolation(SWT.NONE);
//Synthetic comment -- @@ -164,13 +164,12 @@
// If the size of the canvas has changed, we need to make
// another grid.
if (grid != null
                            && (canvasWidth != getBounds().width || canvasHeight != getBounds().height)) {
grid.dispose();
grid = null;
}
                    canvasWidth = getBounds().width;
                    canvasHeight = getBounds().height;
if (grid == null) {
// Make a transparent image;
ImageData imageData =
//Synthetic comment -- @@ -208,10 +207,10 @@
}
};

    private void doRedraw() {
Display.getDefault().asyncExec(new Runnable() {
public void run() {
                redraw();
}
});
}
//Synthetic comment -- @@ -243,22 +242,23 @@
synchronized (this) {
loadImage();
crosshairLocation = model.getCrosshairLocation();
            zoom = model.getZoom();
}
        doRedraw();
}

public void imageChanged() {
synchronized (this) {
loadImage();
}
        doRedraw();
}

public void crosshairMoved() {
synchronized (this) {
crosshairLocation = model.getCrosshairLocation();
}
        doRedraw();
}

public void selectionChanged() {
//Synthetic comment -- @@ -268,4 +268,17 @@
public void focusChanged() {
imageChanged();
}

    public void zoomChanged() {
        synchronized (this) {
            if (grid != null) {
                // To notify that the zoom level has changed, we get rid of the
                // grid.
                grid.dispose();
                grid = null;
                zoom = model.getZoom();
            }
        }
        doRedraw();
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java
//Synthetic comment -- index a85c8eb..9f25a33 100644

//Synthetic comment -- @@ -25,12 +25,14 @@
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
//Synthetic comment -- @@ -38,7 +40,7 @@

import java.util.List;

public class PixelPerfectTree extends Composite implements ImageChangeListener, SelectionListener {

private TreeViewer treeViewer;

//Synthetic comment -- @@ -126,7 +128,9 @@
}

public PixelPerfectTree(Composite parent) {
        super(parent, SWT.NONE);
        setLayout(new FillLayout());
        treeViewer = new TreeViewer(this, SWT.SINGLE);
treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

tree = treeViewer.getTree();
//Synthetic comment -- @@ -154,13 +158,16 @@
folderImage = loader.loadImage("folder.png", Display.getDefault());
}

    @Override
    public void dispose() {
        super.dispose();
fileImage.dispose();
folderImage.dispose();
}

    @Override
    public boolean setFocus() {
        return tree.setFocus();
}

public void imageLoaded() {
//Synthetic comment -- @@ -193,7 +200,16 @@
}

public void widgetSelected(SelectionEvent e) {
        // To combat phantom selection...
        if (((TreeSelection) treeViewer.getSelection()).isEmpty()) {
            model.setSelected(null);
        } else {
            model.setSelected((ViewNode) e.item.getData());
        }
    }

    public void zoomChanged() {
        // pass
}

}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeView.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeView.java
new file mode 100644
//Synthetic comment -- index 0000000..a29bdb3

//Synthetic comment -- @@ -0,0 +1,333 @@
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
import com.android.hierarchyviewerlib.scene.DrawableViewNode.Point;
import com.android.hierarchyviewerlib.scene.DrawableViewNode.Rectangle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class TreeView extends Canvas implements TreeChangeListener {

    private TreeViewModel model;

    private DrawableViewNode tree;

    private Rectangle viewport;

    private Transform transform;

    private Transform inverse;

    private double zoom;

    private Point lastPoint;

    private DrawableViewNode draggedNode;

    public TreeView(Composite parent) {
        super(parent, SWT.NONE);

        model = ComponentRegistry.getTreeViewModel();
        model.addTreeChangeListener(this);

        addPaintListener(paintListener);
        addMouseListener(mouseListener);
        addMouseMoveListener(mouseMoveListener);
        addMouseWheelListener(mouseWheelListener);
        addListener(SWT.Resize, resizeListener);

        transform = new Transform(Display.getDefault());
        inverse = new Transform(Display.getDefault());
    }

    @Override
    public void dispose() {
        super.dispose();
        transform.dispose();
        inverse.dispose();
    }

    private Listener resizeListener = new Listener() {
        public void handleEvent(Event e) {
            synchronized (this) {
                if (tree != null && viewport != null) {

                    // I don't know what the best behaviour is... This seems
                    // like a good idea.
                    Point viewCenter =
                            new Point(viewport.x + viewport.width / 2, viewport.y + viewport.height
                                    / 2);
                    viewport.width = getBounds().width / zoom;
                    viewport.height = getBounds().height / zoom;
                    viewport.x = viewCenter.x - viewport.width / 2;
                    viewport.y = viewCenter.y - viewport.height / 2;
                }
            }
            if (viewport != null) {
                model.setViewport(viewport);
            }
        }
    };

    private MouseListener mouseListener = new MouseListener() {

        public void mouseDoubleClick(MouseEvent e) {
            // pass
        }

        public void mouseDown(MouseEvent e) {
            synchronized (this) {
                if (tree != null && viewport != null) {
                    Point pt = transformPoint(e.x, e.y);
                    draggedNode = tree.getSelected(pt.x, pt.y);
                    if (draggedNode == tree) {
                        draggedNode = null;
                    }
                    if (draggedNode != null) {
                        lastPoint = pt;
                    } else {
                        lastPoint = new Point(e.x, e.y);
                    }
                }
            }
        }

        public void mouseUp(MouseEvent e) {
            boolean redraw = false;
            boolean viewportChanged = false;
            synchronized (this) {
                if (tree != null && viewport != null && lastPoint != null) {
                    if (draggedNode == null) {
                        handleMouseDrag(new Point(e.x, e.y));
                        viewportChanged = true;
                    } else {
                        handleMouseDrag(transformPoint(e.x, e.y));
                    }
                    lastPoint = null;
                    draggedNode = null;
                    redraw = true;
                }
            }
            if (viewportChanged) {
                model.setViewport(viewport);
            } else if (redraw) {
                model.removeTreeChangeListener(TreeView.this);
                model.notifyTreeChanged();
                model.addTreeChangeListener(TreeView.this);
                doRedraw();
            }
        }

    };

    private MouseMoveListener mouseMoveListener = new MouseMoveListener() {
        public void mouseMove(MouseEvent e) {
            boolean redraw = false;
            boolean viewportChanged = false;
            synchronized (this) {
                if (tree != null && viewport != null && lastPoint != null) {
                    if (draggedNode == null) {
                        handleMouseDrag(new Point(e.x, e.y));
                        viewportChanged = true;
                    } else {
                        handleMouseDrag(transformPoint(e.x, e.y));
                    }
                    redraw = true;
                }
            }
            if (viewportChanged) {
                model.setViewport(viewport);
            } else if (redraw) {
                model.removeTreeChangeListener(TreeView.this);
                model.notifyTreeChanged();
                model.addTreeChangeListener(TreeView.this);
                doRedraw();
            }
        }
    };

    private void handleMouseDrag(Point pt) {
        if (draggedNode != null) {
            draggedNode.move(lastPoint.y - pt.y);
            lastPoint = pt;
            return;
        }
        double xDif = (lastPoint.x - pt.x) / zoom;
        double yDif = (lastPoint.y - pt.y) / zoom;

        if (viewport.width > tree.bounds.width) {
            if (xDif < 0 && viewport.x + viewport.width > tree.bounds.x + tree.bounds.width) {
                viewport.x =
                        Math.max(viewport.x + xDif, tree.bounds.x + tree.bounds.width
                                - viewport.width);
            } else if (xDif > 0 && viewport.x < tree.bounds.x) {
                viewport.x = Math.min(viewport.x + xDif, tree.bounds.x);
            }
        } else {
            if (xDif < 0 && viewport.x > tree.bounds.x) {
                viewport.x = Math.max(viewport.x + xDif, tree.bounds.x);
            } else if (xDif > 0 && viewport.x + viewport.width < tree.bounds.x + tree.bounds.width) {
                viewport.x =
                        Math.min(viewport.x + xDif, tree.bounds.x + tree.bounds.width
                                - viewport.width);
            }
        }
        if (viewport.height > tree.bounds.height) {
            if (yDif < 0 && viewport.y + viewport.height > tree.bounds.y + tree.bounds.height) {
                viewport.y =
                        Math.max(viewport.y + yDif, tree.bounds.y + tree.bounds.height
                                - viewport.height);
            } else if (yDif > 0 && viewport.y < tree.bounds.y) {
                viewport.y = Math.min(viewport.y + yDif, tree.bounds.y);
            }
        } else {
            if (yDif < 0 && viewport.y > tree.bounds.y) {
                viewport.y = Math.max(viewport.y + yDif, tree.bounds.y);
            } else if (yDif > 0
                    && viewport.y + viewport.height < tree.bounds.y + tree.bounds.height) {
                viewport.y =
                        Math.min(viewport.y + yDif, tree.bounds.y + tree.bounds.height
                                - viewport.height);
            }
        }
        lastPoint = pt;
    }

    private Point transformPoint(double x, double y) {
        float[] pt = {
                (float) x, (float) y
        };
        inverse.transform(pt);
        return new Point(pt[0], pt[1]);
    }

    private MouseWheelListener mouseWheelListener = new MouseWheelListener() {

        public void mouseScrolled(MouseEvent e) {
            Point zoomPoint = null;
            synchronized (this) {
                if (tree != null && viewport != null) {
                    zoom += Math.ceil(e.count / 3.0) * 0.1;
                    zoomPoint = transformPoint(e.x, e.y);
                }
            }
            if (zoomPoint != null) {
                model.zoomOnPoint(zoom, zoomPoint);
            }
        }
    };

    private PaintListener paintListener = new PaintListener() {
        public void paintControl(PaintEvent e) {
            synchronized (this) {
                e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
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
            paintRecursive(gc, child);
            gc.drawLine(node.left + DrawableViewNode.NODE_WIDTH, (int) Math.round(node.top)
                    + DrawableViewNode.NODE_HEIGHT / 2, child.left, (int) Math.round(child.top)
                    + DrawableViewNode.NODE_HEIGHT / 2);
        }
    }

    private void doRedraw() {
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                redraw();
            }
        });
    }

    public void treeChanged() {
        synchronized (this) {
            tree = model.getTree();
            if (tree == null) {
                viewport = null;
            } else {
                Display.getDefault().syncExec(new Runnable() {
                    public void run() {
                        viewport =
                                new Rectangle((tree.bounds.width - getBounds().width) / 2,
                                        (tree.bounds.height - getBounds().height) / 2,
                                        getBounds().width, getBounds().height);
                    }
                });
            }
        }
        if (viewport != null) {
            model.setViewport(viewport);
        }
    }

    private void setTransform() {
        if (viewport != null && tree != null) {
            // Set the transform.
            transform.identity();
            inverse.identity();

            transform.scale((float) zoom, (float) zoom);
            inverse.scale((float) zoom, (float) zoom);
            transform.translate((float) -viewport.x, (float) -viewport.y);
            inverse.translate((float) -viewport.x, (float) -viewport.y);
            inverse.invert();
        }
    }

    public void viewportChanged() {
        synchronized (this) {
            viewport = model.getViewport();
            zoom = model.getZoom();
            setTransform();
        }
        doRedraw();
    }

    public void zoomChanged() {
        viewportChanged();
    }
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeViewOverview.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeViewOverview.java
new file mode 100644
//Synthetic comment -- index 0000000..f9e4cef

//Synthetic comment -- @@ -0,0 +1,254 @@

package com.android.hierarchyvieweruilib;

import com.android.hierarchyviewerlib.ComponentRegistry;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.TreeChangeListener;
import com.android.hierarchyviewerlib.scene.DrawableViewNode;
import com.android.hierarchyviewerlib.scene.DrawableViewNode.Point;
import com.android.hierarchyviewerlib.scene.DrawableViewNode.Rectangle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class TreeViewOverview extends Canvas implements TreeChangeListener {

    private TreeViewModel model;

    private DrawableViewNode tree;

    private Rectangle viewport;

    private Transform transform;

    private Transform inverse;

    private Rectangle bounds = new Rectangle();

    private double scale;

    private boolean dragging = false;

    public TreeViewOverview(Composite parent) {
        super(parent, SWT.NONE);

        model = ComponentRegistry.getTreeViewModel();
        model.addTreeChangeListener(this);

        addPaintListener(paintListener);
        addMouseListener(mouseListener);
        addMouseMoveListener(mouseMoveListener);
        addListener(SWT.Resize, resizeListener);

        transform = new Transform(Display.getDefault());
        inverse = new Transform(Display.getDefault());
    }

    @Override
    public void dispose() {
        super.dispose();
        transform.dispose();
        inverse.dispose();
    }

    private MouseListener mouseListener = new MouseListener() {

        public void mouseDoubleClick(MouseEvent e) {
            // pass
        }

        public void mouseDown(MouseEvent e) {
            boolean redraw = false;
            synchronized (this) {
                if (tree != null && viewport != null) {
                    dragging = true;
                    redraw = true;
                    handleMouseEvent(transformPoint(e.x, e.y));
                }
            }
            if (redraw) {
                model.removeTreeChangeListener(TreeViewOverview.this);
                model.setViewport(viewport);
                model.addTreeChangeListener(TreeViewOverview.this);
                doRedraw();
            }
        }

        public void mouseUp(MouseEvent e) {
            boolean redraw = false;
            synchronized (this) {
                if (tree != null && viewport != null) {
                    dragging = false;
                    redraw = true;
                    handleMouseEvent(transformPoint(e.x, e.y));
                    setBounds();
                    setTransform();
                }
            }
            if (redraw) {
                model.removeTreeChangeListener(TreeViewOverview.this);
                model.setViewport(viewport);
                model.addTreeChangeListener(TreeViewOverview.this);
                doRedraw();
            }
        }

    };

    private MouseMoveListener mouseMoveListener = new MouseMoveListener() {
        public void mouseMove(MouseEvent e) {
            boolean moved = false;
            synchronized (this) {
                if (dragging) {
                    moved = true;
                    handleMouseEvent(transformPoint(e.x, e.y));
                }
            }
            if (moved) {
                model.removeTreeChangeListener(TreeViewOverview.this);
                model.setViewport(viewport);
                model.addTreeChangeListener(TreeViewOverview.this);
                doRedraw();
            }
        }
    };

    private void handleMouseEvent(Point pt) {
        viewport.x = pt.x - viewport.width / 2;
        viewport.y = pt.y - viewport.height / 2;
        if (viewport.x < bounds.x) {
            viewport.x = bounds.x;
        }
        if (viewport.y < bounds.y) {
            viewport.y = bounds.y;
        }
        if (viewport.x + viewport.width > bounds.x + bounds.width) {
            viewport.x = bounds.x + bounds.width - viewport.width;
        }
        if (viewport.y + viewport.height > bounds.y + bounds.height) {
            viewport.y = bounds.y + bounds.height - viewport.height;
        }
    }

    private Point transformPoint(double x, double y) {
        float[] pt = {
                (float) x, (float) y
        };
        inverse.transform(pt);
        return new Point(pt[0], pt[1]);
    }

    private Listener resizeListener = new Listener() {
        public void handleEvent(Event arg0) {
            synchronized (this) {
                setTransform();
            }
            doRedraw();
        }
    };

    private PaintListener paintListener = new PaintListener() {
        public void paintControl(PaintEvent e) {
            if (tree != null && viewport != null) {
                e.gc.setTransform(transform);
                e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
                e.gc.fillRectangle((int) bounds.x, (int) bounds.y, (int) Math.ceil(bounds.width),
                        (int) Math.ceil(bounds.height));
                TreeView.paintRecursive(e.gc, tree);

                e.gc.setAlpha(100);
                e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
                e.gc.fillRectangle((int) viewport.x, (int) viewport.y, (int) Math
                        .ceil(viewport.width), (int) Math.ceil(viewport.height));

                e.gc.setAlpha(255);
                e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
                e.gc.setLineWidth((int) Math.ceil(2 / scale));
                e.gc.drawRectangle((int) viewport.x, (int) viewport.y, (int) Math
                        .ceil(viewport.width), (int) Math.ceil(viewport.height));
            }
        }
    };

    private void doRedraw() {
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                redraw();
            }
        });
    }

    public void treeChanged() {
        synchronized (this) {
            tree = model.getTree();
            setBounds();
            setTransform();
        }
        doRedraw();
    }

    private void setBounds() {
        if (viewport != null && tree != null) {
            bounds.x = Math.min(viewport.x, tree.bounds.x);
            bounds.y = Math.min(viewport.y, tree.bounds.y);
            bounds.width =
                    Math.max(viewport.x + viewport.width, tree.bounds.x + tree.bounds.width)
                            - bounds.x;
            bounds.height =
                    Math.max(viewport.y + viewport.height, tree.bounds.y + tree.bounds.height)
                            - bounds.y;
        }
    }

    private void setTransform() {
        if (viewport != null && tree != null) {

            transform.identity();
            inverse.identity();
            final Point size = new Point();
            Display.getDefault().syncExec(new Runnable() {
                public void run() {
                    size.x = getBounds().width;
                    size.y = getBounds().height;
                }
            });
            scale = Math.min(size.x / bounds.width, size.y / bounds.height);
            transform.scale((float) scale, (float) scale);
            inverse.scale((float) scale, (float) scale);
            transform.translate((float) -bounds.x, (float) -bounds.y);
            inverse.translate((float) -bounds.x, (float) -bounds.y);
            if (size.x / bounds.width < size.y / bounds.height) {
                transform.translate(0, (float) (size.y / scale - bounds.height) / 2);
                inverse.translate(0, (float) (size.y / scale - bounds.height) / 2);
            } else {
                transform.translate((float) (size.x / scale - bounds.width) / 2, 0);
                inverse.translate((float) (size.x / scale - bounds.width) / 2, 0);
            }
            inverse.invert();
        }
    }

    public void viewportChanged() {
        synchronized (this) {
            viewport = model.getViewport();
            setBounds();
            setTransform();
        }
        doRedraw();
    }

    public void zoomChanged() {
        viewportChanged();
    }

}







