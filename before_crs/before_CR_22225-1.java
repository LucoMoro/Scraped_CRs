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
    private static final int RETRY_UNMOUNT_DELAY = 30; // in ms
    private static final int MAX_UNMOUNT_RETRIES = 4;

class UnmountCallBack {
final String path;
//Synthetic comment -- @@ -285,7 +283,7 @@

void handleFinished() {
if (DEBUG_UNMOUNT) Slog.i(TAG, "Unmounting " + path);
            doUnmountVolume(path, true);
}
}

//Synthetic comment -- @@ -313,7 +311,7 @@

@Override
void handleFinished() {
            int ret = doUnmountVolume(path, true);
if (observer != null) {
try {
observer.onShutDownComplete(ret);
//Synthetic comment -- @@ -351,52 +349,10 @@
if (DEBUG_UNMOUNT) Slog.i(TAG, "H_UNMOUNT_PM_DONE");
if (DEBUG_UNMOUNT) Slog.i(TAG, "Updated status. Processing requests");
mUpdatingStatus = false;
                    int size = mForceUnmounts.size();
                    int sizeArr[] = new int[size];
                    int sizeArrN = 0;
                    // Kill processes holding references first
                    ActivityManagerService ams = (ActivityManagerService)
                    ServiceManager.getService("activity");
                    for (int i = 0; i < size; i++) {
                        UnmountCallBack ucb = mForceUnmounts.get(i);
                        String path = ucb.path;
                        boolean done = false;
                        if (!ucb.force) {
                            done = true;
                        } else {
                            int pids[] = getStorageUsers(path);
                            if (pids == null || pids.length == 0) {
                                done = true;
                            } else {
                                // Eliminate system process here?
                                ams.killPids(pids, "unmount media");
                                // Confirm if file references have been freed.
                                pids = getStorageUsers(path);
                                if (pids == null || pids.length == 0) {
                                    done = true;
                                }
                            }
                        }
                        if (!done && (ucb.retries < MAX_UNMOUNT_RETRIES)) {
                            // Retry again
                            Slog.i(TAG, "Retrying to kill storage users again");
                            mHandler.sendMessageDelayed(
                                    mHandler.obtainMessage(H_UNMOUNT_PM_DONE,
                                            ucb.retries++),
                                    RETRY_UNMOUNT_DELAY);
                        } else {
                            if (ucb.retries >= MAX_UNMOUNT_RETRIES) {
                                Slog.i(TAG, "Failed to unmount media inspite of " +
                                        MAX_UNMOUNT_RETRIES + " retries. Forcibly killing processes now");
                            }
                            sizeArr[sizeArrN++] = i;
                            mHandler.sendMessage(mHandler.obtainMessage(H_UNMOUNT_MS,
                                    ucb));
                        }
                    }
                    // Remove already processed elements from list.
                    for (int i = (sizeArrN-1); i >= 0; i--) {
                        mForceUnmounts.remove(sizeArr[i]);
}
break;
}







