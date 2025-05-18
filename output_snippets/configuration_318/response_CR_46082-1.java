//<Beginning of snippet n. 0>


/**
* Sets a drawable as the content of this ImageView.
* 
* @param drawable The drawable to set
*/
public void setImageDrawable(Drawable drawable) {
    if (mDrawable != drawable) {
        mDrawable = drawable;
        // Additional logic to update the view here
    }
}

/**
* Sets a Bitmap as the content of this ImageView.
* 
* @param bm The bitmap to set. Calling this method with null will clear the image by invoking setImageDrawable(null).
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