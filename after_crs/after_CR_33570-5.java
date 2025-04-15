/*Fixing draw9patch tool is showing incorrect image at opening file from D&D or menu.

This issue is reported as No.26330.http://code.google.com/p/android/issues/detail?id=26330Change-Id:Ib799be5c2ff1cf5d1821a357961435662a1c5f9c*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index e0aa026..fce5582 100644

//Synthetic comment -- @@ -30,8 +30,6 @@
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.image.BufferedImage;
//Synthetic comment -- @@ -58,6 +56,8 @@
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.geom.Area;
//Synthetic comment -- @@ -135,6 +135,12 @@
scroller.setBorder(null);
scroller.getViewport().setBorder(null);
scroller.getViewport().setOpaque(false);
        scroller.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                // Set exactly size.
                viewer.computeSize();
            }
        });

splitter.setLeftComponent(scroller);
splitter.setRightComponent(buildStretchesViewer());
//Synthetic comment -- @@ -659,7 +665,7 @@
private boolean showPatches;
private boolean showLock = true;

        private Dimension size = new Dimension(0, 0);

private boolean locked;

//Synthetic comment -- @@ -699,24 +705,6 @@

setOpaque(true);

findPatches();

addMouseListener(new MouseAdapter() {
//Synthetic comment -- @@ -1022,11 +1010,10 @@
return size;
}

        void computeSize() {
int width = image.getWidth();
int height = image.getHeight();

if (size.height == 0 || (getHeight() - size.height) == 0) {
size.setSize(width * zoom, height * zoom + helpPanel.getHeight());
} else {
//Synthetic comment -- @@ -1040,6 +1027,11 @@
}
}

        void setZoom(int value) {
            zoom = value;
            computeSize();
        }

void setPatchesVisible(boolean visible) {
showPatches = visible;
findPatches();







