/*Eliminating dead code with no other effect than consuming battery life.

Change-Id:I4376902a4e9c301ff16f74137d86eeaff901bbb7*/




//Synthetic comment -- diff --git a/core/java/android/text/style/DrawableMarginSpan.java b/core/java/android/text/style/DrawableMarginSpan.java
//Synthetic comment -- index 3c471a5..c2564d5 100644

//Synthetic comment -- @@ -50,9 +50,6 @@
int dw = mDrawable.getIntrinsicWidth();
int dh = mDrawable.getIntrinsicHeight();

// XXX What to do about Paint?
mDrawable.setBounds(ix, itop, ix+dw, itop+dh);
mDrawable.draw(c);







