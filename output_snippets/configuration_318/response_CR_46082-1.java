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
     * @param bm The bitmap to set
*/
@android.view.RemotableViewMethod
public void setImageBitmap(Bitmap bm) {
    if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
        if (bm == null) {
            // Graceful handling of null bitmap for versions <= 4.2
            Log.w("setImageBitmap", "Bitmap is null and cannot set drawable.");
            return;
        }
    }
    
    if (bm == null) {
        setImageDrawable(null);
    } else {
        setImageDrawable(new BitmapDrawable(mContext.getResources(), bm));
    }
}

public void setImageState(int[] state, boolean merge) {

//<End of snippet n. 0>