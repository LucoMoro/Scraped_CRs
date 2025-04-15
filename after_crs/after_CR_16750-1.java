/*Making the profiling stuff appear in a cool place.

Change-Id:Id28e1a2a01ec88210a7024e2ad115131c9e4d34e*/




//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplicationDirector.java
//Synthetic comment -- index f26cc2c..eb7591b 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
@Override
public String getAdbLocation() {
String hvParentLocation = System.getProperty("com.android.hierarchyviewer.bindir");
        hvParentLocation = "/usr/local/google/android/out/host/linux-x86/bin";
if (hvParentLocation != null && hvParentLocation.length() != 0) {
return hvParentLocation + File.separator + SdkConstants.FN_ADB;
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/HierarchyViewerDirector.java
//Synthetic comment -- index 29b35f1..f0a705c 100644

//Synthetic comment -- @@ -171,6 +171,12 @@
public void windowsChanged(final IDevice device) {
executeInBackground("Refreshing windows", new Runnable() {
public void run() {
                if (!DeviceBridge.isViewServerRunning(device)) {
                    if (!DeviceBridge.startViewServer(device)) {
                        Log.e(TAG, "Unable to debug device " + device);
                        return;
                    }
                }
Window[] windows = DeviceBridge.loadWindows(device);
DeviceSelectionModel.getModel().updateDevice(device, windows);
}
//Synthetic comment -- @@ -320,12 +326,8 @@
public void showCapture(final Shell shell, final ViewNode viewNode) {
executeInBackground("Capturing node", new Runnable() {
public void run() {
                final Image image = loadCapture(viewNode);
if (image != null) {

Display.getDefault().asyncExec(new Runnable() {
public void run() {
//Synthetic comment -- @@ -337,6 +339,25 @@
});
}

    public Image loadCapture(ViewNode viewNode) {
        final Image image = DeviceBridge.loadCapture(viewNode.window, viewNode);
        if (image != null) {
            viewNode.image = image;

            // Force the layout viewer to redraw.
            TreeViewModel.getModel().notifySelectionChanged();
        }
        return image;
    }

    public void loadCaptureInBackground(final ViewNode viewNode) {
        executeInBackground("Capturing node", new Runnable() {
            public void run() {
                loadCapture(viewNode);
            }
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
                } catch (IOException e) {
Log.e(TAG, String.format("Failed to create forward for device %1$s: %2$s",
device, e.getMessage()));
}
//Synthetic comment -- @@ -148,7 +148,7 @@
Log.e(TAG, "Timeout removing port forwarding for " + device);
} catch (AdbCommandRejectedException e) {
// In this case, we want to fail silently.
                } catch (IOException e) {
Log.e(TAG, String.format("Failed to remove forward for device %1$s: %2$s",
device, e.getMessage()));
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java
//Synthetic comment -- index a17baac..5d4c481 100644

//Synthetic comment -- @@ -70,7 +70,6 @@
if (!shellVisible) {
shell.setSize(0, 0);
}
Rectangle bounds =
shell.computeTrim(0, 0, Math.max(buttonBar.getBounds().width,
image.getBounds().width), buttonBar.getBounds().height
//Synthetic comment -- @@ -81,11 +80,10 @@
+ (parentShell.getBounds().width - bounds.width) / 2, parentShell.getBounds().y
+ (parentShell.getBounds().height - bounds.height) / 2);
}
        shell.open();
if (shellVisible) {
canvas.redraw();
}
}

private static void createShell() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java
//Synthetic comment -- index 4dfdf19..6a7af9d 100644

//Synthetic comment -- @@ -37,9 +37,12 @@
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
//Synthetic comment -- @@ -67,6 +70,12 @@

private Point lastPoint;

    private boolean alreadySelectedOnMouseDown;

    private boolean doubleClicked;

    private boolean nodeMoved;

private DrawableViewNode draggedNode;

public static final int LINE_PADDING = 10;
//Synthetic comment -- @@ -87,6 +96,22 @@

private static Image filteredSelectedImage;

    private Color boxColor;

    private Color textBackgroundColor;

    private Rectangle selectedRectangleLocation;

    private Point buttonCenter;
    
    private static final int BUTTON_SIZE = 13;

    private Image scaledSelectedImage;

    private boolean buttonClicked;

    private DrawableViewNode lastDrawnSelectedViewNode;

public TreeView(Composite parent) {
super(parent, SWT.NONE);

//Synthetic comment -- @@ -117,6 +142,11 @@
selectedImage = loader.loadImage("selected.png", Display.getDefault());
filteredImage = loader.loadImage("filtered.png", Display.getDefault());
filteredSelectedImage = loader.loadImage("selected-filtered.png", Display.getDefault());
        boxColor = new Color(Display.getDefault(), new RGB(225, 225, 225));
        textBackgroundColor = new Color(Display.getDefault(), new RGB(82, 82, 82));
        if (scaledSelectedImage != null) {
            scaledSelectedImage.dispose();
        }
}

private DisposeListener disposeListener = new DisposeListener() {
//Synthetic comment -- @@ -124,6 +154,8 @@
model.removeTreeChangeListener(TreeView.this);
transform.dispose();
inverse.dispose();
            boxColor.dispose();
            textBackgroundColor.dispose();
}
};

//Synthetic comment -- @@ -264,6 +296,7 @@
}
if (clickedNode != null) {
HierarchyViewerDirector.getDirector().showCapture(getShell(), clickedNode.viewNode);
                doubleClicked = true;
}
}

//Synthetic comment -- @@ -272,10 +305,26 @@
synchronized (TreeView.this) {
if (tree != null && viewport != null) {
Point pt = transformPoint(e.x, e.y);
                    if (selectedRectangleLocation != null
                            && pt.x >= selectedRectangleLocation.x
                            && pt.x < selectedRectangleLocation.x + selectedRectangleLocation.width
                            && pt.y >= selectedRectangleLocation.y
                            && pt.y < selectedRectangleLocation.y
                                    + selectedRectangleLocation.height) {
                        if ((pt.x - buttonCenter.x) * (pt.x - buttonCenter.x)
                                + (pt.y - buttonCenter.y) * (pt.y - buttonCenter.y) <= (BUTTON_SIZE * BUTTON_SIZE) / 4) {
                            buttonClicked = true;
                            doRedraw();
                        }
                        return;
                    }
draggedNode = tree.getSelected(pt.x, pt.y);
if (draggedNode != null && draggedNode != selectedNode) {
selectedNode = draggedNode;
selectionChanged = true;
                        alreadySelectedOnMouseDown = false;
                    } else if (draggedNode != null) {
                        alreadySelectedOnMouseDown = true;
}
if (draggedNode == tree) {
draggedNode = null;
//Synthetic comment -- @@ -285,6 +334,8 @@
} else {
lastPoint = new Point(e.x, e.y);
}
                    nodeMoved = false;
                    doubleClicked = false;
}
}
if (selectionChanged) {
//Synthetic comment -- @@ -294,7 +345,9 @@

public void mouseUp(MouseEvent e) {
boolean redraw = false;
            boolean redrawButton = false;
boolean viewportChanged = false;
            boolean selectionChanged = false;
synchronized (TreeView.this) {
if (tree != null && viewport != null && lastPoint != null) {
if (draggedNode == null) {
//Synthetic comment -- @@ -303,18 +356,36 @@
} else {
handleMouseDrag(transformPoint(e.x, e.y));
}
                    Point pt = transformPoint(e.x, e.y);
                    DrawableViewNode mouseUpOn = tree.getSelected(pt.x, pt.y);
                    if (mouseUpOn != null && mouseUpOn == selectedNode
                            && alreadySelectedOnMouseDown && !nodeMoved && !doubleClicked) {
                        selectedNode = null;
                        selectionChanged = true;
                    }
lastPoint = null;
draggedNode = null;
redraw = true;
}
                if (buttonClicked) {
                    HierarchyViewerDirector.getDirector().showCapture(getShell(),
                            selectedNode.viewNode);
                    buttonClicked = false;
                    redrawButton = true;
                }
}
if (viewportChanged) {
model.setViewport(viewport);
} else if (redraw) {
model.removeTreeChangeListener(TreeView.this);
model.notifyViewportChanged();
                if (selectionChanged) {
                    model.setSelection(selectedNode);
                }
model.addTreeChangeListener(TreeView.this);
doRedraw();
            } else if (redrawButton) {
                doRedraw();
}
}

//Synthetic comment -- @@ -348,6 +419,9 @@

private void handleMouseDrag(Point pt) {
if (draggedNode != null) {
            if (lastPoint.y - pt.y != 0) {
                nodeMoved = true;
            }
draggedNode.move(lastPoint.y - pt.y);
lastPoint = pt;
return;
//Synthetic comment -- @@ -429,89 +503,244 @@
e.gc.drawPath(connectionPath);
connectionPath.dispose();

                    if (selectedNode != null) {
                        int RECT_WIDTH = 155;
                        int RECT_HEIGHT = 224;

                        int x = selectedNode.left + DrawableViewNode.NODE_WIDTH / 2;
                        int y = (int) selectedNode.top + 4;
                        e.gc.setBackground(boxColor);
                        e.gc.fillPolygon(new int[] {
                                x, y, x - 15, y - 15, x + 15,
                                y - 15
                        });
                        y -= 10 + RECT_HEIGHT;
                        e.gc.fillRoundRectangle(x - RECT_WIDTH / 2, y, RECT_WIDTH,
                                RECT_HEIGHT, 15, 15);
                        selectedRectangleLocation =
                                new Rectangle(x - RECT_WIDTH / 2, y, RECT_WIDTH,
                                        RECT_HEIGHT);
                        int BUTTON_RIGHT_OFFSET = 1;
                        int BUTTON_TOP_OFFSET = 2;
                        
                        buttonCenter =
                                new Point(x - BUTTON_RIGHT_OFFSET + (RECT_WIDTH - BUTTON_SIZE) / 2,
                                        y + BUTTON_TOP_OFFSET + BUTTON_SIZE / 2);

                        if (buttonClicked) {
                            e.gc
                                    .setBackground(Display.getDefault().getSystemColor(
                                            SWT.COLOR_BLACK));
                        } else {
                            e.gc.setBackground(textBackgroundColor);

                        }
                        e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

                        e.gc.fillOval(x + RECT_WIDTH / 2 - BUTTON_RIGHT_OFFSET - BUTTON_SIZE, y
                                + BUTTON_TOP_OFFSET, BUTTON_SIZE, BUTTON_SIZE);

                        int RECTANGLE_SIZE = 5;

                        e.gc.drawRectangle(x - BUTTON_RIGHT_OFFSET
                                + (RECT_WIDTH - BUTTON_SIZE - RECTANGLE_SIZE) / 2 - 1, y
                                + BUTTON_TOP_OFFSET + (BUTTON_SIZE - RECTANGLE_SIZE) / 2,
                                RECTANGLE_SIZE + 1, RECTANGLE_SIZE);

                        y += 15;
                        int IMAGE_WIDTH = 125;
                        int IMAGE_HEIGHT = 120;

                        int IMAGE_OFFSET = 6;
                        int IMAGE_ROUNDING = 8;

                        if (selectedNode.viewNode.image != null
                                && selectedNode.viewNode.image.getBounds().height != 0
                                && selectedNode.viewNode.image.getBounds().width != 0) {
                            if (lastDrawnSelectedViewNode != selectedNode) {
                                if (scaledSelectedImage != null) {
                                    scaledSelectedImage.dispose();
                                    scaledSelectedImage = null;
                                }
                                lastDrawnSelectedViewNode = selectedNode;
                            }
                            if (scaledSelectedImage == null) {
                                double ratio =
                                        1.0 * selectedNode.viewNode.image.getBounds().width
                                                / selectedNode.viewNode.image.getBounds().height;
                                int newWidth, newHeight;
                                if (ratio > 1.0 * IMAGE_WIDTH / IMAGE_HEIGHT) {
                                    newWidth =
                                            Math.min(IMAGE_WIDTH, selectedNode.viewNode.image
                                                    .getBounds().width);
                                    newHeight = (int) (newWidth / ratio);
                                } else {
                                    newHeight =
                                            Math.min(IMAGE_HEIGHT, selectedNode.viewNode.image
                                                    .getBounds().height);
                                    newWidth = (int) (newHeight * ratio);
                                }
                                newWidth = Math.max(newWidth, 1);
                                newHeight = Math.max(newHeight, 1);
                                scaledSelectedImage =
                                        new Image(Display.getDefault(), newWidth, newHeight);
                                GC gc = new GC(scaledSelectedImage);
                                gc.setBackground(textBackgroundColor);
                                gc.fillRectangle(0, 0, newWidth, newHeight);
                                gc.drawImage(selectedNode.viewNode.image, 0, 0,
                                        selectedNode.viewNode.image.getBounds().width,
                                        selectedNode.viewNode.image.getBounds().height, 0, 0,
                                        newWidth, newHeight);
                                gc.dispose();
                            }
                            e.gc.setBackground(textBackgroundColor);
                            e.gc.fillRoundRectangle(x - scaledSelectedImage.getBounds().width / 2
                                    - IMAGE_OFFSET, y
                                    + (IMAGE_HEIGHT - scaledSelectedImage.getBounds().height) / 2
                                    - IMAGE_OFFSET, scaledSelectedImage.getBounds().width + 2
                                    * IMAGE_OFFSET, scaledSelectedImage.getBounds().height + 2
                                    * IMAGE_OFFSET, IMAGE_ROUNDING, IMAGE_ROUNDING);
                            e.gc.drawImage(scaledSelectedImage, x
                                    - scaledSelectedImage.getBounds().width / 2, y
                                    + (IMAGE_HEIGHT - scaledSelectedImage.getBounds().height) / 2);
                        }

                        y += IMAGE_HEIGHT;
                        y += 10;
                        Font font = getFont(8, false);
                        e.gc.setFont(font);

                        String text =
                                selectedNode.viewNode.viewCount + " view"
                                        + (selectedNode.viewNode.viewCount != 1 ? "s" : "");
DecimalFormat formatter = new DecimalFormat("0.000");

                        String measureText =
                                "Measure: "
                                        + (selectedNode.viewNode.measureTime != -1 ? formatter
                                                .format(selectedNode.viewNode.measureTime)
                                                + " ms" : "n/a");
                        String layoutText =
                                "Layout: "
                                        + (selectedNode.viewNode.layoutTime != -1 ? formatter
                                                .format(selectedNode.viewNode.layoutTime)
                                                + " ms" : "n/a");
                        String drawText =
                                "Draw: "
                                        + (selectedNode.viewNode.drawTime != -1 ? formatter
                                                .format(selectedNode.viewNode.drawTime)
                                                + " ms" : "n/a");


                        int TEXT_SIDE_OFFSET = 8;
                        int TEXT_TOP_OFFSET = 4;
                        int TEXT_SPACING = 2;
                        int TEXT_ROUNDING = 20;

                        org.eclipse.swt.graphics.Point titleExtent = e.gc.stringExtent(text);
org.eclipse.swt.graphics.Point measureExtent =
                                e.gc.stringExtent(measureText);
                        org.eclipse.swt.graphics.Point layoutExtent = e.gc.stringExtent(layoutText);
                        org.eclipse.swt.graphics.Point drawExtent = e.gc.stringExtent(drawText);
                        int boxWidth =
                                Math.max(titleExtent.x, Math.max(measureExtent.x, Math.max(
                                        layoutExtent.x, drawExtent.x)))
                                        + 2 * TEXT_SIDE_OFFSET;
                        int boxHeight =
                                titleExtent.y + TEXT_SPACING + measureExtent.y + TEXT_SPACING
                                        + layoutExtent.y + TEXT_SPACING + drawExtent.y + 2
                                        * TEXT_TOP_OFFSET;

                        e.gc.setBackground(textBackgroundColor);
                        e.gc.fillRoundRectangle(x - boxWidth / 2, y, boxWidth, boxHeight,
                                TEXT_ROUNDING, TEXT_ROUNDING);

                        e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

                        y += TEXT_TOP_OFFSET;
                        
                        e.gc.drawText(text, x - titleExtent.x / 2, y, true);
                        
                        x -= boxWidth / 2;
                        x += TEXT_SIDE_OFFSET;

                        y += titleExtent.y + TEXT_SPACING;

                        e.gc.drawText(measureText, x, y, true);

                        y += measureExtent.y + TEXT_SPACING;

                        e.gc.drawText(layoutText, x, y, true);

                        y += layoutExtent.y + TEXT_SPACING;

                        e.gc.drawText(drawText, x, y, true);

                        font.dispose();
                    } else {
                        selectedRectangleLocation = null;
                        buttonCenter = null;
}

                    /*
                     * Transform tempTransform = new
                     * Transform(Display.getDefault());
                     * e.gc.setTransform(tempTransform);
                     * e.gc.setBackground(Display
                     * .getDefault().getSystemColor(SWT.COLOR_GRAY));
                     * e.gc.setForeground
                     * (Display.getDefault().getSystemColor(SWT.COLOR_RED)); //
                     * Draw the number of views. String viewsString =
                     * Integer.toString(tree.viewNode.viewCount) + " view"; if
                     * (tree.viewNode.viewCount != 1) { viewsString += 's'; }
                     * org.eclipse.swt.graphics.Point stringExtent =
                     * e.gc.stringExtent(viewsString); e.gc.fillRectangle(0,
                     * getBounds().height - stringExtent.y - 4, stringExtent.x +
                     * 4, stringExtent.y + 4); e.gc.drawText(viewsString, 2,
                     * getBounds().height - stringExtent.y - 2);
                     * DrawableViewNode profiledNode =
                     * (tree.viewNode.protocolVersion < 3) ? tree :
                     * selectedNode; // Draw the profiling stuff if
                     * (profiledNode != null &&
                     * profiledNode.viewNode.measureTime != -1) { DecimalFormat
                     * formatter = new DecimalFormat("0.000"); String
                     * measureString = "Measure:"; String measureTimeString =
                     * formatter.format(profiledNode.viewNode.measureTime) +
                     * " ms"; String layoutString = "Layout:"; String
                     * layoutTimeString =
                     * formatter.format(profiledNode.viewNode.layoutTime) +
                     * " ms"; String drawString = "Draw:"; String drawTimeString
                     * = formatter.format(profiledNode.viewNode.drawTime) +
                     * " ms"; org.eclipse.swt.graphics.Point measureExtent =
                     * e.gc.stringExtent(measureString);
                     * org.eclipse.swt.graphics.Point measureTimeExtent =
                     * e.gc.stringExtent(measureTimeString);
                     * org.eclipse.swt.graphics.Point layoutExtent =
                     * e.gc.stringExtent(layoutString);
                     * org.eclipse.swt.graphics.Point layoutTimeExtent =
                     * e.gc.stringExtent(layoutTimeString);
                     * org.eclipse.swt.graphics.Point drawExtent =
                     * e.gc.stringExtent(drawString);
                     * org.eclipse.swt.graphics.Point drawTimeExtent =
                     * e.gc.stringExtent(drawTimeString); int letterHeight =
                     * e.gc.getFontMetrics().getHeight(); int width =
                     * Math.max(measureExtent.x, Math.max(layoutExtent.x,
                     * drawExtent.x)) + Math.max(measureTimeExtent.x, Math.max(
                     * layoutTimeExtent.x, drawTimeExtent.x)) + 8; int height =
                     * 3 * letterHeight + 8; int x = getBounds().width - width;
                     * int y = getBounds().height - height;
                     * e.gc.fillRectangle(x, y, width, height); x += 2; y += 2;
                     * e.gc.drawText(measureString, x, y); y += letterHeight +
                     * 2; e.gc.drawText(layoutString, x, y); y += letterHeight +
                     * 2; e.gc.drawText(drawString, x, y); x = getBounds().width
                     * - measureTimeExtent.x - 2; y = getBounds().height -
                     * height + 2; e.gc.drawText(measureTimeString, x, y); x =
                     * getBounds().width - layoutTimeExtent.x - 2; y +=
                     * letterHeight + 2; e.gc.drawText(layoutTimeString, x, y);
                     * x = getBounds().width - drawTimeExtent.x - 2; y +=
                     * letterHeight + 2; e.gc.drawText(drawTimeString, x, y); }
                     * tempTransform.dispose();
                     */

}
}
//Synthetic comment -- @@ -542,15 +771,17 @@
}
double x = node.left + DrawableViewNode.CONTENT_LEFT_RIGHT_PADDING;
double y = node.top + DrawableViewNode.CONTENT_TOP_BOTTOM_PADDING;
        drawTextInArea(gc, transform, name, x, y, contentWidth, fontHeight, 10, true);

y += fontHeight + DrawableViewNode.CONTENT_INTER_PADDING;

        drawTextInArea(gc, transform, "@" + node.viewNode.hashCode, x, y, contentWidth, fontHeight,
                8, false);

y += fontHeight + DrawableViewNode.CONTENT_INTER_PADDING;
if (!node.viewNode.id.equals("NO_ID")) {
            drawTextInArea(gc, transform, node.viewNode.id, x, y, contentWidth, fontHeight, 8,
                    false);
}

if (node.viewNode.measureRating != ProfileRating.NONE) {
//Synthetic comment -- @@ -631,7 +862,13 @@
}

private static void drawTextInArea(GC gc, Transform transform, String text, double x, double y,
            double width, double height, int fontSize, boolean bold) {

        Font oldFont = gc.getFont();

        Font newFont = getFont(fontSize, bold);
        gc.setFont(newFont);

org.eclipse.swt.graphics.Point extent = gc.stringExtent(text);

if (extent.x > width) {
//Synthetic comment -- @@ -652,8 +889,11 @@
transformElements[3], transformElements[4], transformElements[5]);
gc.setTransform(transform);
} else {
            gc.drawText(text, (int) (x + (width - extent.x) / 2),
                    (int) (y + (height - extent.y) / 2), SWT.DRAW_TRANSPARENT);
}
        gc.setFont(oldFont);
        newFont.dispose();

}

//Synthetic comment -- @@ -682,6 +922,18 @@
return image;
}

    private static Font getFont(int size, boolean bold) {
        Font systemFont = Display.getDefault().getSystemFont();
        FontData[] fontData = systemFont.getFontData();
        for (int i = 0; i < fontData.length; i++) {
            fontData[i].setHeight(size);
            if (bold) {
                fontData[i].setStyle(SWT.BOLD);
            }
        }
        return new Font(Display.getDefault(), fontData);
    }

private void doRedraw() {
Display.getDefault().asyncExec(new Runnable() {
public void run() {
//Synthetic comment -- @@ -746,6 +998,10 @@
public void selectionChanged() {
synchronized (this) {
selectedNode = model.getSelection();
            if (selectedNode != null && selectedNode.viewNode.image == null) {
                HierarchyViewerDirector.getDirector()
                        .loadCaptureInBackground(selectedNode.viewNode);
            }
}
doRedraw();
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/DrawableViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/util/DrawableViewNode.java
//Synthetic comment -- index 0d64e86..7570676 100644

//Synthetic comment -- @@ -29,7 +29,7 @@

public final static int NODE_WIDTH = 180;

    public final static int CONTENT_LEFT_RIGHT_PADDING = 9;

public final static int CONTENT_TOP_BOTTOM_PADDING = 8;








