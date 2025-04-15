/*Launcher: Fix the Null point issue for mWorkspace

mWorkspace may have been destroyed when onAnimationEnd is called

Change-Id:I5df190a236bd591b9e363ca4bedb1460f4f83249Author: Yong Yao <yong.yao@intel.com>
Signed-off-by: Yong Yao <yong.yao@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 51436*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index c221815..8ddb525 100644

//Synthetic comment -- @@ -2537,7 +2537,9 @@

if (mWorkspace != null && !springLoaded && !LauncherApplication.isScreenLarge()) {
// Hide the workspace scrollbar
                        mWorkspace.hideScrollingIndicator(true);
hideDockDivider();
}
if (!animationCancelled) {







