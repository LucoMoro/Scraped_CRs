/*Fixing draw9patch tool is showing incorrect image at opening file from D&D or menu.

This issue is reported as No.26330.http://code.google.com/p/android/issues/detail?id=26330Change-Id:Ib799be5c2ff1cf5d1821a357961435662a1c5f9c*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index e0aa026..a3c8b5b 100644

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
//Synthetic comment -- @@ -699,24 +705,9 @@

setOpaque(true);

            // Exact size will be set by setZoom() in AncestorListener#ancestorAdded.
size = new Dimension(0, 0);

findPatches();

addMouseListener(new MouseAdapter() {
//Synthetic comment -- @@ -1022,11 +1013,10 @@
return size;
}

        void computeSize() {
int width = image.getWidth();
int height = image.getHeight();

if (size.height == 0 || (getHeight() - size.height) == 0) {
size.setSize(width * zoom, height * zoom + helpPanel.getHeight());
} else {
//Synthetic comment -- @@ -1038,6 +1028,11 @@
ImageEditorPanel.this.validate();
repaint();
}
	}

        void setZoom(int value) {
            zoom = value;
            computeSize();
}

void setPatchesVisible(boolean visible) {







