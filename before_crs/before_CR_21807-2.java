/*Fix LocationTest to Support More Locales

Some locales use a comma rather than a period for decimal numbers.
Make sure the test uses localized decimal strings.

Change-Id:I254513ffa623c01aa57dbde1db0c63b21fdd89a0*/
//Synthetic comment -- diff --git a/tests/tests/location/src/android/location/cts/LocationTest.java b/tests/tests/location/src/android/location/cts/LocationTest.java
//Synthetic comment -- index 531a949..2855436 100644

//Synthetic comment -- @@ -29,6 +29,8 @@
import android.util.Printer;
import android.util.StringBuilderPrinter;

@TestTargetClass(Location.class)
public class LocationTest extends AndroidTestCase {
private static final float DELTA = 0.1f;
//Synthetic comment -- @@ -141,22 +143,23 @@
args = {double.class, int.class}
)
public void testConvert_CoordinateToRepresentation() {
String result;

result = Location.convert(-80.0, Location.FORMAT_DEGREES);
        assertEquals("-80", result);

result = Location.convert(-80.085, Location.FORMAT_MINUTES);
        assertEquals("-80:5.1", result);

result = Location.convert(-80, Location.FORMAT_MINUTES);
        assertEquals("-80:0", result);

result = Location.convert(-80.075, Location.FORMAT_MINUTES);
        assertEquals("-80:4.5", result);

result = Location.convert(-80.075, Location.FORMAT_DEGREES);
        assertEquals("-80.075", result);

result = Location.convert(-80.075, Location.FORMAT_SECONDS);
assertEquals("-80:4:30", result);







