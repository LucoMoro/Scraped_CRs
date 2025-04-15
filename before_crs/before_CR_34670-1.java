/*Loop doesn't compare the whole bitmap image

The length of loop should be the size of the buffer. Currently,
the comparison makes only one-fourth of passed bitmap.
And also the pixel shoud be gotten by a multiple of four.

The bitmap comparison is still very strict. If a part of view is
shifted by one-pixel, this test case would fail. But it should be
tolerated. I think that one-bit color difference is very very rare
and actually it wouldn't occur.

So I propose a comparison method to a tolerance of 1%.

Change-Id:Ib23c6669a26b0a70d6910cee00b15cc289d573f3*/
//Synthetic comment -- diff --git a/tests/tests/holo/src/android/holo/cts/LayoutTestActivity.java b/tests/tests/holo/src/android/holo/cts/LayoutTestActivity.java
old mode 100644
new mode 100755
//Synthetic comment -- index 052ddea..4ace93c

//Synthetic comment -- @@ -163,51 +163,43 @@
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
if (bitmap.getConfig() != reference.getConfig() ||
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
            for (int i = 0; i < length; i++) {
                int pel1 = buffer1.getInt(i);
                int pel2 = buffer2.getInt(i);
                int dr = (pel1 & 0x000000FF) - (pel2 & 0x000000FF);
                int dg = ((pel1 & 0x0000FF00) - (pel2 & 0x0000FF00)) >> 8;
                int db = ((pel1 & 0x00FF0000) - (pel2 & 0x00FF0000)) >> 16;

                if (Math.abs(db) > threshold ||
                        Math.abs(dg) > threshold ||
                        Math.abs(dr) > threshold) {
                    return false;
                }
                if (bitmap.hasAlpha()) {
                    int da = ((pel1 & 0xFF000000) - (pel2 & 0xFF000000)) >> 24;
                    if (Math.abs(da) > threshold) {
                        return false;
                    }
}
}
            return true;
}

@Override
//Synthetic comment -- @@ -246,6 +238,7 @@
int color1 = bitmap1.getPixel(i, j);
int color2 = bitmap2.getPixel(i, j);
color = color1 == color2 ? color1 : Color.RED;
} else if (inBounds1 && !inBounds2) {
color = Color.BLUE;
} else if (!inBounds1 && inBounds2) {







