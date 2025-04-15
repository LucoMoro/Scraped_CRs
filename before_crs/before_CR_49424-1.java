/*Telephony: fix issue with data state reporting

In case of network lost for short duration, network may not
disconnect the data call. When the network is back, same data
connection may still be alive.

DISCONNECTED should be informed only when the network or modem informs
of the disconnection.

Change-Id:I65b52f64074b93d77cf77a09bd04cbd7a5ed6cb2Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34445*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index b930348..b0190b1 100644

//Synthetic comment -- @@ -285,11 +285,6 @@
// already been called

ret = PhoneConstants.DataState.DISCONNECTED;
        } else if (mSST.getCurrentGprsState()
                != ServiceState.STATE_IN_SERVICE) {
            // If we're out of service, open TCP sockets may still work
            // but no data will flow
            ret = PhoneConstants.DataState.DISCONNECTED;
} else if (mDataConnectionTracker.isApnTypeEnabled(apnType) == false ||
mDataConnectionTracker.isApnTypeActive(apnType) == false) {
//TODO: isApnTypeActive() is just checking whether ApnContext holds







