/*Fix low storage issue on larger disks*/
//Synthetic comment -- diff --git a/services/java/com/android/server/DeviceStorageMonitorService.java b/services/java/com/android/server/DeviceStorageMonitorService.java
//Synthetic comment -- index 85861bb..8d38d92 100644

//Synthetic comment -- @@ -120,7 +120,7 @@

private final void restatDataDir() {
mFileStats.restat(DATA_PATH);
        mFreeMem = mFileStats.getAvailableBlocks()*mBlkSize;
// Allow freemem to be overridden by debug.freemem for testing
String debugFreeMem = SystemProperties.get("debug.freemem");
if (!"".equals(debugFreeMem)) {
//Synthetic comment -- @@ -251,7 +251,7 @@
//initialize block size
mBlkSize = mFileStats.getBlockSize();
//initialize total storage on device
        mTotalMemory = (mFileStats.getBlockCount()*mBlkSize)/100;
mStorageLowIntent = new Intent(Intent.ACTION_DEVICE_STORAGE_LOW);
mStorageOkIntent = new Intent(Intent.ACTION_DEVICE_STORAGE_OK);
checkMemory(true);







