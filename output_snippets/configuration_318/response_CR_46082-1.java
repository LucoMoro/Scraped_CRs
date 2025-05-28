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
* @deprecated This method is safe to call with null and behaves like setImageDrawable(null).
*/
@android.view.RemotableViewMethod
public void setImageBitmap(Bitmap bm) {
    setImageDrawable(bm == null ? null : new BitmapDrawable(mContext.getResources(), bm));
}

public void setImageState(int[] state, boolean merge) {

//<End of snippet n. 0>