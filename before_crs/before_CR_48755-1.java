/*StorageManager: fix android.process.media process stop while plug a discontinuous partitioned SD Card

While get volume state, throw an IllegalArgumentException, catch
this exception in StorageManager and return null to MtpService to
avoid process stoping.

Change-Id:Iebb5c38e375d3f54505d9b9d64fcd0f02a2b60c1Author: Li Gang <gang.g.li@intel.com>
Signed-off-by: Li Gang <gang.g.li@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 47459*/
//Synthetic comment -- diff --git a/core/java/android/os/storage/StorageManager.java b/core/java/android/os/storage/StorageManager.java
//Synthetic comment -- index 862a95c..91e9e6a 100644

//Synthetic comment -- @@ -556,6 +556,9 @@
} catch (RemoteException e) {
Log.e(TAG, "Failed to get volume state", e);
return null;
}
}








