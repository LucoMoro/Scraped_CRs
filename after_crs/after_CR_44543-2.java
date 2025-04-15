/*Support non RGBA textures

Textures that are not of type unsigned byte need to be
unpacked into unsigned byte (1 byte per channel) first.

This CL also increases the protobuf message size and
fixes an NPE that happens when the trace is empty.

Change-Id:I01f20d6292425dbd553f006947dfd1024c6503c6*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceFileReader.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceFileReader.java
//Synthetic comment -- index ba7bf67..76b32b0 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
* Currently, we assume that the maximum is for a 1080p display. Since the buffers compress
* well, we should probably never get close to this.
*/
    private static final int MAX_PROTOBUF_SIZE = 1920 * 1080 * 100;

/**
* Obtain the next protobuf message in this file.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java
//Synthetic comment -- index fa506e2..d118130 100644

//Synthetic comment -- @@ -263,10 +263,12 @@
}

private void refreshUI() {
        if (mTrace == null || mTrace.getGLCalls().size() == 0) {
            setFrameCount(0);
            return;
        }

        setFrameCount(mTrace.getFrames().size());
selectFrame(1);
}

//Synthetic comment -- @@ -324,6 +326,10 @@
}

private void setFrameCount(int nFrames) {
        boolean en = nFrames > 0;
        mFrameSelectionScale.setEnabled(en);
        mFrameSelectionSpinner.setEnabled(en);

mFrameSelectionScale.setMaximum(nFrames);
mFrameSelectionSpinner.setMaximum(nFrames);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/StateTransformFactory.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/StateTransformFactory.java
//Synthetic comment -- index f69f28d..1a95798 100644

//Synthetic comment -- @@ -1052,7 +1052,7 @@
getTextureUnitTargetName(target),
level,
GLStateType.TEXTURE_IMAGE),
                f, format, type, xOffset, yOffset, width, height));

return transforms;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/TexImageTransform.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/TexImageTransform.java
//Synthetic comment -- index 775ff23..451eb70 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.eclipse.gltrace.state.GLStringProperty;
import com.android.ide.eclipse.gltrace.state.IGLProperty;
import com.google.common.io.Files;
import com.google.common.primitives.UnsignedBytes;

import java.awt.image.BufferedImage;
import java.io.File;
//Synthetic comment -- @@ -48,6 +49,7 @@
private String mOldValue;
private String mNewValue;
private GLEnum mFormat;
    private GLEnum mType;

/**
* Construct a texture image transformation.
//Synthetic comment -- @@ -60,10 +62,11 @@
* @param height height of the texture
*/
public TexImageTransform(IGLPropertyAccessor accessor, File textureData, GLEnum format,
            GLEnum type, int xOffset, int yOffset, int width, int height) {
mAccessor = accessor;
mTextureDataFile = textureData;
mFormat = format;
        mType = type;

mxOffset = xOffset;
myOffset = yOffset;
//Synthetic comment -- @@ -167,13 +170,16 @@
byte[] subImageData = Files.toByteArray(textureDataFile);
image.getRaster().setDataElements(xOffset, yOffset, width, height,
formatSourceData(subImageData, width, height));
ImageIO.write(image, PNG_IMAGE_FORMAT, f);

return f.getAbsolutePath();
}

private byte[] formatSourceData(byte[] subImageData, int width, int height) {
        if (mType != GLEnum.GL_UNSIGNED_BYTE) {
            subImageData = unpackData(subImageData, mType);
        }

switch (mFormat) {
case GL_RGBA:
// no conversions necessary
//Synthetic comment -- @@ -191,7 +197,57 @@
}
}

    private byte[] unpackData(byte[] data, GLEnum type) {
        switch (type) {
            case GL_UNSIGNED_BYTE:
                return data;
            case GL_UNSIGNED_SHORT_4_4_4_4:
                return convertShortToUnsigned(data, 0xf000, 12, 0x0f00, 8, 0x00f0, 4, 0x000f, 0,
                        true);
            case GL_UNSIGNED_SHORT_5_6_5:
                return convertShortToUnsigned(data, 0xf800, 11, 0x07e0, 5, 0x001f, 0, 0, 0,
                        false);
            case GL_UNSIGNED_SHORT_5_5_5_1:
                return convertShortToUnsigned(data, 0xf800, 11, 0x07c0, 6, 0x003e, 1, 0x1, 0,
                        true);
            default:
                return data;
        }
    }

   private byte[] convertShortToUnsigned(byte[] shortData,
           int rmask, int rshift,
           int gmask, int gshift,
           int bmask, int bshift,
           int amask, int ashift,
           boolean includeAlpha) {
       int numChannels = includeAlpha ? 4 : 3;
       byte[] unsignedData = new byte[(shortData.length/2) * numChannels];

       for (int i = 0; i < (shortData.length / 2); i++) {
           int hi = UnsignedBytes.toInt(shortData[i*2 + 0]);
           int lo = UnsignedBytes.toInt(shortData[i*2 + 1]);

           int x = hi << 8 | lo;

           int r = (x & rmask) >>> rshift;
           int g = (x & gmask) >>> gshift;
           int b = (x & bmask) >>> bshift;
           int a = (x & amask) >>> ashift;

           unsignedData[i * numChannels + 0] = UnsignedBytes.checkedCast(r);
           unsignedData[i * numChannels + 1] = UnsignedBytes.checkedCast(g);
           unsignedData[i * numChannels + 2] = UnsignedBytes.checkedCast(b);

           if (includeAlpha) {
               unsignedData[i * numChannels + 3] = UnsignedBytes.checkedCast(a);
           }
       }

       return unsignedData;
   }

   private byte[] addAlphaChannel(byte[] sourceData, int width, int height) {
assert sourceData.length == 3 * width * height; // should have R, G & B channels

byte[] data = new byte[4 * width * height];







