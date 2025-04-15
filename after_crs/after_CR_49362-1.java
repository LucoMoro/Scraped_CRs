/*Telephony: SPN name not initialized.

Old spn record cleared upon instantiation and disposal
of SIMRecords.

Change-Id:I3eeab8d68104d81cf56a332a99980fe807d149c5Author: Javen Ni <javen.ni@borqs.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Javen Ni <javen.ni@borqs.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 11860*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/SIMRecords.java b/src/java/com/android/internal/telephony/uicc/SIMRecords.java
//Synthetic comment -- index 66eaf6a..c805361 100755

//Synthetic comment -- @@ -216,6 +216,7 @@
iccid = null;
// -1 means no EF_SPN found; treat accordingly.
spnDisplayCondition = -1;
        spn = null;
efMWIS = null;
efCPHS_MWI = null;
spdiNetworks = null;
//Synthetic comment -- @@ -1698,4 +1699,4 @@
pw.println(" mUsimServiceTable=" + mUsimServiceTable);
pw.flush();
}
\ No newline at end of file
}







