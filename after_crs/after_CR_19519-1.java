/*Fix canvas scrolling bug

Fix the bug where zooming in, then panning, then zooming out could
result in lost scrollbars or the canvas no longer being in the visible
viewport.

Also handle the page increment setting code more cleanly.

Change-Id:I5c8e6580259a3a925e74d700771e740eaa5eb95e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java
//Synthetic comment -- index f905f35..91029ce 100644

//Synthetic comment -- @@ -108,6 +108,7 @@
/** Changes the size of the client size. Recomputes scrollbars. */
public void setClientSize(int clientSize) {
mClientSize = clientSize;
        mScrollbar.setPageIncrement(clientSize);
resizeScrollbar();
}

//Synthetic comment -- @@ -119,14 +120,26 @@
int cx = mClientSize - 2 * IMAGE_MARGIN;

if (sx < cx) {
            mTranslate = 0;
mScrollbar.setEnabled(false);
} else {
mScrollbar.setEnabled(true);

            int selection = mScrollbar.getSelection();
            int thumb = cx;
            int maximum = sx;

            if (selection + thumb > maximum) {
                selection -= (selection + thumb - maximum);
                if (selection < 0) {
                    selection = 0;
                }
            }

            mScrollbar.setValues(selection, mScrollbar.getMinimum(), maximum, thumb, mScrollbar
                    .getIncrement(), mScrollbar.getPageIncrement());

            mTranslate = selection;
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index a49505d..4f7faa6 100755

//Synthetic comment -- @@ -253,10 +253,6 @@
super.controlResized(e);
mHScale.setClientSize(getClientArea().width);
mVScale.setClientSize(getClientArea().height);
}
});








