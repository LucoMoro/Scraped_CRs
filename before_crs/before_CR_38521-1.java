/*Set the mTargetDensity of BitmapState when inflate BitmapDrawable from xml resource.

The mTargetDensity of BitmapSate was not assigned when BitmapDrawable.inflate().

Change-Id:I31f36c5bcb4b3ff178366a35d175125c5d8573b8Signed-off-by: CheolOh,Park <cheoloh.park@gmail.com>*/
//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/BitmapDrawable.java b/graphics/java/android/graphics/drawable/BitmapDrawable.java
//Synthetic comment -- index 87421b1..2ff172b 100644

//Synthetic comment -- @@ -451,6 +451,7 @@
mBitmapState.mBitmap = bitmap;
setBitmap(bitmap);
setTargetDensity(r.getDisplayMetrics());

final Paint paint = mBitmapState.mPaint;
paint.setAntiAlias(a.getBoolean(com.android.internal.R.styleable.BitmapDrawable_antialias,







