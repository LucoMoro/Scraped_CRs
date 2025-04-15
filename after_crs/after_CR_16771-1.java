/*Commentary, getting rid of bad about image, and some clean-up.

Change-Id:I9dad056de8e7e208f156bbedbced47dd49b61fcb*/




//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index c967d6b..7de97f4 100644

//Synthetic comment -- @@ -92,6 +92,8 @@

private static HierarchyViewerApplication APP;

    // Images for moving between the 3 main windows.

private Image deviceViewImage;

private Image pixelPerfectImage;
//Synthetic comment -- @@ -104,12 +106,15 @@

private Image treeViewSelectedImage;

    // And their buttons

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

        // Tree View control panel...
statusBarControlPanel = new Composite(statusBar, SWT.NONE);
FormData statusBarControlPanelFormData = new FormData();
statusBarControlPanelFormData.left = new FormAttachment(pixelPerfectButton, 2);
statusBarControlPanelFormData.top = new FormAttachment(treeViewButton, 0, SWT.CENTER);
statusBarControlPanel.setLayoutData(statusBarControlPanelFormData);

statusBarStackLayout = new StackLayout();
statusBarControlPanel.setLayout(statusBarStackLayout);

//Synthetic comment -- @@ -372,6 +357,27 @@
largeZoomLabel
.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, true));
largeZoomLabel.setText("200%");

        // Progress stuff
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
}

private void buildDeviceSelectorPanel(Composite parent) {
//Synthetic comment -- @@ -743,6 +749,7 @@
pixelPerfectLoupe.setShowOverlay(value);
}

    // Shows the progress notification...
public void startTask(final String taskName) {
progressString = taskName;
Display.getDefault().syncExec(new Runnable() {
//Synthetic comment -- @@ -757,6 +764,7 @@
});
}

    // And hides it!
public void endTask() {
progressString = null;
Display.getDefault().syncExec(new Runnable() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 54ea9f8..9e91375 100644

//Synthetic comment -- @@ -357,7 +357,7 @@
}
}
// Automatic refreshing of windows was added in protocol version 3.
            // Before, the user needed to specify explicitly that he wants to
// get the focused window, which was done using a special type of
// window with hash code -1.
if (serverInfo.protocolVersion < 3) {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/models/DeviceSelectionModel.java
//Synthetic comment -- index d8a9a4f..8888642 100644

//Synthetic comment -- @@ -110,18 +110,11 @@
public void updateFocusedWindow(IDevice device, int focusedWindow) {
Integer oldValue = null;
synchronized (deviceMap) {
            oldValue = focusedWindowHashes.put(device, new Integer(focusedWindow));
}
// Only notify if the values are different. It would be cool if Java
// containers accepted basic types like int.
        if (oldValue == null || (oldValue != null && oldValue.intValue() != focusedWindow)) {
notifyFocusChanged(device);
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/CaptureDisplay.java
//Synthetic comment -- index 5d4c481..3c2c356 100644

//Synthetic comment -- @@ -94,14 +94,12 @@
shell.setLayout(gridLayout);

buttonBar = new Composite(shell, SWT.NONE);
RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
rowLayout.pack = true;
rowLayout.center = true;
buttonBar.setLayout(rowLayout);
Composite buttons = new Composite(buttonBar, SWT.NONE);
buttons.setLayout(new FillLayout());

onWhite = new Button(buttons, SWT.TOGGLE);
onWhite.setText("On White");
//Synthetic comment -- @@ -112,7 +110,6 @@
onBlack.addSelectionListener(blackSelectionListener);

showExtras = new Button(buttonBar, SWT.CHECK);
showExtras.setText("Show Extras");
showExtras.addSelectionListener(extrasSelectionListener);









//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/DeviceSelector.java
//Synthetic comment -- index 2a11df1..1aded6b 100644

//Synthetic comment -- @@ -202,7 +202,8 @@
}
};

    // If the window gets too small, hide the data, otherwise SWT throws an
    // ERROR.

private ControlListener controlListener = new ControlAdapter() {
private boolean noInput = false;
//Synthetic comment -- @@ -272,7 +273,6 @@
}

public void widgetDefaultSelected(SelectionEvent e) {
Object selection = ((TreeItem) e.item).getData();
if (selection instanceof IDevice) {
HierarchyViewerDirector.getDirector().loadPixelPerfectData((IDevice) selection);








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/LayoutViewer.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/LayoutViewer.java
//Synthetic comment -- index 458bae6..6ae086d 100644

//Synthetic comment -- @@ -259,6 +259,9 @@
int y2 =
Math.min(parentClipping.y + parentClipping.height, top + node.viewNode.height
+ (int) Math.ceil(0.3 / scale));

        // Clipping is weird... You set it to -5 and it comes out 17 or
        // something.
if (x2 <= x1 || y2 <= y1) {
return;
}
//Synthetic comment -- @@ -321,6 +324,7 @@
doRedraw();
}

    // Note the syncExec and then synchronized... It avoids deadlock
public void treeChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfect.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfect.java
//Synthetic comment -- index 05b9679..242270b 100644

//Synthetic comment -- @@ -208,10 +208,7 @@
}

if (selectedNode != null) {
                        // If the screen is in landscape mode, the
// coordinates are backwards.
int leftShift = 0;
int topShift = 0;
//Synthetic comment -- @@ -339,6 +336,7 @@
doRedraw();
}

    // Note the syncExec and then synchronized... It avoids deadlock
public void treeChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectLoupe.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PixelPerfectLoupe.java
//Synthetic comment -- index 5497a3f..b5ad13a 100644

//Synthetic comment -- @@ -293,6 +293,7 @@
}
}

    // Note the syncExec and then synchronized... It avoids deadlock
public void imageLoaded() {
Display.getDefault().syncExec(new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PropertyViewer.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/PropertyViewer.java
//Synthetic comment -- index 0b76909..d7c5b1a 100644

//Synthetic comment -- @@ -230,7 +230,8 @@
}
};

    // If the window gets too small, hide the data, otherwise SWT throws an
    // ERROR.

private ControlListener controlListener = new ControlAdapter() {
private boolean noInput = false;








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java
//Synthetic comment -- index f4e9019..0587924 100644

//Synthetic comment -- @@ -112,6 +112,37 @@

private DrawableViewNode lastDrawnSelectedViewNode;

    // The profile-image box needs to be moved to,
    // so add some dragging leeway.
    private static final int DRAG_LEEWAY = 220;

    // Profile-image box constants
    private static final int RECT_WIDTH = 190;

    private static final int RECT_HEIGHT = 224;

    private static final int BUTTON_RIGHT_OFFSET = 5;

    private static final int BUTTON_TOP_OFFSET = 5;

    private static final int IMAGE_WIDTH = 125;

    private static final int IMAGE_HEIGHT = 120;

    private static final int IMAGE_OFFSET = 6;

    private static final int IMAGE_ROUNDING = 8;

    private static final int RECTANGLE_SIZE = 5;

    private static final int TEXT_SIDE_OFFSET = 8;

    private static final int TEXT_TOP_OFFSET = 4;

    private static final int TEXT_SPACING = 2;

    private static final int TEXT_ROUNDING = 20;

public TreeView(Composite parent) {
super(parent, SWT.NONE);

//Synthetic comment -- @@ -164,8 +195,7 @@
synchronized (TreeView.this) {
if (tree != null && viewport != null) {

                    // Keep the center in the same place.
Point viewCenter =
new Point(viewport.x + viewport.width / 2, viewport.y + viewport.height
/ 2);
//Synthetic comment -- @@ -196,10 +226,12 @@
}
break;
case SWT.ARROW_UP:

                            // On up and down, it is cool to go up and down only
                            // the leaf nodes.
                            // It goes well with the layout viewer
DrawableViewNode currentNode = selectedNode;
while (currentNode.parent != null && currentNode.viewNode.index == 0) {
currentNode = currentNode.parent;
}
if (currentNode.parent != null) {
//Synthetic comment -- @@ -211,7 +243,6 @@
currentNode =
currentNode.children
.get(currentNode.children.size() - 1);
}
}
if (selectionChanged) {
//Synthetic comment -- @@ -219,12 +250,10 @@
}
break;
case SWT.ARROW_DOWN:
currentNode = selectedNode;
while (currentNode.parent != null
&& currentNode.viewNode.index + 1 == currentNode.parent.children
.size()) {
currentNode = currentNode.parent;
}
if (currentNode.parent != null) {
//Synthetic comment -- @@ -234,7 +263,6 @@
.get(currentNode.viewNode.index + 1);
while (currentNode.children.size() != 0) {
currentNode = currentNode.children.get(0);
}
}
if (selectionChanged) {
//Synthetic comment -- @@ -245,6 +273,9 @@
DrawableViewNode rightNode = null;
double mostOverlap = 0;
final int N = selectedNode.children.size();

                            // We consider all the children and pick the one
                            // who's tree overlaps the most.
for (int i = 0; i < N; i++) {
DrawableViewNode child = selectedNode.children.get(i);
DrawableViewNode topMostChild = child;
//Synthetic comment -- @@ -305,12 +336,16 @@
synchronized (TreeView.this) {
if (tree != null && viewport != null) {
Point pt = transformPoint(e.x, e.y);

                    // Ignore profiling rectangle, except for...
if (selectedRectangleLocation != null
&& pt.x >= selectedRectangleLocation.x
&& pt.x < selectedRectangleLocation.x + selectedRectangleLocation.width
&& pt.y >= selectedRectangleLocation.y
&& pt.y < selectedRectangleLocation.y
+ selectedRectangleLocation.height) {

                        // the small button!
if ((pt.x - buttonCenter.x) * (pt.x - buttonCenter.x)
+ (pt.y - buttonCenter.y) * (pt.y - buttonCenter.y) <= (BUTTON_SIZE * BUTTON_SIZE) / 4) {
buttonClicked = true;
//Synthetic comment -- @@ -319,6 +354,8 @@
return;
}
draggedNode = tree.getSelected(pt.x, pt.y);

                    // Update the selection.
if (draggedNode != null && draggedNode != selectedNode) {
selectedNode = draggedNode;
selectionChanged = true;
//Synthetic comment -- @@ -326,9 +363,12 @@
} else if (draggedNode != null) {
alreadySelectedOnMouseDown = true;
}

                    // Can't drag the root.
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
                        // The viewport moves.
handleMouseDrag(new Point(e.x, e.y));
viewportChanged = true;
} else {
                        // The nodes move.
handleMouseDrag(transformPoint(e.x, e.y));
}

                    // Deselect on the second click...
                    // This is in the mouse up, because mouse up happens after a
                    // double click event.
                    // During a double click, we don't want to deselect.
Point pt = transformPoint(e.x, e.y);
DrawableViewNode mouseUpOn = tree.getSelected(pt.x, pt.y);
if (mouseUpOn != null && mouseUpOn == selectedNode
//Synthetic comment -- @@ -367,6 +414,8 @@
draggedNode = null;
redraw = true;
}

                // Just clicked the button here.
if (buttonClicked) {
HierarchyViewerDirector.getDirector().showCapture(getShell(),
selectedNode.viewNode);
//Synthetic comment -- @@ -374,6 +423,8 @@
redrawButton = true;
}
}

            // Complicated.
if (viewportChanged) {
model.setViewport(viewport);
} else if (redraw) {
//Synthetic comment -- @@ -418,6 +469,8 @@
};

private void handleMouseDrag(Point pt) {

        // Case 1: a node is dragged. DrawableViewNode knows how to handle this.
if (draggedNode != null) {
if (lastPoint.y - pt.y != 0) {
nodeMoved = true;
//Synthetic comment -- @@ -426,42 +479,42 @@
lastPoint = pt;
return;
}

        // Case 2: the viewport is dragged. We have to make sure we respect the
        // bounds - don't let the user drag way out... + some leeway for the
        // profiling box.
double xDif = (lastPoint.x - pt.x) / zoom;
double yDif = (lastPoint.y - pt.y) / zoom;

        double treeX = tree.bounds.x - DRAG_LEEWAY;
        double treeY = tree.bounds.y - DRAG_LEEWAY;
        double treeWidth = tree.bounds.width + 2 * DRAG_LEEWAY;
        double treeHeight = tree.bounds.height + 2 * DRAG_LEEWAY;

        if (viewport.width > treeWidth) {
            if (xDif < 0 && viewport.x + viewport.width > treeX + treeWidth) {
                viewport.x = Math.max(viewport.x + xDif, treeX + treeWidth - viewport.width);
            } else if (xDif > 0 && viewport.x < treeX) {
                viewport.x = Math.min(viewport.x + xDif, treeX);
}
} else {
            if (xDif < 0 && viewport.x > treeX) {
                viewport.x = Math.max(viewport.x + xDif, treeX);
            } else if (xDif > 0 && viewport.x + viewport.width < treeX + treeWidth) {
                viewport.x = Math.min(viewport.x + xDif, treeX + treeWidth - viewport.width);
}
}
        if (viewport.height > treeHeight) {
            if (yDif < 0 && viewport.y + viewport.height > treeY + treeHeight) {
                viewport.y = Math.max(viewport.y + yDif, treeY + treeHeight - viewport.height);
            } else if (yDif > 0 && viewport.y < treeY) {
                viewport.y = Math.min(viewport.y + yDif, treeY);
}
} else {
            if (yDif < 0 && viewport.y > treeY) {
                viewport.y = Math.max(viewport.y + yDif, treeY);
            } else if (yDif > 0 && viewport.y + viewport.height < treeY + treeHeight) {
                viewport.y = Math.min(viewport.y + yDif, treeY + treeHeight - viewport.height);
}
}
lastPoint = pt;
//Synthetic comment -- @@ -496,6 +549,8 @@
e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
if (tree != null && viewport != null) {

                    // Easy stuff!
e.gc.setTransform(transform);
e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
Path connectionPath = new Path(Display.getDefault());
//Synthetic comment -- @@ -503,24 +558,25 @@
e.gc.drawPath(connectionPath);
connectionPath.dispose();

                    // Draw the profiling box.
if (selectedNode != null) {

                        // Draw the little triangle
int x = selectedNode.left + DrawableViewNode.NODE_WIDTH / 2;
int y = (int) selectedNode.top + 4;
e.gc.setBackground(boxColor);
e.gc.fillPolygon(new int[] {
x, y, x - 15, y - 15, x + 15, y - 15
});

                        // Draw the rectangle and update the location.
y -= 10 + RECT_HEIGHT;
                        e.gc.fillRoundRectangle(x - RECT_WIDTH / 2, y, RECT_WIDTH, RECT_HEIGHT, 30,
                                30);
selectedRectangleLocation =
new Rectangle(x - RECT_WIDTH / 2, y, RECT_WIDTH, RECT_HEIGHT);

                        // Draw the button
buttonCenter =
new Point(x - BUTTON_RIGHT_OFFSET + (RECT_WIDTH - BUTTON_SIZE) / 2,
y + BUTTON_TOP_OFFSET + BUTTON_SIZE / 2);
//Synthetic comment -- @@ -538,23 +594,23 @@
e.gc.fillOval(x + RECT_WIDTH / 2 - BUTTON_RIGHT_OFFSET - BUTTON_SIZE, y
+ BUTTON_TOP_OFFSET, BUTTON_SIZE, BUTTON_SIZE);

e.gc.drawRectangle(x - BUTTON_RIGHT_OFFSET
+ (RECT_WIDTH - BUTTON_SIZE - RECTANGLE_SIZE) / 2 - 1, y
+ BUTTON_TOP_OFFSET + (BUTTON_SIZE - RECTANGLE_SIZE) / 2,
RECTANGLE_SIZE + 1, RECTANGLE_SIZE);

y += 15;

                        // If there is an image, draw it.
if (selectedNode.viewNode.image != null
                                && selectedNode.viewNode.image.getBounds().height != 1
                                && selectedNode.viewNode.image.getBounds().width != 1) {

                            // Scaling the image to the right size takes lots of
                            // time, so we want to do it only once.

                            // If the selection changed, get rid of the old
                            // image.
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
                                
                                // Interesting note... We make the image twice
                                // the needed size so that there is better
                                // resolution under zoom.
                                newWidth = Math.max(newWidth * 2, 1);
                                newHeight = Math.max(newHeight * 2, 1);
scaledSelectedImage =
new Image(Display.getDefault(), newWidth, newHeight);
GC gc = new GC(scaledSelectedImage);
//Synthetic comment -- @@ -591,18 +652,43 @@
newWidth, newHeight);
gc.dispose();
}

                            // Draw the background rectangle
e.gc.setBackground(textBackgroundColor);
                            e.gc.fillRoundRectangle(x - scaledSelectedImage.getBounds().width / 4
- IMAGE_OFFSET, y
                                    + (IMAGE_HEIGHT - scaledSelectedImage.getBounds().height / 2)
                                    / 2 - IMAGE_OFFSET, scaledSelectedImage.getBounds().width / 2
                                    + 2 * IMAGE_OFFSET, scaledSelectedImage.getBounds().height / 2
                                    + 2 * IMAGE_OFFSET, IMAGE_ROUNDING, IMAGE_ROUNDING);

                            // Under max zoom, we want the image to be
                            // untransformed. So, get back to the identity
                            // transform.
                            int imageX = x - scaledSelectedImage.getBounds().width / 4;
                            int imageY =
                                    y + (IMAGE_HEIGHT - scaledSelectedImage.getBounds().height / 2)
                                            / 2;

                            Transform untransformedTransform = new Transform(Display.getDefault());
                            e.gc.setTransform(untransformedTransform);
                            float[] pt = new float[] {
                                    imageX, imageY
                            };
                            transform.transform(pt);
                            System.out.println(pt[0] + " " + pt[1]);
                            e.gc.drawImage(scaledSelectedImage, 0, 0, scaledSelectedImage
                                    .getBounds().width, scaledSelectedImage.getBounds().height,
                                    (int) pt[0], (int) pt[1], (int) (scaledSelectedImage
                                            .getBounds().width
                                            * zoom / 2),
                                    (int) (scaledSelectedImage.getBounds().height * zoom / 2));
                            untransformedTransform.dispose();
                            e.gc.setTransform(transform);
}

                        // Text stuff

y += IMAGE_HEIGHT;
y += 10;
Font font = getFont(8, false);
//Synthetic comment -- @@ -629,11 +715,6 @@
.format(selectedNode.viewNode.drawTime)
+ " ms" : "n/a");

org.eclipse.swt.graphics.Point titleExtent = e.gc.stringExtent(text);
org.eclipse.swt.graphics.Point measureExtent =
e.gc.stringExtent(measureText);
//Synthetic comment -- @@ -678,67 +759,6 @@
selectedRectangleLocation = null;
buttonCenter = null;
}
}
}
}
//Synthetic comment -- @@ -904,6 +924,8 @@
transform.translate((float) -tree.bounds.x, (float) -tree.bounds.y);
Path connectionPath = new Path(Display.getDefault());
GC gc = new GC(image);

        // Can't use Display.getDefault().getSystemColor in a non-UI thread.
Color white = new Color(Display.getDefault(), 255, 255, 255);
Color black = new Color(Display.getDefault(), 0, 0, 0);
gc.setForeground(white);
//Synthetic comment -- @@ -939,6 +961,8 @@
});
}

    // Fickle behaviour... When a new tree is loaded, the model doesn't know
    // about the viewport until it passes through here.
public void treeChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {
//Synthetic comment -- @@ -975,6 +999,7 @@
}
}

    // Note the syncExec and then synchronized... It avoids deadlock
public void viewportChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeViewOverview.java
//Synthetic comment -- index 9a44694..ee84ccb 100644

//Synthetic comment -- @@ -134,6 +134,10 @@
dragging = false;
redraw = true;
handleMouseEvent(transformPoint(e.x, e.y));

                    // Update bounds and transform only on mouse up. That way,
                    // you don't get confusing behaviour during mouse drag and
                    // it snaps neatly at the end
setBounds();
setTransform();
}
//Synthetic comment -- @@ -270,6 +274,7 @@
});
}

    // Note the syncExec and then synchronized... It avoids deadlock
public void treeChanged() {
Display.getDefault().syncExec(new Runnable() {
public void run() {







