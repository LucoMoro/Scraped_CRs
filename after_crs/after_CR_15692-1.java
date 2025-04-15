/*Update GeomagneticFieldTest

Bug 2557378

Squashed commit of the following:

commit 24b1575f9bf6898257cdec9deb2db9ea5c8dbb52
Date:   Tue Mar 30 18:37:29 2010 -0300

    Fixing test

    Change-Id:I6724a1020c9eb0f25b64b31571f5d57f0b576fcfChange-Id:I8ecb4383b164ed6e6ea186e64df21b1d103e0ffd*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/GeomagneticFieldTest.java b/tests/tests/hardware/src/android/hardware/cts/GeomagneticFieldTest.java
//Synthetic comment -- index e08da0e..d98d2d5 100644

//Synthetic comment -- @@ -28,11 +28,11 @@

@TestTargetClass(GeomagneticField.class)
public class GeomagneticFieldTest extends AndroidTestCase {
    // Chengdu: Latitude 30d 40' 12", Longitude 104d 3' 36"
    private static final float LATITUDE_OF_CHENGDU = 30.67f;
    private static final float LONGITUDE_OF_CHENGDU = 104.06f;
private static final float ALTITUDE_OF_CHENGDU = 500f;
    private static final long TEST_TIME = new GregorianCalendar(2010, 5, 1).getTimeInMillis();

@TestTargets({
@TestTargetNew(
//Synthetic comment -- @@ -79,12 +79,16 @@
public void testGeomagneticField() {
GeomagneticField geomagneticField = new GeomagneticField(LATITUDE_OF_CHENGDU,
LONGITUDE_OF_CHENGDU, ALTITUDE_OF_CHENGDU, TEST_TIME);

        // Reference values calculated from NOAA online calculator for WMM 2010,
        // and cross-checked in Matlab. The expected deltas are proportional to the
        // magnitude of each value.
        assertEquals(-1.867f, geomagneticField.getDeclination(), 0.1f);
        assertEquals(47.133f, geomagneticField.getInclination(), 1.0f);
        assertEquals(50375.6f, geomagneticField.getFieldStrength(), 100.0f);
        assertEquals(34269.3f, geomagneticField.getHorizontalStrength(), 100.0f);
        assertEquals(34251.2f, geomagneticField.getX(), 100.0f);
        assertEquals(-1113.2f, geomagneticField.getY(), 10.0f);
        assertEquals(36923.1f, geomagneticField.getZ(), 100.0f);
}
}







