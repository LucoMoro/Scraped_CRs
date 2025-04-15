/*Adding the function which fill color if it clicks pressing the Control key.

Change-Id:I35ef25f383debe9897f0c50161f4af3dc412404c*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index e0aa026..85dd0c6 100644

//Synthetic comment -- @@ -727,7 +727,7 @@
// event returns 0, which appears to be technically correct (no button
// changed state).
currentButton = event.isShiftDown() ? MouseEvent.BUTTON3 : event.getButton();
                    paint(event.getX(), event.getY(), currentButton, event.isControlDown());
}
});
addMouseMotionListener(new MouseMotionAdapter() {
//Synthetic comment -- @@ -849,6 +849,11 @@
}

private void paint(int x, int y, int button) {
            paint(x, y, button, false);
        }

        private void paint(int x, int y, int button, boolean fillMode) {

int color;
switch (button) {
case MouseEvent.BUTTON1:
//Synthetic comment -- @@ -869,16 +874,50 @@

int width = image.getWidth();
int height = image.getHeight();

            int iX = 0;
            int iY = 0;

            if ((x == 0 || x == width - 1) && (y > 0 && y < height - 1)) {
                iX = 0;
                iY = 1;
            } else if ((x > 0 && x < width - 1) && (y == 0 || y == height - 1)) {
                iX = 1;
                iY = 0;
            } else {
                return;
}

            int posX = x;
            int posY = y;

            do {
                if (((posX == 0 || posX == width - 1)
                        && (posY == 0 || posY == height - 1))
                        || image.getRGB(posX, posY) == color) {

                    // Reverse to fill direction
                    if (iX > 0 || iY > 0) {
                        iX *= -1;
                        iY *= -1;
                        posX = x;
                        posY = y;
                    } else {
                        break;
                    }
                }
                image.setRGB(posX, posY, color);
                posX += iX;
                posY += iY;

            } while (fillMode);

            findPatches();
            stretchesViewer.computePatches();
            if (showBadPatches) {
                findBadPatches();
            }
            repaint();
}

private boolean checkLockedRegion(int x, int y) {







