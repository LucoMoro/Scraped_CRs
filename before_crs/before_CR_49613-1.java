/*SUPL early data connection fix

if GPS started before data connection is alive, SUPL will be broken for
entire power cycle (fix in GpsLocationProvider.java)

Change-Id:I2f4b072dc4f48e6f2aa3635df7a540195a87d9e9Author: Shah Hossain <shahadat.hossain@intel.com>
Signed-off-by: Shah Hossain <shahadat.hossain@intel.com>
Signed-off-by: Fabien Peix <fabien.peix@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 30370*/
//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index c272da4..50619a4 100644

//Synthetic comment -- @@ -568,6 +568,8 @@
if (DEBUG) Log.d(TAG, "call native_agps_data_conn_failed");
mAGpsApn = null;
mAGpsDataConnectionState = AGPS_DATA_CONNECTION_CLOSED;
native_agps_data_conn_failed();
}
}







