/*Fix asset studio generator cropping and clipart foreground color

This changeset fixes the asset studio wizard such that
(1) the crop button is enabled and functional
(2) the clipart generator uses the foreground color to color the
    clipart mask
(3) Makes the padding work for images and clipart (this is 20392:
    Android Asset Studio integration bug)

Change-Id:I335fb51f7dba3544e97f0132d5d53afb47a5fe20*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/assetstudio/ConfigureAssetSetPage.java
//Synthetic comment -- index b9ecfbc..105892a 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.assetstudio;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import com.android.assetstudiolib.ActionBarIconGenerator;
import com.android.assetstudiolib.GraphicGenerator;
import com.android.assetstudiolib.GraphicGeneratorContext;
//Synthetic comment -- @@ -24,6 +26,7 @@
import com.android.assetstudiolib.NotificationIconGenerator;
import com.android.assetstudiolib.TabIconGenerator;
import com.android.assetstudiolib.TextRenderUtil;
import com.android.assetstudiolib.Util;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageControl;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils;
//Synthetic comment -- @@ -69,6 +72,7 @@
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;

import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//Synthetic comment -- @@ -272,10 +276,10 @@
new Label(mConfigurationArea, SWT.NONE);

mTrimCheckBox = new Button(mConfigurationArea, SWT.CHECK);
mTrimCheckBox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
mTrimCheckBox.setSelection(false);
mTrimCheckBox.setText("Trim Surrounding Blank Space");
        mTrimCheckBox.addSelectionListener(this);
new Label(mConfigurationArea, SWT.NONE);

Label paddingLabel = new Label(mConfigurationArea, SWT.NONE);
//Synthetic comment -- @@ -912,6 +916,7 @@

CreateAssetSetWizard wizard = (CreateAssetSetWizard) getWizard();
AssetType type = wizard.getAssetType();
        boolean crop = mTrimCheckBox.getSelection();

BufferedImage sourceImage = null;
if (mImageRadio.getSelection()) {
//Synthetic comment -- @@ -930,6 +935,15 @@

setErrorMessage(null);
sourceImage = getImage(path, false);
            if (sourceImage != null) {
                if (crop) {
                    sourceImage = ImageUtils.cropBlank(sourceImage, null, TYPE_INT_ARGB);
                }
                int padding = getPadding();
                if (padding != 0) {
                    sourceImage = Util.paddedImage(sourceImage, padding);
                }
            }
} else if (mTextRadio.getSelection()) {
String text = mText.getText();
TextRenderUtil.Options options = new TextRenderUtil.Options();
//Synthetic comment -- @@ -942,11 +956,36 @@
}
options.foregroundColor = color;
sourceImage = TextRenderUtil.renderTextImage(text, getPadding(), options);

            if (crop) {
                sourceImage = ImageUtils.cropBlank(sourceImage, null, TYPE_INT_ARGB);
            }

            int padding = getPadding();
            if (padding != 0) {
                sourceImage = Util.paddedImage(sourceImage, padding);
            }
} else {
assert mClipartRadio.getSelection();
assert mSelectedClipart != null;
try {
sourceImage = GraphicGenerator.getClipartImage(mSelectedClipart);

                if (crop) {
                    sourceImage = ImageUtils.cropBlank(sourceImage, null, TYPE_INT_ARGB);
                }

                if (type.needsColors()) {
                    int color = 0xFF000000 | (mFgColor.red << 16) | (mFgColor.green << 8)
                            | mFgColor.blue;
                    Paint paint = new java.awt.Color(color);
                    sourceImage = Util.filledImage(sourceImage, paint);
                }

                int padding = getPadding();
                if (padding != 0) {
                    sourceImage = Util.paddedImage(sourceImage, padding);
                }
} catch (IOException e) {
AdtPlugin.log(e, null);
return categoryMap;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index aa8f0e4..e912f53 100644

//Synthetic comment -- @@ -125,6 +125,23 @@
*         and cropping completely removed everything
*/
public static BufferedImage cropBlank(BufferedImage image, Rect initialCrop) {
        return cropBlank(image, initialCrop, image.getType());
    }

    /**
     * Crops blank pixels from the edges of the image and returns the cropped result. We
     * crop off pixels that are blank (meaning they have an alpha value = 0). Note that
     * this is not the same as pixels that aren't opaque (an alpha value other than 255).
     *
     * @param image the image to be cropped
     * @param initialCrop If not null, specifies a rectangle which contains an initial
     *            crop to continue. This can be used to crop an image where you already
     *            know about margins in the image
     * @param imageType the type of {@link BufferedImage} to create
     * @return a cropped version of the source image, or null if the whole image was blank
     *         and cropping completely removed everything
     */
    public static BufferedImage cropBlank(BufferedImage image, Rect initialCrop, int imageType) {
CropFilter filter = new CropFilter() {
@Override
public boolean crop(BufferedImage bufferedImage, int x, int y) {
//Synthetic comment -- @@ -134,7 +151,7 @@
// visual results -- e.g. check <= 0x80000000
}
};
        return crop(image, filter, initialCrop, imageType);
}

/**
//Synthetic comment -- @@ -152,13 +169,32 @@
*/
public static BufferedImage cropColor(BufferedImage image,
final int blankArgb, Rect initialCrop) {
        return cropColor(image, blankArgb, initialCrop, image.getType());
    }

    /**
     * Crops pixels of a given color from the edges of the image and returns the cropped
     * result.
     *
     * @param image the image to be cropped
     * @param blankArgb the color considered to be blank, as a 32 pixel integer with 8
     *            bits of alpha, red, green and blue
     * @param initialCrop If not null, specifies a rectangle which contains an initial
     *            crop to continue. This can be used to crop an image where you already
     *            know about margins in the image
     * @param imageType the type of {@link BufferedImage} to create
     * @return a cropped version of the source image, or null if the whole image was blank
     *         and cropping completely removed everything
     */
    public static BufferedImage cropColor(BufferedImage image,
            final int blankArgb, Rect initialCrop, int imageType) {
CropFilter filter = new CropFilter() {
@Override
public boolean crop(BufferedImage bufferedImage, int x, int y) {
return blankArgb == bufferedImage.getRGB(x, y);
}
};
        return crop(image, filter, initialCrop, imageType);
}

/**
//Synthetic comment -- @@ -177,7 +213,8 @@
boolean crop(BufferedImage image, int x, int y);
}

    private static BufferedImage crop(BufferedImage image, CropFilter filter, Rect initialCrop,
            int imageType) {
if (image == null) {
return null;
}
//Synthetic comment -- @@ -265,7 +302,8 @@
int height = y2 - y1;

// Now extract the sub-image
        BufferedImage cropped = new BufferedImage(width, height,
                imageType != -1 ? imageType : image.getType());
Graphics g = cropped.getGraphics();
g.drawImage(image, 0, 0, width, height, x1, y1, x2, y2, null);








