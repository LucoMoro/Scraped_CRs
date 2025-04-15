/*SystemUI: Update the icon on no sim

Upon SIM removal, MobileActivityIconId is not set to
no sim icon id resulting in no sim icon not shown.

Change-Id:I69d3af0535ff82379b2d04d74f0fd0323458cd45Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 18682*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java b/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java
//Synthetic comment -- index bbb90c8..c35e40a 100644

//Synthetic comment -- @@ -1053,7 +1053,9 @@
combinedSignalIconId = mDataSignalIconId; // set by updateDataIcon()
mContentDescriptionCombinedSignal = mContentDescriptionDataType;
} else {
                mMobileActivityIconId = (IccCardConstants.State.ABSENT == mSimState) ?
                        R.drawable.stat_sys_no_sim : 0;
                combinedActivityIconId = mMobileActivityIconId;
}
}








