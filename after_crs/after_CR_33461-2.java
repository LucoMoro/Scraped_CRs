/*Adding the function which fill color if it clicks pressing the Control key.

Change-Id:I35ef25f383debe9897f0c50161f4af3dc412404c*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index e0aa026..6b936d9 100644

//Synthetic comment -- @@ -728,6 +728,9 @@
// changed state).
currentButton = event.isShiftDown() ? MouseEvent.BUTTON3 : event.getButton();
paint(event.getX(), event.getY(), currentButton);
                    if (event.isControlDown()) {
                       fill(event.getX(), event.getY(), currentButton);
                    }
}
});
addMouseMotionListener(new MouseMotionAdapter() {
//Synthetic comment -- @@ -881,6 +884,72 @@
}
}

        private void fill(int x, int y, int button) {

            int color;
            switch (button) {
                case MouseEvent.BUTTON1:
                    color = 0xFF000000;
                    break;
                case MouseEvent.BUTTON3:
                    color = 0;
                    break;
                default:
                    return;
            }

            int left = (getWidth() - size.width) / 2;
            int top = helpPanel.getHeight() + (getHeight() - size.height) / 2;

            x = (x - left) / zoom;
            y = (y - top) / zoom;

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
            }

            int posX = x;
            int posY = y;

            while (true) {
                posX += iX;
                posY += iY;

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
            }

            findPatches();
            stretchesViewer.computePatches();
            if (showBadPatches) {
                findBadPatches();
            }
            repaint();
        }

private boolean checkLockedRegion(int x, int y) {
int oldX = lastPositionX;
int oldY = lastPositionY;







