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
    private static final int MAX_PROTOBUF_SIZE = 1920 * 1080 * 4;

/**
* Obtain the next protobuf message in this file.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java
//Synthetic comment -- index fa506e2..f953bf3 100644

//Synthetic comment -- @@ -263,10 +263,11 @@
}

private void refreshUI() {
        int nFrames = 0;

        nFrames = mTrace == null ? 1 : mTrace.getFrames().size();
        setFrameCount(nFrames);
selectFrame(1);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/StateTransformFactory.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/StateTransformFactory.java
//Synthetic comment -- index f69f28d..1a95798 100644

//Synthetic comment -- @@ -1052,7 +1052,7 @@
getTextureUnitTargetName(target),
level,
GLStateType.TEXTURE_IMAGE),
                f, format, xOffset, yOffset, width, height));

return transforms;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/TexImageTransform.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/TexImageTransform.java
//Synthetic comment -- index 775ff23..9ef14af 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.eclipse.gltrace.state.GLStringProperty;
import com.android.ide.eclipse.gltrace.state.IGLProperty;
import com.google.common.io.Files;

import java.awt.image.BufferedImage;
import java.io.File;
//Synthetic comment -- @@ -48,6 +49,7 @@
private String mOldValue;
private String mNewValue;
private GLEnum mFormat;

/**
* Construct a texture image transformation.
//Synthetic comment -- @@ -60,10 +62,11 @@
* @param height height of the texture
*/
public TexImageTransform(IGLPropertyAccessor accessor, File textureData, GLEnum format,
            int xOffset, int yOffset, int width, int height) {
mAccessor = accessor;
mTextureDataFile = textureData;
mFormat = format;

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
switch (mFormat) {
case GL_RGBA:
// no conversions necessary
//Synthetic comment -- @@ -191,7 +197,54 @@
}
}

    private byte[] addAlphaChannel(byte[] sourceData, int width, int height) {
assert sourceData.length == 3 * width * height; // should have R, G & B channels

byte[] data = new byte[4 * width * height];







