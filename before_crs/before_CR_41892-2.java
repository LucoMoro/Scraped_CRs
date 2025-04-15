/*MtpService: check primary storage status when use it in PTP mode

Using single storage solution means its primary storage is physical SD
card. So, it is possible for user to insert a non-Vfat SD card to make
primary storage not mounted.
So, before using primary storage in PTP mode, let's have a check first

Change-Id:I3932c16399675232d8b2329c8e30cf6030b3586eAuthor: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 30725*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MtpService.java b/src/com/android/providers/media/MtpService.java
//Synthetic comment -- index fce8360..545fcde 100644

//Synthetic comment -- @@ -50,7 +50,13 @@
private void addStorageDevicesLocked() {
if (mPtpMode) {
// In PTP mode we support only primary storage
            addStorageLocked(mVolumeMap.get(mVolumes[0].getPath()));
} else {
for (StorageVolume volume : mVolumeMap.values()) {
addStorageLocked(volume);







