/*Fix FastScroller overlay position bug
after setFastScrollEnabled(false)-->setFastScrollEnabled(true)
the overlay shows up at the top-left instead of center

Change-Id:Idd5d4640398def8391f99962bd1838a3bde98157*/
//Synthetic comment -- diff --git a/core/java/android/widget/FastScroller.java b/core/java/android/widget/FastScroller.java
//Synthetic comment -- index 54c4b36..4bcc56c 100644

//Synthetic comment -- @@ -154,6 +154,11 @@
int textColorNormal = textColor.getDefaultColor();
mPaint.setColor(textColorNormal);
mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

mState = STATE_NONE;
}







