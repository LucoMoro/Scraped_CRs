/*ADT/Layoutlib: New API to let the caller instantiate the bitmap.

This allows us to use a bitmap more compatible with SWT.

In ADT's case, because the bitmap needs to be converted to SWT
before being displayed, we create a BufferedImage using a byte[]
instead of a int[] so that we can simply do an array copy.

Also, we reuse the generated BufferedImage unless the size changed,
which lets us see less GC during animation playback.

Change-Id:I0062a4f4442ff6469cf0ad4f501c1fbe8c719400*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 8cb13e6..1e51994 100755

//Synthetic comment -- @@ -1436,12 +1436,17 @@
theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,
mLogger);

if (transparentBackground) {
// It doesn't matter what the background color is as long as the alpha
// is 0 (fully transparent). We're using red to make it more obvious if
// for some reason the background is painted when it shouldn't be.
params.setCustomBackgroundColor(0x00FF0000);
}

        // set the Image Overlay as the image factory.
        params.setImageFactory(getCanvasControl().getImageOverlay());

LayoutScene scene = layoutLib.getBridge().createScene(params);

return scene;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index 893ed8f..4e5f56a 100644

//Synthetic comment -- @@ -16,20 +16,37 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.layoutlib.api.IImageFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

/**
* The {@link ImageOverlay} class renders an image as an overlay.
*/
public class ImageOverlay extends Overlay implements IImageFactory {
/** Current background image. Null when there's no image. */
private Image mImage;
    /** Current background AWT image. This is created by {@link #getImage()}, which is called
     * by the LayoutLib. */
    private BufferedImage mAwtImage;

/** The associated {@link LayoutCanvas}. */
private LayoutCanvas mCanvas;
//Synthetic comment -- @@ -41,6 +58,12 @@
private CanvasTransform mHScale;

/**
     * A lazily instantiated SWT sample model
     */
    private PixelInterleavedSampleModel mSampleModel;


    /**
* Constructs an {@link ImageOverlay} tied to the given canvas.
*
* @param canvas The {@link LayoutCanvas} to paint the overlay over.
//Synthetic comment -- @@ -77,14 +100,34 @@
* @return The corresponding SWT image, or null.
*/
public Image setImage(BufferedImage awtImage) {
        if (awtImage != mAwtImage) {
            mAwtImage = null;

            if (mImage != null) {
                mImage.dispose();
            }

            if (awtImage == null) {
                mImage = null;
            } else {
                mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
            }
} else {
            // The image being passed is the one that was created in #getImage(int,int),
            // we can create an SWT image more efficiently.
            WritableRaster awtRaster = mAwtImage.getRaster();
            DataBufferByte byteBuffer = (DataBufferByte) awtRaster.getDataBuffer();
            byte[] data = byteBuffer.getData();

            ImageData imageData = new ImageData(mAwtImage.getWidth(), mAwtImage.getHeight(), 32,
                        new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF));

            // normally we'd use ImageData.setPixels() but it only accepts int[] for 32 bits image.
            // However from #getImage(int, int), we know the raster data is the same exact format
            // as the SWT image, so we just do a copy.
            System.arraycopy(data, 0, imageData.data, 0, data.length);

            mImage  = new Image(getDevice(), imageData);
}

return mImage;
//Synthetic comment -- @@ -180,4 +223,47 @@
}
}

    public BufferedImage getImage(int w, int h) {
        if (mAwtImage == null ||
                mAwtImage.getWidth() != w ||
                mAwtImage.getHeight() != h) {

            ImageData imageData =
                new ImageData(w, h, 32, new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF));
            Image image = new Image(getDevice(), imageData);

            // get the new imageData in case the host OS forced a different format.
            imageData = image.getImageData();

            // create a writable raster around the image data.
            WritableRaster raster = (WritableRaster) Raster.createRaster(
                    getSampleModel(imageData.palette, w, h),
                    new DataBufferByte(imageData.data, imageData.data.length),
                    new Point(0,0));

            ColorModel colorModel = new ComponentColorModel(
                    ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB),
                    true /*hasAlpha*/, true /*isAlphaPremultiplied*/,
                    ColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);

            mAwtImage = new BufferedImage(colorModel, raster, false, null);
        }

        return mAwtImage;
    }

    private SampleModel getSampleModel(PaletteData palette, int w, int h) {
        if (mSampleModel == null) {
            return mSampleModel = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, w, h,
                    4 /*pixel stride*/, w * 4 /*scanlineStride*/,
                    getBandOffset(palette));
        }

        return mSampleModel.createCompatibleSampleModel(w, h);
    }

    private int[] getBandOffset(PaletteData palette) {
        // FIXME actually figure out the PixelInterleavedSampleModel's band offset from the image data palette.
        return new int[] {3, 2, 1, 0};
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 430fd68..e05db27 100755

//Synthetic comment -- @@ -1025,14 +1025,12 @@
*/
public void drawImage() {
// get last image
synchronized (this) {
                                        mImageOverlay.setImage(mImage);
mImage = null;
mPendingDrawing = false;
}

redraw();
}
});








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/Overlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/Overlay.java
//Synthetic comment -- index ac96d76..432d074 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
* {@link MoveGesture}.
*/
public abstract class Overlay {
    private Device mDevice;

/**
* Construct the overlay, using the given graphics context for painting.
*/
//Synthetic comment -- @@ -41,6 +43,7 @@
*            to {@link #paint} will correspond to this device.
*/
public void create(Device device) {
        mDevice = device;
}

/**
//Synthetic comment -- @@ -59,4 +62,8 @@
throw new IllegalArgumentException("paint() not implemented, probably done "
+ "with specialized paint signature");
}

    public Device getDevice() {
        return mDevice;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IImageFactory.java b/layoutlib_api/src/com/android/layoutlib/api/IImageFactory.java
new file mode 100644
//Synthetic comment -- index 0000000..626423e

//Synthetic comment -- @@ -0,0 +1,41 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.layoutlib.api;

import java.awt.image.BufferedImage;

/**
 * Image Factory Interface.
 *
 * An Image factory's task is to create the {@link BufferedImage} into which the scene will be
 * rendered. The goal is to let the layoutlib caller create an image that's optimized for its use
 * case.
 *
 * If no factory is passed in {@link SceneParams#setImageFactory(IImageFactory)}, then a default
 * {@link BufferedImage} of type {@link BufferedImage#TYPE_INT_ARGB} is created.
 *
 */
public interface IImageFactory {

    /**
     * Creates a buffered image with the given size
     * @param width the width of the image
     * @param height the height of the image
     * @return a new (or reused) BufferedImage of the given size.
     */
    BufferedImage getImage(int width, int height);
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java b/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java
//Synthetic comment -- index 0eb9768..1bbd96a 100644

//Synthetic comment -- @@ -64,6 +64,8 @@
private int mCustomBackgroundColor;
private long mTimeout;

    private IImageFactory mImageFactory = null;

/**
*
* @param layoutDescription the {@link IXmlPullParser} letting the LayoutLib Bridge visit the
//Synthetic comment -- @@ -136,6 +138,7 @@
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
mCustomBackgroundColor = params.mCustomBackgroundColor;
mTimeout = params.mTimeout;
        mImageFactory = params.mImageFactory;
}

public void setCustomBackgroundColor(int color) {
//Synthetic comment -- @@ -147,6 +150,10 @@
mTimeout = timeout;
}

    public void setImageFactory(IImageFactory imageFactory) {
        mImageFactory = imageFactory;
    }

public IXmlPullParser getLayoutDescription() {
return mLayoutDescription;
}
//Synthetic comment -- @@ -214,4 +221,8 @@
public long getTimeout() {
return mTimeout;
}

    public IImageFactory getImageFactory() {
        return mImageFactory;
    }
}







