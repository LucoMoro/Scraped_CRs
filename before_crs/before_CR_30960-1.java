/*Fixes update of extras Bundle in new Locations for GPS Provider

This patch fixes the update of the extras Bundle in the Location object each time the platform derives a new GPS location and passes it to listening applications via LocationListener.onLocationChanged().  Sometime between Android 1.6 and 2.1 a bug was introduced that stopped any extras calculated by the platform from being added into the Location object, which means they were never passed up to any LocationListeners for the GPS Provider.  This manifested as an issue where the number of satellites used to derive a fix always reported “0” when retrieved from Location.getExtras() (Issue 4810 on the Android Issues page -http://code.google.com/p/android/issues/detail?id=4810).

Sample code to be used within LocationListener.onLocationChanged(Location location) which demonstrates this problem:

Bundle extras = location.getExtras();

Int numSats = extras.getInt("satellites");  //This always reports “0” post 1.5, although in 1.5 it properly reported the number of satellites used to derive this location

The “satellites” extra key/value pair for Locations is defined inhttp://developer.android.com/reference/android/location/Location.html#getExtras()This patch modifies GPSLocationProvider.java to properly update the Location object with the extras Bundle before the Location object is passed to any listening applications, and therefore the number of satellites used to derive a fix can now be retrieved properly through location.getExtras() (as shown in sample code above).  Therefore, this patch fixes Issue 4810.

Signed-off-by: Sean Barbeau <sjbarbeau@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index 00788ba..588fa93 100755

//Synthetic comment -- @@ -1077,6 +1077,7 @@
} else {
mLocation.removeAccuracy();
}

try {
mLocationManager.reportLocation(mLocation, false);







