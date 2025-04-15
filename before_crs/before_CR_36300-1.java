/*Adding the function which fill color if it clicks pressing the Alt or Option key.

Change-Id:I35ef25f383debe9897f0c50161f4af3dc412404cConflicts:

	draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index 7c719b4..c3863f9 100644

//Synthetic comment -- @@ -79,6 +79,20 @@
// For Layout Bounds
private static final int RED_TICK = 0xFFFF0000;

private String name;
private BufferedImage image;
private boolean is9Patch;
//Synthetic comment -- @@ -678,6 +692,8 @@

private JLabel helpLabel;
private boolean eraseMode;

private JButton checkButton;
private List<Rectangle> corruptedPatches;
//Synthetic comment -- @@ -690,8 +706,7 @@
helpPanel = new JPanel(new BorderLayout());
helpPanel.setBorder(new EmptyBorder(0, 6, 0, 6));
helpPanel.setBackground(HELP_COLOR);
            helpLabel = new JLabel("Press Shift to erase pixels."
                    + " Press Control to draw layout bounds");
helpLabel.putClientProperty("JComponent.sizeVariant", "small");            
helpPanel.add(helpLabel, BorderLayout.WEST);
checkButton = new JButton("Show bad patches");
//Synthetic comment -- @@ -732,9 +747,9 @@
// below, because on linux, calling MouseEvent.getButton() for the drag
// event returns 0, which appears to be technically correct (no button
// changed state).
                    currentButton = event.isShiftDown() ? MouseEvent.BUTTON3 : event.getButton();
                    currentButton = event.isControlDown() ? MouseEvent.BUTTON2 : currentButton;
                    paint(event.getX(), event.getY(), currentButton);
}
});
addMouseMotionListener(new MouseMotionAdapter() {
//Synthetic comment -- @@ -753,7 +768,7 @@
});
Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
public void eventDispatched(AWTEvent event) {
                    enableEraseMode((KeyEvent) event);                    
}
}, AWTEvent.KEY_EVENT_MASK);

//Synthetic comment -- @@ -770,6 +785,8 @@
showBadPatches = !showBadPatches;
}
});
}

private void findBadPatches() {
//Synthetic comment -- @@ -843,20 +860,41 @@
return false;
}

        private void enableEraseMode(KeyEvent event) {
            boolean oldEraseMode = eraseMode;
eraseMode = event.isShiftDown();
            if (eraseMode != oldEraseMode) {
                if (eraseMode) {
                    helpLabel.setText("Release Shift to draw pixels");
                } else {
                    helpLabel.setText("Press Shift to erase pixels."
                                      + " Press Control to draw layout bounds");
                }
}
}

private void paint(int x, int y, int button) {
int color;
switch (button) {
case MouseEvent.BUTTON1:
//Synthetic comment -- @@ -880,16 +918,51 @@

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







