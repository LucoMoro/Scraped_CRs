/*Java crash when switching Google Map and Home quickly on 4.0.3

Build 4.0.3 or upper version (with GMS) and flash the image to phone.
Put Google Map icon on the desktop
Swiching Google Map and Home very quickly.

Change-Id:Ic901e0f09df985d2d5d5f27c2092c454ec4f4fcbhttp://code.google.com/p/android/issues/detail?id=27562*/
//Synthetic comment -- diff --git a/core/java/android/view/SurfaceView.java b/core/java/android/view/SurfaceView.java
//Synthetic comment -- index 6726c56e..f242791 100644

//Synthetic comment -- @@ -522,7 +522,7 @@
mSurface.transferFrom(mNewSurface);

if (visible) {
                        if (!mSurfaceCreated && (surfaceChanged || visibleChanged)) {
mSurfaceCreated = true;
mIsCreating = true;
if (DEBUG) Log.i(TAG, "visibleChanged -- surfaceCreated");







