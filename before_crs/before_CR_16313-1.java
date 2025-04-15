/*Adding the new tree view.

Change-Id:I490f475fadaec5625352a49524ec8cb3a6e9b7d4*/
//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index 6acbdff..c3538dc 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;

public class HierarchyViewerApplication {
public static void main(String[] args) {
//Synthetic comment -- @@ -27,6 +28,7 @@
ComponentRegistry.setDirector(director);
ComponentRegistry.setDeviceSelectionModel(new DeviceSelectionModel());
ComponentRegistry.setPixelPerfectModel(new PixelPerfectModel());
director.initDebugBridge();
director.startListenForDevices();
director.populateDeviceSelectionModel();








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java
//Synthetic comment -- index 5321ce7..8d77410 100644

//Synthetic comment -- @@ -43,6 +43,9 @@
@Override
public String getAdbLocation() {
String hvParentLocation = System.getProperty("com.android.hierarchyviewer.bindir");
if (hvParentLocation != null && hvParentLocation.length() != 0) {
return hvParentLocation + File.separator + SdkConstants.FN_ADB;
}
//Synthetic comment -- @@ -58,7 +61,9 @@
public void executeInBackground(final Runnable task) {
executor.execute(new Runnable() {
public void run() {
task.run();
}
});
}








//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/UIThread.java
//Synthetic comment -- index 485fbb5..1b9270b 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.hierarchyviewer;

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyvieweruilib.DeviceSelector;
import com.android.hierarchyvieweruilib.PixelPerfect;
import com.android.hierarchyvieweruilib.PixelPerfectLoupe;
//Synthetic comment -- @@ -26,6 +27,9 @@
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
//Synthetic comment -- @@ -41,23 +45,20 @@
shell.open();
Shell shell2 = new Shell(display);
shell2.setLayout(new FillLayout());
        PixelPerfect pixelPerfect = new PixelPerfect(shell2);
        shell2.open();
        Shell shell3 = new Shell(display);
        shell3.setLayout(new FillLayout());
        final PixelPerfectLoupe pixelPerfectLoupe = new PixelPerfectLoupe(shell3);
        shell3.open();
        Shell shell4 = new Shell(display);
        shell4.setLayout(new FillLayout());
        PixelPerfectTree pixelPerfectTree = new PixelPerfectTree(shell4);
        shell4.open();
        Shell shell5 = new Shell(display);
        shell5.setLayout(new FillLayout());
        final Slider slider = new Slider(shell5, SWT.HORIZONTAL);
slider.setMinimum(2);
slider.setMaximum(25);
slider.setSelection(8);
slider.setThumb(1);
slider.addSelectionListener(new SelectionListener() {
private int oldZoom = 8;

//Synthetic comment -- @@ -68,15 +69,23 @@
public void widgetSelected(SelectionEvent arg0) {
int newZoom = slider.getSelection();
if (newZoom != oldZoom) {
                    pixelPerfectLoupe.setZoom(newZoom);
oldZoom = newZoom;
}
}

});
        shell5.open();
        while (!shell.isDisposed() && !shell2.isDisposed() && !shell3.isDisposed()
                && !shell4.isDisposed()) {
if (!display.readAndDispatch()) {
display.sleep();
}
//Synthetic comment -- @@ -87,19 +96,9 @@
if (!shell2.isDisposed()) {
shell2.dispose();
}
        if (!shell3.isDisposed()) {
            shell3.dispose();
        }
        if (!shell4.isDisposed()) {
            shell4.dispose();
        }

// NO LONGER TESTING STUFF.

        deviceSelector.terminate();
        pixelPerfect.terminate();
        pixelPerfectLoupe.terminate();
        pixelPerfectTree.terminate();
ImageLoader.dispose();
display.dispose();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ComponentRegistry.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ComponentRegistry.java
//Synthetic comment -- index 528c35c..a50478e 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.hierarchyviewerlib.models.DeviceSelectionModel;
import com.android.hierarchyviewerlib.models.PixelPerfectModel;

/**
* This is the central point for getting access to the various parts of the
//Synthetic comment -- @@ -32,6 +33,8 @@

private static PixelPerfectModel pixelPerfectModel;

public static HierarchyViewerDirector getDirector() {
return director;
}
//Synthetic comment -- @@ -55,4 +58,12 @@
public static PixelPerfectModel getPixelPerfectModel() {
return pixelPerfectModel;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index c21da49..554d543 100644

//Synthetic comment -- @@ -109,6 +109,9 @@
DeviceBridge.removeDeviceForward(device);
DeviceBridge.removeViewServerInfo(device);
ComponentRegistry.getDeviceSelectionModel().removeDevice(device);
}

public void deviceChanged(IDevice device, int changeMask) {
//Synthetic comment -- @@ -192,4 +195,17 @@
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
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java
//Synthetic comment -- index 00453ee..3e9ca58 100644

//Synthetic comment -- @@ -76,6 +76,17 @@

public int index;

public ViewNode(ViewNode parent, String data) {
this.parent = parent;
index = this.parent == null ? 0 : this.parent.children.size();
//Synthetic comment -- @@ -88,6 +99,10 @@
delimIndex = data.indexOf(' ');
hashCode = data.substring(0, delimIndex);
loadProperties(data.substring(delimIndex + 1).trim());
}

private void loadProperties(String data) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/PixelPerfectModel.java
//Synthetic comment -- index 6963b25..275658b 100644

//Synthetic comment -- @@ -24,6 +24,12 @@

public class PixelPerfectModel {

public static class Point {
public int x;

//Synthetic comment -- @@ -45,6 +51,8 @@

private ViewNode selected;

private final ArrayList<ImageChangeListener> imageChangeListeners =
new ArrayList<ImageChangeListener>();

//Synthetic comment -- @@ -53,8 +61,13 @@
this.device = device;
this.image = image;
this.viewNode = viewNode;
            this.crosshairLocation = new Point(image.width / 2, image.height / 2);
this.selected = null;
}
notifyImageLoaded();
}
//Synthetic comment -- @@ -82,6 +95,19 @@
notifyFocusChanged();
}

public ViewNode getViewNode() {
synchronized (this) {
return viewNode;
//Synthetic comment -- @@ -112,6 +138,12 @@
}
}

public static interface ImageChangeListener {
public void imageLoaded();

//Synthetic comment -- @@ -122,6 +154,8 @@
public void selectionChanged();

public void focusChanged();
}

private ImageChangeListener[] getImageChangeListenerList() {
//Synthetic comment -- @@ -182,6 +216,15 @@
}
}

public void addImageChangeListener(ImageChangeListener listener) {
synchronized (imageChangeListeners) {
imageChangeListeners.add(listener);








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/TreeViewModel.java
new file mode 100644
//Synthetic comment -- index 0000000..890b88c

//Synthetic comment -- @@ -0,0 +1,174 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/scene/DrawableViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/scene/DrawableViewNode.java
new file mode 100644
//Synthetic comment -- index 0000000..4ff8983

//Synthetic comment -- @@ -0,0 +1,234 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/DeviceSelector.java
//Synthetic comment -- index 60d8e6d..187a3fa 100644

//Synthetic comment -- @@ -36,13 +36,14 @@
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class DeviceSelector implements WindowChangeListener, SelectionListener {
private TreeViewer treeViewer;

private Tree tree;
//Synthetic comment -- @@ -144,7 +145,9 @@
}

public DeviceSelector(Composite parent) {
        treeViewer = new TreeViewer(parent, SWT.SINGLE);
treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

tree = treeViewer.getTree();
//Synthetic comment -- @@ -189,13 +192,16 @@
.getSystemColor(SWT.COLOR_BLUE));
}

    public void terminate() {
model.removeWindowChangeListener(this);
boldFont.dispose();
}

    public void setFocus() {
        tree.setFocus();
}

public void deviceConnected(final IDevice device) {
//Synthetic comment -- @@ -246,6 +252,8 @@
Object selection = ((TreeItem) e.item).getData();
if (selection instanceof IDevice) {
ComponentRegistry.getDirector().loadPixelPerfectData((IDevice) selection);
}
}









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfect.java
//Synthetic comment -- index e4345e1..3972830 100644

//Synthetic comment -- @@ -39,13 +39,11 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfect implements ImageChangeListener {
private Canvas canvas;

private PixelPerfectModel model;

    private ScrolledComposite scrolledComposite;

private Image image;

private Color crosshairColor;
//Synthetic comment -- @@ -65,11 +63,11 @@
private ViewNode selectedNode;

public PixelPerfect(Composite parent) {
        scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        canvas = new Canvas(scrolledComposite, SWT.NONE);
        scrolledComposite.setContent(canvas);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
model = ComponentRegistry.getPixelPerfectModel();
model.addImageChangeListener(this);

//Synthetic comment -- @@ -83,7 +81,9 @@
paddingColor = new Color(Display.getDefault(), new RGB(0, 0, 255));
}

    public void terminate() {
if (image != null) {
image.dispose();
}
//Synthetic comment -- @@ -92,8 +92,9 @@
paddingColor.dispose();
}

    public void setFocus() {
        canvas.setFocus();
}

private MouseListener mouseListener = new MouseListener() {
//Synthetic comment -- @@ -220,7 +221,7 @@
}
};

    private void redraw() {
Display.getDefault().asyncExec(new Runnable() {
public void run() {
canvas.redraw();
//Synthetic comment -- @@ -252,7 +253,7 @@
}
Display.getDefault().asyncExec(new Runnable() {
public void run() {
                scrolledComposite.setMinSize(width, height);
}
});
}
//Synthetic comment -- @@ -263,28 +264,28 @@
crosshairLocation = model.getCrosshairLocation();
selectedNode = model.getSelected();
}
        redraw();
}

public void imageChanged() {
synchronized (this) {
loadImage();
}
        redraw();
}

public void crosshairMoved() {
synchronized (this) {
crosshairLocation = model.getCrosshairLocation();
}
        redraw();
}

public void selectionChanged() {
synchronized (this) {
selectedNode = model.getSelected();
}
        redraw();
}

public void focusChanged() {
//Synthetic comment -- @@ -292,6 +293,10 @@
loadImage();
selectedNode = model.getSelected();
}
        redraw();
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectLoupe.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectLoupe.java
//Synthetic comment -- index eb1df88..62bb8c7 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
//Synthetic comment -- @@ -39,9 +40,7 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class PixelPerfectLoupe implements ImageChangeListener {
    private Canvas canvas;

private PixelPerfectModel model;

private Image image;
//Synthetic comment -- @@ -65,20 +64,22 @@
private int canvasHeight;

public PixelPerfectLoupe(Composite parent) {
        canvas = new Canvas(parent, SWT.NONE);
model = ComponentRegistry.getPixelPerfectModel();
model.addImageChangeListener(this);

        canvas.addPaintListener(paintListener);
        canvas.addMouseListener(mouseListener);

crosshairColor = new Color(Display.getDefault(), new RGB(255, 94, 254));

transform = new Transform(Display.getDefault());
        zoom = 8;
}

    public void terminate() {
if (image != null) {
image.dispose();
}
//Synthetic comment -- @@ -89,23 +90,6 @@
}
}

    public void setFocus() {
        canvas.setFocus();
    }

    public void setZoom(int value) {
        synchronized (this) {
            if (grid != null) {
                // To notify that the zoom level has changed, we get rid of the
                // grid.
                grid.dispose();
                grid = null;
                zoom = value;
            }
        }
        redraw();
    }

private MouseListener mouseListener = new MouseListener() {

public void mouseDoubleClick(MouseEvent e) {
//Synthetic comment -- @@ -113,13 +97,31 @@
}

public void mouseDown(MouseEvent e) {
            // pass
        }

        public void mouseUp(MouseEvent e) {
handleMouseEvent(e);
}

};

private void handleMouseEvent(MouseEvent e) {
//Synthetic comment -- @@ -129,8 +131,8 @@
if (image == null) {
return;
}
            int zoomedX = -crosshairLocation.x * zoom - zoom / 2 + canvas.getBounds().width / 2;
            int zoomedY = -crosshairLocation.y * zoom - zoom / 2 + canvas.getBounds().height / 2;
int x = (e.x - zoomedX) / zoom;
int y = (e.y - zoomedY) / zoom;
if (x >= 0 && x < width && y >= 0 && y < height) {
//Synthetic comment -- @@ -147,12 +149,10 @@
public void paintControl(PaintEvent e) {
synchronized (this) {
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
                e.gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
if (image != null && crosshairLocation != null) {
                    int zoomedX =
                            -crosshairLocation.x * zoom - zoom / 2 + canvas.getBounds().width / 2;
                    int zoomedY =
                            -crosshairLocation.y * zoom - zoom / 2 + canvas.getBounds().height / 2;
transform.translate(zoomedX, zoomedY);
transform.scale(zoom, zoom);
e.gc.setInterpolation(SWT.NONE);
//Synthetic comment -- @@ -164,13 +164,12 @@
// If the size of the canvas has changed, we need to make
// another grid.
if (grid != null
                            && (canvasWidth != canvas.getBounds().width || canvasHeight != canvas
                                    .getBounds().height)) {
grid.dispose();
grid = null;
}
                    canvasWidth = canvas.getBounds().width;
                    canvasHeight = canvas.getBounds().height;
if (grid == null) {
// Make a transparent image;
ImageData imageData =
//Synthetic comment -- @@ -208,10 +207,10 @@
}
};

    private void redraw() {
Display.getDefault().asyncExec(new Runnable() {
public void run() {
                canvas.redraw();
}
});
}
//Synthetic comment -- @@ -243,22 +242,23 @@
synchronized (this) {
loadImage();
crosshairLocation = model.getCrosshairLocation();
}
        redraw();
}

public void imageChanged() {
synchronized (this) {
loadImage();
}
        redraw();
}

public void crosshairMoved() {
synchronized (this) {
crosshairLocation = model.getCrosshairLocation();
}
        redraw();
}

public void selectionChanged() {
//Synthetic comment -- @@ -268,4 +268,17 @@
public void focusChanged() {
imageChanged();
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/PixelPerfectTree.java
//Synthetic comment -- index a85c8eb..9f25a33 100644

//Synthetic comment -- @@ -25,12 +25,14 @@
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
//Synthetic comment -- @@ -38,7 +40,7 @@

import java.util.List;

public class PixelPerfectTree implements ImageChangeListener, SelectionListener {

private TreeViewer treeViewer;

//Synthetic comment -- @@ -126,7 +128,9 @@
}

public PixelPerfectTree(Composite parent) {
        treeViewer = new TreeViewer(parent, SWT.SINGLE);
treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

tree = treeViewer.getTree();
//Synthetic comment -- @@ -154,13 +158,16 @@
folderImage = loader.loadImage("folder.png", Display.getDefault());
}

    public void terminate() {
fileImage.dispose();
folderImage.dispose();
}

    public void setFocus() {
        tree.setFocus();
}

public void imageLoaded() {
//Synthetic comment -- @@ -193,7 +200,16 @@
}

public void widgetSelected(SelectionEvent e) {
        model.setSelected((ViewNode) e.item.getData());
}

}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeView.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeView.java
new file mode 100644
//Synthetic comment -- index 0000000..3c7f4c5

//Synthetic comment -- @@ -0,0 +1,354 @@








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeViewOverview.java b/hierarchyviewer2/libs/hierarchyvieweruilib/src/com/android/hierarchyvieweruilib/TreeViewOverview.java
new file mode 100644
//Synthetic comment -- index 0000000..f9e4cef

//Synthetic comment -- @@ -0,0 +1,254 @@







