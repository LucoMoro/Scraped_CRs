/*Fixing draw9patch tool is showing incorrect image at opening file from D&D or menu.

This issue is reported as No.26330.http://code.google.com/p/android/issues/detail?id=26330Change-Id:Ib799be5c2ff1cf5d1821a357961435662a1c5f9c*/
//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index e0aa026..4fa97f2 100644

//Synthetic comment -- @@ -699,7 +699,7 @@

setOpaque(true);

            // Exact size will be set by setZoom() in AncestorListener#ancestorMoved.
size = new Dimension(0, 0);

addAncestorListener(new AncestorListener() {
//Synthetic comment -- @@ -708,12 +708,12 @@
}
@Override
public void ancestorMoved(AncestorEvent event) {
                    // Set exactly size.
                    viewer.setZoom(DEFAULT_ZOOM);
                    viewer.removeAncestorListener(this);
}
@Override
public void ancestorAdded(AncestorEvent event) {
}
});








