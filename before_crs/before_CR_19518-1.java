/*Fix Palette scrollbar bug

Fixes the problem that on Mac, when you close a palette category the
scrollbar thumb size was not updated. The thumb value could be set to
0 in that case, which is ignored by SWT.

This changeset also sets the increment and the page increment on the
scrollbar to improve the behavior of clicking the scrollbar arrows or
clicking above and below the thumb.

The changeset also sets all the values of the scrollbar in a single go
(via the setValues() call); this should improve performance, and in
conjunction with the first fix (to the thumb value) I am hoping this
addresses issue 13097: Gingerbread Layout Editor scroll bar bugs

Change-Id:I8274e4bdd8828ac814f04b99650f1209b8636797*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index e3bbd00..2571753 100755

//Synthetic comment -- @@ -236,14 +236,20 @@
// Thumb size is the ratio between root view and visible height.
float ft = ry > 0 ? (float)vy / ry : 1;
int thumb = (int) Math.ceil(y * ft);
y += thumb;


if (y != max) {
mVBar.setEnabled(y > 0);
                mVBar.setMaximum(y < 0 ? 1 : y);
                mVBar.setSelection((int) (y * current));
                mVBar.setThumb(thumb);
scrollScrollbar();
return true;
}







