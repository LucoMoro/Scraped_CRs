
//<Beginning of snippet n. 0>


int dw = mDrawable.getIntrinsicWidth();
int dh = mDrawable.getIntrinsicHeight();

        if (dir < 0)
            x -= dw;

// XXX What to do about Paint?
mDrawable.setBounds(ix, itop, ix+dw, itop+dh);
mDrawable.draw(c);

//<End of snippet n. 0>








