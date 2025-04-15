/*Test MediaCodec encoding of video

This tests encoding video in a multiple-of-8 (but not
multiple-of-16) size. This requires understanding all the common
quirks for setting up encoders for such encoding. Device vendors
with varying quirks need to update this test to take their quirks
into account, effectively documenting it for third party vendors.

This test verifies the decoded data in the same way as for the
plain decoding test.

Change-Id:If197fbfdaa0148eec0c6a3b3b08b712bf1219432*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/DecoderTest.java b/tests/tests/media/src/android/media/cts/DecoderTest.java
//Synthetic comment -- index 1167d5b..a39c6616 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import android.content.res.Resources;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.test.AndroidTestCase;
//Synthetic comment -- @@ -70,6 +71,9 @@
public void testDecodeH264() throws Exception {
decodeVideo(R.raw.video_328x248_h264, 328, 248, 50, 50);
}
    public void testEncodeDecodeH264() throws Exception {
        encodeDecodeVideo();
    }

/**
* @param testinput the file to decode
//Synthetic comment -- @@ -398,5 +402,228 @@
return failed;
}

    private void encodeDecodeVideo() {
        int width = 328, height = 248;
        int bitRate = 1000000;
        int frameRate = 15;
        String mimeType = "video/avc";
        int threshold = 50;
        int maxerror = 50;

        MediaCodec encoder, decoder = null;
        ByteBuffer[] encoderInputBuffers;
        ByteBuffer[] encoderOutputBuffers;
        ByteBuffer[] decoderInputBuffers = null;
        ByteBuffer[] decoderOutputBuffers = null;

        int numCodecs = MediaCodecList.getCodecCount();
        MediaCodecInfo codecInfo = null;
        for (int i = 0; i < numCodecs && codecInfo == null; i++) {
            MediaCodecInfo info = MediaCodecList.getCodecInfoAt(i);
            if (!info.isEncoder()) {
                continue;
            }
            String[] types = info.getSupportedTypes();
            boolean found = false;
            for (int j = 0; j < types.length && !found; j++) {
                if (types[j].equals(mimeType))
                    found = true;
            }
            if (!found)
                continue;
            codecInfo = info;
        }
        Log.d(TAG, "Found " + codecInfo.getName() + " supporting " + mimeType);

        int colorFormat = 0;
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(mimeType);
        for (int i = 0; i < capabilities.colorFormats.length && colorFormat == 0; i++) {
            int format = capabilities.colorFormats[i];
            switch (format) {
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
                colorFormat = format;
                break;
            default:
                Log.d(TAG, "Skipping unsupported color format " + format);
                break;
            }
        }
        assertTrue("no supported color format", colorFormat != 0);
        Log.d(TAG, "Using color format " + colorFormat);

        if (codecInfo.getName().equals("OMX.TI.DUCATI1.VIDEO.H264E")) {
            // This codec doesn't support a width not a multiple of 16,
            // so round down.
            width &= ~15;
        }
        int stride = width;
        int sliceHeight = height;
        if (codecInfo.getName().startsWith("OMX.Nvidia.")) {
            stride = (stride + 15)/16*16;
            sliceHeight = (sliceHeight + 15)/16*16;
        }
        encoder = MediaCodec.createByCodecName(codecInfo.getName());
        MediaFormat inputFormat = MediaFormat.createVideoFormat(mimeType, width, height);
        inputFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
        inputFormat.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
        inputFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
        inputFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 75);
        inputFormat.setInteger("stride", stride);
        inputFormat.setInteger("slice-height", sliceHeight);
        Log.d(TAG, "Configuring encoder with input format " + inputFormat);
        encoder.configure(inputFormat, null /* surface */, null /* crypto */, MediaCodec.CONFIGURE_FLAG_ENCODE);
        encoder.start();
        encoderInputBuffers = encoder.getInputBuffers();
        encoderOutputBuffers = encoder.getOutputBuffers();

        int chromaStride = stride/2;
        int frameSize = stride*sliceHeight + 2*chromaStride*sliceHeight/2;
        byte[] inputFrame = new byte[frameSize];
        if (colorFormat == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar ||
            colorFormat == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int Y = (x + y) & 255;
                    int Cb = 255*x/width;
                    int Cr = 255*y/height;
                    inputFrame[y*stride + x] = (byte) Y;
                    inputFrame[stride*sliceHeight + (y/2)*chromaStride + (x/2)] = (byte) Cb;
                    inputFrame[stride*sliceHeight + chromaStride*(sliceHeight/2) + (y/2)*chromaStride + (x/2)] = (byte) Cr;
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int Y = (x + y) & 255;
                    int Cb = 255*x/width;
                    int Cr = 255*y/height;
                    inputFrame[y*stride + x] = (byte) Y;
                    inputFrame[stride*sliceHeight + 2*(y/2)*chromaStride + 2*(x/2)] = (byte) Cb;
                    inputFrame[stride*sliceHeight + 2*(y/2)*chromaStride + 2*(x/2) + 1] = (byte) Cr;
                }
            }
        }

        // start encoding + decoding
        final long kTimeOutUs = 5000;
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        boolean sawInputEOS = false;
        boolean sawOutputEOS = false;
        MediaFormat oformat = null;
        int errors = -1;
        int numInputFrames = 0;
        while (!sawOutputEOS && errors < 0) {
            if (!sawInputEOS) {
                int inputBufIndex = encoder.dequeueInputBuffer(kTimeOutUs);

                if (inputBufIndex >= 0) {
                    ByteBuffer dstBuf = encoderInputBuffers[inputBufIndex];

                    int sampleSize = frameSize;
                    long presentationTimeUs = 0;

                    if (numInputFrames >= 10) {
                        Log.d(TAG, "saw input EOS.");
                        sawInputEOS = true;
                        sampleSize = 0;
                    } else {
                        dstBuf.clear();
                        dstBuf.put(inputFrame);
                        presentationTimeUs = numInputFrames*1000000/frameRate;
                        numInputFrames++;
                    }

                    encoder.queueInputBuffer(
                            inputBufIndex,
                            0 /* offset */,
                            sampleSize,
                            presentationTimeUs,
                            sawInputEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);
                }
            }

            int res = encoder.dequeueOutputBuffer(info, kTimeOutUs);

            if (res >= 0) {
                int outputBufIndex = res;
                ByteBuffer buf = encoderOutputBuffers[outputBufIndex];

                buf.position(info.offset);
                buf.limit(info.offset + info.size);

                if ((info.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {

                    decoder = MediaCodec.createDecoderByType(mimeType);
                    MediaFormat format = MediaFormat.createVideoFormat(mimeType, width, height);
                    format.setByteBuffer("csd-0", buf);
                    decoder.configure(format, null /* surface */, null /* crypto */, 0 /* flags */);
                    decoder.start();
                    decoderInputBuffers = decoder.getInputBuffers();
                    decoderOutputBuffers = decoder.getOutputBuffers();
                } else {
                    int decIndex = decoder.dequeueInputBuffer(-1);
                    decoderInputBuffers[decIndex].clear();
                    decoderInputBuffers[decIndex].put(buf);
                    decoder.queueInputBuffer(decIndex, 0, info.size, info.presentationTimeUs, info.flags);
                }

                encoder.releaseOutputBuffer(outputBufIndex, false /* render */);
            } else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                encoderOutputBuffers = encoder.getOutputBuffers();

                Log.d(TAG, "encoder output buffers have changed.");
            } else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                MediaFormat encformat = encoder.getOutputFormat();

                Log.d(TAG, "encoder output format has changed to " + encformat);
            }

            if (decoder == null)
                res = MediaCodec.INFO_TRY_AGAIN_LATER;
            else
                res = decoder.dequeueOutputBuffer(info, kTimeOutUs);

            if (res >= 0) {
                int outputBufIndex = res;
                ByteBuffer buf = decoderOutputBuffers[outputBufIndex];

                buf.position(info.offset);
                buf.limit(info.offset + info.size);

                if (info.size > 0) {
                    errors = checkFrame(buf, info, oformat, width, height, threshold);
                }

                decoder.releaseOutputBuffer(outputBufIndex, false /* render */);

                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    Log.d(TAG, "saw output EOS.");
                    sawOutputEOS = true;
                }
            } else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                decoderOutputBuffers = decoder.getOutputBuffers();

                Log.d(TAG, "decoder output buffers have changed.");
            } else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                oformat = decoder.getOutputFormat();

                Log.d(TAG, "decoder output format has changed to " + oformat);
            }

        }

        encoder.stop();
        encoder.release();
        decoder.stop();
        decoder.release();

        assertTrue("no frame decoded", errors >= 0);
        assertTrue("decoding error too big: " + errors + "/" + maxerror, errors <= maxerror);
    }

}








