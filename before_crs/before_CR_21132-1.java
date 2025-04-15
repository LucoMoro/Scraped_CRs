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
* Called when the current theme changes. The theme can be queried with
* {@link ConfigurationComposite#getTheme()}.
*/
//Synthetic comment -- @@ -1770,6 +1776,7 @@

if (computeCurrentConfig() && mListener != null) {
mListener.onConfigurationChange();
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 5506319..39a9172 100644

//Synthetic comment -- @@ -716,6 +716,17 @@
}
}.schedule();
}
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java
//Synthetic comment -- index 5f2450c..313064a 100644

//Synthetic comment -- @@ -339,7 +339,7 @@
mZoomFitButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                rescaleToFit();
}
});

//Synthetic comment -- @@ -431,8 +431,8 @@
/**
* Reset the canvas scale to best fit (so content is as large as possible without scrollbars)
*/
    void rescaleToFit() {
        mEditor.getCanvasControl().setFitScale();
}

boolean rescaleToReal(boolean real) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 289831f..c47dc5e 100755

//Synthetic comment -- @@ -294,7 +294,10 @@
if (c == '1' && actionBar.isZoomingAllowed()) {
setScale(1, true);
} else if (c == '0' && actionBar.isZoomingAllowed()) {
                        setFitScale();
} else if (c == '+' && actionBar.isZoomingAllowed()) {
actionBar.rescale(1);
} else if (c == '-' && actionBar.isZoomingAllowed()) {
//Synthetic comment -- @@ -578,8 +581,14 @@
AdtPlugin.setFileProperty(mLayoutEditor.getInputFile(), NAME_ZOOM, zoomValue);
}

    /** Scales the canvas to best fit */
    void setFitScale() {
Image image = getImageOverlay().getImage();
if (image != null) {
Rectangle canvasSize = getClientArea();
//Synthetic comment -- @@ -610,10 +619,15 @@
vMargin = vDelta / 2;
}

            double hScale = canvasWidth / (double) (sceneWidth - hMargin);
            double vScale = canvasHeight / (double) (sceneHeight - vMargin);

double scale = Math.min(hScale, vScale);
setScale(scale, true);
}
}







