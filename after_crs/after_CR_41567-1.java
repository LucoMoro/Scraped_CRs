/*Services: Adding HSPAP info in Android

HSPA+ management is missing in GPS Loc service.

Change-Id:If828c1b078d5989f501c3eb917a573a6db773453Author: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Regnier, PhilippeX <philippex.regnier@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 41786*/




//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index 4ad6140..006d85e 100755

//Synthetic comment -- @@ -1530,7 +1530,8 @@
if (networkType == TelephonyManager.NETWORK_TYPE_UMTS
|| networkType == TelephonyManager.NETWORK_TYPE_HSDPA
|| networkType == TelephonyManager.NETWORK_TYPE_HSUPA
                    || networkType == TelephonyManager.NETWORK_TYPE_HSPA
                    || networkType == TelephonyManager.NETWORK_TYPE_HSPAP) {
type = AGPS_REF_LOCATION_TYPE_UMTS_CELLID;
} else {
type = AGPS_REF_LOCATION_TYPE_GSM_CELLID;







