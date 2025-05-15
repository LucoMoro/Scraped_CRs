//<Beginning of snippet n. 0>

/**
 * Sets a drawable as the content of this ImageView.
 * 
 * @param drawable The drawable to set
 */
public void setImageDrawable(Drawable drawable) {
    if (mDrawable != drawable) {
        // implementation for setting the drawable
    }
}

/**
 * Sets a Bitmap as the content of this ImageView.
 * 
 * @param bm The bitmap to set
 */
@android.view.RemotableViewMethod
public void setImageBitmap(Bitmap bm) {
    if (bm == null) {
        setImageDrawable(null); // Clear image if bitmap is null
    } else {
        // if this is used frequently, may handle bitmaps explicitly
        // to reduce the intermediate drawable object
        setImageDrawable(new BitmapDrawable(mContext.getResources(), bm));
    }
}

public void setImageState(int[] state, boolean merge) {
// implementation for setting the image state
}

//<End of snippet n. 0>