/*Updated to WMM2010

Change-Id:I5937eed4a8775eae044500552ec81030b93acb18*/




//Synthetic comment -- diff --git a/core/java/android/hardware/GeomagneticField.java b/core/java/android/hardware/GeomagneticField.java
//Synthetic comment -- index b4c04b1..96fbe77 100644

//Synthetic comment -- @@ -26,8 +26,9 @@
* <p>This uses the World Magnetic Model produced by the United States National
* Geospatial-Intelligence Agency.  More details about the model can be found at
* <a href="http://www.ngdc.noaa.gov/geomag/WMM/DoDWMM.shtml">http://www.ngdc.noaa.gov/geomag/WMM/DoDWMM.shtml</a>.
 * This class currently uses WMM-2010 which is valid until 2015, but should
 * produce acceptable results for several years after that. Future versions of
 * Android may use a newer version of the model.
*/
public class GeomagneticField {
// The magnetic field at a given point, in nonoteslas in geodetic
//Synthetic comment -- @@ -43,75 +44,73 @@

// Constants from WGS84 (the coordinate system used by GPS)
static private final float EARTH_SEMI_MAJOR_AXIS_KM = 6378.137f;
    static private final float EARTH_SEMI_MINOR_AXIS_KM = 6356.7523142f;
static private final float EARTH_REFERENCE_RADIUS_KM = 6371.2f;

// These coefficients and the formulae used below are from:
    // NOAA Technical Report: The US/UK World Magnetic Model for 2010-2015
static private final float[][] G_COEFF = new float[][] {
        { 0.0f },
        { -29496.6f, -1586.3f },
        { -2396.6f, 3026.1f, 1668.6f },
        { 1340.1f, -2326.2f, 1231.9f, 634.0f },
        { 912.6f, 808.9f, 166.7f, -357.1f, 89.4f },
        { -230.9f, 357.2f, 200.3f, -141.1f, -163.0f, -7.8f },
        { 72.8f, 68.6f, 76.0f, -141.4f, -22.8f, 13.2f, -77.9f },
        { 80.5f, -75.1f, -4.7f, 45.3f, 13.9f, 10.4f, 1.7f, 4.9f },
        { 24.4f, 8.1f, -14.5f, -5.6f, -19.3f, 11.5f, 10.9f, -14.1f, -3.7f },
        { 5.4f, 9.4f, 3.4f, -5.2f, 3.1f, -12.4f, -0.7f, 8.4f, -8.5f, -10.1f },
        { -2.0f, -6.3f, 0.9f, -1.1f, -0.2f, 2.5f, -0.3f, 2.2f, 3.1f, -1.0f, -2.8f },
        { 3.0f, -1.5f, -2.1f, 1.7f, -0.5f, 0.5f, -0.8f, 0.4f, 1.8f, 0.1f, 0.7f, 3.8f },
        { -2.2f, -0.2f, 0.3f, 1.0f, -0.6f, 0.9f, -0.1f, 0.5f, -0.4f, -0.4f, 0.2f, -0.8f, 0.0f } };

static private final float[][] H_COEFF = new float[][] {
        { 0.0f },
        { 0.0f, 4944.4f },
        { 0.0f, -2707.7f, -576.1f },
        { 0.0f, -160.2f, 251.9f, -536.6f },
        { 0.0f, 286.4f, -211.2f, 164.3f, -309.1f },
        { 0.0f, 44.6f, 188.9f, -118.2f, 0.0f, 100.9f },
        { 0.0f, -20.8f, 44.1f, 61.5f, -66.3f, 3.1f, 55.0f },
        { 0.0f, -57.9f, -21.1f, 6.5f, 24.9f, 7.0f, -27.7f, -3.3f },
        { 0.0f, 11.0f, -20.0f, 11.9f, -17.4f, 16.7f, 7.0f, -10.8f, 1.7f },
        { 0.0f, -20.5f, 11.5f, 12.8f, -7.2f, -7.4f, 8.0f, 2.1f, -6.1f, 7.0f },
        { 0.0f, 2.8f, -0.1f, 4.7f, 4.4f, -7.2f, -1.0f, -3.9f, -2.0f, -2.0f, -8.3f },
        { 0.0f, 0.2f, 1.7f, -0.6f, -1.8f, 0.9f, -0.4f, -2.5f, -1.3f, -2.1f, -1.9f, -1.8f },
        { 0.0f, -0.9f, 0.3f, 2.1f, -2.5f, 0.5f, 0.6f, 0.0f, 0.1f, 0.3f, -0.9f, -0.2f, 0.9f } };

static private final float[][] DELTA_G = new float[][] {
        { 0.0f },
        { 11.6f, 16.5f },
        { -12.1f, -4.4f, 1.9f },
        { 0.4f, -4.1f, -2.9f, -7.7f },
        { -1.8f, 2.3f, -8.7f, 4.6f, -2.1f },
        { -1.0f, 0.6f, -1.8f, -1.0f, 0.9f, 1.0f },
        { -0.2f, -0.2f, -0.1f, 2.0f, -1.7f, -0.3f, 1.7f },
        { 0.1f, -0.1f, -0.6f, 1.3f, 0.4f, 0.3f, -0.7f, 0.6f },
        { -0.1f, 0.1f, -0.6f, 0.2f, -0.2f, 0.3f, 0.3f, -0.6f, 0.2f },
        { 0.0f, -0.1f, 0.0f, 0.3f, -0.4f, -0.3f, 0.1f, -0.1f, -0.4f, -0.2f },
        { 0.0f, 0.0f, -0.1f, 0.2f, 0.0f, -0.1f, -0.2f, 0.0f, -0.1f, -0.2f, -0.2f },
        { 0.0f, 0.0f, 0.0f, 0.1f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.1f, 0.0f },
        { 0.0f, 0.0f, 0.1f, 0.1f, -0.1f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.1f, 0.1f } };

static private final float[][] DELTA_H = new float[][] {
        { 0.0f },
        { 0.0f, -25.9f },
        { 0.0f, -22.5f, -11.8f },
        { 0.0f, 7.3f, -3.9f, -2.6f },
        { 0.0f, 1.1f, 2.7f, 3.9f, -0.8f },
        { 0.0f, 0.4f, 1.8f, 1.2f, 4.0f, -0.6f },
        { 0.0f, -0.2f, -2.1f, -0.4f, -0.6f, 0.5f, 0.9f },
        { 0.0f, 0.7f, 0.3f, -0.1f, -0.1f, -0.8f, -0.3f, 0.3f },
        { 0.0f, -0.1f, 0.2f, 0.4f, 0.4f, 0.1f, -0.1f, 0.4f, 0.3f },
        { 0.0f, 0.0f, -0.2f, 0.0f, -0.1f, 0.1f, 0.0f, -0.2f, 0.3f, 0.2f },
        { 0.0f, 0.1f, -0.1f, 0.0f, -0.1f, -0.1f, 0.0f, -0.1f, -0.2f, 0.0f, -0.1f },
        { 0.0f, 0.0f, 0.1f, 0.0f, 0.1f, 0.0f, 0.1f, 0.0f, -0.1f, -0.1f, 0.0f, -0.1f },
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f } };

static private final long BASE_TIME =
        new GregorianCalendar(2010, 1, 1).getTimeInMillis();

// The ratio between the Gauss-normalized associated Legendre functions and
// the Schmid quasi-normalized ones. Compute these once staticly since they
//Synthetic comment -- @@ -191,7 +190,7 @@
// We now compute the magnetic field strength given the geocentric
// location. The magnetic field is the derivative of the potential
// function defined by the model. See NOAA Technical Report: The US/UK
        // World Magnetic Model for 2010-2015 for the derivation.
float gcX = 0.0f;  // Geocentric northwards component.
float gcY = 0.0f;  // Geocentric eastwards component.
float gcZ = 0.0f;  // Geocentric downwards component.
//Synthetic comment -- @@ -406,4 +405,4 @@
}
return schmidtQuasiNorm;
}
\ No newline at end of file
}







