/*MediaControll: Change the icon status to pause after tapping "pause" button

Fix the Android issue - can't get status of MediaPlayer immediately in MediaController
after user action

Change-Id:I6a88f5c8792302943d0bd8e729da778664bc70b3Author: Weian Chen <weian.chen@intel.com>
Signed-off-by: Weian Chen <weian.chen@intel.com>
Signed-off-by: Chen Jun<jun.d.chen@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 48896, 21813, 6113*/
//Synthetic comment -- diff --git a/core/java/android/widget/MediaController.java b/core/java/android/widget/MediaController.java
old mode 100644
new mode 100755
//Synthetic comment -- index fc35f05..966b558

//Synthetic comment -- @@ -380,13 +380,18 @@
int pos;
switch (msg.what) {
case FADE_OUT:
                    hide();
break;
case SHOW_PROGRESS:
pos = setProgress();
if (!mDragging && mShowing && mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
sendMessageDelayed(msg, 1000 - (pos % 1000));
}
break;
}







