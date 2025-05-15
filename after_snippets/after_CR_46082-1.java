
//<Beginning of snippet n. 0>


/**
* Sets a drawable as the content of this ImageView.
* 
     * @param drawable The drawable to set. Use null to clear the image.
*/
public void setImageDrawable(Drawable drawable) {
if (mDrawable != drawable) {
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

//<End of snippet n. 0>








