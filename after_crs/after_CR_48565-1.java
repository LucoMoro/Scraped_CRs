/*stk: add support of duration for GET INPUT/INKEY

According to the 3GPP spec, the GET INPUT and GET INKEY
proactive commands can have a duration tag. If this tag
is not handle, the sequence 8.1 of the conformance test
27.22.4.2.8 of the 3GPP 31.124 will be failed.

This patch add the support of the duration tag for the
GET INPUT and GET INKEY proactive commands.

Change-Id:Ic22e6f5727cbf9b3b96082031681fd4d9518f1c9Author: Guillaume Lucas <guillaume.lucas@intel.com>
Signed-off-by: Guillaume Lucas <guillaume.lucas@intel.com>
Signed-off-by: Nizar Haouati <nizar.haouati@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 2944*/




//Synthetic comment -- diff --git a/src/com/android/stk/StkInputActivity.java b/src/com/android/stk/StkInputActivity.java
//Synthetic comment -- index b6228fb..2cae44f 100644

//Synthetic comment -- @@ -257,8 +257,12 @@

private void startTimeOut() {
cancelTimeOut();
        int dialogDuration = StkApp.calculateDurationInMilis(mStkInput.duration);
        if (dialogDuration == 0) {
            dialogDuration = StkApp.UI_TIMEOUT;
        }
mTimeoutHandler.sendMessageDelayed(mTimeoutHandler
                .obtainMessage(MSG_ID_TIMEOUT), dialogDuration);
}

private void configInputDisplay() {







