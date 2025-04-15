/*Updated to WMM2010

Change-Id:I5937eed4a8775eae044500552ec81030b93acb18*/
//Synthetic comment -- diff --git a/core/java/android/hardware/GeomagneticField.java b/core/java/android/hardware/GeomagneticField.java
//Synthetic comment -- index b4c04b1..96fbe77 100644

//Synthetic comment -- @@ -26,8 +26,9 @@
* <p>This uses the World Magnetic Model produced by the United States National
* Geospatial-Intelligence Agency.  More details about the model can be found at
* <a href="http://www.ngdc.noaa.gov/geomag/WMM/DoDWMM.shtml">http://www.ngdc.noaa.gov/geomag/WMM/DoDWMM.shtml</a>.
 * This class currently uses WMM-2005 which is valid until 2010, but should
 * produce acceptable results for several years after that.
*/
public class GeomagneticField {
// The magnetic field at a given point, in nonoteslas in geodetic
//Synthetic comment -- @@ -43,75 +44,73 @@

// Constants from WGS84 (the coordinate system used by GPS)
static private final float EARTH_SEMI_MAJOR_AXIS_KM = 6378.137f;
    static private final float EARTH_SEMI_MINOR_AXIS_KM = 6356.7523f;
static private final float EARTH_REFERENCE_RADIUS_KM = 6371.2f;

// These coefficients and the formulae used below are from:
    // NOAA Technical Report: The US/UK World Magnetic Model for 2005-2010
static private final float[][] G_COEFF = new float[][] {
        { 0f },
        { -29556.8f, -1671.7f },
        { -2340.6f, 3046.9f, 1657.0f },
        { 1335.4f, -2305.1f, 1246.7f, 674.0f },
        { 919.8f, 798.1f, 211.3f, -379.4f, 100.0f },
        { -227.4f, 354.6f, 208.7f, -136.5f, -168.3f, -14.1f },
        { 73.2f, 69.7f, 76.7f, -151.2f, -14.9f, 14.6f, -86.3f },
        { 80.1f, -74.5f, -1.4f, 38.5f, 12.4f, 9.5f, 5.7f, 1.8f },
        { 24.9f, 7.7f, -11.6f, -6.9f, -18.2f, 10.0f, 9.2f, -11.6f, -5.2f },
        { 5.6f, 9.9f, 3.5f, -7.0f, 5.1f, -10.8f, -1.3f, 8.8f, -6.7f, -9.1f },
        { -2.3f, -6.3f, 1.6f, -2.6f, 0.0f, 3.1f, 0.4f, 2.1f, 3.9f, -0.1f, -2.3f },
        { 2.8f, -1.6f, -1.7f, 1.7f, -0.1f, 0.1f, -0.7f, 0.7f, 1.8f, 0.0f, 1.1f, 4.1f },
        { -2.4f, -0.4f, 0.2f, 0.8f, -0.3f, 1.1f, -0.5f, 0.4f, -0.3f, -0.3f, -0.1f,
          -0.3f, -0.1f } };

static private final float[][] H_COEFF = new float[][] {
        { 0f },
        { 0.0f, 5079.8f },
        { 0.0f, -2594.7f, -516.7f },
        { 0.0f, -199.9f, 269.3f, -524.2f },
        { 0.0f, 281.5f, -226.0f, 145.8f, -304.7f },
        { 0.0f, 42.4f, 179.8f, -123.0f, -19.5f, 103.6f },
        { 0.0f, -20.3f, 54.7f, 63.6f, -63.4f, -0.1f, 50.4f },
        { 0.0f, -61.5f, -22.4f, 7.2f, 25.4f, 11.0f, -26.4f, -5.1f },
        { 0.0f, 11.2f, -21.0f, 9.6f, -19.8f, 16.1f, 7.7f, -12.9f, -0.2f },
        { 0.0f, -20.1f, 12.9f, 12.6f, -6.7f, -8.1f, 8.0f, 2.9f, -7.9f, 6.0f },
        { 0.0f, 2.4f, 0.2f, 4.4f, 4.8f, -6.5f, -1.1f, -3.4f, -0.8f, -2.3f, -7.9f },
        { 0.0f, 0.3f, 1.2f, -0.8f, -2.5f, 0.9f, -0.6f, -2.7f, -0.9f, -1.3f, -2.0f, -1.2f },
        { 0.0f, -0.4f, 0.3f, 2.4f, -2.6f, 0.6f, 0.3f, 0.0f, 0.0f, 0.3f, -0.9f, -0.4f,
          0.8f } };

static private final float[][] DELTA_G = new float[][] {
        { 0f },
        { 8.0f, 10.6f },
        { -15.1f, -7.8f, -0.8f },
        { 0.4f, -2.6f, -1.2f, -6.5f },
        { -2.5f, 2.8f, -7.0f, 6.2f, -3.8f },
        { -2.8f, 0.7f, -3.2f, -1.1f, 0.1f, -0.8f },
        { -0.7f, 0.4f, -0.3f, 2.3f, -2.1f, -0.6f, 1.4f },
        { 0.2f, -0.1f, -0.3f, 1.1f, 0.6f, 0.5f, -0.4f, 0.6f },
        { 0.1f, 0.3f, -0.4f, 0.3f, -0.3f, 0.2f, 0.4f, -0.7f, 0.4f },
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f },
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f },
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f },
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f } };

static private final float[][] DELTA_H = new float[][] {
        { 0f },
        { 0.0f, -20.9f },
        { 0.0f, -23.2f, -14.6f },
        { 0.0f, 5.0f, -7.0f, -0.6f },
        { 0.0f, 2.2f, 1.6f, 5.8f, 0.1f },
        { 0.0f, 0.0f, 1.7f, 2.1f, 4.8f, -1.1f },
        { 0.0f, -0.6f, -1.9f, -0.4f, -0.5f, -0.3f, 0.7f },
        { 0.0f, 0.6f, 0.4f, 0.2f, 0.3f, -0.8f, -0.2f, 0.1f },
        { 0.0f, -0.2f, 0.1f, 0.3f, 0.4f, 0.1f, -0.2f, 0.4f, 0.4f },
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f },
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f },
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f },
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f } };

static private final long BASE_TIME =
        new GregorianCalendar(2005, 1, 1).getTimeInMillis();

// The ratio between the Gauss-normalized associated Legendre functions and
// the Schmid quasi-normalized ones. Compute these once staticly since they
//Synthetic comment -- @@ -191,7 +190,7 @@
// We now compute the magnetic field strength given the geocentric
// location. The magnetic field is the derivative of the potential
// function defined by the model. See NOAA Technical Report: The US/UK
        // World Magnetic Model for 2005-2010 for the derivation.
float gcX = 0.0f;  // Geocentric northwards component.
float gcY = 0.0f;  // Geocentric eastwards component.
float gcZ = 0.0f;  // Geocentric downwards component.
//Synthetic comment -- @@ -406,4 +405,4 @@
}
return schmidtQuasiNorm;
}
}
\ No newline at end of file







