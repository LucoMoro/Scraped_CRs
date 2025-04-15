/*Unmount is too brutal

Unmounting SD card with force=true would kill processes before
broadcasting MEDIA_EJECT not giving them a chance to react.

This change removes the killing of processes in MountService,
thus letting them handle MEDIA_EJECT (they get 10 seconds to
react). If they ignore MEDIA_EJECT and keep their files open
they will be killed at a later stage (in Volume.cpp:496) if
force is true.

Change-Id:I16aa5fe3d8a44ef2d4b4c2197aa5a5f72685cd98*/




//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 4d934b6..6281321 100644

//Synthetic comment -- @@ -269,8 +269,6 @@
private static final int H_UNMOUNT_PM_UPDATE = 1;
private static final int H_UNMOUNT_PM_DONE = 2;
private static final int H_UNMOUNT_MS = 3;

class UnmountCallBack {
final String path;
//Synthetic comment -- @@ -285,7 +283,7 @@

void handleFinished() {
if (DEBUG_UNMOUNT) Slog.i(TAG, "Unmounting " + path);
            doUnmountVolume(path, force);
}
}

//Synthetic comment -- @@ -313,7 +311,7 @@

@Override
void handleFinished() {
            int ret = doUnmountVolume(path, force);
if (observer != null) {
try {
observer.onShutDownComplete(ret);
//Synthetic comment -- @@ -351,52 +349,10 @@
if (DEBUG_UNMOUNT) Slog.i(TAG, "H_UNMOUNT_PM_DONE");
if (DEBUG_UNMOUNT) Slog.i(TAG, "Updated status. Processing requests");
mUpdatingStatus = false;
                    // Do unmount for all queued unmount's
                    while (mForceUnmounts.size() > 0) {
                        UnmountCallBack ucb = mForceUnmounts.remove(0);
                        mHandler.sendMessage(mHandler.obtainMessage(H_UNMOUNT_MS, ucb));
}
break;
}







