/*draw9patch: drag to draw pixels*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index bf3754b..e9b8ed4 100644

//Synthetic comment -- @@ -51,6 +51,7 @@
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
//Synthetic comment -- @@ -735,16 +736,16 @@
// changed state).
currentButton = event.isShiftDown() ? MouseEvent.BUTTON3 : event.getButton();
currentButton = event.isControlDown() ? MouseEvent.BUTTON2 : currentButton;

					paint(event.getX(), event.getY(), currentButton, false);
}
});
addMouseMotionListener(new MouseMotionAdapter() {
@Override
public void mouseDragged(MouseEvent event) {
					checkLockedRegion(event.getX(), event.getY());
					
					paint(event.getX(), event.getY(), currentButton, true);
}

@Override
//Synthetic comment -- @@ -857,7 +858,9 @@
}
}

		private int lastX = -1;
		private int lastY = -1;
        private void paint(int x, int y, int button, boolean dragging) {
int color;
switch (button) {
case MouseEvent.BUTTON1:
//Synthetic comment -- @@ -879,17 +882,85 @@
x = (x - left) / zoom;
y = (y - top) / zoom;

			int width = image.getWidth();
			int height = image.getHeight();

			if (dragging) {
				if (lastX == 0) {
					x = 0;
				}
				if (lastY == 0) {
					y = 0;
				}
				if (lastX == width - 1) {
					x = width - 1;
				}
				if (lastY == height - 1) {
					y = height - 1;
				}
			} else {
				int distanceFromEdge = x;
				int pointX = 0;
				int pointY = y;
				
				if (distanceFromEdge > y) {
					distanceFromEdge = y;
					
					pointX = x;
					pointY = 0;
				}
				if (distanceFromEdge > (width - x - 1)) {
					distanceFromEdge = (width - x - 1);
					
					pointX = width - 1;
					pointY = y;
				}
				if (distanceFromEdge > (height - y - 1)) {
					distanceFromEdge = height - y - 1;
					
					pointX = x;
					pointY = height - 1;
				}
				
				x = pointX;
				y = pointY;
			}

if (((x == 0 || x == width - 1) && (y > 0 && y < height - 1)) ||
((x > 0 && x < width - 1) && (y == 0 || y == height - 1))) {
				if (dragging) {
					if (x < lastX) {
						for (int i = x; i < lastX; i++) {
							image.setRGB(i, y, color);
						}
					}
					if (x > lastX) {
						for (int i = lastX; i < x; i++) {
							image.setRGB(i, y, color);
						}
					}
					if (y < lastY) {
						for (int i = y; i < lastY; i++) {
							image.setRGB(x, i, color);
						}
					}
					if (y > lastY) {
						for (int i = lastY; i < y; i++) {
							image.setRGB(x, i, color);
						}
					}
				}
				image.setRGB(x, y, color);

findPatches();
stretchesViewer.computePatches();
if (showBadPatches) {
findBadPatches();
}
repaint();
				
				lastX = x;
				lastY = y;
}
}

//Synthetic comment -- @@ -1165,7 +1236,7 @@

for (int i = 1; i < pixels.length - 1; i++) {
int pixel = pixels[i];
                if (pixel != lastPixel && (pixel == BLACK_TICK || lastPixel == BLACK_TICK)) {
if (lastPixel == BLACK_TICK) {
if (first) startWithPatch[0] = true;
patches.add(new Pair<Integer>(lastIndex, i));







