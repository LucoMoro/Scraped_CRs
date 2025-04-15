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
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.test.AndroidTestCase;
//Synthetic comment -- @@ -70,6 +71,9 @@
public void testDecodeH264() throws Exception {
decodeVideo(R.raw.video_328x248_h264, 328, 248, 50, 50);
}

/**
* @param testinput the file to decode
//Synthetic comment -- @@ -398,5 +402,228 @@
return failed;
}

}








