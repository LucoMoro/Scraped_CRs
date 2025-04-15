/*Auto-zoom views the first time they are opened

Now that we default to the tablet screen size, creating a new layout
usually opens up a view which only shows the top left part of the
design view, and horizontal and vertical scrollbars.

This changeset adds auto-zoom such that the first time a view is
opened, the view also auto-zooms. Note that the	type of zooming
performed is zoom-out-only; it never zooms in, so a small phone layout
will continue to be shown at 100%.  We already perform auto-zoom in
several other scenarios (such as changing orientation).

Change-Id:I1e9a9e8357c952f84b541ea799c529d5dc4b3239*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 277c194..321ad01 100755

//Synthetic comment -- @@ -214,6 +214,11 @@
private final GestureManager mGestureManager = new GestureManager(this);

/**
     * When set, performs a zoom-to-fit when the next rendering image arrives.
     */
    private boolean mZoomFitNextImage;

    /**
* Native clipboard support.
*/
private ClipboardSupport mClipboardSupport;
//Synthetic comment -- @@ -244,6 +249,8 @@
} catch (NumberFormatException nfe) {
// Ignore - use zoom=100%
}
            } else {
                mZoomFitNextImage = true;
}
}

//Synthetic comment -- @@ -559,6 +566,16 @@
if (image != null) {
mHScale.setSize(image.getImageData().width, getClientArea().width);
mVScale.setSize(image.getImageData().height, getClientArea().height);
                if (mZoomFitNextImage) {
                    mZoomFitNextImage = false;
                    // Must be run asynchronously because getClientArea() returns 0 bounds
                    // when the editor is being initialized
                    getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            setFitScale(true);
                        }
                    });
                }
}
}








