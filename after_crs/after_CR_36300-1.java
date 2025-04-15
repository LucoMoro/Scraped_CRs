/*Adding the function which fill color if it clicks pressing the Alt or Option key.

Change-Id:I35ef25f383debe9897f0c50161f4af3dc412404cConflicts:

	draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index 7c719b4..c3863f9 100644

//Synthetic comment -- @@ -79,6 +79,20 @@
// For Layout Bounds
private static final int RED_TICK = 0xFFFF0000;

    // Alt or Option key name is environment dependent
    private static final String KEY_NAME_ALT = "Alt";
    private static final String KEY_NAME_OPTION = "Option";

    private static final String ALT_KEY_NAME;
    static {
        String os = System.getProperty("os.name");
        if (os.startsWith("Mac OS")) {
            ALT_KEY_NAME = KEY_NAME_OPTION;
        } else {
            ALT_KEY_NAME = KEY_NAME_ALT;
        }
    }

private String name;
private BufferedImage image;
private boolean is9Patch;
//Synthetic comment -- @@ -678,6 +692,8 @@

private JLabel helpLabel;
private boolean eraseMode;
        private boolean fillMode = false;
        private boolean boundsMode = false;

private JButton checkButton;
private List<Rectangle> corruptedPatches;
//Synthetic comment -- @@ -690,8 +706,7 @@
helpPanel = new JPanel(new BorderLayout());
helpPanel.setBorder(new EmptyBorder(0, 6, 0, 6));
helpPanel.setBackground(HELP_COLOR);
            helpLabel = new JLabel("");
helpLabel.putClientProperty("JComponent.sizeVariant", "small");            
helpPanel.add(helpLabel, BorderLayout.WEST);
checkButton = new JButton("Show bad patches");
//Synthetic comment -- @@ -732,9 +747,9 @@
// below, because on linux, calling MouseEvent.getButton() for the drag
// event returns 0, which appears to be technically correct (no button
// changed state).
                    currentButton = boundsMode ? MouseEvent.BUTTON2 : event.getButton();
                    currentButton = eraseMode ? MouseEvent.BUTTON3 : currentButton;
                    paint(event.getX(), event.getY(), currentButton, fillMode);
}
});
addMouseMotionListener(new MouseMotionAdapter() {
//Synthetic comment -- @@ -753,7 +768,7 @@
});
Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
public void eventDispatched(AWTEvent event) {
                    changeMode((KeyEvent)event);
}
}, AWTEvent.KEY_EVENT_MASK);

//Synthetic comment -- @@ -770,6 +785,8 @@
showBadPatches = !showBadPatches;
}
});

            updateHelpLabel();
}

private void findBadPatches() {
//Synthetic comment -- @@ -843,20 +860,41 @@
return false;
}

        private void changeMode(KeyEvent event) {
eraseMode = event.isShiftDown();
            boundsMode = event.isControlDown();
            fillMode = event.isAltDown();
            updateHelpLabel();
        }

        private void updateHelpLabel() {
            StringBuffer sb = new StringBuffer();
            if (eraseMode) {
                sb.append("Release Shift to draw pixels.");
            } else {
                sb.append("Press Shift to erase pixels.");
}
            if (boundsMode && !eraseMode) {
                sb.append(" Release Control to draw pixels.");
            } else if (!eraseMode) {
                sb.append(" Press Control to draw layout bounds.");
            }
            if (fillMode) {
                sb.append(String.format(" Release %s to %s a pixel.", ALT_KEY_NAME,
                        eraseMode ? "erase" : "draw"));
            } else {
                sb.append(String.format(" Press %s to %s area.", ALT_KEY_NAME,
                        eraseMode ? "erase" : "fill"));
            }
            helpLabel.setText(sb.toString());
}

private void paint(int x, int y, int button) {
            paint(x, y, button, false);
        }

        private void paint(int x, int y, int button, boolean fillMode) {

int color;
switch (button) {
case MouseEvent.BUTTON1:
//Synthetic comment -- @@ -880,16 +918,51 @@

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
            int firstColor = image.getRGB(posX, posY);

            do {
                if (((posX == 0 || posX == width - 1)
                        && (posY == 0 || posY == height - 1))
                        || image.getRGB(posX, posY) != firstColor) {

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







