/*MultiWaveView: The lock icon moves up after touching the right area of the unlock screen.

Issue Description:
After Xoom tablet boots up, you will find the Lock icon doesn't show in the middle of screen in vertical direction.
Touching the right area of the unlock screen at this time, the lock icon will move up to the middle of screen in vertical direction.
The issue won't be seen again until reboot tablet.

Root Cause:
The lock icon should be shown in the middle of screen in vertical direction.
Xoom is 800 pixel height. The UI height is 800 after booting before the TabletStatusBarView initialization.
The UI height is 752 after TabletStatusBarView initialization.
The icon is only first initialized at point (320,400) in performInitialLayout function.
The icon can not be moved to point(320,376) when layout is changing to 752 height without touching.

Solution:
Force move to right location in onLayout function.

Notes:
This issue can be reproduced on moto Xoom tablet.

Change-Id:I1887c8adf1434566b3cf768ea5bde102399c6418Signed-off-by: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Zhenghua Wang <zhenghua.wang@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/multiwaveview/MultiWaveView.java b/core/java/com/android/internal/widget/multiwaveview/MultiWaveView.java
//Synthetic comment -- index ebd355a..e59b911 100644

//Synthetic comment -- @@ -840,6 +840,7 @@
mOuterRing.setX(mWaveCenterX);
mOuterRing.setY(Math.max(mWaveCenterY, mWaveCenterY));

            moveHandleTo(mWaveCenterX, mWaveCenterY, false);
updateTargetPositions();
}
if (DEBUG) dump();







