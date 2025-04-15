/*Fix SurfaceView notifies on invalid surfaces.

SurfaceView notifies registered callbacks on invalid
Surfaces.

Change-Id:Iddc9a5cd073fb73a0e7e9b9ca64ff4fac0777ca7*/
//Synthetic comment -- diff --git a/core/java/android/view/SurfaceView.java b/core/java/android/view/SurfaceView.java
//Synthetic comment -- index fd302dc..ed4c75c 100644

//Synthetic comment -- @@ -531,7 +531,7 @@

mSurface.transferFrom(mNewSurface);

                    if (visible) {
if (!mSurfaceCreated && (surfaceChanged || visibleChanged)) {
mSurfaceCreated = true;
mIsCreating = true;







