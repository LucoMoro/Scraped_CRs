/*The decimal point in some locale is "," not "." . EX:en_ZA.

Change-Id:I254513ffa623c01aa57dbde1db0c63b21fdd89a0*/
//Synthetic comment -- diff --git a/tests/tests/location/src/android/location/cts/LocationTest.java b/tests/tests/location/src/android/location/cts/LocationTest.java
//Synthetic comment -- index 531a949..67f838f 100644

//Synthetic comment -- @@ -29,6 +29,8 @@
import android.util.Printer;
import android.util.StringBuilderPrinter;

@TestTargetClass(Location.class)
public class LocationTest extends AndroidTestCase {
private static final float DELTA = 0.1f;
//Synthetic comment -- @@ -141,6 +143,12 @@
args = {double.class, int.class}
)
public void testConvert_CoordinateToRepresentation() {
String result;

result = Location.convert(-80.0, Location.FORMAT_DEGREES);
//Synthetic comment -- @@ -181,6 +189,10 @@
} catch (IllegalArgumentException e) {
// expected.
}
}

@TestTargetNew(







