//<Beginning of snippet n. 0>


/**
* Sets a drawable as the content of this ImageView.
* 
* @param drawable The drawable to set
*/
public void setImageDrawable(Drawable drawable) {
    if (mDrawable != drawable) {
        mDrawable = drawable;
        // additional logic for drawable state change
    }
}

/**
* Sets a Bitmap as the content of this ImageView.
* 
* @param bm The bitmap to set
* 
* @throws IllegalArgumentException if bm is null on Android < 4.3
*/
@android.view.RemotableViewMethod
public void setImageBitmap(Bitmap bm) {
    if (bm == null) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            throw new IllegalArgumentException("setImageBitmap(null) is not supported on this Android version.");
        }
        setImageDrawable(null);
    } else {
        setImageDrawable(new BitmapDrawable(mContext.getResources(), bm));
    }
}

public void setImageState(int[] state, boolean merge) {

//<End of snippet n. 0>