/*GPS: Changes to enable on-demand data call flow for CDMA connections

In the CDMA case we do not get a valid APN on a data connection request
to the connectivity manager. A fix is put in so that even if we get a
null APN we populate this field before we call native_agps_data_conn_open()
method to avoid a run time exception.

Change-Id:I134ead5d8b177fced9b14756c6bd8199a2b9c35d*/




//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index b1ab05b..834119a 100755

//Synthetic comment -- @@ -496,7 +496,12 @@
if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE_SUPL
&& mAGpsDataConnectionState == AGPS_DATA_CONNECTION_OPENING) {
String apnName = info.getExtraInfo();
            if (mNetworkAvailable) {
                if (apnName == null) {
                    /* Assign a dummy value in the case of C2K as otherwise we will have a runtime 
                    exception in the following call to native_agps_data_conn_open*/
                    apnName = "dummy-apn";
                }
mAGpsApn = apnName;
if (DEBUG) Log.d(TAG, "call native_agps_data_conn_open");
native_agps_data_conn_open(apnName);







