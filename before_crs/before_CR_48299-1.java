/*Launcher: fix crash when call new LoaderTask thread's runBindSynchronousPage before old LoaderTask thread stopped.

when mIsLoaderTaskRunning is true it will raise up java.lang.RuntimeException.

Make sure mIsLoaderTaskRunning == false when new LoaderTask ready to run.
stopLoader() will be called everytime before startLoader() then it can
avoid java.lang.RuntimeExcepion.

Change-Id:I5d0ab5412d535cd910f5466ae47532fee6858e77Author: Jun Wu <junx.wu@intel.com>
Signed-off-by: Jun Wu <junx.wu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 66274 67779*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/LauncherModel.java b/src/com/android/launcher2/LauncherModel.java
//Synthetic comment -- index 7302310..4d95617 100644

//Synthetic comment -- @@ -916,6 +916,7 @@
synchronized (mLock) {
if (mLoaderTask != null) {
mLoaderTask.stopLocked();
}
}
}







