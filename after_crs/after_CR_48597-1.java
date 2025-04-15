/*Pop-up option menu can't be taped when unlock the screen.

There are 3 window(Subsettings,Add Window, PopupWindow) at Subsettings activity.
After resume processing. The subsettings relayout process just change
the focus to "Add Window" not PopupWindow.
The PopupWindow's AbsListView accept the input event, but not do PerformClick because
The AbsListView is not at focus window.

Add the flag to let the WMS know that WMS should updateFocusWindow
after the PopupWindow's animation finish.

Change-Id:I74ef166fcb46aff5881fa4de301ce947f63f40ccAuthor: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Jun Wu <junx.wu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 68374*/




//Synthetic comment -- diff --git a/services/java/com/android/server/wm/WindowStateAnimator.java b/services/java/com/android/server/wm/WindowStateAnimator.java
//Synthetic comment -- index 7b30c89..d4ffc64 100644

//Synthetic comment -- @@ -1418,6 +1418,7 @@
// loop, this will cause it to restart with a new
// layout.
c.mDisplayContent.layoutNeeded = true;
                        mService.mFocusMayChange = true;
}
}
}







