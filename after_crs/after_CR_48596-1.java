/*[Camera 2.0][Always]Camera force close when press delete button and gallery icon at the same time.

When press 2 point on Camera delete pop Item and gallery icon.
The system will dispatch ACTION_POINTER_UP event contain 2 point.
It will split 2 ACTION_UP event at LinearLayout.
One will call to camera app then dismiss the popup window.
The dismiss process will clear all TouchTarget.So mFirstTouchTarget will be null in the while circle.
The resetCancelNextUpFlag functioin is called after clearTouchTargets

Change-Id:Ifb061c412b8d6904f773f9ae9642eab3f5b28707Author: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Erjun Ding <erjunx.ding@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 69665*/




//Synthetic comment -- diff --git a/core/java/android/view/ViewGroup.java b/core/java/android/view/ViewGroup.java
//Synthetic comment -- index 00723f3..d08eb0d 100644

//Synthetic comment -- @@ -1907,7 +1907,7 @@
// dispatched to it.  Cancel touch targets if necessary.
TouchTarget predecessor = null;
TouchTarget target = mFirstTouchTarget;
                while (target != null && mFirstTouchTarget != null) {
final TouchTarget next = target.next;
if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
handled = true;







