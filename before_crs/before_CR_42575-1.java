/*Detach engines before destroying WallpaperService

WallpaperService contained a race condition where the service might be
destroyed before all its engines had been detached. For subclass
ImageWallpaper this would leak the engine's BroadcastListener, which in
turn would trigger a system crash (as ImageWallpaper runs as part of
the system).

Change-Id:Ia900173a1d3070cbf6bcf769f68376c8c8cbc62a*/
//Synthetic comment -- diff --git a/core/java/android/service/wallpaper/WallpaperService.java b/core/java/android/service/wallpaper/WallpaperService.java
//Synthetic comment -- index 3e0942c..42e3422 100644

//Synthetic comment -- @@ -1011,13 +1011,17 @@
}
Engine engine = onCreateEngine();
mEngine = engine;
                    mActiveEngines.add(engine);
                    engine.attach(this);
return;
}
case DO_DETACH: {
                    mActiveEngines.remove(mEngine);
                    mEngine.detach();
return;
}
case DO_SET_DESIRED_SIZE: {
//Synthetic comment -- @@ -1095,10 +1099,12 @@
@Override
public void onDestroy() {
super.onDestroy();
        for (int i=0; i<mActiveEngines.size(); i++) {
            mActiveEngines.get(i).detach();
}
        mActiveEngines.clear();
}

/**







