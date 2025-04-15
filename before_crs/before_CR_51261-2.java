/*draw9patch tool changed to draw lines instead of pixels.

I changed the tool to draw lines when you are draging mouse. Before
my change it puts pixels when draging but that way it misses some pixels
because user cannot make straight line with mouse.

Change-Id:I64d6f2eb7924a67051cafabdbfb0950d07a206c7Signed-off-by: Kaloyan Donev <kdonev@gmail.com>*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index bf3754b..845ee54 100644

//Synthetic comment -- @@ -685,6 +685,12 @@
private boolean showBadPatches;

private JPanel helpPanel;

ImageViewer() {
setLayout(new GridBagLayout());
//Synthetic comment -- @@ -735,7 +741,12 @@
// changed state).
currentButton = event.isShiftDown() ? MouseEvent.BUTTON3 : event.getButton();
currentButton = event.isControlDown() ? MouseEvent.BUTTON2 : currentButton;
                    paint(event.getX(), event.getY(), currentButton);
}
});
addMouseMotionListener(new MouseMotionAdapter() {
//Synthetic comment -- @@ -743,7 +754,8 @@
public void mouseDragged(MouseEvent event) {
if (!checkLockedRegion(event.getX(), event.getY())) {
// use the stored button, see note above
                        paint(event.getX(), event.getY(),  currentButton);
}
}

//Synthetic comment -- @@ -754,7 +766,7 @@
});
Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
public void eventDispatched(AWTEvent event) {
                    enableEraseMode((KeyEvent) event);                    
}
}, AWTEvent.KEY_EVENT_MASK);

//Synthetic comment -- @@ -857,21 +869,34 @@
}
}

        private void paint(int x, int y, int button) {
            int color;
            switch (button) {
                case MouseEvent.BUTTON1:
                    color = BLACK_TICK;
                    break;
                case MouseEvent.BUTTON2:
                    color = RED_TICK;
                    break;
                case MouseEvent.BUTTON3:
                    color = 0;
                    break;
                default:
                    return;
}

int left = (getWidth() - size.width) / 2;
int top = helpPanel.getHeight() + (getHeight() - size.height) / 2;
//Synthetic comment -- @@ -881,16 +906,74 @@

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
//Synthetic comment -- @@ -915,8 +998,10 @@
locked = x > 0 && x < width - 1 && y > 0 && y < height - 1;

boolean previousCursor = showCursor;
            showCursor = ((x == 0 || x == width - 1) && (y > 0 && y < height - 1)) ||
                    ((x > 0 && x < width - 1) && (y == 0 || y == height - 1));

if (locked != previousLock) {
repaint();
//Synthetic comment -- @@ -989,6 +1074,32 @@

g2.dispose();

if (showCursor) {
Graphics cursor = g.create();
cursor.setXORMode(Color.WHITE);







