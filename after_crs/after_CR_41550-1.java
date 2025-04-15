/*Phone browser application stopped while connect BT headset and wsheadset browsing video streaming.

If we tap the full screen button quickly in video playing webpage,
first play is playing and enter to full screen mode,
and second action will check first play status and release something,
 so the error happens, when play same videos,
I add free action and set flags to default
before we start to play second video in full screen mode.

Change-Id:Ia980cc1b77f983bce0fbd0c04931a4627d819e96Author: Bin Xu <bxu10X@intel.com>
Signed-off-by: Bin Xu <bxu10X@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 44018*/




//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoViewProxy.java b/core/java/android/webkit/HTML5VideoViewProxy.java
//Synthetic comment -- index ab884df..23af1d3 100644

//Synthetic comment -- @@ -159,6 +159,12 @@
// If we are playing the same video, then it is better to
// save the current position.
if (layerId == mHTML5VideoView.getVideoLayerId()) {
                        //if we double tapped the full screen button, we must release
                        //some variable and set flag to default.
                        WebChromeClient client = webView.getWebChromeClient();
                        if (client != null) {
                            client.onHideCustomView();
                        }
savePosition = mHTML5VideoView.getCurrentPosition();
canSkipPrepare = (playerState == HTML5VideoView.STATE_PREPARING
|| playerState == HTML5VideoView.STATE_PREPARED







