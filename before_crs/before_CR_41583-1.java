/*Progressbar: correct the first updated value for progressbar

This patch corrects the first updated value from 0 to 1. After
execute first operation, then need to update Progress to 1, not
0.

Change-Id:Ie3ae6bea79b3e50e727723d75de84b1797aaf382Author: Wu, Hao <hao.wu@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34729*/
//Synthetic comment -- diff --git a/src/com/android/gallery3d/ui/MenuExecutor.java b/src/com/android/gallery3d/ui/MenuExecutor.java
//Synthetic comment -- index 3619ca0..101d592 100644

//Synthetic comment -- @@ -393,7 +393,7 @@
if (!execute(manager, jc, mOperation, id)) {
result = EXECUTION_RESULT_FAIL;
}
                    onProgressUpdate(index++, mListener);
}
} catch (Throwable th) {
Log.e(TAG, "failed to execute operation " + mOperation







