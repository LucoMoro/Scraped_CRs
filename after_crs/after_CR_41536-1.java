/*fix surfaceview bug: when surface format changed should not call surfaceDestroyed

rootcause: formatchange should not destroy surface

Change-Id:Ib431206a213d7d56f7079c86f9119981de32c39cAuthor: Hongyu Zhang <hongyu.zhang@intel.com>
Signed-off-by: b529 <b529@borqs.com>
Signed-off-by: Hongyu Zhang <hongyu.zhang@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 37272*/




//Synthetic comment -- diff --git a/core/java/android/view/SurfaceView.java b/core/java/android/view/SurfaceView.java
//Synthetic comment -- index ed4c75c..0e51f62 100644

//Synthetic comment -- @@ -518,7 +518,7 @@

final boolean surfaceChanged =
(relayoutResult&WindowManagerImpl.RELAYOUT_RES_SURFACE_CHANGED) != 0;
                    if (mSurfaceCreated && (surfaceChanged || (!visible && visibleChanged)) && !formatChanged) {
mSurfaceCreated = false;
if (mSurface.isValid()) {
if (DEBUG) Log.i(TAG, "visibleChanged -- surfaceDestroyed");







