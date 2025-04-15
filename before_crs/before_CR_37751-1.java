/*CTS Holo: Enlarge the threshold from 1 to 2

Optimized Skia implementation may have 1 bit difference with the accurate result,
which is same as Google's default Skia implementation.
But other vendor may choose different optimization with Google's,
so the maximum difference between Google's C optimization and other vendor's optimization may be 2

Change-Id:Iac19e6b8bd53b3932ebb38c12c4b795ee31316f8Signed-off-by: Xu Ziyi <zyxu@marvell.com>*/
//Synthetic comment -- diff --git a/tests/tests/holo/src/android/holo/cts/LayoutTestActivity.java b/tests/tests/holo/src/android/holo/cts/LayoutTestActivity.java
//Synthetic comment -- index 24c8088..2d56dad 100755

//Synthetic comment -- @@ -163,7 +163,7 @@
protected void onPreExecute() {
mBitmap = getBitmap();
mReferenceBitmap = BitmapAssets.getBitmap(getApplicationContext(), mBitmapName);
            final int threshold = 1;
mSame = compareTo(mBitmap, mReferenceBitmap, threshold);
}








