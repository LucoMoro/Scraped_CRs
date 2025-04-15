/*fix surfaceview bug when surface format changed should not call surfaceDestroyed

formatchange should not destroy surface

Change-Id:I20fa219a4a8ecb7b504518f5eba7e342dd55c569Author: Hongyu Zhang <hongyu.zhang@intel.com>
Signed-off-by: b529 <b529@borqs.com>
Signed-off-by: Hongyu Zhang <hongyu.zhang@intel.com>
Signed-off-by: Bing Deng <bingx.deng@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 37272*/




//Synthetic comment -- diff --git a/core/java/android/view/SurfaceView.java b/core/java/android/view/SurfaceView.java
//Synthetic comment -- index 9008521..3729afe 100644

//Synthetic comment -- @@ -544,7 +544,7 @@

final boolean surfaceChanged = (relayoutResult
& WindowManagerGlobal.RELAYOUT_RES_SURFACE_CHANGED) != 0;
                    if (mSurfaceCreated && (surfaceChanged || (!visible && visibleChanged)) && !formatChanged) {
mSurfaceCreated = false;
if (mSurface.isValid()) {
if (DEBUG) Log.i(TAG, "visibleChanged -- surfaceDestroyed");







