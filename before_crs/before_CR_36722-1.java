/*Fix a BadTokenException when user wakeuped device

SlideshowActivity will be destoryed if user pressed POWER
to sleep the device, since it was set to noHistory=true in
AndroidManifest.xml. So it will make SlideView throw a
BadTokenException when user wakeup and then scroll the
SlideView.

Change-Id:Ib67e7547aa47f7c7c6bb469c07482f24db34668dSigned-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideView.java b/src/com/android/mms/ui/SlideView.java
//Synthetic comment -- index 4d3710b..5b36b3c 100644

//Synthetic comment -- @@ -472,7 +472,8 @@
protected void onScrollChanged(int l, int t, int oldl, int oldt) {
// Shows MediaController when the view is scrolled to the top/bottom of itself.
if (t == 0 || t >= mBottomY){
                        if (mMediaController != null) {
mMediaController.show();
}
}







