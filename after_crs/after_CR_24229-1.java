/*frameworks/base: Fix for the race in Wallpaperservice

Wallpaperservice has a race among message handler of
DO_DETACH and service's onDestroy. In certain cases,
the engine kept track in mActiveEngines is removed by
message handler of DO_DETACH and service's onDestroy
doesn't get a entry in mActivieEngines and isn't able
to invoke detach. This keeps the broadcast receiver,
mReceiver active and is unregistered by the framework
and a corresponding leak is reported.

  Later, the message handler of DO_DETACH continues
and invokes detach on mEngine, which attempts to
unregister mReceiver and framework throws an exception
"Receiver not registered: android.service.wallpaper.
WallpaperService$Engine$1@40874580" and causes the
framework to reboot.

  In case of system_server, WindownManagerPolicy and
android.server.ServerThread contest for access of
shared members, mActiveEngines and mEgine. Fix is to
protect the critical section via synchronized block.

Change-Id:Ice79cb8344fb54bcc6e9f76f4b143daac2608d7b*/




//Synthetic comment -- diff --git a/core/java/android/service/wallpaper/WallpaperService.java b/core/java/android/service/wallpaper/WallpaperService.java
//Synthetic comment -- index 9f362d3..b6656fa 100644

//Synthetic comment -- @@ -874,13 +874,19 @@
}
Engine engine = onCreateEngine();
mEngine = engine;
                    synchronized (mActiveEngines) {
                        mActiveEngines.add(engine);
                    }
engine.attach(this);
return;
}
case DO_DETACH: {
                    synchronized (mEngine.mLock) {
                        mEngine.detach();
                    }
                    synchronized (mActiveEngines) {
                        mActiveEngines.remove(mEngine);
                    }
return;
}
case DO_SET_DESIRED_SIZE: {
//Synthetic comment -- @@ -958,10 +964,15 @@
@Override
public void onDestroy() {
super.onDestroy();
        synchronized (mActiveEngines) {
           for (int i=0; i<mActiveEngines.size(); i++) {
                Engine engine = mActiveEngines.get(i);
                synchronized (engine.mLock) {
                   engine.detach();
                }
           }
           mActiveEngines.clear();
}
}

/**







