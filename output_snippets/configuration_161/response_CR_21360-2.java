//<Beginning of snippet n. 0>
int dw = mDrawable.getIntrinsicWidth();
int dh = mDrawable.getIntrinsicHeight();

x -= dw;

mDrawable.setBounds(ix, itop, ix + dw, itop + dh);
mDrawable.draw(c);
//<End of snippet n. 0>