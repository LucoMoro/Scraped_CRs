/*Let setImageBitmap(null) work as expected.

Most people expect this to clear the image, but actually only setImageDrawable(null) works. This updates setImageBitmap(null) to work too and documents them both.
For an example of why this is needed, seehttp://stackoverflow.com/questions/2859212/how-to-clear-an-imageview-in-androidThere is a risk with this change that users of Android 4.3 will expect setImageBitmap(null) to work and be surprised when it crashes on Android <= 4.2. I'm not sure what your policy is with fixing bugs like this.

Change-Id:I4c5910d15bdeb17dd33fdb2a2f294a334eaec88cSigned-off-by: Tim Hutt <tdhutt@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/widget/ImageView.java b/core/java/android/widget/ImageView.java
//Synthetic comment -- index cf28da4..60e3853 100644

//Synthetic comment -- @@ -385,7 +385,7 @@
/**
* Sets a drawable as the content of this ImageView.
* 
     * @param drawable The drawable to set. Use null to clear the image.
*/
public void setImageDrawable(Drawable drawable) {
if (mDrawable != drawable) {
//Synthetic comment -- @@ -407,13 +407,17 @@
/**
* Sets a Bitmap as the content of this ImageView.
* 
     * @param bm The bitmap to set. Use null to clear the image.
*/
@android.view.RemotableViewMethod
public void setImageBitmap(Bitmap bm) {
// if this is used frequently, may handle bitmaps explicitly
// to reduce the intermediate drawable object
        
        if (bm == null)
            setImageDrawable(null);
        else
            setImageDrawable(new BitmapDrawable(mContext.getResources(), bm));
}

public void setImageState(int[] state, boolean merge) {







