/*TOMBSTONE in com.android.browser (- android_atomic_inc+11)

there are race condition in save history webpage, at the same time,
browser destory the tab and webview, so we synchronized the tab to
resolve conflict.

Change-Id:Ib30a1215023542440e39251e8e0cd587db0ee11eAuthor: binx.xu <binx.xu@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 62273*/




//Synthetic comment -- diff --git a/src/com/android/browser/TabControl.java b/src/com/android/browser/TabControl.java
//Synthetic comment -- index 150ece0..dd9134b 100644

//Synthetic comment -- @@ -257,8 +257,10 @@
mCurrentTab = getTabPosition(current);
}

        synchronized (t) {
// destroy the tab
            t.destroy();
        }
// clear it's references to parent and children
t.removeFromTree();








