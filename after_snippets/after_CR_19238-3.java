
//<Beginning of snippet n. 0>


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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



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
private CanvasTransform mHScale;

/**
     * A lazily instantiated SWT sample model
     */
    private PixelInterleavedSampleModel mSampleModel;


    /**
* Constructs an {@link ImageOverlay} tied to the given canvas.
*
* @param canvas The {@link LayoutCanvas} to paint the overlay over.
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
            // FIXME actually figure out the PixelInterleavedSampleModel's band offset from the image data palette.
            WritableRaster raster = (WritableRaster) Raster.createRaster(
                    getSampleModel(imageData.palette, w, h),
                    new DataBufferByte(imageData.data, imageData.data.length),
                    new Point(0,0));

            ColorModel colorModel = new ComponentColorModel(
                    ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB),
                    true /*hasAlpha*/, true /*isAlphaPremultiplied*/,
                    ColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);

            mAwtImage = new BufferedImage(colorModel, raster, false, null);
            System.out.println("Create new image");
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
        return new int[] {3, 2, 1, 0};
    }
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


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

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


* {@link MoveGesture}.
*/
public abstract class Overlay {
    private Device mDevice;

/**
* Construct the overlay, using the given graphics context for painting.
*/
*            to {@link #paint} will correspond to this device.
*/
public void create(Device device) {
        mDevice = device;
}

/**
throw new IllegalArgumentException("paint() not implemented, probably done "
+ "with specialized paint signature");
}

    public Device getDevice() {
        return mDevice;
    }
}

//<End of snippet n. 3>










//<Beginning of snippet n. 4>

new file mode 100644

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

//<End of snippet n. 4>










//<Beginning of snippet n. 5>


private int mCustomBackgroundColor;
private long mTimeout;

    private IImageFactory mImageFactory = null;

/**
*
* @param layoutDescription the {@link IXmlPullParser} letting the LayoutLib Bridge visit the
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
mCustomBackgroundColor = params.mCustomBackgroundColor;
mTimeout = params.mTimeout;
        mImageFactory = params.mImageFactory;
}

public void setCustomBackgroundColor(int color) {
mTimeout = timeout;
}

    public void setImageFactory(IImageFactory imageFactory) {
        mImageFactory = imageFactory;
    }

public IXmlPullParser getLayoutDescription() {
return mLayoutDescription;
}
public long getTimeout() {
return mTimeout;
}

    public IImageFactory getImageFactory() {
        return mImageFactory;
    }
}

//<End of snippet n. 5>








