/*ProgressBar: Fix error of process bar cannot update on some condition.

When onDetachedFromWindow in processbar, if flag mRefreshIsPosted is true,
we should set it to false. Otherwise, this flag will stay in true status,
cannot turn to false anymore. At that condition, processbar cannot get
update.

Change-Id:I14c4e976b165ad737aae0a403a44822a7b3b2422Author: Liang Wang <liangx.wang@intel.com>
Signed-off-by: Liang Wang <liangx.wang@intel.com>
Signed-off-by: Bing Deng <bingx.deng@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 61090*/
//Synthetic comment -- diff --git a/core/java/android/widget/ProgressBar.java b/core/java/android/widget/ProgressBar.java
//Synthetic comment -- index ea50e2e..8f71d8c 100644

//Synthetic comment -- @@ -1214,7 +1214,7 @@
removeCallbacks(mRefreshProgressRunnable);
}
if (mRefreshProgressRunnable != null && mRefreshIsPosted) {
            removeCallbacks(mRefreshProgressRunnable);
}
if (mAccessibilityEventSender != null) {
removeCallbacks(mAccessibilityEventSender);







