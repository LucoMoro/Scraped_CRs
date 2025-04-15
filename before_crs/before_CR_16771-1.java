/*Commentary, getting rid of bad about image, and some clean-up.

Change-Id:I9dad056de8e7e208f156bbedbced47dd49b61fcb*/
//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index c967d6b..7de97f4 100644

//Synthetic comment -- @@ -92,6 +92,8 @@

private static HierarchyViewerApplication APP;

private Image deviceViewImage;

private Image pixelPerfectImage;
//Synthetic comment -- @@ -104,12 +106,15 @@

private Image treeViewSelectedImage;

private Button treeViewButton;

private Button pixelPerfectButton;

private Button deviceViewButton;

private Label progressLabel;

private ProgressBar progressBar;
//Synthetic comment -- @@ -270,6 +275,7 @@
return control;
}

private void buildStatusBar(Composite parent) {
statusBar = new Composite(parent, SWT.NONE);
statusBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -305,34 +311,13 @@
pixelPerfectButtonFormData.left = new FormAttachment(treeViewButton, 2);
pixelPerfectButton.setLayoutData(pixelPerfectButtonFormData);

        // Control panel should go here.
statusBarControlPanel = new Composite(statusBar, SWT.NONE);
FormData statusBarControlPanelFormData = new FormData();
statusBarControlPanelFormData.left = new FormAttachment(pixelPerfectButton, 2);
statusBarControlPanelFormData.top = new FormAttachment(treeViewButton, 0, SWT.CENTER);
statusBarControlPanel.setLayoutData(statusBarControlPanelFormData);

        // Label should go on top
        progressLabel = new Label(statusBar, SWT.RIGHT);

        progressBar = new ProgressBar(statusBar, SWT.HORIZONTAL | SWT.INDETERMINATE | SWT.SMOOTH);
        FormData progressBarFormData = new FormData();
        progressBarFormData.right = new FormAttachment(100, 0);
        progressBarFormData.top = new FormAttachment(treeViewButton, 0, SWT.CENTER);
        progressBar.setLayoutData(progressBarFormData);

        FormData progressLabelFormData = new FormData();
        progressLabelFormData.right = new FormAttachment(progressBar, -2);
        progressLabelFormData.top = new FormAttachment(treeViewButton, 0, SWT.CENTER);
        progressLabel.setLayoutData(progressLabelFormData);

        if (progressString == null) {
            progressLabel.setVisible(false);
            progressBar.setVisible(false);
        } else {
            progressLabel.setText(progressString);
        }

statusBarStackLayout = new StackLayout();
statusBarControlPanel.setLayout(statusBarStackLayout);

//Synthetic comment -- @@ -372,6 +357,27 @@
largeZoomLabel
.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
largeZoomLabel.setText("200%");
}

private void buildDeviceSelectorPanel(Composite parent) {
//Synthetic comment -- @@ -743,6 +749,7 @@
pixelPerfectLoupe.setShowOverlay(value);
}

public void startTask(final String taskName) {
progressString = taskName;
Display.getDefault().syncExec(new Runnable() {
//Synthetic comment -- @@ -757,6 +764,7 @@
});
}

public void endTask() {
progressString = null;
Display.getDefault().syncExec(new Runnable() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 54ea9f8..9e91375 100644

//Synthetic comment -- @@ -357,7 +357,7 @@
}
}
// Automatic refreshing of windows was added in protocol version 3.
            // Before, the user needed to specify explicitely that he wants to
// get the focused window, which was done using a special type of
// window with hash code -1.
if (serverInfo.protocolVersion < 3) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java
//Synthetic comment -- index d8a9a4f..8888642 100644

//Synthetic comment -- @@ -110,18 +110,11 @@
public void updateFocusedWindow(IDevice device, int focusedWindow) {
Integer oldValue = null;
synchronized (deviceMap) {
            // A value of -1 means that no window has focus. This is a strange
            // transitive state in the window manager service.
            if (focusedWindow == -1) {
                oldValue = focusedWindowHashes.remove(device);
            } else {
                oldValue = focusedWindowHashes.put(device, new Integer(focusedWindow));
            }
}
// Only notify if the values are different. It would be cool if Java
// containers accepted basic types like int.
        if ((oldValue == null && focusedWindow != -1)
                || (oldValue != null && oldValue.intValue() != focusedWindow)) {
notifyFocusChanged(device);
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java
//Synthetic comment -- index 5d4c481..3c2c356 100644

//Synthetic comment -- @@ -94,14 +94,12 @@
shell.setLayout(gridLayout);

buttonBar = new Composite(shell, SWT.NONE);
        // buttonBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
rowLayout.pack = true;
rowLayout.center = true;
buttonBar.setLayout(rowLayout);
Composite buttons = new Composite(buttonBar, SWT.NONE);
buttons.setLayout(new FillLayout());
        // buttons.setLayoutData(new RowData());

onWhite = new Button(buttons, SWT.TOGGLE);
onWhite.setText("On White");
//Synthetic comment -- @@ -112,7 +110,6 @@
onBlack.addSelectionListener(blackSelectionListener);

showExtras = new Button(buttonBar, SWT.CHECK);
        // showExtras.setLayoutData(new RowData());
showExtras.setText("Show Extras");
showExtras.addSelectionListener(extrasSelectionListener);









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java
//Synthetic comment -- index 2a11df1..1aded6b 100644

//Synthetic comment -- @@ -202,7 +202,8 @@
}
};

    // HACK TO GET RID OF AN ERROR

private ControlListener controlListener = new ControlAdapter() {
private boolean noInput = false;
//Synthetic comment -- @@ -272,7 +273,6 @@
}

public void widgetDefaultSelected(SelectionEvent e) {
        // TODO: Double click to open view hierarchy
Object selection = ((TreeItem) e.item).getData();
if (selection instanceof IDevice) {
HierarchyViewerDirector.getDirector().loadPixelPerfectData((IDevice) selection);








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/LayoutViewer.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/LayoutViewer.java
//Synthetic comment -- index 458bae6..6ae086d 100644

//Synthetic comment -- @@ -259,6 +259,9 @@
int y2 =
Math.min(parentClipping.y + parentClipping.height, top + node.viewNode.height
+ (int) Math.ceil(0.3 / scale));
if (x2 <= x1 || y2 <= y1) {
return;
}
//Synthetic comment -- @@ -321,6 +324,7 @@
doRedraw();
}

public void treeChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfect.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfect.java
//Synthetic comment -- index 05b9679..242270b 100644

//Synthetic comment -- @@ -208,10 +208,7 @@
}

if (selectedNode != null) {
                        // There are a few quirks here. First of all, margins
                        // are sometimes negative or positive numbers... Yet,
                        // they are always treated as positive.
                        // Secondly, if the screen is in landscape mode, the
// coordinates are backwards.
int leftShift = 0;
int topShift = 0;
//Synthetic comment -- @@ -339,6 +336,7 @@
doRedraw();
}

public void treeChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectLoupe.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectLoupe.java
//Synthetic comment -- index 5497a3f..b5ad13a 100644

//Synthetic comment -- @@ -293,6 +293,7 @@
}
}

public void imageLoaded() {
Display.getDefault().syncExec(new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PropertyViewer.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PropertyViewer.java
//Synthetic comment -- index 0b76909..d7c5b1a 100644

//Synthetic comment -- @@ -230,7 +230,8 @@
}
};

    // HACK TO GET RID OF AN ERROR

private ControlListener controlListener = new ControlAdapter() {
private boolean noInput = false;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java
//Synthetic comment -- index f4e9019..0587924 100644

//Synthetic comment -- @@ -112,6 +112,37 @@

private DrawableViewNode lastDrawnSelectedViewNode;

public TreeView(Composite parent) {
super(parent, SWT.NONE);

//Synthetic comment -- @@ -164,8 +195,7 @@
synchronized (TreeView.this) {
if (tree != null && viewport != null) {

                    // I don't know what the best behaviour is... This seems
                    // like a good idea.
Point viewCenter =
new Point(viewport.x + viewport.width / 2, viewport.y + viewport.height
/ 2);
//Synthetic comment -- @@ -196,10 +226,12 @@
}
break;
case SWT.ARROW_UP:
                            int levelsOut = 0;
DrawableViewNode currentNode = selectedNode;
while (currentNode.parent != null && currentNode.viewNode.index == 0) {
                                levelsOut++;
currentNode = currentNode.parent;
}
if (currentNode.parent != null) {
//Synthetic comment -- @@ -211,7 +243,6 @@
currentNode =
currentNode.children
.get(currentNode.children.size() - 1);
                                    levelsOut--;
}
}
if (selectionChanged) {
//Synthetic comment -- @@ -219,12 +250,10 @@
}
break;
case SWT.ARROW_DOWN:
                            levelsOut = 0;
currentNode = selectedNode;
while (currentNode.parent != null
&& currentNode.viewNode.index + 1 == currentNode.parent.children
.size()) {
                                levelsOut++;
currentNode = currentNode.parent;
}
if (currentNode.parent != null) {
//Synthetic comment -- @@ -234,7 +263,6 @@
.get(currentNode.viewNode.index + 1);
while (currentNode.children.size() != 0) {
currentNode = currentNode.children.get(0);
                                    levelsOut--;
}
}
if (selectionChanged) {
//Synthetic comment -- @@ -245,6 +273,9 @@
DrawableViewNode rightNode = null;
double mostOverlap = 0;
final int N = selectedNode.children.size();
for (int i = 0; i < N; i++) {
DrawableViewNode child = selectedNode.children.get(i);
DrawableViewNode topMostChild = child;
//Synthetic comment -- @@ -305,12 +336,16 @@
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
//Synthetic comment -- @@ -319,6 +354,8 @@
return;
}
draggedNode = tree.getSelected(pt.x, pt.y);
if (draggedNode != null && draggedNode != selectedNode) {
selectedNode = draggedNode;
selectionChanged = true;
//Synthetic comment -- @@ -326,9 +363,12 @@
} else if (draggedNode != null) {
alreadySelectedOnMouseDown = true;
}
if (draggedNode == tree) {
draggedNode = null;
}
if (draggedNode != null) {
lastPoint = pt;
} else {
//Synthetic comment -- @@ -351,11 +391,18 @@
synchronized (TreeView.this) {
if (tree != null && viewport != null && lastPoint != null) {
if (draggedNode == null) {
handleMouseDrag(new Point(e.x, e.y));
viewportChanged = true;
} else {
handleMouseDrag(transformPoint(e.x, e.y));
}
Point pt = transformPoint(e.x, e.y);
DrawableViewNode mouseUpOn = tree.getSelected(pt.x, pt.y);
if (mouseUpOn != null && mouseUpOn == selectedNode
//Synthetic comment -- @@ -367,6 +414,8 @@
draggedNode = null;
redraw = true;
}
if (buttonClicked) {
HierarchyViewerDirector.getDirector().showCapture(getShell(),
selectedNode.viewNode);
//Synthetic comment -- @@ -374,6 +423,8 @@
redrawButton = true;
}
}
if (viewportChanged) {
model.setViewport(viewport);
} else if (redraw) {
//Synthetic comment -- @@ -418,6 +469,8 @@
};

private void handleMouseDrag(Point pt) {
if (draggedNode != null) {
if (lastPoint.y - pt.y != 0) {
nodeMoved = true;
//Synthetic comment -- @@ -426,42 +479,42 @@
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
//Synthetic comment -- @@ -496,6 +549,8 @@
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
if (tree != null && viewport != null) {
e.gc.setTransform(transform);
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
Path connectionPath = new Path(Display.getDefault());
//Synthetic comment -- @@ -503,24 +558,25 @@
e.gc.drawPath(connectionPath);
connectionPath.dispose();

if (selectedNode != null) {
                        int RECT_WIDTH = 155;
                        int RECT_HEIGHT = 224;

int x = selectedNode.left + DrawableViewNode.NODE_WIDTH / 2;
int y = (int) selectedNode.top + 4;
e.gc.setBackground(boxColor);
e.gc.fillPolygon(new int[] {
x, y, x - 15, y - 15, x + 15, y - 15
});
y -= 10 + RECT_HEIGHT;
                        e.gc.fillRoundRectangle(x - RECT_WIDTH / 2, y, RECT_WIDTH, RECT_HEIGHT, 15,
                                15);
selectedRectangleLocation =
new Rectangle(x - RECT_WIDTH / 2, y, RECT_WIDTH, RECT_HEIGHT);
                        int BUTTON_RIGHT_OFFSET = 1;
                        int BUTTON_TOP_OFFSET = 2;

buttonCenter =
new Point(x - BUTTON_RIGHT_OFFSET + (RECT_WIDTH - BUTTON_SIZE) / 2,
y + BUTTON_TOP_OFFSET + BUTTON_SIZE / 2);
//Synthetic comment -- @@ -538,23 +594,23 @@
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
//Synthetic comment -- @@ -562,6 +618,7 @@
}
lastDrawnSelectedViewNode = selectedNode;
}
if (scaledSelectedImage == null) {
double ratio =
1.0 * selectedNode.viewNode.image.getBounds().width
//Synthetic comment -- @@ -578,8 +635,12 @@
.getBounds().height);
newWidth = (int) (newHeight * ratio);
}
                                newWidth = Math.max(newWidth, 1);
                                newHeight = Math.max(newHeight, 1);
scaledSelectedImage =
new Image(Display.getDefault(), newWidth, newHeight);
GC gc = new GC(scaledSelectedImage);
//Synthetic comment -- @@ -591,18 +652,43 @@
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
//Synthetic comment -- @@ -629,11 +715,6 @@
.format(selectedNode.viewNode.drawTime)
+ " ms" : "n/a");

                        int TEXT_SIDE_OFFSET = 8;
                        int TEXT_TOP_OFFSET = 4;
                        int TEXT_SPACING = 2;
                        int TEXT_ROUNDING = 20;

org.eclipse.swt.graphics.Point titleExtent = e.gc.stringExtent(text);
org.eclipse.swt.graphics.Point measureExtent =
e.gc.stringExtent(measureText);
//Synthetic comment -- @@ -678,67 +759,6 @@
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
}
//Synthetic comment -- @@ -904,6 +924,8 @@
transform.translate((float) -tree.bounds.x, (float) -tree.bounds.y);
Path connectionPath = new Path(Display.getDefault());
GC gc = new GC(image);
Color white = new Color(Display.getDefault(), 255, 255, 255);
Color black = new Color(Display.getDefault(), 0, 0, 0);
gc.setForeground(white);
//Synthetic comment -- @@ -939,6 +961,8 @@
});
}

public void treeChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
//Synthetic comment -- @@ -975,6 +999,7 @@
}
}

public void viewportChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java
//Synthetic comment -- index 9a44694..ee84ccb 100644

//Synthetic comment -- @@ -134,6 +134,10 @@
dragging = false;
redraw = true;
handleMouseEvent(transformPoint(e.x, e.y));
setBounds();
setTransform();
}
//Synthetic comment -- @@ -270,6 +274,7 @@
});
}

public void treeChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {







