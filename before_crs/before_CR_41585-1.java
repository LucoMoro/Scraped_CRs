/*Phone: Ignore restart ringer once silent requested when incoming call

Issue: When incall coming, press volume key can make the ringing silent, but
after you select SMS and cancel the dialog box, the ringer re-started.

Solution: Once silent requested when incoming call, keep ringer tone silent
until the call end.

Change-Id:I7261a9b39c7d0965c96971fda7031b385c7a084fAuthor: Zhuangzhi Li <zhuangzhi.li@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 32156*/
//Synthetic comment -- diff --git a/src/com/android/phone/CallNotifier.java b/src/com/android/phone/CallNotifier.java
//Synthetic comment -- index eb88c75..442941f 100755

//Synthetic comment -- @@ -1359,7 +1359,8 @@
*/
/* package */ void restartRinger() {
if (DBG) log("restartRinger()...");
        if (isRinging()) return;  // Already ringing; no need to restart.

final Call ringingCall = mCM.getFirstActiveRingingCall();
// Don't check ringingCall.isRinging() here, since that'll be true







