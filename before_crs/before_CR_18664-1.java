/*BitmapDrawable constructed by filepath(InputStream) and Resources should use passed resources down instead of null.

Change-Id:I54ea6ed3e7e5ffc174d86209165d19e2aa95af35*/
//Synthetic comment -- diff --git a/graphics/java/android/graphics/drawable/BitmapDrawable.java b/graphics/java/android/graphics/drawable/BitmapDrawable.java
//Synthetic comment -- index 29e14d2..c13ef1e 100644

//Synthetic comment -- @@ -42,8 +42,8 @@
* a {@link android.graphics.Bitmap} object.
* <p>It can be defined in an XML file with the <code>&lt;bitmap></code> element.</p>
* <p>
 * Also see the {@link android.graphics.Bitmap} class, which handles the management and 
 * transformation of raw bitmap graphics, and should be used when drawing to a 
* {@link android.graphics.Canvas}.
* </p>
*
//Synthetic comment -- @@ -67,11 +67,11 @@
private boolean mApplyGravity;
private boolean mRebuildShader;
private boolean mMutated;
    
// These are scaled to match the target density.
private int mBitmapWidth;
private int mBitmapHeight;
    
/**
* Create an empty drawable, not dealing with density.
* @deprecated Use {@link #BitmapDrawable(Resources)} to ensure
//Synthetic comment -- @@ -127,7 +127,7 @@
* Create a drawable by opening a given file path and decoding the bitmap.
*/
public BitmapDrawable(Resources res, String filepath) {
        this(new BitmapState(BitmapFactory.decodeFile(filepath)), null);
mBitmapState.mTargetDensity = mTargetDensity;
if (mBitmap == null) {
android.util.Log.w("BitmapDrawable", "BitmapDrawable cannot decode " + filepath);
//Synthetic comment -- @@ -151,7 +151,7 @@
* Create a drawable by decoding a bitmap from the given input stream.
*/
public BitmapDrawable(Resources res, java.io.InputStream is) {
        this(new BitmapState(BitmapFactory.decodeStream(is)), null);
mBitmapState.mTargetDensity = mTargetDensity;
if (mBitmap == null) {
android.util.Log.w("BitmapDrawable", "BitmapDrawable cannot decode " + is);
//Synthetic comment -- @@ -161,7 +161,7 @@
public final Paint getPaint() {
return mBitmapState.mPaint;
}
    
public final Bitmap getBitmap() {
return mBitmap;
}
//Synthetic comment -- @@ -170,7 +170,7 @@
mBitmapWidth = mBitmap.getScaledWidth(mTargetDensity);
mBitmapHeight = mBitmap.getScaledHeight(mTargetDensity);
}
    
private void setBitmap(Bitmap bitmap) {
mBitmap = bitmap;
if (bitmap != null) {
//Synthetic comment -- @@ -231,7 +231,7 @@
public int getGravity() {
return mBitmapState.mGravity;
}
    
/** Set the gravity used to position/stretch the bitmap within its bounds.
See android.view.Gravity
* @param gravity the gravity
//Synthetic comment -- @@ -244,7 +244,7 @@
public void setAntiAlias(boolean aa) {
mBitmapState.mPaint.setAntiAlias(aa);
}
    
@Override
public void setFilterBitmap(boolean filter) {
mBitmapState.mPaint.setFilterBitmap(filter);
//Synthetic comment -- @@ -285,7 +285,7 @@
public int getChangingConfigurations() {
return super.getChangingConfigurations() | mBitmapState.mChangingConfigurations;
}
    
@Override
protected void onBoundsChange(Rect bounds) {
super.onBoundsChange(bounds);
//Synthetic comment -- @@ -456,12 +456,12 @@
public Drawable newDrawable() {
return new BitmapDrawable(this, null);
}
        
@Override
public Drawable newDrawable(Resources res) {
return new BitmapDrawable(this, res);
}
        
@Override
public int getChangingConfigurations() {
return mChangingConfigurations;







