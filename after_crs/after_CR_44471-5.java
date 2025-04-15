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
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.annotations.Nullable;
import com.android.ide.common.api.Rect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * This class implements 2D bin packing: packing rectangles into a given area as
 * tightly as "possible" (bin packing in general is NP hard, so this class uses
 * heuristics).
 * <p>
 * The algorithm implemented is to keep a set of (possibly overlapping)
 * available areas for placement. For each newly inserted rectangle, we first
 * pick which available space to occupy, and we then subdivide the
 * current rectangle into all the possible remaining unoccupied sub-rectangles.
 * We also remove any other space rectangles which are no longer eligible if
 * they are intersecting the newly placed rectangle.
 * <p>
 * This algorithm is not very fast, so should not be used for a large number of
 * rectangles.
 */
class BinPacker {
    /**
     * When enabled, the successive passes are dumped as PNG images showing the
     * various available and occupied rectangles)
     */
    private static final boolean DEBUG = false;

    private final List<Rect> mSpace = new ArrayList<Rect>();
    private final int mMinHeight;
    private final int mMinWidth;

    /**
     * Creates a new {@linkplain BinPacker}. To use it, first add one or more
     * initial available space rectangles with {@link #addSpace(Rect)}, and then
     * place the rectangles with {@link #occupy(int, int)}. The returned
     * {@link Rect} from {@link #occupy(int, int)} gives the coordinates of the
     * positioned rectangle.
     *
     * @param minWidth the smallest width of any rectangle placed into this bin
     * @param minHeight the smallest height of any rectangle placed into this bin
     */
    BinPacker(int minWidth, int minHeight) {
        mMinWidth = minWidth;
        mMinHeight = minHeight;

        if (DEBUG) {
            mAllocated = new ArrayList<Rect>();
            sLayoutId++;
            sRectId = 1;
        }
    }

    /** Adds more available space */
    void addSpace(Rect rect) {
        if (rect.w >= mMinWidth && rect.h >= mMinHeight) {
            mSpace.add(rect);
        }
    }

    /** Attempts to place a rectangle of the given dimensions, if possible */
    @Nullable
    Rect occupy(int width, int height) {
        int index = findBest(width, height);
        if (index == -1) {
            return null;
        }

        return split(index, width, height);
    }

    /**
     * Finds the best available space rectangle to position a new rectangle of
     * the given size in.
     */
    private int findBest(int width, int height) {
        if (mSpace.isEmpty()) {
            return -1;
        }

        // Try to pack as far up as possible first
        int bestIndex = -1;
        boolean multipleAtSameY = false;
        int minY = Integer.MAX_VALUE;
        for (int i = 0, n = mSpace.size(); i < n; i++) {
            Rect rect = mSpace.get(i);
            if (rect.y <= minY) {
                if (rect.w >= width && rect.h >= height) {
                    if (rect.y < minY) {
                        minY = rect.y;
                        multipleAtSameY = false;
                        bestIndex = i;
                    } else if (minY == rect.y) {
                        multipleAtSameY = true;
                    }
                }
            }
        }

        if (!multipleAtSameY) {
            return bestIndex;
        }

        bestIndex = -1;

        // Pick a rectangle. This currently tries to find the rectangle whose shortest
        // side most closely matches the placed rectangle's size.
        // Attempt to find the best short side fit
        int bestShortDistance = Integer.MAX_VALUE;
        int bestLongDistance = Integer.MAX_VALUE;

        for (int i = 0, n = mSpace.size(); i < n; i++) {
            Rect rect = mSpace.get(i);
            if (rect.y != minY) { // Only comparing elements at same y
                continue;
            }
            if (rect.w >= width && rect.h >= height) {
                if (width < height) {
                    int distance = rect.w - width;
                    if (distance < bestShortDistance ||
                            distance == bestShortDistance &&
                            (rect.h - height) < bestLongDistance) {
                        bestShortDistance = distance;
                        bestLongDistance = rect.h - height;
                        bestIndex = i;
                    }
                } else {
                    int distance = rect.w - width;
                    if (distance < bestShortDistance ||
                            distance == bestShortDistance &&
                            (rect.h - height) < bestLongDistance) {
                        bestShortDistance = distance;
                        bestLongDistance = rect.h - height;
                        bestIndex = i;
                    }
                }
            }
        }

        return bestIndex;
    }

    /**
     * Removes the rectangle at the given index. Since the rectangles are in an
     * {@link ArrayList}, removing a rectangle in the normal way is slow (it
     * would involve shifting all elements), but since we don't care about
     * order, this always swaps the to-be-deleted element to the last position
     * in the array first, <b>then</b> it deletes it (which should be
     * immediate).
     *
     * @param index the index in the {@link #mSpace} list to remove a rectangle
     *            from
     */
    private void removeRect(int index) {
        assert !mSpace.isEmpty();
        int lastIndex = mSpace.size() - 1;
        if (index != lastIndex) {
            // Swap before remove to make deletion faster since we don't
            // care about order
            Rect temp = mSpace.get(index);
            mSpace.set(index, mSpace.get(lastIndex));
            mSpace.set(lastIndex, temp);
        }

        mSpace.remove(lastIndex);
    }

    /**
     * Splits the rectangle at the given rectangle index such that it can contain
     * a rectangle of the given width and height. */
    private Rect split(int index, int width, int height) {
        Rect rect = mSpace.get(index);
        assert rect.w >= width && rect.h >= height : rect;

        Rect r = new Rect(rect);
        r.w = width;
        r.h = height;

        // Remove all rectangles that intersect my rectangle
        for (int i = 0; i < mSpace.size(); i++) {
            Rect other = mSpace.get(i);
            if (other.intersects(r)) {
                removeRect(i);
                i--;
            }
        }


        // Split along vertical line x = rect.x + width:
        // (rect.x,rect.y)
        //     +-------------+-------------------------+
        //     |             |                         |
        //     |             |                         |
        //     |             | height                  |
        //     |             |                         |
        //     |             |                         |
        //     +-------------+           B             | rect.h
        //     |   width                               |
        //     |             |                         |
        //     |      A                                |
        //     |             |                         |
        //     |                                       |
        //     +---------------------------------------+
        //                    rect.w
        int remainingHeight = rect.h - height;
        int remainingWidth = rect.w - width;
        if (remainingHeight >= mMinHeight) {
            mSpace.add(new Rect(rect.x, rect.y + height, width, remainingHeight));
        }
        if (remainingWidth >= mMinWidth) {
            mSpace.add(new Rect(rect.x + width, rect.y, remainingWidth, rect.h));
        }

        // Split along horizontal line y = rect.y + height:
        //     +-------------+-------------------------+
        //     |             |                         |
        //     |             | height                  |
        //     |             |          A              |
        //     |             |                         |
        //     |             |                         | rect.h
        //     +-------------+ - - - - - - - - - - - - |
        //     |  width                                |
        //     |                                       |
        //     |                B                      |
        //     |                                       |
        //     |                                       |
        //     +---------------------------------------+
        //                   rect.w
        if (remainingHeight >= mMinHeight) {
            mSpace.add(new Rect(rect.x, rect.y + height, rect.w, remainingHeight));
        }
        if (remainingWidth >= mMinWidth) {
            mSpace.add(new Rect(rect.x + width, rect.y, remainingWidth, height));
        }

        // Remove redundant rectangles. This is not very efficient.
        for (int i = 0; i < mSpace.size() - 1; i++) {
            for (int j = i + 1; j < mSpace.size(); j++) {
                Rect iRect = mSpace.get(i);
                Rect jRect = mSpace.get(j);
                if (jRect.contains(iRect)) {
                    removeRect(i);
                    i--;
                    break;
                }
                if (iRect.contains(jRect)) {
                    removeRect(j);
                    j--;
                }
            }
        }

        if (DEBUG) {
            mAllocated.add(r);
            dumpImage();
        }

        return r;
    }

    // DEBUGGING CODE: Enable with DEBUG

    private List<Rect> mAllocated;
    private static int sLayoutId;
    private static int sRectId;
    private void dumpImage() {
        if (DEBUG) {
            int width = 100;
            int height = 100;
            for (Rect rect : mSpace) {
                width = Math.max(width, rect.w);
                height = Math.max(height, rect.h);
            }
            width += 10;
            height += 10;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());

            Color[] colors = new Color[] {
                    Color.blue, Color.cyan,
                    Color.green, Color.magenta, Color.orange,
                    Color.pink, Color.red, Color.white, Color.yellow, Color.darkGray,
                    Color.lightGray, Color.gray,
            };

            char allocated = 'A';
            for (Rect rect : mAllocated) {
                Color color = new Color(0x9FFFFFFF, true);
                g.setColor(color);
                g.setBackground(color);
                g.fillRect(rect.x, rect.y, rect.w, rect.h);
                g.setColor(Color.WHITE);
                g.drawRect(rect.x, rect.y, rect.w, rect.h);
                g.drawString("" + (allocated++),
                        rect.x + rect.w / 2, rect.y + rect.h / 2);
            }

            int colorIndex = 0;
            for (Rect rect : mSpace) {
                Color color = colors[colorIndex];
                colorIndex = (colorIndex + 1) % colors.length;

                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 128);
                g.setColor(color);

                g.fillRect(rect.x, rect.y, rect.w, rect.h);
                g.setColor(Color.WHITE);
                g.drawString(Integer.toString(colorIndex),
                        rect.x + rect.w / 2, rect.y + rect.h / 2);
            }


            g.dispose();

            File file = new File("/tmp/layout" + sLayoutId + "_pass" + sRectId + ".png");
            try {
                ImageIO.write(image, "PNG", file);
                System.out.println("Wrote diagnostics image " + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sRectId++;
        }
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index aeafa6d..b5bc9aa 100644

//Synthetic comment -- @@ -671,8 +671,14 @@
null);
}

    /**
     * Reads the given image from the plugin folder
     *
     * @param name the name of the image (including file extension)
     * @return the corresponding image, or null if something goes wrong
     */
@Nullable
    public static BufferedImage readImage(@NonNull String name) {
InputStream stream = ImageUtils.class.getResourceAsStream("/icons/" + name); //$NON-NLS-1$
if (stream != null) {
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index cb55b59..e3a68b8 100644

//Synthetic comment -- @@ -59,6 +59,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.io.IAbstractFile;
import com.android.resources.Density;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
//Synthetic comment -- @@ -85,8 +86,10 @@
import org.eclipse.ui.progress.UIJob;
import org.w3c.dom.Document;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Comparator;
import java.util.Map;

/**
//Synthetic comment -- @@ -152,6 +155,7 @@
private int mX;
private int mY;
private double mScale = 1.0;
    private double mAspectRatio;

/** If non null, points to a separate file containing the source */
private @Nullable IFile mInput;
//Synthetic comment -- @@ -203,6 +207,7 @@
mConfiguration = configuration;
mWidth = width;
mHeight = height;
        mAspectRatio = mWidth / (double) mHeight;
}

/**
//Synthetic comment -- @@ -220,15 +225,20 @@
* @param scale the factor to scale the thumbnail picture by
*/
public void setScale(double scale) {
        disposeThumbnail();
mScale = scale;
}

/**
     * Returns the aspect ratio of this render preview
     *
     * @return the aspect ratio
     */
    public double getAspectRatio() {
        return mAspectRatio;
    }

    /**
* Returns whether the preview is actively hovered
*
* @return whether the mouse is hovering over the preview
//Synthetic comment -- @@ -277,6 +287,15 @@
}

/**
     * Returns the area of this render preview, PRIOR to scaling
     *
     * @return the area (width times height without scaling)
     */
    int getArea() {
        return mWidth * mHeight;
    }

    /**
* Sets whether the preview is visible. Previews that are off
* screen are typically marked invisible during layout, which means we don't
* have to expend effort computing preview thumbnails etc
//Synthetic comment -- @@ -289,10 +308,13 @@
if (mVisible) {
if (mDirty != 0) {
// Just made the render preview visible:
                    configurationChanged(mDirty); // schedules render
} else {
updateForkStatus();
                    mManager.scheduleRender(this);
}
            } else {
                dispose();
}
}
}
//Synthetic comment -- @@ -404,10 +426,7 @@
* of image resources etc
*/
public void dispose() {
        disposeThumbnail();

if (mJob != null) {
mJob.cancel();
//Synthetic comment -- @@ -415,6 +434,14 @@
}
}

    /** Disposes the thumbnail rendering. */
    void disposeThumbnail() {
        if (mThumbnail != null) {
            mThumbnail.dispose();
            mThumbnail = null;
        }
    }

/**
* Returns the display name of this preview
*
//Synthetic comment -- @@ -482,10 +509,7 @@

/** Render immediately */
private void renderSync() {
        disposeThumbnail();

GraphicalEditorPart editor = mCanvas.getEditorDelegate().getGraphicalEditor();
ResourceResolver resolver = getResourceResolver();
//Synthetic comment -- @@ -522,6 +546,7 @@
Document document = DomUtilities.getDocument(mInput);
if (document == null) {
mError = true;
                createErrorThumbnail();
return;
}
model.loadFromXmlNode(document);
//Synthetic comment -- @@ -558,9 +583,13 @@
if (render.isSuccess()) {
BufferedImage image = session.getImage();
if (image != null) {
                createThumbnail(image);
}
}

        if (mError) {
            createErrorThumbnail();
        }
}

private ResourceResolver getResourceResolver() {
//Synthetic comment -- @@ -626,13 +655,12 @@
*
* @param image the full size image
*/
    void createThumbnail(BufferedImage image) {
if (image == null) {
mThumbnail = null;
return;
}

double scale = getWidth() / (double) image.getWidth();
if (scale < 1.0) {
if (LARGE_SHADOWS) {
//Synthetic comment -- @@ -664,6 +692,33 @@
true /* transferAlpha */, -1);
}

    void createErrorThumbnail() {
        int shadowSize = LARGE_SHADOWS ? SHADOW_SIZE : SMALL_SHADOW_SIZE;
        int width = getWidth();
        int height = getHeight();
        BufferedImage image = new BufferedImage(width + shadowSize, height + shadowSize,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = image.createGraphics();
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.dispose();

        if (LARGE_SHADOWS) {
            ImageUtils.drawRectangleShadow(image, 0, 0,
                    image.getWidth() - SHADOW_SIZE,
                    image.getHeight() - SHADOW_SIZE);
        } else {
            ImageUtils.drawSmallRectangleShadow(image, 0, 0,
                    image.getWidth() - SMALL_SHADOW_SIZE,
                    image.getHeight() - SMALL_SHADOW_SIZE);
        }

        mThumbnail = SwtUtils.convertToSwt(mCanvas.getDisplay(), image,
                true /* transferAlpha */, -1);
    }

private static double getScale(int width, int height) {
int maxWidth = RenderPreviewManager.getMaxWidth();
int maxHeight = RenderPreviewManager.getMaxHeight();
//Synthetic comment -- @@ -685,7 +740,7 @@
* @return the width in pixels
*/
public int getWidth() {
        return (int) (mWidth * mScale * RenderPreviewManager.getScale());
}

/**
//Synthetic comment -- @@ -694,7 +749,7 @@
* @return the height in pixels
*/
public int getHeight() {
        return (int) (mHeight * mScale * RenderPreviewManager.getScale());
}

/**
//Synthetic comment -- @@ -774,35 +829,57 @@
* @param y the y coordinate to paint the preview at
*/
void paint(GC gc, int x, int y) {
        int width = getWidth();
        int height = getHeight();
        if (mThumbnail != null && !mError) {
gc.drawImage(mThumbnail, x, y);

if (mActive) {
int oldWidth = gc.getLineWidth();
gc.setLineWidth(3);
gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_LIST_SELECTION));
                gc.drawRectangle(x - 1, y - 1, width + 2, height + 2);
gc.setLineWidth(oldWidth);
}
} else if (mError) {
            if (mThumbnail != null) {
                gc.drawImage(mThumbnail, x, y);
            } else {
                gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WIDGET_BORDER));
                gc.drawRectangle(x, y, width, height);
            }

            gc.setClipping(x, y, width, height + 100);
Image icon = IconFactory.getInstance().getIcon("renderError"); //$NON-NLS-1$
ImageData data = icon.getImageData();
int prevAlpha = gc.getAlpha();
            int alpha = 128-32;
            if (mThumbnail != null) {
                alpha -= 64;
            }
            gc.setAlpha(alpha);
            gc.drawImage(icon, x + (width - data.width) / 2, y + (height - data.height) / 2);

            Density density = mConfiguration.getDensity();
            if (density == Density.TV || density == Density.LOW) {
                gc.setAlpha(255);
                gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
                gc.drawText("Broken rendering\nlibrary;\nunsupported DPI\n\nTry updating\nSDK platforms",
                        x + 8, y + HEADER_HEIGHT, true);
            }

gc.setAlpha(prevAlpha);
            gc.setClipping((Region) null);
} else {
gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WIDGET_BORDER));
            gc.drawRectangle(x, y, width, height);

Image icon = IconFactory.getInstance().getIcon("refreshPreview"); //$NON-NLS-1$
ImageData data = icon.getImageData();
int prevAlpha = gc.getAlpha();
gc.setAlpha(128-32);
            gc.drawImage(icon, x + (width - data.width) / 2,
                    y + (height - data.height) / 2);
gc.setAlpha(prevAlpha);
}

//Synthetic comment -- @@ -812,7 +889,7 @@
gc.setAlpha(128+32);
Color bg = mCanvas.getDisplay().getSystemColor(SWT.COLOR_WHITE);
gc.setBackground(bg);
            gc.fillRectangle(left, y, x + width - left, HEADER_HEIGHT);
gc.setAlpha(prevAlpha);

// Paint icons
//Synthetic comment -- @@ -935,9 +1012,12 @@
int temp = mHeight;
mHeight = mWidth;
mWidth = temp;
            mAspectRatio = mWidth / (double) mHeight;
}

mDirty = 0;

        mManager.scheduleRender(this);
}

/**
//Synthetic comment -- @@ -1069,4 +1149,23 @@
public ConfigurationDescription getDescription() {
return mDescription;
}

    /** Sorts render previews into increasing aspect ratio order */
    static Comparator<RenderPreview> INCREASING_ASPECT_RATIO = new Comparator<RenderPreview>() {
        @Override
        public int compare(RenderPreview preview1, RenderPreview preview2) {
            return (int) Math.signum(preview1.mAspectRatio - preview2.mAspectRatio);
        }
    };
    /** Sorts render previews into visual order: row by row, column by column */
    static Comparator<RenderPreview> VISUAL_ORDER = new Comparator<RenderPreview>() {
        @Override
        public int compare(RenderPreview preview1, RenderPreview preview2) {
            int delta = preview1.mY - preview2.mY;
            if (delta == 0) {
                delta = preview1.mX - preview2.mX;
            }
            return delta;
        }
    };
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java
//Synthetic comment -- index 593dbc4..9d36c9a 100644

//Synthetic comment -- @@ -22,6 +22,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.Rect;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.DeviceConfigHelper;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -29,6 +30,7 @@
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ComplementingConfiguration;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.Configuration;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationChooser;
//Synthetic comment -- @@ -52,6 +54,7 @@
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.ScrollBar;

//Synthetic comment -- @@ -60,20 +63,24 @@
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
* Manager for the configuration previews, which handles layout computations,
* managing the image buffer cache, etc
*/
public class RenderPreviewManager {
private static double sScale = 1.0;
    private static final int RENDER_DELAY = 150;
private static final int PREVIEW_VGAP = 18;
private static final int PREVIEW_HGAP = 12;
private static final int MAX_WIDTH = 200;
private static final int MAX_HEIGHT = MAX_WIDTH;
    private static final int ZOOM_ICON_WIDTH = 16;
    private static final int ZOOM_ICON_HEIGHT = 16;
private @Nullable List<RenderPreview> mPreviews;
private @Nullable RenderPreviewList mManualList;
private final @NonNull LayoutCanvas mCanvas;
//Synthetic comment -- @@ -87,12 +94,15 @@
private @Nullable RenderPreview mActivePreview;
private @Nullable ScrollBarListener mListener;
private int mLayoutHeight;
/** Last seen state revision in this {@link RenderPreviewManager}. If less
* than {@link #sRevision}, the previews need to be updated on next exposure */
private static int mRevision;
/** Current global revision count */
private static int sRevision;
    private boolean mNeedLayout;
    private boolean mNeedRender;
    private boolean mNeedZoom;
    private SwapAnimation mAnimation;

/**
* Creates a {@link RenderPreviewManager} associated with the given canvas
//Synthetic comment -- @@ -154,19 +164,26 @@
updatedZoom();
}

    /** Zooms to 100 (resets zoom) */
    public void zoomReset() {
        sScale = 1.0;
        updatedZoom();
        mNeedZoom = mNeedLayout = true;
        mCanvas.redraw();
    }

private void updatedZoom() {
if (hasPreviews()) {
for (RenderPreview preview : mPreviews) {
                preview.disposeThumbnail();
}
RenderPreview preview = mCanvas.getPreview();
if (preview != null) {
                preview.disposeThumbnail();
}
}

        mNeedLayout = mNeedRender = true;
mCanvas.redraw();
}

//Synthetic comment -- @@ -178,6 +195,10 @@
return (int) sScale * MAX_HEIGHT;
}

    static double getScale() {
        return sScale;
    }

/** Delete all the previews */
public void deleteManualPreviews() {
disposePreviews();
//Synthetic comment -- @@ -242,43 +263,28 @@

/**
* Layout Algorithm. This sets the {@link RenderPreview#getX()} and
     * {@link RenderPreview#getY()} coordinates of all the previews. It also
     * marks previews as visible or invisible via
     * {@link RenderPreview#setVisible(boolean)} according to their position and
     * the current visible view port in the layout canvas. Finally, it also sets
     * the {@code mLayoutHeight} field, such that the scrollbars can compute the
     * right scrolled area, and that scrolling can cause render refreshes on
     * views that are made visible.
* <p>
     * This is not a traditional bin packing problem, because the objects to be
     * packaged do not have a fixed size; we can scale them up and down in order
     * to provide an "optimal" size.
* <p>
     * See http://en.wikipedia.org/wiki/Packing_problem See
     * http://en.wikipedia.org/wiki/Bin_packing_problem
*/
    void layout(boolean refresh) {
        mNeedLayout = false;

if (mPreviews == null || mPreviews.isEmpty()) {
            return;
}

int scaledImageWidth = mHScale.getScaledImgSize();
int scaledImageHeight = mVScale.getScaledImgSize();
Rectangle clientArea = mCanvas.getClientArea();
//Synthetic comment -- @@ -289,7 +295,7 @@
&& clientArea.width == mPrevCanvasWidth
&& clientArea.height == mPrevCanvasHeight)) {
// No change
            return;
}

mPrevImageWidth = scaledImageWidth;
//Synthetic comment -- @@ -297,6 +303,41 @@
mPrevCanvasWidth = clientArea.width;
mPrevCanvasHeight = clientArea.height;

        if (mListener == null) {
            mListener = new ScrollBarListener();
            mCanvas.getVerticalBar().addSelectionListener(mListener);
        }

        beginRenderScheduling();

        mLayoutHeight = 0;

        if (previewsHaveIdenticalSize() || fixedOrder()) {
            // If all the preview boxes are of identical sizes, or if the order is predetermined,
            // just lay them out in rows.
            rowLayout();
        } else if (previewsFit()) {
            layoutFullFit();
        } else {
            rowLayout();
        }

        mCanvas.updateScrollBars();
    }

    /**
     * Performs a simple layout where the views are laid out in a row, wrapping
     * around the top left canvas image.
     */
    private void rowLayout() {
        // TODO: Separate layout heuristics for portrait and landscape orientations (though
        // it also depends on the dimensions of the canvas window, which determines the
        // shape of the leftover space)

        int scaledImageWidth = mHScale.getScaledImgSize();
        int scaledImageHeight = mVScale.getScaledImgSize();
        Rectangle clientArea = mCanvas.getClientArea();

int availableWidth = clientArea.x + clientArea.width - getX();
int availableHeight = clientArea.y + clientArea.height - getY();
int maxVisibleY = clientArea.y + clientArea.height;
//Synthetic comment -- @@ -319,7 +360,11 @@
}
}

        ArrayList<RenderPreview> aspectOrder = new ArrayList<RenderPreview>(mPreviews);
        Collections.sort(aspectOrder, RenderPreview.INCREASING_ASPECT_RATIO);


        for (RenderPreview preview : aspectOrder) {
if (x > 0 && x + preview.getWidth() > availableWidth) {
x = rightHandSide;
int prevY = y;
//Synthetic comment -- @@ -362,10 +407,9 @@

preview.setPosition(x, y);

            if (y > maxVisibleY && maxVisibleY > 0) {
preview.setVisible(false);
} else if (!preview.isVisible()) {
preview.setVisible(true);
}

//Synthetic comment -- @@ -375,12 +419,119 @@
}

mLayoutHeight = nextY;
    }

    private boolean fixedOrder() {
        // Currently, none of the lists have fixed order. Possibly we could
        // consider mMode == RenderPreviewMode.CUSTOM to be fixed.
        return false;
    }

    /** Returns true if all the previews have the same identical size */
    private boolean previewsHaveIdenticalSize() {
        if (!hasPreviews()) {
            return true;
        }

        Iterator<RenderPreview> iterator = mPreviews.iterator();
        RenderPreview first = iterator.next();
        int width = first.getWidth();
        int height = first.getHeight();

        while (iterator.hasNext()) {
            RenderPreview preview = iterator.next();
            if (width != preview.getWidth() || height != preview.getHeight()) {
                return false;
            }
        }

return true;
}

    /** Returns true if all the previews can fully fit in the available space */
    private boolean previewsFit() {
        int scaledImageWidth = mHScale.getScaledImgSize();
        int scaledImageHeight = mVScale.getScaledImgSize();
        Rectangle clientArea = mCanvas.getClientArea();
        int availableWidth = clientArea.x + clientArea.width - getX();
        int availableHeight = clientArea.y + clientArea.height - getY();
        int bottomBorder = scaledImageHeight;
        int rightHandSide = scaledImageWidth + PREVIEW_HGAP;

        // First see if we can fit everything; if so, we can try to make the layouts
        // larger such that they fill up all the available space
        long availableArea = rightHandSide * bottomBorder +
                availableWidth * (Math.max(0, availableHeight - bottomBorder));

        long requiredArea = 0;
        for (RenderPreview preview : mPreviews) {
            // Note: This does not include individual preview scale; the layout
            // algorithm itself may be tweaking the scales to fit elements within
            // the layout
            requiredArea += preview.getArea();
        }

        return requiredArea * sScale < availableArea;
    }

    private void layoutFullFit() {
        int scaledImageWidth = mHScale.getScaledImgSize();
        int scaledImageHeight = mVScale.getScaledImgSize();
        Rectangle clientArea = mCanvas.getClientArea();
        int availableWidth = clientArea.x + clientArea.width - getX();
        int availableHeight = clientArea.y + clientArea.height - getY();
        int maxVisibleY = clientArea.y + clientArea.height;
        int bottomBorder = scaledImageHeight;
        int rightHandSide = scaledImageWidth + PREVIEW_HGAP;

        int minWidth = Integer.MAX_VALUE;
        int minHeight = Integer.MAX_VALUE;
        for (RenderPreview preview : mPreviews) {
            minWidth = Math.min(minWidth, preview.getWidth());
            minHeight = Math.min(minHeight, preview.getHeight());
        }

        BinPacker packer = new BinPacker(minWidth, minHeight);

        // TODO: Instead of this, just start with client area and occupy scaled image size!

        // Add in gap on right and bottom since we'll add that requirement on the width and
        // height rectangles too (for spacing)
        packer.addSpace(new Rect(rightHandSide, 0,
                availableWidth - rightHandSide + PREVIEW_HGAP,
                availableHeight + PREVIEW_VGAP));
        if (maxVisibleY > bottomBorder) {
            packer.addSpace(new Rect(0, bottomBorder + PREVIEW_VGAP,
                    availableWidth + PREVIEW_HGAP, maxVisibleY - bottomBorder + PREVIEW_VGAP));
        }

        // TODO: Sort previews first before attempting to position them?

        ArrayList<RenderPreview> aspectOrder = new ArrayList<RenderPreview>(mPreviews);
        Collections.sort(aspectOrder, RenderPreview.INCREASING_ASPECT_RATIO);

        for (RenderPreview preview : aspectOrder) {
            int previewWidth = preview.getWidth();
            int previewHeight = preview.getHeight();
            previewHeight += PREVIEW_VGAP;
            if (preview.isForked()) {
                previewHeight += PREVIEW_VGAP;
            }
            previewWidth += PREVIEW_HGAP;
            // title height? how do I account for that?
            Rect position = packer.occupy(previewWidth, previewHeight);
            if (position != null) {
                preview.setPosition(position.x, position.y);
                preview.setVisible(true);
            } else {
                // Can't fit: give up and do plain row layout
                rowLayout();
                return;
            }
        }

        mLayoutHeight = availableHeight;
    }
/**
* Paints the configuration previews
*
//Synthetic comment -- @@ -389,7 +540,15 @@
void paint(GC gc) {
if (hasPreviews()) {
// Ensure up to date at all times; consider moving if it's too expensive
            layout(mNeedLayout);
            if (mNeedRender) {
                renderPreviews();
            }
            if (mNeedZoom) {
                boolean allowZoomIn = true /*mMode == RenderPreviewMode.NONE*/;
                mCanvas.setFitScale(false /*onlyZoomOut*/, allowZoomIn);
                mNeedZoom = false;
            }
int rootX = getX();
int rootY = getY();

//Synthetic comment -- @@ -415,6 +574,23 @@
int y = destY + destHeight - preview.getHeight();
preview.paintTitle(gc, x, y, false /*showFile*/);
}

            // Zoom overlay
            int x = getZoomX();
            if (x > 0) {
                int y = getZoomY();
                IconFactory iconFactory = IconFactory.getInstance();
                Image zoomOut = iconFactory.getIcon("zoomminus"); //$NON-NLS-1$);
                Image zoomIn = iconFactory.getIcon("zoomplus"); //$NON-NLS-1$);
                Image zoom100 = iconFactory.getIcon("zoom100"); //$NON-NLS-1$);

                gc.drawImage(zoomIn, x, y);
                y += ZOOM_ICON_HEIGHT;
                gc.drawImage(zoomOut, x, y);
                y += ZOOM_ICON_HEIGHT;
                gc.drawImage(zoom100, x, y);
                y += ZOOM_ICON_HEIGHT;
            }
} else if (mMode == RenderPreviewMode.CUSTOM) {
int rootX = getX();
rootX += mHScale.getScaledImgSize();
//Synthetic comment -- @@ -426,6 +602,10 @@
gc.drawText("Add previews with \"Add as Thumbnail\"\nin the configuration menu",
rootX, rootY, true);
}

        if (mAnimation != null) {
            mAnimation.tick(gc);
        }
}

private void addPreview(@NonNull RenderPreview preview) {
//Synthetic comment -- @@ -460,7 +640,8 @@
addPreview(preview);

layout(true);
            beginRenderScheduling();
            scheduleRender(preview);
mCanvas.setFitScale(true /* onlyZoomOut */, false /*allowZoomIn*/);

if (mManualList == null) {
//Synthetic comment -- @@ -647,11 +828,13 @@
assert false : mMode;
}

        // We schedule layout for the next redraw rather than process it here immediately;
        // not only does this let us avoid doing work for windows where the tab is in the
        // background, but when a file is opened we may not know the size of the canvas
        // yet, and the layout methods need it in order to do a good job. By the time
        // the canvas is painted, we have accurate bounds.
        mNeedLayout = mNeedRender = true;
        mCanvas.redraw();

return true;
}
//Synthetic comment -- @@ -883,20 +1066,16 @@
public void configurationChanged(int flags) {
// Similar to renderPreviews, but only acts on incomplete previews
if (hasPreviews()) {
// Do zoomed images first
            beginRenderScheduling();
for (RenderPreview preview : mPreviews) {
if (preview.getScale() > 1.2) {
preview.configurationChanged(flags);
}
}
for (RenderPreview preview : mPreviews) {
if (preview.getScale() <= 1.2) {
preview.configurationChanged(flags);
}
}
RenderPreview preview = mCanvas.getPreview();
//Synthetic comment -- @@ -904,7 +1083,7 @@
preview.configurationChanged(flags);
preview.dispose();
}
            mNeedLayout = true;
mCanvas.redraw();
}
}
//Synthetic comment -- @@ -912,22 +1091,49 @@
/** Updates the configuration preview thumbnails */
public void renderPreviews() {
if (hasPreviews()) {
            beginRenderScheduling();

            // Process in visual order
            ArrayList<RenderPreview> visualOrder = new ArrayList<RenderPreview>(mPreviews);
            Collections.sort(visualOrder, RenderPreview.VISUAL_ORDER);

// Do zoomed images first
            for (RenderPreview preview : visualOrder) {
if (preview.getScale() > 1.2 && preview.isVisible()) {
                    scheduleRender(preview);
}
}
// Non-zoomed images
            for (RenderPreview preview : visualOrder) {
if (preview.getScale() <= 1.2 && preview.isVisible()) {
                    scheduleRender(preview);
}
}
}

        mNeedRender = false;
    }

    private int mPendingRenderCount;

    /**
     * Reset rendering scheduling. The next render request will be scheduled
     * after a single delay unit.
     */
    public void beginRenderScheduling() {
        mPendingRenderCount = 0;
    }

    /**
     * Schedule rendering the given preview. Each successive call will add an additional
     * delay unit to the schedule from the previous {@link #scheduleRender(RenderPreview)}
     * call, until {@link #beginRenderScheduling()} is called again.
     *
     * @param preview the preview to render
     */
    public void scheduleRender(@NonNull RenderPreview preview) {
        mPendingRenderCount++;
        preview.render(mPendingRenderCount * RENDER_DELAY);
}

/**
//Synthetic comment -- @@ -961,12 +1167,24 @@

// Switch main editor to the clicked configuration preview
mCanvas.setPreview(preview);

        Configuration newConfiguration = preview.getConfiguration();
        if (newConfiguration instanceof NestedConfiguration) {
            // Should never use a complementing configuration for the main
            // rendering's configuration; instead, create a new configuration
            // with a snapshot of the configuration's current values
            newConfiguration = Configuration.copy(preview.getConfiguration());
        }
        chooser.setConfiguration(newConfiguration);

editor.recomputeLayout();

        // Scroll to the top again, if necessary
mCanvas.getVerticalBar().setSelection(mCanvas.getVerticalBar().getMinimum());

        mNeedLayout = mNeedZoom = true;
mCanvas.redraw();
        mAnimation = new SwapAnimation(preview);
}

/**
//Synthetic comment -- @@ -1003,6 +1221,22 @@
return mVScale.translate(0);
}

    private int getZoomX() {
        Rectangle clientArea = mCanvas.getClientArea();
        int x = clientArea.x + clientArea.width - ZOOM_ICON_WIDTH;
        if (x < mHScale.getScaledImgSize() + PREVIEW_HGAP) {
            // No visible previews because the main image is zoomed too far
            return -1;
        }

        return x - 6;
    }

    private int getZoomY() {
        Rectangle clientArea = mCanvas.getClientArea();
        return clientArea.y + 3;
    }

/**
* Returns the height of the layout
*
//Synthetic comment -- @@ -1062,6 +1296,24 @@
* @return true if the click occurred over a preview and was handled, false otherwise
*/
public boolean click(ControlPoint mousePos) {
        // Clicked zoom?
        int x = getZoomX();
        if (x > 0) {
            if (mousePos.x >= x && mousePos.x <= x + ZOOM_ICON_WIDTH) {
                int y = getZoomY();
                if (mousePos.y >= y && mousePos.y <= y + 3 * ZOOM_ICON_HEIGHT) {
                    if (mousePos.y < y + ZOOM_ICON_HEIGHT) {
                        zoomIn();
                    } else if (mousePos.y < y + 2 * ZOOM_ICON_HEIGHT) {
                        zoomOut();
                    } else {
                        zoomReset();
                    }
                }
            }
            return true;
        }

RenderPreview preview = getPreview(mousePos);
if (preview != null) {
boolean handled = preview.click(mousePos.x - getX() - preview.getX(),
//Synthetic comment -- @@ -1160,11 +1412,9 @@
int selection = bar.getSelection();
int thumb = bar.getThumb();
int maxY = selection + thumb;
            beginRenderScheduling();
for (RenderPreview preview : mPreviews) {
if (!preview.isVisible() && preview.getY() <= maxY) {
preview.setVisible(true);
}
}
//Synthetic comment -- @@ -1174,4 +1424,51 @@
public void widgetDefaultSelected(SelectionEvent e) {
}
}

    /** Animation overlay shown briefly after swapping two previews */
    private class SwapAnimation implements Runnable {
        private long begin;
        private long end;
        private static final long DURATION = 400; // ms
        private Rect initialPos;

        SwapAnimation(RenderPreview preview) {
            begin = System.currentTimeMillis();
            end = begin + DURATION;

            initialPos = new Rect(preview.getX(), preview.getY(),
                    preview.getWidth(), preview.getHeight());
        }

        void tick(GC gc) {
            long now = System.currentTimeMillis();
            if (now > end || mCanvas.isDisposed()) {
                mAnimation = null;
                return;
            }

            // For now, just animation rect1 towards rect2
            // The shape of the canvas might have shifted if zoom or device size
            // or orientation changed, so compute a new target size
            CanvasTransform hi = mCanvas.getHorizontalTransform();
            CanvasTransform vi = mCanvas.getVerticalTransform();
            Rect rect = new Rect(hi.translate(0), vi.translate(0),
                    hi.getScaledImgSize(), vi.getScaledImgSize());
            double portion = (now - begin) / (double) DURATION;
            rect.x = (int) (portion * (rect.x - initialPos.x) + initialPos.x);
            rect.y = (int) (portion * (rect.y - initialPos.y) + initialPos.y);
            rect.w = (int) (portion * (rect.w - initialPos.w) + initialPos.w);
            rect.h = (int) (portion * (rect.h - initialPos.h) + initialPos.h);

            gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_GRAY));
            gc.drawRectangle(rect.x, rect.y, rect.w, rect.h);

            mCanvas.getDisplay().timerExec(5, this);
        }

        @Override
        public void run() {
            mCanvas.redraw();
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/api/RectTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/api/RectTest.java
//Synthetic comment -- index 784ad8f..2f2c59a 100755

//Synthetic comment -- @@ -132,7 +132,13 @@
public final void testContainsPoint_Null() {
// contains(null) returns false rather than an NPE
Rect r = new Rect(3, 4, -20, -30);
        assertFalse(r.contains((Point) null));
    }

    public final void testContainsRect_Null() {
        // contains(null) returns false rather than an NPE
        Rect r = new Rect(3, 4, -20, -30);
        assertFalse(r.contains((Rect) null));
}

public final void testContainsPoint() {
//Synthetic comment -- @@ -152,6 +158,29 @@
assertFalse(r.contains(new Point(3,    4+30)));
}

    public final void testContainsRect() {
        Rect r = new Rect(3, 4, 20, 30);

        assertTrue(r.contains(new Rect(3, 4, 5, 10)));
        assertFalse(r.contains(new Rect(3 - 1, 4, 5, 10)));
    }

    public final void testIntersects() {
        Rect r1 = new Rect(0, 0, 10, 10);
        Rect r2 = new Rect(1, 1, 5, 5);
        Rect r3 = new Rect(10, 0, 1, 1);
        Rect r4 = new Rect(5, 5, 10, 10);
        Rect r5 = new Rect(-1, 0, 1, 1);
        Rect r6 = new Rect(0, 10, 1, 1);

        assertTrue(r1.intersects(r2));
        assertTrue(r2.intersects(r1));
        assertTrue(r1.intersects(r4));
        assertFalse(r1.intersects(r3));
        assertFalse(r1.intersects(r5));
        assertFalse(r1.intersects(r6));
    }

public final void testMoveTo() {
Rect r = new Rect(3, 4, 20, 30);
Rect r2 = r.moveTo(100, 200);








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/Rect.java b/rule_api/src/com/android/ide/common/api/Rect.java
//Synthetic comment -- index 0fb791b..88c04a6 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.common.api;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.google.common.annotations.Beta;


/**
//Synthetic comment -- @@ -81,11 +81,39 @@

/** Returns true if the rectangle contains the x,y coordinates, borders included. */
public boolean contains(int x, int y) {
        return isValid()
                && x >= this.x
                && y >= this.y
                && x < (this.x + this.w)
                && y < (this.y + this.h);
    }

    /**
     * Returns true if this rectangle intersects the given rectangle.
     * Two rectangles intersect if they overlap.
     * @param other the other rectangle to test
     * @return true if the two rectangles overlap
     */
    public boolean intersects(@Nullable Rect other) {
        if (other == null) {
            return false;
        }
        if (x2() <= other.x
                || other.x2() <= x
                || y2() <= other.y
                || other.y2() <= y) {
            return false;
        }

        return true;
    }

    /** Returns true if the rectangle fully contains the given rectangle */
    public boolean contains(@Nullable Rect rect) {
        return rect != null && x <= rect.x
                && y <= rect.y
                && x2() >= rect.x2()
                && y2() >= rect.y2();
}

/** Returns true if the rectangle contains the x,y coordinates, borders included. */







