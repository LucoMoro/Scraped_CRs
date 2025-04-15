/*StorageManager: fix android.process.media process stop while plug a discontinuous partitioned SD Card

While get volume state, throw an IllegalArgumentException, catch
this exception in StorageManager and return null to MtpService to
avoid process stoping.

Change-Id:I7d32e3007296eca5903e6753f6a3395be4bf48c9Author: Gang Li <gang.g.li@intel.com>
Signed-off-by: Gang Li <gang.g.li@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 47459*/
//Synthetic comment -- diff --git a/core/java/android/os/storage/StorageManager.java b/core/java/android/os/storage/StorageManager.java
//Synthetic comment -- index a4819d8..7386620 100644

//Synthetic comment -- @@ -556,6 +556,9 @@
} catch (RemoteException e) {
Log.e(TAG, "Failed to get volume state", e);
return null;
}
}








