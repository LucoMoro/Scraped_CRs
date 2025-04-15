/*Prevent java.lang.ArithmeticException in draw9patch

Initialize the zoom field to 1 instead of the default 0 since
I ran into a scenario where a division by an uninitialized zoom
caused a divide by zero exception.

Exception in thread "AWT-EventQueue-0" java.lang.ArithmeticException:
    / by zero
at com.android.draw9patch.ui.ImageEditorPanel$ImageViewer.checkLockedR
    egion(ImageEditorPanel.java:894)
at com.android.draw9patch.ui.ImageEditorPanel$ImageViewer.access$1800(
    ImageEditorPanel.java:646)
at com.android.draw9patch.ui.ImageEditorPanel$ImageViewer$3.mouseMoved
    (ImageEditorPanel.java:745)
at java.awt.Component.processMouseMotionEvent(Component.java:6397)

Change-Id:I7d2252f07458864d591be91636477ace4b520855*/




//Synthetic comment -- diff --git a/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java b/draw9patch/src/com/android/draw9patch/ui/ImageEditorPanel.java
//Synthetic comment -- index 1afc2ce..ecfed3f 100644

//Synthetic comment -- @@ -655,7 +655,7 @@
private static final double STRIPES_SPACING = 6.0;
private static final int STRIPES_ANGLE = 45;

        private int zoom = 1;
private boolean showPatches;
private boolean showLock = true;








