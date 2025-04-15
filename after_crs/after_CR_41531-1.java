/*GPS: SUPL early data connection fix

if GPS started before data connection is alive, SUPL will be broken for
      entire power cycle (fix in GpsLocationProvider.java)

Change-Id:I6a85420f96eeb0699e207c0527146427106a3295Author: Fabien Peix <fabienx.peix@intel.com>
Signed-off-by: Fabien Peix <fabienx.peix@intel.com>
Signed-off-by: Shah Hossain <shahadat.hossain@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 30370*/




//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index 4ad6140..a1e5291 100755

//Synthetic comment -- @@ -536,6 +536,8 @@
if (DEBUG) Log.d(TAG, "call native_agps_data_conn_failed");
mAGpsApn = null;
mAGpsDataConnectionState = AGPS_DATA_CONNECTION_CLOSED;
                mConnMgr.stopUsingNetworkFeature(
                        ConnectivityManager.TYPE_MOBILE, Phone.FEATURE_ENABLE_SUPL);
native_agps_data_conn_failed();
}
}







