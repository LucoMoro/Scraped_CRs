/*Phone browser application stopped while connect BT headset and WS headset browsing video streaming.

Error is occurred by double tapping in full screen button, When we tap full
screen button to enter full screen mode, we should check current status,
and must reset tags if device is in full screen mode.

Change-Id:I2d84c7a17b5703f4e834816a5dd6cc8f48389b1eAuthor: bxu10X <bxu10X@intel.com>
Signed-off-by: bxu10X <bxu10X@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 44018*/
//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoViewProxy.java b/core/java/android/webkit/HTML5VideoViewProxy.java
//Synthetic comment -- index a3d62ae..bfadb23 100644

//Synthetic comment -- @@ -154,6 +154,12 @@
// If we are playing the same video, then it is better to
// save the current position.
if (layerId == mHTML5VideoView.getVideoLayerId()) {
savePosition = mHTML5VideoView.getCurrentPosition();
canSkipPrepare = (playerState == HTML5VideoView.STATE_PREPARING
|| playerState == HTML5VideoView.STATE_PREPARED







