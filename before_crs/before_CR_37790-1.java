/*Prevent zoom flicker

The layout editor preserves the zoom level of a file when you close
and reopen it later.  However, when a layout is opened for the first
time, there is a brief flicker as the layout is first shown at zoom
level 100%, and then briefly thereafter rezoomed to fit the available
canvas space.

The reason for this is that when the editor is first created, the size
of the layout editor is not known (SWT reports it as 0,0), so it can't
compute a correct zoom level. The earlier solution was to do a
Display.asyncExec() to rezoom soon.

This changeset improves upon this by observing that we don't need the
zoom to be recomputed until the canvas image is actually painted, so
the delayed zoom is queried lazily both by the paint routine as well
as the delayed setup runnable. This makes new layouts come up without
any zoom flicker.

Change-Id:I72a3a30fe40f26255951de50dcb8dcaeff44cbf7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index d079ff4..3704d8f 100644

//Synthetic comment -- @@ -190,6 +190,7 @@
public synchronized void paint(GC gc) {
if (mImage != null) {
boolean valid = mCanvas.getViewHierarchy().isValid();
if (!valid) {
gc_setAlpha(gc, 128); // half-transparent
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 1e1813f..d17b802 100644

//Synthetic comment -- @@ -348,7 +348,11 @@
Boolean zoomed = coordinator.isEditorMaximized();
if (mWasZoomed != zoomed) {
if (mWasZoomed != null) {
                        setFitScale(true /*onlyZoomOut*/);
}
mWasZoomed = zoomed;
}
//Synthetic comment -- @@ -620,16 +624,16 @@
mEditorDelegate.getGraphicalEditor().setModel(mViewHierarchy.getRoot());

if (image != null) {
                mHScale.setSize(image.getImageData().width, getClientArea().width);
                mVScale.setSize(image.getImageData().height, getClientArea().height);
if (mZoomFitNextImage) {
                    mZoomFitNextImage = false;
// Must be run asynchronously because getClientArea() returns 0 bounds
// when the editor is being initialized
getDisplay().asyncExec(new Runnable() {
@Override
public void run() {
                            setFitScale(true);
}
});
}
//Synthetic comment -- @@ -639,7 +643,18 @@
redraw();
}

    /* package */ void setShowOutline(boolean newState) {
mShowOutline = newState;
redraw();
}







