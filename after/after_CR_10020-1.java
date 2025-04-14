Fix low storage issue on larger disks












diff --git a/services/java/com/android/server/DeviceStorageMonitorService.java b/services/java/com/android/server/DeviceStorageMonitorService.java
index 85861bb..8d38d92 100644

@@ -120,7 +120,7 @@

private final void restatDataDir() {
mFileStats.restat(DATA_PATH);
        mFreeMem = (long)mFileStats.getAvailableBlocks() * (long)mBlkSize;
// Allow freemem to be overridden by debug.freemem for testing
String debugFreeMem = SystemProperties.get("debug.freemem");
if (!"".equals(debugFreeMem)) {
@@ -251,7 +251,7 @@
//initialize block size
mBlkSize = mFileStats.getBlockSize();
//initialize total storage on device
        mTotalMemory = ((long)mFileStats.getBlockCount() * (long)mBlkSize)/100;
mStorageLowIntent = new Intent(Intent.ACTION_DEVICE_STORAGE_LOW);
mStorageOkIntent = new Intent(Intent.ACTION_DEVICE_STORAGE_OK);
checkMemory(true);







