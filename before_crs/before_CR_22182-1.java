/*Fix CTS Geocoder fail when there's no GMS

CTS GeocoderTest#testGetFromLocation,testGetFromLocationName fail
if the ROM doesn't contain GMS network location. Handle the exception
in Geocoder.java when there are no network location service.

Change-Id:I1c546aa85de5a21e123d13c31fedc0c90dea6fac*/
//Synthetic comment -- diff --git a/location/java/android/location/Geocoder.java b/location/java/android/location/Geocoder.java
//Synthetic comment -- index ac7eb8b..69b4699 100644

//Synthetic comment -- @@ -133,7 +133,12 @@
String ex =  mService.getFromLocation(latitude, longitude, maxResults,
mParams, results);
if (ex != null) {
                throw new IOException(ex);
} else {
return results;
}
//Synthetic comment -- @@ -175,7 +180,12 @@
String ex = mService.getFromLocationName(locationName,
0, 0, 0, 0, maxResults, mParams, results);
if (ex != null) {
                throw new IOException(ex);
} else {
return results;
}







