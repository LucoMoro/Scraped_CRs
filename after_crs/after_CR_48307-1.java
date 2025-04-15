/*Phone: Ignore restart ringer once silent requested when incoming call

Issue: When incall coming, press volume key can make the ringing silent, but
after you select SMS and cancel the dialog box, the ringer re-started.

Solution: Once silent requested when incoming call, keep ringer tone silent
until the call end.

Change-Id:Ie1288b310eb9d95dbb3f2be4e5951dd7e1d2ab7fAuthor: Zhuangzhi Li <zhuangzhi.li@intel.com>
Signed-off-by: Zhuangzhi Li <zhuangzhi.li@intel.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 32156*/




//Synthetic comment -- diff --git a/src/com/android/phone/CallNotifier.java b/src/com/android/phone/CallNotifier.java
//Synthetic comment -- index 99636df..050de62 100755

//Synthetic comment -- @@ -1331,7 +1331,7 @@
*/
/* package */ void restartRinger() {
if (DBG) log("restartRinger()...");
        if (isRinging() || mSilentRingerRequested) return;

final Call ringingCall = mCM.getFirstActiveRingingCall();
// Don't check ringingCall.isRinging() here, since that'll be true







