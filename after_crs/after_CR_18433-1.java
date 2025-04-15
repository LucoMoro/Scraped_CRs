/*Set hover fill color

The code which paints the hover didn't actually set the hover fill color
on the graphics context so it was just blending with white. This happens
to be the color the fill was initialized to so the problem wasn't
noticeable.

Change-Id:I95f0eface23f2a772b687ba348997a3eac2d85a9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index e945ba2..8991f55 100755

//Synthetic comment -- @@ -1032,6 +1032,7 @@
if (mHoverFillColor != null) {
int oldAlpha = gc.getAlpha();
gc.setAlpha(SwtDrawingStyle.HOVER.getFillAlpha());
                    gc.setBackground(mHoverFillColor);
gc.fillRectangle(x, y, w, h);
gc.setAlpha(oldAlpha);
}







