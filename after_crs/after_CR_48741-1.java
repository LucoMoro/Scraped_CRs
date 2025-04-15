/*StorageManager: only register MountService listener when needed

When other service get StorageManager, StorageManager will register a
listener in MountService, which won't be unregistered.

So change the policy to be:
1. when other service needs to register listener in StorageManager,
StorageManager will register listener in MountService.
2. When other service needs to unreg0ster listener in StorageManager,
if there is no more other listeners in StorageManager, StorageManager
will unregister listener in MountService

Change-Id:Ie59ad96279ac3b51f7a606fcfbbde0c130cf61dcAuthor: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Bing Deng <bingx.deng@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 30943*/




//Synthetic comment -- diff --git a/core/java/android/os/storage/StorageManager.java b/core/java/android/os/storage/StorageManager.java
//Synthetic comment -- index 862a95c..85d557a 100644

//Synthetic comment -- @@ -331,7 +331,7 @@
}

synchronized (mListeners) {
            if (mBinderListener == null && mMountService != null) {
try {
mBinderListener = new MountServiceBinderListener();
mMountService.registerListener(mBinderListener);
//Synthetic comment -- @@ -365,7 +365,7 @@
break;
}
}
            if (mListeners.size() == 0 && mBinderListener !=null && mMountService != null) {
try {
mMountService.unregisterListener(mBinderListener);
} catch (RemoteException rex) {







