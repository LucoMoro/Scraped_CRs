/*Shortened the property name for location features to ro.com.google.enable_loc_code since the propery names may not exceed 31 characters*/
//Synthetic comment -- diff --git a/location/java/com/android/internal/location/LocationCollector.java b/location/java/com/android/internal/location/LocationCollector.java
//Synthetic comment -- index 3d2f6bf..013885a 100644

//Synthetic comment -- @@ -350,14 +350,14 @@
* is healthy.
*
* Additionally, data collection will *never* happen if the system
     * property ro.com.google.enable_google_location_features is not set.
*
* @return true if anonymous location collection is enabled
*/
private boolean isCollectionEnabled() {
// This class provides a Google-specific location feature, so it's enabled only
        // when the system property ro.com.google.enable_google_location_features is set.
        if (!SystemProperties.get("ro.com.google.enable_google_location_features").equals("1")) {
return false;
}
return mBatteryLevelIsHealthy && mNetworkProviderIsEnabled;








//Synthetic comment -- diff --git a/location/java/com/android/internal/location/NetworkLocationProvider.java b/location/java/com/android/internal/location/NetworkLocationProvider.java
//Synthetic comment -- index 7d3fda1..2632a6c 100644

//Synthetic comment -- @@ -107,8 +107,8 @@

public static boolean isSupported() {
// This class provides a Google-specific location feature, so it's enabled only
        // when the system property ro.com.google.enable_google_location_features  is set.
        if (!SystemProperties.get("ro.com.google.enable_google_location_features").equals("1")) {
return false;
}








