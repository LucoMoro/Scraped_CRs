/*Use view bounds to speed up image cropping

Use the view bounds from the layout result as an initial cropping
size. This saves us from looking at a lot of empty pixels when we
render small views. This also means we can attempt rendering a larger
surface for the preview, which makes accidentally cropping large views
less likely.

(Also fix a couple of other suggestions from previous commit)

Change-Id:I9215f00db14adc42950897637e9f66aa921d484e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GlobalCanvasDragInfo.java
//Synthetic comment -- index ff3645c..ab39563 100755

//Synthetic comment -- @@ -45,7 +45,7 @@
private Object mSourceCanvas = null;
private Runnable mRemoveSourceHandler;

    private ControlPoint mImageOffset;

/** Private constructor. Use {@link #getInstance()} to retrieve the singleton. */
private GlobalCanvasDragInfo() {
//Synthetic comment -- @@ -127,7 +127,7 @@
* @return The image offset, or null if none apply
*/
public ControlPoint getImageOffset() {
        return mImageOffset;
}

/**
//Synthetic comment -- @@ -137,6 +137,6 @@
* @param imageOffset a new offset to apply
*/
public void setImageOffset(ControlPoint imageOffset) {
        this.mImageOffset = imageOffset;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 344f3a0..74f12e7 100755

//Synthetic comment -- @@ -1391,8 +1391,7 @@
if (target != null) {
AndroidTargetData data = Sdk.getCurrent().getTargetData(target);
if (data != null) {
                return data.getLayoutLibrary();
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index d7fdd1e..ec40d53 100755

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.layoutlib.api.LayoutBridge;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.ViewInfo;
import com.android.sdklib.SdkConstants;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -547,10 +548,10 @@
// actually scanning pixels.

/** Width of the rendered preview image (before it is cropped) */
        private static final int RENDER_HEIGHT = 400;

/** Height of the rendered preview image (before it is cropped) */
        private static final int RENDER_WIDTH = 500;

/** Amount of alpha to multiply into the image (divided by 256) */
private static final int IMG_ALPHA = 192;
//Synthetic comment -- @@ -703,11 +704,21 @@
BufferedImage image = result.getImage();
if (image != null) {
BufferedImage cropped;
                    Rect initialCrop = null;
                    ViewInfo viewInfo = result.getRootView();
                    if (viewInfo != null) {
                        int x1 = viewInfo.getLeft();
                        int x2 = viewInfo.getRight();
                        int y2 = viewInfo.getBottom();
                        int y1 = viewInfo.getTop();
                        initialCrop = new Rect(x1, y1, x2 - x1, y2 - y1);
                    }

if (hasTransparency) {
                        cropped = SwtUtils.cropBlank(image, initialCrop);
} else {
int edgeColor = image.getRGB(image.getWidth() - 1, image.getHeight() - 1);
                        cropped = SwtUtils.cropColor(image, edgeColor, initialCrop);
}

if (cropped != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java
//Synthetic comment -- index 05a3d5d..3080716 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.Rect;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
//Synthetic comment -- @@ -91,18 +93,22 @@
* this is not the same as pixels that aren't opaque (an alpha value other than 255).
*
* @param image the image to be cropped
     * @param initialCrop If not null, specifies a rectangle which contains an initial
     *            crop to continue. This can be used to crop an image where you already
     *            know about margins in the image
* @return a cropped version of the source image, or null if the whole image was blank
*         and cropping completely removed everything
*/
    public static BufferedImage cropBlank(BufferedImage image, Rect initialCrop) {
        CropFilter filter = new CropFilter() {
public boolean crop(BufferedImage bufferedImage, int x, int y) {
int rgb = bufferedImage.getRGB(x, y);
return (rgb & 0xFF000000) == 0x00000000;
// TODO: Do a threshold of 80 instead of just 0? Might give better
// visual results -- e.g. check <= 0x80000000
}
        };
        return crop(image, filter, initialCrop);
}

/**
//Synthetic comment -- @@ -112,15 +118,20 @@
* @param image the image to be cropped
* @param blankRgba the color considered to be blank, as a 32 pixel integer with 8
*            bits of alpha, red, green and blue
     * @param initialCrop If not null, specifies a rectangle which contains an initial
     *            crop to continue. This can be used to crop an image where you already
     *            know about margins in the image
* @return a cropped version of the source image, or null if the whole image was blank
*         and cropping completely removed everything
*/
    public static BufferedImage cropColor(BufferedImage image,
            final int blankRgba, Rect initialCrop) {
        CropFilter filter = new CropFilter() {
public boolean crop(BufferedImage bufferedImage, int x, int y) {
return blankRgba == bufferedImage.getRGB(x, y);
}
        };
        return crop(image, filter, initialCrop);
}

/**
//Synthetic comment -- @@ -139,16 +150,29 @@
boolean crop(BufferedImage image, int x, int y);
}

    private static BufferedImage crop(BufferedImage image, CropFilter filter, Rect initialCrop) {
if (image == null) {
return null;
}

// First, determine the dimensions of the real image within the image
        int x1, y1, x2, y2;
        if (initialCrop != null) {
            x1 = initialCrop.x;
            y1 = initialCrop.y;
            x2 = initialCrop.x + initialCrop.w;
            y2 = initialCrop.y + initialCrop.h;
        } else {
            x1 = 0;
            y1 = 0;
            x2 = image.getWidth();
            y2 = image.getHeight();
        }

        // Nothing left to crop
        if (x1 == x2 || y1 == y2) {
            return null;
        }

// This algorithm is a bit dumb -- it just scans along the edges looking for
// a pixel that shouldn't be cropped. I could maybe try to make it smarter by








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java
//Synthetic comment -- index 7ed5f1f..30bb82e 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.Rect;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -35,7 +37,18 @@
g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

        BufferedImage crop = SwtUtils.cropBlank(image, null);
        assertNull(crop);
    }

    public void testCropBlankPre() throws Exception {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0, true));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();

        BufferedImage crop = SwtUtils.cropBlank(image, new Rect(5, 5, 80, 80));
assertNull(crop);
}

//Synthetic comment -- @@ -46,7 +59,7 @@
g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

        BufferedImage crop = SwtUtils.cropBlank(image, null);
assertNotNull(crop);
assertEquals(image.getWidth(), crop.getWidth());
assertEquals(image.getHeight(), crop.getHeight());
//Synthetic comment -- @@ -61,7 +74,41 @@
g.fillRect(25, 25, 50, 50);
g.dispose();

        BufferedImage crop = SwtUtils.cropBlank(image, null);
        assertNotNull(crop);
        assertEquals(50, crop.getWidth());
        assertEquals(50, crop.getHeight());
        assertEquals(0xFF00FF00, crop.getRGB(0, 0));
        assertEquals(0xFF00FF00, crop.getRGB(49, 49));
    }

    public void testCropSomethingPre() throws Exception {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0, true));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(new Color(0xFF00FF00, true));
        g.fillRect(25, 25, 50, 50);
        g.dispose();

        BufferedImage crop = SwtUtils.cropBlank(image, new Rect(0, 0, 100, 100));
        assertNotNull(crop);
        assertEquals(50, crop.getWidth());
        assertEquals(50, crop.getHeight());
        assertEquals(0xFF00FF00, crop.getRGB(0, 0));
        assertEquals(0xFF00FF00, crop.getRGB(49, 49));
    }

    public void testCropSomethingPre2() throws Exception {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0, true));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(new Color(0xFF00FF00, true));
        g.fillRect(25, 25, 50, 50);
        g.dispose();

        BufferedImage crop = SwtUtils.cropBlank(image, new Rect(5, 5, 80, 80));
assertNotNull(crop);
assertEquals(50, crop.getWidth());
assertEquals(50, crop.getHeight());
//Synthetic comment -- @@ -76,7 +123,7 @@
g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

        BufferedImage crop = SwtUtils.cropColor(image, 0xFF00FF00, null);
assertNull(crop);
}

//Synthetic comment -- @@ -87,7 +134,7 @@
g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

        BufferedImage crop = SwtUtils.cropColor(image, 0xFFFF0000, null);
assertNotNull(crop);
assertEquals(image.getWidth(), crop.getWidth());
assertEquals(image.getHeight(), crop.getHeight());
//Synthetic comment -- @@ -102,7 +149,7 @@
g.fillRect(25, 25, 50, 50);
g.dispose();

        BufferedImage crop = SwtUtils.cropColor(image, 0xFF00FF00, null);
assertEquals(50, crop.getWidth());
assertEquals(50, crop.getHeight());
assertEquals(0xFFFF0000, crop.getRGB(0, 0));
//Synthetic comment -- @@ -110,8 +157,8 @@
}

public void testNullOk() throws Exception {
        SwtUtils.cropBlank(null, null);
        SwtUtils.cropColor(null, 0, null);
}

public void testImageConvertNoAlpha() throws Exception {
//Synthetic comment -- @@ -208,4 +255,15 @@
}
}

    public void testNothingTodo() throws Exception {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xFF00FF00, true));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();

        BufferedImage crop = SwtUtils.cropColor(image, 0xFFFF0000, new Rect(40, 40, 0, 0));
        assertNull(crop);
    }

}







