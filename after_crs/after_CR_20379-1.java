/*Fix for crash when setting live wallpaper.

This fix prevents a crash that sometimes happens when setting a
live wallpaper. It happened when pressing "Set wallpaper" button
in the live wallpaper preview activity, before the preview was
fully loaded.

The crash happened in native code while updating the wallpaper
surface when calling mInputChannel.registerInputChannel(),
because the previous call to
mSession.add(mWindow, mLayout, View.VISIBLE, mContentInsets,
             mInputChannel)
had failed. The fix aborts the surface update when it is not
possible to add the window.

Change-Id:I0e79a851e5c7f7b15eb07043c63d1f4d78f14616*/




//Synthetic comment -- diff --git a/core/java/android/service/wallpaper/WallpaperService.java b/core/java/android/service/wallpaper/WallpaperService.java
//Synthetic comment -- index 26346d2..67ddc15 100644

//Synthetic comment -- @@ -514,8 +514,11 @@
mLayout.windowAnimations =
com.android.internal.R.style.Animation_Wallpaper;
mInputChannel = new InputChannel();
                        if (mSession.add(mWindow, mLayout, View.VISIBLE, mContentInsets,
                                mInputChannel) < 0 ) {
                            Log.w(TAG, "Failed to add window while updating wallpaper surface.");
                            return;
                        }
mCreated = true;

InputQueue.registerInputChannel(mInputChannel, mInputHandler,







