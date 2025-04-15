/*Wait for IDLE state before mounting on insert

As soon as we recive the insert event we try to
mount the mmc sending a "volume mount" command.
But, the volume manager sometimes is still on
"pending" state, and the insertion mount will
fail.

This patch calls the doMount command on insert
only after the state has changed to idle.
This way, the mount will not fail, because the
state is pending.

Change-Id:I1f8f13c8e232a6b9159eeb78a9484838f1c600afSigned-off-by: Axel Haslam <axelhaslam@ti.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
old mode 100644
new mode 100755
//Synthetic comment -- index cb55808..a158c26

//Synthetic comment -- @@ -136,6 +136,8 @@
private static final int RETRY_UNMOUNT_DELAY = 30; // in ms
private static final int MAX_UNMOUNT_RETRIES = 4;

class UnmountCallBack {
String path;
int retries;
//Synthetic comment -- @@ -495,6 +497,26 @@
* Format: "NNN Volume <label> <path> state changed
* from <old_#> (<old_str>) to <new_#> (<new_str>)"
*/
notifyVolumeStateChange(
cooked[2], cooked[3], Integer.parseInt(cooked[7]),
Integer.parseInt(cooked[10]));
//Synthetic comment -- @@ -526,18 +548,11 @@
}

if (code == VoldResponseCode.VolumeDiskInserted) {
                new Thread() {
                    public void run() {
                        try {
                            int rc;
                            if ((rc = doMountVolume(path)) != StorageResultCode.OperationSucceeded) {
                                Slog.w(TAG, String.format("Insertion mount failed (%d)", rc));
                            }
                        } catch (Exception ex) {
                            Slog.w(TAG, "Failed to mount media on insertion", ex);
                        }
                    }
                }.start();
} else if (code == VoldResponseCode.VolumeDiskRemoved) {
/*
* This event gets trumped if we're already in BAD_REMOVAL state







