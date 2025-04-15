/*Casting floating point computation back to int.

This way, comparison is done on integer values, which is reliable,
contraty to floating point comparision done before.

Change-Id:Ib8b7ee9e7ed5708be8dac1e92092aa78e1259b16*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebView.java b/core/java/android/webkit/WebView.java
//Synthetic comment -- index 88516ce..4e55529 100644

//Synthetic comment -- @@ -6899,7 +6899,7 @@

if (mInitialScaleInPercent > 0) {
setNewZoomScale(mInitialScaleInPercent / 100.0f,
                                    mInitialScaleInPercent != mTextWrapScale * 100,
false);
} else if (restoreState.mViewScale > 0) {
mTextWrapScale = restoreState.mTextWrapScale;







