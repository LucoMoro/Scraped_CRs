/*MountService: correct the storage patch

When booting with a special SD card which let Volume state in pending
state, MountService will change the storage state wrongly to another patch.
Like if volume /mnt/sdcard_ext is pending, MountService will update /mnt/sdcard
volume state wrongly.

This patch can fix this.

Change-Id:Ia86231cf5a87f8b3e54098bb8f57a06e4237788aAuthor: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34100*/
//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 04267a3..0ea3c82 100644

//Synthetic comment -- @@ -651,6 +651,7 @@
/**
* Determine media state and UMS detection status
*/
try {
final String[] vols = NativeDaemonEvent.filterMessageList(
mConnector.executeForList("volume", "list"),
//Synthetic comment -- @@ -658,7 +659,7 @@
for (String volstr : vols) {
String[] tok = volstr.split(" ");
// FMT: <label> <mountpoint> <state>
                        String path = tok[1];
String state = Environment.MEDIA_REMOVED;

int st = Integer.parseInt(tok[2]);
//Synthetic comment -- @@ -683,7 +684,8 @@
}
} catch (Exception e) {
Slog.e(TAG, "Error processing initial volume state", e);
                    updatePublicVolumeState(mExternalStoragePath, Environment.MEDIA_REMOVED);
}

/*







