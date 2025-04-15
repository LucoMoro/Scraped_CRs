/*Set layout editor scrollbar increments

Fix 13097: Gingerbread Layout Editor scroll bar bugs, part 1:
Set the layout editor scrollbar increments to 20 (they were 1, meaning
that each click on the scrollbar down button would scroll the view by
one pixel rather than a "chunk" like 20).

Set the scrollbar page increment to the visual height of the
canvas. This means that if you click below the visible range in the
scrollbar it will jump a full scrollbar increment, as expected.

(This is the first half of 13097. The second half deals with something
unrelated.)

Change-Id:I80c544b749d08d1fdc127d682ef8a75aed9aa8e7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java
//Synthetic comment -- index 8a4ebc6..f905f35 100644

//Synthetic comment -- @@ -58,6 +58,7 @@
CanvasTransform.this.mCanvas.redraw();
}
});
        mScrollbar.setIncrement(20);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 443171e..389b25f 100755

//Synthetic comment -- @@ -255,6 +255,10 @@
super.controlResized(e);
mHScale.setClientSize(getClientArea().width);
mVScale.setClientSize(getClientArea().height);

                Rectangle canvasBounds = LayoutCanvas.this.getBounds();
                getHorizontalBar().setPageIncrement(canvasBounds.width);
                getVerticalBar().setPageIncrement(canvasBounds.height);
}
});








