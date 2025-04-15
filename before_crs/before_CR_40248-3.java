/*StorageManager: fix issue that GREF has increased to 2011 in system server with intel stress test.

Issue description:
When run ICS stress test, always meet GREF issue. one of contributor is MountService$MountServiceBinderListener.
log info
19:21:11.609   222 24316 W dalvikvm:        24 of com.android.server.am.ActivityManagerService$AppDeathRecipient (24 unique instances)
19:21:11.609   222 24316 W dalvikvm:       479 of com.android.server.MountService$MountServiceBinderListener (479 unique instances)
19:21:11.619   222 24316 W dalvikvm:         7 of com.android.server.accessibility.AccessibilityManagerService$6 (7 unique instances)
Note: PID 222 is system server.

Issue alaysis:
Everyone can call getSystemService(Context.STORAGE_SERVICE) to get service.
When other service get StorageManager, StorageManager will new MountServiceBinderLister and
register a listener in MountService, which won't be unregistered. It's easy to generate a lot of
instance of unused MountService$MountServiceBinderListener in system server.

Issue fix:
So change the policy to be:
1. Doesn't new MountServiceBinderLister in construction.
2. when other service needs to register listener in StorageManager,
StorageManager will register listener with MountService.
3. When other service needs to unregister listener in StorageManager,
if there is no more other listeners in StorageManager, StorageManager
will unregister listener in MountService.

Change-Id:Iaaf889f44a1a5f62b9f65b3ab1b486c9b7dcaf7fAuthor: Chuanxiao Dong <chuanxiao.dong@intel.com>
Signed-off-by: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Signed-off-by: Bruce Beare  <bruce.j.beare@intel.com>*/
//Synthetic comment -- diff --git a/core/java/android/os/storage/StorageManager.java b/core/java/android/os/storage/StorageManager.java
//Synthetic comment -- index fbf512c..a4819d8 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
/*
* Our internal MountService binder reference
*/
    private IMountService mMountService;

/*
* The looper target for callbacks
//Synthetic comment -- @@ -304,8 +304,6 @@
return;
}
mTgtLooper = tgtLooper;
        mBinderListener = new MountServiceBinderListener();
        mMountService.registerListener(mBinderListener);
}


//Synthetic comment -- @@ -322,6 +320,15 @@
}

synchronized (mListeners) {
mListeners.add(new ListenerDelegate(listener));
}
}
//Synthetic comment -- @@ -347,7 +354,15 @@
break;
}
}
        }
}

/**







