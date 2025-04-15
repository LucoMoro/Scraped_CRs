/*GLE: Yet another reference to event.offsetX/Y.

Change-Id:I13eeb5ed05a2f62d6afeb7009d934bd60f6f5cfd*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index 7bf336c..d7fdd1e 100755

//Synthetic comment -- @@ -606,24 +606,29 @@
// mouse pointer

// TODO quick'ndirty fix. swt.dnd in 3.4 doesn't have offsetX/Y.
try {
Field xField = event.getClass().getDeclaredField("offsetX");
Field yField = event.getClass().getDeclaredField("offsetY");

                    xField.set(event, Integer.valueOf(mImage.getBounds().width / 2));
                    yField.set(event, Integer.valueOf(mImage.getBounds().height / 2));
} catch (SecurityException e) {
} catch (NoSuchFieldException e) {
} catch (IllegalArgumentException e) {
} catch (IllegalAccessException e) {
}

                // ...and record this info in the drag state object such that we can
                // account for it when performing the drop, since we want to place the newly
                // inserted object where it is currently shown, not with its top left corner
                // in the center where the mouse cursor was (this mostly matters for
                // the AbsoluteLayout).
                ControlPoint offset = ControlPoint.create(null, -event.offsetX, -event.offsetY);
GlobalCanvasDragInfo.getInstance().setImageOffset(offset);
}
}







