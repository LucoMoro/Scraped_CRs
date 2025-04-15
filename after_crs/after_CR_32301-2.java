/*WallpaperManagerService does not properly propagate setDimensionHints()

During bootstrap, Launcher could be initialized in between
WallpaperManagerService and ImageWallpaper. In case Launcher's
WindowManager.suggestDesiredDimenstions() is called after
WallpaperManagerService begin to create ImageWallpaper and before
ImageWallpaper Engine attached, that mostly resulted in Black Edge
of image wallpaper.

This can be reproduced easily (1 in 3)
 - set image wallpaper other than default.
 - modify Launcher/Workspace to call suggestDesiredDimenstions(w,h)
 - reboot

To fix Black Edge, WallpaperManagerService modified to maintain
a flag mDesiredDimensionChanging and set desired size at Engine
attach. Black Edge still can be shown for some moment. but, always
recoverd. To eliminate Black Edge it seems that
IWallpaperConnection.aidl need more method.

NOTE: Many market launcher and some phone vendor launcher call
suggestDesiredDimenstions with argument (w,h) instead aosp's
original (w*2,h) for single page wallpaper.

Change-Id:Ib28636e6b2964d9deeee1f1e1d304554cc7a837e*/




//Synthetic comment -- diff --git a/services/java/com/android/server/WallpaperManagerService.java b/services/java/com/android/server/WallpaperManagerService.java
//Synthetic comment -- index 4925a4e..cca6536 100644

//Synthetic comment -- @@ -169,6 +169,7 @@
WallpaperConnection mWallpaperConnection;
long mLastDiedTime;
boolean mWallpaperUpdating;
    boolean mDesiredDimensionChanging;

class WallpaperConnection extends IWallpaperConnection.Stub
implements ServiceConnection {
//Synthetic comment -- @@ -213,6 +214,13 @@

public void attachEngine(IWallpaperEngine engine) {
mEngine = engine;
             if (engine != null && mDesiredDimensionChanging) {
                 try {
                     engine.setDesiredSize(mWidth, mHeight);
                     mDesiredDimensionChanging = false;
                 } catch (RemoteException e) {
                 }
             }
}

public ParcelFileDescriptor setWallpaper(String name) {
//Synthetic comment -- @@ -395,6 +403,7 @@

synchronized (mLock) {
if (width != mWidth || height != mHeight) {
                boolean desiredDimensionPropagated = false;
mWidth = width;
mHeight = height;
saveSettingsLocked();
//Synthetic comment -- @@ -403,11 +412,15 @@
try {
mWallpaperConnection.mEngine.setDesiredSize(
width, height);
                            desiredDimensionPropagated = true;
} catch (RemoteException e) {
}
notifyCallbacksLocked();
}
}
                if (!desiredDimensionPropagated) {
                    mDesiredDimensionChanging = true;
                }
}
}
}







