/*Ensure that canvas zoom is non-zero

Ensure that the canvas zoom is always greater than zero. This is a
safeguard against a scenario where the "compute best fit" code runs
too early where the SWT window bounds are 0 and the wrong value gets
computed.

Change-Id:I1519270f42c5a848fa0756f2f40cf6af69bb1865*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 719fdbf..59c2810 100755

//Synthetic comment -- @@ -242,7 +242,7 @@
if (zoom != null) {
try {
double initialScale = Double.parseDouble(zoom);
                    if (initialScale > 0.1) {
mHScale.setScale(initialScale);
mVScale.setScale(initialScale);
}
//Synthetic comment -- @@ -593,6 +593,10 @@
}

/* package */ void setScale(double scale, boolean redraw) {
        if (scale <= 0.0) {
            scale = 1.0;
        }

if (scale == getScale()) {
return;
}







