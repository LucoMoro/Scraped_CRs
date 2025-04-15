/*Perform auto-zoom-to-fit on device changes

When you change the device in the configuration chooser (e.g. either
the screen type or the orientation), the layout editor will now
automatically apply a special version of the "Zoom to Fit" scaling
operation: Zoom to fit but at most 100%.

In practice this means that if you have zoomed out a lot (for example
to accomodate a tablet in portrait mode) and you switch to landscape,
it will zoom back in a bit further such that the landscape orientation
fits better without leaving a lot of unused screen space.  Similarly,
if you go from a low resolution screen to a higher resolution screen,
it will zoom out if necessary to keep the picture in view.  It will
however never zoom to more than 100%, so if you go to a lower
resolution screen will not show blurry pixels, you will see the
natural size of the image.

This changeset also contains a fix for the fit-to-zoom code such that
the margin handling works properly for zoom factors greater than one.

Change-Id:I14a6061e0492c6a116eb6d4fcedd0b5c108caabe*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 0e927fa..bc298ff 100644

//Synthetic comment -- @@ -164,6 +164,12 @@
void onConfigurationChange();

/**
         * Called after a device has changed (in addition to {@link #onConfigurationChange}
         * getting called)
         */
        void onDevicePostChange();

        /**
* Called when the current theme changes. The theme can be queried with
* {@link ConfigurationComposite#getTheme()}.
*/
//Synthetic comment -- @@ -1770,6 +1776,7 @@

if (computeCurrentConfig() && mListener != null) {
mListener.onConfigurationChange();
            mListener.onDevicePostChange();
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 5506319..39a9172 100644

//Synthetic comment -- @@ -716,6 +716,17 @@
}
}.schedule();
}

        /**
         * When the device changes, zoom the view to fit, but only up to 100% (e.g. zoom
         * out to fit the content, or zoom back in if we were zoomed out more from the
         * previous view, but only up to 100% such that we never blow up pixels
         */
        public void onDevicePostChange() {
            if (mActionBar.isZoomingAllowed()) {
                getCanvasControl().setFitScale(true);
            }
        }
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java
//Synthetic comment -- index 5f2450c..313064a 100644

//Synthetic comment -- @@ -339,7 +339,7 @@
mZoomFitButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                rescaleToFit(true);
}
});

//Synthetic comment -- @@ -431,8 +431,8 @@
/**
* Reset the canvas scale to best fit (so content is as large as possible without scrollbars)
*/
    void rescaleToFit(boolean onlyZoomOut) {
        mEditor.getCanvasControl().setFitScale(onlyZoomOut);
}

boolean rescaleToReal(boolean real) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 8e53831..37ce440 100755

//Synthetic comment -- @@ -298,7 +298,10 @@
if (c == '1' && actionBar.isZoomingAllowed()) {
setScale(1, true);
} else if (c == '0' && actionBar.isZoomingAllowed()) {
                        setFitScale(true);
                    } else if (e.keyCode == '0' && (e.stateMask & SWT.MOD2) != 0
                            && actionBar.isZoomingAllowed()) {
                        setFitScale(false);
} else if (c == '+' && actionBar.isZoomingAllowed()) {
actionBar.rescale(1);
} else if (c == '-' && actionBar.isZoomingAllowed()) {
//Synthetic comment -- @@ -587,8 +590,14 @@
AdtPlugin.setFileProperty(mLayoutEditor.getInputFile(), NAME_ZOOM, zoomValue);
}

    /**
     * Scales the canvas to best fit
     *
     * @param onlyZoomOut if true, then the zooming factor will never be larger than 1,
     *            which means that this function will zoom out if necessary to show the
     *            rendered image, but it will never zoom in.
     */
    void setFitScale(boolean onlyZoomOut) {
Image image = getImageOverlay().getImage();
if (image != null) {
Rectangle canvasSize = getClientArea();
//Synthetic comment -- @@ -619,10 +628,15 @@
vMargin = vDelta / 2;
}

            double hScale = (canvasWidth - 2 * hMargin) / (double) sceneWidth;
            double vScale = (canvasHeight - 2 * vMargin) / (double) sceneHeight;

double scale = Math.min(hScale, vScale);

            if (onlyZoomOut) {
                scale = Math.min(1.0, scale);
            }

setScale(scale, true);
}
}







