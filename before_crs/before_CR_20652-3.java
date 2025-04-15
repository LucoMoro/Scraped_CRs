/*Fixes problem in "draw9patch tool".

If a big PNG image will be loading, HelpPanel hides ImageView.
This patch fix that problem.

Change-Id:I615705849b475e5b88daae7bef2af92cb5fa0657*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index 84b96a5..aac6145 100644

//Synthetic comment -- @@ -30,6 +30,8 @@
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.image.BufferedImage;
//Synthetic comment -- @@ -697,7 +699,25 @@

setOpaque(true);

            setZoom(DEFAULT_ZOOM);
findPatches();

addMouseListener(new MouseAdapter() {
//Synthetic comment -- @@ -843,7 +863,7 @@
}

int left = (getWidth() - size.width) / 2;
            int top = (helpPanel.getHeight() + getHeight() - size.height) / 2;

x = (x - left) / zoom;
y = (y - top) / zoom;
//Synthetic comment -- @@ -869,7 +889,7 @@
lastPositionY = y;

int left = (getWidth() - size.width) / 2;
            int top = (helpPanel.getHeight() + getHeight() - size.height) / 2;

x = (x - left) / zoom;
y = (y - top) / zoom;
//Synthetic comment -- @@ -903,7 +923,7 @@
@Override
protected void paintComponent(Graphics g) {
int x = (getWidth() - size.width) / 2;
            int y = (helpPanel.getHeight() + getHeight() - size.height) / 2;

Graphics2D g2 = (Graphics2D) g.create();
g2.setColor(BACK_COLOR);
//Synthetic comment -- @@ -1008,7 +1028,11 @@
int height = image.getHeight();

zoom = value;
            size = new Dimension(width * zoom, height * zoom);

setSize(size);
ImageEditorPanel.this.validate();







