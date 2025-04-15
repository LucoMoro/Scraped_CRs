/*Java Crash happened in 'com.android.browser' at 'java.lang.IIIegalArgumentException' during monkey test.

check whether the bitmap has been recycled, before drawbitmap

Change-Id:I8ac40b24a062486d82616f562a30b6be6f3a64d1Author: Yijun Zhu <yijunx.zhu@intel.com>
Signed-off-by: Yijun Zhu <yijunx.zhu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 62442*/




//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/BitmapDrawable.java b/graphics/java/android/graphics/drawable/BitmapDrawable.java
//Synthetic comment -- index e82ccd4..2b89185 100644

//Synthetic comment -- @@ -390,7 +390,10 @@
getBounds(), mDstRect, layoutDirection);
mApplyGravity = false;
}
                if (!bitmap.isRecycled())
                {
                    canvas.drawBitmap(bitmap, null, mDstRect, state.mPaint);
                }
} else {
if (mApplyGravity) {
copyBounds(mDstRect);







