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

    private ControlPoint imageOffset;

/** Private constructor. Use {@link #getInstance()} to retrieve the singleton. */
private GlobalCanvasDragInfo() {
//Synthetic comment -- @@ -127,7 +127,7 @@
* @return The image offset, or null if none apply
*/
public ControlPoint getImageOffset() {
        return imageOffset;
}

/**
//Synthetic comment -- @@ -137,6 +137,6 @@
* @param imageOffset a new offset to apply
*/
public void setImageOffset(ControlPoint imageOffset) {
        this.imageOffset = imageOffset;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 344f3a0..74f12e7 100755

//Synthetic comment -- @@ -1391,8 +1391,7 @@
if (target != null) {
AndroidTargetData data = Sdk.getCurrent().getTargetData(target);
if (data != null) {
                LayoutLibrary layoutLib = data.getLayoutLibrary();
                return layoutLib;
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index d7fdd1e..ec40d53 100755

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.layoutlib.api.LayoutBridge;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneResult;
import com.android.sdklib.SdkConstants;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -547,10 +548,10 @@
// actually scanning pixels.

/** Width of the rendered preview image (before it is cropped) */
        private static final int RENDER_HEIGHT = 200;

/** Height of the rendered preview image (before it is cropped) */
        private static final int RENDER_WIDTH = 300;

/** Amount of alpha to multiply into the image (divided by 256) */
private static final int IMG_ALPHA = 192;
//Synthetic comment -- @@ -703,11 +704,21 @@
BufferedImage image = result.getImage();
if (image != null) {
BufferedImage cropped;
if (hasTransparency) {
                        cropped = SwtUtils.cropBlank(image);
} else {
int edgeColor = image.getRGB(image.getWidth() - 1, image.getHeight() - 1);
                        cropped = SwtUtils.cropColor(image, edgeColor);
}

if (cropped != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java
//Synthetic comment -- index 05a3d5d..3080716 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
//Synthetic comment -- @@ -91,18 +93,22 @@
* this is not the same as pixels that aren't opaque (an alpha value other than 255).
*
* @param image the image to be cropped
* @return a cropped version of the source image, or null if the whole image was blank
*         and cropping completely removed everything
*/
    public static BufferedImage cropBlank(BufferedImage image) {
        return crop(image, new CropFilter() {
public boolean crop(BufferedImage bufferedImage, int x, int y) {
int rgb = bufferedImage.getRGB(x, y);
return (rgb & 0xFF000000) == 0x00000000;
// TODO: Do a threshold of 80 instead of just 0? Might give better
// visual results -- e.g. check <= 0x80000000
}
        });
}

/**
//Synthetic comment -- @@ -112,15 +118,20 @@
* @param image the image to be cropped
* @param blankRgba the color considered to be blank, as a 32 pixel integer with 8
*            bits of alpha, red, green and blue
* @return a cropped version of the source image, or null if the whole image was blank
*         and cropping completely removed everything
*/
    public static BufferedImage cropColor(BufferedImage image, final int blankRgba) {
        return crop(image, new CropFilter() {
public boolean crop(BufferedImage bufferedImage, int x, int y) {
return blankRgba == bufferedImage.getRGB(x, y);
}
        });
}

/**
//Synthetic comment -- @@ -139,16 +150,29 @@
boolean crop(BufferedImage image, int x, int y);
}

    private static BufferedImage crop(BufferedImage image, CropFilter filter) {
if (image == null) {
return null;
}

// First, determine the dimensions of the real image within the image
        int x1 = 0;
        int y1 = 0;
        int x2 = image.getWidth();
        int y2 = image.getHeight();

// This algorithm is a bit dumb -- it just scans along the edges looking for
// a pixel that shouldn't be cropped. I could maybe try to make it smarter by








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java
//Synthetic comment -- index 7ed5f1f..30bb82e 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -35,7 +37,18 @@
g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

        BufferedImage crop = SwtUtils.cropBlank(image);
assertNull(crop);
}

//Synthetic comment -- @@ -46,7 +59,7 @@
g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

        BufferedImage crop = SwtUtils.cropBlank(image);
assertNotNull(crop);
assertEquals(image.getWidth(), crop.getWidth());
assertEquals(image.getHeight(), crop.getHeight());
//Synthetic comment -- @@ -61,7 +74,41 @@
g.fillRect(25, 25, 50, 50);
g.dispose();

        BufferedImage crop = SwtUtils.cropBlank(image);
assertNotNull(crop);
assertEquals(50, crop.getWidth());
assertEquals(50, crop.getHeight());
//Synthetic comment -- @@ -76,7 +123,7 @@
g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

        BufferedImage crop = SwtUtils.cropColor(image, 0xFF00FF00);
assertNull(crop);
}

//Synthetic comment -- @@ -87,7 +134,7 @@
g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

        BufferedImage crop = SwtUtils.cropColor(image, 0xFFFF0000);
assertNotNull(crop);
assertEquals(image.getWidth(), crop.getWidth());
assertEquals(image.getHeight(), crop.getHeight());
//Synthetic comment -- @@ -102,7 +149,7 @@
g.fillRect(25, 25, 50, 50);
g.dispose();

        BufferedImage crop = SwtUtils.cropColor(image, 0xFF00FF00);
assertEquals(50, crop.getWidth());
assertEquals(50, crop.getHeight());
assertEquals(0xFFFF0000, crop.getRGB(0, 0));
//Synthetic comment -- @@ -110,8 +157,8 @@
}

public void testNullOk() throws Exception {
        SwtUtils.cropBlank(null);
        SwtUtils.cropColor(null, 0);
}

public void testImageConvertNoAlpha() throws Exception {
//Synthetic comment -- @@ -208,4 +255,15 @@
}
}

}







