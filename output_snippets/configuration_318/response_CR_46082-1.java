//<Beginning of snippet n. 0>

/**
* Sets a drawable as the content of this ImageView.
* 
* @param drawable The drawable to set
*/
public void setImageDrawable(Drawable drawable) {
    if (mDrawable != drawable) {
        // Logic for setting the drawable
    }
}

/**
* Sets a Bitmap as the content of this ImageView.
* 
* @param bm The bitmap to set
* This method will set the drawable to null if bm is null.
* Note: There may be potential crash risks when called with null on Android versions ≤ 4.2.
*/
@android.view.RemotableViewMethod
public void setImageBitmap(Bitmap bm) {
    if (bm == null) {
        setImageDrawable(null);
    } else {
        setImageDrawable(new BitmapDrawable(mContext.getResources(), bm));
    }
}

public void setImageState(int[] state, boolean merge) {

//<End of snippet n. 0>