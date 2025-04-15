/*draw9patch: drag to draw pixels*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index bf3754b..e9b8ed4 100644

//Synthetic comment -- @@ -51,6 +51,7 @@
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.AWTEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
//Synthetic comment -- @@ -735,16 +736,16 @@
// changed state).
currentButton = event.isShiftDown() ? MouseEvent.BUTTON3 : event.getButton();
currentButton = event.isControlDown() ? MouseEvent.BUTTON2 : currentButton;
                    paint(event.getX(), event.getY(), currentButton);
}
});
addMouseMotionListener(new MouseMotionAdapter() {
@Override
public void mouseDragged(MouseEvent event) {
                    if (!checkLockedRegion(event.getX(), event.getY())) {
                        // use the stored button, see note above
                        paint(event.getX(), event.getY(),  currentButton);
                    }
}

@Override
//Synthetic comment -- @@ -857,7 +858,9 @@
}
}

        private void paint(int x, int y, int button) {
int color;
switch (button) {
case MouseEvent.BUTTON1:
//Synthetic comment -- @@ -879,17 +882,85 @@
x = (x - left) / zoom;
y = (y - top) / zoom;

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

//Synthetic comment -- @@ -1165,7 +1236,7 @@

for (int i = 1; i < pixels.length - 1; i++) {
int pixel = pixels[i];
                if (pixel != lastPixel) {
if (lastPixel == BLACK_TICK) {
if (first) startWithPatch[0] = true;
patches.add(new Pair<Integer>(lastIndex, i));







