/*Making the profiling stuff appear in a cool place.

Change-Id:Id28e1a2a01ec88210a7024e2ad115131c9e4d34e*/
//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java
//Synthetic comment -- index f26cc2c..eb7591b 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
@Override
public String getAdbLocation() {
String hvParentLocation = System.getProperty("com.android.hierarchyviewer.bindir");
if (hvParentLocation != null && hvParentLocation.length() != 0) {
return hvParentLocation + File.separator + SdkConstants.FN_ADB;
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index 29b35f1..f0a705c 100644

//Synthetic comment -- @@ -171,6 +171,12 @@
public void windowsChanged(final IDevice device) {
executeInBackground("Refreshing windows", new Runnable() {
public void run() {
Window[] windows = DeviceBridge.loadWindows(device);
DeviceSelectionModel.getModel().updateDevice(device, windows);
}
//Synthetic comment -- @@ -320,12 +326,8 @@
public void showCapture(final Shell shell, final ViewNode viewNode) {
executeInBackground("Capturing node", new Runnable() {
public void run() {
                final Image image = DeviceBridge.loadCapture(viewNode.window, viewNode);
if (image != null) {
                    viewNode.image = image;

                    // Force the layout viewer to redraw.
                    TreeViewModel.getModel().notifySelectionChanged();

Display.getDefault().asyncExec(new Runnable() {
public void run() {
//Synthetic comment -- @@ -337,6 +339,25 @@
});
}

public void showCapture(Shell shell) {
DrawableViewNode viewNode = TreeViewModel.getModel().getSelection();
if (viewNode != null) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index c2cb668..54ea9f8 100644

//Synthetic comment -- @@ -129,7 +129,7 @@
} catch (AdbCommandRejectedException e) {
Log.e(TAG, String.format("Adb rejected forward command for device %1$s: %2$s",
device, e.getMessage()));
                } catch (Exception e) {
Log.e(TAG, String.format("Failed to create forward for device %1$s: %2$s",
device, e.getMessage()));
}
//Synthetic comment -- @@ -148,7 +148,7 @@
Log.e(TAG, "Timeout removing port forwarding for " + device);
} catch (AdbCommandRejectedException e) {
// In this case, we want to fail silently.
                } catch (Exception e) {
Log.e(TAG, String.format("Failed to remove forward for device %1$s: %2$s",
device, e.getMessage()));
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java
//Synthetic comment -- index a17baac..5d4c481 100644

//Synthetic comment -- @@ -70,7 +70,6 @@
if (!shellVisible) {
shell.setSize(0, 0);
}
        shell.open();
Rectangle bounds =
shell.computeTrim(0, 0, Math.max(buttonBar.getBounds().width,
image.getBounds().width), buttonBar.getBounds().height
//Synthetic comment -- @@ -81,11 +80,10 @@
+ (parentShell.getBounds().width - bounds.width) / 2, parentShell.getBounds().y
+ (parentShell.getBounds().height - bounds.height) / 2);
}
if (shellVisible) {
canvas.redraw();
}
        // Odd bug in setting the size... Do it again.
        shell.setSize(bounds.width, bounds.height);
}

private static void createShell() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java
//Synthetic comment -- index 4dfdf19..6a7af9d 100644

//Synthetic comment -- @@ -37,9 +37,12 @@
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
//Synthetic comment -- @@ -67,6 +70,12 @@

private Point lastPoint;

private DrawableViewNode draggedNode;

public static final int LINE_PADDING = 10;
//Synthetic comment -- @@ -87,6 +96,22 @@

private static Image filteredSelectedImage;

public TreeView(Composite parent) {
super(parent, SWT.NONE);

//Synthetic comment -- @@ -117,6 +142,11 @@
selectedImage = loader.loadImage("selected.png", Display.getDefault());
filteredImage = loader.loadImage("filtered.png", Display.getDefault());
filteredSelectedImage = loader.loadImage("selected-filtered.png", Display.getDefault());
}

private DisposeListener disposeListener = new DisposeListener() {
//Synthetic comment -- @@ -124,6 +154,8 @@
model.removeTreeChangeListener(TreeView.this);
transform.dispose();
inverse.dispose();
}
};

//Synthetic comment -- @@ -264,6 +296,7 @@
}
if (clickedNode != null) {
HierarchyViewerDirector.getDirector().showCapture(getShell(), clickedNode.viewNode);
}
}

//Synthetic comment -- @@ -272,10 +305,26 @@
synchronized (TreeView.this) {
if (tree != null && viewport != null) {
Point pt = transformPoint(e.x, e.y);
draggedNode = tree.getSelected(pt.x, pt.y);
if (draggedNode != null && draggedNode != selectedNode) {
selectedNode = draggedNode;
selectionChanged = true;
}
if (draggedNode == tree) {
draggedNode = null;
//Synthetic comment -- @@ -285,6 +334,8 @@
} else {
lastPoint = new Point(e.x, e.y);
}
}
}
if (selectionChanged) {
//Synthetic comment -- @@ -294,7 +345,9 @@

public void mouseUp(MouseEvent e) {
boolean redraw = false;
boolean viewportChanged = false;
synchronized (TreeView.this) {
if (tree != null && viewport != null && lastPoint != null) {
if (draggedNode == null) {
//Synthetic comment -- @@ -303,18 +356,36 @@
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
model.notifyViewportChanged();
model.addTreeChangeListener(TreeView.this);
doRedraw();
}
}

//Synthetic comment -- @@ -348,6 +419,9 @@

private void handleMouseDrag(Point pt) {
if (draggedNode != null) {
draggedNode.move(lastPoint.y - pt.y);
lastPoint = pt;
return;
//Synthetic comment -- @@ -429,89 +503,244 @@
e.gc.drawPath(connectionPath);
connectionPath.dispose();

                    Transform tempTransform = new Transform(Display.getDefault());
                    e.gc.setTransform(tempTransform);

                    e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
                    e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));

                    // Draw the number of views.
                    String viewsString = Integer.toString(tree.viewNode.viewCount) + " view";
                    if (tree.viewNode.viewCount != 1) {
                        viewsString += 's';
                    }
                    org.eclipse.swt.graphics.Point stringExtent = e.gc.stringExtent(viewsString);

                    e.gc.fillRectangle(0, getBounds().height - stringExtent.y - 4,
                            stringExtent.x + 4, stringExtent.y + 4);

                    e.gc.drawText(viewsString, 2, getBounds().height - stringExtent.y - 2);

                    DrawableViewNode profiledNode =
                            (tree.viewNode.protocolVersion < 3) ? tree : selectedNode;

                    // Draw the profiling stuff
                    if (profiledNode != null && profiledNode.viewNode.measureTime != -1) {
DecimalFormat formatter = new DecimalFormat("0.000");
                        String measureString = "Measure:";
                        String measureTimeString =
                                formatter.format(profiledNode.viewNode.measureTime) + " ms";
                        String layoutString = "Layout:";
                        String layoutTimeString =
                                formatter.format(profiledNode.viewNode.layoutTime) + " ms";
                        String drawString = "Draw:";
                        String drawTimeString =
                                formatter.format(profiledNode.viewNode.drawTime) + " ms";

org.eclipse.swt.graphics.Point measureExtent =
                                e.gc.stringExtent(measureString);
                        org.eclipse.swt.graphics.Point measureTimeExtent =
                                e.gc.stringExtent(measureTimeString);
                        org.eclipse.swt.graphics.Point layoutExtent =
                                e.gc.stringExtent(layoutString);
                        org.eclipse.swt.graphics.Point layoutTimeExtent =
                                e.gc.stringExtent(layoutTimeString);
                        org.eclipse.swt.graphics.Point drawExtent = e.gc.stringExtent(drawString);
                        org.eclipse.swt.graphics.Point drawTimeExtent =
                                e.gc.stringExtent(drawTimeString);

                        int letterHeight = e.gc.getFontMetrics().getHeight();

                        int width =
                                Math.max(measureExtent.x, Math.max(layoutExtent.x, drawExtent.x))
                                        + Math.max(measureTimeExtent.x, Math.max(
                                                layoutTimeExtent.x, drawTimeExtent.x)) + 8;
                        int height = 3 * letterHeight + 8;

                        int x = getBounds().width - width;
                        int y = getBounds().height - height;

                        e.gc.fillRectangle(x, y, width, height);

                        x += 2;
                        y += 2;
                        e.gc.drawText(measureString, x, y);

                        y += letterHeight + 2;
                        e.gc.drawText(layoutString, x, y);

                        y += letterHeight + 2;
                        e.gc.drawText(drawString, x, y);

                        x = getBounds().width - measureTimeExtent.x - 2;
                        y = getBounds().height - height + 2;
                        e.gc.drawText(measureTimeString, x, y);

                        x = getBounds().width - layoutTimeExtent.x - 2;
                        y += letterHeight + 2;
                        e.gc.drawText(layoutTimeString, x, y);

                        x = getBounds().width - drawTimeExtent.x - 2;
                        y += letterHeight + 2;
                        e.gc.drawText(drawTimeString, x, y);

}
                    tempTransform.dispose();

}
}
//Synthetic comment -- @@ -542,15 +771,17 @@
}
double x = node.left + DrawableViewNode.CONTENT_LEFT_RIGHT_PADDING;
double y = node.top + DrawableViewNode.CONTENT_TOP_BOTTOM_PADDING;
        drawTextInArea(gc, transform, name, x, y, contentWidth, fontHeight);

y += fontHeight + DrawableViewNode.CONTENT_INTER_PADDING;

        gc.drawText("@" + node.viewNode.hashCode, (int) x, (int) y, SWT.DRAW_TRANSPARENT);

y += fontHeight + DrawableViewNode.CONTENT_INTER_PADDING;
if (!node.viewNode.id.equals("NO_ID")) {
            drawTextInArea(gc, transform, node.viewNode.id, x, y, contentWidth, fontHeight);
}

if (node.viewNode.measureRating != ProfileRating.NONE) {
//Synthetic comment -- @@ -631,7 +862,13 @@
}

private static void drawTextInArea(GC gc, Transform transform, String text, double x, double y,
            double width, double height) {
org.eclipse.swt.graphics.Point extent = gc.stringExtent(text);

if (extent.x > width) {
//Synthetic comment -- @@ -652,8 +889,11 @@
transformElements[3], transformElements[4], transformElements[5]);
gc.setTransform(transform);
} else {
            gc.drawText(text, (int) x, (int) y, SWT.DRAW_TRANSPARENT);
}

}

//Synthetic comment -- @@ -682,6 +922,18 @@
return image;
}

private void doRedraw() {
Display.getDefault().asyncExec(new Runnable() {
public void run() {
//Synthetic comment -- @@ -746,6 +998,10 @@
public void selectionChanged() {
synchronized (this) {
selectedNode = model.getSelection();
}
doRedraw();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/DrawableViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/DrawableViewNode.java
//Synthetic comment -- index 0d64e86..7570676 100644

//Synthetic comment -- @@ -29,7 +29,7 @@

public final static int NODE_WIDTH = 180;

    public final static int CONTENT_LEFT_RIGHT_PADDING = 7;

public final static int CONTENT_TOP_BOTTOM_PADDING = 8;








