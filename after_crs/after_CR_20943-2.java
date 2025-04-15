/*draw9patch tool: improving zoom process.

Change-Id:Ie4d6408858db5b2bafb76ecb916e6b7c00ba0bb7*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index aac6145..1afc2ce 100644

//Synthetic comment -- @@ -659,7 +659,7 @@
private boolean showPatches;
private boolean showLock = true;

        private final Dimension size;

private boolean locked;

//Synthetic comment -- @@ -699,7 +699,6 @@

setOpaque(true);

// Exact size will be set by setZoom() in AncestorListener#ancestorMoved.
size = new Dimension(0, 0);

//Synthetic comment -- @@ -1029,14 +1028,16 @@

zoom = value;
if (size.height == 0 || (getHeight() - size.height) == 0) {
                size.setSize(width * zoom, height * zoom + helpPanel.getHeight());
} else {
                size.setSize(width * zoom, height * zoom);
}

            if (!size.equals(getSize())) {
                setSize(size);
                ImageEditorPanel.this.validate();
                repaint();
            }
}

void setPatchesVisible(boolean visible) {







