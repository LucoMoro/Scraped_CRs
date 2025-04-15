/*CTS Holo: Add +/- tolerance to bitmap comparison

This will allow CTS Holo to pass as long as the bitmap color value is within +/-tolerance.

Change-Id:I75c7c73b60abac0cbac3e15039e5dbed740ceb8eSigned-off-by: Jack Yen <jyen@ti.com>
Signed-off-by: Jack Yen <ckrston@gmail.com>*/




//Synthetic comment -- diff --git a/tests/tests/holo/src/android/holo/cts/LayoutTestActivity.java b/tests/tests/holo/src/android/holo/cts/LayoutTestActivity.java
//Synthetic comment -- index 88b9e5a..a584aec 100644

//Synthetic comment -- @@ -33,6 +33,7 @@

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
* {@link Activity} that applies a theme, inflates a layout, and then either
//Synthetic comment -- @@ -160,7 +161,51 @@
protected void onPreExecute() {
mBitmap = getBitmap();
mReferenceBitmap = BitmapAssets.getBitmap(getApplicationContext(), mBitmapName);
            final int threshold = 1;
            mSame = compareTo(mBitmap, mReferenceBitmap, threshold);
        }

        /* Compares 2 bitmaps' width, height and pixels.
         * 2 Bitmaps are consider the same if color value difference is less than 
         * or equal to +/-threshold
         */
        private boolean compareTo(Bitmap bitmap, Bitmap reference, int threshold) {
            if(bitmap.getConfig() != reference.getConfig() ||
               bitmap.getWidth() != reference.getWidth() ||
               bitmap.getHeight() != reference.getHeight()) {
                   return false;
            }

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            ByteBuffer buffer1 = ByteBuffer.allocate(bitmap.getByteCount());
            ByteBuffer buffer2 = ByteBuffer.allocate(reference.getByteCount());

            bitmap.copyPixelsToBuffer(buffer1);
            reference.copyPixelsToBuffer(buffer2);

            final int length = w*h;
            for (int i = 0 ; i < length ; i++) {
                int pel1 = buffer1.getInt(i);
                int pel2 = buffer2.getInt(i);
                int dr = (pel1 & 0x000000FF) - (pel2 & 0x000000FF);
                int dg = (pel1 & 0x0000FF00) - (pel2 & 0x0000FF00) >> 8;
                int db = (pel1 & 0x00FF0000) - (pel2 & 0x00FF0000) >> 16;

                if(Math.abs(db) > threshold ||
                   Math.abs(dg) > threshold ||
                   Math.abs(dr) > threshold) {
                    return false;
                }
                if(bitmap.hasAlpha()) {
                    int da = (pel1 & 0xFF000000) - (pel2 & 0xFF000000) >> 24;
                    if(Math.abs(da) > threshold) {
                        return false;
                    }
                }
            }
            return true;
}

@Override







