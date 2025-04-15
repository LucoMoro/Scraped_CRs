/*Improvements to the multi-configuration layout

This adds a new layout algorithm which tries to do a more optimal fit
if all the configuration previews can fit on the current screen
without scrolling. (However, it still doesn't scale up these previews
to fit all available space, that's coming in a later CL).

It also delays rendering previews and performing layout until the
layout is actually painted, and improves the error rendering a bit.
It's also more deliberate in how preview renderings are scheduled,
performing them in visual order etc.

There's a new brief animation when you switch to a preview.

Finally, there are some preview zoom controls now.

Change-Id:Iea503a3fd57dfcaea7656e47b946bfcfea3eecb1*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/BinPacker.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/BinPacker.java
new file mode 100644
//Synthetic comment -- index 0000000..9fc2e09

//Synthetic comment -- @@ -0,0 +1,352 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index aeafa6d..b5bc9aa 100644

//Synthetic comment -- @@ -671,8 +671,14 @@
null);
}

@Nullable
    private static BufferedImage readImage(@NonNull String name) {
InputStream stream = ImageUtils.class.getResourceAsStream("/icons/" + name); //$NON-NLS-1$
if (stream != null) {
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index cb55b59..e3a68b8 100644

//Synthetic comment -- @@ -59,6 +59,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.io.IAbstractFile;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
//Synthetic comment -- @@ -85,8 +86,10 @@
import org.eclipse.ui.progress.UIJob;
import org.w3c.dom.Document;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

/**
//Synthetic comment -- @@ -152,6 +155,7 @@
private int mX;
private int mY;
private double mScale = 1.0;

/** If non null, points to a separate file containing the source */
private @Nullable IFile mInput;
//Synthetic comment -- @@ -203,6 +207,7 @@
mConfiguration = configuration;
mWidth = width;
mHeight = height;
}

/**
//Synthetic comment -- @@ -220,15 +225,20 @@
* @param scale the factor to scale the thumbnail picture by
*/
public void setScale(double scale) {
        Image thumbnail = mThumbnail;
        mThumbnail = null;
        if (thumbnail != null) {
            thumbnail.dispose();
        }
mScale = scale;
}

/**
* Returns whether the preview is actively hovered
*
* @return whether the mouse is hovering over the preview
//Synthetic comment -- @@ -277,6 +287,15 @@
}

/**
* Sets whether the preview is visible. Previews that are off
* screen are typically marked invisible during layout, which means we don't
* have to expend effort computing preview thumbnails etc
//Synthetic comment -- @@ -289,10 +308,13 @@
if (mVisible) {
if (mDirty != 0) {
// Just made the render preview visible:
                    configurationChanged(mDirty);
} else {
updateForkStatus();
}
}
}
}
//Synthetic comment -- @@ -404,10 +426,7 @@
* of image resources etc
*/
public void dispose() {
        if (mThumbnail != null) {
            mThumbnail.dispose();
            mThumbnail = null;
        }

if (mJob != null) {
mJob.cancel();
//Synthetic comment -- @@ -415,6 +434,14 @@
}
}

/**
* Returns the display name of this preview
*
//Synthetic comment -- @@ -482,10 +509,7 @@

/** Render immediately */
private void renderSync() {
        if (mThumbnail != null) {
            mThumbnail.dispose();
            mThumbnail = null;
        }

GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ResourceResolver resolver = getResourceResolver();
//Synthetic comment -- @@ -522,6 +546,7 @@
Document document = DomUtilities.getDocument(mInput);
if (document == null) {
mError = true;
return;
}
model.loadFromXmlNode(document);
//Synthetic comment -- @@ -558,9 +583,13 @@
if (render.isSuccess()) {
BufferedImage image = session.getImage();
if (image != null) {
                setFullImage(image);
}
}
}

private ResourceResolver getResourceResolver() {
//Synthetic comment -- @@ -626,13 +655,12 @@
*
* @param image the full size image
*/
    void setFullImage(BufferedImage image) {
if (image == null) {
mThumbnail = null;
return;
}

        //double scale = getScale(image);
double scale = getWidth() / (double) image.getWidth();
if (scale < 1.0) {
if (LARGE_SHADOWS) {
//Synthetic comment -- @@ -664,6 +692,33 @@
true /* transferAlpha */, -1);
}

private static double getScale(int width, int height) {
int maxWidth = RenderPreviewManager.getMaxWidth();
int maxHeight = RenderPreviewManager.getMaxHeight();
//Synthetic comment -- @@ -685,7 +740,7 @@
* @return the width in pixels
*/
public int getWidth() {
        return (int) (mScale * mWidth);
}

/**
//Synthetic comment -- @@ -694,7 +749,7 @@
* @return the height in pixels
*/
public int getHeight() {
        return (int) (mScale * mHeight);
}

/**
//Synthetic comment -- @@ -774,35 +829,57 @@
* @param y the y coordinate to paint the preview at
*/
void paint(GC gc, int x, int y) {
        if (mThumbnail != null) {
gc.drawImage(mThumbnail, x, y);

if (mActive) {
int oldWidth = gc.getLineWidth();
gc.setLineWidth(3);
gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_LIST_SELECTION));
                gc.drawRectangle(x - 1, y - 1, getWidth() + 2, getHeight() + 2);
gc.setLineWidth(oldWidth);
}
} else if (mError) {
            gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WIDGET_BORDER));
            gc.drawRectangle(x, y, getWidth(), getHeight());
Image icon = IconFactory.getInstance().getIcon("renderError"); //$NON-NLS-1$
ImageData data = icon.getImageData();
int prevAlpha = gc.getAlpha();
            gc.setAlpha(128-32);
            gc.drawImage(icon, x + (getWidth() - data.width) / 2,
                    y + (getHeight() - data.height) / 2);
gc.setAlpha(prevAlpha);
} else {
gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WIDGET_BORDER));
            gc.drawRectangle(x, y, getWidth(), getHeight());
Image icon = IconFactory.getInstance().getIcon("refreshPreview"); //$NON-NLS-1$
ImageData data = icon.getImageData();
int prevAlpha = gc.getAlpha();
gc.setAlpha(128-32);
            gc.drawImage(icon, x + (getWidth() - data.width) / 2,
                    y + (getHeight() - data.height) / 2);
gc.setAlpha(prevAlpha);
}

//Synthetic comment -- @@ -812,7 +889,7 @@
gc.setAlpha(128+32);
Color bg = mCanvas.getDisplay().getSystemColor(SWT.COLOR_WHITE);
gc.setBackground(bg);
            gc.fillRectangle(left, y, x + getWidth() - left, HEADER_HEIGHT);
gc.setAlpha(prevAlpha);

// Paint icons
//Synthetic comment -- @@ -935,9 +1012,12 @@
int temp = mHeight;
mHeight = mWidth;
mWidth = temp;
}

mDirty = 0;
}

/**
//Synthetic comment -- @@ -1069,4 +1149,23 @@
public ConfigurationDescription getDescription() {
return mDescription;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java
//Synthetic comment -- index 593dbc4..9d36c9a 100644

//Synthetic comment -- @@ -22,6 +22,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.DeviceConfigHelper;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -29,6 +30,7 @@
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ComplementingConfiguration;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationChooser;
//Synthetic comment -- @@ -52,6 +54,7 @@
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.ScrollBar;

//Synthetic comment -- @@ -60,20 +63,24 @@
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* Manager for the configuration previews, which handles layout computations,
* managing the image buffer cache, etc
*/
public class RenderPreviewManager {
private static double sScale = 1.0;
    private static final int RENDER_DELAY = 100;
private static final int PREVIEW_VGAP = 18;
private static final int PREVIEW_HGAP = 12;
private static final int MAX_WIDTH = 200;
private static final int MAX_HEIGHT = MAX_WIDTH;
private @Nullable List<RenderPreview> mPreviews;
private @Nullable RenderPreviewList mManualList;
private final @NonNull LayoutCanvas mCanvas;
//Synthetic comment -- @@ -87,12 +94,15 @@
private @Nullable RenderPreview mActivePreview;
private @Nullable ScrollBarListener mListener;
private int mLayoutHeight;
    private int mMaxVisibleY;
/** Last seen state revision in this {@link RenderPreviewManager}. If less
* than {@link #sRevision}, the previews need to be updated on next exposure */
private static int mRevision;
/** Current global revision count */
private static int sRevision;

/**
* Creates a {@link RenderPreviewManager} associated with the given canvas
//Synthetic comment -- @@ -154,19 +164,26 @@
updatedZoom();
}

private void updatedZoom() {
if (hasPreviews()) {
for (RenderPreview preview : mPreviews) {
                preview.setScale(sScale);
}
RenderPreview preview = mCanvas.getPreview();
if (preview != null) {
                preview.setScale(sScale);
}
}

        renderPreviews();
        layout(true);
mCanvas.redraw();
}

//Synthetic comment -- @@ -178,6 +195,10 @@
return (int) sScale * MAX_HEIGHT;
}

/** Delete all the previews */
public void deleteManualPreviews() {
disposePreviews();
//Synthetic comment -- @@ -242,43 +263,28 @@

/**
* Layout Algorithm. This sets the {@link RenderPreview#getX()} and
     * {@link RenderPreview#getY()} coordinates of all the previews. It also marks
     * previews as visible or invisible via {@link RenderPreview#setVisible(boolean)}
     * according to their position and the current visible view port in the layout canvas.
     * Finally, it also sets the {@code mMaxVisibleY} and {@code mLayoutHeight} fields,
     * such that the scrollbars can compute the right scrolled area, and that scrolling
     * can cause render refreshes on views that are made visible.
     *
* <p>
     * Two shapes to fill. The screen is typically wide. When showing a phone,
     * I should use all the vertical space; I then show thumbnails on the right.
     * When showing the tablet, I need to do something in between. I reserve at least
     * 200 pixels either on the right or on the bottom and use the remainder.
     * TODO: Look up better algorithms. Optimal space division algorithm. Can prune etc.
* <p>
     * This is not a traditional bin packing problem, because the objects to be packaged
     * do not have a fixed size; we can scale them up and down in order to provide an
     * "optimal" size.
     * <p>
     * See http://en.wikipedia.org/wiki/Packing_problem
     * See http://en.wikipedia.org/wiki/Bin_packing_problem
     * <p>
     * Returns true if the layout changed (so a redraw is desired)
*/
    boolean layout(boolean refresh) {
if (mPreviews == null || mPreviews.isEmpty()) {
            return false;
}

        if (mListener == null) {
            mListener = new ScrollBarListener();
            mCanvas.getVerticalBar().addSelectionListener(mListener);
        }

        // TODO: Separate layout heuristics for portrait and landscape orientations (though
        // it also depends on the dimensions of the canvas window, which determines the
        // shape of the leftover space)

int scaledImageWidth = mHScale.getScaledImgSize();
int scaledImageHeight = mVScale.getScaledImgSize();
Rectangle clientArea = mCanvas.getClientArea();
//Synthetic comment -- @@ -289,7 +295,7 @@
&& clientArea.width == mPrevCanvasWidth
&& clientArea.height == mPrevCanvasHeight)) {
// No change
            return false;
}

mPrevImageWidth = scaledImageWidth;
//Synthetic comment -- @@ -297,6 +303,41 @@
mPrevCanvasWidth = clientArea.width;
mPrevCanvasHeight = clientArea.height;

int availableWidth = clientArea.x + clientArea.width - getX();
int availableHeight = clientArea.y + clientArea.height - getY();
int maxVisibleY = clientArea.y + clientArea.height;
//Synthetic comment -- @@ -319,7 +360,11 @@
}
}

        for (RenderPreview preview : mPreviews) {
if (x > 0 && x + preview.getWidth() > availableWidth) {
x = rightHandSide;
int prevY = y;
//Synthetic comment -- @@ -362,10 +407,9 @@

preview.setPosition(x, y);

            if (y > maxVisibleY) {
preview.setVisible(false);
} else if (!preview.isVisible()) {
                preview.render(RENDER_DELAY);
preview.setVisible(true);
}

//Synthetic comment -- @@ -375,12 +419,119 @@
}

mLayoutHeight = nextY;
        mMaxVisibleY = maxVisibleY;
        mCanvas.updateScrollBars();

return true;
}

/**
* Paints the configuration previews
*
//Synthetic comment -- @@ -389,7 +540,15 @@
void paint(GC gc) {
if (hasPreviews()) {
// Ensure up to date at all times; consider moving if it's too expensive
            layout(false);
int rootX = getX();
int rootY = getY();

//Synthetic comment -- @@ -415,6 +574,23 @@
int y = destY + destHeight - preview.getHeight();
preview.paintTitle(gc, x, y, false /*showFile*/);
}
} else if (mMode == RenderPreviewMode.CUSTOM) {
int rootX = getX();
rootX += mHScale.getScaledImgSize();
//Synthetic comment -- @@ -426,6 +602,10 @@
gc.drawText("Add previews with \"Add as Thumbnail\"\nin the configuration menu",
rootX, rootY, true);
}
}

private void addPreview(@NonNull RenderPreview preview) {
//Synthetic comment -- @@ -460,7 +640,8 @@
addPreview(preview);

layout(true);
            preview.render(RENDER_DELAY);
mCanvas.setFitScale(true /* onlyZoomOut */, false /*allowZoomIn*/);

if (mManualList == null) {
//Synthetic comment -- @@ -647,11 +828,13 @@
assert false : mMode;
}

        layout(true);
        renderPreviews();
        boolean allowZoomIn = mMode == RenderPreviewMode.NONE;
        mCanvas.setFitScale(true /*onlyZoomOut*/, allowZoomIn);
        mCanvas.updateScrollBars();

return true;
}
//Synthetic comment -- @@ -883,20 +1066,16 @@
public void configurationChanged(int flags) {
// Similar to renderPreviews, but only acts on incomplete previews
if (hasPreviews()) {
            long delay = 0;
// Do zoomed images first
for (RenderPreview preview : mPreviews) {
if (preview.getScale() > 1.2) {
preview.configurationChanged(flags);
                    delay += RENDER_DELAY;
                    preview.render(delay);
}
}
for (RenderPreview preview : mPreviews) {
if (preview.getScale() <= 1.2) {
preview.configurationChanged(flags);
                    delay += RENDER_DELAY;
                    preview.render(delay);
}
}
RenderPreview preview = mCanvas.getPreview();
//Synthetic comment -- @@ -904,7 +1083,7 @@
preview.configurationChanged(flags);
preview.dispose();
}
            layout(true);
mCanvas.redraw();
}
}
//Synthetic comment -- @@ -912,22 +1091,49 @@
/** Updates the configuration preview thumbnails */
public void renderPreviews() {
if (hasPreviews()) {
            long delay = 0;
// Do zoomed images first
            for (RenderPreview preview : mPreviews) {
if (preview.getScale() > 1.2 && preview.isVisible()) {
                    delay += RENDER_DELAY;
                    preview.render(delay);
}
}
// Non-zoomed images
            for (RenderPreview preview : mPreviews) {
if (preview.getScale() <= 1.2 && preview.isVisible()) {
                    delay += RENDER_DELAY;
                    preview.render(delay);
}
}
}
}

/**
//Synthetic comment -- @@ -961,12 +1167,24 @@

// Switch main editor to the clicked configuration preview
mCanvas.setPreview(preview);
        chooser.setConfiguration(preview.getConfiguration());
editor.recomputeLayout();
mCanvas.getVerticalBar().setSelection(mCanvas.getVerticalBar().getMinimum());
        mCanvas.setFitScale(true /*onlyZoomOut*/, false /*allowZoomIn*/);
        layout(true);
mCanvas.redraw();
}

/**
//Synthetic comment -- @@ -1003,6 +1221,22 @@
return mVScale.translate(0);
}

/**
* Returns the height of the layout
*
//Synthetic comment -- @@ -1062,6 +1296,24 @@
* @return true if the click occurred over a preview and was handled, false otherwise
*/
public boolean click(ControlPoint mousePos) {
RenderPreview preview = getPreview(mousePos);
if (preview != null) {
boolean handled = preview.click(mousePos.x - getX() - preview.getX(),
//Synthetic comment -- @@ -1160,11 +1412,9 @@
int selection = bar.getSelection();
int thumb = bar.getThumb();
int maxY = selection + thumb;
            if (maxY > mMaxVisibleY) {
            }
for (RenderPreview preview : mPreviews) {
if (!preview.isVisible() && preview.getY() <= maxY) {
                    preview.render(RENDER_DELAY);
preview.setVisible(true);
}
}
//Synthetic comment -- @@ -1174,4 +1424,51 @@
public void widgetDefaultSelected(SelectionEvent e) {
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/api/RectTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/api/RectTest.java
//Synthetic comment -- index 784ad8f..2f2c59a 100755

//Synthetic comment -- @@ -132,7 +132,13 @@
public final void testContainsPoint_Null() {
// contains(null) returns false rather than an NPE
Rect r = new Rect(3, 4, -20, -30);
        assertFalse(r.contains(null));
}

public final void testContainsPoint() {
//Synthetic comment -- @@ -152,6 +158,29 @@
assertFalse(r.contains(new Point(3,    4+30)));
}

public final void testMoveTo() {
Rect r = new Rect(3, 4, 20, 30);
Rect r2 = r.moveTo(100, 200);








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/Rect.java b/rule_api/src/com/android/ide/common/api/Rect.java
//Synthetic comment -- index 0fb791b..88c04a6 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.common.api;

import com.google.common.annotations.Beta;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;


/**
//Synthetic comment -- @@ -81,11 +81,39 @@

/** Returns true if the rectangle contains the x,y coordinates, borders included. */
public boolean contains(int x, int y) {
        return isValid() &&
            x >= this.x &&
            y >= this.y &&
            x < (this.x + this.w) &&
            y < (this.y + this.h);
}

/** Returns true if the rectangle contains the x,y coordinates, borders included. */







