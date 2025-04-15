/*Adding the function which fill color if it clicks pressing the Control key.

Change-Id:I35ef25f383debe9897f0c50161f4af3dc412404c*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index e0aa026..85dd0c6 100644

//Synthetic comment -- @@ -727,7 +727,7 @@
// event returns 0, which appears to be technically correct (no button
// changed state).
currentButton = event.isShiftDown() ? MouseEvent.BUTTON3 : event.getButton();
                    paint(event.getX(), event.getY(), currentButton);
}
});
addMouseMotionListener(new MouseMotionAdapter() {
//Synthetic comment -- @@ -849,6 +849,11 @@
}

private void paint(int x, int y, int button) {
int color;
switch (button) {
case MouseEvent.BUTTON1:
//Synthetic comment -- @@ -869,16 +874,50 @@

int width = image.getWidth();
int height = image.getHeight();
            if (((x == 0 || x == width - 1) && (y > 0 && y < height - 1)) ||
                    ((x > 0 && x < width - 1) && (y == 0 || y == height - 1))) {
                image.setRGB(x, y, color);
                findPatches();
                stretchesViewer.computePatches();
                if (showBadPatches) {
                    findBadPatches();
                }
                repaint();
}
}

private boolean checkLockedRegion(int x, int y) {







