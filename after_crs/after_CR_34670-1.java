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
            final float tolerance = 0.01f;  // 1%
            mSame = compareTo(mBitmap, mReferenceBitmap, tolerance);
}

        /* Compare two bitmaps with a specified tolerance.
         *
         * This method returns true even if two bitmaps are not completely same
         * but are alomst same within the specified tolerance.
*/
        private boolean compareTo(Bitmap bitmap, Bitmap reference, float tolerance) {
if (bitmap.getConfig() != reference.getConfig() ||
bitmap.getWidth() != reference.getWidth() ||
bitmap.getHeight() != reference.getHeight()) {
return false;
}

            int resultBytes = bitmap.getByteCount();
            ByteBuffer resultBuffer = ByteBuffer.allocate(resultBytes);
            bitmap.copyPixelsToBuffer(resultBuffer);

            int referenceBytes = reference.getByteCount();
            ByteBuffer referenceBuffer = ByteBuffer.allocate(referenceBytes);
            reference.copyPixelsToBuffer(referenceBuffer);

            int unmatchCount = 0;
            final int length = Math.max(resultBytes, referenceBytes);
            for (int i = 0; i < length; i += 4) {
                int resultPixel = resultBuffer.getInt(i);
                int referencePixel = referenceBuffer.getInt(i);

                if (resultPixel != referencePixel) {
                    unmatchCount++;
}
}

            float rate = unmatchCount/(length/4.0f);
            return (rate < tolerance) ? true : false;
}

@Override
//Synthetic comment -- @@ -246,6 +238,7 @@
int color1 = bitmap1.getPixel(i, j);
int color2 = bitmap2.getPixel(i, j);
color = color1 == color2 ? color1 : Color.RED;

} else if (inBounds1 && !inBounds2) {
color = Color.BLUE;
} else if (!inBounds1 && inBounds2) {







