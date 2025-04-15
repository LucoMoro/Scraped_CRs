/*Leave scrollbars visible

Remove the code I added recently which hides the scrollbars when they
are not enabled. While I want to restore it later, I need to guard
against a possible scenario where showing the scrollbars causes the
viewport size to change which can in turn cause the scrollbars to be
hidden recursively. For R10 let's play it safe.

Change-Id:I3fa30035ecbede55fd6e0063f222efc6fead386f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java
//Synthetic comment -- index d00908c..831367a 100644

//Synthetic comment -- @@ -122,10 +122,8 @@
if (sx < cx) {
mTranslate = 0;
mScrollbar.setEnabled(false);
            mScrollbar.setVisible(false);
} else {
mScrollbar.setEnabled(true);
            mScrollbar.setVisible(true);

int selection = mScrollbar.getSelection();
int thumb = cx;







