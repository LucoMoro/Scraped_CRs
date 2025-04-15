/*Telephony: Fix issue in notifying disconnect completed

Currently, on Inactive state notifyDisconnectCompleted is
called with sendAll set to TRUE. This results in
DisconnectCompleted to all the contexts in the apnlist.
With multiple PDP context, there can be more than
1 apn in the apncontextlist.

With this patch, notifyDisconnectCompleted will be
called with sendAll set to false. This way,
NotifyDisconnectCompleted will be sent for only the
reported apnContext.

Change-Id:I2d2460dedbc6f38293aee14e05d18dca3a4ec428Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 44786*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnection.java b/src/java/com/android/internal/telephony/DataConnection.java
//Synthetic comment -- index 9751040..4c16133 100644

//Synthetic comment -- @@ -852,7 +852,7 @@
}
if (mDisconnectParams != null) {
if (VDBG) log("DcInactiveState: enter notifyDisconnectCompleted");
                notifyDisconnectCompleted(mDisconnectParams, true);
}
clearSettings();
}







