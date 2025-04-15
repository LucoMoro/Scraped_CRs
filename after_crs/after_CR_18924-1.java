/*GLE: Yet another reference to event.offsetX/Y.

Change-Id:I13eeb5ed05a2f62d6afeb7009d934bd60f6f5cfd*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index 7bf336c..d7fdd1e 100755

//Synthetic comment -- @@ -606,24 +606,29 @@
// mouse pointer

// TODO quick'ndirty fix. swt.dnd in 3.4 doesn't have offsetX/Y.
                ControlPoint offset = ControlPoint.create(null, 0, 0);
try {
Field xField = event.getClass().getDeclaredField("offsetX");
Field yField = event.getClass().getDeclaredField("offsetY");

                    int offsetX = mImage.getBounds().width / 2;
                    int offsetY = mImage.getBounds().height / 2;
                    xField.set(event, Integer.valueOf(offsetX));
                    yField.set(event, Integer.valueOf(offsetY));

                    // ...and record this info in the drag state object such that we can
                    // account for it when performing the drop, since we want to place the newly
                    // inserted object where it is currently shown, not with its top left corner
                    // in the center where the mouse cursor was (this mostly matters for
                    // the AbsoluteLayout).
                    offset = ControlPoint.create(null, -offsetX, -offsetY);

} catch (SecurityException e) {
} catch (NoSuchFieldException e) {
} catch (IllegalArgumentException e) {
} catch (IllegalAccessException e) {
}

GlobalCanvasDragInfo.getInstance().setImageOffset(offset);
}
}







