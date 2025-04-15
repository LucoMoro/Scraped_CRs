/*Fix cross-canvas refresh issue

If you drag & drop a widget from one canvas to another canvas, the
operation seems to have no effect - the new widget is not drawn. If
you actually click on the target canvas it will suddenly appear.

We were calling	redraw() on the target, but that does not seem to be
enough; an explicit focus request is necessary too (and has other
benefits; once you drop you expect that element to receive a
subsequent Delete keystroke etc).

Change-Id:I2b8cd2098dd5044fe79fc730771eede5f766d8cf*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java
//Synthetic comment -- index b3af5f7..d6d624c 100755

//Synthetic comment -- @@ -282,6 +282,10 @@

clearDropInfo();
mCanvas.redraw();
}

/**







