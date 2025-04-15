/*Telephony: Call Forwarding items are grayed when wrong numbers are used

In call forwarding settings (always, busy, unanswered, unreachable), when us
enter an empty or too long phone number, a pop-up with an error message
appears and button is grayed.
If user wants to modify the settings again, it is necessary to go in sleep
mode or go back and re-enter in the menu.
With this patch, pop-up is still available and button is never grayed.
When user enter a wrong phone number, button is still available.

Change-Id:I0efdcdf55af3f89876a7bc455296292b769ff825Author: Tony Goubert <tonyx.goubert@intel.com>
Signed-off-by: Tony Goubert <tonyx.goubert@intel.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 52109*/




//Synthetic comment -- diff --git a/src/com/android/phone/TimeConsumingPreferenceActivity.java b/src/com/android/phone/TimeConsumingPreferenceActivity.java
//Synthetic comment -- index 19c4dda..479dd94 100644

//Synthetic comment -- @@ -172,7 +172,6 @@
if (mIsForeground) {
showDialog(error);
}
}

@Override







