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
//Synthetic comment -- index 18167b6..c22be6c 100644

//Synthetic comment -- @@ -878,6 +878,7 @@
}

void detach() {
if (mDestroyed) {
return;
}
//Synthetic comment -- @@ -919,6 +920,7 @@
mInputChannel = null;
}
}
}
}

//Synthetic comment -- @@ -996,13 +998,17 @@
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
//Synthetic comment -- @@ -1080,10 +1086,13 @@
@Override
public void onDestroy() {
super.onDestroy();
        for (int i=0; i<mActiveEngines.size(); i++) {
            mActiveEngines.get(i).detach();
}
        mActiveEngines.clear();
}

/**







