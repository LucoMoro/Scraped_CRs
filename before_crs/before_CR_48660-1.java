/*Services: Adding HSPAP info in Android

HSPA+ management is missing in GPS Loc service.

Change-Id:I7134f1b465d3d8be5c75d08c4b8090df816d7099Author: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Regnier, PhilippeX <philippex.regnier@intel.com>
Signed-off-by: Fabien Peix <fabien.peix@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 41786*/
//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index c272da4..22c52f4 100644

//Synthetic comment -- @@ -1464,7 +1464,8 @@
if (networkType == TelephonyManager.NETWORK_TYPE_UMTS
|| networkType == TelephonyManager.NETWORK_TYPE_HSDPA
|| networkType == TelephonyManager.NETWORK_TYPE_HSUPA
                    || networkType == TelephonyManager.NETWORK_TYPE_HSPA) {
type = AGPS_REF_LOCATION_TYPE_UMTS_CELLID;
} else {
type = AGPS_REF_LOCATION_TYPE_GSM_CELLID;







