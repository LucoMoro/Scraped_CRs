/*location provider fixes: test the provider without assuming presence of any particular provider on the device

Change-Id:Ibac21f9f29f8ab5583cadae8e4a6dded209ebe67*/
//Synthetic comment -- diff --git a/tests/tests/location/src/android/location/cts/LocationProviderTest.java b/tests/tests/location/src/android/location/cts/LocationProviderTest.java
//Synthetic comment -- index 3625684..9e53583 100644

//Synthetic comment -- @@ -28,6 +28,8 @@

@TestTargetClass(LocationProvider.class)
public class LocationProviderTest extends AndroidTestCase {
private LocationManager mLocationManager;

@Override
//Synthetic comment -- @@ -35,6 +37,31 @@
super.setUp();
mLocationManager =
(LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
}

@TestTargetNew(
//Synthetic comment -- @@ -43,9 +70,8 @@
args = {}
)
public void testGetName() {
        String name = "gps";
        LocationProvider locationProvider = mLocationManager.getProvider(name);
        assertEquals(name, locationProvider.getName());
}

@TestTargetNew(
//Synthetic comment -- @@ -54,7 +80,7 @@
args = {android.location.Criteria.class}
)
public void testMeetsCriteria() {
        LocationProvider locationProvider = mLocationManager.getProvider("gps");

Criteria criteria = new Criteria();
criteria.setAltitudeRequired(true);







