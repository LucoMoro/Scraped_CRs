/*[Audio]Press camera button during Http live Audio playing, then return Browser, DUT display abnormal.

When long press camera key, dispatchKeyEvent function will add control surface after the control view hide,
actually, we needn't show control view after we press camera key.

Change-Id:I45175c4eb97b3f13469cd9d8bacb295c9c5803ddAuthor: Bin Xu <bxu10X@intel.com>
Signed-off-by: Bin Xu <bxu10X@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 28344*/
//Synthetic comment -- diff --git a/core/java/android/widget/MediaController.java b/core/java/android/widget/MediaController.java
//Synthetic comment -- index fc35f05..f76ab2b 100644

//Synthetic comment -- @@ -477,7 +477,8 @@
return true;
} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
|| keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
// don't show the controls for volume adjustment
return super.dispatchKeyEvent(event);
} else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {







