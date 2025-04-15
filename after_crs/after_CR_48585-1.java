/*[Browser]Full screen progress bar displays on the windows screen during playing vp8 clips or http live streaming clips with html5.

When quit to window screen from full screen, hide the controller bar.

Change-Id:Ifac3c8f6bc0e9632364ecac85d55b706e9204778Author: Yuhan Xu <yuhanx.xu@intel.com>
Signed-off-by: Yuhan Xu <yuhanx.xu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 40809*/




//Synthetic comment -- diff --git a/core/java/android/widget/MediaController.java b/core/java/android/widget/MediaController.java
//Synthetic comment -- index f76ab2b..8ca29cb 100644

//Synthetic comment -- @@ -478,7 +478,8 @@
} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
|| keyCode == KeyEvent.KEYCODE_VOLUME_UP
|| keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
                || keyCode == KeyEvent.KEYCODE_CAMERA
                || keyCode == KeyEvent.KEYCODE_SEARCH) {
// don't show the controls for volume adjustment
return super.dispatchKeyEvent(event);
} else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {







