/*Force Close when preview a live wallpaper

After entered Live Wallpaper from Settings, repeatedly enter and
exit "Grass", FC will be seen within 1-2 minutes.
The rootcause is that updateLocation() was called after this live
wallpaper was already stopped by calling stop().
To fix, added the check to make sure mLocationUpdater != null before
calling updateLocation().

Change-Id:I7006394a8de9248a9753236fdb5a6613aa464722*/
//Synthetic comment -- diff --git a/src/com/android/wallpaper/grass/GrassRS.java b/src/com/android/wallpaper/grass/GrassRS.java
//Synthetic comment -- index b665aa2..b3c2e20 100644

//Synthetic comment -- @@ -356,7 +356,7 @@

private class LocationUpdater implements LocationListener {
public void onLocationChanged(Location location) {
            updateLocation(location);
}

public void onStatusChanged(String provider, int status, Bundle extras) {







