/*Telephony: Clear CallForwarding and Message waiting Indications

When SIM is removed the SIMRecords object is disposed, but
the relevant events to clear the Callforwarding and message
waiting icons are not send.

This patch clears the call forwarding and message waiting
indicators by clearing the internal member data and sending
out notifications for the change in data.

Change-Id:I8916494e04e6bc7096773c2a3d473faaf9915a04Author: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 64610*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/SIMRecords.java b/src/java/com/android/internal/telephony/uicc/SIMRecords.java
//Synthetic comment -- index 66eaf6a..1f15855 100755

//Synthetic comment -- @@ -218,6 +218,11 @@
spnDisplayCondition = -1;
efMWIS = null;
efCPHS_MWI = null;
spdiNetworks = null;
pnnHomeName = null;

//Synthetic comment -- @@ -1698,4 +1703,4 @@
pw.println(" mUsimServiceTable=" + mUsimServiceTable);
pw.flush();
}
}
\ No newline at end of file







