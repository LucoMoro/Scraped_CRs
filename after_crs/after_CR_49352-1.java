/*Telephony: Call onHangupLocal on rejectCall request

Upon call disconnected state, disconnect cause
is checked for determining whether the hangup is
local or missed call. Currently, cause is not
set on user rejecting the call.

Based on the cause, UI message "incoming" or "missed"
call will be shown. So set the cause when the
user rejects the call.

Change-Id:I49f627742787c1423a0029fca6ccca90c99688b5Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 31496*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index 2080976..8e2ff54 100644

//Synthetic comment -- @@ -267,6 +267,8 @@
// so if the phone isn't ringing, this could hang up held
if (ringingCall.getState().isRinging()) {
cm.rejectCall(obtainCompleteMessage());
            ringingCall.onHangupLocal();
            phone.notifyPreciseCallStateChanged();
} else {
throw new CallStateException("phone not ringing");
}







