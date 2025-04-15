/*Fix visual glitch when the drag element is dropped

Window animations moves the drag view when it is dropped and removed.

By changing its visibility to gone before removing it from view we ensure the drag view doesn't appear at the middle of the view before disapearing.

Change-Id:Ib5344de0cfe08862c45220994de42ca2d5290177*/




//Synthetic comment -- diff --git a/src/com/android/music/TouchInterceptor.java b/src/com/android/music/TouchInterceptor.java
//Synthetic comment -- index b070053..5b4bf58 100644

//Synthetic comment -- @@ -361,6 +361,7 @@

private void stopDragging() {
if (mDragView != null) {
            mDragView.setVisibility(GONE);
WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
wm.removeView(mDragView);
mDragView.setImageDrawable(null);







