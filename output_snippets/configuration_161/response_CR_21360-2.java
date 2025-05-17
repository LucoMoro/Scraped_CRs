//<Beginning of snippet n. 0>
if (mDrawable != null) {
    int intrinsicWidth = mDrawable.getIntrinsicWidth();
    int intrinsicHeight = mDrawable.getIntrinsicHeight();
    if (intrinsicWidth > 0 && intrinsicHeight > 0) {
        mDrawable.setBounds(ix, itop, ix + intrinsicWidth, itop + intrinsicHeight);
        mDrawable.draw(c);
    }
}
//<End of snippet n. 0>